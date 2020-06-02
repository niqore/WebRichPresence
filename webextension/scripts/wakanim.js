var interval = 1;
var connection = new WebSocket('ws://127.0.0.1:54917');

function send_data(title, episode, progressTime, totalTime, playing) {
    connection.send('b{"title":"' + title + '","episode":"' + episode + '","progressTime":' + progressTime + ',"totalTime":' + totalTime + ',"playing":' + playing + '}');
}

function send_no_video() {
    connection.send('b{"title":"none"}');
}

function poll_activity() {

    if (document.getElementsByClassName("jw-video jw-reset")[0] == null) {
        send_no_video();
        return;
    }

    var currentTime = Math.floor(document.getElementsByClassName("jw-video jw-reset")[0].currentTime),
        duration =  Math.floor(document.getElementsByClassName("jw-video jw-reset")[0].duration);

    if (isNaN(currentTime) || isNaN(duration)) {
        return;
    }

    var saison = document.getElementsByClassName("episode_h1")[0].children[0].children[1].innerHTML,
        title = document.getElementsByClassName("episode_h1")[0].children[1].innerHTML + " " + saison,
        numEpisode = document.getElementsByClassName("episode_h1")[0].children[0].children[0].children[0].innerHTML,
        episode = "Épisode " + numEpisode,
        playing = !document.getElementsByClassName("jw-video jw-reset")[0].paused;

    if (!title || !episode) {
        send_no_video();
        return;
    }

    send_data(title, episode, currentTime, duration, playing);
}

connection.onopen = function () {
    connection.send('awakanim')
    poll_activity();
    setInterval(poll_activity, interval * 1000);
};

connection.onerror = function (error) {
};

connection.onmessage = function (e) {
};