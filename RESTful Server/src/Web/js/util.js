/**
 * Created by victor on 11/6/2015.
 * Contains utility functions.
 */
function makeStruct(attributes) { // a general factory for structs
    var fields = attributes.split(' ');
    var count = fields.length;
    function constructor() {
        for (var i = 0; i < count; i++) {
            this[fields[i]] = arguments[i];
        }
    }
    return constructor;
}

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
        // MSIE
        result = new ActiveXObject("Microsoft.XMLHTTP");
    }
    else {
        alert("Invalid Browser!");
    }
    return result;
}

function sendPOSTRequest(path, data){
    var req = createRequest();
    req.open("POST", Paths.ROOT + path, true);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(data);
    alert("Sent\n" + data)
}

