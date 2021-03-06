/**
 * Created by Victor on 10/03/2016.
 *
 */

main_module.service('behaviorService',['objectService', function(objectService){

    this.newBehavior = function(){
        return {
            id : '',
            name: '',
            applicationId: '',
            username: '',
            action: {
                operandObject: {},
                operator: '',
                operandAttribute: {},
                actionChain: {},
                conditions: []
            }
        };
    };
    
    this.currentBehavior = this.newBehavior();

    this.addBehavior = function(application){
        this.addBehaviorToApplication(application, this.currentBehavior);
    };

    this.editBehavior = function(application, username, behavior){
        this.removeBehaviorFromApplication(application, behavior.name);
        this.currentBehavior.id = behavior.id;
        this.currentBehavior.applicationId = application.id;
        this.currentBehavior.username = username;
        this.addBehaviorToApplication(application, this.currentBehavior);
    };

    this.deleteBehavior = function(application, behavior){
        this.removeBehaviorFromApplication(application, behavior.name);
        this.currentBehavior = {};
    };

    this.isValidBehavior = function($scope, behavior){
        var all_behaviors = $scope.getCurrentApplication().behaviors;
        if (all_behaviors == undefined){
            return true;
        }
        for (var i=0; i< all_behaviors.length; i++) {
            if (all_behaviors[i].name === behavior.name
                && all_behaviors[i].id !== behavior.id){
                $scope.behaviorCreateErrorMessage = "A Behavior by that name already exists!";
                return false;
            }
        }
        return true;
    };

    this.createBlankBehavior = function() {
        this.currentBehavior = this.newBehavior();
    };

    this.addBehaviorToApplication = function(application, behavior){
        application.behaviors.push(behavior);
    };

    this.removeBehaviorFromApplication = function(application, behaviorName){
        for(var i = application.behaviors.length - 1; i >= 0; i--){
            if(application.behaviors[i].name == behaviorName){
                application.behaviors.splice(i,1);
            }
        }
    };

    this.addCondition = function(){
        this.currentBehavior.action.conditions.push(
            {
                operandType: '',
                attribute: {},
                actionChain: {},
                logicOperation: '',
                value: ''}
        )
    };

    this.removeCondition = function($index){
        this.currentBehavior.action.conditions.splice($index, 1);
    };

    this.getBehaviorAction = function($scope, object, actionName, conditions){
        var instances = this.getInstancesFilteredByConditions($scope, $scope.instances[object.name], conditions, object);
        if (actionName == "Sum of All"){
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
            return function (operand){
                var accumulatorFunction = function(initial, action, index) {
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
        } else if (actionName == "Minimum"){
            return function (operand){
                var accumulatorFunction = function(initial, action, index) {
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
        } else if (actionName == "Product of All"){
            return function (operand){
                var accumulatorFunction = function(initial, action, index) {
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
        } else if (actionName == "Average"){
            return function (operand){
                var accumulatorFunction = function(initial, action, index) {
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
                    return initial/size;
                };
                return getAccumulatedValue($scope, object, operand, 0, accumulatorFunction);
            };
        } else if (actionName == "Display"){
            return function (operand){
                var accumulatorFunction = function(initial, action, index) {
                    for (var instanceId in instances) {
                        if (instances.hasOwnProperty(instanceId)) {
                            if (action == undefined)
                                initial += instances[instanceId][index] + ", ";
                            else
                                initial += action(instances[instanceId]) + ", ";
                        }
                    }
                    initial = initial.substring(0,initial.length - 2);
                    return initial;
                };
                return getAccumulatedValue($scope, object, operand, "", accumulatorFunction);
            };
        }
        else {
            return undefined;
        }
    };

    this.getInstancesFilteredByConditions = function($scope, instances, conditions, object){
        if(conditions == null || conditions.length ==  0){
            return instances;
        }
        var filteredInstances = [];
        var instanceValue;
        var maxInstanceValue = '';
        var maxInstances = [];
        var minInstanceValue = '';
        var minInstances = [];
        for (var i = 0 ; i < conditions.length; i++) {
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
                        if (maxInstanceValue === '' || instanceValue > maxInstanceValue){
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

    var getAccumulatedValue = function($scope, object, operand, initial, accumulatorFunction){
        var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
        var result = 0;
        if (index < 0){//not an attribute
            var actionChainIndex = object.actionChains.map(function(a) {return a.name;}).indexOf(operand);
            var actionChainName = object.actionChains[actionChainIndex].name;
            var action = $scope.getObjectAction(actionChainName, object);
            if (action == undefined){
                return undefined;
            }
            result = accumulatorFunction(initial, action, index);
        } else {
            result = accumulatorFunction(initial, undefined, index);
            // result = accumulatorFunction(initial, function (x){return x}, index);
        }
        return result;
    };

}]);