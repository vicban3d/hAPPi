/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('behaviorService',[function(){

    this.isValidActionBehavior = function(val){
        return val.operandObject != '' && val.operandAttribute != '' && val.operator != '';
    };

    this.addBehavior = function($scope){
        if ($scope.behaviorName == '' || $scope.behaviorName =='Invalid Name!'){
            $scope.behaviorName = 'Invalid Name!'
        }
        else {
            var newBehavior = {
                name: $scope.behaviorName,
                actions: $scope.all_acts_Behavior.filter($scope.isValidActionBehavior),
                conditions: $scope.all_conditions.filter($scope.isValidCondition)
            };
            $scope.addBehaviorToApplication(newBehavior);
            $scope.all_acts_Behavior = [];
            $scope.all_conditions = [];
            $scope.numOfActions = 0;
            $scope.numOfConditions = 0;
            $scope.behaviorName = '';
            $scope.showBehaviorDetails(newBehavior);
            $scope.hideArea("actionsEditAreaObject");
            $scope.hideArea("actionsEditAreaBehavior");
            $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_BEHAVIOR, angular.toJson(newBehavior)));
        }
    };

    this.addCondition = function(){
        $scope.numOfConditions+=1;
    };

    this.editBehaviorDetails = function($scope, behavior){
        $scope.currentBehavior = behavior;
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
                    var action = getObjectAction(actionName, parseFloat(operand2));
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

    this.editBehaviorDetails = function($scope, behavior){
        $scope.currentBehavior = behavior;
    };

    this.addNewBehavior = function($scope){
        var empty = {name:'new behavior'};
        $scope.showBehaviorDetails(empty);
        $scope.hideArea("objectDetailsArea");
        $scope.hideArea("objectEditArea");
        $scope.hideArea("behaviorDetailsArea");
        $scope.showArea("behaviorEditArea");
    };

    this.addNewCondition = function($scope){
        if ($scope.conditionName == '' || $scope.conditionName =='Invalid Name!'){ //TODO - check what invalid
            $scope.conditionName = 'Invalid Name!'
        }
        else {
            $scope.numOfConditions += 1;
            $scope.hideArea("actionsEditAreaObject");
            $scope.hideArea("actionsEditAreaBehavior");
        }
    };

    this.deleteBehavior = function($scope, behavior){
        var index =  $scope.applications[appService.getCurrentApplication().id].behaviors.indexOf(behavior);
        $scope.applications[appService.getCurrentApplication().id].behaviors.splice(index, 1);
        if (behavior == $scope.currentBehavior){
            $scope.currentBehavior = {};
        }
        $scope.acceptMessageResult(sendPOSTRequest(Paths.REMOVE_BEHAVIOR, angular.toJson(behavior)));
        $scope.hideArea("behaviorDetailsArea");
    };

    this.showBehaviorDetails = function($scope, behavior){
        if ($scope.areaFlags["behaviorDetailsArea"] == false || behavior != $scope.currentBehavior) {
            $scope.currentBehavior = behavior;
            $scope.hideArea("objectDetailsArea");
            $scope.hideArea("objectEditArea");
            $scope.hideArea("behaviorEditArea");
            $scope.showArea("behaviorDetailsArea");
        }
    };

    this.addActionBehavior = function($scope){
        $scope.showArea("actionsEditAreaBehavior");
        $scope.numOfConditions = 0;
        $scope.all_conditions = [];
    };
}]);