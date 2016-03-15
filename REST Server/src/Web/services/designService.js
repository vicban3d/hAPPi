/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('designService',[function(){

    this.designDisplayObjectPage = function(object){
        this.currentInstance = object;
        this.emulatorOutput = '';
    };

    this.addInstance = function($scope){
        if ($scope.instances[this.currentInstance.name] == undefined){
            $scope.instances[this.currentInstance.name] = [];
        }
        $scope.instances[this.currentInstance.name].push(this.attribute_values);
        this.attribute_values = [];
    };

    this.removeInstance = function($scope, id){
        $scope.instances[this.currentInstance.name].splice(parseInt(id),1);
    };

    this.performBehaviorAction = function($scope, behavior){
        var object = behavior.actions[0].operandObject;
        var operand1 = behavior.actions[0].operandAttribute.name;
        var action = $scope.getBehaviorAction(object, behavior.actions[0].operator);
        this.emulatorOutput = action(operand1);
    };

    this.getCurrentInstance = function(){
        return this.currentInstance;
    };

    this.getOutput = function(){
        return this.emulatorOutput;
    };
}]);