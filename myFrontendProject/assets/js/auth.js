function pagesBase() {

    const path = window.location.pathname;

    const marker = "/pages/";

    const idx = path.indexOf(marker);

    if (idx !== -1) {
        return path.slice(0, idx + marker.length);
    }

    return "/pages/";

}

window.Auth = {

    token() {

        return localStorage.getItem("accessToken");

    },
    role() {

        return localStorage.getItem("role");

    },

    email() {

        return localStorage.getItem("email");

    },

    logged() {

        const token = this.token();

        return (
            token !== null &&
            token !== ""
        );

    },

    save(data, email) {

        if (!data) {

            console.error(
                "Auth.save(): no se recibió información del login."
            );

            return;

        }


        if (!data.accessToken) {

            console.error(
                "Auth.save(): el backend no devolvió accessToken."
            );

            return;

        }


        localStorage.setItem(
            "accessToken",
            data.accessToken
        );


        localStorage.setItem(
            "tokenType",
            data.tokenType || "Bearer"
        );


        localStorage.setItem(
            "role",
            data.role || ""
        );


        localStorage.setItem(
            "email",
            email || ""
        );


        localStorage.setItem(
            "mustChangePassword",
            String(
                data.mustChangePassword ?? false
            )
        );


        console.log(
            "Sesión guardada correctamente."
        );

    },

    logout() {

        localStorage.removeItem(
            "accessToken"
        );

        localStorage.removeItem(
            "tokenType"
        );

        localStorage.removeItem(
            "role"
        );

        localStorage.removeItem(
            "email"
        );

        localStorage.removeItem(
            "mustChangePassword"
        );

        window.location.href =
            pagesBase() + "login.html";

    },

    require() {

        if (!this.logged()) {

            console.warn(
                "No existe una sesión activa."
            );

            window.location.href =
                pagesBase() + "login.html";

            return false;

        }


        return true;

    },

    pagesBase

};