var main_module = angular.module('main', []);

main_module.controller('ctrl_main', ['$scope',
    function($scope) {
        $scope.applicationName = "<[NAME]>";
        $scope.objects = [];
        $scope.behaviors = [];
        $scope.currentInstance = '';
        $scope.showBehaviors = true;
        $scope.showInstance = false;
        $scope.attribute_values = [];
        $scope.instances = [];
        $scope.emulatorOutput = '';

        $scope.initializeDataStructures = function(){
            var objectsString = "<[OBJECTS]>";
            var behaviorsString = "<[BEHAVIORS]>";
            $scope.objects = JSON.parse(objectsString);
            $scope.behaviors = JSON.parse(behaviorsString);
        };

        $scope.designDisplayObjectPage = function(object){
            $scope.currentInstance = object;
            $scope.showBehaviors = false;
            $scope.showInstance = true;
        };

        $scope.designDisplayBehaviorPage = function(){
            $scope.showBehaviors = true;
            $scope.showInstance = false;
        };

        $scope.addInstance = function(){
            if ($scope.instances[$scope.currentInstance.name] == undefined){
                $scope.instances[$scope.currentInstance.name] = [];
            }
            $scope.instances[$scope.currentInstance.name].push($scope.attribute_values);
            $scope.attribute_values = [];
        };

        $scope.removeInstance = function(idx){
            $scope.instances[$scope.currentInstance.name].splice(parseInt(idx),1);
        };

        $scope.performBehaviorAction = function(behavior){
            var object = behavior.actions[0].operandObject;
            var operand1 = behavior.actions[0].operandAttribute.name;
            var action = getBehaviorAction(object, behavior.actions[0].operator);
            if (action == undefined){
                $scope.emulatorOutput = "Unsupported Operation"
            } else {
                $scope.emulatorOutput = action(operand1);
            }
        };

        function getObjectAction(actionName, operand2){
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
        }

        function getBehaviorAction(object, actionName){
            if (actionName == "Sum of All"){
                return function (operand){
                    var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
                    var i = 0;
                    var result = 0;
                    if (index < 0){
                        var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand);
                        var actionName = object.actions[actionIndex].operator;
                        var operand1 = object.actions[actionIndex].operand1.name;
                        var operand2 = object.actions[actionIndex].operand2;
                        index = object.attributes.map(function(a) {return a.name;}).indexOf(operand1);
                        var action = getObjectAction(actionName, parseFloat(operand2));
                        if (action == undefined){
                            return undefined;
                        }
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            result += action(parseFloat($scope.instances[object.name][i][index]));
                        }
                    } else {
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            result += parseFloat($scope.instances[object.name][i][index]);
                        }
                    }
                    return result;
                };
            } else if (actionName == "Maximum") {
                return function (operand){
                    var result = 0;
                    var i=0;
                    var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
                    if (index < 0){
                        var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand);
                        var actionName = object.actions[actionIndex].operator;
                        var operand1 = object.actions[actionIndex].operand1.name;
                        var operand2 = object.actions[actionIndex].operand2;
                        index = object.attributes.map(function(a) {return a.name;}).indexOf(operand1);
                        var action = getObjectAction(actionName, parseFloat(operand2));
                        if (action == undefined){
                            return undefined;
                        }
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            if (result < action(parseFloat($scope.instances[object.name][i][index]))) {
                                result = action(parseFloat($scope.instances[object.name][i][index]));
                            }
                        }
                    } else {
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            if (result < parseFloat($scope.instances[object.name][i][index])) {
                                result = parseFloat($scope.instances[object.name][i][index]);
                            }
                        }
                    }
                    return result;
                };
            } else if (actionName == "Minimum"){
                return function (operand){
                    var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
                    var result = Number.MAX_VALUE;
                    var i = 0;
                    if (index < 0){
                        var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand);
                        var actionName = object.actions[actionIndex].operator;
                        var operand1 = object.actions[actionIndex].operand1.name;
                        var operand2 = object.actions[actionIndex].operand2;
                        index = object.attributes.map(function(a) {return a.name;}).indexOf(operand1);
                        var action = getObjectAction(actionName, parseFloat(operand2));
                        if (action == undefined){
                            return undefined;
                        }
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            if (result > action(parseFloat($scope.instances[object.name][i][index]))) {
                                result = action(parseFloat($scope.instances[object.name][i][index]));
                            }
                        }
                    } else {
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            if (result > parseFloat($scope.instances[object.name][i][index])) {
                                result = parseFloat($scope.instances[object.name][i][index]);
                            }
                        }
                    }
                    return result;
                };
            } else if (actionName == "Product of All"){
                return function (operand){
                    var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
                    var result = 1;
                    var i = 0;
                    if (index < 0){
                        var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand);
                        var actionName = object.actions[actionIndex].operator;
                        var operand1 = object.actions[actionIndex].operand1.name;
                        var operand2 = object.actions[actionIndex].operand2;
                        index = object.attributes.map(function(a) {return a.name;}).indexOf(operand1);
                        var action = getObjectAction(actionName, parseFloat(operand2));
                        if (action == undefined){
                            return undefined;
                        }
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            result *= action(parseFloat($scope.instances[object.name][i][index]));
                        }
                    } else {
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            result *= parseFloat($scope.instances[object.name][i][index]);
                        }
                    }
                    return result;
                };
            } else if (actionName == "Average"){
                return function (operand){
                    var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
                    var result = 0;
                    var i = 0;
                    if (index < 0){
                        var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand);
                        var actionName = object.actions[actionIndex].operator;
                        var operand1 = object.actions[actionIndex].operand1.name;
                        var operand2 = object.actions[actionIndex].operand2;
                        index = object.attributes.map(function(a) {return a.name;}).indexOf(operand1);
                        var action = getObjectAction(actionName, parseFloat(operand2));
                        if (action == undefined){
                            return undefined;
                        }
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            result += action(parseFloat($scope.instances[object.name][i][index]));
                        }
                    } else {
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            result += parseFloat($scope.instances[object.name][i][index]);
                        }
                    }
                    return result/$scope.instances[object.name].length;
                };
            } else {
                return undefined;
            }
        }
    }]);
