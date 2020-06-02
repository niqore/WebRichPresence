var s = document.createElement('script');
s.src = chrome.extension.getURL('crunchyroll-script.js');
(document.head||document.documentElement).appendChild(s);
s.onload = function() {
    s.remove();
};

var interval = 1;
var connection = new WebSocket('ws://127.0.0.1:54917');
var isVideo, currentTime, duration, playing;

document.addEventListener('RW759_connectExtension', function(e) {
    isVideo = e.detail[0];
    currentTime = Math.floor(e.detail[1]);
    duration = Math.floor(e.detail[2]);
    playing = e.detail[3];
});

function send_data(title, episode, progressTime, totalTime, playing) {
    connection.send('b{"title":"' + title + '","episode":"' + episode + '","progressTime":' + progressTime + ',"totalTime":' + totalTime + ',"playing":' + playing + '}');
}

function send_no_video() {
    connection.send('b{"title":"none"}');
}

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

connection.onerror = function (error) {
};

connection.onmessage = function (e) {
};