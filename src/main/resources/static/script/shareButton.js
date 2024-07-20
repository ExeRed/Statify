function openModal() {
    document.getElementById('imageModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('imageModal').style.display = 'none';
}

window.onclick = function(event) {
    const modal = document.getElementById('imageModal');
    if (event.target === modal) {
        closeModal();
    }
}

async function shareImage() {
    const imageElement = document.getElementById('topTracksImage');

    // Check if browser supports Web Share API
    if (!navigator.canShare) {
        alert('Sharing is not supported by your browser.');
        return;
    }

    // Create a Blob object from the image data
    const imageBlob = await new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', imageElement.src);
        xhr.responseType = 'blob';
        xhr.onload = () => resolve(xhr.response);
        xhr.onerror = reject;
        xhr.send();
    });

    // Share data object with image
    const data = {
        files: [new File([imageBlob], 'top_tracks.png', { type: 'image/png' })],
        title: 'Spotify Stats', // Optional title
        text: 'Check out my stats!' // Optional description
    };

    try {
        await navigator.share(data);
        console.log('Image shared successfully');
    } catch (error) {
        console.error('Sharing failed:', error);
    }
}