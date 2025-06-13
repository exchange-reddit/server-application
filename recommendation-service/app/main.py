# app/main.py
from fastapi import FastAPI
from app.db import Neo4jConnection

app = FastAPI()

@app.get("/")
def read_root():
    return {"Hello": "World"}

@app.get("/friend-recommendations/{user_id}")
def get_friend_recommendations(user_id: str): 
    return {"user_id": user_id, "recommendations": ["user1", "user2", "user3"]}

@app.get("/nodes")
def get_nodes():
    result = db.query("MATCH (n) RETURN n LIMIT 10")
    return {"nodes": result}