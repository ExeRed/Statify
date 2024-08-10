const words = document.querySelectorAll('.hero-text span');

words.forEach(word => {
    word.addEventListener('mouseover', () => {
        word.classList.add('highlighted');
    });

    word.addEventListener('mouseout', () => {
        word.classList.remove('highlighted');
    });
});