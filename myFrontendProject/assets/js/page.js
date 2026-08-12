document.addEventListener("DOMContentLoaded", () => {
    if (typeof Module === "undefined") {
        console.error("Module.js no está cargado.");
        return;
    }

    if (!window.PAGE_KEY) {
        console.error("PAGE_KEY no está definido.");
        return;
    }

    Module.render(window.PAGE_KEY);
});