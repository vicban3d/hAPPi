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
            $scope.showMyApps = false;
            $scope.showAddObjects = false;
            $scope.showMenuArea = false;
            $scope.showCreateApplication = false;
            $scope.showApplicationList = true;
            $scope.showApplictionDetailsFlag = false;
            $scope.showApplicationEditFlag = false;
            $scope.applications = [];
            $scope.currentApplication = '';
            $scope.platforms = [];
            $scope.currentBehavior = '';
            $scope.showBehaviorDetailsFlag = false;
            $scope.showBehaviorEditFlag = false;
            $scope.behaviors = [];
            $scope.showAddBehaviors = false;
            $scope.operators = ['Increase By', 'Reduce By', 'Multiply By', 'Divide By', 'Change To'];

            $scope.loader = {
                loading : false
            };
            $scope.showMessage = false;

            $scope.getPlatform = function(){
                if($scope.android == true)
                    $scope.platforms.push('android');
                if($scope.ios == true)
                    $scope.platforms.push('ios');
                if($scope.windowsPhone == true)
                    $scope.platforms.push('windowsPhone');
            };

            $scope.addPlatforms = function (platforms) {
                for(i=0; i<platforms.length; i++) {
                    var result;
                    if (platforms[i] == 'android')
                        result = sendPOSTRequest(Paths.ADD_PLATFORM_ANDROID, JSON.stringify(platforms[i]));
                    else if(platforms[i] == 'ios')
                        result = sendPOSTRequest(Paths.ADD_PLATFORM_IOS, JSON.stringify(platforms[i]));
                    else if(platforms[i] == 'windowsPhone')
                        result = sendPOSTRequest(Paths.ADD_PLATFORM_WINDOWS_PHONE, JSON.stringify(platforms[i]));

                    result.onreadystatechange = function(){
                        if (result.readyState != 4 && result.status != 200){
                            $scope.message = "Error";
                            $scope.$apply();
                        }
                        else if (result.readyState == 4 && result.status == 200){
                            $scope.message = result.responseText;
                            $scope.$apply();
                        }
                    };
                }
            };

            $scope.addApplication = function(){
                if ($scope.name == '' || $scope.name =='Invalid Name!') {
                    $scope.name = 'Invalid Name!'
                }
                $scope.getPlatform();
                var newApplication = {name: $scope.name, platforms: $scope.platforms};//TODO : change the name to application or object name
                $scope.loader.loading = true;
                $scope.showMessage = true;
                $scope.applications.push(newApplication);
                $scope.createApplication($scope.name, $scope.platforms);
                $scope.name = '';
                $scope.android = false;
                $scope.ios = false;
                $scope.windowsPhone = false;
                $scope.platforms = [];
                $scope.showApplicationDetails(newApplication);
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

            $scope.deleteApplication = function(application){
                var index =  $scope.applications.indexOf(application);
                $scope.applications.splice(index, 1);
                if (application == $scope.currentApplication){
                    $scope.currentApplication = {};
                }
                sendPOSTRequest(Paths.REMOVE_APP, JSON.stringify(application));
            };

            $scope.getNumber = function(num) {
                return new Array(num);
            };
            $scope.addAttribute = function(){
                $scope.numOfAttributes+=1;
            };

            $scope.addAction = function(){
                $scope.showActionsEditFlag = true;
            };

            $scope.addNewAction = function(){
                if ($scope.actionName == '' || $scope.actionName =='Invalid Name!'){
                    $scope.actionName = 'Invalid Name!'
                }
                else {
                    $scope.all_acts[$scope.numOfActions] = {
                        name: $scope.actionName,
                        operand1: $scope.operand1,
                        operator: $scope.operator,
                        operand2: $scope.operand2
                    };
                    $scope.numOfActions += 1;
                    $scope.showActionsEditFlag = false;
                }
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
                    $scope.showApplicationDetailsFlag = 1
                }
                $scope.showApplicationEditFlag = false;
                $scope.showApplicationDetailsFlag = true;
            };

            $scope.getApplication = function(application){
                if ($scope.showApplicationDetailsFlag == 1 || application != $scope.currentApplication){
                    $scope.currentApplication = application;
                    $scope.showApplicationDetailsFlag = 0
                } else{
                    $scope.showApplicationDetailsFlag = 1
                }
                $scope.showApplicationEditFlag = false;
                $scope.showApplicationDetailsFlag = false;
                $scope.showMenuArea = true;
                $scope.showCreateApplication = true;
                $scope.showApplicationList = false;
                $scope.showMyApps = true;
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
                $scope.message = '';
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

            $scope.createApplication = function(name, platformsToAdd){
                var newApplication = {
                    name: name
                };
                var result = sendPOSTRequest(Paths.CREATE_APP, JSON.stringify(newApplication));
                result.onreadystatechange = function(){
                    if (result.readyState != 4 && result.status != 200){
                        $scope.message = "Error";
                        $scope.$apply();
                    }
                    else if (result.readyState == 4 && result.status == 200){
                        $scope.message = result.responseText;
                        $scope.loader.loading = false;
                        $scope.showMessage = false;
                        $scope.$apply();
                        $scope.addPlatforms(platformsToAdd);
                    }
                };

            };

            $scope.menuHome = function(){
                $scope.showObjectDetailsFlag = false;
                $scope.showObjectEditFlag = false;
                $scope.showActionsEditFlag = false;
                $scope.showAddObjects = false;
                $scope.showMenuArea = false;
                $scope.showCreateApplication = false;
                $scope.showApplicationList = true;
                $scope.showApplictionDetailsFlag = false;
                $scope.showApplicationEditFlag = false;
                $scope.showBehaviorDetailsFlag = false;
                $scope.showBehaviorEditFlag = false;
                $scope.showAddBehaviors = false;
                $scope.message = "";
                $scope.showMyApps = true;
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
                $scope.showMyApps = false;
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
                $scope.showMyApps = false;
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