import { useEffect, useState } from "react";
import { getReporteAreas } from "../api/reportes";

export default function ReporteAreas() {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState(null);

  useEffect(() => {
    getReporteAreas()
      .then(setRows)
      .catch(() => setErr("No se pudo cargar el reporte"))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Cargando...</p>;
  if (err) return <p style={{ color:"crimson" }}>{err}</p>;

  return (
    <div>
      <h3>Reporte: Áreas y Empleados</h3>
      <table width="100%" cellPadding="8" style={{ borderCollapse:"collapse" }}>
        <thead>
          <tr style={{ textAlign:"left", borderBottom:"1px solid #ddd" }}>
            <th>Área</th><th>Total</th><th>Profesores</th>
            <th>Planta</th><th>Contratistas</th><th>Administrativos</th>
          </tr>
        </thead>
        <tbody>
          {rows.map(r => (
            <tr key={r.areaId} style={{ borderBottom:"1px solid #eee" }}>
              <td>{r.areaNombre}</td>
              <td>{r.totalEmpleados}</td>
              <td>{r.totalProfesores}</td>
              <td>{r.profesoresPlanta}</td>
              <td>{r.profesoresContratistas}</td>
              <td>{r.totalAdministrativos}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
