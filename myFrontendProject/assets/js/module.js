const ENUMS = {
    documentType: ["TI", "CC", "CE", "PASSPORT"],
    gender: ["MALE", "FEMALE", "OTHER"],
    educationLevel: ["BACHELOR", "UNDERGRADUATE", "SPECIALIST", "MASTER", "DOCTORATE"],
    contractType: ["FULL_TIME", "PART_TIME", "HOURLY", "TEMPORARY"],
    area: ["ACADEMIC", "DISCIPLINE", "ADMINISTRATIVE"],
    status: ["ACTIVE", "CANCELLED", "COMPLETED"],
    period: ["PERIOD_1", "PERIOD_2", "PERIOD_3", "PERIOD_4"],
    attendanceStatus: ["PRESENT", "ABSENT", "LATE"],
    dayOfWeek: ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]
};

const CONFIG = {
    students: {
        endpoint: "/students",
        fields: [
            ["documentType", "Tipo documento", "select", "documentType"],
            ["document", "Documento", "text"],
            ["email", "Correo", "email"],
            ["fullName", "Nombre completo", "text"],
            ["phone", "Teléfono", "text"],
            ["birthDate", "Fecha nacimiento", "date"],
            ["gender", "Género", "select", "gender"],
            ["address", "Dirección", "text"],
            ["guardianName", "Acudiente", "text"],
            ["guardianPhone", "Teléfono acudiente", "text"],
            ["guardianEmail", "Correo acudiente", "email"]
        ],
        update: [
            "guardianName",
            "guardianPhone",
            "guardianEmail"
        ],
        roleCreate: "ADMIN",
        // El backend no tiene /students/{id}/activate.
        // El estado "enabled" del estudiante es el del User
        // vinculado, así que se activa/desactiva vía /users/{userId}.
        toggle: {
            idField: "userId",
            statusField: "enabled",
            activeValue: true,
            activateUrl: id => `/users/${id}/activate`,
            deactivateUrl: id => `/users/${id}/deactivate`,
            role: "ADMIN"
        }
    },

    teachers: {
        endpoint: "/teachers",
        fields: [
            ["documentType", "Tipo documento", "select", "documentType"],
            ["document", "Documento", "text"],
            ["email", "Correo", "email"],
            ["fullName", "Nombre completo", "text"],
            ["phone", "Teléfono", "text"],
            ["birthDate", "Fecha nacimiento", "date"],
            ["gender", "Género", "select", "gender"],
            ["address", "Dirección", "text"],
            ["employeeCode", "Código empleado", "text"],
            ["hireDate", "Fecha contratación", "date"],
            ["profession", "Profesión", "text"],
            ["specialty", "Especialidad", "text"],
            ["educationLevel", "Nivel educativo", "select", "educationLevel"],
            ["contractType", "Tipo contrato", "select", "contractType"]
        ],
        update: [
            "employeeCode",
            "hireDate",
            "profession",
            "specialty",
            "educationLevel",
            "contractType"
        ],
        roleCreate: "ADMIN",
        toggle: {
            idField: "userId",
            statusField: "enabled",
            activeValue: true,
            activateUrl: id => `/users/${id}/activate`,
            deactivateUrl: id => `/users/${id}/deactivate`,
            role: "ADMIN"
        }
    },

    coordinators: {
        endpoint: "/coordinators",
        fields: [
            ["documentType", "Tipo documento", "select", "documentType"],
            ["document", "Documento", "text"],
            ["email", "Correo", "email"],
            ["fullName", "Nombre completo", "text"],
            ["phone", "Teléfono", "text"],
            ["birthDate", "Fecha nacimiento", "date"],
            ["gender", "Género", "select", "gender"],
            ["address", "Dirección", "text"],
            ["employeeCode", "Código empleado", "text"],
            ["area", "Área", "select", "area"],
            ["assignmentDate", "Fecha asignación", "date"],
            ["educationLevel", "Nivel educativo", "select", "educationLevel"]
        ],
        update: [
            "employeeCode",
            "area",
            "assignmentDate",
            "educationLevel"
        ],
        roleCreate: "ADMIN",
        toggle: {
            idField: "userId",
            statusField: "enabled",
            activeValue: true,
            activateUrl: id => `/users/${id}/activate`,
            deactivateUrl: id => `/users/${id}/deactivate`,
            role: "ADMIN"
        }
    },

    rectors: {
        endpoint: "/rectors",
        fields: [
            ["documentType", "Tipo documento", "select", "documentType"],
            ["document", "Documento", "text"],
            ["email", "Correo", "email"],
            ["fullName", "Nombre completo", "text"],
            ["phone", "Teléfono", "text"],
            ["birthDate", "Fecha nacimiento", "date"],
            ["gender", "Género", "select", "gender"],
            ["address", "Dirección", "text"],
            ["employeeCode", "Código empleado", "text"],
            ["appointmentDate", "Fecha nombramiento", "date"],
            ["administrativePeriod", "Periodo administrativo", "text"],
            ["educationLevel", "Nivel educativo", "select", "educationLevel"]
        ],
        update: [
            "employeeCode",
            "appointmentDate",
            "administrativePeriod",
            "educationLevel"
        ],
        roleCreate: "ADMIN",
        toggle: {
            idField: "userId",
            statusField: "enabled",
            activeValue: true,
            activateUrl: id => `/users/${id}/activate`,
            deactivateUrl: id => `/users/${id}/deactivate`,
            role: "ADMIN"
        }
    },

    users: {
        endpoint: "/users",
        fields: [
            ["documentType", "Tipo documento", "select", "documentType"],
            ["document", "Documento", "text"],
            ["email", "Correo", "email"],
            ["fullName", "Nombre completo", "text"],
            ["phone", "Teléfono", "text"],
            ["birthDate", "Fecha nacimiento", "date"],
            ["gender", "Género", "select", "gender"],
            ["address", "Dirección", "text"]
        ],
        update: [
            "documentType",
            "document",
            "email",
            "fullName",
            "phone",
            "birthDate",
            "gender",
            "address"
        ],
        roleCreate: "ADMIN",
        toggle: {
            idField: "id",
            statusField: "enabled",
            activeValue: true,
            activateUrl: id => `/users/${id}/activate`,
            deactivateUrl: id => `/users/${id}/deactivate`,
            role: "ADMIN"
        },
        resetPassword: {
            idField: "id",
            url: id => `/users/${id}/reset-password`,
            role: "ADMIN"
        }
    },

    courses: {
        endpoint: "/courses",
        fields: [
            ["name", "Nombre", "text"],
            ["grade", "Grado", "text"],
            ["classroom", "Aula", "text"],
            ["schoolYear", "Año escolar", "number"],
            ["directorTeacherId", "Director docente ID", "number"]
        ],
        update: [
            "name",
            "grade",
            "classroom",
            "schoolYear",
            "directorTeacherId"
        ],
        roleCreate: "ADMIN",
        toggle: {
            idField: "id",
            statusField: "active",
            activeValue: true,
            activateUrl: id => `/courses/${id}/activate`,
            deactivateUrl: id => `/courses/${id}/deactivate`,
            role: "ADMIN"
        }
    },

    subjects: {
        endpoint: "/subjects",
        fields: [
            ["name", "Nombre", "text"]
        ],
        update: [
            "name"
        ],
        roleCreate: "ADMIN",
        toggle: {
            idField: "id",
            statusField: "active",
            activeValue: true,
            activateUrl: id => `/subjects/${id}/activate`,
            deactivateUrl: id => `/subjects/${id}/deactivate`,
            role: "ADMIN"
        }
    },

    "course-subjects": {
        endpoint: "/course-subjects",
        fields: [
            ["courseId", "Curso ID", "number"],
            ["subjectId", "Materia ID", "number"],
            ["teacherId", "Docente ID", "number"]
        ],
        createOnly: true,
        roleCreate: "ADMIN",
        toggle: {
            idField: "id",
            statusField: "active",
            activeValue: true,
            deactivateUrl: id => `/course-subjects/${id}/deactivate`,
            deactivateLabel: "Desactivar",
            role: "ADMIN"
        }
    },

    enrollments: {
        endpoint: "/enrollments",
        fields: [
            ["studentId", "Estudiante ID", "number"],
            ["courseId", "Curso ID", "number"]
        ],
        createOnly: true,
        roleCreate: "ADMIN,COORDINATOR",
        toggle: {
            idField: "id",
            statusField: "status",
            activeValue: "ACTIVE",
            deactivateUrl: id => `/enrollments/${id}/cancel`,
            deactivateLabel: "Cancelar",
            role: "ADMIN,COORDINATOR"
        }
    },

    schedules: {
        endpoint: "/schedules",
        fields: [
            ["courseSubjectId", "Curso-materia ID", "number"],
            ["dayOfWeek", "Día", "select", "dayOfWeek"],
            ["startTime", "Hora inicio", "time"],
            ["endTime", "Hora fin", "time"]
        ],
        createOnly: true,
        roleCreate: "ADMIN",
        listBy: {
            param: "courseId",
            label: "Curso ID",
            path: id => `/schedules/course/${id}`
        }
    },

    assignments: {
        endpoint: "/assignments",
        fields: [
            ["courseSubjectId", "Curso-materia ID", "number"],
            ["title", "Título", "text"],
            ["description", "Descripción", "textarea"],
            ["dueDate", "Fecha límite", "date"]
        ],
        createOnly: true,
        roleCreate: "TEACHER",
        listBy: {
            param: "courseSubjectId",
            label: "Curso-materia ID",
            path: id => `/assignments/course-subject/${id}`
        }
    },

    grades: {
        endpoint: "/grades",
        fields: [
            ["studentId", "Estudiante ID", "number"],
            ["courseSubjectId", "Curso-materia ID", "number"],
            ["period", "Periodo", "select", "period"],
            ["value", "Nota (0.0 - 10.0)", "number"]
        ],
        update: [
            "value"
        ],
        roleCreate: "TEACHER",
        // El backend no tiene GET /grades (lista completa).
        // Se consulta por estudiante: GET /grades/student/{studentId}
        listBy: {
            param: "studentId",
            label: "Estudiante ID",
            path: id => `/grades/student/${id}`
        }
    },

    attendance: {
        endpoint: "/attendances",
        fields: [
            ["studentId", "Estudiante ID", "number"],
            ["courseSubjectId", "Curso-materia ID", "number"],
            ["date", "Fecha", "date"],
            ["status", "Estado", "select", "attendanceStatus"]
        ],
        createOnly: true,
        roleCreate: "TEACHER",
        listBy: {
            param: "studentId",
            label: "Estudiante ID",
            path: id => `/attendances/student/${id}`
        }
    }
};

