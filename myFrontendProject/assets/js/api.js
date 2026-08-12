window.API_URL = (window.CONFIG && window.CONFIG.API_URL) || "http://localhost:8080";

window.Api = {

    async request(endpoint, options = {}) {

        const token = localStorage.getItem("accessToken");

        const headers = {
            "Content-Type": "application/json",
            "Accept": "application/json"
        };

        if (options.headers) {
            Object.assign(headers, options.headers);
        }

        if (token) {
            headers["Authorization"] = `Bearer ${token}`;
        }

        console.log(
            "API REQUEST:",
            `${window.API_URL}${endpoint}`
        );

        const response = await fetch(
            `${window.API_URL}${endpoint}`,
            {
                ...options,
                headers: headers
            }
        );

        let data = {};

        try {
            data = await response.json();
        } catch (error) {
            console.warn(
                "La respuesta no contiene JSON"
            );
        }

        console.log(
            "API RESPONSE:",
            response.status,
            data
        );

        if (!response.ok) {

            if (response.status === 401) {

                const backendMessage = data.message || "";

                const isSessionError =
                    backendMessage
                        .toLowerCase()
                        .includes("no autenticado") ||
                    backendMessage
                        .toLowerCase()
                        .includes("token") ||
                    !backendMessage;

                if (isSessionError) {

                    localStorage.removeItem("accessToken");

                    throw new Error(
                        "No autenticado. Inicia sesión nuevamente."
                    );
                }
                throw new Error(backendMessage);
            }

            if (response.status === 403) {

                throw new Error(
                    "No tienes permisos para realizar esta acción."
                );
            }

            throw new Error(
                data.message ||
                `Error HTTP ${response.status}`
            );
        }

        return data;
    },


    async get(endpoint) {

        return this.request(
            endpoint,
            {
                method: "GET"
            }
        );

    },


    async post(endpoint, body) {

        return this.request(
            endpoint,
            {
                method: "POST",
                body: JSON.stringify(body)
            }
        );

    },


    async put(endpoint, body) {

        return this.request(
            endpoint,
            {
                method: "PUT",
                body: JSON.stringify(body)
            }
        );

    },


    async delete(endpoint) {

        return this.request(
            endpoint,
            {
                method: "DELETE"
            }
        );

    }

};