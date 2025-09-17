import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createArea, getArea, updateArea } from "../api/areas";

export default function AreaForm() {
  const { id } = useParams();
  const editing = Boolean(id);
  const [nombre, setNombre] = useState("");
  const [err, setErr] = useState(null);
  const nav = useNavigate();

  useEffect(() => {
    if (!editing) return;
    getArea(id)
      .then(a => setNombre(a.nombre))
      .catch(() => setErr("No se pudo cargar el área"));
  }, [id, editing]);

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = { id: editing ? Number(id) : null, nombre };
      if (editing) await updateArea(id, payload);
      else await createArea(payload);
      nav("/areas");
    } catch (e) {
      // mostramos mensaje del backend si viene
      setErr(e?.response?.data?.message || "Error al guardar");
    }
  };

  return (
    <div>
      <h3>{editing ? "Editar Área" : "Nueva Área"}</h3>
      {err && <p style={{ color:"crimson" }}>{err}</p>}
      <form onSubmit={onSubmit} style={{ display:"grid", gap:12, maxWidth: 420 }}>
        <label style={{ display:"grid", gap:6 }}>
          Nombre
          <input
            value={nombre}
            onChange={e => setNombre(e.target.value)}
            required
            maxLength={120}
            placeholder="Ej. Académica"
          />
        </label>
        <div style={{ display:"flex", gap:8 }}>
          <button type="submit">{editing ? "Actualizar" : "Crear"}</button>
          <button type="button" onClick={() => nav("/areas")}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}
