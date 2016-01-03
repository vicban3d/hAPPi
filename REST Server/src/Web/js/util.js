/**
 * Created by victor on 11/6/2015.
 * Contains utility functions.
 */
function createRequest() {
    var result = null;
    if (window.XMLHttpRequest) {
        // FireFox, Safari, etc.
        result = new XMLHttpRequest();
        if (typeof result.overrideMimeType != 'undefined') {
            result.overrideMimeType('text/xml'); // Or anything else
        }
    }
    else if (window.ActiveXObject) {
        // Internet Explorer
        result = new ActiveXObject("Microsoft.XMLHTTP");
    }
    else {
        alert("Invalid Browser!");
    }
    return result;
}

function sendPOSTRequest(path, data) {
    var req = createRequest();
    req.open("POST", Paths.ROOT + path, true);
    req.setRequestHeader("Content-Type", "text/plain");
    req.send(data);
    return req;
}

