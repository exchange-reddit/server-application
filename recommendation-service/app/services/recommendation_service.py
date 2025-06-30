from app.db import Neo4jConnection
from py2neo import Node
from app.config import RecommendationConfig
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
        
    def remove_user(self, user_id: str) -> bool:
        """
        Removes a user and all their relationships from the graph database.
        Returns True if the user was successfully deleted, False if user not found or error occurs.
        """
        if not user_id:
            logger.error("User removal failed: 'user_id' is missing.")
            return False

        logger.info(f"Attempting to remove user {user_id}")
        
        try:
            # Check if user exists first
            existing_user = self.graph.nodes.match("User", userId=user_id).first()
            if not existing_user:
                logger.warning(f"User removal failed: User with ID {user_id} does not exist.")
                return False
            
            # Delete user and all their relationships using DETACH DELETE
            query = """
            MATCH (user:User {userId: $user_id})
            DETACH DELETE user
            RETURN count(user) AS deletedCount
            """
            
            result = self.graph.run(query, user_id=user_id).evaluate()
            
            if result > 0:
                logger.info(f"User {user_id} and all their relationships deleted successfully.")
                return True
            else:
                logger.error(f"Failed to delete user {user_id}.")
                return False
                
        except Exception as e:
            logger.error(f"An error occurred during user removal for {user_id}: {e}")
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
        Excludes the target user and users already being followed by the target user.
        Uses configurable scoring weights and supports 3-tier language preferences.
        """
        # Get configuration values
        weights = RecommendationConfig.get_score_weights()
        lang_weights = RecommendationConfig.get_language_weights()
        
        query = f"""
        MATCH (target:User {{userId: $userId}})
        MATCH (other:User)
        WHERE target <> other
        AND NOT (target)-[:FOLLOWS]->(other)

        // Calculate individual score components
        WITH target, other,
             // Home university match
             CASE WHEN target.homeUni IS NOT NULL AND target.homeUni = other.homeUni THEN 1 ELSE 0 END AS homeUniMatch,
             
             // Exchange university match  
             CASE WHEN target.exchangeUni IS NOT NULL AND target.exchangeUni = other.exchangeUni THEN 1 ELSE 0 END AS exchangeUniMatch,
             
             // Department match
             CASE WHEN target.department IS NOT NULL AND target.department = other.department THEN 1 ELSE 0 END AS departmentMatch,
             
             // Nationality match
             CASE WHEN target.nationality IS NOT NULL AND target.nationality = other.nationality THEN 1 ELSE 0 END AS nationalityMatch,
             
             // Gender match
             CASE WHEN target.gender IS NOT NULL AND target.gender = other.gender THEN 1 ELSE 0 END AS genderMatch,
             
             // Follower bonus
             CASE WHEN (other)-[:FOLLOWS]->(target) THEN 1 ELSE 0 END AS followerMatch,
             
             // Complex language similarity calculation
             (
                 {lang_weights[0]} * (
                     CASE WHEN target.preferredLanguage1 IS NOT NULL AND target.preferredLanguage1 = other.preferredLanguage1 THEN 1.0
                          WHEN target.preferredLanguage1 IS NOT NULL AND target.preferredLanguage1 = other.preferredLanguage2 THEN 1.0/2.0
                          WHEN target.preferredLanguage1 IS NOT NULL AND target.preferredLanguage1 = other.preferredLanguage3 THEN 1.0/3.0
                          ELSE 0 END
                 ) +
                 {lang_weights[1]} * (
                     CASE WHEN target.preferredLanguage2 IS NOT NULL AND target.preferredLanguage2 = other.preferredLanguage1 THEN 1.0
                          WHEN target.preferredLanguage2 IS NOT NULL AND target.preferredLanguage2 = other.preferredLanguage2 THEN 1.0/2.0
                          WHEN target.preferredLanguage2 IS NOT NULL AND target.preferredLanguage2 = other.preferredLanguage3 THEN 1.0/3.0
                          ELSE 0 END
                 ) +
                 {lang_weights[2]} * (
                     CASE WHEN target.preferredLanguage3 IS NOT NULL AND target.preferredLanguage3 = other.preferredLanguage1 THEN 1.0
                          WHEN target.preferredLanguage3 IS NOT NULL AND target.preferredLanguage3 = other.preferredLanguage2 THEN 1.0/2.0
                          WHEN target.preferredLanguage3 IS NOT NULL AND target.preferredLanguage3 = other.preferredLanguage3 THEN 1.0/3.0
                          ELSE 0 END
                 )
             ) AS languageSimilarity

        // Calculate final weighted score using dot product
        WITH other, 
             (homeUniMatch * {weights[0]} + 
              exchangeUniMatch * {weights[1]} + 
              languageSimilarity * {weights[2]} + 
              departmentMatch * {weights[3]} + 
              nationalityMatch * {weights[4]} + 
              genderMatch * {weights[5]} + 
              followerMatch * {weights[6]}) AS totalScore,
             homeUniMatch, exchangeUniMatch, languageSimilarity, 
             departmentMatch, nationalityMatch, genderMatch, followerMatch
        
        WHERE totalScore > 0
        ORDER BY totalScore DESC
        LIMIT $limit

        RETURN other.userId AS userId, other.name AS name, totalScore AS score,
               homeUniMatch, exchangeUniMatch, languageSimilarity, 
               departmentMatch, nationalityMatch, genderMatch, followerMatch
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
