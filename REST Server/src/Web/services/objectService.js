/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('objectService',[function(){

    this.numOfAttributes = 0;
    this.numOfActions = 0;

    this.currentObject = {
        name: '',
        attributes:  [],
        actions: []
    };

    this.getNumOfAttributes = function(){
        return this.numOfAttributes;
    };

    this.getNumOfActions = function(){
        return this.numOfActions;
    };

    this.addObject = function($scope, name, all_attrs, all_acts) {
        var all_attributes = all_attrs.filter(this.isValidAttribute);
        var all_actions = all_acts.filter(this.isValidActionObject);
        var newObject = {
            name: name,
            attributes:  all_attributes,
            actions: all_actions
        };
        $scope.addObjectToApplication(newObject);
        //this.showObjectDetails(newObject);
        $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_OBJECT, angular.toJson(newObject)));
    };

    this.deleteObject = function($scope, object){
        var index =  $scope.applications[$scope.currentApplication.id].objects.indexOf(object);
        $scope.applications[$scope.currentApplication.id].objects.splice(index, 1);
        if (object == this.currentObject){
            this.currentObject = {};
        }
        $scope.acceptMessageResult(sendPOSTRequest(Paths.REMOVE_OBJECT, angular.toJson(object)));
    };

    this.addAttribute = function(){
        this.numOfAttributes+=1;
    };

    this.showObjectDetails = function($scope, object){
        if ($scope.areaFlags != undefined && $scope.areaFlags["objectDetailsArea"] == false || object != $scope.currentObject){
            this.currentObject = object;
        }
    };

    this.addNewAction = function(){
            this.numOfActions += 1;
    };

    this.isValidAttribute = function(val){
        return val.name != '' && val.type != '';
    };

    this.addActionObject = function(){
        //$scope.showArea("actionsEditAreaObject");
        this.numOfActions += 1;
    };

    this.getAttributeName = function(val){
        return val.name;
    };

    this.isValidActionObject = function(val){
        return val.name != '' && val.operand1 != '' && val.operator != '' && val.operand2 != '';
    };

    this.addNewObject = function() {
        var empty = {name: 'new object', attributes: [{name: 'attr', type: 'number'}]};
        this.showObjectDetails(empty);
    };

    this.getObjectAction = function(actionName, operand2){
        if (actionName == "Increase By") {
            return function (operand) {
                return operand + operand2;
            };
        }
        if (actionName == "Reduce By") {
            return function (operand) {
                return operand - operand2;
            };
        }
        if (actionName == "Multiply By") {
            return function (operand) {
                return operand * operand2;
            };
        }
        if (actionName == "Divide By") {
            return function (operand) {
                return operand / operand2;
            };
        }
        if (actionName == "Change To") {
            return function () {
                return operand2;
            };
        }
    }
}]);