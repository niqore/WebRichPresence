var s = document.createElement('script');
s.src = chrome.extension.getURL('scripts/websites/crunchyroll/crunchyroll-script.js');
(document.head||document.documentElement).appendChild(s);
s.onload = function() {
    s.remove();
};

var interval = 1;
var isVideo, currentTime, duration, playing;

document.addEventListener('RW759_connectExtension', function(e) {
    isVideo = e.detail[0];
    currentTime = Math.floor(e.detail[1]);
    duration = Math.floor(e.detail[2]);
    playing = e.detail[3];
});

function poll_activity() {

    if (!isVideo) {
        send_no_video();
        return;
    }

    if (isNaN(currentTime) || isNaN(duration)) {
        return;
    }

    var title = document.querySelectorAll('h1.ellipsis')[0].childNodes[1].innerText.trim(),
        episode = document.querySelectorAll('h1.ellipsis')[0].childNodes[4].textContent.trim().replace('\n', '').replace(/ +(?= )/g,'');

    if (!title || !episode) {
        send_no_video();
        return;
    }

    send_data(title, episode, currentTime, duration, playing);
}

connection.onopen = function () {
    connection.send('acrunchyroll')
    poll_activity();
    setInterval(poll_activity, interval * 1000);
};