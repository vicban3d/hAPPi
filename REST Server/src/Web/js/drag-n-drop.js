/**
 * Created by victor on 1/2/2016.
 *
 */


var clone_index=0;
function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    //noinspection JSUnresolvedVariable
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
    ev.preventDefault();
    //noinspection JSUnresolvedVariable
    var data = ev.dataTransfer.getData("text");
    var original = document.getElementById(data);
    var copy;
    if (original.getAttribute("id").indexOf("_clone") > -1){
        copy = original;
    } else {
        copy = original.cloneNode(true);
        copy.setAttribute("id", original.getAttribute("id") + clone_index + "_clone");
        clone_index += 1;
    }
    ev.target.appendChild(copy);
}

function dropDelete(ev) {
    ev.preventDefault();
    //noinspection JSUnresolvedVariable
    var data = ev.dataTransfer.getData("text");
    var original = document.getElementById(data);
    if (original.getAttribute("id").indexOf("_clone") > -1) {
        document.getElementById(data).remove();
        clone_index -= 1;
    }
}