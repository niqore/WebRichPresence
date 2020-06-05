function send_details(iframe, url) {
	let ep = document.getElementsByClassName("details")[0].children[0].children[1].innerText;
	iframe.contentWindow.postMessage({title: document.getElementsByClassName("details")[0].children[0].children[0].innerText, episode: ep[0] + ep.substring(1).toLowerCase(), module: "nekosama" }, url);
	iframe.details_sent = true;
}

function check_details_sent() {
	let iframe = document.getElementById('un_episode');
	if (iframe && !iframe.details_sent) {
		if (iframe.src.includes("pstream")) {
			send_details(iframe, 'https://www.pstream.net');
		}
		else if (iframe.src.includes("mystream")) {
			send_details(iframe, 'https://embed.mystream.to');
		}
	}
}

setInterval(check_details_sent, 1000);