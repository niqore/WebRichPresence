function send_details(iframe, url, title, episode) {
	console.log("Details sent to " + url);
	iframe.contentWindow.postMessage({title: title, episode: episode, module: "fullanimefr" }, url);
}

window.onload = function() {
	setInterval(check_details_sent, 1000);
}

var gounlimitedSent = false;
var xuploadSent = false;
var mystreamSent = false;
var sendvidSent = false;
var uqloadSent = false;
var title = "";
var ep = "";

function check_details_sent() {
	
	if (title == "") {
		title = document.getElementsByClassName("entry-category")[0].children[0].innerText;
	
		var re = new RegExp("Episode \\s*(\\d+)");
		var m = document.getElementsByClassName("entry-title")[0].innerText.match(re);
		ep = m[0];
	}
	
	let iframes = document.getElementsByTagName('iframe');
	for (let i = 0; i < iframes.length; ++i) {
		let iframe = iframes[i];
		if (iframe.src != undefined) {
			if (iframe.src.includes("gounlimited") && !gounlimitedSent) {
				send_details(iframe, "https://gounlimited.to", title, ep);
				gounlimitedSent = true;
			}
			else if (iframe.src.includes("xupload") && !xuploadSent) {
				send_details(iframe, "https://xupload.ws", title, ep);
				xuploadSent = true;
			}
			else if (iframe.src.includes("mystream") && !mystreamSent) {
				send_details(iframe, "https://embed.mystream.to", title, ep);
				mystreamSent = true;
			}
			else if (iframe.src.includes("sendvid") && !sendvidSent) {
				send_details(iframe, "https://sendvid.com", title, ep);
				sendvidSent = true;
			}
			else if (iframe.src.includes("uqload") && !uqloadSent) {
				send_details(iframe, "https://uqload.com", title, ep);
				uqloadSent = true;
			}
		}
	}
}