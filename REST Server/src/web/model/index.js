/*
REQUIRED FUNCTIONS:
     designDisplayBehaviorPage
     getCurrentApplication
     designDisplayObjectPage
     gotoAppInstance
     getShowEmulatorMainPage
     getShowInstancePage
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
        $scope.showInstancePage = true;
        $scope.attributeValues = [];
        $scope.instances = [];
        $scope.emulatorOutput = '';
        $scope.phoneNumber = '';

        $scope.objectOperators = [];
        $scope.objectOperators['Increase By'] = {
            TYPE: "Number",
            /**
             * @return {number}
             */
            FUNCTION: function (op1, op2) {
                return parseFloat(op1) + parseFloat(op2);
            }
        };
        $scope.objectOperators['Multiply By'] = {
            TYPE: "Number",
            /**
             * @return {number}
             */
            FUNCTION: function (op1, op2) {
                return parseFloat(op1) * parseFloat(op2);
            }
        };
        $scope.objectOperators['Reduce By'] = {
            TYPE: "Number",
            /**
             * @return {number}
             */
            FUNCTION: function (op1, op2) {
                return parseFloat(op1) - parseFloat(op2);
            }
        };
        $scope.objectOperators['Divide By'] = {
            TYPE: "Number",
            /**
             * @return {number}
             */
            FUNCTION: function (op1, op2) {
                return parseFloat(op1) / parseFloat(op2);
            }
        };
        $scope.objectOperators['Add Prefix'] = {
            TYPE: "Text",
            FUNCTION: function (op1, op2) {
                return op2 + op1;
            }
        };
        $scope.objectOperators['Add Suffix'] = {
            TYPE: "Text",
            FUNCTION: function (op1, op2) {
                return op1 + op2;
            }
        };
        $scope.objectOperators['Change To'] = {
            TYPE: "Text",
            FUNCTION: function (op1, op2) {
                return op2;
            }
        };

        $scope.designDisplayObjectPage = function (object) {
            $scope.currentInstance = object;
            $scope.emulatorOutput = '';
            $scope.showInstancePage = true;
        };

        $scope.getShowInstancePage = function () {
            return $scope.showInstancePage;
        };

        $scope.getCurrentApplication = function () {
            return $scope.currentApplication;
        };

        $scope.getShowEmulatorMainPage = function () {
            return $scope.showEmulatorMainPage;
        };

        $scope.gotoAppInstance = function (phoneNumber) {
            $scope.phoneNumber = phoneNumber;
            $scope.showEmulatorMainPage = false;
            $scope.showInstancePage = false;
        };

        $scope.designDisplayBehaviorPage = function () {
            $scope.currentInstance = {};
            $scope.showInstancePage = false;
        };

        $scope.addInstance = function (attributes) {
            if ($scope.instances[this.currentInstance.name] == undefined) {
                $scope.instances[this.currentInstance.name] = {};
            }
            var insId = generateUUID();
            $scope.instances[this.currentInstance.name][insId] = attributes;
            $scope.attributeValues = [];

            var postBody = {
                id: this.phoneNumber,
                app_id: $scope.getCurrentApplication().id,
                objName: this.currentInstance.name,
                attributesList: attributes,
                insId: insId
            };
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.ADD_OBJECT_INSTANCE, angular.toJson(postBody)),
                function () {
                },
                function () {
                    alert("Failed to add instance!")
                }
            );
        };

        $scope.removeInstance = function (insId) {
            delete $scope.instances[this.currentInstance.name][insId];
            var postBody = {
                id: this.phoneNumber,
                app_id: $scope.getCurrentApplication().id,
                objName: this.currentInstance.name,
                insId: insId
            };

            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.REMOVE_OBJECT_INSTANCE, angular.toJson(postBody)));
        };

        $scope.getOutput = function () {
            return $scope.emulatorOutput;
        };

        $scope.getCurrentInstance = function () {
            return $scope.currentInstance;
        };

        $scope.performBehaviorAction = function (behavior) {
            var object = behavior.action.operandObject;
            var operand1 = behavior.action.operandAttribute == null ? undefined : behavior.action.operandAttribute.name;
            var chain = behavior.action.actionChain == null ? undefined : behavior.action.actionChain.name;
            var action = $scope.getBehaviorAction(object, behavior.action.operator, behavior.action.conditions);
            if (action == undefined) {
                this.emulatorOutput = "Unsupported Operation"
            } else if (chain == undefined) {
                this.emulatorOutput = action(operand1);
            }
            else if (operand1 == undefined) {
                this.emulatorOutput = action(chain);
            }
        };

        $scope.performObjectAction = function (action, object, instance, insId, dynamicValue) {
            var operand2 = undefined;

            if (action.operandType === "Fixed Value") {
                operand2 = action.operand2;
            }

            if (action.operandType === "Attribute") {
                for (var j = 0; j < object.attributes.length; j++) {
                    if (object.attributes[j].name === action.operand2) {
                        break;
                    }
                }
                operand2 = instance[j];
            }
            if (action.operandType === "Dynamic") {
                operand2 = dynamicValue;
            }


            var actionFunc = getAction(action.name, object);

            for (var i = 0; i < object.attributes.length; i++) {
                if (object.attributes[i].name === action.operand1.name) {
                    break;
                }
            }

            instance[i] = actionFunc(myParseFloat(instance[i]), myParseFloat(operand2));

            var postBody = {
                id: $scope.getPhoneNumber(),
                app_id: $scope.getCurrentApplication().id,
                objName: $scope.getCurrentInstance().name,
                insId: insId,
                attributesList: instance
            };

            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.UPDATE_OBJECT_INSTANCE, angular.toJson(postBody)));
        };

        $scope.getObjectAction = function (actionChainName, object) {
            for (var i = 0; i < object.actionChains.length; i++) {
                if (object.actionChains[i].name == actionChainName) {
                    var actions = object.actionChains[i].actions;
                    return function (instances) {
                        var newInstances = [];
                        if (actions.length == 0)
                            return 0;
                        for (var i = 0; i < actions.length; i++) {
                            if (actions[i].operandAttribute != undefined) {//if its attributes
                                var index = object.attributes.map(function (a) {
                                    return a.name;
                                }).indexOf(actions[i].operandAttribute.name);
                                newInstances.push(myParseFloat(instances[index]));
                            }
                            else {
                                var index = object.actions.map(function (a) {
                                    return a.name;
                                }).indexOf(actions[i].operandAction.name);
                                var index2 = object.attributes.map(function (a) {
                                    return a.name;
                                }).indexOf(object.actions[index].operand1.name);
                                if (index2 >= 0) {//if its actions
                                    var action = getAction(actions[i].operandAction.name, object);
                                    newInstances.push(action(myParseFloat(instances[index2]), myParseFloat(object.actions[index].operand2)));
                                }
                            }
                        }
                        var sum = newInstances[0];
                        for (i = 0; i < newInstances.length; i++) {
                            if (actions[i].operator == '+')
                                sum += myParseFloat(newInstances[i + 1]);
                            else if (actions[i].operator == '-')
                                sum -= myParseFloat(newInstances[i + 1]);
                        }
                        return sum;
                    };
                }
            }
            return undefined;
        };

        var getAction = function (actionName, object) {
            var index = object.actions.map(function (a) {
                return a.name;
            }).indexOf(actionName);
            var action = object.actions[index].operator;

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
            if (action == "Add Prefix") {
                return function (operand1, operand2) {
                    return operand2 + operand1;
                };
            }
            if (action == "Add Suffix") {
                return function (operand1, operand2) {
                    return operand1 + operand2;
                };
            }
            return undefined;
        };


        $scope.getAccumulatedValue = function (object, operand, initial, accumulatorFunction) {
            var index = object.attributes.map(function (a) {
                return a.name;
            }).indexOf(operand);
            var result = 0;
            if (index < 0) {//not an attribute
                var actionChainIndex = object.actionChains.map(function (a) {
                    return a.name;
                }).indexOf(operand);
                var actionChainName = object.actionChains[actionChainIndex].name;
                var action = $scope.getObjectAction(actionChainName, object);
                if (action == undefined) {
                    return undefined;
                }
                result = accumulatorFunction(initial, action, index);
            } else {
                result = accumulatorFunction(initial, undefined, index);
                // result = accumulatorFunction(initial, function (x){return x}, index);
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
        $scope.getInstancesFilteredByConditions = function (instances, conditions, object) {
            if (conditions == null || conditions.length == 0) {
                return instances;
            }
            var filteredInstances = [];
            var instanceValue;
            var maxInstanceValue = '';
            var maxInstances = [];
            var minInstanceValue = '';
            var minInstances = [];
            for (var i = 0; i < conditions.length; i++) {
                for (var instanceId in instances) {
                    if (instances.hasOwnProperty(instanceId)) {
                        // get relevant value
                        if (conditions[i].attribute) {
                            var index = object.attributes.map(function (a) {
                                return a.name;
                            }).indexOf(conditions[i].attribute.name);
                            instanceValue = parseInt(instances[instanceId][index]);
                        }
                        else if (conditions[i].actionChain) {
                            var funcIns = objectService.getObjectAction(conditions[i].actionChain.name, object);
                            instanceValue = funcIns(instances[instanceId]);
                        }

                        var logicOperation = conditions[i].logicOperation;
                        var conditionValue = conditions[i].value;
                        if (logicOperation == "Greater Than") {
                            if (instanceValue > conditionValue)
                                filteredInstances.push(instances[instanceId]);
                        }
                        else if (logicOperation == "Less Than") {
                            if (instanceValue < conditionValue)
                                filteredInstances.push(instances[instanceId]);
                        }
                        else if (logicOperation == "Equal") {
                            if (instanceValue == conditionValue)
                                filteredInstances.push(instances[instanceId]);
                        }
                        else if (logicOperation == "Not Equal") {
                            if (instanceValue != conditionValue)
                                filteredInstances.push(instances[instanceId]);
                        }
                        else if (logicOperation == "Is Maximal") {
                            if (maxInstanceValue === '' || instanceValue > maxInstanceValue) {
                                maxInstanceValue = instanceValue;
                                maxInstances = instances[instanceId];
                            }
                        }
                        else if (logicOperation == "Is Minimal") {
                            if (minInstanceValue === '' || instanceValue < minInstanceValue) {
                                minInstanceValue = instanceValue;
                                minInstances = instances[instanceId];
                            }
                        }
                    }
                }
            }

            if (logicOperation == "Is Maximal") {
                filteredInstances.push(maxInstances);
            }
            else if (logicOperation == "Is Minimal") {
                filteredInstances.push(minInstances);
            }
            return filteredInstances;
        };

        $scope.getBehaviorAction = function ($scope, object, actionName, conditions) {
            var instances = this.getInstancesFilteredByConditions($scope, $scope.instances[object.name], conditions, object);
            if (actionName == "Sum of All") {
                return function (operand) {
                    var accumulatorFunction = function (initial, action, index) {
                        for (var instanceId in instances) {
                            if (instances.hasOwnProperty(instanceId)) {
                                if (action == undefined)
                                    initial += parseFloat(instances[instanceId][index]);
                                else
                                    initial += action(instances[instanceId]);
                            }
                        }
                        return initial
                    };
                    return getAccumulatedValue($scope, object, operand, 0, accumulatorFunction);
                };
            } else if (actionName == "Maximum") {
                return function (operand) {
                    var accumulatorFunction = function (initial, action, index) {
                        for (var instanceId in instances) {
                            if (instances.hasOwnProperty(instanceId)) {
                                if (action == undefined) {
                                    if (initial < parseFloat(instances[instanceId][index]))
                                        initial = parseFloat(instances[instanceId][index]);
                                }
                                else if (initial < action(instances[instanceId]))
                                    initial = action(instances[instanceId]);
                            }
                        }
                        return initial
                    };
                    return getAccumulatedValue($scope, object, operand, Number.MIN_VALUE, accumulatorFunction);
                };
            } else if (actionName == "Minimum") {
                return function (operand) {
                    var accumulatorFunction = function (initial, action, index) {
                        for (var instanceId in instances) {
                            if (instances.hasOwnProperty(instanceId)) {
                                if (action == undefined) {
                                    if (initial > parseFloat(instances[instanceId][index]))
                                        initial = parseFloat(instances[instanceId][index]);
                                }
                                else if (initial > action(parseFloat(instances[instanceId][index])))
                                    initial = action(parseFloat(instances[instanceId][index]));
                            }
                        }
                        return initial
                    };
                    return getAccumulatedValue($scope, object, operand, Number.MAX_VALUE, accumulatorFunction);
                };
            } else if (actionName == "Product of All") {
                return function (operand) {
                    var accumulatorFunction = function (initial, action, index) {
                        for (var instanceId in instances) {
                            if (instances.hasOwnProperty(instanceId)) {
                                if (action == undefined)
                                    initial *= parseFloat(instances[instanceId][index]);
                                else
                                    initial *= action(instances[instanceId]);
                            }
                        }
                        return initial
                    };
                    return getAccumulatedValue($scope, object, operand, 1, accumulatorFunction);
                };
            } else if (actionName == "Average") {
                return function (operand) {
                    var accumulatorFunction = function (initial, action, index) {
                        var size = 0;
                        for (var instanceId in instances) {
                            if (instances.hasOwnProperty(instanceId)) {
                                size += 1;
                                if (action == undefined)
                                    initial += parseFloat(instances[instanceId][index]);
                                else
                                    initial += action(instances[instanceId]);
                            }
                        }
                        return initial / size;
                    };
                    return getAccumulatedValue($scope, object, operand, 0, accumulatorFunction);
                };
            } else if (actionName == "Display") {
                return function (operand) {
                    var accumulatorFunction = function (initial, action, index) {
                        for (var instanceId in instances) {
                            if (instances.hasOwnProperty(instanceId)) {
                                if (action == undefined)
                                    initial += instances[instanceId][index] + ", ";
                                else
                                    initial += action(instances[instanceId]) + ", ";
                            }
                        }
                        initial = initial.substring(0, initial.length - 2);
                        return initial;
                    };
                    return getAccumulatedValue($scope, object, operand, "", accumulatorFunction);
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
                    fail();
                    $scope.$apply();
                }
                else if (result.readyState == 4 && result.status == 200){
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

var myParseFloat = function(n){
    if (isNumber(n)){
        return parseFloat(n);
    } else {
        return n;
    }
};

function generateUUID() {
    var d = new Date().getTime();
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
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
    ADD_OBJECT_INSTANCE: "/AddObjInstance",
    REMOVE_OBJECT_INSTANCE: "/RemoveObjInstance"
};
