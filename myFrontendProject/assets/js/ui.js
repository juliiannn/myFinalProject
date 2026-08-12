window.UI = {

    esc(value) {
        return String(value ?? "").replace(/[&<>"']/g, character => ({
            "&": "&amp;",
            "<": "&lt;",
            ">": "&gt;",
            '"': "&quot;",
            "'": "&#039;"
        }[character]));
    },

    date(value) {
        if (!value) {
            return "";
        }

        return new Date(
            value + "T00:00:00"
        ).toLocaleDateString("es-CO");
    },

    toast(message, type = "success") {

        let element = document.getElementById("toast");

        if (element) {
            element.remove();
        }

        element = document.createElement("div");

        element.id = "toast";

        element.style.cssText = `
            position: fixed;
            right: 20px;
            bottom: 20px;
            z-index: 9999;
            color: white;
            padding: 13px 17px;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            font-family: Arial, sans-serif;
            font-size: 14px;
            max-width: 350px;
        `;

        element.style.background =
            type === "error"
                ? "#991b1b"
                : "#166534";

        element.textContent = message;

        document.body.appendChild(element);

        setTimeout(() => {

            if (element && element.parentNode) {
                element.remove();
            }

        }, 3000);
    },

    modal(title, body) {

        const oldModal =
            document.getElementById("genericModal");

        if (oldModal) {
            oldModal.remove();
        }

        const modal =
            document.createElement("div");

        modal.id = "genericModal";

        modal.className = "modal open";

        modal.innerHTML = `
            <div class="modal-card">

                <div class="modal-head">

                    <h2>
                        ${this.esc(title)}
                    </h2>

                    <button
                        type="button"
                        class="close"
                        onclick="this.closest('.modal').remove()"
                    >
                        ✕
                    </button>

                </div>

                ${body}

            </div>
        `;

        document.body.appendChild(modal);

        return modal;
    }

};