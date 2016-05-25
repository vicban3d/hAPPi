/*
REQUIRED FUNCTIONS:
     designDisplayBehaviorPage
     getCurrentApplication
     designDisplayObjectPage
     gotoAppInstance
     getShowEmulatorMainPage
     getShowAddInstance
     performBehaviorAction
     getBehaviorAction
     addInstance
     removeInstance
     getCurrentInstance
     getOutput

REQUIRED STYLES:
     designAreaTopMenu
     designAreaPage
     formInput
     componentMainTitle
     removeFormFieldsButton
     designAreaOutputLabel
 */


var main_module = angular.module('main', []);

main_module.controller('ctrl_main', ['$scope',
    function($scope) {

        $scope.currentApplication = {
            id: "<[APP_ID]>",
            name: "<[NAME]>",
            platforms: '',
            objects: JSON.parse("<[OBJECTS]>"),
            behaviors: JSON.parse("<[BEHAVIORS]>"),
            events: [],
            username: ''
        };

        $scope.currentInstance = '';
        $scope.showEmulatorMainPage = true;
        $scope.showAddInstance = true;
        $scope.attribute_values = [];
        $scope.instances = [];
        $scope.emulatorOutput = '';
        $scope.phoneNumber = '';

        $scope.objectOperators = [];
        $scope.objectOperators['Increase By'] = {
            TYPE: "Number",
            /**
             * @return {number}
             */
            FUNCTION: function(op1, op2){ return parseFloat(op1) + parseFloat(op2); }
        };
        $scope.objectOperators['Multiply By'] = {
            TYPE: "Number",
            /**
             * @return {number}
             */
            FUNCTION: function(op1, op2){ return parseFloat(op1) * parseFloat(op2); }
        };
        $scope.objectOperators['Reduce By'] = {
            TYPE: "Number",
            /**
             * @return {number}
             */
            FUNCTION: function(op1, op2){ return parseFloat(op1) - parseFloat(op2); }
        };
        $scope.objectOperators['Divide By'] = {
            TYPE: "Number",
            /**
             * @return {number}
             */
            FUNCTION: function(op1, op2){ return parseFloat(op1) / parseFloat(op2); }
        };
        $scope.objectOperators['Add Prefix'] = {
            TYPE: "Text",
            FUNCTION: function(op1, op2){ return op2 + op1; }
        };
        $scope.objectOperators['Add Suffix'] = {
            TYPE: "Text",
            FUNCTION: function(op1, op2){ return op1 + op2; }
        };
        $scope.objectOperators['Change To'] = {
            TYPE: "Text",
            FUNCTION: function (op1, op2){ return op2; }
        };

        $scope.designDisplayObjectPage = function(object){
            $scope.currentInstance = object;
            $scope.emulatorOutput = '';
            $scope.showAddInstance = true;
        };

        $scope.getShowAddInstance = function(){
            return $scope.showAddInstance;
        };

        $scope.getCurrentApplication = function(){
            return $scope.currentApplication;
        };

        $scope.getShowEmulatorMainPage = function(){
            return $scope.showEmulatorMainPage;
        };

        $scope.gotoAppInstance = function(phoneNumber){
            $scope.phoneNumber = phoneNumber;
            $scope.showEmulatorMainPage = false;
            $scope.showAddInstance = false;
        };

        $scope.designDisplayBehaviorPage = function(){
            $scope.currentInstance = {};
            $scope.showAddInstance = false;
        };

        $scope.addInstance = function(){
            if ($scope.instances[$scope.currentInstance.name] == undefined){
                $scope.instances[$scope.currentInstance.name] = [];
            }
            $scope.instances[$scope.currentInstance.name].push($scope.attribute_values);

            var postBody = {
                id : $scope.phoneNumber,
                app_id: $scope.currentApplication.id,
                objName: $scope.currentInstance.name,
                attributesList: $scope.attribute_values
            };
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.ADDOBJ_INSTANCE, angular.toJson(postBody)));
            $scope.attribute_values = [];
        };

        $scope.removeInstance = function(idx){
            if(isNumber(idx))
                $scope.instances[$scope.currentInstance.name].splice(parseInt(idx),1);
            else
                alert("Please choose index from the list!");

            var postBody = {
                id : $scope.phoneNumber,
                app_id: $scope.currentApplication.id,
                objName: $scope.currentInstance.name,
                index: idx
            };
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.REMOVEOBJ_INSTANCE, angular.toJson(postBody)));
        };

        $scope.getOutput = function(){
            return $scope.emulatorOutput;
        };

        $scope.getCurrentInstance = function(){
            return $scope.currentInstance;
        };

        $scope.performBehaviorAction = function(behavior){
            var object = behavior.action.operandObject;
            var operand1 = behavior.action.operandAttribute.name;
            var action = $scope.getBehaviorAction(object, behavior.action.operator, behavior.action.conditions);
            if (action == undefined){
                $scope.emulatorOutput = "Unsupported Operation"
            } else {
                $scope.emulatorOutput = action(operand1);
            }
        };

        $scope.performObjectAction = function(action, object, instance, dynamicValue){
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

            instance[i] = actionFunc(instance[i], operand2);
        };

        $scope.getObjectAction = function(actionChainName, object){
            for (var i = 0; i < object.actionChains.length; i++){
                if(object.actionChains[i].name == actionChainName){
                    var actions = object.actionChains[i].actions;
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

        var getAction = function(actionName, object){
            var index = object.actions.map(function(a) {return a.name;}).indexOf(actionName);
            var action = object.actions[index].operator;
            return $scope.objectOperators[action].FUNCTION;
        };


        $scope.getAccumulatedValue = function(object, operand, initial, accumulatorFunction){
            var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
            var result = 0;
            if (index < 0){
                var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand);
                var actionName = object.actions[actionIndex].operator;
                var operand1 = object.actions[actionIndex].operand1.name;
                var operand2 = object.actions[actionIndex].operand2;
                index = object.attributes.map(function(a) {return a.name;}).indexOf(operand1);
                var action = $scope.getObjectAction(actionName, parseFloat(operand2));
                if (action == undefined){
                    return undefined;
                }
                result = accumulatorFunction(initial, action, index);
            } else {
                result = accumulatorFunction(initial, function (x){return x}, index);
            }
            return result;
        };


        /* ----------------------------------------------------------------------------------
         Behavior Action Template:

         if (actionName == "<ACTION NAME>"){
         return function (operand){
         var accumulatorFunction = function(initial, action, index) {
         for (var i = 0; i < $scope.instances[object.name].length; i++) {
         <LOGIC ON initial>
         }
         return initial
         };
         return getAccumulatedValue(object, operand, accumulatorFunction);
         };
         * ---------------------------------------------------------------------------------- */
        $scope.getInstancesFilteredByConditions = function(instances, conditions, object){
            if(conditions == null || conditions.length ==  0){
                return instances;
            }
            for (var i = 0 ; i < conditions.length; i++){
                var attrIndex =  object.attributes.indexOf(conditions[i].attribute);
                var temp = instances.map(function(instance) {
                    var instanceValue = parseInt(instance[attrIndex]);
                    var logicOperation = conditions[i].logicOperation;
                    var conditionValue = conditions[i].value;

                    if (logicOperation == "Greater Than") {
                        if(instanceValue > conditionValue)
                            return instance;
                    }
                    else if (logicOperation == "Less Than") {
                        if(instanceValue < conditionValue)
                            return instance;
                    }
                    else if (logicOperation == "Equal") {
                        if(instanceValue == conditionValue)
                            return instance;
                    }
                    else if (logicOperation == "Not Equal") {
                        if(instanceValue != conditionValue)
                            return instance;
                    }
                    return null;
                });
                temp = temp.filter(function(x) {return x != null});
            }
            return temp;
        };

        $scope.getBehaviorAction = function(object, actionName, conditions){
            var instances = $scope.getInstancesFilteredByConditions($scope.instances[object.name], conditions, object);
            if (actionName == "Sum of All"){
                return function (operand){
                    var accumulatorFunction = function(initial, action, index) {
                        for (var i = 0; i < instances.length; i++) {
                            initial += action(parseFloat(instances[i][index]));
                        }
                        return initial
                    };
                    return $scope.getAccumulatedValue(object, operand, 0, accumulatorFunction);
                };
            } else if (actionName == "Maximum") {
                return function (operand){
                    var accumulatorFunction = function(initial, action, index) {
                        for (var i = 0; i < instances.length; i++) {
                            if (initial < action(parseFloat(instances[i][index]))) {
                                initial = action(parseFloat(instances[i][index]));
                            }
                        }
                        return initial
                    };
                    return $scope.getAccumulatedValue(object, operand, Number.MIN_VALUE, accumulatorFunction);
                };
            } else if (actionName == "Minimum"){
                return function (operand){
                    var accumulatorFunction = function(initial, action, index) {
                        for (var i = 0; i < instances.length; i++) {
                            if (initial > action(parseFloat(instances[i][index]))) {
                                initial = action(parseFloat(instances[i][index]));
                            }
                        }
                        return initial
                    };
                    return $scope.getAccumulatedValue(object, operand, Number.MAX_VALUE, accumulatorFunction);
                };
            } else if (actionName == "Product of All"){
                return function (operand){
                    var accumulatorFunction = function(initial, action, index) {
                        for (var i = 0; i < instances.length; i++) {
                            initial *= action(parseFloat(instances[i][index]));
                        }
                        return initial
                    };
                    return $scope.getAccumulatedValue(object, operand, 1, accumulatorFunction);
                };
            } else if (actionName == "Average"){
                return function (operand){
                    var accumulatorFunction = function(initial, action, index) {
                        for (var i = 0; i < instances.length; i++) {
                            initial += action(parseFloat(instances[i][index]));
                        }
                        return initial/instances.length;
                    };
                    return $scope.getAccumulatedValue(object, operand, 0, accumulatorFunction);
                };
            } else if (actionName == "Display"){
                return function (operand){
                    var accumulatorFunction = function(initial, action, index) {
                        for (var i = 0; i < instances.length - 1; i++) {
                            initial = initial + action(instances[i][index]) + ", ";
                        }
                        initial = initial + action(instances[i][index]);
                        return initial;
                    };
                    return $scope.getAccumulatedValue(object, operand, "", accumulatorFunction);
                };
            }
            else {
                return undefined;
            }
        };

        $scope.acceptMessageResult = function(result, success, fail){
            if (success == undefined){
                success = function(){};
            }
            if (fail == undefined){
                fail = function(){};
            }
            result.onreadystatechange = function(){
                if (result.readyState != 4 && result.status != 200){
                    $scope.message = "Error";
                    fail();
                    $scope.$apply();
                }
                else if (result.readyState == 4 && result.status == 200){
                    $scope.message = result.responseText;
                    success(result.responseText);
                    $scope.$apply();
                }
            };
        };

    }]);

