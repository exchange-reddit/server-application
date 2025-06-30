import os
from dotenv import load_dotenv

load_dotenv()

class Settings:
    NEO4J_URI: str = os.getenv("RECOMMENDATION_DB_URL", "bolt://recommendation-db:7687")
    NEO4J_USER: str = os.getenv("RECOMMENDATION_DB_USERNAME", "neo4j")
    NEO4J_PASSWORD: str = os.getenv("RECOMMENDATION_DB_PASSWORD", "neo4jpassword")

settings = Settings()


# Recommendation Service Configuration
# This section contains all configurable parameters for the recommendation scoring algorithm

class RecommendationConfig:
    """
    Configuration class for recommendation scoring parameters.
    Modify these values to adjust the recommendation algorithm behavior.
    """
    
    # Main scoring weights vector
    # Order: [is_same_home_uni, is_same_exchange_uni, similarity_preferred_language, 
    #         is_same_department, is_same_nationality, is_same_gender, is_follower]
    SCORE_WEIGHTS = [10, 10000, 30, 5, 10, 0, 20]
    
    # Language preference weights (1st, 2nd, 3rd priority)
    LANGUAGE_PRIORITY_WEIGHTS = [0.5, 0.3, 0.2]
    
    # Individual score component names (for debugging and logging)
    SCORE_COMPONENT_NAMES = [
        "home_uni_score",
        "exchange_uni_score", 
        "language_similarity_score",
        "department_score",
        "nationality_score",
        "gender_score",
        "follower_bonus_score"
    ]
    
    @classmethod
    def get_score_weights(cls):
        """Returns the current score weights vector."""
        return cls.SCORE_WEIGHTS
    
    @classmethod
    def get_language_weights(cls):
        """Returns the language priority weights."""
        return cls.LANGUAGE_PRIORITY_WEIGHTS
    
    @classmethod
    def update_score_weights(cls, new_weights):
        """Update the score weights vector. Must be a list of 7 numbers."""
        if len(new_weights) != 7:
            raise ValueError("Score weights must contain exactly 7 values")
        cls.SCORE_WEIGHTS = new_weights
    
    @classmethod
    def update_language_weights(cls, new_weights):
        """Update the language priority weights. Must be a list of 3 numbers."""
        if len(new_weights) != 3:
            raise ValueError("Language weights must contain exactly 3 values")
        cls.LANGUAGE_PRIORITY_WEIGHTS = new_weights