/**
 * Created by Gila-Ber on 07/11/2015.
 */
var numOfAttributes = 0;

function buildAttributeInputFields(list){
    var tags = "";
    for (i=0; i<list.length; i++){
        tags += "<br>(" + i + ") <input id=\"att" + i + "\" value=\"" + list[i] + "\">"
    }
    return tags;
}

function collectAttributeValues(){
    if (numOfAttributes == 0) {
        return "";
    }

    var result = [];
    var elements = document.getElementById("attributesDiv").children;
    for(var i = 0; i < elements.length; i++) {
        if(elements[i].tagName == 'INPUT') {
            result.push(elements[i].value);
        }
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
    var entityName = document.getElementById("entityName").value;
    var attributes = collectAttributeValues();
    alert("Entity Name : " + entityName + "\nAttributes : " + attributes + "\nNumber Of Attributes : " + attributes.length);
}