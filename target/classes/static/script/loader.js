document.addEventListener("DOMContentLoaded", function() {
    var loader = document.getElementById("loader");

    // Показываем анимацию загрузки
    function showLoader() {
        document.body.classList.add("loading");
        loader.style.display = "flex";
    }

    // Скрываем анимацию загрузки
    function hideLoader() {
        document.body.classList.remove("loading");
        loader.style.display = "none";
    }

    // Показываем анимацию загрузки при загрузке страницы
    showLoader();

    // Скрываем анимацию загрузки после полной загрузки страницы
    window.addEventListener("load", function() {
        hideLoader();
    });

    // Можно также скрывать анимацию вручную, вызвав hideLoader() при необходимости
});