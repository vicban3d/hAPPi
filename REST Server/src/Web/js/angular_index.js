var main_module = angular.module('main', []);

main_module.controller('ctrl_main', ['$scope', '$sce',
        function($scope, $sce) {

            var PATH_APPS = "http://localhost:80/Applications/";
            var PATH_APP_INDEX = "/www/index.html";

            // Variable Declaration //
            $scope.areaFlags = [];
            $scope.areaFlags["titleArea"] = true;
            $scope.areaFlags["workArea"] = true;
            $scope.areaFlags["centralArea"] = true;
            $scope.areaFlags["sideArea"] = true;
            $scope.areaFlags["loadingArea"] = false;
            $scope.areaFlags["myApplicationAreaArea"] = false;
            $scope.areaFlags["menuArea"] = true;
            $scope.areaFlags["menuButtonsArea"] = true;
            $scope.areaFlags["applicationListArea"] = true;
            $scope.areaFlags["applicationDetailsArea"] = false;
            $scope.areaFlags["applicationEditArea"] = false;
            $scope.areaFlags["objectAddArea"] = false;
            $scope.areaFlags["objectDetailsArea"] = false;
            $scope.areaFlags["objectEditArea"] = false;
            $scope.areaFlags["behaviorAddArea"] = false;
            $scope.areaFlags["behaviorDetailsArea"] = false;
            $scope.areaFlags["behaviorEditArea"] = false;
            $scope.areaFlags["actionEditArea"] = false;
            $scope.areaFlags["behaviorEditArea"] = false;
            $scope.areaFlags["designArea"] = false;

            $scope.menuButtons = [
                {'label': 'Apps',        'function': function(){$scope.menuHome()}},
                {'label': 'Objects',    'function': function(){$scope.menuAddObjects()}},
                {'label': 'Behavior',   'function': function(){$scope.menuAddBehaviors()}},
                {'label': 'Design',         'function': function(){$scope.menuDesign()}},
                {'label': 'Release',        'function':  function(){}}
            ];

            $scope.basic_types = ["Number", "Text"];

            $scope.numOfAttributes = 0;
            $scope.numOfActions = 0;
            $scope.currentObject = '';
            $scope.all_attrs = [];
            $scope.all_acts = [];
            $scope.objects = [];
            $scope.applications = [];
            $scope.currentApplication = '';
            $scope.platforms = [];
            $scope.currentBehavior = '';
            $scope.behaviors = [];
            $scope.currentAppURL = '';
            $scope.operators = ['Increase By', 'Reduce By', 'Multiply By', 'Divide By', 'Change To'];

            $scope.loader = {
                loading : false
            };

            // General Functions //
            $scope.menuHome = function(){
                $scope.hideAll();
                $scope.hideArea("menuButtonsArea");
                $scope.showArea("applicationListArea");
            };

            $scope.menuAddObjects = function(){
                $scope.hideAll();
                $scope.showArea("objectsAddArea");
            };

            $scope.menuAddBehaviors = function(){
                $scope.hideAll();
                $scope.showArea("behaviorAddArea");
            };

            $scope.menuDesign = function(){
                $scope.hideAll();
                $scope.showArea("designArea");
            };

            $scope.hideAll = function () {
                for (var f in $scope.areaFlags) {
                    if ($scope.areaFlags.hasOwnProperty(f)) {
                        $scope.areaFlags[f] = false;
                    }
                }
                $scope.areaFlags["titleArea"] = true;
                $scope.areaFlags["workArea"] = true;
                $scope.areaFlags["centralArea"] = true;
                $scope.areaFlags["sideArea"] = true;
                $scope.areaFlags["menuArea"] = true;
                $scope.areaFlags["menuButtonsArea"] = true;
            };

            $scope.showArea = function (area) {
                $scope.areaFlags[area] = true;
            };

            $scope.hideArea = function (area) {
                $scope.areaFlags[area] = false;
            };

            // Application Creation //
            $scope.addPlatforms = function (platforms) {
                for(var i=0; i<platforms.length; i++) {
                    var result;
                    var platform;
                    if (platforms[i] == 'android'){
                        platform = "android";
                        result = sendPOSTRequest(Paths.ADD_PLATFORM_ANDROID, angular.toJson(platforms[i]));
                    }
                    else if(platforms[i] == 'ios'){
                        platform = "ios";
                        result = sendPOSTRequest(Paths.ADD_PLATFORM_IOS, angular.toJson(platforms[i]));
                    }
                    else if(platforms[i] == 'windowsPhone'){
                        platform = "windowsPhone";
                        result = sendPOSTRequest(Paths.ADD_PLATFORM_WINDOWS_PHONE, angular.toJson(platforms[i]));
                    }

                    result.onreadystatechange = function(){
                        if (result.readyState != 4 && result.status != 200){
                            $scope.message = "Error adding " + platform + " platform";
                            $scope.$apply();
                        }
                        else if (result.readyState == 4 && result.status == 200){
                            $scope.message = result.responseText;
                            $scope.$apply();
                        }
                    };
                }
            };

            $scope.getPlatform = function(){
                if($scope.android == true)
                    $scope.platforms.push('android');
                if($scope.ios == true)
                    $scope.platforms.push('ios');
                if($scope.windowsPhone == true)
                    $scope.platforms.push('windowsPhone');
            };

            $scope.deleteApplication = function(application){
                var index =  $scope.applications.indexOf(application);
                $scope.applications.splice(index, 1);
                if (application == $scope.currentApplication){
                    $scope.currentApplication = {};
                    $scope.currentAppURL = '';
                }
                sendPOSTRequest(Paths.REMOVE_APP, angular.toJson(application));
            };

            $scope.addApplication = function(){
                if ($scope.name == '' || $scope.name =='Invalid Name!') {
                    $scope.name = 'Invalid Name!'
                }
                $scope.getPlatform();
                var newApplication = {name: $scope.name, platforms: $scope.platforms};//TODO : change the name to application or object name
                $scope.loader.loading = true;
                $scope.showArea("loadingArea");
                $scope.applications.push(newApplication);
                $scope.createApplication($scope.name, $scope.platforms);
                $scope.name = '';
                $scope.android = false;
                $scope.ios = false;
                $scope.windowsPhone = false;
                $scope.platforms = [];
                $scope.showApplicationDetails(newApplication);
            };

            $scope.showApplicationDetails = function(application){
                if ($scope.areaFlags["applicationDetailsArea"] == false || application != $scope.currentApplication){
                    $scope.currentApplication = application;
                    $scope.hideArea("applicationEditArea");
                    $scope.showArea("applicationDetailsArea");
                }
            };

            $scope.getApplication = function(application){
                if ($scope.showApplicationDetailsFlag == 1 || application != $scope.currentApplication){
                    $scope.currentApplication = application;
                    $scope.showApplicationDetailsFlag = 0
                } else{
                    $scope.showApplicationDetailsFlag = 1
                }
                $scope.currentApplication = application;
                $scope.currentAppURL = $sce.trustAsResourceUrl($scope.currentAppURL = PATH_APPS + application.name + PATH_APP_INDEX);
                $scope.hideArea("applicationEditArea");
                $scope.hideArea("applicationDetailsArea");
                $scope.hideArea("applicationListArea");
                $scope.showArea("menuButtonsArea");
            };

            $scope.editApplicationDetails = function(application){
                $scope.currentApplication = application;
            };

            $scope.addNewApplication = function(){
                $scope.message = '';
                $scope.hideArea("applicationDetailsArea");
                $scope.showArea("applicationEditArea")
            };

            $scope.createApplication = function(name, platformsToAdd){
                var newApplication = {
                    name: name
                };
                var result = sendPOSTRequest(Paths.CREATE_APP, angular.toJson(newApplication));
                result.onreadystatechange = function(){
                    if (result.readyState != 4 && result.status != 200){
                        $scope.loader.loading = false;
                        $scope.showMessage = false;
                        $scope.message = "Error";
                        $scope.$apply();
                    }
                    else if (result.readyState == 4 && result.status == 200){
                        $scope.message = result.responseText;
                        $scope.loader.loading = false;
                        $scope.hideArea("loadingArea");
                        $scope.$apply();
                        $scope.addPlatforms(platformsToAdd);
                    }
                };
            };

            // Object Creation //
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
                    $scope.hideArea("actionsEditArea");
                    sendPOSTRequest(Paths.CREATE_ENTITY, angular.toJson(newObject));
                }
            };

            $scope.deleteObject = function(object){
                var index =  $scope.objects.indexOf(object);
                $scope.objects.splice(index, 1);
                if (object == $scope.currentObject){
                    $scope.currentObject = {};
                }
                sendPOSTRequest(Paths.REMOVE_ENTITY, angular.toJson(object));
                $scope.hideArea("objectDetailsArea");
            };

            $scope.addAttribute = function(){
                $scope.numOfAttributes+=1;
            };

            $scope.addAction = function(){
                $scope.showArea("actionsEditArea");
            };

            $scope.addNewAction = function(){
                if ($scope.actionName == '' || $scope.actionName =='Invalid Name!'){
                    $scope.actionName = 'Invalid Name!'
                }
                else {
                    $scope.numOfActions += 1;
                    $scope.hideArea("actionsEditArea");
                }
            };

            $scope.showObjectDetails = function(object){
                if ($scope.areaFlags["objectDetailsArea"] == false || object != $scope.currentObject){
                    $scope.currentObject = object;
                    $scope.hideArea("behaviorDetailsArea");
                    $scope.hideArea("behaviorEditArea");
                    $scope.hideArea("objectEditArea");
                    $scope.showArea("objectDetailsArea");
                }
            };

            $scope.editObjectDetails = function(object){
                $scope.currentObject = object;
            };

            $scope.isValidAttribute = function(val){
                return val.name != '' && val.type != '';

            };

            $scope.getAttributeName = function(val){
                return val.name;

            };

            $scope.isValidAction = function(val){
                return val.name != '' && val.operand1 != '' && val.operator != '' && val.operand2 != '';
            };

            $scope.addNewObject = function(){
                var empty = {name:'new object', attributes: [{name: 'attr', type: 'number'}]};
                $scope.showObjectDetails(empty);
                $scope.hideArea("behaviorDetailsArea");
                $scope.hideArea("behaviorEditArea");
                $scope.hideArea("objectDetailsArea");
                $scope.showArea("objectEditArea");
            };

            // Behavior Creation //
            $scope.addBehavior = function(){
                if ($scope.name == '' || $scope.name =='Invalid Name!'){
                    $scope.name = 'Invalid Name!'
                }
                else {
                    var newBehavior = {name: $scope.name};
                    $scope.behaviors.push(newBehavior);
                    $scope.name = '';
                    $scope.showBehaviorDetails(newBehavior);
                    sendPOSTRequest(Paths.CREATE_ENTITY, angular.toJson(newBehavior));
                }
            };

            $scope.editBehaviorDetails = function(behavior){
                $scope.currentBehavior = behavior;
            };

            $scope.addNewBehavior = function(){
                var empty = {name:'new behavior'};
                $scope.showBehaviorDetails(empty);
                $scope.hideArea("objectDetailsArea");
                $scope.hideArea("objectEditArea");
                $scope.hideArea("behaviorDetailsArea");
                $scope.showArea("behaviorEditArea");
            };

            $scope.deleteBehavior = function(behavior){
                var index =  $scope.behaviors.indexOf(behavior);
                $scope.behaviors.splice(index, 1);
                if (behavior == $scope.currentBehavior){
                    $scope.currentBehavior = {};
                }
                sendPOSTRequest(Paths.REMOVE_ENTITY, angular.toJson(behavior));
            };

            $scope.showBehaviorDetails = function(behavior){
                if ($scope.showBehaviorDetailsFlag == 1 || behavior != $scope.currentBehavior){
                    $scope.currentBehavior = behavior;
                    $scope.showBehaviorDetailsFlag = 0
                } else{
                    $scope.showBehaviorDetailsFlag = 1
                }
                $scope.hideArea("objectDetailsArea");
                $scope.hideArea("objectEditArea");
                $scope.hideArea("behaviorEditArea");
                $scope.showArea("behaviorDetailsArea");
            };

            // Design //

            // Release //

        }]);



