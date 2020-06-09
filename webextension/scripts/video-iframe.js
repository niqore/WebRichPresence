var interval = 1;
document.vTitle = "???";
document.episode = "???";
document.module = "";
document.moduleConnected = false;

window.addEventListener('message', event => {
	console.log(event);
	if (event.origin === get_website_origin()) {
		return;
	}
	if (event.data.module != undefined) {
		document.vTitle = event.data.title;
		document.episode = event.data.episode;
		document.module = event.data.module;
		console.log("Module: " + document.module + " received in " + get_website_origin());
	}
});

function connect_module() {
	if (document.module !== "" && connection.readyState == 1) {
		console.log("Module connection: " + get_website_origin());
		connection.send('a' + document.module);
		document.moduleConnected = true;
	}
}

function poll_activity() {
	
	let videoObj = get_video_obj();

    if (videoObj == null) {
        return;
    }

    var currentTime = Math.floor(videoObj.currentTime),
        duration =  Math.floor(videoObj.duration);

    if (isNaN(currentTime) || isNaN(duration)) {
        return;
    }
	
	if (!document.moduleConnected) {
		connect_module();
	}

    var playing = !videoObj.paused;

    send_data(document.vTitle, document.episode, currentTime, duration, playing);
}

poll_activity();
setInterval(poll_activity, interval * 1000);