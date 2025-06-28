from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.responses import JSONResponse
from pydantic import BaseModel, Field # Import BaseModel for request body validation
from app.services.recommendation_service import RecommendationService
from app.db import Neo4jConnection
from typing import List, Dict, Any, Optional
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

class FollowRequest(BaseModel):
    follower_id: str
    followed_id: str

# Based on User.java entity
class UserEntity(BaseModel):
    userId: str = Field(..., alias="userId")
    name: str
    homeUni: Optional[str] = None
    exchangeUni: Optional[str] = None
    nationality: Optional[str] = None
    preferredLanguage: Optional[str] = None
    # Add other fields as needed, keeping them optional
    userType: Optional[str] = None
    privateEmail: Optional[str] = None
    homeEmail: Optional[str] = None
    
    class Config:
        allow_population_by_field_name = True

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

# http://Omniversity/server/recommendation-service/recommendations/user_id
# http method

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
    user_data: UserEntity, 
    service: RecommendationService = Depends(get_recommendation_service)
): 
    """
    Registers a new user based on the User.java entity structure.
    """
    # Pydantic model is converted to dict and passed to the service
    success = service.add_user(user_data.dict(by_alias=True))
    if success:
        return {"message": "User registered successfully.", "userId": user_data.userId}
    else:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"User with ID {user_data.userId} already exists or registration failed."
        )
        
@app.post("/follows", status_code=status.HTTP_201_CREATED)
async def add_follow(
    request: FollowRequest,
    service: RecommendationService = Depends(get_recommendation_service)
):
    """
    Adds a new directional follow relationship from one user to another.
    """
    follower_id = request.follower_id
    followed_id = request.followed_id

    if not follower_id or not followed_id:
        raise HTTPException(status_code=400, detail="Both follower_id and followed_id are required.")
    if follower_id == followed_id:
        raise HTTPException(status_code=400, detail="A user cannot follow themselves.")

    try:
        success = service.add_follow_relationship(follower_id, followed_id)
        if success:
            logger.info(f"User {follower_id} now follows {followed_id}.")
            return {"message": "User followed successfully.", "follower_id": follower_id, "followed_id": followed_id}
        else:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="One or both users not found.")
    except Exception as e:
        logger.error(f"Error adding follow relationship between {follower_id} and {followed_id}: {e}")
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to add follow relationship: {e}"
        )

@app.delete("/follows", status_code=status.HTTP_200_OK)
async def remove_follow(
    request: FollowRequest,
    service: RecommendationService = Depends(get_recommendation_service)
):
    """
    Deletes a follow relationship from one user to another.
    """
    follower_id = request.follower_id
    followed_id = request.followed_id

    if not follower_id or not followed_id:
        raise HTTPException(status_code=400, detail="Both follower_id and followed_id are required.")
    if follower_id == followed_id:
        raise HTTPException(status_code=400, detail="Invalid operation for a user on themselves.")

    try:
        success = service.remove_follow_relationship(follower_id, followed_id)
        if success:
            logger.info(f"User {follower_id} has unfollowed {followed_id}.")
            return {"message": "User unfollowed successfully.", "follower_id": follower_id, "followed_id": followed_id}
        else:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Follow relationship not found.")
    except Exception as e:
        logger.error(f"Error deleting follow relationship from {follower_id} to {followed_id}: {e}")
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to delete follow relationship: {e}"
        )


# You can add more endpoints here as new recommendation algorithms are developed.