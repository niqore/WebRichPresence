function send_details(iframe, url, title, episode) {
	console.log("Details sent to " + url);
	iframe.contentWindow.postMessage({title: title, episode: episode, module: "mavanimes" }, url);
}

window.onload = function() {
	setInterval(check_details_sent, 1000);
}

var gounlimitedSent = false;
var mystreamSent = false;
var mavplaySent = false;
var title = "";
var ep = "";

function check_details_sent() {
	
	if (title == "") {
		title = document.getElementsByClassName("entry-title")[0].innerText;
	}
	
	let iframes = document.getElementsByTagName('iframe');
	for (let i = 0; i < iframes.length; ++i) {
		let iframe = iframes[i];
		if (iframe.src != undefined) {
			if (iframe.src.includes("gounlimited") && !gounlimitedSent) {
				send_details(iframe, "https://gounlimited.to", title, ep);
				gounlimitedSent = true;
			}
			else if (iframe.src.includes("mystream") && !mystreamSent) {
				send_details(iframe, "https://embed.mystream.to", title, ep);
				mystreamSent = true;
			}
			else if (iframe.src.includes("mavplay") && !mavplaySent) {
				send_details(iframe, "https://mavplay.com", title, ep);
				mavplaySent = true;
			}
		}
	}
}