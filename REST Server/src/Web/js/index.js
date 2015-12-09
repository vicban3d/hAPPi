/**
 * Created by Gila-Ber on 07/11/2015.
 *
 */
var numOfAttributes = 0;
var currentApp = "";

function buildAttributeInputFields(list){
    var tags = "";
    for (var i=0; i<list.length; i++){
        tags += "<br>(" + i + ") <input id=\"in_attribute" + i + "\" value=\"" + list[i] + "\">"
    }
    return tags;
}

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

function createNewEntity(){
    clearDiv("div_message");
    document.getElementById("div_createEntity").innerHTML +=
        "<hr>" +
        "Entity Name: " +
        "<input id=\"in_entityName\">" +
        "<button id=\"btn_addAttribute\" onclick=\"addNewAttribute()\">Add Attribute</button>" +
        "<hr><div id=\"div_attributes\"></div><hr>" +
        "<br><button id=\"btn_submit\" onclick=submit()>Submit</button>";
    document.getElementById("btn_createEntity").disabled = true;
}

function addNewAttribute(){
    document.getElementById("div_attributes").innerHTML = buildAttributeInputFields(collectAttributeValues());
    document.getElementById("div_attributes").innerHTML +=
        "<br>(" +
        numOfAttributes + ") " +
        "<input id=\"in_attribute" +
        numOfAttributes +
        "\">";
    numOfAttributes++;
}

function openNewApplicationForm(){
    document.getElementById("div_createApplication").innerHTML =
    "<hr>" +
    "Application Name: " +
    "<input id=\"in_applicationName\">" +
    "<br><button id=\"btn_create\" onclick=createApplication()>Create</button>";
}
function createApplication(){
    document.getElementById("div_message").innerHTML = "<span style=\"color: #b86102; \">Creating application...</span>";
    var applicationName = document.getElementById("in_applicationName").value;
    var newApplication = {
        name: applicationName
    };
    currentApp = applicationName;
    var result = sendPOSTRequest(Paths.CREATE_APP, JSON.stringify(newApplication));
    clearDiv("div_createApplication");
    setResponseText(result);
}

function setResponseText(result){
    result.onreadystatechange = function(){
        if (result.readyState != 4)
            writeMessage("<span style=\"color: red;\">Error</span>");
        if (result.status != 200)
            writeMessage("<span style=\"color: red;\">Error</span>");
        else
            writeMessage("<span style=\"color: #00cc00;\">" + result.responseText + "</span>");
    }
}

function writeMessage(message){
    document.getElementById("div_message").innerHTML = message;
}

function clearDiv(divName){
    document.getElementById(divName).innerHTML ="";
}

function addAndroid(){
    document.getElementById("div_message").innerHTML = "<span style=\"color: #b86102; \">Adding Android platform...</span>";
    var result = sendPOSTRequest(Paths.ADD_PLATFORM_ANDROID);
    setResponseText(result);
}

function addIOS(){
    document.getElementById("div_message").innerHTML = "<span style=\"color: #b86102; \">Adding iOS platform...</span>";
    var result = sendPOSTRequest(Paths.ADD_PLATFORM_IOS);
    setResponseText(result);
}

function addWindowsPhone(){
    document.getElementById("div_message").innerHTML = "<span style=\"color: #b86102; \">Adding wp8 platform...</span>";
    var result = sendPOSTRequest(Paths.ADD_PLATFORM_WINDOWS_PHONE);
    setResponseText(result);
}

function buildApplication(){
    document.getElementById("div_message").innerHTML = "<span style=\"color: #b86102; \">Building application...</span>";
    var result = sendPOSTRequest(Paths.BUILD_APP);
    setResponseText(result);
}

function emulateAndroid(){
    var result = sendPOSTRequest(Paths.ADD_PLATFORM_WINDOWS_PHONE);
    setResponseText(result);
    document.getElementById("div_emulator").innerHTML = "<html><head></head><body><h1>HELLO</h1></body></html>"
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