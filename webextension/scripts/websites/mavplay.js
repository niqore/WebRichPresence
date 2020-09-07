function get_website_origin() {
	return "https://mavanimes.com";
}

function get_video_obj() {
	let vid = document.getElementsByTagName("video");
	if (vid.length == 0) {
		return null;
	}
	return vid[0];
}