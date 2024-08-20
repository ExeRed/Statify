function toggleDropdown() {
    const dropdownList = document.getElementById('dropdownList');
    dropdownList.classList.toggle('open');
}

document.querySelectorAll('.dropdown-item').forEach(item => {
    item.addEventListener('click', function () {
        const selectedValue = this.getAttribute('data-value');
        const dropdownHeader = document.querySelector('.dropdown-header span:first-child');
        dropdownHeader.textContent = this.textContent;

        // Устанавливаем выбранное значение в скрытом input
        document.getElementById('selectedTimePeriod').value = selectedValue;

        // Автоматически отправляем форму
        document.getElementById('hiddenForm').submit();

        // Закрываем dropdown
        document.getElementById('dropdownList').classList.remove('open');
    });
});

document.addEventListener('click', function (event) {
    const dropdown = document.querySelector('.custom-dropdown');
    if (!dropdown.contains(event.target)) {
        document.getElementById('dropdownList').classList.remove('open');
    }
});