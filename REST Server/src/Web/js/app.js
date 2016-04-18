var main_module = angular.module('main', []);

main_module.controller('ctrl_main', ['appService', 'objectService', 'behaviorService', 'designService', 'releaseService', '$scope', '$timeout',
    function(appService, objectService, behaviorService, designService, releaseService, $scope, $timeout) {

        // ----------------------------------------------------------------------Variable Declaration-------------------

        // Shows debug messages on the web page.
        $scope.DEBUG = true;
        $scope.message = "";

        $scope.applications = [];
        $scope.completeApplications = {};

        $scope.areaFlags = [];
        $scope.areaFlags["mainPage"] = true;
        $scope.areaFlags["workArea"] = false;
        $scope.areaFlags["centralArea"] = false;
        $scope.areaFlags["sideArea"] = false;
        $scope.areaFlags["menuArea"] = false;
        $scope.areaFlags["objectCreateArea"] = false;
        $scope.areaFlags["behaviorCreateArea"] = false;
        $scope.areaFlags["designArea"] = false;
        $scope.areaFlags["releaseArea"] = false;
        $scope.areaFlags["frontPage"] = false;
        $scope.areaFlags["myApps"] = false;

        $scope.applicationBuilt = false;

        $scope.basic_types = ["Number", "Text"];
        $scope.andOrOperator = ["Or", "And"];
        $scope.logicOperations = ["Greater Than", "Less Than", "Equal", "Not Equal"];
        $scope.operators = ['Increase By', 'Reduce By', 'Multiply By', 'Divide By', 'Change To'];
        $scope.behaviorOperators = ['Sum of All', 'Product of All', 'Maximum', 'Minimum', 'Average', 'Display'];

        $scope.instances = [];
        $scope.emulatorOutput = '';
        $scope.attribute_values = [];
        $scope.showNoObjectMembersImage = true;
        $scope.showNoBehaviorMembersImage = true;

        // Preload phone images
        var images = [];

        function preload() {
            for (var i = 0; i < preload.arguments.length; i++) {
                images[i] = new Image();
                images[i].src = preload.arguments[i];
            }
        }

        preload(
            "http://localhost/img/ios_disabled.png",
            "http://localhost/img/ios_enabled.png",
            "http://localhost/img/android_disabled.png",
            "http://localhost/img/android_enabled.png",
            "http://localhost/img/wp_disabled.png",
            "http://localhost/img/wp_enabled.png",
            "http://localhost/img/ios_checkbox.png",
            "http://localhost/img/ios_checkbox_checked.png",
            "http://localhost/img/android_checkbox.png",
            "http://localhost/img/android_checkbox_checked.png",
            "http://localhost/img/wp_checkbox.png",
            "http://localhost/img/wp_checkbox_checked.png"
        );

        // ios images
        $scope.iosImg = "ios_disabled.png";
        $scope.iosImgEnable = "ios_enabled.png";
        $scope.iosCheckboxImg = "ios_checkbox.png";
        $scope.iosCheckboxImgEnable = "ios_checkbox_checked.png";
        $scope.switchIosImg = 1;
        $scope.toggleIos = function(){
            $scope.ios = !$scope.ios;
            $scope.switchIosImg = 3- $scope.switchIosImg;
        };
        // android images
        $scope.androidImg = "android_disabled.png";
        $scope.androidImgEnable = "android_enabled.png";
        $scope.androidCheckboxImg = "android_checkbox.png";
        $scope.androidCheckboxImgEnable = "android_checkbox_checked.png";
        $scope.switchAndroidImg = 1;
        $scope.toggleAndroid = function(){
            $scope.android = !$scope.android;
            $scope.switchAndroidImg = 3- $scope.switchAndroidImg;
        };
        // wp images
        $scope.windowsImg = "wp_disabled.png";
        $scope.windowsImgEnable = "wp_enabled.png";
        $scope.windowsCheckboxImg = "wp_checkbox.png";
        $scope.windowsCheckboxImgEnable = "wp_checkbox_checked.png";
        $scope.switchWindowsImg = 1;
        $scope.toggleWindows = function(){
            $scope.windowsPhone = !$scope.windowsPhone;
            $scope.switchWindowsImg = 3- $scope.switchWindowsImg;
        };

        // ----------------------------------------------------------------------General Functions----------------------

        $scope.showWorkAreas = function(){
            $scope.showArea('workArea');
            $scope.showArea('centralArea');
            $scope.showArea('menuArea');
            $scope.showArea('sideArea');
            $scope.showArea("designArea");
        };

        $scope.menuAddObjects = function(){
            $scope.hideAll();
            $scope.showWorkAreas();
            $scope.showArea("objectsAddArea");
        };

        $scope.menuAddBehaviors = function(){
            $scope.hideAll();
            $scope.showWorkAreas();
            $scope.showArea("behaviorCreateArea");

        };

        $scope.menuRelease = function(){
            $scope.hideAll();
            $scope.showWorkAreas();
            $scope.showArea("releaseArea");
        };

        $scope.gotoApp = function(){
            $scope.hideArea("frontPage");
        };

        $scope.gotoMainPage = function(){
            $scope.hideAll();
            $scope.showArea('mainPage');
        };

        $scope.backToMain = function(){
            $scope.hideArea('myApps');
            $scope.hideArea('mainPage');
            $scope.switchAndroidImg = 1;
            $scope.switchIosImg = 1;
            $scope.switchWindowsImg = 1;
            $scope.android = false;
            $scope.ios = false;
            $scope.windowsPhone = false;
            $scope.applicationName = '';
            $scope.showArea("frontPage");
        };

        $scope.showAppsList = function(){
            $scope.hideAll();
            $scope.showArea("myApps");
        };

        $scope.hideAll = function () {
            for (var f in $scope.areaFlags) {
                if ($scope.areaFlags.hasOwnProperty(f)) {
                    $scope.areaFlags[f] = false;
                }
            }
        };

        $scope.showArea = function (area) {
            $scope.areaFlags[area] = true;
        };

        $scope.hideArea = function (area) {
            $scope.areaFlags[area] = false;
        };

        $scope.toggleArea = function (area) {
            $scope.areaFlags[area] = !$scope.areaFlags[area];
        };

        // ----------------------------------------------------------------------Applications Service Methods-----------

        $scope.getPlatformsArray = function(){ appService.getPlatformsArray(); };

        $scope.getCurrentApplication = function() { return appService.getCurrentApplication() };

        $scope.showCurrentPlatforms = function(){ appService.showCurrentPlatforms(); };

        $scope.deleteApplication = function(application){
            appService.deleteApplication($scope.applications, application);
            $scope.acceptMessageResult(sendPOSTRequest(Paths.REMOVE_APP, angular.toJson(application)));
        };

        $scope.addApplication = function(){
            $scope.updateStatus("Creating " + $scope.applicationName + "...");
            appService.currentApplication =
            {
                id: '',
                name: $scope.applicationName,
                platforms: appService.getPlatformsArray([$scope.android, $scope.ios, $scope.windowsPhone]),
                objects: [],
                behaviors: []
            };
            

            appService.addApplication($scope.applications);
            $scope.completeApplications[appService.currentApplication.id] = false;
            var id = appService.currentApplication.id;

            $scope.acceptMessageResult(
                sendPOSTRequest(Paths.CREATE_APP, angular.toJson(appService.currentApplication)),
                function(){
                    $scope.completeApplications[id] = true;
                });
            $scope.getApplication(appService.currentApplication);
            $scope.menuAddObjects();
        };

        $scope.editApplication = function(application){
            $scope.message = "Updating application...";
            $scope.completeApplications[appService.currentApplication.id] = false;
            var id = appService.currentApplication.id;

            appService.currentApplication.platforms = appService.getPlatformsArray([$scope.android, $scope.ios, $scope.windowsPhone]);

            appService.editApplication($scope.applications, application);
            $scope.indexToShow = -1;
            $scope.acceptMessageResult(
                sendPOSTRequest(Paths.UPDATE_APP, angular.toJson(appService.currentApplication)),
                function(){
                    $scope.completeApplications[id] = true;
                });
        };

        $scope.removeApplicationFromApplicationList = function(id){appService.removeApplicationFromApplicationList($scope, id); };

        $scope.updateApplication = function(id, name, platforms){appService.updateApplication($scope, id,name,platforms)};

        $scope.getApplication = function(application){ appService.getApplication($scope, application); };

        $scope.hideApplicationEditArea = function(){
            $scope.indexToShow = -1;
            $scope.applicationName = "";
            $scope.android = false;
            $scope.ios = false;
            $scope.windowsPhone = false;
        };

        $scope.showApplicationEditArea = function($event, application){
            $event.stopPropagation();
            if ($scope.indexToShow != application.id) {
                $scope.indexToShow = application.id;
            } else {
                $scope.indexToShow = -1
            }
            appService.currentApplication = {
                id: application.id,
                name: application.name,
                platforms: application.platforms,
                objects: application.objects,
                behaviors: application.behaviors
            };

            $scope.android = false;
            $scope.ios = false;
            $scope.windowsPhone = false;

            for(var i=0; i<appService.currentApplication.platforms.length; i++)
            {
                if(appService.currentApplication.platforms[i] == "android") {
                    $scope.switchAndroidImg = 2;
                    $scope.switchIosImg = 1;
                    $scope.switchWindowsImg = 1;
                    $scope.android = true;
                }
                else if(appService.currentApplication.platforms[i] == "ios") {
                    $scope.switchAndroidImg = 1;
                    $scope.switchIosImg = 2;
                    $scope.switchWindowsImg = 1;
                    $scope.ios = true;
                }
                else if(appService.currentApplication.platforms[i] == "wp8") {
                    $scope.switchAndroidImg = 1;
                    $scope.switchIosImg = 1;
                    $scope.switchWindowsImg = 2;
                    $scope.windowsPhone = true;
                }
            }

        };

        $scope.createApplication = function(id, name, platforms){ appService.createApplication($scope, id, name, platforms); };

        $scope.addObjectToApplication = function(object){appService.addObjectToApplication($scope, object);};

        $scope.addBehaviorToApplication = function(behavior){appService.addBehaviorToApplication($scope, behavior);};

        // ----------------------------------------------------------------------Object Service methods-----------------

        $scope.addObject = function() {
            objectService.addObject(appService.currentApplication);
            $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_OBJECT, angular.toJson(objectService.currentObject)));
            $scope.hideArea("objectCreateArea");
        };

        $scope.deleteObject = function(object){
            objectService.deleteObject(appService.currentApplication, object);
            if (!appService.currentApplication.objects.length){
                $scope.showNoObjectMembersImage = true;
            }
            $scope.indexToShow = -1;
            $scope.acceptMessageResult(sendPOSTRequest(Paths.REMOVE_OBJECT, angular.toJson(object)));
        };

        $scope.addAttribute = function(){ objectService.addAttribute(); };

        $scope.removeAttribute = function($index){
            objectService.removeAttribute($index);
        };
        
        $scope.addAction = function(){ objectService.addAction(); };
        
        $scope.removeAction = function($index){
            objectService.removeAction($index);
        };

        $scope.getAttributeName = function(val){ objectService.getAttributeName(val); };

        $scope.addNewObject = function(){
            objectService.addNewObject();
            $scope.showNoObjectMembersImage = !$scope.showNoObjectMembersImage;
            $scope.toggleArea("objectCreateArea");
        };

        $scope.getObjectAction = function(actionName, operand2){ return objectService.getObjectAction(actionName, operand2); };

        $scope.editObject = function(object){
            var all_behaviors = appService.currentApplication.behaviors;
            for (var i=0; i< all_behaviors.length; i++){
                if (all_behaviors[i].operandObject == object){
                    all_behaviors[i].operandObject = objectService.currentObject;
                    if (objectService.currentObject.attributes.indexOf(objectService.currentObject.operandAttribute) >= 0){
                        all_behaviors[i].operandAttribute = objectService.currentObject.operandAttribute;
                    } else {
                        alert("Warning: Operand in behavior " + all_behaviors[i].name + " was deleted or replaced!");
                    }
                }
            }
            objectService.editObject(appService.currentApplication, object);
            $scope.acceptMessageResult(sendPOSTRequest(Paths.UPDATE_OBJECT, angular.toJson(objectService.currentObject)));
            $scope.indexToShow = -1;
        };

        $scope.showObjectEditArea = function($event, object){
            $event.stopPropagation();
            if ($scope.indexToShow != object.id) {
                $scope.indexToShow = object.id;
                $scope.message = object.attributes;
                objectService.currentObject = {
                    id: '',
                    name: object.name,
                    attributes: copyArray(object.attributes),
                    actions: copyArray(object.actions)
                };
            } else {
                $scope.indexToShow = -1
            }
        };

        $scope.hideObjectEditArea = function(){
            $scope.indexToShow = -1;
            objectService.currentObject = {
                id: '',
                name: '',
                attributes: [],
                actions: []
            };
        };

        $scope.getCurrentObject = function(){
            return objectService.currentObject;
        };

        // ----------------------------------------------------------------------Behavior Service Methods---------------

        $scope.addBehavior = function(){
            behaviorService.addBehavior(appService.getCurrentApplication());
            $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_BEHAVIOR, angular.toJson(behaviorService.currentBehavior)));
            $scope.hideArea("behaviorCreateArea");
        };

        $scope.getCurrentBehavior = function(){
            return behaviorService.currentBehavior;
        };

        $scope.editBehavior = function(behavior){
            behaviorService.editBehavior(appService.getCurrentApplication(), behavior);
            $scope.acceptMessageResult(sendPOSTRequest(Paths.UPDATE_BEHAVIOR, angular.toJson(behaviorService.currentBehavior)));
            $scope.indexToShow = -1;
        };

        $scope.addCondition = function(){behaviorService.addCondition();};

        $scope.removeCondition = function($index){behaviorService.removeCondition($index);};

        $scope.getBehaviorAction = function(object, actionName, conditions){return behaviorService.getBehaviorAction($scope, object, actionName, conditions);};

        $scope.showBehaviorEditArea = function($event, behavior){
            $event.stopPropagation();
            if ($scope.indexToShow != behavior.id) {
                $scope.indexToShow = behavior.id;
                behaviorService.currentBehavior = {
                    id: '',
                    name: behavior.name,
                    operandObject: behavior.operandObject,
                    operator: behavior.operator,
                    operandAttribute: behavior.operandAttribute,
                    conditions: copyArray(behavior.conditions)
                };
            } else {
                $scope.indexToShow = -1
            }
        };

        $scope.hideBehaviorEditArea = function(){
            $scope.indexToShow = -1;
            behaviorService.currentBehavior = {
                id: '',
                name: '',
                operandObject: {},
                operator: '',
                operand: '',
                conditions: []
            };
        };

        $scope.addNewBehavior = function(){
            behaviorService.addNewBehavior();
            $scope.showNoBehaviorMembersImage = !$scope.showNoBehaviorMembersImage;
            $scope.toggleArea("behaviorCreateArea");
        };

        $scope.addNewCondition = function(){behaviorService.addNewCondition();};

        $scope.deleteBehavior = function(behavior){
            behaviorService.deleteBehavior(appService.currentApplication, behavior);
            if (!appService.currentApplication.behaviors.length){
                $scope.showNoBehaviorMembersImage = true;
            }
            $scope.indexToShow = -1;
            $scope.acceptMessageResult(sendPOSTRequest(Paths.REMOVE_BEHAVIOR, angular.toJson(behavior)));
        };

        $scope.showBehaviorDetails = function(behavior){
            behaviorService.showBehaviorDetails($scope, behavior);
            $scope.hideArea("objectCreateArea");
            $scope.hideArea("behaviorCreateArea");
        };

        $scope.addActionBehavior = function(){
            $scope.showArea("actionsEditAreaBehavior");
            $scope.showArea("actionsEditAreaBehavior");
            behaviorService.addActionBehavior();
            $scope.all_conditions = [];
        };

        // ----------------------------------------------------------------------Design Service Methods-----------------

        $scope.designDisplayObjectPage = function(object){
            designService.designDisplayObjectPage(object);
        };

        $scope.designDisplayBehaviorPage = function(){
            designService.showEmulatorMainPage = true;
        };

        $scope.getShowEmulatorMainPage = function(){
            return designService.getShowEmulatorMainPage()
        };

        $scope.addInstance = function(){
            designService.addInstance($scope, $scope.attribute_values);
        };

        $scope.removeInstance = function(idx){
            designService.removeInstance($scope, idx);
        };

        $scope.performBehaviorAction = function(behavior){
            designService.performBehaviorAction($scope, behavior);
        };

        $scope.getOutput = function(){
            return designService.getOutput();
        };

        $scope.getCurrentInstance = function(){
            return designService.getCurrentInstance()
        };


        // ----------------------------------------------------------------------Release Service Methods----------------

        $scope.releaseBuildApplication = function(application){
            $scope.message = "Building application...";
            releaseService.releaseBuildApplication($scope, application);
        };

        $scope.getApplicationBuilt = function(){
            return releaseService.getApplicationBuilt();
        };

        // ----------------------------------------------------------------------User Management------------------------

        $scope.login = function(user){
            $scope.message = user;
        };

        // ----------------------------------------------------------------------Status Message-------------------------

        $scope.acceptMessageResult = function(result, success, fail){
            var displayDuration = 10000;
            if (success == undefined){
                success = function(){};
            }
            if (fail == undefined){
                fail = function(){};
            }
            result.onreadystatechange = function(){
                if (result.readyState != 4 && result.status != 200){
                    $scope.updateStatus("Error");
                    fail();
                    $timeout(function () {
                        $scope.updateStatus('');
                    }, displayDuration);
                    $scope.$apply();
                }
                else if (result.readyState == 4 && result.status == 200){
                    $scope.updateStatus(result.responseText);
                    success();
                    $timeout(function () {
                        $scope.updateStatus('');
                    }, displayDuration);
                    $scope.$apply();
                }
            };
        };

        $scope.updateStatus = function(text){
            $scope.message = text;
        };

        $scope.isNumber = function(n) {
            return !isNaN(parseFloat(n)) && isFinite(n);
        };



    }]);