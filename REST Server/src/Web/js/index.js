/**
 * Created by Gila-Ber on 07/11/2015.
 *
 */
var numOfAttributes = 0;

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

function openNewProjectForm(){
    document.getElementById("div_createProject").innerHTML =
    "<hr>" +
    "Project Name: " +
    "<input id=\"in_projectName\">" +
    "<br><button id=\"btn_create\" onclick=createProject()>create</button>";
}
function createProject(){
    var projectName = document.getElementById("in_projectName").value;
    var newProject = {
        name: projectName
    };
    sendPOSTRequest(Paths.CREATE_PROJECT, JSON.stringify(newProject));
    clearDiv("div_createProject");
}

function clearDiv(divName){
    document.getElementById(divName).innerHTML ="";
}

function addAndroid(){
    var newProject = {
        name: "TEMP_PROJECT_NAME"
    };
    sendPOSTRequest(Paths.ADD_PLATFORM_ANDROID, JSON.stringify(newProject));
}

function buildProject(){
    var newProject = {
        name: "TEMP_PROJECT_NAME"
    };
    sendPOSTRequest(Paths.BUILD_PROJECT, JSON.stringify(newProject));
}

function submit(){
    var entityName = document.getElementById("in_entityName").value;
    var attributes = collectAttributeValues().join(" ");
    var newEntity = {
        name: entityName,
        attributes: attributes
    };

    sendPOSTRequest(Paths.CREATE_ENTITY, JSON.stringify(newEntity));
    alert("here");
    clearDiv("div_createEntity");
    alert("here2");
    document.getElementById("btn_createEntity").disabled = false;
    alert("here3");
}