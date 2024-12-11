function filterCards() {
    const searchInput = document.getElementById("search").value.toLowerCase();
    const cards = document.querySelectorAll(".doctor-card");
    let hasResults = false;

    cards.forEach(card => {
        const nama = card.querySelector(".nama").textContent.toLowerCase();
        const spesialisasi = card.querySelector(".spesialisasi").textContent.toLowerCase();

        if (nama.includes(searchInput) || spesialisasi.includes(searchInput)) {
            card.style.display = "";
            hasResults = true;
        } else {
            card.style.display = "none";
        }
    });

    const noResultsMessage = document.getElementById("no-results");
    if (hasResults) {
        noResultsMessage.style.display = "none";
    } else {
        noResultsMessage.style.display = "block";
    }
}


document.addEventListener('DOMContentLoaded', () => {
    const searchButton = document.getElementById('searchButton');
    searchButton.addEventListener('click', filterCards);
});

