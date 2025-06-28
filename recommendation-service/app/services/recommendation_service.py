from app.db import Neo4jConnection
from py2neo import Node
from typing import List, Dict, Any
import logging

logger = logging.getLogger(__name__)

class RecommendationService:
    def __init__(self):
        self.graph = Neo4jConnection.get_graph()
    
    def add_user(self, user_data: dict) -> bool:
        """
        Adds a user node to the graph database if it does not already exist,
        based on the structure from User.java.
        Returns True if the user was created, False if the user already exists or an error occurs.
        """
        user_id = user_data.get("userId")
        if not user_id:
            logger.error("Registration failed: 'userId' is missing from user data.")
            return False

        logger.info(f"Attempting to register user {user_id}")
        
        try:
            # Check if user already exists using direct graph query
            existing_user = self.graph.nodes.match("User", userId=user_id).first()
            if existing_user:
                logger.warning(f"Registration failed: User with ID {user_id} already exists.")
                return False
            
            # Create new user node
            user = Node("User", **user_data)
            self.graph.create(user)
            logger.info(f"User {user_id} registered successfully.")
            return True
        except Exception as e:
            logger.error(f"An error occurred during user registration for {user_id}: {e}")
            return False
        
    def add_follow_relationship(self, follower_id: str, followed_id: str) -> bool:
        """
        Adds a directional FOLLOWS relationship from a follower to a followed user.
        """
        logger.info(f"Attempting to add follow relationship from {follower_id} to {followed_id}")
        
        try:
            # Query that validates users exist and creates relationship
            query = """
            MATCH (follower:User {userId: $follower_id})
            MATCH (followed:User {userId: $followed_id})
            MERGE (follower)-[:FOLLOWS]->(followed)
            RETURN follower.userId AS follower_userId, followed.userId AS followed_userId
            """
            
            result = self.graph.run(query, follower_id=follower_id, followed_id=followed_id)
            data = result.data()
            
            if data:
                logger.info(f"Follow relationship created successfully from {follower_id} to {followed_id}")
                return True
            else:
                # If no data returned, it means one or both users don't exist
                # Check which user is missing for better error logging
                follower_exists = self.graph.nodes.match("User", userId=follower_id).first() is not None
                followed_exists = self.graph.nodes.match("User", userId=followed_id).first() is not None
                
                if not follower_exists and not followed_exists:
                    logger.warning(f"Both users not found: {follower_id} and {followed_id}")
                elif not follower_exists:
                    logger.warning(f"Follower user {follower_id} not found.")
                elif not followed_exists:
                    logger.warning(f"Followed user {followed_id} not found.")
                
                return False
                
        except Exception as e:
            logger.error(f"Error creating follow relationship from {follower_id} to {followed_id}: {e}")
            return False

    def remove_follow_relationship(self, follower_id: str, followed_id: str) -> bool:
        """
        Removes a directional FOLLOWS relationship from a follower to a followed user.
        """
        logger.info(f"Attempting to remove follow relationship from {follower_id} to {followed_id}")
        try:
            query = """
            MATCH (follower:User {userId: $follower_id})-[r:FOLLOWS]->(followed:User {userId: $followed_id})
            DELETE r
            RETURN count(r) AS relationshipsDeleted
            """
            result = self.graph.run(query, follower_id=follower_id, followed_id=followed_id).evaluate()
            
            if result > 0:
                logger.info(f"Follow relationship removed from {follower_id} to {followed_id}.")
                return True
            else:
                logger.info(f"No follow relationship found to remove from {follower_id} to {followed_id}.")
                return False
        except Exception as e:
            logger.error(f"Error removing follow relationship from {follower_id} to {followed_id}: {e}")
            return False
    
    def get_recommended_friends(self, user_id: str, limit: int = 10) -> List[Dict[str, Any]]:
        """
        Calculates a recommendation score based on User.java entity fields and returns a sorted list.
        """
        query = """
        MATCH (target:User {userId: $userId})
        MATCH (other:User)
        WHERE target <> other

        // Calculate score components
        WITH target, other,
             CASE WHEN target.homeUni IS NOT NULL AND target.homeUni = other.homeUni THEN 10 ELSE 0 END AS homeUniScore,
             CASE WHEN target.exchangeUni IS NOT NULL AND target.exchangeUni = other.exchangeUni THEN 10000 ELSE 0 END AS exchangeUniScore,
             CASE WHEN target.nationality IS NOT NULL AND target.nationality = other.nationality THEN 10 ELSE 0 END AS nationalityScore,
             CASE WHEN target.preferredLanguage IS NOT NULL AND target.preferredLanguage = other.preferredLanguage THEN 30 ELSE 0 END AS languageScore

        // Calculate total score
        WITH other, (homeUniScore + exchangeUniScore + nationalityScore + languageScore) AS totalScore
        ORDER BY totalScore DESC
        LIMIT $limit

        RETURN other.userId AS userId, other.name AS name, totalScore AS score
        """
        logger.info(f"Executing recommendation query for user {user_id}")
        try:
            result = self.graph.run(query, userId=user_id, limit=limit)
            recommendations = [record for record in result.data()]
            logger.info(f"Found {len(recommendations)} recommendations for user {user_id}.")
            return recommendations
        except Exception as e:
            logger.error(f"Error getting recommendations for user {user_id}: {e}")
            return []
