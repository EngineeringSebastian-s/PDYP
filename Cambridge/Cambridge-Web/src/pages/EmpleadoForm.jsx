import {useEffect, useMemo, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {getAreas} from "../api/areas";
import {getOficinas} from "../api/oficinas";
import {createEmpleado, getEmpleado, updateEmpleado} from "../api/empleados";

export default function EmpleadoForm() {
    const {id} = useParams();
    const editing = Boolean(id);

    const [nombre, setNombre] = useState("");
    const [documento, setDocumento] = useState("");
    const [areaId, setAreaId] = useState("");
    const [oficinaId, setOficinaId] = useState("");
    const [tipoEmpleado, setTipoEmpleado] = useState("ADMINISTRATIVO"); // default
    const [tipoProfesor, setTipoProfesor] = useState(""); // PLANTA/CONTRATISTA (solo si profesor)

    const [areas, setAreas] = useState([]);
    const [oficinas, setOficinas] = useState([]);
    const [err, setErr] = useState(null);
    const nav = useNavigate();

    const isProfesor = useMemo(() => tipoEmpleado === "PROFESOR", [tipoEmpleado]);

    useEffect(() => {
        const load = async () => {
            const [ars, ofs] = await Promise.all([getAreas(), getOficinas()]);
            setAreas(ars);
            setOficinas(ofs);

            if (editing) {
                const e = await getEmpleado(id);
                setNombre(e.nombre);
                setDocumento(e.documento);
                setAreaId(String(e.areaId));
                setOficinaId(String(e.oficinaId));
                setTipoEmpleado(e.tipoEmpleado || "ADMINISTRATIVO");
                setTipoProfesor(e.tipoProfesor || "");
            } else {
                if (ars.length) setAreaId(String(ars[0].id));
                if (ofs.length) setOficinaId(String(ofs[0].id));
            }
        };
        load().catch(() => setErr("No se pudo cargar la información"));
    }, [id, editing]);

    const onSubmit = async (e) => {
        e.preventDefault();
        try {
            const payload = {
                nombre,
                documento,
                areaId: Number(areaId),
                oficinaId: Number(oficinaId),
                tipoEmpleado,
                tipoProfesor: isProfesor ? (tipoProfesor || "") : null
            };
            if (editing) await updateEmpleado(id, payload);
            else await createEmpleado(payload);
            nav("/empleados");
        } catch (e) {
            setErr(e?.response?.data?.message || "Error al guardar");
        }
    };

    return (
        <div>
            <h3>{editing ? "Editar Empleado" : "Nuevo Empleado"}</h3>
            {err && <p style={{color: "crimson"}}>{err}</p>}

            <form onSubmit={onSubmit} style={{display: "grid", gap: 12, maxWidth: 520}}>
                <label style={{display: "grid", gap: 6}}>
                    Nombre
                    <input value={nombre} onChange={e => setNombre(e.target.value)} required maxLength={120}/>
                </label>

                <label style={{display: "grid", gap: 6}}>
                    Documento
                    <input value={documento} onChange={e => setDocumento(e.target.value)} required maxLength={40}/>
                </label>

                <div style={{display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12}}>
                    <label style={{display: "grid", gap: 6}}>
                        Área
                        <select value={areaId} onChange={e => setAreaId(e.target.value)} required>
                            {areas.map(a => <option key={a.id} value={a.id}>{a.nombre}</option>)}
                        </select>
                    </label>

                    <label style={{display: "grid", gap: 6}}>
                        Oficina
                        <select value={oficinaId} onChange={e => setOficinaId(e.target.value)} required>
                            {oficinas.map(o => <option key={o.id} value={o.id}>{o.codigo}</option>)}
                        </select>
                    </label>
                </div>

                <label style={{display: "grid", gap: 6}}>
                    Tipo de empleado
                    <select value={tipoEmpleado} onChange={e => setTipoEmpleado(e.target.value)} required>
                        <option value="ADMINISTRATIVO">ADMINISTRATIVO</option>
                        <option value="PROFESOR">PROFESOR</option>
                    </select>
                </label>

                {isProfesor && (
                    <label style={{display: "grid", gap: 6}}>
                        Tipo de profesor
                        <select value={tipoProfesor} onChange={e => setTipoProfesor(e.target.value)}
                                required={isProfesor}>
                            <option value="">Seleccione…</option>
                            <option value="PLANTA">PLANTA</option>
                            <option value="CONTRATISTA">CONTRATISTA</option>
                        </select>
                    </label>
                )}

                <div style={{display: "flex", gap: 8}}>
                    <button type="submit">{editing ? "Actualizar" : "Crear"}</button>
                    <button type="button" onClick={() => nav("/empleados")}>Cancelar</button>
                </div>
            </form>
        </div>
    );
}
