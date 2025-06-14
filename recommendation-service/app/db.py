from py2neo import Graph, RelationshipMatcher
from app.config import settings
import logging

logger = logging.getLogger(__name__)

class Neo4jConnection:
    _graph = None

    @classmethod
    def get_graph(cls):
        if cls._graph is None:
            try:
                logger.info("Connecting to Neo4j with URI: %s", settings.NEO4J_URI)
                logger.info("Connecting to Neo4j with username: %s", settings.NEO4J_USER) 
                logger.info("Connecting to Neo4j with password: %s", settings.NEO4J_PASSWORD)
                cls._graph = Graph(settings.NEO4J_URI, auth=(settings.NEO4J_USER, settings.NEO4J_PASSWORD))
                # Test connection
                cls._graph.run("RETURN 1").evaluate()
                logger.info("Successfully connected to Neo4j.")
            except Exception as e:
                logger.error(f"Failed to connect to Neo4j: {e}")
                cls._graph = None # Ensure _graph is None if connection fails
                raise ConnectionError(f"Could not connect to Neo4j: {e}")
        return cls._graph

    @classmethod
    def close_graph(cls):
        # In py2neo, there's no explicit close method for the Graph object
        # The connection is managed internally. However, for a clean shutdown
        # or if connection pooling needs explicit reset, you might clear it.
        # For simple cases, just setting _graph to None is fine.
        cls._graph = None
        logger.info("Neo4j graph object reset.")

# Initialize graph connection on startup (FastAPI's lifespan events)
# or when `get_graph` is first called.
# For simplicity, we'll rely on `get_graph` being called when needed.