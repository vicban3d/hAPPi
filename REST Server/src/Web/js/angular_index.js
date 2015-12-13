angular.module('main', [])
    .controller('ctrl_main', [
        '$scope',
        function($scope){
            $scope.attributes = 0;
            $scope.currentObject = '';
            $scope.all_attrs = [];
            $scope.showObjectDetailsFlag = false;
            $scope.showObjectEditFlag = false;
            $scope.objects = [];
            $scope.showAddObjects = false;
            $scope.showCreateApplication = false;
            $scope.currentBehavior = '';
            $scope.showBehaviorDetailsFlag = false;
            $scope.showBehaviorEditFlag = false;
            $scope.behaviors = [];
            $scope.showAddBehaviors = false;

            $scope.addObject = function(){
                if ($scope.name == '' || $scope.name =='Invalid Name!'){
                    $scope.name = 'Invalid Name!'
                }
                else {
                    var newObject = {name: $scope.name, attributes: $scope.all_attrs.filter($scope.isValidAttribute)};
                    $scope.objects.push(newObject);
                    $scope.all_attrs = [];
                    $scope.attributes = 0;
                    $scope.name = '';
                    $scope.showObjectDetails(newObject);
                    sendPOSTRequest(Paths.CREATE_ENTITY, JSON.stringify(newObject));
                }
            };

            $scope.addBehavior = function(){
                if ($scope.name == '' || $scope.name =='Invalid Name!'){
                    $scope.name = 'Invalid Name!'
                }
                else {
                    var newBehavior = {name: $scope.name};
                    $scope.behaviors.push(newBehavior);
                    $scope.name = '';
                    $scope.showBehaviorDetails(newBehavior);
                    sendPOSTRequest(Paths.CREATE_ENTITY, JSON.stringify(newBehavior));
                }
            };

            $scope.deleteObject = function(object){
                var index =  $scope.objects.indexOf(object);
                $scope.objects.splice(index, 1);
                if (object == $scope.currentObject){
                    $scope.currentObject = {};
                }
                sendPOSTRequest(Paths.REMOVE_ENTITY, JSON.stringify(object));
            };

            $scope.getNumber = function(num) {
                return new Array(num);
            };
            $scope.addAttribute = function(){
                $scope.attributes+=1;
            };

            $scope.showObjectDetails = function(object){
                if ($scope.showObjectDetailsFlag == 1 || object != $scope.currentObject){
                    $scope.currentObject = object;
                    $scope.showObjectDetailsFlag = 0
                } else{
                    /*$scope.currentObject = '';*/
                    $scope.showObjectDetailsFlag = 1
                }
                $scope.showBehaviorDetailsFlag = false;
                $scope.showBehaviorEditFlag = false;
                $scope.showObjectEditFlag = false;
                $scope.showObjectDetailsFlag = true;
            };

            $scope.editObjectDetails = function(object){
                    $scope.currentObject = object;
            };

            $scope.editBehaviorDetails = function(behavior){
                $scope.currentBehavior = behavior;
            };

            $scope.isValidAttribute = function(val){
                return val != '';
            };

            $scope.addFirstObject = function(){
                var empty = {name:'new object', attributes: 'attrs'};
                $scope.showObjectDetails(empty);
                $scope.showBehaviorDetailsFlag = false;
                $scope.showBehaviorEditFlag = false;
                $scope.showObjectDetailsFlag = false;
                $scope.showObjectEditFlag = true;
            };

            $scope.addFirstBehavior = function(){
                var empty = {name:'new behavior'};
                $scope.showBehaviorDetails(empty);
                $scope.showObjectDetailsFlag = false;
                $scope.showObjectEditFlag = false;
                $scope.showBehaviorDetailsFlag = false;
                $scope.showBehaviorEditFlag = true;
            };

            $scope.createApplication = function(app_name){
                var newApplication = {
                    name:  app_name
                };
                sendPOSTRequest(Paths.CREATE_APP, JSON.stringify(newApplication));
            };

            $scope.menuAddObjects = function(){
                $scope.showCreateApplication = false;
                $scope.showAddBehaviors = false;
                $scope.showObjectDetailsFlag = false;
                $scope.showBehaviorDetailsFlag = false;
                $scope.showBehaviorEditFlag = false;
                $scope.showObjectEditFlag = false;
                $scope.showAddObjects = true;
            };

            $scope.menuCreateApplication = function(){
                $scope.showAddObjects = false;
                $scope.showAddBehaviors = false;
                $scope.showObjectDetailsFlag = false;
                $scope.showBehaviorEditFlag = false;
                $scope.showObjectEditFlag = false;
                $scope.showBehaviorDetailsFlag = false;
                $scope.showCreateApplication = true;
            };

            $scope.menuAddBehaviors = function(){
                $scope.showCreateApplication = false;
                $scope.showAddObjects = false;
                $scope.showObjectDetailsFlag = false;
                $scope.showBehaviorEditFlag = false;
                $scope.showObjectEditFlag = false;
                $scope.showBehaviorDetailsFlag = false;
                $scope.showAddBehaviors = true;
            };



            $scope.deleteBehavior = function(behavior){
                var index =  $scope.behaviors.indexOf(behavior);
                $scope.behaviors.splice(index, 1);
                if (behavior == $scope.currentBehavior){
                    $scope.currentBehavior = {};
                }
                sendPOSTRequest(Paths.REMOVE_ENTITY, JSON.stringify(behavior));
            };

            $scope.showBehaviorDetails = function(behavior){
                if ($scope.showBehaviorDetailsFlag == 1 || behavior != $scope.currentBehavior){
                    $scope.currentBehavior = behavior;
                    $scope.showBehaviorDetailsFlag = 0
                } else{
                    /*$scope.currentBehavior = '';*/
                    $scope.showBehaviorDetailsFlag = 1
                }
                $scope.showObjectDetailsFlag = false;
                $scope.showObjectEditFlag = false;
                $scope.showBehaviorEditFlag = false;
                $scope.showBehaviorDetailsFlag = true;
            };
        }]);