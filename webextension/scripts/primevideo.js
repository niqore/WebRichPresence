var interval = 1;
var connection = new WebSocket('ws://127.0.0.1:54917');

function send_data(title, episode, progressTime, totalTime, playing) {
    connection.send('b{"title":"' + title + '","episode":"' + episode + '","progressTime":' + progressTime + ',"totalTime":' + totalTime + ',"playing":' + playing + '}');
}

function send_no_video() {
    connection.send('b{"title":"none"}');
}

function poll_activity() {

    if (document.getElementsByClassName('scalingVideoContainerBottom')[0] == undefined || document.getElementsByClassName('scalingVideoContainerBottom')[0].children[0].children[1] == undefined) {
        send_no_video();
        return;
    }

    var currentTime = Math.floor(document.getElementsByClassName('scalingVideoContainerBottom')[0].children[0].children[1].currentTime),
        duration =  Math.floor(document.getElementsByClassName('scalingVideoContainerBottom')[0].children[0].children[1].duration);

    if (isNaN(currentTime) || isNaN(duration)) {
        return;
    }

    var title = document.getElementsByClassName('fgzdi7m f10ip5t1 fs89ngr')[0].innerHTML,
        episode = document.getElementsByClassName('f15586js f1iodedr fdm7v fs89ngr')[0].innerHTML,
        playing = !document.getElementsByClassName('scalingVideoContainerBottom')[0].children[0].children[1].paused;

    if (!title || !episode) {
        send_no_video();
        return;
    }

    send_data(title, episode, currentTime, duration, playing);
}

connection.onopen = function () {
    connection.send('aprimevideo')
    poll_activity();
    setInterval(poll_activity, interval * 1000);
};

connection.onerror = function (error) {
};

connection.onmessage = function (e) {
};