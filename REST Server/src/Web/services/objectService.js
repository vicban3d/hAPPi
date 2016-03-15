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
        var objectId = generateUUID();
        var all_attributes = all_attrs.filter(this.isValidAttribute);
        var all_actions = all_acts.filter(this.isValidActionObject);
        var newObject = {
            id: objectId,
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


    this.editObject = function($scope){
        if ($scope.objectName == '' || $scope.objectName == 'Invalid Name!') {
            $scope.objectName = 'Invalid Name!'
        }
        else {
            $scope.all_attributes = $scope.all_attrs.filter($scope.isValidAttribute);
            $scope.all_acts_Object = $scope.all_acts_Object.filter($scope.isValidActionObject);


            var newObject = {
                id: currentObject.id,
                name: $scope.objectName,
                attributes:  $scope.all_attributes,
                actions: $scope.all_acts_Object
            };

            $scope.message = "Updating object...";
            $scope.showArea("messageArea");
            removeObjectFromAppList($scope, currentApplication.id, currentObject);
            $scope.addObjectToApplication(newObject);
            $scope.acceptMessageResult(sendPostRequest(Paths.UPDATE_OBJECT, angular.toJson(newObject)));
            this.showObjectDetails(newObject);
            this.currentObject = newObject;
            alert(currentObject);
            alert(newObject);


            //TODO
        }

        };

    this.editObjectDetails = function($scope, $event, object){
        $event.stopPropagation();
        this.currentObject = object;
        this.objectName = $scope.currentObject.name;
        $scope.showArea("objectEditArea");
        $scope.hideArea("objectDetailsArea");
    };

    this.removeObjectFromAppList= function($scope, appId, object){
        for(var i = $scope.applications[appId].objects.length - 1; i >= 0; i--){
            if($scope.applications[appId].objects[i] == object){
                $scope.applications[appId].objects[i].splice(i,1);
            }
        }
    };

    var generateUUID = function() {
        var d = new Date().getTime();
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
    };

}]);