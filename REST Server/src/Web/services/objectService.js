/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('objectService',[function(){

    this.currentObject = {
        id: '',
        name: '',
        attributes: [],
        actions: [],
        actionsChain: [],
        applicationId: '',
        username: ''
    };

    this.addNewObject = function(applicationId, username) {
        this.currentObject = {id: '', name: '', attributes: [], actions: [], actionsChain: [], applicationId: applicationId, username: username};
        this.currentObject.actionsChain.push({name: '', actions: []});
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
        this.currentObject = {id: '', name: '', attributes: [], actions: [], actionsChain: [], applicationId: '', username: ''};
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
        this.currentObject.attributes.push({name: '', type: ''});
    };

    this.removeAttribute = function($index){
      this.currentObject.attributes.splice($index, 1);
    };

    this.addAction = function(){
        this.currentObject.actions.push({name: '', operand1: '', operator: '', type: '', operand2: ''});
    };

    this.addActionChain = function(){
        this.currentObject.actionsChain[0].actions.push({operand: '', operator: ''});
    };

    this.removeAction = function($index){
        this.currentObject.actions.splice($index, 1);
    };

    this.removeActionChain = function($index){
        this.currentObject.actionsChain.actions.splice($index, 1);
    };

    this.checkDisabled = function(){
        if (
            this.currentObject.actionsChain != undefined &&
            this.currentObject.actionsChain[0] != undefined &&
            this.currentObject.actionsChain[0].actions.length > 0 &&
            this.currentObject.actionsChain[0].actions[
                this.currentObject.actionsChain[0].actions.length - 1
                ].operator == 'DONE') {

            return true;
        }
        return false;
    };

    this.getObjectAction = function(actionChainName, object){
        for (var i = 0; i < object.actionsChain.length; i++){
            if(object.actionsChain[i].name == actionChainName){
                var actions = object.actionsChain[i].actions;
                return function (instances){
                    var newInstances = [];
                    if(actions.length == 0)
                        return 0;
                    for (var i = 0; i < actions.length; i++){
                        var index = object.attributes.map(function(a) {return a.name;}).indexOf(actions[i].operand.name);
                        if(index >= 0)//if its attributes
                            newInstances.push(parseFloat(instances[index]));
                        else {
                            index = object.actions.map(function(a) {return a.name;}).indexOf(actions[i].operand.name);//exam5 : exam , exam5
                            var index2 = object.attributes.map(function(a) {return a.name;}).indexOf(object.actions[index].operand1.name);
                            if(index2 >= 0){//if its actions
                                var action = getAction(actions[i].operand.name, object);
                                newInstances.push(action(parseFloat(instances[index2])));
                            }
                        }
                    }
                    var sum = newInstances[0];
                    for (i = 0; i < newInstances.length; i++){
                        if(actions[i].operator == '+')
                            sum += parseFloat(newInstances[i+1]);
                        else if(actions[i].operator == '-')
                            sum -= parseFloat(newInstances[i+1]);
                    }
                    return sum;
                };
            }
        }
        return undefined;
    };

    var getAction = function(actionName, object, operand2){
        var index = object.actions.map(function(a) {return a.name;}).indexOf(actionName);
        var action = object.actions[index].operator;
        if (operand2 == undefined) {
            operand2 = parseFloat(object.actions[index].operand2);
        }
        if (action == "Increase By") {
            return function (operand) {
                return operand + operand2;
            };
        }
        if (action == "Reduce By") {
            return function (operand) {
                return operand - operand2;
            };
        }
        if (action == "Multiply By") {
            return function (operand) {
                return operand * operand2;
            };
        }
        if (action == "Divide By") {
            return function (operand) {
                return operand / operand2;
            };
        }
        if (action == "Change To") {
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
    };

    this.performObjectAction = function(action, object, instance, dynamicValue){
        var operand2 = undefined;

        if (action.type === "Fixed Value"){
            operand2 = parseFloat(action.operand2);
        }
        if (action.type === "Attribute"){
            for (var j = 0; j < object.attributes.length; j++) {
                if (object.attributes[j].name === action.operand2.name) {
                    break;
                }
            }
            operand2 = parseFloat(instance[j]);
        }
        if (action.type === "Dynamic"){
            operand2 = parseFloat(dynamicValue);
        }


        var actionFunc = getAction(action.name, object, operand2);
            for (var i = 0; i < object.attributes.length; i++) {
                if (object.attributes[i].name === action.operand1.name) {
                    break;
                }
            }
        instance[i] = actionFunc(parseFloat(instance[i]));
    };

}]);