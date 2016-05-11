/*
REQUIRED FUNCTIONS:
     initializeDataStructures
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
        };

        $scope.designDisplayBehaviorPage = function(){
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
            if($scope.isNumber(idx))
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

        $scope.getObjectAction = function(actionName, operand2){
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
                    var instanceValue = instance[attrIndex];
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
