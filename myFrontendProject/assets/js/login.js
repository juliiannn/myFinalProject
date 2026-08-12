document.getElementById("loginForm").addEventListener("submit", async function (e) {

    e.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const message = document.getElementById("loginMessage");

    message.textContent = "Iniciando sesión...";
    message.className = "message";

    try {

        const response = await fetch(
            `${API_URL}/auth/login`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            }
        );

        const result = await response.json();

        console.log("LOGIN:", result);

        if (!response.ok) {
            throw new Error(
                result.message || `Error HTTP ${response.status}`
            );
        }

        if (!result.data || !result.data.accessToken) {
            throw new Error("El servidor no devolvió el token.");
        }

        Auth.save(result.data, email);

        if (result.data.mustChangePassword) {

            window.location.href = "change-password.html";

        } else {

            window.location.href = "dashboard.html";

        }

    } catch (error) {

        console.error("ERROR LOGIN:", error);

        message.textContent = error.message;
        message.className = "message error";

    }

});

const forgotPasswordLink =
    document.getElementById("forgotPasswordLink");

if (forgotPasswordLink) {

    forgotPasswordLink.addEventListener("click", function (e) {

        e.preventDefault();

        alert(
            "Este sistema no tiene recuperación automática por " +
            "correo electrónico.\n\n" +
            "Si olvidaste tu contraseña, pide a un administrador " +
            "que la restablezca desde el panel de Usuarios. " +
            "Quedará igual a tu número de documento y se te " +
            "pedirá cambiarla al iniciar sesión."
        );

    });

}