var interval = 1;

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

    var saison = "";
    if (document.getElementsByClassName("episode_h1")[0].children[0].children[1] != undefined) {
        saison += " " + document.getElementsByClassName("episode_h1")[0].children[0].children[1].innerHTML;
    }

    var title = document.getElementsByClassName("episode_h1")[0].children[1].innerHTML + saison,
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