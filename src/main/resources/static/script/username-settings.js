document.addEventListener("DOMContentLoaded", function () {
    const usernameInput = document.getElementById("usernameInput");
    const editButton = document.getElementById("editButton");
    const saveButton = document.getElementById("saveButton");

    // Делаем поле редактируемым при нажатии на кнопку "Edit"
    editButton.addEventListener("click", function () {
        usernameInput.removeAttribute("readonly");
        usernameInput.focus();
        editButton.style.display = "none"; // Скрываем кнопку "Edit"
        saveButton.style.display = "inline-block"; // Показываем кнопку "Save"
    });

    // Сохраняем изменения при нажатии на кнопку "Save"
    saveButton.addEventListener("click", function () {
        usernameInput.setAttribute("readonly", true);
        saveButton.style.display = "none"; // Скрываем кнопку "Save"
        editButton.style.display = "inline-block"; // Показываем кнопку "Edit"
    });
});