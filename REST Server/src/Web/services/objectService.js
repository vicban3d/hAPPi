/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('objectService',[function(){

    this.currentObject = {
        id: '',
        name: '',
        attributes: [],
        actions: []
    };

    this.addNewObject = function() {
        this.currentObject = {id: '', name: '', attributes: [], actions: []};
    };

    this.addObject = function(application) {
        this.currentObject.id = generateUUID();
        addObjectToApplication(application, this.currentObject);
    };

    this.editObject = function(application, object){
        removeObjectFromApplication(application, object.name);
        addObjectToApplication(application, this.currentObject);
    };

    this.deleteObject = function(application, object){
       removeObjectFromApplication(application, object.name);
        this.currentObject = {};
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

}]);