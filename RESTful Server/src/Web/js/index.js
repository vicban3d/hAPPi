function debug(text){
    document.getElementById("debug").innerHTML += "<br><label>" + text + "</label>";
}
var numOfAttributes = 0;

function buildAttributeInputFields(list){
    var tags = "";
    for (i=0; i<list.length; i++){
        var value = document.getElementById(list[i]).value;
        tags+="<br>(" + i + ") <input id=\"" + list[i] + "\" value=\"" + value + "\">"
    }
    return tags;
}

function collectAttributeValues(){
    if (numOfAttributes == 0) {
        return "";
    }
    debug("RESULT: " + numOfAttributes);
    var result = [];
    var content = document.getElementById("attributesDiv").innerHTML;
    debug("CONTENT " + content);
    tags = content.split("\n");
    debug("TAGS " + tags);
    for (i=0; i<tags.length; i++){
        var value = tags[i].split("id=\"")[1];
        value = value.split("\"")[0];
        result[i] = value;
    }

    return result;
}

function createNewEntity(){
    document.getElementById("entityDiv").innerHTML +=
        "<hr>" +
        "Entity Name: " +
        "<input id=\"entityName\">" +
        "<button id=\"addAttribute\" onclick=\"addNewAttribute()\">Add Attribute</button>" +
        "<hr><div id=\"attributesDiv\"></div><hr>" +
        "<br><button id=\"submitButton\" onclick=submit()>Submit</button>";
}
function addNewAttribute(){
    document.getElementById("attributesDiv").innerHTML = buildAttributeInputFields(collectAttributeValues());
    document.getElementById("attributesDiv").innerHTML +=
        "<br>(" +
        numOfAttributes + ") " +
        "<input id=\"attribute" +
        numOfAttributes +
        "\">";
    numOfAttributes++;

}

function submit(){
    alert("Added new entity " + document.getElementById("entityName").value +
        "\nAttributes: " + numOfAttributes);

}
