function get_website_origin() {
	return "https://gounlimited.to";
}

function get_video_obj() {
	let vid = document.getElementById("vjsplayer_html5_api");
	if (vid.currentTime == 0) {
		return null;
	}
	return vid;
}