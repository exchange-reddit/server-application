import os
from dotenv import load_dotenv

load_dotenv()

class Settings:
    NEO4J_URI: str = os.getenv("RECOMMENDATION_DB_URL", "bolt://recommendation-db:7687")
    NEO4J_USER: str = os.getenv("RECOMMENDATION_DB_USERNAME", "neo4j")
    NEO4J_PASSWORD: str = os.getenv("RECOMMENDATION_DB_PASSWORD", "neo4jpassword")

settings = Settings()