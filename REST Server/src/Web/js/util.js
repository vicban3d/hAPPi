/**
 * Created by victor on 11/6/2015.
 * Contains utility functions.
 */






// Global Functions
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

function sendPOSTRequestPlainText(path, data) {
    var req = createRequest();
    req.open("POST", Paths.ROOT + path, true);
    req.setRequestHeader("Content-Type", "text/plain");
    req.send(data);
    return req;
}

function sendPOSTRequest(path, data) {
    var req = createRequest();
    req.open("POST", Paths.ROOT + path, true);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(data);
    return req;
}

// function sendGETRequest(path, data){
//     var req = createRequest();
//     req.open("GET", Paths.ROOT + path, true);
//     req.setRequestHeader("Content-Type", "application/json");
//     req.send(data);
//     return req;
// }

function copyArray(array){
    var copy = [];
    for (var i=0; i< array.length; i++){
            copy.push(clone(array[i]));
    }
    return copy;
}

function clone(obj) {
    if (null == obj || "object" != typeof obj) return obj;
    var copy = obj.constructor();
    for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
    }
    return copy;
}

function generateUUID() {
    var d = new Date().getTime();
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
}

function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}
