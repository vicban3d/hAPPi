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
        this.phoneNumber = '';

        $scope.designDisplayObjectPage = function(object){
            this.currentInstance = object;
            this.emulatorOutput = '';
            this.showAddInstance = true;
        };

        $scope.getShowAddInstance = function(){
            return this.showAddInstance;
        };

        $scope.getCurrentApplication = function(){
            return $scope.currentApplication;
        };

        $scope.getShowEmulatorMainPage = function(){
            return this.showEmulatorMainPage;
        };

        $scope.gotoAppInstance = function(phoneNumber){
            this.phoneNumber = phoneNumber;
            this.showEmulatorMainPage = false;
            this.showAddInstance = false;
        };

        $scope.designDisplayBehaviorPage = function(){
            this.currentInstance = {};
            this.showAddInstance = false;
        };

        $scope.addInstance = function($scope, attributes){
            if ($scope.instances[this.currentInstance.name] == undefined){
                $scope.instances[this.currentInstance.name] = [];
            }
            $scope.instances[this.currentInstance.name].push(attributes);
            $scope.attribute_values = [];

            var postBody = {
                id : this.phoneNumber,
                app_id: $scope.getCurrentApplication().id,
                objName: this.currentInstance.name,
                attributesList: attributes
            };
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.ADDOBJ_INSTANCE, angular.toJson(postBody)));
        };

        $scope.removeInstance = function($scope, idx){
            if(isNumber(idx))
                $scope.instances[this.currentInstance.name].splice(parseInt(idx),1);
            else
                alert("Please choose index from the list!");

            var postBody = {
                id : this.phoneNumber,
                app_id: $scope.getCurrentApplication().id,
                objName: this.currentInstance.name,
                index: idx
            };
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.REMOVEOBJ_INSTANCE, angular.toJson(postBody)));
        };

        $scope.getOutput = function(){
            return this.emulatorOutput;
        };

        $scope.getCurrentInstance = function(){
            return this.currentInstance;
        };

        $scope.performBehaviorAction = function($scope, behavior){
            var object = behavior.operandObject;
            var operand1 = behavior.operandAttribute.name;
            var action = $scope.getBehaviorAction(object, behavior.operator, behavior.conditions);
            if (action == undefined){
                this.emulatorOutput = "Unsupported Operation"
            } else {
                this.emulatorOutput = action(operand1);
            }
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

        var getAction = function(actionName, object, operand2){
            var index = object.actions.map(function(a) {return a.name;}).indexOf(actionName);
            var action = object.actions[index].operator;
            if (operand2 == undefined) {
                operand2 = object.actions[index].operand2;
            }

            if (isNumber(operand2)) {
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
            }
            // operand is Text
            else {
                if (action == "Add Prefix") {
                    return function (operand) {
                        return operand + operand2;
                    };
                }
                if (action == "Add Suffix") {
                    return function (operand) {
                        return operand2 + operand;
                    };
                }
                if (action == "Change To") {
                    return function () {
                        return operand2;
                    };
                }
            }
            return undefined;
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
         return getAccumulatedValue($scope, object, operand, accumulatorFunction);
         };
         * ---------------------------------------------------------------------------------- */
        this.getInstancesFilteredByConditions = function(instances, conditions, object){
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

        this.getBehaviorAction = function($scope, object, actionName, conditions){
            var instances = this.getInstancesFilteredByConditions($scope.instances[object.name], conditions, object);
            if (actionName == "Sum of All"){
                return function (operand){
                    var accumulatorFunction = function(initial, action, index) {
                        for (var i = 0; i < instances.length; i++) {
                            initial += action(parseFloat(instances[i][index]));
                        }
                        return initial
                    };
                    return getAccumulatedValue($scope, object, operand, 0, accumulatorFunction);
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
                    return getAccumulatedValue($scope, object, operand, Number.MIN_VALUE, accumulatorFunction);
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
                    return getAccumulatedValue($scope, object, operand, Number.MAX_VALUE, accumulatorFunction);
                };
            } else if (actionName == "Product of All"){
                return function (operand){
                    var accumulatorFunction = function(initial, action, index) {
                        for (var i = 0; i < instances.length; i++) {
                            initial *= action(parseFloat(instances[i][index]));
                        }
                        return initial
                    };
                    return getAccumulatedValue($scope, object, operand, 1, accumulatorFunction);
                };
            } else if (actionName == "Average"){
                return function (operand){
                    var accumulatorFunction = function(initial, action, index) {
                        for (var i = 0; i < instances.length; i++) {
                            initial += action(parseFloat(instances[i][index]));
                        }
                        return initial/instances.length;
                    };
                    return getAccumulatedValue($scope, object, operand, 0, accumulatorFunction);
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
                    return getAccumulatedValue($scope, object, operand, "", accumulatorFunction);
                };
            }
            else {
                return undefined;
            }
        };

    }]);
