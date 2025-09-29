import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {deleteEmpleado, getEmpleados} from "../api/empleados";
import {getAreas} from "../api/areas";
import {getOficinas} from "../api/oficinas";

export default function EmpleadosList() {
    const [items, setItems] = useState([]);
    const [areasById, setAreasById] = useState({});
    const [oficinasById, setOficinasById] = useState({});
    const [loading, setLoading] = useState(true);
    const [err, setErr] = useState(null);

    const load = async () => {
        try {
            setLoading(true);
            const [emps, ars, ofs] = await Promise.all([getEmpleados(), getAreas(), getOficinas()]);
            setItems(emps);
            setAreasById(Object.fromEntries(ars.map(a => [a.id, a.nombre])));
            setOficinasById(Object.fromEntries(ofs.map(o => [o.id, o.codigo])));
            setErr(null);
        } catch (e) {
            setErr(e?.response?.data?.message || "No se pudieron cargar los empleados");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        load();
    }, []);

    const onDelete = async (id) => {
        if (!confirm("¿Eliminar empleado?")) return;
        try {
            await deleteEmpleado(id);
            await load();
        } catch (e) {
            alert(e?.response?.data?.message || "No se pudo eliminar");
        }
    };

    if (loading) return <p>Cargando...</p>;
    if (err) return <p style={{color: "crimson"}}>{err}</p>;

    return (
        <div>
            <div style={{display: "flex", justifyContent: "space-between", marginBottom: 12}}>
                <h3>Empleados</h3>
                <Link to="/empleados/nuevo">+ Nuevo</Link>
            </div>

            {items.length === 0 ? (
                <p>No hay empleados aún.</p>
            ) : (
                <table width="100%" cellPadding="8" style={{borderCollapse: "collapse"}}>
                    <thead>
                    <tr style={{textAlign: "left", borderBottom: "1px solid #ddd"}}>
                        <th style={{width: 70}}>ID</th>
                        <th>Nombre</th>
                        <th>Documento</th>
                        <th>Área</th>
                        <th>Oficina</th>
                        <th>Tipo Empleado</th>
                        <th>Tipo Profesor</th>
                        <th style={{width: 160}}></th>
                    </tr>
                    </thead>
                    <tbody>
                    {items.map(e => (
                        <tr key={e.id} style={{borderBottom: "1px solid #eee"}}>
                            <td>{e.id}</td>
                            <td>{e.nombre}</td>
                            <td>{e.documento}</td>
                            <td>{areasById[e.areaId] ?? e.areaId}</td>
                            <td>{oficinasById[e.oficinaId] ?? e.oficinaId}</td>
                            <td>{e.tipoEmpleado}</td>
                            <td>{e.tipoProfesor ?? "-"}</td>
                            <td style={{textAlign: "right"}}>
                                <Link to={`/empleados/${e.id}/editar`} style={{marginRight: 8}}>Editar</Link>
                                <button onClick={() => onDelete(e.id)}>Eliminar</button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
