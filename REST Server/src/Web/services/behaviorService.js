/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('behaviorService',[function(){

    this.currentBehavior =
    {
        id : '',
        name: '',
        operandObject: {},
        operator: '',
        operandAttribute: {},
        conditions: []
    };

    this.addNewBehavior = function() {
        this.currentBehavior =
        {
            id : '',
            name: '',
            operandObject: {},
            operator: '',
            operandAttribute: {},
            conditions: []
        };
    };

    this.addBehavior = function(application){
        this.currentBehavior.id = generateUUID();
        addBehaviorToApplication(application, this.currentBehavior);
    };

    this.editBehavior = function(application, behavior){
        removeBehaviorFromApplication(application, behavior.name);
        addBehaviorToApplication(application, this.currentBehavior);
    };

    this.deleteBehavior = function(application, behavior){
        removeBehaviorFromApplication(application, behavior.name);
        this.currentBehavior = {};
    };

    var addBehaviorToApplication = function(application, behavior){
        application.behaviors.push(behavior);
    };

    var removeBehaviorFromApplication = function(application, behaviorName){
        for(var i = application.behaviors.length - 1; i >= 0; i--){
            if(application.behaviors[i].name == behaviorName){
                application.behaviors.splice(i,1);
            }
        }
    };

    this.addCondition = function(){
        this.currentBehavior.conditions.push({attribute: {}, logicOperation: '', value: ''})
    };

    this.removeCondition = function($index){
        this.currentBehavior.conditions.splice($index, 1);
    };

    var getAccumulatedValue = function($scope, object, operand, initial, accumulatorFunction){
        var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand.name);
        var result = 0;
        if (index < 0){
            var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand.name);
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

    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

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

    this.isValidBehavior = function($scope){
        var all_behaviors = $scope.getCurrentApplication().behaviors;
        if (all_behaviors == undefined){
            return true;
        }
        for (var i=0; i< all_behaviors.length; i++) {
            if (all_behaviors[i].name.valueOf() == this.currentBehavior.name.valueOf()
                && all_behaviors[i].id.valueOf() != this.currentBehavior.id.valueOf()){
                $scope.createErrorMessage = "A Behavior by that name already exists!";
                return false;
            }
        }
        return true;
    }

}]);