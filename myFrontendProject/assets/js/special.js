window.Special = {

    async render(key) {

        const box =
            document.getElementById("pageContent");

        if (!box) {
            console.error("No existe #pageContent");
            return;
        }

        const map = {

            "student-profile": [
                "/students/me",
                "Mi perfil"
            ],

            "teacher-profile": [
                "/teachers/me",
                "Mi perfil"
            ],

            "student-grades": [
                "/grades/me",
                "Mis notas"
            ],

            "student-attendance": [
                "/attendances/me",
                "Mi asistencia"
            ],

            "student-schedule": [
                "/schedules/me",
                "Mi horario"
            ],

            "student-assignments": [
                "/assignments/me",
                "Mis actividades"
            ],

            "teacher-schedule": [
                "/schedules/teacher/me",
                "Mi horario"
            ]

        };

        if (key === "student-report") {

            box.innerHTML = `
                <div class="card">

                    <h2>
                        Mi boletín
                    </h2>

                    <p class="muted">
                        Descarga tu boletín en PDF.
                    </p>

                    <button
                        class="btn btn-primary"
                        id="downloadReport"
                    >
                        Descargar boletín
                    </button>

                </div>
            `;

            const button =
                document.getElementById(
                    "downloadReport"
                );

            if (button) {

                button.onclick = () => {

                    Special.pdf(
                        "/report-cards/me",
                        "boletin.pdf"
                    );

                };

            }

            return;
        }

        if (!map[key]) {

            box.innerHTML = `
                <div class="card">

                    <h2>
                        Página no encontrada
                    </h2>

                </div>
            `;

            return;
        }

        const [url, title] =
            map[key];

        box.innerHTML = `
            <div class="card">

                <h2>
                    ${UI.esc(title)}
                </h2>

                <div
                    id="specialData"
                    class="loading"
                >
                    Cargando...
                </div>

            </div>
        `;

        try {

            const response =
                await Api.get(url);

            const data =
                response.data;

            const container =
                document.getElementById(
                    "specialData"
                );

            if (!container) {
                return;
            }

            if (Array.isArray(data)) {

                container.innerHTML =
                    tableHtml(
                        data,
                        () => ""
                    );

                return;
            }

            if (
                typeof data === "object" &&
                data !== null
            ) {

                container.innerHTML = `
                    <div
                        class="grid"
                        style="
                            grid-template-columns:
                            repeat(
                                auto-fit,
                                minmax(220px, 1fr)
                            );
                        "
                    >

                        ${Object.entries(data)
                            .map(([key, value]) => `
                                <div class="card">

                                    <strong>
                                        ${UI.esc(key)}
                                    </strong>

                                    <div>
                                        ${rowValue(value)}
                                    </div>

                                </div>
                            `)
                            .join("")}

                    </div>
                `;

                return;
            }

            container.innerHTML = `
                <div class="empty">
                    No hay información disponible.
                </div>
            `;

        } catch (error) {

            const container =
                document.getElementById(
                    "specialData"
                );

            if (container) {

                container.innerHTML = `
                    <div class="danger-text">
                        ${UI.esc(error.message)}
                    </div>
                `;

            }

            console.error(
                "Error cargando módulo especial:",
                error
            );
        }

    },

    async pdf(url, name) {

        try {

            const token =
                localStorage.getItem(
                    "accessToken"
                );

            if (!token) {

                throw new Error(
                    "No estás autenticado."
                );

            }

            const response =
                await fetch(
                    `${API_URL}${url}`,
                    {
                        method: "GET",

                        headers: {
                            "Accept":
                                "application/pdf",

                            "Authorization":
                                `Bearer ${token}`
                        }
                    }
                );

            if (!response.ok) {

                let message =
                    `Error HTTP ${response.status}`;

                try {

                    const data =
                        await response.json();

                    if (data.message) {
                        message =
                            data.message;
                    }

                } catch {}

                throw new Error(message);
            }

            const blob =
                await response.blob();

            const objectUrl =
                URL.createObjectURL(blob);

            const link =
                document.createElement("a");

            link.href =
                objectUrl;

            link.download =
                name;

            document.body.appendChild(link);

            link.click();

            link.remove();

            URL.revokeObjectURL(
                objectUrl
            );

        } catch (error) {

            console.error(
                "Error descargando PDF:",
                error
            );

            UI.toast(
                error.message,
                "error"
            );

        }

    }

};