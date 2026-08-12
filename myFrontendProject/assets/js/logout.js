document.addEventListener("click", function (e) {

    const logoutButton = e.target.closest("#logout");

    if (!logoutButton) {
        return;
    }

    e.preventDefault();

    Auth.logout();

});