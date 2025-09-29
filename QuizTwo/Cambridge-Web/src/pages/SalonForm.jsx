import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createSalon, getSalon, updateSalon } from "../api/salones";

export default function SalonForm() {
  const { id } = useParams();
  const editing = Boolean(id);
  const [codigo, setCodigo] = useState("");
  const [err, setErr] = useState(null);
  const nav = useNavigate();

  useEffect(() => {
    if (!editing) return;
    getSalon(id).then(s => setCodigo(s.codigo)).catch(() => setErr("No se pudo cargar el salón"));
  }, [id, editing]);

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = { id: editing ? Number(id) : null, codigo };
      if (editing) await updateSalon(id, payload);
      else await createSalon(payload);
      nav("/salones");
    } catch (e) {
      setErr(e?.response?.data?.message || "Error al guardar");
    }
  };

  return (
    <div>
      <h3>{editing ? "Editar Salón" : "Nuevo Salón"}</h3>
      {err && <p style={{ color:"crimson" }}>{err}</p>}
      <form onSubmit={onSubmit} style={{ display:"grid", gap:12, maxWidth:420 }}>
        <label style={{ display:"grid", gap:6 }}>
          Código
          <input value={codigo} onChange={e => setCodigo(e.target.value)} required maxLength={50}/>
        </label>
        <div style={{ display:"flex", gap:8 }}>
          <button type="submit">{editing ? "Actualizar" : "Crear"}</button>
          <button type="button" onClick={() => nav("/salones")}>Cancelar</button>
        </div>
      </form>
    </div>
  );
}
