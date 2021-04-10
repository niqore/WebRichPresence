var interval = 1;

function poll_activity() {

    if (document.getElementsByTagName("video")[0] == null) {
        send_no_video();
        return;
    }

    var currentTime = Math.floor(document.getElementsByTagName("video")[0].currentTime),
        duration =  Math.floor(document.getElementsByTagName("video")[0].duration);

    if (isNaN(currentTime) || isNaN(duration)) {
        return;
    }

    var title = document.getElementsByTagName("h1")[0].children[1].innerHTML,
        episode = document.getElementsByTagName("h1")[0].children[0].innerHTML.trim(),
        playing = !document.getElementsByTagName("video")[0].paused;

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