function fieldsHtml(fields, values = {}, only = null) {
    return `
        <div class="form-grid">
            ${fields
                .filter(field => !only || only.includes(field[0]))
                .map(field => {
                    const [name, label, type, enumKey] = field;
                    const value = values[name] ?? "";

                    if (type === "select") {
                        return `
                            <label>
                                <span>${label}</span>
                                <select name="${name}" required>
                                    <option value="">Seleccione</option>
                                    ${ENUMS[enumKey]
                                        .map(option => `
                                            <option
                                                value="${option}"
                                                ${value === option ? "selected" : ""}
                                            >
                                                ${option}
                                            </option>
                                        `)
                                        .join("")}
                                </select>
                            </label>
                        `;
                    }

                    if (type === "textarea") {
                        return `
                            <label>
                                <span>${label}</span>
                                <textarea
                                    name="${name}"
                                    required
                                >${UI.esc(value)}</textarea>
                            </label>
                        `;
                    }

                    return `
                        <label>
                            <span>${label}</span>
                            <input
                                name="${name}"
                                type="${type}"
                                value="${UI.esc(value)}"
                                ${type === "number" ? 'step="any"' : ""}
                                required
                            >
                        </label>
                    `;
                })
                .join("")}
        </div>
    `;
}

function rowValue(value) {
    if (value === true) {
        return "Sí";
    }

    if (value === false) {
        return "No";
    }

    if (typeof value === "object" && value !== null) {
        return UI.esc(JSON.stringify(value));
    }

    return UI.esc(value ?? "");
}

