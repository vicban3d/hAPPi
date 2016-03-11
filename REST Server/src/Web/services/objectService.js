/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('objectService',[function(){



    this.addObject = function($scope) {
        alert($scope.objectName + " " + $scope.all_attrs + " " + $scope.all_acts_Object);
        if ($scope.objectName == '' || $scope.objectName == 'Invalid Name!') {
            $scope.objectName = 'Invalid Name!'
        }
        else {
            $scope.all_attributes = $scope.all_attrs.filter($scope.isValidAttribute);
            $scope.all_acts_Object = $scope.all_acts_Object.filter($scope.isValidActionObject);

            var newObject = {
                name: $scope.objectName,
                attributes:  $scope.all_attributes,
                actions: $scope.all_acts_Object
            };

            $scope.addObjectToApplication(newObject);
            $scope.all_attrs = [];
            $scope.all_acts_Object = [];
            $scope.numOfAttributes = 0;
            $scope.numOfActions = 0;
            $scope.numOfConditions = 0;
            $scope.all_conditions = [];
            $scope.objectName = '';
            this.showObjectDetails(newObject);
            $scope.hideArea("actionsEditAreaObject");
            $scope.hideArea("conditionEditArea");
            $scope.hideArea("actionsEditAreaBehavior");
            $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_OBJECT, angular.toJson(newObject)));
        }
    };

    this.deleteObject = function($scope, object){
        var index =  $scope.applications[$scope.currentApplication.id].objects.indexOf(object);
        $scope.applications[$scope.currentApplication.id].objects.splice(index, 1);
        if (object == $scope.currentObject){
            $scope.currentObject = {};
        }
        $scope.acceptMessageResult(sendPOSTRequest(Paths.REMOVE_OBJECT, angular.toJson(object)));
        $scope.hideArea("objectDetailsArea");
    };

    this.addAttribute = function($scope){
        $scope.numOfAttributes+=1;
    };

    this.showObjectDetails = function($scope, object){
        if ($scope.areaFlags != undefined && $scope.areaFlags["objectDetailsArea"] == false || object != $scope.currentObject){
            $scope.currentObject = object;
            $scope.hideArea("behaviorDetailsArea");
            $scope.hideArea("behaviorEditArea");
            $scope.hideArea("objectEditArea");
            $scope.showArea("objectDetailsArea");
        }
    };

    this.addNewAction = function($scope){
        if ($scope.actionName == '' || $scope.actionName =='Invalid Name!'){
            $scope.actionName = 'Invalid Name!'
        }
        else {
            $scope.numOfActions += 1;
            $scope.hideArea("actionsEditAreaObject");
            $scope.hideArea("actionsEditAreaBehavior");
        }
    };

    this.isValidAttribute = function(val){
        return val.name != '' && val.type != '';
    };

    this.addActionObject = function($scope){
        //$scope.showArea("actionsEditAreaObject");
        $scope.numOfActions += 1;
    };

    this.getAttributeName = function(val){
        return val.name;
    };

    this.isValidActionObject = function(val){
        return val.name != '' && val.operand1 != '' && val.operator != '' && val.operand2 != '';
    };

    this.isValidActionBehavior = function(val){
        return val.operandObject != '' && val.operandAttribute != '' && val.operator != '';
    };

    this.addNewObject = function($scope) {
        var empty = {name: 'new object', attributes: [{name: 'attr', type: 'number'}]};
        $scope.showObjectDetails(empty);
        $scope.hideArea("behaviorDetailsArea");
        $scope.hideArea("behaviorEditArea");
        $scope.hideArea("objectDetailsArea");
        $scope.showArea("objectEditArea");
    };



}]);