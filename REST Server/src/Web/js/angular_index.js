angular.module('main', [])
    .controller('ctrl_main', [
        '$scope',
        function($scope){
            $scope.numOfAttributes = 0;
            $scope.numOfActions = 0;
            $scope.currentObject = '';
            $scope.all_attrs = [];
            $scope.all_acts = [];
            $scope.showObjectDetailsFlag = false;
            $scope.showObjectEditFlag = false;
            $scope.showActionsEditFlag = false;
            $scope.objects = [];

            $scope.operators = ['Increase By', 'Reduce By', 'Multiply By', 'Divide By', 'Change To'];

            $scope.showAddObjects = false;
            $scope.showMenuArea = true;
            $scope.showCreateApplication = false;
            $scope.showApplicationList = false;
            $scope.showApplictionDetailsFlag = false;
            $scope.showApplicationEditFlag = false;
            $scope.applications = [];
            $scope.currentApplication = '';
            $scope.platform = '';
            $scope.currentBehavior = '';
            $scope.showBehaviorDetailsFlag = false;
            $scope.showBehaviorEditFlag = false;
            $scope.behaviors = [];
            $scope.showAddBehaviors = false;

            $scope.getPlatform = function(){
                if($scope.android = true)
                    $scope.platform = 'android';
                else if($scope.ios = true)
                    $scope.platform = 'ios';
                else if($scope.windowsPhone = true)
                    $scope.platform = 'windowsPhone';
            };

            $scope.addApplication = function(){
                if ($scope.name == '' || $scope.name =='Invalid Name!'){
                    $scope.name = 'Invalid Name!'
                }
                getPlatform();
                var newApplication = {name: $scope.name, platform: $scope.platform};//TODO : change the name to application or object name
                $scope.applications.push(newApplication);
                $scope.name = '';
                $scope.android = false;
                $scope.ios = false;
                $scope.windowsPhone = false;
                $scope.showObjectDetails(newApplication);
                sendPOSTRequest(Paths.CREATE_APP, JSON.stringify(name));
                //TODO: send request to add platform
            };

            $scope.addObject = function() {
                if ($scope.name == '' || $scope.name == 'Invalid Name!') {
                    $scope.name = 'Invalid Name!'
                }
                else {
                    var newObject = {
                        name: $scope.name,
                        attributes: $scope.all_attrs.filter($scope.isValidAttribute),
                        actions: $scope.all_acts.filter($scope.isValidAction)
                    };

                    $scope.objects.push(newObject);
                    $scope.all_attrs = [];
                    $scope.all_acts = [];
                    $scope.numOfAttributes = 0;
                    $scope.numOfActions = 0;
                    $scope.name = '';
                    $scope.showObjectDetails(newObject);
                    $scope.showActionsEditFlag = false;
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
                $scope.numOfAttributes+=1;
            };

            $scope.addAction = function(){
                $scope.showActionsEditFlag = false;
                $scope.numOfActions+=1;
            };

            $scope.addNewAction = function(){
                $scope.showActionsEditFlag = true;
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

            $scope.showApplicationDetails = function(application){
                if ($scope.showApplicationDetailsFlag == 1 || application != $scope.currentApplication){
                    $scope.currentApplication = application;
                    $scope.showApplicationDetailsFlag = 0
                } else{
                    /*$scope.currentApplication = '';*/
                    $scope.showApplicationDetailsFlag = 1
                }
                $scope.showApplicationEditFlag = false;
                $scope.showApplicationDetailsFlag = true;
            };

            $scope.editObjectDetails = function(object){
                    $scope.currentObject = object;
            };

            $scope.editBehaviorDetails = function(behavior){
                $scope.currentBehavior = behavior;
            };

            $scope.editApplicationDetails = function(application){
                $scope.currentApplication = application;
            };
            $scope.isValidAttribute = function(val){
                return val != '';
            };
            $scope.isValidAction = function(val){
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

            $scope.addNewApplication = function(){
                var empty = {name:'new object'};
                $scope.showApplicationDetails(empty);
                $scope.showApplicationDetailsFlag = false;
                $scope.showApplicationEditFlag = true;
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
                $scope.showApplicationList = false;
                $scope.showActionsEditFlag = false;
                $scope.showAddObjects = true;
            };

            $scope.menuCreateApplication = function(){
                $scope.showAddObjects = false;
                $scope.showApplicationList = false;
                $scope.showAddBehaviors = false;
                $scope.showObjectDetailsFlag = false;
                $scope.showBehaviorEditFlag = false;
                $scope.showObjectEditFlag = false;
                $scope.showBehaviorDetailsFlag = false;
                $scope.showActionsEditFlag = false;
                $scope.showCreateApplication = true;
            };

            $scope.menuAddBehaviors = function(){
                $scope.showCreateApplication = false;
                $scope.showAddObjects = false;
                $scope.showObjectDetailsFlag = false;
                $scope.showBehaviorEditFlag = false;
                $scope.showObjectEditFlag = false;
                $scope.showBehaviorDetailsFlag = false;
                $scope.showActionsEditFlag = false;
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