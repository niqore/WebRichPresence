var interval = 1;

function poll_activity() {

    if (window.location.href.includes("www.netflix.com/browse")) {
        send_no_video();
        return;
    }

    if (document.querySelectorAll('video')[0] == null) {
        send_no_video();
        return;
    }

    var currentTime = Math.floor(document.querySelectorAll('video')[0].currentTime),
        duration =  Math.floor(document.querySelectorAll('video')[0].duration);

    if (isNaN(currentTime) || isNaN(duration)) {
        return;
    }

    var title = document.getElementsByClassName("ellipsize-text")[0].children[0].innerHTML,
        numEpisode = document.getElementsByClassName("ellipsize-text")[0].children[1].innerHTML,
        titleEpisode = document.getElementsByClassName("ellipsize-text")[0].children[2].innerHTML,
        episode = numEpisode + " " + titleEpisode,
        playing = !document.querySelectorAll('video')[0].paused;

    if (!title || !episode) {
        send_no_video();
        return;
    }

    send_data(title, episode, currentTime, duration, playing);
}

connection.onopen = function () {
    connection.send('anetflix')
    poll_activity();
    setInterval(poll_activity, interval * 1000);
};