document.addEventListener("DOMContentLoaded", () => {
    //Server Color
    const title = document.querySelector('h1');
    title.style.backgroundColor = serverColor;
    console.log("Server Color:", serverColor);


    //Button Color
    const buttons = document.querySelectorAll("button");
    buttons.forEach(button => {
        button.addEventListener("click", () => {
            const color = button.textContent;
            
            title.style.backgroundColor = color;
            window.location.href = `/?color=${color}`;
        });
    });
});