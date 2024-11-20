(function() {
    const homePage = document.querySelector("homeButton");
    homePage.addEventListener('mouseclick', (event) => {
        location.href = "/"
    })

    const loginButton = document.querySelector("login");
    loginButton.addEventListener('mouseclick', (event) => {
        location.href = "redirect:/index.html"
    })
})();