angular.module('main', []).
    controller('ctrl_main', ['$scope',
        function($scope) {

            // Variable Declaration //
            $scope.areaFlags = [];
            $scope.areaFlags["titleArea"] = true;
            $scope.areaFlags["workArea"] = true;
            $scope.areaFlags["centralArea"] = true;
            $scope.areaFlags["sideArea"] = true;
            $scope.areaFlags["loadingArea"] = false;
            $scope.areaFlags["myApplicationAreaArea"] = false;
            $scope.areaFlags["menuArea"] = true;
            $scope.areaFlags["menuButtonsArea"] = false;
            $scope.areaFlags["applicationListArea"] = true;
            $scope.areaFlags["applicationDetailsArea"] = false;
            $scope.areaFlags["applicationEditArea"] = false;
            $scope.areaFlags["applicationCreateArea"] = false;
            $scope.areaFlags["objectAddArea"] = false;
            $scope.areaFlags["objectDetailsArea"] = false;
            $scope.areaFlags["objectEditArea"] = false;
            $scope.areaFlags["objectCreateArea"] = false;
            $scope.areaFlags["behaviorAddArea"] = false;
            $scope.areaFlags["behaviorDetailsArea"] = false;
            $scope.areaFlags["behaviorEditArea"] = false;
            $scope.areaFlags["actionEditArea"] = false;
            $scope.areaFlags["behaviorEditArea"] = false;

            $scope.menuButtons = [
                {'label': 'My Apps',        'function': function(){$scope.menuHome()}},
                {'label': 'Add Objects',    'function': function(){$scope.menuAddObjects()}},
                {'label': 'Add Behavior',   'function': function(){$scope.menuAddBehaviors()}},
                {'label': 'Design',         'function': function(){}},
                {'label': 'Release',        'function':  function(){}}
            ];

            $scope.numOfAttributes = 0;
            $scope.numOfActions = 0;
            $scope.currentObject = '';
            $scope.all_attrs = [];
            $scope.all_acts = [];
            $scope.objects = [];
            $scope.applications = [];
            $scope.currentApplication = {id: "", name: $scope.name, platforms: $scope.platforms};
            $scope.platforms = [];
            $scope.currentBehavior = '';
            $scope.behaviors = [];
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

            $scope.hideAll = function () {
                for (var f in $scope.areaFlags) {
                    $scope.areaFlags[f] = false;
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

            $scope.getPlatform = function(){
                $scope.platforms = [];
                if($scope.android == true)
                    $scope.platforms.push("android");
                if($scope.ios == true)
                    $scope.platforms.push("ios");
                if($scope.windowsPhone == true)
                    $scope.platforms.push("windowsPhone");
            };

            function generateUUID() {
                var d = new Date().getTime();
                var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                    var r = (d + Math.random()*16)%16 | 0;
                    d = Math.floor(d/16);
                    return (c=='x' ? r : (r&0x3|0x8)).toString(16);
                });
                return uuid;
            };

            $scope.deleteApplication = function(application){
                var index =  $scope.applications.indexOf(application);
                $scope.applications.splice(index, 1);
                if (application == $scope.currentApplication){
                    $scope.currentApplication = {};
                }
                sendPOSTRequest(Paths.REMOVE_APP, JSON.stringify(application));
            };

            $scope.addApplication = function(){
                if ($scope.name == '' || $scope.name =='Invalid Name!') {
                    $scope.name = 'Invalid Name!'
                }
                $scope.getPlatform();
                var appId = generateUUID();
                var newApplication = {id: appId, name: $scope.name, platforms: $scope.platforms};//TODO : change the name to application or object name
                $scope.currentApplication = newApplication;
                $scope.loader.loading = true;
                $scope.showArea("loadingArea");
                $scope.applications.push(newApplication);
                $scope.createApplication(appId, $scope.name, $scope.platforms);

                $scope.name = '';
                $scope.android = false;
                $scope.ios = false;
                $scope.windowsPhone = false;
                $scope.platforms = [];
                $scope.showApplicationDetails(newApplication);
            };

            $scope.editApplication = function(){
                if ($scope.name == '' || $scope.name =='Invalid Name!') {
                    $scope.name = 'Invalid Name!'
                }
                $scope.getPlatform();
                var newApplication = {id: $scope.currentApplication.id ,name: $scope.name, platforms: $scope.platforms};//TODO : change the name to application or object name
                $scope.loader.loading = true;
                $scope.showArea("loadingArea");
                $scope.removeApplication($scope.currentApplication.id);
                $scope.applications.push(newApplication);
                $scope.updateApplication($scope.currentApplication.id, $scope.name, $scope.platforms);
                $scope.currentApplication = newApplication;
                $scope.name = '';
                $scope.android = false;
                $scope.ios = false;
                $scope.windowsPhone = false;
                $scope.platforms = [];
                $scope.showApplicationDetails(newApplication);
            };

            $scope.removeApplication = function(id){
                for(var i = $scope.applications.length - 1; i >= 0; i--){
                    if($scope.applications[i].id == id){
                        $scope.applications.splice(i,1);
                    }
                }
            };

            $scope.updateApplication = function(id, name, platformsToAdd){
                var newApplication = {
                    id: id,
                    name: name,
                    platforms: platformsToAdd
                };
                var result = sendPOSTRequest(Paths.UPDATE_APP, JSON.stringify(newApplication));
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
                    }
                };
            };

            $scope.showApplicationDetails = function(application){
                if ($scope.areaFlags["applicationDetailsArea"] == false || application != $scope.currentApplication){
                    $scope.currentApplication = application;
                    $scope.hideArea("applicationCreateArea");
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
                $scope.hideArea("applicationCreateArea");
                $scope.hideArea("applicationDetailsArea");
                $scope.hideArea("applicationListArea");
                $scope.showArea("menuButtonsArea");
            };

            $scope.editApplicationDetails = function(application){
                $scope.currentApplication = application;
                //TODO
                $scope.hideArea("menuButtonsArea");
                $scope.showArea("applicationEditArea");
                $scope.showArea("centralArea");
                $scope.showArea("applicationListArea");
            };

            $scope.addNewApplication = function(){
                $scope.message = '';
                $scope.hideArea("applicationDetailsArea");
                $scope.showArea("applicationCreateArea")
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
                    sendPOSTRequest(Paths.CREATE_ENTITY, JSON.stringify(newObject));
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
                    $scope.all_acts[$scope.numOfActions] = {
                        name: $scope.actionName,
                        operand1: $scope.operand1,
                        operator: $scope.operator,
                        operand2: $scope.operand2
                    };
                    $scope.numOfActions += 1;
                    $scope.hideArea("actionsEditArea");
                }
            };

            $scope.showObjectDetails = function(object){
                if ($scope.areaFlags["objectDetailsArea"] == false || object != $scope.currentObject){
                    $scope.currentObject = object;
                    $scope.hideArea("behaviorDetailsArea");
                    $scope.hideArea("behaviorEditArea");
                    $scope.hideArea("objectCreateArea");
                    $scope.showArea("objectDetailsArea");
                }
            };

            $scope.editObjectDetails = function(object){
                $scope.currentObject = object;
                $scope.showArea("objectEditArea");
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
                $scope.hideArea("behaviorDetailsArea");
                $scope.hideArea("behaviorEditArea");
                $scope.hideArea("objectDetailsArea");
                $scope.showArea("objectCreateArea");
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
                    sendPOSTRequest(Paths.CREATE_ENTITY, JSON.stringify(newBehavior));
                }
            };

            $scope.editBehaviorDetails = function(behavior){
                $scope.currentBehavior = behavior;
            };

            $scope.addFirstBehavior = function(){
                var empty = {name:'new behavior'};
                $scope.showBehaviorDetails(empty);
                $scope.hideArea("objectDetailsArea");
                $scope.hideArea("objectCreateArea");
                $scope.hideArea("behaviorDetailsArea");
                $scope.showArea("behaviorEditArea");
            };

            $scope.createApplication = function(id, name, platformsToAdd){
                var newApplication = {
                    id: id,
                    name: name,
                    platforms: platformsToAdd
                };
                var result = sendPOSTRequest(Paths.CREATE_APP, JSON.stringify(newApplication));
                result.onreadystatechange = function(){
                    if (result.readyState != 4 && result.status != 200){
                        $scope.hideArea("loadingArea");
                        $scope.message = "Error with creating the application " + name;
                        $scope.$apply();
                    }
                    else if (result.readyState == 4 && result.status == 200){
                        $scope.message = result.responseText;
                        $scope.hideArea("loadingArea");
                        $scope.$apply();
                    }
                };

            };

            $scope.menuHome = function(){
                $scope.hideAll();
                $scope.showArea("applicationListArea");
            };

            $scope.menuAddObjects = function(){
                $scope.hideAll();
                $scope.showArea("objectsAddArea");
            };

            $scope.menuCreateApplication = function(){
                $scope.hideAll();
                $scope.showArea("applicationListArea");
            };

            $scope.menuAddBehaviors = function(){
                $scope.hideAll();
                $scope.showArea("behaviorAddArea");
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
                    $scope.showBehaviorDetailsFlag = 1
                }
                $scope.hideArea("objectDetailsArea");
                $scope.hideArea("objectCreateArea");
                $scope.hideArea("behaviorEditArea");
                $scope.showArea("behaviorDetailsArea");
            };

            // Design //
            // Release //
        }]);
