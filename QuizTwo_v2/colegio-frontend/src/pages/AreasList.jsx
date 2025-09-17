import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAreas, deleteArea } from "../api/areas";

export default function AreasList() {
  const [areas, setAreas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState(null);

  const load = async () => {
    try {
      setLoading(true);
      const data = await getAreas();
      setAreas(data);
      setErr(null);
    } catch (e) {
      setErr(e?.response?.data?.message || "No se pudieron cargar las áreas");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const onDelete = async (id) => {
    if (!confirm("¿Eliminar el registro?")) return;
    try {
      await deleteArea(id);
      await load();
    } catch (e) {
      alert(e?.response?.data?.message || "No se pudo eliminar");
    }
  };

  if (loading) return <p>Cargando...</p>;
  if (err) return <p style={{ color: "crimson" }}>{err}</p>;

  return (
    <div>
      <div style={{ display:"flex", justifyContent:"space-between", marginBottom:12 }}>
        <h3>Áreas</h3>
        <Link to="/areas/nueva">+ Nueva</Link>
      </div>

      {areas.length === 0 ? (
        <p>No hay áreas aún.</p>
      ) : (
        <table width="100%" cellPadding="8" style={{ borderCollapse:"collapse" }}>
          <thead>
            <tr style={{ textAlign:"left", borderBottom:"1px solid #ddd" }}>
              <th style={{ width: 80 }}>ID</th>
              <th>Nombre</th>
              <th style={{ width: 160 }}></th>
            </tr>
          </thead>
          <tbody>
            {areas.map(a => (
              <tr key={a.id} style={{ borderBottom:"1px solid #eee" }}>
                <td>{a.id}</td>
                <td>{a.nombre}</td>
                <td style={{ textAlign:"right" }}>
                  <Link to={`/areas/${a.id}/editar`} style={{ marginRight: 8 }}>Editar</Link>
                  <button onClick={() => onDelete(a.id)}>Eliminar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
