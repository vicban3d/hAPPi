/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('objectService',[function(){

    this.numOfAttributes = 0;
    this.numOfActions = 0;

    this.currentObject = {
        id: '',
        name: '',
        attributes: [],
        actions: []
    };

    this.getNumOfAttributes = function(){
        return this.numOfAttributes;
    };

    this.getNumOfActions = function(){
        return this.numOfActions;
    };

    this.addObject = function($scope, name, all_attrs, all_acts) {
        if ($scope.objectName == '' || $scope.objectName == 'Invalid Name!') {
            $scope.objectName = 'Invalid Name!'
        }
        else{
            var objectId = $scope.generateUUID();
            var all_attributes = all_attrs.filter(this.isValidAttribute);
            var all_actions = all_acts.filter(this.isValidActionObject);
            var newObject = {
                id: objectId,
                name: name,
                attributes:  all_attributes,
                actions: all_actions
            };
            $scope.addObjectToApplication(newObject);
            this.currentObject = newObject;
            $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_OBJECT, angular.toJson(newObject)));
        }
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
        if ($scope.areaFlags != undefined && $scope.areaFlags["objectDetailsArea"] == false || object != this.currentObject){
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
        return undefined;
    };

    this.editObject = function($scope, name, all_attrs, all_acts){
        if ($scope.objectName == '' || $scope.objectName == 'Invalid Name!') {
            $scope.objectName = 'Invalid Name!'
        }
        else {
            var all_attributes = all_attrs.filter(this.isValidAttribute);
            var all_actions = all_acts.filter(this.isValidActionObject);
            var newObject = {
                id: this.currentObject.id,
                name: $scope.objectName,
                attributes:  all_attributes,
                actions: all_actions
            };
            $scope.message = "Updating object...";//TODO
            $scope.showArea("messageArea");
            $scope.removeObjectFromAppList($scope, $scope.currentApplication.id, this.currentObject);
            $scope.addObjectToApplication(newObject);
            this.currentObject = newObject;
            $scope.acceptMessageResult(sendPOSTRequest(Paths.UPDATE_OBJECT, angular.toJson(newObject)));
            $scope.showObjectDetails(newObject);
        }
        };

    this.editObjectDetails = function($scope, $event, object){
        $event.stopPropagation();
        this.currentObject = object;
        $scope.objectName = this.currentObject.name;
        $scope.hideArea("objectCreateArea");
        $scope.showArea("objectEditArea");
        $scope.hideArea("objectDetailsArea");
        //$scope.hideArea("objectAddArea");
    };

    this.removeObjectFromAppList= function($scope, appId){
        for(var i = $scope.applications[appId].objects.length - 1; i >= 0; i--){
            if($scope.applications[appId].objects[i] == this.currentObject){
                $scope.applications[appId].objects.splice(i,1);
            }
        }
    };
}]);