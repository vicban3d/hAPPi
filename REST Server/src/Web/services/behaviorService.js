/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('behaviorService',[function(){

    this.currentBehavior = {
        id: '',
        name: '',
        actions: [],
        conditions: []
    };

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

    this.addBehavior = function($scope, name, all_acts, all_conditions){
        var id = $scope.generateUUID();
        var newBehavior = {
            id: id,
            name: name,
            actions: all_acts,
            conditions: all_conditions
        };
        $scope.addBehaviorToApplication(newBehavior);
        $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_BEHAVIOR, angular.toJson(newBehavior)));
    };

    this.addCondition = function(){
        this.numOfConditions += 1;
    };

    var getAccumulatedValue = function($scope, object, operand, initial, accumulatorFunction){
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


    this.getBehaviorAction = function($scope, object, actionName){
        if (actionName == "Sum of All"){
            return function (operand){
                var accumulatorFunction = function(initial, action, index) {
                    for (var i = 0; i < $scope.instances[object.name].length; i++) {
                        initial += action(parseFloat($scope.instances[object.name][i][index]));
                    }
                    return initial
                };
                return getAccumulatedValue($scope, object, operand, 0, accumulatorFunction);
            };
        } else if (actionName == "Maximum") {
            return function (operand){
                var accumulatorFunction = function(initial, action, index) {
                    for (var i = 0; i < $scope.instances[object.name].length; i++) {
                        if (initial < action(parseFloat($scope.instances[object.name][i][index]))) {
                            initial = action(parseFloat($scope.instances[object.name][i][index]));
                        }
                    }
                    return initial
                };
                return getAccumulatedValue($scope, object, operand, Number.MIN_VALUE, accumulatorFunction);
            };
        } else if (actionName == "Minimum"){
            return function (operand){
                var accumulatorFunction = function(initial, action, index) {
                    for (var i = 0; i < $scope.instances[object.name].length; i++) {
                        if (initial > action(parseFloat($scope.instances[object.name][i][index]))) {
                            initial = action(parseFloat($scope.instances[object.name][i][index]));
                        }
                    }
                    return initial
                };
                return getAccumulatedValue($scope, object, operand, Number.MAX_VALUE, accumulatorFunction);
            };
        } else if (actionName == "Product of All"){
            return function (operand){
                var accumulatorFunction = function(initial, action, index) {
                    for (var i = 0; i < $scope.instances[object.name].length; i++) {
                        initial *= action(parseFloat($scope.instances[object.name][i][index]));
                    }
                    return initial
                };
                return getAccumulatedValue($scope, object, operand, 1, accumulatorFunction);
            };
        } else if (actionName == "Average"){
            return function (operand){
                var accumulatorFunction = function(initial, action, index) {
                    for (var i = 0; i < $scope.instances[object.name].length; i++) {
                        initial += action(parseFloat($scope.instances[object.name][i][index]));
                    }
                    return initial/$scope.instances[object.name].length;
                };
                return getAccumulatedValue($scope, object, operand, 0, accumulatorFunction);
            };
        } else {
            return undefined;
        }
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

    this.editBehavior = function($scope){
        if ($scope.behaviorName == '' || $scope.behaviorName == 'Invalid Name!') {
            $scope.behaviorName = 'Invalid Name!'
        }
        else {
            $scope.all_acts_Behavior = $scope.all_acts_Behavior.filter($scope.isValidActionBehavior);
            $scope.all_conditions = $scope.all_conditions.filter($scope.isValidCondition);

            var newBehavior = {
                id: currentBehavior.id,
                name: $scope.behaviorName,
                actions:  $scope.all_acts_Behavior,
                conditions: $scope.all_conditions
            };

            $scope.message = "Updating behavior...";
            $scope.showArea("messageArea");
            removeBehaviorFromAppList($scope, currentApplication.id, currentBehavior);
            $scope.addBehaviorToApplication(newBehavior);
            $scope.acceptMessageResult(sendPostRequest(Paths.UPDATE_BEHAVIOR, angular.toJson(newBehavior)));
            this.showBehaviorDetails(newBehavior);
            this.currentBehavior = newBehavior;
            //TODO
        }

    };

    this.removeBehaviorFromAppList= function($scope, appId, behavior){
        for(var i = $scope.applications[appId].behaviors.length - 1; i >= 0; i--){
            if($scope.applications[appId].behaviors[i] == behavior){
                $scope.applications[appId].behaviors[i].splice(i,1);
            }
        }
    };

    this.editBehaviorDetails = function($scope, $event, behavior){
       $event.stopPropagation(); //TODO What?
        this.currentBehavior = behavior;
        this.behaviorName = $scope.currentBehavior.name;
        $scope.showArea("behaviorEditArea");
        $scope.hideArea("behaviorDetailsArea");
        alert("here" + this.currentBehavior.toString());
    };

}]);