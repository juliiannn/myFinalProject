(function () {

    Auth.require();

    const key = window.PAGE_KEY;
    const title = window.PAGE_TITLE;
    const role = Auth.role();
    const nav = [

        [
            "dashboard",
            "🏠",
            "Dashboard",
            "dashboard.html",
            "ALL"
        ],

        [
            "students",
            "👨‍🎓",
            "Estudiantes",
            "people/students.html",
            "ADMIN,RECTOR,COORDINATOR"
        ],

        [
            "teachers",
            "👨‍🏫",
            "Docentes",
            "people/teachers.html",
            "ADMIN,RECTOR,COORDINATOR"
        ],

        [
            "coordinators",
            "🧑‍💼",
            "Coordinadores",
            "people/coordinators.html",
            "ADMIN,RECTOR"
        ],

        [
            "rectors",
            "🎓",
            "Rectores",
            "people/rectors.html",
            "ADMIN"
        ],

        [
            "users",
            "👤",
            "Usuarios",
            "admin/users.html",
            "ADMIN"
        ],

        [
            "courses",
            "🏫",
            "Cursos",
            "academic/courses.html",
            "ADMIN,RECTOR,COORDINATOR,TEACHER"
        ],

        [
            "subjects",
            "📚",
            "Materias",
            "academic/subjects.html",
            "ADMIN,RECTOR,COORDINATOR,TEACHER"
        ],

        [
            "course-subjects",
            "🔗",
            "Asignaciones",
            "academic/course-subjects.html",
            "ADMIN,RECTOR,COORDINATOR"
        ],

        [
            "enrollments",
            "📝",
            "Matrículas",
            "academic/enrollments.html",
            "ADMIN,RECTOR,COORDINATOR,TEACHER"
        ],

        [
            "grades",
            "📊",
            "Notas",
            "academic/grades.html",
            "ADMIN,RECTOR,COORDINATOR,TEACHER,STUDENT"
        ],

        [
            "attendance",
            "📅",
            "Asistencia",
            "academic/attendance.html",
            "ADMIN,RECTOR,COORDINATOR,TEACHER,STUDENT"
        ],

        [
            "assignments",
            "📌",
            "Actividades",
            "academic/assignments.html",
            "ADMIN,RECTOR,COORDINATOR,TEACHER,STUDENT"
        ],

        [
            "schedules",
            "🗓️",
            "Horarios",
            "academic/schedules.html",
            "ADMIN,RECTOR,COORDINATOR,TEACHER,STUDENT"
        ],

        [
            "student-profile",
            "🙋",
            "Mi perfil",
            "student/profile.html",
            "STUDENT"
        ],

        [
            "student-grades",
            "📈",
            "Mis notas",
            "student/grades.html",
            "STUDENT"
        ],

        [
            "student-attendance",
            "📅",
            "Mi asistencia",
            "student/attendance.html",
            "STUDENT"
        ],

        [
            "student-schedule",
            "🗓️",
            "Mi horario",
            "student/schedule.html",
            "STUDENT"
        ],

        [
            "student-assignments",
            "📌",
            "Mis actividades",
            "student/assignments.html",
            "STUDENT"
        ],

        [
            "student-report",
            "📄",
            "Mi boletín",
            "student/report-card.html",
            "STUDENT"
        ],

        [
            "teacher-profile",
            "🙋",
            "Mi perfil",
            "teacher/profile.html",
            "TEACHER"
        ],

        [
            "teacher-schedule",
            "🗓️",
            "Mi horario",
            "teacher/schedule.html",
            "TEACHER"
        ]

    ];

    const visible = nav.filter(item => {

        const roles = item[4].split(",");

        return roles.includes("ALL") ||
               roles.includes(role);

    });

    const groups = [

        [
            "Principal",

            visible.filter(item =>
                ["dashboard"].includes(item[0])
            )
        ],

        [
            "Personas",

            visible.filter(item =>
                [
                    "students",
                    "teachers",
                    "coordinators",
                    "rectors",
                    "users"
                ].includes(item[0])
            )
        ],

        [
            "Académico",

            visible.filter(item =>
                [
                    "courses",
                    "subjects",
                    "course-subjects",
                    "enrollments",
                    "grades",
                    "attendance",
                    "assignments",
                    "schedules"
                ].includes(item[0])
            )
        ],

        [
            "Mi espacio",

            visible.filter(item =>
                item[0].startsWith("student-") ||
                item[0].startsWith("teacher-")
            )
        ]

    ];

    const side = groups

        .filter(group => group[1].length > 0)

        .map(group => {

            const titleHtml =
                `<div class="nav-title">${group[0]}</div>`;

            const linksHtml = group[1]

                .map(item => {

                    const active =
                        key === item[0]
                            ? "active"
                            : "";

                    return `
                        <a
                            class="nav-link ${active}"
                            href="${Auth.pagesBase()}${item[3]}"
                        >
                            <span>${item[1]}</span>
                            <span class="nav-text">
                                ${item[2]}
                            </span>
                        </a>
                    `;

                })

                .join("");

            return titleHtml + linksHtml;

        })

        .join("");

    let email = "";

    try {

        email = Auth.email() || "";

    } catch (error) {

        console.warn(
            "No se pudo obtener el correo del usuario."
        );

    }

    const app =
        document.getElementById("app");


    if (!app) {

        console.error(
            "No se encontró el elemento #app en el HTML."
        );

        return;

    }


    app.innerHTML = `

        <div class="app-shell">

            <aside class="sidebar">

                <div class="brand">

                    <span class="brand-mark">
                        SA
                    </span>

                    <span>
                        Sistema Académico
                    </span>

                </div>


                ${side}


                <a
                    id="logout"
                    class="nav-link"
                    href="#"
                >

                    <span>🚪</span>

                    <span class="nav-text">
                        Cerrar sesión
                    </span>

                </a>

            </aside>


            <main class="main">

                <header class="topbar">

                    <div>

                        <h1>
                            ${UI.esc(title || "Sistema Académico")}
                        </h1>

                        <span class="muted">
                            Panel de gestión académica
                        </span>

                    </div>


                    <div class="user-chip">

                        ${UI.esc(email)}

                        ·

                        <strong>
                            ${UI.esc(role || "")}
                        </strong>

                    </div>

                </header>


                <section id="pageContent">

                </section>

            </main>

        </div>

    `;

    const logoutButton =
        document.getElementById("logout");


    if (logoutButton) {

        logoutButton.addEventListener(
            "click",
            function (event) {

                event.preventDefault();

                if (
                    typeof Auth.logout === "function"
                ) {

                    Auth.logout();

                } else {

                    
                    localStorage.removeItem(
                        "accessToken"
                    );

                    localStorage.removeItem(
                        "token"
                    );

                    localStorage.removeItem(
                        "role"
                    );

                    localStorage.removeItem(
                        "email"
                    );

                    window.location.href =
                        Auth.pagesBase() + "login.html";

                }

            }
        );

    }

})();