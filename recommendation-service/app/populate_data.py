import os
from py2neo import Graph, Node, Relationship
from dotenv import load_dotenv
import logging
import sys

# Configure logging
logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(levelname)s - %(message)s',
                    stream=sys.stdout) # Ensure logs go to stdout/console
logger = logging.getLogger(__name__)

# Load environment variables from .env file
load_dotenv()

# --- Configuration ---
NEO4J_URI = os.getenv("NEO4J_URI", "bolt://recommendation-db:7687")
NEO4J_USER = os.getenv("NEO4J_USER", "neo4j")
NEO4J_PASSWORD = os.getenv("NEO4J_PASSWORD", "neo4jpassword")

# Sample Data
USERS_DATA = [
    {"id": "user1", "name": "Alice"},
    {"id": "user2", "name": "Bob"},
    {"id": "user3", "name": "Charlie"},
    {"id": "user4", "name": "David"},
    {"id": "user5", "name": "Eve"},
    {"id": "user6", "name": "Frank"},
    {"id": "user7", "name": "Grace"},
    {"id": "user8", "name": "Heidi"},
    {"id": "user9", "name": "Ivan"},
    {"id": "user10", "name": "Judy"},
]

FRIENDSHIPS_DATA = [
    ("user1", "user2"),
    ("user2", "user3"),
    ("user2", "user4"),
    ("user4", "user5"),
    ("user3", "user6"),
    ("user5", "user6"),
    ("user1", "user4"), # Alice and David are direct friends
    ("user7", "user8"),
    ("user7", "user1"), # Grace is friends with Alice
    ("user8", "user9"),
    ("user9", "user10"),
    ("user10", "user2"), # Judy is friends with Bob
]

INTERESTS_DATA = [
    "Reading", "Hiking", "Cooking", "Gaming", "Photography", "Music", "Sports", "Art"
]

USER_INTERESTS_DATA = {
    "user1": ["Reading", "Hiking"],
    "user2": ["Reading", "Music"],
    "user3": ["Hiking", "Sports"],
    "user4": ["Gaming", "Photography"],
    "user5": ["Gaming", "Cooking"],
    "user6": ["Reading", "Cooking"],
    "user7": ["Photography", "Art"],
    "user8": ["Hiking", "Music"],
    "user9": ["Sports", "Gaming"],
    "user10": ["Reading", "Art"],
    "user1": ["Music"], # Alice also likes music
    "user2": ["Cooking"], # Bob also likes cooking
    "user3": ["Music"],
    "user4": ["Cooking"],
    "user5": ["Reading"],
    "user6": ["Gaming"]
}

def create_indexes(graph):
    """Creates necessary indexes for efficient lookups."""
    logger.info("Creating indexes if they don't exist...")
    graph.run("CREATE CONSTRAINT ON (u:User) ASSERT u.id IS UNIQUE")
    graph.run("CREATE CONSTRAINT ON (i:Interest) ASSERT i.name IS UNIQUE")
    logger.info("Indexes created/ensured.")

def populate_users(graph):
    """Creates User nodes."""
    logger.info("Populating Users...")
    for user_data in USERS_DATA:
        user_node = Node("User", id=user_data["id"], name=user_data["name"])
        try:
            # Merge ensures creation if not exists, and updates if it does
            graph.merge(user_node, "User", "id")
            logger.info(f"Merged User: {user_data['name']} (ID: {user_data['id']})")
        except Exception as e:
            logger.error(f"Error merging user {user_data['id']}: {e}")
    logger.info("Users population complete.")

def populate_friendships(graph):
    """Creates FRIENDS_WITH relationships."""
    logger.info("Populating Friendships...")
    for user_id1, user_id2 in FRIENDSHIPS_DATA:
        try:
            # Find or create nodes, then create relationship
            tx = graph.begin()
            user1 = tx.nodes.match("User", id=user_id1).first()
            user2 = tx.nodes.match("User", id=user_id2).first()

            if user1 and user2:
                # Use MERGE to ensure relationship is created only if it doesn't exist
                tx.merge(Relationship(user1, "FRIENDS_WITH", user2), "FRIENDS_WITH", {})
                tx.merge(Relationship(user2, "FRIENDS_WITH", user1), "FRIENDS_WITH", {}) # Ensure bi-directional
                tx.commit()
                logger.info(f"Created/Ensured friendship between {user_id1} and {user_id2}")
            else:
                logger.warning(f"Skipping friendship: User {user_id1} or {user_id2} not found.")
        except Exception as e:
            logger.error(f"Error creating friendship between {user_id1} and {user_id2}: {e}")
    logger.info("Friendships population complete.")

def populate_interests(graph):
    """Creates Interest nodes."""
    logger.info("Populating Interests...")
    for interest_name in INTERESTS_DATA:
        interest_node = Node("Interest", name=interest_name)
        try:
            graph.merge(interest_node, "Interest", "name")
            logger.info(f"Merged Interest: {interest_name}")
        except Exception as e:
            logger.error(f"Error merging interest {interest_name}: {e}")
    logger.info("Interests population complete.")

def populate_user_interests(graph):
    """Creates HAS_INTEREST relationships."""
    logger.info("Populating User Interests...")
    for user_id, interests in USER_INTERESTS_DATA.items():
        user_node = graph.nodes.match("User", id=user_id).first()
        if not user_node:
            logger.warning(f"Skipping interests for user {user_id}: User not found.")
            continue

        for interest_name in interests:
            interest_node = graph.nodes.match("Interest", name=interest_name).first()
            if interest_node:
                try:
                    tx = graph.begin()
                    # Ensure HAS_INTEREST relationship exists
                    tx.merge(Relationship(user_node, "HAS_INTEREST", interest_node), "HAS_INTEREST", {})
                    tx.commit()
                    logger.info(f"Assigned interest '{interest_name}' to user {user_id}")
                except Exception as e:
                    logger.error(f"Error assigning interest {interest_name} to user {user_id}: {e}")
            else:
                logger.warning(f"Skipping interest '{interest_name}' for user {user_id}: Interest not found.")
    logger.info("User Interests population complete.")

def clear_all_data(graph):
    """Deletes all nodes and relationships."""
    logger.warning("Clearing all existing data from Neo4j...")
    try:
        graph.run("MATCH (n) DETACH DELETE n")
        logger.info("All data cleared successfully.")
    except Exception as e:
        logger.error(f"Error clearing data: {e}")

def main():
    try:
        logger.info("Attempting to connect to Neo4j...")
        graph = Graph(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))
        graph.run("RETURN 1").evaluate() # Test connection
        logger.info("Successfully connected to Neo4j.")

        # Option to clear existing data before populating
        # clear_all_data(graph)

        create_indexes(graph)
        populate_users(graph)
        populate_friendships(graph)
        populate_interests(graph)
        populate_user_interests(graph)

        logger.info("Data population script finished.")

    except Exception as e:
        logger.critical(f"Failed to connect or populate data: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()