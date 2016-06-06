/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('objectService',[function(){

    this.currentObject = {
        id: '',
        name: '',
        attributes: [],
        actions: [],
        actionChains: [],
        applicationId: '',
        username: ''
    };

    this.objectOperators = [];
    this.objectOperators['Increase By'] = {
            TYPE: "Number",
        /**
         * @return {number}
         */
        FUNCTION: function(op1, op2){ return parseFloat(op1) + parseFloat(op2); }
        };
    this.objectOperators['Multiply By'] = {
            TYPE: "Number",
        /**
         * @return {number}
         */
        FUNCTION: function(op1, op2){ return parseFloat(op1) * parseFloat(op2); }
        };
    this.objectOperators['Reduce By'] = {
            TYPE: "Number",
        /**
         * @return {number}
         */
        FUNCTION: function(op1, op2){ return parseFloat(op1) - parseFloat(op2); }
        };
    this.objectOperators['Divide By'] = {
            TYPE: "Number",
        /**
         * @return {number}
         */
        FUNCTION: function(op1, op2){ return parseFloat(op1) / parseFloat(op2); }
        };
    this.objectOperators['Add Prefix'] = {
            TYPE: "Text",
            FUNCTION: function(op1, op2){ return op2 + op1; }
        };
    this.objectOperators['Add Suffix'] = {
            TYPE: "Text",
            FUNCTION: function(op1, op2){ return op1 + op2; }
        };
    this.objectOperators['Change To'] = {
            TYPE: "Text",
            FUNCTION: function (op1, op2){ return op2; }
        };

    this.getOperatorListByType = function (type){
        var keys = [];
        for (var key in this.objectOperators) {
            if (this.objectOperators.hasOwnProperty(key) && this.objectOperators[key].TYPE === type) {
                keys.push(key);
            }
        }
        return keys;
    };

    this.addNewObject = function(applicationId, username) {
        this.currentObject = {id: '', name: '', attributes: [], actions: [], actionChains: [], applicationId: applicationId, username: username};
        // this.currentObject.actionChains.push({name: '', actions: []});
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
        this.currentObject = {id: '', name: '', attributes: [], actions: [], actionChains: [], applicationId: '', username: ''};
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
        this.currentObject.actions.push({name: '', operand1: '', operator: '', operandType: '', operand2: ''});
    };

    this.addActionChain = function(){
        if (this.currentObject.actionChains == undefined){
            this.currentObject.actionChains = [];
        }
        this.currentObject.actionChains.push({name: "", actions: []});
    };

    this.addActionChainLink = function(index){
        this.currentObject.actionChains[index].actions.push({operandAttribute: "", operandAction: "", operator: ""});
    };

    this.isLastActionChainLink = function(chainIndex, index){
        if (index == this.currentObject.actionChains[chainIndex].actions.length - 1){
            return ["DONE"];
        } else {
            return [];
        }
    };

    this.removeAction = function(index){
        this.currentObject.actions.splice(index, 1);
    };

    this.removeActionChain = function(index){
        this.currentObject.actionChains.splice(index, 1);
    };

    this.removeActionChainLink = function(chainIndex, linkIndex){
        this.currentObject.actionChains[chainIndex].actions.splice(linkIndex, 1);
    };

    this.checkDisabled = function(){
        if (
            this.currentObject.actionChains != undefined &&
            this.currentObject.actionChains[0] != undefined &&
            this.currentObject.actionChains[0].actions.length > 0 &&
            this.currentObject.actionChains[0].actions[
                this.currentObject.actionChains[0].actions.length - 1
                ].operator == 'DONE') {

            return true;
        }
        return false;
    };

 /*   var getAction = function(actionName, object){
        var index = object.actions.map(function(a) {return a.name;}).indexOf(actionName);
        var action = object.actions[index].operator;
        return this.objectOperators[action].FUNCTION;
    };*/

    var getAction = function(actionName, object){
        var index = object.actions.map(function(a) {return a.name;}).indexOf(actionName);
        var action = object.actions[index].operator;
        // var operand2 = parseFloat(object.actions[index].operand2);

        if (action == "Increase By") {
            return function (operand1, operand2) {
                return operand1 + operand2;
            };
        }
        if (action == "Reduce By") {
            return function (operand1, operand2) {
                return operand1 - operand2;
            };
        }
        if (action == "Multiply By") {
            return function (operand1, operand2) {
                return operand1 * operand2;
            };
        }
        if (action == "Divide By") {
            return function (operand1, operand2) {
                return operand1 / operand2;
            };
        }
        if (action == "Change To") {
            return function (operand1, operand2) {
                return operand2;
            };
        }
        return undefined;
    };

    this.getObjectAction = function(actionChainName, object){
        for (var i = 0; i < object.actionChains.length; i++){
            if(object.actionChains[i].name == actionChainName){
                var actions = object.actionChains[i].actions;
                return function (instances){
                    var newInstances = [];
                    if(actions.length == 0)
                        return 0;
                    for (var i = 0; i < actions.length; i++){
                        if(actions[i].operandAttribute != undefined){//if its attributes
                            var index = object.attributes.map(function(a) {return a.name;}).indexOf(actions[i].operandAttribute.name);
                            newInstances.push(myParseFloat(instances[index]));
                    }
                        else {
                            var index = object.actions.map(function(a) {return a.name;}).indexOf(actions[i].operandAction.name);
                            var index2 = object.attributes.map(function(a) {return a.name;}).indexOf(object.actions[index].operand1.name);
                            if(index2 >= 0){//if its actions
                                var action = getAction(actions[i].operandAction.name, object);
                                newInstances.push(action(myParseFloat(instances[index2]), myParseFloat(object.actions[index].operand2)));
                            }
                        }
                    }
                    var sum = newInstances[0];
                    for (i = 0; i < newInstances.length; i++){
                        if(actions[i].operator == '+')
                            sum += myParseFloat(newInstances[i+1]);
                        else if(actions[i].operator == '-')
                            sum -= myParseFloat(newInstances[i+1]);
                    }
                    return sum;
                };
            }
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
            if (all_objects[i].name === object.name
                && all_objects[i].id !== object.id){
                $scope.objectCreateErrorMessage = "An Object by that name already exists!";
                return false;
            }
        }
        return true;
    };

    this.performObjectAction = function(action, object, instance, dynamicValue){
        var operand2 = undefined;

        if (action.operandType === "Fixed Value"){
            operand2 = action.operand2;
        }

        if (action.operandType === "Attribute"){
            for (var j = 0; j < object.attributes.length; j++) {
                if (object.attributes[j].name === action.operand2.name) {
                    break;
                }
            }
            operand2 = instance[j];
        }
        if (action.operandType === "Dynamic"){
            operand2 = dynamicValue;
        }


        var actionFunc = getAction(action.name, object);

        for (var i = 0; i < object.attributes.length; i++) {
            if (object.attributes[i].name === action.operand1.name) {
                break;
            }
        }

        instance[i] = actionFunc(parseFloat(instance[i]), parseFloat(operand2));
    };

    var myParseFloat = function(n){
        if (isNumber(n)){
            return parseFloat(n);
        } else {
            return n;
        }
    };

}]);