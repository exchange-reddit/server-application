from app.db import Neo4jConnection
from py2neo import Node, Relationship
from typing import List, Dict, Any
import logging

logger = logging.getLogger(__name__)

class RecommendationService:
    def __init__(self):
        self.graph = Neo4jConnection.get_graph()
    
    def add_user(self, user_id: str, name: str) -> bool: 
        """
        Adds a user node to the graph database.
        Returns True if successful, False if user already exists or an error occurs. 

        Args:
            user_id (str): _user identifier_
            name (str): _user name_

        Returns:
            bool: _True if user added successfully, False otherwise_
        """
        logger.info(f"attempting to add user {user_id} with name {name}")
        try: 
            # Create a new user node with the given ID and name
            user = Node("User", id=user_id, name=name)
            self.graph.create(user)
            logger.info(f"User {user_id} added successfully.")
            return True
        except Exception as e:
            # If an error occurs, log it and return False
            logger.error(f"Error adding user {user_id}: {e}")
            return False
        
    def add_friendship(self, user_id1: str, user_id2: str) -> bool:
        """
        Adds a bi-directional FRIENDS_WITH relationship between two users.
        Returns True if successful, False if users not found.
        Args:
            user_id1 (str): _first user identifier_
            user_id2 (str): _second user identifier_

        Returns:
            bool: _True if friendship added successfully, False otherwise_ 
        """
        # Graph db transaction begins
        tx = self.graph.begin()
        
        # Find users by their IDs 
        user1 = self.graph.nodes.match("User", id=user_id1).first()
        user2 = self.graph.nodes.match("User", id=user_id2).first()
        

        # If either user does not exist, rollback the transaction 
        if not user1 or not user2:
            tx.rollback() 
            logger.warning(f"Attempted to add friendship between non-existent users: {user_id1}, {user_id2}")
            return False
        
        logger.info(f"Found users {user_id1} and {user_id2}, proceeding to add friendship.")
        try:
            # Use MERGE to create the relationship only if it doesn't already exist
            FRIENDS_WITH = Relationship.type("FRIENDS_WITH")
            tx.merge(FRIENDS_WITH(user1, user2))
            
            # Commit the transaction
            tx.commit()
            logger.info(f"Friendship MERGED successfully between {user_id1} and {user_id2}")
            return True
        except Exception as e:
            # If an error occurs, rollback the transaction
            tx.rollback()
            logger.error(f"Error merging friendship between {user_id1} and {user_id2}: {e}")
            return False

    def remove_friendship(self, user_id1: str, user_id2: str) -> bool:
        """
        Removes a bi-directional FRIENDS_WITH relationship between two users.
        """
        logger.info(f"Attempting to remove friendship between {user_id1} and {user_id2}")
        try:
            # Query to find and delete the FRIENDS_WITH relationship
            # It uses MATCH to find the relationship and DELETE to remove it
            # Returns the count of relationships deleted
            query = f"""
            MATCH (u1:User {{id: '{user_id1}'}})-[r:FRIENDS_WITH]-(u2:User {{id: '{user_id2}'}})
            DELETE r
            RETURN count(r) AS relationshipsDeleted
            """
            result = self.graph.run(query).evaluate()
            
            # If the deleted relationships count is greater than 0, the friendship was removed
            if result > 0:
                logger.info(f"Friendship removed between {user_id1} and {user_id2}. Relationships deleted: {result}")
                return True
            # If no relationships were deleted, it means there was no friendship to remove
            else:
                logger.info(f"No friendship found to remove between {user_id1} and {user_id2}.")
                return False
        except Exception as e:
            logger.error(f"Error removing friendship between {user_id1} and {user_id2}: {e}")
            return False
    
    def get_recommended_friends(self, user_id: str, limit: int = 10) -> List[Dict[str, Any]]:
        """
        TODO:
        Placeholder for the friend recommendation algorithm.
        The algorithm team will implement the logic here.

        """
        query = f"""
        """
        logger.info(f"Executing recommendation query for user {user_id}: {query}")
        try:
            result = self.graph.run(query)
            recommendations = [record for record in result.data()]
            logger.info(f"Found {len(recommendations)} recommendations for user {user_id}.")
            return recommendations
        except Exception as e:
            logger.error(f"Error getting recommendations for user {user_id}: {e}")
            return []