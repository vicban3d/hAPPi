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
        if ($scope.behaviorName == '' || $scope.behaviorName == 'Invalid Name!') {
            $scope.behaviorName = 'Invalid Name!'
        }
        else{
            var behaviorId = $scope.generateUUID();
            var newBehavior = {
                id: behaviorId,
                name: name,
                actions: all_acts,
                conditions: all_conditions
            };
            $scope.addBehaviorToApplication(newBehavior);
            this.currentBehavior = newBehavior;
            $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_BEHAVIOR, angular.toJson(newBehavior)));
        }
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

    this.getInstancesFilteredByConditions = function(instances, conditions, object){
        if(conditions == null || conditions.length ==  0){
            return instances;
        }
        var filteredInstances = [];
        for (i = 0 ; i < conditions.length; i++){
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
            temp.forEach(function(value){
                if (filteredInstances.indexOf(value)==-1) filteredInstances.push(value);
            });
        }
        return filteredInstances;
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
        if ($scope.areaFlags != undefined && $scope.areaFlags["behaviorDetailsArea"] == false || behavior != this.currentBehavior) {
            this.currentBehavior = behavior;
        }
    };

    this.addActionBehavior = function(){
        this.numOfConditions = 0;
    };


    this.addNewBehavior = function() {
            var empty = {name: 'new behavior', actions: [{operandObject: 'operand', operator: 'operator', operandAttribute: 'operand'}]}; // TODO add condition
            this.showBehaviorDetails(empty);
        };

    this.editBehavior = function($scope, name, all_acts, all_conditions){
        if ($scope.behaviorName == '' || $scope.behaviorName == 'Invalid Name!') {
            $scope.behaviorName = 'Invalid Name!'
        }
        else {
            var all_acts_Behavior = all_acts.filter($scope.isValidActionBehavior);
            var all_conditions = all_conditions.filter($scope.isValidCondition);
            var newBehavior = {
                id: this.currentBehavior.id,
                name: $scope.behaviorName,
                actions:  all_acts_Behavior,
                conditions: all_conditions
            };
            $scope.message = "Updating behavior..."; //TODO
            $scope.showArea("messageArea");
            $scope.removeBehaviorFromAppList($scope, $scope.currentApplication.id, this.currentBehavior);
            $scope.addBehaviorToApplication(newBehavior);
            this.currentBehavior = newBehavior;
            $scope.acceptMessageResult(sendPostRequest(Paths.UPDATE_BEHAVIOR, angular.toJson(newBehavior)));
            $scope.showBehaviorDetails(newBehavior);
        }
    };

    this.editBehaviorDetails = function($scope, $event, behavior){
        $event.stopPropagation();
        this.currentBehavior = behavior;
        $scope.behaviorName = this.currentBehavior.name;
        $scope.hideArea("behaviorCreateArea");
        $scope.showArea("behaviorEditArea");
        $scope.hideArea("behaviorDetailsArea");
        //$scope.hideArea("behaviorAddArea");
    };

    this.removeBehaviorFromAppList= function($scope, appId){
        for(var i = $scope.applications[appId].behaviors.length - 1; i >= 0; i--){
            if($scope.applications[appId].behaviors[i] == this.currentBehavior){
                $scope.applications[appId].behaviors[i].splice(i,1);
            }
        }
    };
}]);