import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getSalones, deleteSalon } from "../api/salones";

export default function SalonesList() {
  const [salones, setSalones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState(null);

  const load = async () => {
    try {
      setLoading(true);
      const data = await getSalones();
      setSalones(data);
      setErr(null);
    } catch (e) {
      setErr(e?.response?.data?.message || "No se pudieron cargar los salones");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const onDelete = async (id) => {
    if (!confirm("¿Eliminar el salón?")) return;
    try { await deleteSalon(id); await load(); }
    catch (e) { alert(e?.response?.data?.message || "No se pudo eliminar"); }
  };

  if (loading) return <p>Cargando...</p>;
  if (err) return <p style={{ color:"crimson" }}>{err}</p>;

  return (
    <div>
      <div style={{ display:"flex", justifyContent:"space-between", marginBottom:12 }}>
        <h3>Salones</h3>
        <Link to="/salones/nuevo">+ Nuevo</Link>
      </div>

      {salones.length === 0 ? (
        <p>No hay salones aún.</p>
      ) : (
        <table width="100%" cellPadding="8" style={{ borderCollapse:"collapse" }}>
          <thead>
            <tr style={{ textAlign:"left", borderBottom:"1px solid #ddd" }}>
              <th style={{ width: 80 }}>ID</th>
              <th>Código</th>
              <th style={{ width:160 }}></th>
            </tr>
          </thead>
          <tbody>
            {salones.map(s => (
              <tr key={s.id} style={{ borderBottom:"1px solid #eee" }}>
                <td>{s.id}</td>
                <td>{s.codigo}</td>
                <td style={{ textAlign:"right" }}>
                  <Link to={`/salones/${s.id}/editar`} style={{ marginRight: 8 }}>Editar</Link>
                  <button onClick={() => onDelete(s.id)}>Eliminar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