function tableHtml(data, actions) {
    if (!data || !data.length) {
        return `
            <div class="empty">
                No hay registros para mostrar.
            </div>
        `;
    }

    const keys = Object.keys(data[0])
        .filter(key => !["userId", "createdAt"].includes(key));

    return `
        <div class="table-wrap">
            <table class="table">
                <thead>
                    <tr>
                        ${keys.map(key => `<th>${key}</th>`).join("")}
                        <th>Acciones</th>
                    </tr>
                </thead>

                <tbody>
                    ${data.map((row, index) => `
                        <tr>
                            ${keys.map(key => `
                                <td>
                                    ${rowValue(row[key])}
                                </td>
                            `).join("")}

                            <td>
                                ${actions(row, index)}
                            </td>
                        </tr>
                    `).join("")}
                </tbody>
            </table>
        </div>
    `;
}

async function loadCrud(key, overrideEndpoint = null) {
    const cfg = CONFIG[key];
    const box = document.getElementById("pageContent");

    if (!cfg || !box) {
        return;
    }

    const role =
        window.Auth &&
        typeof window.Auth.role === "function"
            ? window.Auth.role()
            : "";

    const canToggle =
        cfg.toggle &&
        cfg.toggle.role
            ?.split(",")
            .map(item => item.trim())
            .includes(role);

    const canReset =
        cfg.resetPassword &&
        cfg.resetPassword.role
            ?.split(",")
            .map(item => item.trim())
            .includes(role);

    try {
        const response = await Api.get(overrideEndpoint || cfg.endpoint);
        const data = response.data || [];

        const dataCard = box.querySelector(".data-card");

        if (!dataCard) {
            return;
        }

        dataCard.innerHTML = tableHtml(
            data,
            row => `
                ${
                    cfg.update
                        ? `
                            <button
                                class="btn btn-secondary btn-sm"
                                onclick='Module.edit(
                                    "${key}",
                                    ${JSON.stringify(row).replace(/'/g, "&#39;")}
                                )'
                            >
                                Editar
                            </button>
                        `
                        : ""
                }
                ${
                    canToggle
                        ? toggleButtonHtml(key, cfg, row)
                        : ""
                }
                ${
                    canReset
                        ? resetPasswordButtonHtml(key, cfg, row)
                        : ""
                }
            `
        );

    } catch (error) {

        const dataCard = box.querySelector(".data-card");

        if (dataCard) {
            dataCard.innerHTML = `
                <div class="card danger-text">
                    ${UI.esc(error.message)}
                </div>
            `;
        }

        console.error(
            "Error cargando módulo:",
            key,
            error
        );
    }
}

function toggleButtonHtml(key, cfg, row) {

    const toggle = cfg.toggle;

    const id = row[toggle.idField];

    if (id === undefined || id === null) {
        return "";
    }

    const isActive =
        row[toggle.statusField] === toggle.activeValue;

    if (isActive) {

        if (!toggle.deactivateUrl) {
            return "";
        }

        return `
            <button
                class="btn btn-danger btn-sm"
                onclick='Module.toggleStatus(
                    "${key}", ${JSON.stringify(id)}, false
                )'
            >
                ${toggle.deactivateLabel || "Desactivar"}
            </button>
        `;
    }

    if (!toggle.activateUrl) {
        return "";
    }

    return `
        <button
            class="btn btn-primary btn-sm"
            onclick='Module.toggleStatus(
                "${key}", ${JSON.stringify(id)}, true
            )'
        >
            ${toggle.activateLabel || "Activar"}
        </button>
    `;
}

function resetPasswordButtonHtml(key, cfg, row) {

    const reset = cfg.resetPassword;

    const id = row[reset.idField];

    if (id === undefined || id === null) {
        return "";
    }

    return `
        <button
            class="btn btn-warning btn-sm"
            onclick='Module.resetPassword(
                "${key}", ${JSON.stringify(id)}
            )'
        >
            Restablecer contraseña
        </button>
    `;
}

window.Module = {

    async render(key) {

        const box =
            document.getElementById("pageContent");

        if (!box) {
            console.error(
                "No existe el elemento #pageContent"
            );
            return;
        }

        if (key === "dashboard") {

            if (
                window.Dashboard &&
                typeof window.Dashboard.render === "function"
            ) {
                return window.Dashboard.render();
            }

            console.error(
                "Dashboard no está disponible"
            );

            return;
        }

        const specialPages = [
            "student-profile",
            "student-grades",
            "student-attendance",
            "student-schedule",
            "student-assignments",
            "student-report",
            "teacher-profile",
            "teacher-schedule"
        ];

        if (specialPages.includes(key)) {

            if (
                window.Special &&
                typeof window.Special.render === "function"
            ) {
                return window.Special.render(key);
            }

            console.error(
                "Special no está disponible"
            );

            return;
        }

        const cfg = CONFIG[key];

        if (!cfg) {

            box.innerHTML = `
                <div class="card">
                    <h2>Módulo no encontrado</h2>
                </div>
            `;

            return;
        }

        const role =
            window.Auth &&
            typeof window.Auth.role === "function"
                ? window.Auth.role()
                : "";

        const canCreate =
            cfg.roleCreate
                ?.split(",")
                .map(item => item.trim())
                .includes(role);

        const title =
            window.PAGE_TITLE ||
            "Gestión";

        box.innerHTML = `
            <div class="card">

                <div class="toolbar">

                    <div>
                        <h2>${UI.esc(title)}</h2>

                        <p class="muted">
                            ${
                                cfg.listBy
                                    ? "Este listado no se puede cargar completo: busca por " + cfg.listBy.label + "."
                                    : "Gestiona la información desde esta pantalla."
                            }
                        </p>
                    </div>

                    <div class="actions">

                        ${
                            canCreate
                                ? `
                                    <button
                                        class="btn btn-primary"
                                        onclick="Module.create('${key}')"
                                    >
                                        + Nuevo
                                    </button>
                                `
                                : ""
                        }

                    </div>

                </div>

                ${
                    cfg.listBy
                        ? `
                            <form id="filterForm" class="toolbar" style="gap: 8px;">
                                <label style="flex:1;">
                                    <span>${UI.esc(cfg.listBy.label)}</span>
                                    <input
                                        name="filterId"
                                        type="number"
                                        required
                                        placeholder="Ej: 1"
                                    >
                                </label>
                                <button
                                    type="submit"
                                    class="btn btn-secondary"
                                    style="align-self: flex-end;"
                                >
                                    Buscar
                                </button>
                            </form>
                        `
                        : ""
                }

                <div class="data-card">
                    ${
                        cfg.listBy
                            ? `
                                <div class="empty">
                                    Ingresa un ${UI.esc(cfg.listBy.label)}
                                    y presiona Buscar.
                                </div>
                            `
                            : `
                                <div class="loading">
                                    Cargando...
                                </div>
                            `
                    }
                </div>

            </div>
        `;

        if (cfg.listBy) {

            const filterForm =
                box.querySelector("#filterForm");

            if (filterForm) {

                filterForm.onsubmit = async event => {

                    event.preventDefault();

                    const id =
                        new FormData(filterForm)
                            .get("filterId");

                    const dataCard =
                        box.querySelector(".data-card");

                    if (dataCard) {
                        dataCard.innerHTML = `
                            <div class="loading">
                                Cargando...
                            </div>
                        `;
                    }

                    await loadCrud(
                        key,
                        cfg.listBy.path(id)
                    );

                };

            }

            return;
        }

        await loadCrud(key);
    },

    create(key) {

        const cfg = CONFIG[key];

        if (!cfg) {
            UI.toast(
                "Módulo no encontrado",
                "error"
            );
            return;
        }

        const modal = UI.modal(
            "Nuevo registro",
            `
                <form id="crudForm">

                    ${fieldsHtml(cfg.fields)}

                    <div class="page-actions">

                        <button
                            type="submit"
                            class="btn btn-primary"
                        >
                            Guardar
                        </button>

                    </div>

                </form>
            `
        );

        const form =
            modal.querySelector("#crudForm");

        form.onsubmit = async event => {

            event.preventDefault();

            const body =
                Object.fromEntries(
                    new FormData(form)
                );

            const numericFields = [
                "schoolYear",
                "directorTeacherId",
                "courseSubjectId",
                "studentId",
                "courseId",
                "subjectId",
                "teacherId",
                "value"
            ];

            numericFields.forEach(field => {

                if (
                    body[field] !== undefined &&
                    body[field] !== ""
                ) {
                    body[field] = Number(body[field]);
                }

            });

            try {

                await Api.post(
                    cfg.endpoint,
                    body
                );

                modal.remove();

                UI.toast(
                    "Registro creado correctamente"
                );

                if (cfg.listBy) {
                    // Este módulo no tiene un GET de lista completa;
                    // el usuario debe volver a buscar con el filtro.
                } else {
                    await loadCrud(key);
                }

            } catch (error) {

                console.error(
                    "Error creando registro:",
                    error
                );

                UI.toast(
                    error.message,
                    "error"
                );
            }
        };
    },

    edit(key, row) {

        const cfg = CONFIG[key];

        if (!cfg || !cfg.update) {
            UI.toast(
                "Este módulo no tiene actualización en el backend.",
                "error"
            );
            return;
        }

        const modal = UI.modal(
            "Editar registro",
            `
                <form id="crudForm">

                    ${fieldsHtml(
                        cfg.fields,
                        row,
                        cfg.update
                    )}

                    <div class="page-actions">

                        <button
                            type="submit"
                            class="btn btn-primary"
                        >
                            Guardar cambios
                        </button>

                    </div>

                </form>
            `
        );

        const form =
            modal.querySelector("#crudForm");

        form.onsubmit = async event => {

            event.preventDefault();

            const body =
                Object.fromEntries(
                    new FormData(form)
                );

            const numericFields = [
                "schoolYear",
                "directorTeacherId",
                "value"
            ];

            numericFields.forEach(field => {

                if (
                    body[field] !== undefined &&
                    body[field] !== ""
                ) {
                    body[field] = Number(body[field]);
                }

            });

            try {

                await Api.put(
                    `${cfg.endpoint}/${row.id}`,
                    body
                );

                modal.remove();

                UI.toast(
                    "Cambios guardados"
                );

                await loadCrud(key);

            } catch (error) {

                console.error(
                    "Error actualizando registro:",
                    error
                );

                UI.toast(
                    error.message,
                    "error"
                );
            }
        };
    },

    async toggleStatus(key, id, activate) {

        const cfg = CONFIG[key];

        if (!cfg || !cfg.toggle) {
            return;
        }

        const toggle = cfg.toggle;

        const url = activate
            ? toggle.activateUrl?.(id)
            : toggle.deactivateUrl?.(id);

        if (!url) {
            return;
        }

        try {

            await Api.put(url, {});

            UI.toast(
                activate
                    ? "Registro activado correctamente"
                    : "Registro desactivado correctamente"
            );

            await loadCrud(key);

        } catch (error) {

            console.error(
                "Error cambiando estado:",
                key,
                error
            );

            UI.toast(
                error.message,
                "error"
            );
        }
    },

    async resetPassword(key, id) {

        const cfg = CONFIG[key];

        if (!cfg || !cfg.resetPassword) {
            return;
        }

        const confirmed = window.confirm(
            "¿Restablecer la contraseña de este usuario? " +
            "Quedará igual a su número de documento y deberá " +
            "cambiarla en el próximo inicio de sesión."
        );

        if (!confirmed) {
            return;
        }

        try {

            await Api.put(
                cfg.resetPassword.url(id),
                {}
            );

            UI.toast(
                "Contraseña restablecida. El usuario deberá " +
                "iniciar sesión con su documento como contraseña."
            );

        } catch (error) {

            console.error(
                "Error restableciendo contraseña:",
                key,
                error
            );

            UI.toast(
                error.message,
                "error"
            );
        }
    }
};