
import strawberry

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
