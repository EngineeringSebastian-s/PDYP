# schema.py
import strawberry
from typing import List, Optional
from models import Area, Empleado, Oficina, Salon, AreaEmpleadosReport
import httpx

BASE_URL = "http://localhost:8080/api"  

@strawberry.type
class Query:
    @strawberry.field
    def areas(self) -> List[Area]:
        response = httpx.get(f"{BASE_URL}/areas")
        return [Area(**item) for item in response.json()]

    @strawberry.field
    def area(self, id: int) -> Optional[Area]:
        response = httpx.get(f"{BASE_URL}/areas/{id}")
        if response.status_code == 200:
            return Area(**response.json())
        return None

    @strawberry.field
    def empleados(self) -> List[Empleado]:
        response = httpx.get(f"{BASE_URL}/empleados")
        return [Empleado(**item) for item in response.json()]

    @strawberry.field
    def empleado(self, id: int) -> Optional[Empleado]:
        response = httpx.get(f"{BASE_URL}/empleados/{id}")
        if response.status_code == 200:
            return Empleado(**response.json())
        return None

    @strawberry.field
    def oficinas(self) -> List[Oficina]:
        response = httpx.get(f"{BASE_URL}/oficinas")
        return [Oficina(**item) for item in response.json()]

    @strawberry.field
    def salones(self) -> List[Salon]:
        response = httpx.get(f"{BASE_URL}/salones")
        return [Salon(**item) for item in response.json()]

    @strawberry.field
    def areas_empleados_report(self) -> List[AreaEmpleadosReport]:
        response = httpx.get(f"{BASE_URL}/reportes/areas-empleados")
        return [AreaEmpleadosReport(**item) for item in response.json()]

@strawberry.type
class Mutation:
    @strawberry.mutation
    def crear_area(self, nombre: str) -> Area:
        response = httpx.post(f"{BASE_URL}/areas", json={"nombre": nombre})
        return Area(**response.json())

    @strawberry.mutation
    def actualizar_area(self, id: int, nombre: str) -> Area:
        response = httpx.put(f"{BASE_URL}/areas/{id}", json={"nombre": nombre})
        return Area(**response.json())

    @strawberry.mutation
    def eliminar_area(self, id: int) -> bool:
        response = httpx.delete(f"{BASE_URL}/areas/{id}")
        return response.status_code == 204
