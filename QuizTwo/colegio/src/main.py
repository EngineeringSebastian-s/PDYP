import strawberry
from fastapi import FastAPI
from strawberry.fastapi import GraphQLRouter
import httpx
from typing import List, Optional
from schema import *


BASE_URL = "http://localhost:8080/api"  

# ======== MODELOS (DTOs equivalentes) ==========
@strawberry.type
class Area:
    id: int
    nombre: str

@strawberry.type
class Empleado:
    id: int
    nombre: str
    documento: str

@strawberry.type
class Oficina:
    id: int
    codigo: str

@strawberry.type
class Salon:
    id: int
    codigo: str

@strawberry.type
class AreaEmpleadosReport:
    area: str
    cantidad_empleados: int

# ========== QUERIES ========================
@strawberry.type
class Query:
    # ---- Áreas ----
    @strawberry.field
    def areas(self) -> List[Area]:
        r = httpx.get(f"{BASE_URL}/areas")
        return [Area(**a) for a in r.json()]

    @strawberry.field
    def area(self, id: int) -> Optional[Area]:
        r = httpx.get(f"{BASE_URL}/areas/{id}")
        if r.status_code == 200:
            return Area(**r.json())
        return None

    # ---- Empleados ----
    @strawberry.field
    def empleados(self) -> List[Empleado]:
        r = httpx.get(f"{BASE_URL}/empleados")
        return [Empleado(**e) for e in r.json()]

    @strawberry.field
    def empleado(self, id: int) -> Optional[Empleado]:
        r = httpx.get(f"{BASE_URL}/empleados/{id}")
        if r.status_code == 200:
            return Empleado(**r.json())
        return None

    # ---- Oficinas ----
    @strawberry.field
    def oficinas(self) -> List[Oficina]:
        r = httpx.get(f"{BASE_URL}/oficinas")
        return [Oficina(**o) for o in r.json()]

    # ---- Salones ----
    @strawberry.field
    def salones(self) -> List[Salon]:
        r = httpx.get(f"{BASE_URL}/salones")
        return [Salon(**s) for s in r.json()]

    # ---- Reportes ----
    @strawberry.field
    def areas_empleados_report(self) -> List[AreaEmpleadosReport]:
        r = httpx.get(f"{BASE_URL}/reportes/areas-empleados")
        return [AreaEmpleadosReport(**rep) for rep in r.json()]

# ========== MUTATIONS ========================
@strawberry.type
class Mutation:
    @strawberry.mutation
    def crear_area(self, nombre: str) -> Area:
        r = httpx.post(f"{BASE_URL}/areas", json={"nombre": nombre})
        return Area(**r.json())

    @strawberry.mutation
    def actualizar_area(self, id: int, nombre: str) -> Area:
        r = httpx.put(f"{BASE_URL}/areas/{id}", json={"nombre": nombre})
        return Area(**r.json())

    @strawberry.mutation
    def eliminar_area(self, id: int) -> bool:
        r = httpx.delete(f"{BASE_URL}/areas/{id}")
        return r.status_code == 204

# ========== CONFIGURACIÓN FASTAPI ============
schema = strawberry.Schema(query=Query, mutation=Mutation)
graphql_app = GraphQLRouter(schema)

app = FastAPI()
graphql_app = GraphQLRouter(schema)

app.include_router(graphql_app, prefix="/graphql")
