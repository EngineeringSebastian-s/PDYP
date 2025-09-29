from fastapi import FastAPI
from graphene import Schema
from fastapi import GraphQLApp
from schema import schema  

app = FastAPI()

# Agregar la ruta para el servidor GraphQL
app.add_route("/graphql", GraphQLApp(schema=schema))

@app.get("/")
def read_root():
    return {"message": "Welcome to the Colegio API!"}
