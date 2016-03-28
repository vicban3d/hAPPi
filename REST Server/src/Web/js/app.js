var main_module = angular.module('main', []);

main_module.controller('ctrl_main', ['appService', 'objectService', 'behaviorService', 'designService', 'releaseService', '$scope', '$timeout', '$sce',
    function(appService, objectService, behaviorService, designService, releaseService, $scope, $timeout) {

        // ----------------------------------------------------------------------Variable Declaration-------------------

        // Shows debug messages on the web page.
        $scope.DEBUG = true;
        $scope.message = "";

        $scope.areaFlags = [];
        $scope.areaFlags["titleArea"] = true;
        $scope.areaFlags["workArea"] = true;
        $scope.areaFlags["centralArea"] = true;
        $scope.areaFlags["sideArea"] = true;
        $scope.areaFlags["messageArea"] = false;
        $scope.areaFlags["menuArea"] = true;
        $scope.areaFlags["menuButtonsArea"] = true;
        $scope.areaFlags["applicationListArea"] = true;
        $scope.areaFlags["applicationDetailsArea"] = true;
        $scope.areaFlags["applicationEditArea"] = false;
        $scope.areaFlags["applicationCreateArea"] = true;
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
        $scope.areaFlags["conditionEditArea"] = false;
        $scope.areaFlags["frontPage"] = true;


        $scope.applicationBuilt = false;

        $scope.basic_types = ["Number", "Text"];
        $scope.andOrOperator = ["Or", "And"];
        $scope.logicOperations = ["Greater Than", "Less Than", "Equal", "Not Equal"];
        $scope.operators = ['Increase By', 'Reduce By', 'Multiply By', 'Divide By', 'Change To'];
        $scope.behaviorOperators = ['Sum of All', 'Product of All', 'Maximum', 'Minimum', 'Average'];

        $scope.instances = [];

        $scope.emulatorOutput = '';

        $scope.appications = {};

        $scope.attribute_values = [];


        // Preload phone images
        var images = [];
        function preload() {
            for (var i = 0; i < preload.arguments.length; i++) {
                images[i] = new Image();
                images[i].src = preload.arguments[i];
            }
        }
        preload(
            "http://localhost:5555/img/ios_disabled.png",
            "http://localhost:5555/img/ios_enabled.png",
            "http://localhost:5555/img/android_disabled.png",
            "http://localhost:5555/img/android_enabled.png",
            "http://localhost:5555/img/wp_disabled.png",
            "http://localhost:5555/img/wp_enabled.png"
        );

        // ios images
        $scope.iosImg = "ios_disabled.png";
        $scope.iosImgEnable = "ios_enabled.png";
        $scope.switchIosImg = 1;
        $scope.toggleIos = function(){
            $scope.switchIosImg = 3- $scope.switchIosImg;
        };
        // android images
        $scope.androidImg = "android_disabled.png";
        $scope.androidImgEnable = "android_enabled.png";
        $scope.switchAndroidImg = 1;
        $scope.toggleAndroid = function(){
            $scope.switchAndroidImg = 3- $scope.switchAndroidImg;
        };
        // wp images
        $scope.windowsImg = "wp_disabled.png";
        $scope.windowsImgEnable = "wp_enabled.png";
        $scope.switchWindowsImg = 1;
        $scope.toggleWindows = function(){
            $scope.switchWindowsImg = 3- $scope.switchWindowsImg;
        };

        $scope.menuButtons = [
            {'label': 'Objects',    'function': function(){$scope.menuAddObjects()}},
            {'label': 'Behavior',   'function': function(){$scope.menuAddBehaviors()}},
            {'label': 'Release',    'function': function(){$scope.menuRelease()}}
        ];

        // ----------------------------------------------------------------------General Functions----------------------

        $scope.menuAddObjects = function(){
            $scope.hideAll();
            $scope.showArea("objectsAddArea");
        };

        $scope.menuAddBehaviors = function(){
            $scope.hideAll();
            $scope.showArea("behaviorAddArea");
        };

        $scope.menuRelease = function(){
            $scope.hideAll();
            $scope.showArea("releaseArea");
        };

        $scope.menuHome = function(){
            $scope.hideAll();
            $scope.hideArea("menuButtonsArea");
            $scope.showArea("applicationListArea");
        };

        $scope.gotoApp = function(){
            $scope.hideArea("frontPage");
        };

        $scope.backToMain = function(){
            $scope.showArea("frontPage");
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

        $scope.toggleArea = function (area) {
            $scope.areaFlags[area] = !$scope.areaFlags[area];
        };

        // ----------------------------------------------------------------------Applications Service Methods-----------

        $scope.getPlatform = function(){ appService.getPlatform(); };

        $scope.getCurrentApplication = function() { return appService.getCurrentApplication() };

        $scope.showCurrentPlatforms = function(){ appService.showCurrentPlatforms(); };

        $scope.deleteApplication = function($event, application){ appService.deleteApplication($scope, $event, application);};

        $scope.editApplication = function(){appService.editApplication($scope, $scope.applicationName, [$scope.android, $scope.ios, $scope.windowsPhone]);};

        $scope.removeApplicationFromAppList = function(id){appService.removeApplicationFromAppList($scope, id); };

        $scope.updateApplication = function(id, name, platforms){appService.updateApplication($scope, id,name,platforms)};

        $scope.showApplicationDetails = function(application){ appService.showApplicationDetails($scope, application) };

        $scope.getApplication = function(application){ appService.getApplication($scope, application); };

        $scope.editApplicationDetails = function($event, application){ appService.editApplicationDetails($scope, $event, application); };

        $scope.createApplication = function(id, name, platforms){ appService.createApplication($scope, id, name, platforms); };

        $scope.addApplication = function(){
            $scope.updateStatus("Creating " + $scope.applicationName + "...");
            appService.addApplication($scope, $scope.applicationName, [$scope.android, $scope.ios, $scope.windowsPhone]);
        };

        $scope.addNewApplication = function(){
            $scope.hideArea("applicationDetailsArea");
            $scope.showArea("applicationCreateArea");
        };


        $scope.addObjectToApplication = function(object){
            appService.addObjectToApplication($scope, object);
        };

        $scope.addBehaviorToApplication = function(behavior){
            appService.addBehaviorToApplication($scope, behavior);
        };

        // ----------------------------------------------------------------------Object Service methods-----------------

        $scope.addObject = function() {
            objectService.addObject($scope, $scope.objectName, $scope.all_attrs, $scope.all_acts_Object);
            $scope.objectName = '';
            $scope.all_attrs = [];
            $scope.all_acts_Object = [];
            objectService.numOfAttributes = 0;
            objectService.numOfActions = 0;
            $scope.hideArea("objectCreateArea");
            $scope.hideArea("actionsEditAreaObject");
            $scope.hideArea("conditionEditArea");
            $scope.hideArea("actionsEditAreaBehavior");
        };

        $scope.getNumOfAttributes = function(){
          return objectService.getNumOfAttributes();
        };

        $scope.getNumOfObjectActions = function(){
            return objectService.getNumOfActions();
        };

        $scope.deleteObject = function(object){
            objectService.deleteObject($scope, object);
            $scope.hideArea("objectDetailsArea");
        };

        $scope.addAttribute = function(){ objectService.addAttribute(); };

        $scope.addActionObject = function(){ objectService.addActionObject(); };

        $scope.addNewAction = function(){
            objectService.addNewAction();
            $scope.hideArea("actionsEditAreaObject");
            $scope.hideArea("actionsEditAreaBehavior");
        };

        $scope.showObjectDetails = function(object){
            objectService.showObjectDetails($scope, object);
            $scope.hideArea("behaviorDetailsArea");
            $scope.hideArea("behaviorEditArea");
            $scope.hideArea("objectCreateArea");
            $scope.showArea("objectDetailsArea");
        };

        $scope.isValidAttribute = function(val){ objectService.isValidAttribute(val); };

        $scope.getAttributeName = function(val){ objectService.getAttributeName(val); };

        $scope.isValidActionObject = function(val){ objectService.isValidActionObject(val); };

        $scope.addNewObject = function(){
            objectService.addNewObject();
            $scope.hideArea("behaviorDetailsArea");
            $scope.hideArea("behaviorEditArea");
            $scope.hideArea("objectDetailsArea");
            $scope.toggleArea("objectCreateArea");
        };

        $scope.isValidCondition = function(val){
            return val.attribute != '' && val.logicOperation != '' && val.operandObject!= '';
        };

        $scope.getObjectAction = function(actionName, operand2){ return objectService.getObjectAction(actionName, operand2); };

        $scope.editObject = function(){objectService.editObject($scope, $scope.objectName, $scope.all_attrs, $scope.all_acts_Object);};

        $scope.removeObjectFromAppList = function() {objectService.removeObjectFromAppList($scope, $scope.currentApplication.id, this.currentObject);};
        $scope.editObjectDetails = function($event, object){objectService.editObjectDetails($scope, $event, object);};

        // ----------------------------------------------------------------------Behavior Service Methods---------------

        $scope.isValidActionBehavior = function(val){ behaviorService.isValidActionBehavior(val) };

        $scope.addBehavior = function(){
            behaviorService.addBehavior($scope,  $scope.behaviorName, $scope.all_acts_Behavior, $scope.all_conditions);
            $scope.all_acts_Behavior = [];
            $scope.all_conditions = [];
            behaviorService.numOfActions = 0;
            behaviorService.numOfConditions = 0;
            $scope.behaviorName = '';
            $scope.hideArea("actionsEditAreaObject");
            $scope.hideArea("actionsEditAreaBehavior");
            $scope.hideArea("conditionEditArea");
            $scope.hideArea("behaviorEditArea");
        };

        $scope.getNumOfBehaviorActions = function(){
            return behaviorService.getNumOfActions();
        };

        $scope.getNumOfConditions = function(){
            return behaviorService.getNumOfConditions();
        };

        $scope.addCondition = function(){
            behaviorService.addCondition();
        };

        $scope.getBehaviorAction = function(object, actionName, conditions){
            return behaviorService.getBehaviorAction($scope, object, actionName, conditions);
        };

        $scope.editBehaviorDetails = function(behavior){
            behaviorService.editBehaviorDetails($scope, behavior);
        };

        $scope.addNewBehavior = function(){
            $scope.hideArea("objectDetailsArea");
            $scope.hideArea("objectCreateArea");
            $scope.hideArea("behaviorDetailsArea");
            $scope.toggleArea("behaviorEditArea");
        };

        $scope.addNewCondition = function(){
            behaviorService.addNewCondition();
        };

        $scope.deleteBehavior = function(behavior){
            behaviorService.deleteBehavior($scope, behavior);
            $scope.hideArea("behaviorDetailsArea");
        };

        $scope.showBehaviorDetails = function(behavior){
            behaviorService.showBehaviorDetails($scope, behavior);
            $scope.hideArea("objectDetailsArea");
            $scope.hideArea("objectCreateArea");
            $scope.hideArea("behaviorEditArea");
            $scope.showArea("behaviorDetailsArea");
        };

        $scope.addActionBehavior = function(){
            $scope.showArea("actionsEditAreaBehavior");
            behaviorService.addActionBehavior();
            $scope.all_conditions = [];
        };


        $scope.editBehavior = function(){
            behaviorService.editBehavior($scope);
        };

        $scope.removeBehaviorFromAppList = function() {
            behaviorService.removeBehaviorFromAppList($scope, currentApplication.id, currentBehavior)
        };

        $scope.editBehaviorDetails = function($event, behavior){
            behaviorService.editBehaviorDetails($scope, $event, behavior);
        };

        // ----------------------------------------------------------------------Design Service Methods-----------------

        $scope.designDisplayObjectPage = function(object){
            designService.designDisplayObjectPage(object);
        };

        $scope.designDisplayBehaviorPage = function(){
            designService.showBehaviors = true;
            designService.showInstance = false;
        };



        $scope.getShowInstance = function(){
            return designService.getShowInstance()
        };

        $scope.getShowBehaviors = function(){
            return designService.getShowBehaviors()
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
            releaseService.releaseBuildApplication($scope, application);
            $scope.message = "Application built successfully!";
        };

        $scope.getApplicationBuilt = function(){
            return releaseService.getApplicationBuilt();
        };

        // ----------------------------------------------------------------------Status Message-------------------------

        $scope.acceptMessageResult = function(result){
            var displayDuration = 10000;
            result.onreadystatechange = function(){
                if (result.readyState != 4 && result.status != 200){
                    $scope.updateStatus("Error");
                    $timeout(function () {
                        $scope.updateStatus('');
                    }, displayDuration);
                    $scope.$apply();
                }
                else if (result.readyState == 4 && result.status == 200){
                    $scope.updateStatus(result.responseText);
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

        $scope.generateUUID = function() {
            var d = new Date().getTime();
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
        };
    }]);