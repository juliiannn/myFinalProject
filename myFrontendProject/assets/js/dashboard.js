window.Dashboard = {

    charts: {},

    async render() {

        const box = document.getElementById("pageContent");

        if (!box) {
            console.error("No existe #pageContent");
            return;
        }

        if (this.charts.students) {
            this.charts.students.destroy();
        }

        if (this.charts.grades) {
            this.charts.grades.destroy();
        }

        if (this.charts.attendance) {
            this.charts.attendance.destroy();
        }

        const role =
            window.Auth &&
            typeof window.Auth.role === "function"
                ? window.Auth.role()
                : "";

        const canSeeStats =
            role === "ADMIN" || role === "RECTOR";

        if (!canSeeStats) {

            box.innerHTML = `
                <div class="card">

                    <h2>
                        Bienvenido${
                            window.Auth &&
                            typeof window.Auth.email === "function" &&
                            window.Auth.email()
                                ? ", " + UI.esc(window.Auth.email())
                                : ""
                        }
                    </h2>

                    <p class="muted">
                        Usa el menú de la izquierda para
                        gestionar la información disponible
                        para tu rol (${UI.esc(role || "usuario")}).
                    </p>

                </div>
            `;

            return;
        }

        box.innerHTML = `
            <div class="grid stats">

                <div class="card kpi">
                    <div class="stat-label">Estudiantes</div>
                    <div id="sStudents" class="stat-value">—</div>
                </div>

                <div class="card kpi">
                    <div class="stat-label">Docentes</div>
                    <div id="sTeachers" class="stat-value">—</div>
                </div>

                <div class="card kpi">
                    <div class="stat-label">Coordinadores</div>
                    <div id="sCoordinators" class="stat-value">—</div>
                </div>

                <div class="card kpi">
                    <div class="stat-label">Cursos activos</div>
                    <div id="sCourses" class="stat-value">—</div>
                </div>

                <div class="card kpi">
                    <div class="stat-label">Materias</div>
                    <div id="sSubjects" class="stat-value">—</div>
                </div>

            </div>

            <div class="grid"
                 style="grid-template-columns:repeat(auto-fit,minmax(320px,1fr))">

                <div class="card">
                    <h2>Estudiantes por curso</h2>

                    <div class="chart-box">
                        <canvas id="studentsChart"></canvas>
                    </div>
                </div>

                <div class="card">
                    <h2>Promedios por materia</h2>

                    <div class="chart-box">
                        <canvas id="gradesChart"></canvas>
                    </div>
                </div>

                <div class="card">
                    <h2>Asistencia por curso</h2>

                    <div class="chart-box">
                        <canvas id="attendanceChart"></canvas>
                    </div>
                </div>

            </div>
        `;

        try {

            const response =
                await Api.get("/statistics/general");
            const data =
                response.data || {};

            document.getElementById("sStudents").textContent =
                data.totalStudents ?? 0;

            document.getElementById("sTeachers").textContent =
                data.totalTeachers ?? 0;

            document.getElementById("sCoordinators").textContent =
                data.totalCoordinators ?? 0;

            document.getElementById("sCourses").textContent =
                data.activeCourses ?? 0;

            document.getElementById("sSubjects").textContent =
                data.totalSubjects ?? 0;

        } catch (error) {

            console.error(
                "Error estadísticas generales:",
                error
            );

        }

        try {

            const response =
                await Api.get(
                    "/statistics/students-by-course"
                );

            const data =
                response.data || [];

            const canvas =
                document.getElementById(
                    "studentsChart"
                );

            if (
                typeof Chart !== "undefined" &&
                canvas
            ) {

                this.charts.students =
                    new Chart(canvas, {

                        type: "bar",

                        data: {

                            labels: data.map(
                                item =>
                                    item.courseName
                            ),

                            datasets: [
                                {
                                    label: "Estudiantes",

                                    data: data.map(
                                        item =>
                                            Number(
                                                item.studentCount
                                            )
                                    )
                                }
                            ]

                        },

                        options: {
                            responsive: true,
                            maintainAspectRatio: false
                        }

                    });

            }

        } catch (error) {

            console.error(
                "Error estudiantes por curso:",
                error
            );

        }

        try {

            const response =
                await Api.get(
                    "/statistics/average-grades"
                );

            const data =
                response.data || [];

            const canvas =
                document.getElementById(
                    "gradesChart"
                );

            if (
                typeof Chart !== "undefined" &&
                canvas
            ) {

                this.charts.grades =
                    new Chart(canvas, {

                        type: "bar",

                        data: {

                            labels: data.map(
                                item =>
                                    item.subjectName
                            ),

                            datasets: [
                                {
                                    label: "Promedio",

                                    data: data.map(
                                        item =>
                                            Number(
                                                item.averageGrade
                                            )
                                    )
                                }
                            ]

                        },

                        options: {
                            responsive: true,
                            maintainAspectRatio: false
                        }

                    });

            }

        } catch (error) {

            console.error(
                "Error promedio de notas:",
                error
            );

        }

        try {

            const response =
                await Api.get(
                    "/statistics/attendance-rate"
                );

            const data =
                response.data || [];

            const canvas =
                document.getElementById(
                    "attendanceChart"
                );

            if (
                typeof Chart !== "undefined" &&
                canvas
            ) {

                this.charts.attendance =
                    new Chart(canvas, {

                        type: "line",

                        data: {

                            labels: data.map(
                                item =>
                                    item.courseName
                            ),

                            datasets: [
                                {
                                    label: "Asistencia %",

                                    data: data.map(
                                        item =>
                                            Number(
                                                item.attendanceRate
                                            )
                                    )
                                }
                            ]

                        },

                        options: {
                            responsive: true,
                            maintainAspectRatio: false
                        }

                    });

            }

        } catch (error) {

            console.error(
                "Error asistencia:",
                error
            );

        }

    }

};