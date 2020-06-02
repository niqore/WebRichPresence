var interval = 1;
var connection = new WebSocket('ws://127.0.0.1:54917');

function send_data(title, episode, progressTime, totalTime, playing) {
    connection.send('b{"title":"' + title + '","episode":"' + episode + '","progressTime":' + progressTime + ',"totalTime":' + totalTime + ',"playing":' + playing + '}');
}

function send_no_video() {
    connection.send('b{"title":"none"}');
}

function poll_activity() {

    if (document.getElementById("adn-video-js_html5_api") == null) {
        send_no_video();
        return;
    }

    var currentTime = Math.floor(document.getElementById("adn-video-js_html5_api").currentTime),
        duration =  Math.floor(document.getElementById("adn-video-js_html5_api").duration);

    if (isNaN(currentTime) || isNaN(duration)) {
        return;
    }

    var title = document.getElementsByClassName("adn-player-header")[0].children[0].children[0].innerHTML,
        episode = document.getElementsByClassName("adn-player-header")[0].children[0].children[1].innerHTML.trim(),
        playing = !document.getElementById("adn-video-js_html5_api").paused;

    if (!title || !episode) {
        send_no_video();
        return;
    }

    send_data(title, episode, currentTime, duration, playing);
}

connection.onopen = function () {
    connection.send('aadn')
    poll_activity();
    setInterval(poll_activity, interval * 1000);
};

connection.onerror = function (error) {
};

connection.onmessage = function (e) {
};