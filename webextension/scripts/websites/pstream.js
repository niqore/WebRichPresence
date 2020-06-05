var interval = 1;
document.vTitle = "???";
document.episode = "???";
document.module = "";

window.addEventListener('message', event => {
    document.vTitle = event.data.title;
	document.episode = event.data.episode;
	document.module = event.data.module;
	connect_module();
});

function connect_module() {
	if (document.module !== "" && connection.readyState == 1) {
		connection.send('a' + document.module);
		poll_activity();
		setInterval(poll_activity, interval * 1000);
	}
}

function poll_activity() {

    if (document.getElementById("video_player_html5_api") == null) {
        return;
    }

    var currentTime = Math.floor(document.getElementById("video_player_html5_api").currentTime),
        duration =  Math.floor(document.getElementById("video_player_html5_api").duration);

    if (isNaN(currentTime) || isNaN(duration)) {
        return;
    }

    var playing = !document.getElementById("video_player_html5_api").paused;

    send_data(document.vTitle, document.episode, currentTime, duration, playing);
}

connection.onopen = function () {
	connect_module();
};