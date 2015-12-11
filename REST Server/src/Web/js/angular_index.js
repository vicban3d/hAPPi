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
            $scope.deleteObject = function(object){
                var index =  $scope.objects.indexOf(object);
                $scope.objects.splice(index, 1);
                if (object == $scope.currentObject){
                    $scope.currentObject = {};
                }
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
                $scope.showObjectEditFlag = false;
                $scope.showObjectDetailsFlag = true;
            };

            $scope.editObjectDetails = function(object){
                    $scope.currentObject = object;
            };


            $scope.isValidAttribute = function(val){
                return val != '';
            };

            $scope.addFirstObject = function(){
                var empty = {name:'new object', attributes: 'attrs'};
                $scope.showObjectDetails(empty);
                $scope.showObjectDetailsFlag = false;
                $scope.showObjectEditFlag = true;
            };

            $scope.createApplication = function(app_name){
                var newApplication = {
                    name:  app_name
                };
                sendPOSTRequest(Paths.CREATE_APP, JSON.stringify(newApplication));
            };

            $scope.menuAddObjects = function(){
                $scope.showCreateApplication = false;
                $scope.showAddObjects = true;
            };

            $scope.menuCreateApplication = function(){
                $scope.showAddObjects = false;
                $scope.showCreateApplication = true;
            };
        }]);