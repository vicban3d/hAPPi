var main_module = angular.module('main', []);

main_module.controller('ctrl_main', ['$scope', '$timeout', '$sce',
    function($scope, $timeout) {

        // Variable Declaration //
        $scope.areaFlags = [];
        $scope.areaFlags["titleArea"] = true;
        $scope.areaFlags["workArea"] = true;
        $scope.areaFlags["centralArea"] = true;
        $scope.areaFlags["sideArea"] = true;
        $scope.areaFlags["messageArea"] = false;
        $scope.areaFlags["menuArea"] = true;
        $scope.areaFlags["menuButtonsArea"] = true;
        $scope.areaFlags["applicationListArea"] = false;
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
        $scope.areaFlags["designArea"] = false;
        $scope.areaFlags["releaseArea"] = false;

        $scope.menuButtons = [
            {'label': 'Apps',        'function': function(){$scope.menuHome()}},
            {'label': 'Objects',    'function': function(){$scope.menuAddObjects()}},
            {'label': 'Behavior',   'function': function(){$scope.menuAddBehaviors()}},
            {'label': 'Design',         'function': function(){$scope.menuDesign()}},
            {'label': 'Release',        'function':  function(){$scope.menuRelease()}}
        ];

        $scope.basic_types = ["Number", "Text"];
        $scope.conditions = ["Or", "And"];
        $scope.logic_types = ["Greater Than", "Less Than"];

        $scope.numOfAttributes = 0;
        $scope.numOfActions = 0;
        $scope.currentObject = '';
        $scope.all_attrs = [];
        $scope.all_acts_Object = []
        $scope.all_conditions = [];
        $scope.all_acts_Behavior = [];
        $scope.currentApplication = {id: "", name: $scope.applicationName, platforms: $scope.platforms, objects: [], behaviors: []};
        $scope.platforms = [];
        $scope.currentBehavior = '';
        $scope.currentAppURL = '';
        $scope.instances = [];
        $scope.applicationName = '';
        $scope.behaviorName = '';
        $scope.objectName = '';
        $scope.applications = {};
        $scope.operators = ['Increase By', 'Reduce By', 'Multiply By', 'Divide By', 'Change To'];
        $scope.behaviorOperators = ['Sum of All', 'Product of All', 'Maximum', 'Minimum'];
        $scope.emulatorOutput = '';

        $scope.showBehaviors = true;
        $scope.showInstance = false;

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

        $scope.menuRelease = function(){
            $scope.hideAll();
            $scope.showArea("releaseArea");
        };

        $scope.hideAll = function () {
            for (var f in $scope.areaFlags) {
                if ($scope.areaFlags.hasOwnProperty(f)) {
                    $scope.areaFlags[f] = false;
                }
            }
            $scope.showArea("messageArea");
            $scope.showArea("titleArea");
            $scope.showArea("workArea");
            $scope.showArea("centralArea");
            $scope.showArea("sideArea");
            $scope.showArea("menuArea");
            $scope.showArea("menuButtonsArea");
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

        $scope.showCurrentPlatforms = function(){
            for(var i=0; i<$scope.currentApplication.platforms.length; i++)
            {
                if($scope.currentApplication.platforms[i] == "android")
                    $scope.android = true;
                else if($scope.currentApplication.platforms[i] == "ios")
                    $scope.ios = true;
                else if($scope.currentApplication.platforms[i] == "windowsPhone")
                    $scope.windowsPhone = true;
            }
        };

        function generateUUID() {
            var d = new Date().getTime();
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
        }

        $scope.deleteApplication = function($event, application){
            $event.stopPropagation();
            delete $scope.applications[application.id];
            if (application == $scope.currentApplication){
                $scope.currentApplication = {};
                $scope.currentAppURL = '';
            }
            sendPOSTRequest(Paths.REMOVE_APP, angular.toJson(application));
        };

        $scope.addApplication = function(){
            if ($scope.applicationName == '' || $scope.applicationName =='Invalid Name!') {
                $scope.applicationName = 'Invalid Name!'
            }
            else{
                $scope.getPlatform();
                var appId = generateUUID();
                var newApplication = applicationConstructor(appId, $scope.applicationName, $scope.platforms, [],[]);
                $scope.currentApplication = newApplication;
                $scope.message = "Create new application...";
                $scope.showArea("messageArea");
                $scope.addAppToApplicationList(newApplication);
                $scope.createApplication(appId, $scope.applicationName, $scope.platforms);
                $scope.applicationName = '';
                $scope.android = false;
                $scope.ios = false;
                $scope.windowsPhone = false;
                $scope.platforms = [];
                $scope.showApplicationDetails(newApplication);
            }
        };

        $scope.editApplication = function(){
            if ($scope.applicationName == '' || $scope.applicationName =='Invalid Name!') {
                $scope.applicationName = 'Invalid Name!'
            }
            else{
                $scope.getPlatform();
                var newApplication = applicationConstructor($scope.currentApplication.id, $scope.applicationName, $scope.platforms,
                    $scope.currentApplication.actions, $scope.currentApplication.behaviors);
                $scope.message = "Updating application...";
                $scope.showArea("messageArea");
                $scope.removeApplicationFromAppList($scope.currentApplication.id);
                $scope.addAppToApplicationList(newApplication);
                $scope.updateApplication($scope.currentApplication.id, $scope.applicationName, $scope.platforms);
                $scope.applicationName = '';
                $scope.android = false;
                $scope.ios = false;
                $scope.windowsPhone = false;
                $scope.platforms = [];
                $scope.showApplicationDetails(newApplication);
                $scope.currentApplication = newApplication;
                $scope.hideArea("menuButtonsArea");
                $scope.hideArea("applicationEditArea");
                $scope.showArea("centralArea");
                $scope.showArea("applicationListArea");
            }
        };

        var applicationConstructor = function(id, name, platforms, actions, behaviors){
            return {id: id, name: name, platforms: platforms, objects: actions, behaviors: behaviors};
        }

        $scope.removeApplicationFromAppList = function(id){
            for(var i = $scope.applications.length - 1; i >= 0; i--){
                if($scope.applications[i] == id){
                    $scope.applications.splice(i,1);
                }
            }
        };

        $scope.updateApplication = function(id, name, platforms){
            var newApplication = {
                id: id,
                name: name,
                platforms: platforms
            };
            var result = sendPOSTRequest(Paths.UPDATE_APP, angular.toJson(newApplication));
            result.onreadystatechange = function(){
                if (result.readyState != 4 && result.status != 200){
                    $scope.message = "Error";
                    $scope.showArea("messageArea");
                    $timeout(function () {
                        $scope.message = '';
                    }, 5000);
                    $scope.$apply();
                }
                else if (result.readyState == 4 && result.status == 200){
                    $scope.message = result.responseText;
                    $scope.showArea("messageArea");
                    $timeout(function () {
                        $scope.message = '';
                    }, 5000);
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

        $scope.editApplicationDetails = function($event, application){
            $event.stopPropagation();
            $scope.currentApplication = application;
            $scope.showCurrentPlatforms();
            $scope.applicationName = $scope.currentApplication.name;
            $scope.showArea("applicationEditArea");

            /* $scope.hideArea("menuButtonsArea");
             $scope.showArea("centralArea");
             $scope.showArea("applicationListArea");*/
        };

        $scope.addNewApplication = function(){
            $scope.message = '';
            $scope.hideArea("applicationDetailsArea");
            $scope.showArea("applicationCreateArea")
        };


        // Object Creation //
        $scope.addObject = function() {
            if ($scope.objectName == '' || $scope.objectName == 'Invalid Name!') {
                $scope.objectName = 'Invalid Name!'
            }
            else {
                var newObject = {
                    name: $scope.objectName,
                    attributes: $scope.all_attrs.filter($scope.isValidAttribute),
                    actions: $scope.all_acts_Object.filter($scope.isValidActionObject)
                };

                $scope.addObjectToApplication(newObject);
                $scope.all_attrs = [];
                $scope.all_acts_Object = [];
                $scope.numOfAttributes = 0;
                $scope.numOfActions = 0;
                $scope.numOfConditions = 0;
                $scope.all_conditions = [];
                $scope.objectName = '';
                $scope.showObjectDetails(newObject);
                $scope.hideArea("actionsEditAreaObject");
                $scope.hideArea("actionsEditAreaBehavior");
                sendPOSTRequest(Paths.CREATE_OBJECT, angular.toJson(newObject));
            }
        };

        $scope.deleteObject = function(object){
            var index =  $scope.applications[$scope.currentApplication.id].objects.indexOf(object);
            $scope.applications[$scope.currentApplication.id].objects.splice(index, 1);
            if (object == $scope.currentObject){
                $scope.currentObject = {};
            }
            sendPOSTRequest(Paths.REMOVE_OBJECT, angular.toJson(object));
            $scope.hideArea("objectDetailsArea");
        };

        $scope.addAttribute = function(){
            $scope.numOfAttributes+=1;
        };

        $scope.addCondition = function(){
            $scope.numOfConditions+=1;
        };

        $scope.addActionObject = function(){
            $scope.showArea("actionsEditAreaObject");
        };

        $scope.addNewAction = function(){
            if ($scope.actionName == '' || $scope.actionName =='Invalid Name!'){
                $scope.actionName = 'Invalid Name!'
            }
            else {
                $scope.numOfActions += 1;
                $scope.hideArea("actionsEditAreaObject");
                $scope.hideArea("actionsEditAreaBehavior");
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
            /*$scope.objectName = $scope.currentObject.name;
             $scope.attributes = $scope.currentObject.attributes;*/
            $scope.hideArea("objectDetailsArea");
            $scope.showArea("objectEditArea");

        };

        $scope.isValidAttribute = function(val){
            return val.name != '' && val.type != '';
        };

        $scope.getAttributeName = function(val){
            return val.name;
        };

        $scope.isValidActionObject = function(val){
            return val.name != '' && val.operand1 != '' && val.operator != '' && val.operand2 != '';
        };

        $scope.isValidActionBehavior = function(val){
            return val.operandObject != '' && val.operandAttribute != '' && val.operator != '';
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
            if ($scope.behaviorName == '' || $scope.behaviorName =='Invalid Name!'){
                $scope.behaviorName = 'Invalid Name!'
            }
            else {
                var newBehavior = {
                    name: $scope.behaviorName,
                    actions: $scope.all_acts_Behavior.filter($scope.isValidActionBehavior)
                };
                $scope.addBehaviorToApplication(newBehavior);
                $scope.all_acts_Behavior = [];
                $scope.all_conditions = [];
                $scope.numOfActions = 0;
                $scope.numOfConditions = 0;
                $scope.behaviorName = '';
                $scope.showBehaviorDetails(newBehavior);
                $scope.hideArea("actionsEditAreaObject");
                $scope.hideArea("actionsEditAreaBehavior");
                sendPOSTRequest(Paths.CREATE_OBJECT, angular.toJson(newBehavior));
            }
        };

        $scope.editBehaviorDetails = function(behavior){
            $scope.currentBehavior = behavior;
        };

        function getObjectAction(actionName, operand2){
            if (actionName == "Increase By") {
                return function (operand) {
                    return operand + operand2;
                };
            }
            if (actionName == "Reduce By") {
                return function (operand) {
                    return operand -     operand2;
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
                return function (operand) {
                    return operand2;
                };
            }
        }

        function getBehaviorAction(object, actionName){
            if (actionName == "Sum of All"){
                return function (operand){
                    var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
                    var i = 0;
                    var result = 0;
                    if (index < 0){
                        var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand);
                        var actionName = object.actions[actionIndex].operator;
                        var operand1 = object.actions[actionIndex].operand1.name;
                        var operand2 = object.actions[actionIndex].operand2;
                        index = object.attributes.map(function(a) {return a.name;}).indexOf(operand1);
                        var action = getObjectAction(actionName, parseFloat(operand2));
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            result += action(parseFloat($scope.instances[object.name][i][index]));
                        }
                    } else {
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            result += parseFloat($scope.instances[object.name][i][index]);
                        }
                    }
                    return result;
                };
            } else if (actionName == "Maximum") {
                return function (operand){
                    var result = 0;
                    var i=0;
                    var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
                    if (index < 0){
                        var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand);
                        var actionName = object.actions[actionIndex].operator;
                        var operand1 = object.actions[actionIndex].operand1.name;
                        var operand2 = object.actions[actionIndex].operand2;
                        index = object.attributes.map(function(a) {return a.name;}).indexOf(operand1);
                        var action = getObjectAction(actionName, parseFloat(operand2));
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            if (result < action(parseFloat($scope.instances[object.name][i][index]))) {
                                result = action(parseFloat($scope.instances[object.name][i][index]));
                            }
                        }
                    } else {
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            if (result < parseFloat($scope.instances[object.name][i][index])) {
                                result = parseFloat($scope.instances[object.name][i][index]);
                            }
                        }
                    }
                    return result;
                };
            } else if (actionName == "Minimum"){
                return function (operand){
                    var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
                    var result = Number.MAX_VALUE;
                    var i = 0;
                    if (index < 0){
                        var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand);
                        var actionName = object.actions[actionIndex].operator;
                        var operand1 = object.actions[actionIndex].operand1.name;
                        var operand2 = object.actions[actionIndex].operand2;
                        index = object.attributes.map(function(a) {return a.name;}).indexOf(operand1);
                        var action = getObjectAction(actionName, parseFloat(operand2));
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            if (result > action(parseFloat($scope.instances[object.name][i][index]))) {
                                result = action(parseFloat($scope.instances[object.name][i][index]));
                            }
                        }
                    } else {
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            if (result > parseFloat($scope.instances[object.name][i][index])) {
                                result = parseFloat($scope.instances[object.name][i][index]);
                            }
                        }
                    }
                    return result;
                };
            } else if (actionName == "Product of All"){
                return function (operand){
                    var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
                    var result = 1;
                    var i = 0;
                    if (index < 0){
                        var actionIndex = object.actions.map(function(a) {return a.name;}).indexOf(operand);
                        var actionName = object.actions[actionIndex].operator;
                        var operand1 = object.actions[actionIndex].operand1.name;
                        var operand2 = object.actions[actionIndex].operand2;
                        index = object.attributes.map(function(a) {return a.name;}).indexOf(operand1);
                        var action = getObjectAction(actionName, parseFloat(operand2));
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            result *= action(parseFloat($scope.instances[object.name][i][index]));
                        }
                    } else {
                        for (i = 0; i < $scope.instances[object.name].length; i++) {
                            result *= parseFloat($scope.instances[object.name][i][index]);
                        }
                    }
                    return result;
                };
            } else {
                return "UNSUPPORTED";
            }
        }

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

        $scope.createApplication = function(id, name, platforms){
            var newApplication = {
                id: id,
                name: name,
                platforms: platforms,
                objects: [],
                behaviors: []
            };
            var result = sendPOSTRequest(Paths.CREATE_APP, angular.toJson(newApplication));
            result.onreadystatechange = function(){
                if (result.readyState != 4 && result.status != 200){
                    $scope.message = "Error";
                    $scope.showArea("messageArea");
                    $timeout(function () {
                        $scope.message = '';
                    }, 5000);
                    $scope.$apply();
                }
                else if (result.readyState == 4 && result.status == 200){
                    $scope.message = result.responseText;
                    $scope.showArea("messageArea");
                    $timeout(function () {
                        $scope.message = '';
                    }, 5000);
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
            var index =  $scope.applications[$scope.currentApplication.id].behaviors.indexOf(behavior);
            $scope.applications[$scope.currentApplication.id].behaviors.splice(index, 1);
            if (behavior == $scope.currentBehavior){
                $scope.currentBehavior = {};
            }
            sendPOSTRequest(Paths.REMOVE_OBJECT, angular.toJson(behavior));
            $scope.hideArea("behaviorDetailsArea");
        };

        $scope.showBehaviorDetails = function(behavior){
            if ($scope.areaFlags["behaviorDetailsArea"] == false || behavior != $scope.currentBehavior) {
                $scope.currentBehavior = behavior;
                $scope.hideArea("objectDetailsArea");
                $scope.hideArea("objectEditArea");
                $scope.hideArea("behaviorEditArea");
                $scope.showArea("behaviorDetailsArea");
            }
        };

        $scope.addActionBehavior = function(){
            $scope.showArea("actionsEditAreaBehavior");
            $scope.numOfConditions = 0;
            $scope.all_conditions = [];
        };

        $scope.currentInstance = '';
        $scope.vals = [];
        // Design //
        $scope.designDisplayObjectPage = function(object){
            $scope.currentInstance = object;
            $scope.showBehaviors = false;
            $scope.showInstance = true;
        };

        $scope.designDisplayBehaviorPage = function(){
            $scope.showBehaviors = true;
            $scope.showInstance = false;
        };

        $scope.addInstance = function(){
            if ($scope.instances[$scope.currentInstance.name] == undefined){
                $scope.instances[$scope.currentInstance.name] = [];
            }
            $scope.instances[$scope.currentInstance.name].push($scope.vals);
            $scope.vals = [];
        };

        $scope.removeInstance = function(id){
            $scope.instances[$scope.currentInstance.name].splice(parseInt(id),1);
        };

        $scope.performBehaviorAction = function(behavior){
            var object = behavior.actions[0].operandObject;
            var operand1 = behavior.actions[0].operandAttribute.name;
            var action = getBehaviorAction(object, behavior.actions[0].operator);
            $scope.emulatorOutput = action(operand1);
        };

        $scope.addObjectToApplication = function(object){
            $scope.applications[$scope.currentApplication.id].objects.push(object);
        };

        $scope.addBehaviorToApplication = function(behavior){
            $scope.applications[$scope.currentApplication.id].behaviors.push(behavior);
        };

        $scope.addAppToApplicationList = function(application){
            if ($scope.applications[application.id] == undefined){
                $scope.applications[application.id] = {id:'',name:'',platforms:[],objects:[],behaviors:[]};
            }
            $scope.applications[application.id].id = application.id;
            $scope.applications[application.id].name = application.name;
            $scope.applications[application.id].platforms.push(application.platforms);
        };


        // Release //
        $scope.releaseBuildApplication = function(application){
            sendPOSTRequest(Paths.BUILD_APP, angular.toJson(application))
        };
    }]);