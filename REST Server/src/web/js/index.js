/**
 * Created by Gila-Ber on 07/11/2015.
 *
 */
var numOfAttributes = 0;

function collectAttributeValues(){
    if (numOfAttributes == 0) {
        return "";
    }
    var result = [];
    var elements = document.getElementById("div_attributes").children;
    for(var i = 0; i < elements.length; i++) {
        if(elements[i].tagName == 'INPUT') {
            result.push(elements[i].value);
        }
    }
    return result;
}

function clearDiv(divName){
    document.getElementById(divName).innerHTML ="";
}

function submit(){
    var entityName = document.getElementById("in_entityName").value;
    var attributes = collectAttributeValues().join(" ");
    var newEntity = {
        name: entityName,
        attributes: attributes
    };

    sendPOSTRequest(Paths.CREATE_ENTITY, JSON.stringify(newEntity));
    clearDiv("div_createEntity");
    document.getElementById("btn_createEntity").disabled = false;
    numOfAttributes = 0;
    document.getElementById("div_message").innerHTML = "<span style=\"color: #00cc00;\">Added new object.</span>"
}