function sendPOSTRequestPlainText(path, data) {
    var req = createRequest();
    req.open("POST", Paths.ROOT + path, true);
    req.setRequestHeader("Content-Type", "text/plain");
    req.send(data);
    return req;
}

function createRequest() {
    var result = null;
    if (window.XMLHttpRequest) {
        // FireFox, Safari, etc.
        result = new XMLHttpRequest();
        if (typeof result.overrideMimeType != 'undefined') {
            result.overrideMimeType('text/xml'); // Or anything else
        }
    }
    else if (window.ActiveXObject) {
        // Internet Explorer
        result = new ActiveXObject("Microsoft.XMLHTTP");
    }
    else {
        alert("Invalid Browser!");
    }
    return result;
}

function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

var Paths = {
    ROOT: "http://localhost",
    PATH_APP_INDEX: "/www/index.html",
    CREATE_APP: "/createApplication",
    CREATE_OBJECT: "/createObject",
    CREATE_BEHAVIOR: "/createBehavior",
    REMOVE_APP: "/removeApplication",
    REMOVE_OBJECT: "/removeObject",
    REMOVE_BEHAVIOR: "/removeBehavior",
    UPDATE_APP: "/updateApplication",
    UPDATE_OBJECT: "/updateObject",
    UPDATE_BEHAVIOR: "/updateBehavior",
    ADD_PLATFORM_ANDROID: "/addAndroid",
    ADD_PLATFORM_IOS: "/addIOS",
    ADD_PLATFORM_WINDOWS_PHONE: "/addWindowsPhone",
    EMULATE_ANDROID: "/emulateAndroid",
    BUILD_APP: "/build",
    SIGNUP_PAGE: "/signup",
    CREATE_USER: "/addUser",
    LOGIN: "/login",
    CREATE_EVENT: "/createEvent",
    REMOVE_EVENT: "/removeEvent",
    UPDATE_EVENT: "/updateEvent",
    ADDOBJ_INSTANCE: "/AddObjInstance",
    REMOVEOBJ_INSTANCE: "/RemoveObjInstance"
};