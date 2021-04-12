var interval = 1;

function poll_activity() {

    if (window.location.href.includes("www.netflix.com/browse")) {
        send_no_video();
        return;
    }

    if (document.getElementsByTagName('video')[0] == null) {
        send_no_video();
        return;
    }

    var currentTime = Math.floor(document.getElementsByTagName('video')[0].currentTime),
        duration =  Math.floor(document.getElementsByTagName('video')[0].duration);

    if (isNaN(currentTime) || isNaN(duration)) {
        return;
    }

    var title, episode;

    if (document.getElementsByClassName("ellipsize-text")[0].children.length === 0) {
        // For movies
        title = document.getElementsByClassName("ellipsize-text")[0].innerHTML;
        episode = "Movie"
    } else {
        // For series
        title = document.getElementsByClassName("ellipsize-text")[0].children[0].innerHTML;
        episode = document.getElementsByClassName("ellipsize-text")[0].children[1].innerHTML;
        if (document.getElementsByClassName("ellipsize-text")[0].children.length > 2) {
            episode += " " + document.getElementsByClassName("ellipsize-text")[0].children[2].innerHTML;
        }
    }
    var playing = !document.getElementsByTagName('video')[0].paused;

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