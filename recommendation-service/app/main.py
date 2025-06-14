from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.responses import JSONResponse
from pydantic import BaseModel # Import BaseModel for request body validation
from app.services.recommendation_service import RecommendationService
from app.db import Neo4jConnection
from typing import List, Dict, Any
import logging

# Configure logging
logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

class FriendshipRequest(BaseModel): 
    """Model for friendship request data validation."""
    user_id1: str
    user_id2: str

class DeleteFriendshipRequest(BaseModel): 
    """Model for friendship request data validation."""
    user_id1: str
    user_id2: str
    
class UserRegistrationRequest(BaseModel): 
    """Model for user registration data validation."""
    user_id: str
    name: str
app = FastAPI(
    title="Friend Recommendation Microservice",
    description="Provides friend recommendations based on user relationships and other criteria.",
    version="1.0.0"
)

# Dependency to get RecommendationService instance
def get_recommendation_service():
    """Provides a RecommendationService instance with a Neo4j graph connection."""
    try:
        # This will initialize the graph connection if it's not already
        # or return the existing one.
        service = RecommendationService()
        return service
    except ConnectionError as e:
        logger.critical(f"Failed to initialize RecommendationService due to DB connection error: {e}")
        raise HTTPException(status_code=status.HTTP_503_SERVICE_UNAVAILABLE, detail=f"Database connection error: {e}")
    except Exception as e:
        logger.critical(f"An unexpected error occurred during service initialization: {e}")
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=f"Internal server error during service initialization: {e}")


# Application startup and shutdown events to manage Neo4j connection lifecycle
# This ensures that the db connection is established when the app starts 
@app.on_event("startup")
async def startup_event():
    logger.info("Application startup: Attempting to connect to Neo4j...")
    try:
        # Explicitly try to get a graph connection at startup
        Neo4jConnection.get_graph()
        logger.info("Successfully connected to Neo4j during startup.")
    except Exception as e:
        logger.error(f"Failed to connect to Neo4j at startup: {e}")
        # Depending on your deployment strategy, you might want to exit here
        # or just log and let subsequent requests fail gracefully.
        # For this skeleton, we'll log and let `get_recommendation_service` handle it.

# This ensures that the Neo4j connection is closed gracefully when the app shuts down 
@app.on_event("shutdown")
async def shutdown_event():
    logger.info("Application shutdown: Closing Neo4j connection...")
    Neo4jConnection.close_graph()
    logger.info("Neo4j connection closed.")


@app.get("/")
async def read_root():
    return {"message": "Friend Recommendation Service is running!"}

@app.get("/recommendations/{user_id}", response_model=List[Dict[str, Any]])
async def get_friend_recommendations(
    user_id: str,
    limit: int = 10,
    service: RecommendationService = Depends(get_recommendation_service)
):
    """
    Retrieves a list of recommended friends for a given user.
    """
    if not user_id:
        raise HTTPException(status_code=400, detail="User ID cannot be empty.")

    recommendations = service.get_recommended_friends(user_id, limit)
    if not recommendations:
        logger.info(f"No recommendations found for user {user_id}.")
        return JSONResponse(status_code=200, content={"message": "No recommendations found.", "recommendations": []})
    return recommendations

@app.post("/register-user", status_code=status.HTTP_201_CREATED)
async def register_user(
    user_id: str, 
    name: str, 
    service: RecommendationService = Depends(get_recommendation_service)
): 
    """Registers a new user in the recommendation system. 

    Args:
        user_id (str): _description_
        name (str): _description_
        service (RecommendationService, optional): _description_. Defaults to Depends(get_recommendation_service).

    Raises:
        HTTPException: _description_
        HTTPException: _description_
        HTTPException: _description_
        HTTPException: _description_

    Returns:
        _type_: _description_
    """
    if not user_id or not name: 
        raise HTTPException(status_code=400, detail="User ID and name are required.")
    
    success = service.add_user(user_id, name)
    if success:
        return {"message": "User registered successfully.", "user_id": user_id, "name": name}
    else:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"User with ID {user_id} already exists."
        )
        
@app.post("/friends", status_code=status.HTTP_201_CREATED)
async def add_friendship(
    request: FriendshipRequest,
    service: RecommendationService = Depends(get_recommendation_service)
):
    """
    Adds a new bi-directional friendship between two users.
    This event triggers potential recalculations for recommendations.
    """
    user_id1 = request.user_id1
    user_id2 = request.user_id2

    if not user_id1 or not user_id2:
        raise HTTPException(status_code=400, detail="Both user IDs are required.")
    if user_id1 == user_id2:
        raise HTTPException(status_code=400, detail="A user cannot be friends with themselves.")

    try:
        success = service.add_friendship(user_id1, user_id2)
        if success:
            logger.info(f"Friendship added between {user_id1} and {user_id2}. Triggering recommendation recalculation if needed.")
            # In a real-world scenario, you might trigger an asynchronous task here
            # to re-evaluate recommendations for user_id1 and user_id2.
            # For now, it's a conceptual "trigger".
            return {"message": "Friendship added successfully.", "user_id1": user_id1, "user_id2": user_id2}
    except Exception as e:
        logger.error(f"Error adding friendship between {user_id1} and {user_id2}: {e}")
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to add friendship: {e}"
        )

@app.delete("/friends", status_code=status.HTTP_201_CREATED)
async def remove_friendship(
    request: DeleteFriendshipRequest,
    service: RecommendationService = Depends(get_recommendation_service)
):
    """
    Deletes friendship between two users.
    This event triggers potential recalculations for recommendations.
    """
    user_id1 = request.user_id1
    user_id2 = request.user_id2

    if not user_id1 or not user_id2:
        raise HTTPException(status_code=400, detail="Both user IDs are required.")
    if user_id1 == user_id2:
        raise HTTPException(status_code=400, detail="A user cannot be friends with themselves.")

    try:
        success = service.remove_friendship(user_id1, user_id2)
        if success:
            logger.info(f"Friendship deleted between {user_id1} and {user_id2}. Triggering recommendation recalculation if needed.")
            return {"message": "Friendship deleted successfully.", "user_id1": user_id1, "user_id2": user_id2}
    except Exception as e:
        logger.error(f"Error deleting friendship between {user_id1} and {user_id2}: {e}")
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to delete friendship: {e}"
        )


# You can add more endpoints here as new recommendation algorithms are developed.