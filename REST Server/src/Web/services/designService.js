/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('designService',[function(){

    this.currentInstance = [];
    this.showInstance = false;
    this.showBehaviors = true;
    this.emulatorOutput = '';

    this.getShowInstance = function(){
        return this.showInstance;
    };

    this.getShowBehaviors = function(){
        return this.showBehaviors;
    };

    this.designDisplayObjectPage = function(object){
        this.currentInstance = object;
        this.emulatorOutput = '';
        this.showInstance = true;
        this.showBehaviors = false;
    };

    this.addInstance = function($scope, attributes){
        if ($scope.instances[this.currentInstance.name] == undefined){
            $scope.instances[this.currentInstance.name] = [];
        }
        $scope.instances[this.currentInstance.name].push(attributes);
        $scope.attribute_values = [];
    };

    this.removeInstance = function($scope, idx){
        $scope.instances[this.currentInstance.name].splice(parseInt(idx),1);
    };

    this.performBehaviorAction = function($scope, behavior){
        var object = behavior.actions[0].operandObject;
        var operand1 = behavior.actions[0].operandAttribute.name;
        var action = $scope.getBehaviorAction(object, behavior.actions[0].operator);
        if (action == undefined){
            this.emulatorOutput = "Unsupported Operation"
        } else {
            this.emulatorOutput = action(operand1);
        }
    };

    this.getCurrentInstance = function(){
        return this.currentInstance;
    };

    this.getOutput = function(){
        return this.emulatorOutput;
    };
}]);