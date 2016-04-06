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

    this.addObject = function($scope) {
        this.currentObject.id = $scope.generateUUID();
        this.currentObject.attributes = this.currentObject.attributes.filter(this.isValidAttribute);
        this.currentObject.actions = this.currentObject.actions.filter(this.isValidActionObject);
        $scope.addObjectToApplication(this.currentObject);
        $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_OBJECT, angular.toJson(this.currentObject)));
    };

    this.deleteObject = function($scope, object){
        var index =  $scope.getCurrentApplication().objects.indexOf(object);
        $scope.getCurrentApplication().objects.splice(index, 1);
        if (object == this.currentObject){
            this.currentObject = {};
        }
        if (!$scope.getCurrentApplication().objects.length){
            $scope.showNoMembersImage = true;
        }
        $scope.acceptMessageResult(sendPOSTRequest(Paths.REMOVE_OBJECT, angular.toJson(object)));
    };

    this.addAttribute = function(){
        this.currentObject.attributes.push({name: '', type: 'Text'});
    };

    this.removeAttribute = function($index){
      this.currentObject.attributes.splice($index, 1);
    };

     this.removeAction = function($index){
        this.currentObject.actions.splice($index, 1);
    };

    this.isValidAttribute = function(val){
        return val.name != '' && val.type != '';
    };

    this.addActionObject = function(){
        this.currentObject.actions.push({name: '', operand1: '', operator: '', operand2: ''});
    };

    this.getAttributeName = function(val){
        return val.name;
    };

    this.isValidActionObject = function(val){
        return val.name != '' && val.operand1 != '' && val.operator != '' && val.operand2 != '';
    };

    this.addNewObject = function() {
        this.currentObject = {name: '', attributes: [], actions: []};
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

    this.editObject = function($scope, object){
        $scope.message = "Updating object...";
        this.removeObjectFromApplication($scope, object.name);
        $scope.addObjectToApplication(this.currentObject);
        $scope.acceptMessageResult(sendPOSTRequest(Paths.UPDATE_OBJECT, angular.toJson(this.currentObject)));

    };

    this.showObjectEditArea = function($scope, $event, object){
        $event.stopPropagation();
        
        this.currentObject = object;
        $scope.objectName = this.currentObject.name;
        
    };

    this.removeObjectFromApplication= function($scope, objectName){
        for(var i = $scope.getCurrentApplication().objects.length - 1; i >= 0; i--){
            if($scope.getCurrentApplication().objects[i].name == objectName){
                $scope.getCurrentApplication().objects.splice(i,1);
            }
        }
    };
}]);