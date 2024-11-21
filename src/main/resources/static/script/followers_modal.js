function openFollowersModal(tab) {
    document.getElementById("followersFollowingModal").style.display = "flex";
    if (tab === 'followers') {
        document.getElementById("defaultOpen").click();
    } else if (tab === 'following') {
        document.querySelector('.tablinks:nth-child(2)').click();
    } else if (tab === 'search') {
        document.querySelector('.tablinks:nth-child(3)').click(); // Вкладка поиска
    }
}

function switchTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
}

function toggleModal() {
    document.getElementById("followersFollowingModal").style.display = "none";
}

function closeModalOutside(event) {
    if (event.target === document.getElementById("followersFollowingModal")) {
        toggleModal();
    }
}


// Функция для отправки запроса на сервер и отображения результатов
//search
document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const searchQuery = document.getElementById('searchQuery');
    const searchResults = document.getElementById('searchResults');

    searchForm.addEventListener('submit', function(event) {
        event.preventDefault();
        const query = searchQuery.value.trim();

        if (query === '') {
            searchResults.innerHTML = '';
            return;
        }

        fetch(`/search?query=${encodeURIComponent(query)}`)
            .then(response => response.text())
            .then(html => {
                // Обновляем содержимое результатов поиска
                searchResults.innerHTML = html;
            })
            .catch(error => {
                console.error('Ошибка поиска:', error);
                searchResults.innerHTML = '<li>Ошибка при поиске</li>';
            });
    });
});

// Устанавливаем вкладку по умолчанию
document.getElementById("defaultOpen").click();


// Подписаться на пользователя
function followUser(button) {
    const userId = $(button).data('user-id'); // Получаем ID пользователя из атрибута data-user-id
    console.log("Подписываемся на пользователя с ID:", userId);

    $.ajax({
        url: '/' + userId + '/follow', // Используем правильный userId в URL
        type: 'POST',
        success: function(response) {
            // Обновляем кнопку после успешной подписки
            $(button).removeClass('btn-follow').addClass('btn-following').text('Following');
            $(button).attr('onclick', 'unfollowUser(this)');
        },
        error: function() {
            alert('Ошибка при подписке на пользователя: ' + userId); // Используем полученный userId
        }
    });
}

// Отписаться от пользователя
function unfollowUser(button) {
    const userId = $(button).data('user-id'); // Получаем ID пользователя из атрибута data-user-id
    console.log("Отписываемся от пользователя с ID:", userId);

    $.ajax({
        url: '/' + userId + '/unfollow', // Используем правильный userId в URL
        type: 'POST',
        success: function(response) {
            // Обновляем кнопку после успешной отписки
            $(button).removeClass('btn-following').addClass('btn-follow').text('Follow');
            $(button).attr('onclick', 'followUser(this)');
        },
        error: function() {
            alert('Ошибка при отписке от пользователя: ' + userId); // Используем полученный userId
        }
    });
}

