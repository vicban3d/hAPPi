/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('objectService',[function(){

    this.currentObject = {
        id: '',
        name: '',
        attributes: [],
        actions: [],
        applicationId: '',
        username: ''
    };

    this.addNewObject = function(applicationId, username) {
        this.currentObject = {id: '', name: '', attributes: [], actions: [], applicationId: applicationId, username: username};
    };

    this.addObject = function(application) {
        this.currentObject.id = generateUUID();
        /*this.currentObject.applicationId = application.id;
        this.currentObject.username = username;*/
        addObjectToApplication(application, this.currentObject);
    };

    this.editObject = function(application, object){
        removeObjectFromApplication(application, object.name);
        this.currentObject.applicationId = application.id;
        addObjectToApplication(application, this.currentObject);
    };

    this.deleteObject = function(application, object){
       removeObjectFromApplication(application, object.name);
        this.currentObject = {id: '', name: '', attributes: [], actions: [], applicationId: '', username: ''};
    };

    var addObjectToApplication = function(application, object){
        application.objects.push(object);
    };

    var removeObjectFromApplication= function(application, objectName){
        for(var i = application.objects.length - 1; i >= 0; i--){
            if(application.objects[i].name == objectName){
                application.objects.splice(i,1);
            }
        }
    };

    this.addAttribute = function(){
        this.currentObject.attributes.push({name: '', type: 'Text'});
    };

    this.removeAttribute = function($index){
      this.currentObject.attributes.splice($index, 1);
    };

    this.addAction = function(){
        this.currentObject.actions.push({name: '', operand1: '', operator: '', operand2: ''});
    };

    this.removeAction = function($index){
        this.currentObject.actions.splice($index, 1);
    };

    this.getObjectAction = function(actionName, operand2){
        if (actionName == "Increase By") {
            return function (operand) {
                return operand + operand2;
            };
        }
        if (actionName == "Reduce By") {
            return function (operand) {
                return operand - operand2;
            };
        }
        if (actionName == "Multiply By") {
            return function (operand) {
                return operand * operand2;
            };
        }
        if (actionName == "Divide By") {
            return function (operand) {
                return operand / operand2;
            };
        }
        if (actionName == "Change To") {
            return function () {
                return operand2;
            };
        }
        return undefined;
    };

    this.isValidObject = function($scope, object){
        if (object == undefined || object === ""){
            return false;
        }
        
        var all_objects = $scope.getCurrentApplication().objects;
        if (all_objects == undefined){
            return true;
        }
        for (var i=0; i< all_objects.length; i++) {
            if (all_objects[i].name.valueOf() == object.name.valueOf()
                && all_objects[i].id.valueOf() != object.id.valueOf()){
                $scope.createErrorMessage = "An Object by that name already exists!";
                return false;
            }
        }
        return true;
    }

}]);