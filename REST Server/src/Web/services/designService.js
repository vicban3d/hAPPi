/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('designService',[function(){

    this.currentInstance = [];
    this.showEmulatorMainPage = true;
    this.emulatorOutput = '';

    this.getShowEmulatorMainPage = function(){
        return this.showEmulatorMainPage;
    };

    this.designDisplayObjectPage = function(object){
        this.currentInstance = object;
        this.emulatorOutput = '';
        this.showEmulatorMainPage = false;
    };

    this.addInstance = function($scope, attributes){
        if ($scope.instances[this.currentInstance.name] == undefined){
            $scope.instances[this.currentInstance.name] = [];
        }
        $scope.instances[this.currentInstance.name].push(attributes);
        $scope.attribute_values = [];
    };

    this.removeInstance = function($scope, idx){
        if($scope.isNumber(idx))
            $scope.instances[this.currentInstance.name].splice(parseInt(idx),1);
        else
            alert("Please choose index from the list!");
    };

    this.performBehaviorAction = function($scope, behavior){
        var object = behavior.operandObject;
        var operand1 = behavior.operandAttribute.name;
        var action = $scope.getBehaviorAction(object, behavior.operator, behavior.conditions);
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