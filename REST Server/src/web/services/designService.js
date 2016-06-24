/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('designService',[function(){
    this.currentInstance = {};
    this.showEmulatorMainPage = true;
    this.showAddInstance = false;
    this.emulatorOutput = '';

    this.getShowEmulatorMainPage = function(){
        return this.showEmulatorMainPage;
    };

    this.getShowAddInstance = function(){
        return this.showAddInstance;
    };

    this.gotoAppInstance = function($scope, phoneNumber){ //book
        this.phoneNumber = phoneNumber;
        this.showEmulatorMainPage = false;
        this.showAddInstance = false;
        var jsonObj = {app_id: $scope.getCurrentApplication().id, id : phoneNumber};
        $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.GETOBJ_INSTANCE,angular.toJson(jsonObj)),
                            function (res) {
                                if (res !== undefined)
                                    $scope.instances = JSON.parse(res).object_map;
                            },null);
    };

    this.designDisplayObjectPage = function(object){
        this.currentInstance = object;
        this.emulatorOutput = '';
        this.showAddInstance = true;
    };

    this.designDisplayBehaviorPage = function(){
        this.currentInstance = {};
        this.showAddInstance = false;
    };

    this.addInstance = function($scope, attributes){
        if ($scope.instances[this.currentInstance.name] == undefined){
            $scope.instances[this.currentInstance.name] = {};
        }
        var insId = generateUUID();
        $scope.instances[this.currentInstance.name][insId] = attributes;
        $scope.attribute_values = [];

        var postBody = {
            id : this.phoneNumber,
            app_id: $scope.getCurrentApplication().id,
            objName: this.currentInstance.name,
            attributesList: attributes,
            insId : insId
        };
        $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.ADDOBJ_INSTANCE, angular.toJson(postBody)),
            function () {},
            function () {alert("Failed to add instance!")}
        );
    };

    this.removeInstance = function($scope, insId){
        //$scope.instances[this.currentInstance.name][this.currentInstance.insId].splice(parseInt(idx),1);
        delete $scope.instances[this.currentInstance.name][insId];
        var postBody = {
            id : this.phoneNumber,
            app_id: $scope.getCurrentApplication().id,
            objName: this.currentInstance.name,
            insId: insId
        };

        $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.REMOVEOBJ_INSTANCE, angular.toJson(postBody)));
    };

    this.performBehaviorAction = function($scope, behavior){
        var object = behavior.action.operandObject;
        var operand1 = behavior.action.operandAttribute == null ? undefined : behavior.action.operandAttribute.name;
        var chain = behavior.action.actionChain == null ? undefined : behavior.action.actionChain.name;
        var action = $scope.getBehaviorAction(object, behavior.action.operator, behavior.action.conditions);
        if (action == undefined){
            this.emulatorOutput = "Unsupported Operation"
        } else if (chain == undefined){
            this.emulatorOutput = action(operand1);
        }
        else if(operand1 == undefined){
            this.emulatorOutput = action(chain);
        }
    };

    this.getCurrentInstance = function(){
        return this.currentInstance;
    };

    this.getOutput = function(){
        return this.emulatorOutput;
    };
}]);