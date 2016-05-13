/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('designService',[function(){
    this.currentInstance = [];
    this.showEmulatorMainPage = true;
    this.showAddInstance = false;
    this.emulatorOutput = '';

    this.getShowEmulatorMainPage = function(){
        return this.showEmulatorMainPage;
    };

    this.getShowAddInstance = function(){
        return this.showAddInstance;
    };

    this.gotoAppInstance = function(phoneNumber){
        this.phoneNumber = phoneNumber;
        this.showEmulatorMainPage = false;
        this.showAddInstance = false;
    };

    this.designDisplayObjectPage = function(object){
        this.currentInstance = object;
        this.emulatorOutput = '';
        this.showAddInstance = true;
    };

    this.designDisplayBehaviorPage = function(){
        this.showAddInstance = false;
    };

    this.addInstance = function($scope, attributes){
        if ($scope.instances[this.currentInstance.name] == undefined){
            $scope.instances[this.currentInstance.name] = [];
        }
        $scope.instances[this.currentInstance.name].push(attributes);
        $scope.attribute_values = [];

        var postBody = {
            id : this.phoneNumber,
            app_id: $scope.getCurrentApplication().id,
            objName: this.currentInstance.name,
            attributesList: attributes
        };
        $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.ADDOBJ_INSTANCE, angular.toJson(postBody)));
    };

    this.removeInstance = function($scope, idx){
        if($scope.isNumber(idx))
            $scope.instances[this.currentInstance.name].splice(parseInt(idx),1);
        else
            alert("Please choose index from the list!");

        var postBody = {
            id : this.phoneNumber,
            app_id: $scope.getCurrentApplication().id,
            objName: this.currentInstance.name,
            index: idx
        };

        $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.REMOVEOBJ_INSTANCE, angular.toJson(postBody)));
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