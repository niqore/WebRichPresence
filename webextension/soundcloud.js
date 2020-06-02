var interval = 1;
var connection = new WebSocket('ws://127.0.0.1:54917');

function send_data(title, author, progressTime, totalTime, playing) {
	connection.send('b{"title":"' + title + '","author":"' + author + '","progressTime":' + progressTime + ',"totalTime":' + totalTime + ',"playing":' + playing + '}');
}

function poll_activity() {
    var $title = document.querySelector(".playbackSoundBadge__titleLink"),
		$author = document.querySelector(".playbackSoundBadge__lightLink"),
		$progress = document.querySelector(".playbackTimeline__progressWrapper"),
		$play = document.querySelector(".playControls__play");

	if (!$title || !$progress || !$play || !$author)
		return;

	var pos = parseInt($progress.getAttribute("aria-valuenow"), 10),
		duration = parseInt($progress.getAttribute("aria-valuemax"), 10),
		playing = $play.classList.contains("playing");

	send_data($title.getAttribute("title"), $author.getAttribute("title"), pos, duration, playing);
}

connection.onopen = function () {
	waitForSocketConnection(connection, function(){
		connection.send('asoundcloud');
		setInterval(poll_activity, interval * 1000);
    });
};

connection.onerror = function (error) {
};

connection.onmessage = function (e) {
};

// Make the function wait until the connection is made...
function waitForSocketConnection(socket, callback){
    setTimeout(
        function () {
            if (socket.readyState === 1) {
                if (callback != null){
                    callback();
                }
            } else {
                waitForSocketConnection(socket, callback);
            }

        }, 50); // wait 50 miliseconds for the connection...
}