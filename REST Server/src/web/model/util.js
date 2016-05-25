/**
 * Created by Gila on 04/05/2016.
 */

function sendPOSTRequestPlainText(path, data) {
    var req = createRequest();
    req.open("POST", Paths.ROOT + path, true);
    req.setRequestHeader("Content-Type", "text/plain");
    req.send(data);
    return req;
}
