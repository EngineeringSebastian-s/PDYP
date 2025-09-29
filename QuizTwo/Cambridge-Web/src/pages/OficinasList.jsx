import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {deleteOficina, getOficinas} from "../api/oficinas";
import {getAreas} from "../api/areas";

export default function OficinasList() {
    const [oficinas, setOficinas] = useState([]);
    const [areasById, setAreasById] = useState({});
    const [loading, setLoading] = useState(true);
    const [err, setErr] = useState(null);

    const load = async () => {
        try {
            setLoading(true);
            const [ofs, ars] = await Promise.all([getOficinas(), getAreas()]);
            setOficinas(ofs);
            setAreasById(Object.fromEntries(ars.map(a => [a.id, a.nombre])));
            setErr(null);
        } catch (e) {
            setErr(e?.response?.data?.message || "No se pudieron cargar las oficinas");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        load();
    }, []);

    const onDelete = async (id) => {
        if (!confirm("¿Eliminar la oficina?")) return;
        try {
            await deleteOficina(id);
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
                <h3>Oficinas</h3>
                <Link to="/oficinas/nueva">+ Nueva</Link>
            </div>

            {oficinas.length === 0 ? (
                <p>No hay oficinas aún.</p>
            ) : (
                <table width="100%" cellPadding="8" style={{borderCollapse: "collapse"}}>
                    <thead>
                    <tr style={{textAlign: "left", borderBottom: "1px solid #ddd"}}>
                        <th style={{width: 80}}>ID</th>
                        <th style={{width: 160}}>Código</th>
                        <th>Área</th>
                        <th style={{width: 160}}></th>
                    </tr>
                    </thead>
                    <tbody>
                    {oficinas.map(o => (
                        <tr key={o.id} style={{borderBottom: "1px solid #eee"}}>
                            <td>{o.id}</td>
                            <td>{o.codigo}</td>
                            <td>{areasById[o.areaId] ?? o.areaId}</td>
                            <td style={{textAlign: "right"}}>
                                <Link to={`/oficinas/${o.id}/editar`} style={{marginRight: 8}}>Editar</Link>
                                <button onClick={() => onDelete(o.id)}>Eliminar</button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
