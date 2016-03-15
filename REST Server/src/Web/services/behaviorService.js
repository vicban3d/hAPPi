/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('behaviorService',[function(){

    this.currentBehavior = [];
    this.numOfActions = 0;
    this.numOfConditions = 0;

    this.isValidActionBehavior = function(val){
        return val.operandObject != '' && val.operandAttribute != '' && val.operator != '';
    };

    this.getNumOfActions = function(){
        return this.numOfActions;
    };

    this.getNumOfConditions = function(){
        return this.numOfConditions;
    };

    this.addBehavior = function($scope, name, all_acts, all_conds){
        var newBehavior = {
            name: name,
            actions: all_acts,
            conditions: all_conds.filter($scope.isValidCondition)
        };
        $scope.addBehaviorToApplication(newBehavior);
        //$scope.showBehaviorDetails(newBehavior);
        $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_BEHAVIOR, angular.toJson(newBehavior)));
    };

    this.addCondition = function(){
        this.numOfConditions += 1;
    };

    this.addNewCondition = function(){
        this.numOfConditions += 1;
    };

    this.editBehaviorDetails = function($scope, behavior){
        this.currentBehavior = behavior;
    };

    this.getBehaviorAction = function($scope, object, actionName){
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
                    var action = $scope.getObjectAction(actionName, parseFloat(operand2));
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
                    var action = $scope.getObjectAction(actionName, parseFloat(operand2));
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
                    var action = $scope.getObjectAction(actionName, parseFloat(operand2));
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
                    var action = $scope.getObjectAction(actionName, parseFloat(operand2));
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
                    var action = $scope.getObjectAction(actionName, parseFloat(operand2));
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
            return "UNSUPPORTED";
        }
    };

    this.addNewBehavior = function($scope){
        //var empty = {name:'new behavior'};
        //$scope.showBehaviorDetails(empty);
    };



    this.deleteBehavior = function($scope, behavior){
        var index =  $scope.applications[$scope.getCurrentApplication().id].behaviors.indexOf(behavior);
        $scope.applications[$scope.getCurrentApplication().id].behaviors.splice(index, 1);
        if (behavior == this.currentBehavior){
            this.currentBehavior = {};
        }
        $scope.acceptMessageResult(sendPOSTRequest(Paths.REMOVE_BEHAVIOR, angular.toJson(behavior)));
    };

    this.showBehaviorDetails = function($scope, behavior){
        if ($scope.areaFlags["behaviorDetailsArea"] == false || behavior != this.currentBehavior) {
            this.currentBehavior = behavior;
        }
    };

    this.addActionBehavior = function(){
        this.numOfConditions = 0;
    };
}]);