var main_module = angular.module('main', []);

main_module.controller('ctrl_main', ['appService', 'objectService', 'behaviorService', 'eventService', 'designService', 'releaseService', '$scope',
    function(appService, objectService, behaviorService, eventService, designService, releaseService, $scope) {

        // ----------------------------------------------------------------------Variable Declaration-------------------

        $scope.message = "";

        $scope.applications = [];

        $scope.states = {
            READY : 0,
            UPDATING : 1,
            BUILDING : 2,
            DELETING : 3
        };
        
        $scope.applicationStates = {};

        $scope.areaFlags = [];
        $scope.areaFlags["mainPage"] = true;
        $scope.areaFlags["workArea"] = false;
        $scope.areaFlags["centralArea"] = false;
        $scope.areaFlags["sideArea"] = false;
        $scope.areaFlags["menuArea"] = false;
        $scope.areaFlags["objectCreateArea"] = false;
        $scope.areaFlags["behaviorCreateArea"] = false;
        $scope.areaFlags["eventCreateArea"] = false;
        $scope.areaFlags["designArea"] = false;
        $scope.areaFlags["releaseArea"] = false;
        $scope.areaFlags["frontPage"] = false;
        $scope.areaFlags["myApps"] = false;

        $scope.applicationBuilt = false;

        $scope.basic_types = ["Number", "Text"];
        $scope.andOrOperator = ["Or", "And"];
        $scope.logicOperations = ["Greater Than", "Less Than", "Equal", "Not Equal"];
        // $scope.numberOperators = ['Increase By', 'Multiply By', 'Reduce By', 'Divide By', 'Change To'];
        // $scope.textOperators = ['Add Prefix', 'Add Suffix', 'Change To'];
        $scope.behaviorTextOperators = ['Display'];
        $scope.behaviorNumberOperators = ['Sum of All', 'Display', 'Product of All', 'Maximum', 'Minimum', 'Average'];
        $scope.actionOperations = ["+", "-"];
        $scope.operand_types = ["Fixed Value", "Attribute", "Dynamic"];

        $scope.instances = [];
        $scope.emulatorOutput = '';
        $scope.attribute_values = [];
        $scope.showNoObjectMembersImage = true;
        $scope.showNoBehaviorMembersImage = true;
        
        $scope.isLastActionChainLink = function(chainIndex, index){
            return objectService.isLastActionChainLink(chainIndex, index)
        };

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
            // $scope.ios = !$scope.ios;
            // $scope.switchIosImg = 3- $scope.switchIosImg;
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
            // $scope.windowsPhone = !$scope.windowsPhone;
            // $scope.switchWindowsImg = 3- $scope.switchWindowsImg;
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
            $scope.showNoBehaviorMembersImage = true;
            $scope.indexToShow = -1;
            $scope.hideAll();
            $scope.showWorkAreas();
            $scope.showArea("objectsAddArea");
        };

        $scope.menuAddBehaviors = function(){
            $scope.showNoObjectMembersImage = true;
            $scope.indexToShow = -1;
            $scope.hideAll();
            $scope.showWorkAreas();
            $scope.showArea("behaviorCreateArea");

        };

        $scope.menuRelease = function(){
            $scope.showNoObjectMembersImage = true;
            $scope.showNoBehaviorMembersImage = true;
            $scope.indexToShow = -1;
            $scope.hideAll();
            $scope.showWorkAreas();
            $scope.showArea("releaseArea");
        };

        $scope.gotoApp = function(){
            $scope.showNoObjectMembersImage = true;
            $scope.showNoBehaviorMembersImage = true;
            $scope.indexToShow = -1;
            $scope.hideArea("frontPage");
        };

        $scope.gotoMainPage = function(){
            $scope.showNoObjectMembersImage = true;
            $scope.showNoBehaviorMembersImage = true;
            $scope.indexToShow = -1;
            $scope.hideAll();
            $scope.showArea('mainPage');
        };

        $scope.backToMain = function(){
            $scope.showNoObjectMembersImage = true;
            $scope.showNoBehaviorMembersImage = true;
            $scope.indexToShow = -1;
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
            $scope.showNoObjectMembersImage = true;
            $scope.showNoBehaviorMembersImage = true;
            $scope.indexToShow = -1;
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

        $scope.isValidApplication = function (application) {
            return appService.isValidApplication($scope, application);
        };

        $scope.getCurrentApplication = function() { return appService.getCurrentApplication() };

        $scope.showCurrentPlatforms = function(){ appService.showCurrentPlatforms(); };

        $scope.deleteApplication = function(application){
            $scope.applicationStates[application.id] = $scope.states.DELETING;
            $scope.indexToDelete = -1;
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.REMOVE_APP,
                angular.toJson(application)),
                function () {appService.deleteApplication($scope.applications, application);},
                function () {alert('Failed to delete application!')}
            );
        };

        $scope.addApplication = function(){
            $scope.message = "Creating " + $scope.applicationName + "...";
            appService.currentApplication =
            {
                id: '',
                name: $scope.applicationName,
                username: $scope.currentUser.username,
                platforms: appService.getPlatformsArray([$scope.android, $scope.ios, $scope.windowsPhone]),
                objects: [],
                behaviors: [],
                events: []
            };
            

            appService.addApplication($scope.applications);

            $scope.applicationStates[appService.currentApplication.id] = $scope.states.UPDATING;
            var id = appService.currentApplication.id;
            $scope.acceptMessageResult(
                sendPOSTRequestPlainText(Paths.CREATE_APP, angular.toJson(appService.currentApplication)),
                function(){
                    $scope.applicationStates[id] = $scope.states.READY;
                });
            $scope.getApplication(appService.currentApplication);
            $scope.menuAddObjects();
        };

        $scope.editApplication = function(application){
            $scope.message = "Updating application...";

            var id = application.id;
            $scope.applicationStates[id] = $scope.states.UPDATING;
            appService.currentApplication.platforms = appService.getPlatformsArray([$scope.android, $scope.ios, $scope.windowsPhone]);
            appService.currentApplication.username = $scope.currentUser.username;
            appService.editApplication($scope.applications, application);
            $scope.indexToShow = -1;
            $scope.acceptMessageResult(
                sendPOSTRequestPlainText(Paths.UPDATE_APP, angular.toJson(appService.currentApplication)),
                function(){
                    $scope.applicationStates[id] = $scope.states.READY;
                });
        };

        $scope.removeApplicationFromApplicationList = function(id){appService.removeApplicationFromApplicationList($scope, id); };

        $scope.updateApplication = function(id, name, platforms){appService.updateApplication($scope, id,name,platforms)};

        $scope.getApplication = function(application){
            $scope.indexToShow = -1;
            appService.getApplication($scope, application);
            $scope.menuAddObjects();

        };

        $scope.hideApplicationDeleteArea = function(){
            $scope.indexToDelete = -1;
        };

        $scope.hideApplicationEditArea = function(){
            $scope.indexToShow = -1;
            $scope.applicationName = "";
            $scope.android = false;
            $scope.ios = false;
            $scope.windowsPhone = false;
        };

        $scope.showApplicationDeleteArea = function(application){
            $scope.indexToDelete = application.id;
            appService.currentApplication = {
                id: application.id,
                name: application.name,
                username: $scope.currentUser.username,
                platforms: application.platforms,
                objects: application.objects,
                behaviors: application.behaviors
            };
        };

        $scope.showApplicationEditArea = function(application){
            $scope.indexToShow = application.id;
            appService.currentApplication = {
                id: application.id,
                name: application.name,
                username: $scope.currentUser.username,
                platforms: application.platforms,
                objects: application.objects,
                behaviors: application.behaviors
            };

            $scope.android = false;
            $scope.ios = false;
            $scope.windowsPhone = false;
            $scope.switchAndroidImg = 1;
            $scope.switchIosImg = 1;
            $scope.switchWindowsImg = 1;

            for(var i=0; i<appService.currentApplication.platforms.length; i++)
            {
                if(appService.currentApplication.platforms[i] == "android") {
                    $scope.switchAndroidImg = 2;
                    $scope.android = true;
                }
                else if(appService.currentApplication.platforms[i] == "ios") {
                    $scope.switchIosImg = 2;
                    $scope.ios = true;
                }
                else if(appService.currentApplication.platforms[i] == "wp8") {
                    $scope.switchWindowsImg = 2;
                    $scope.windowsPhone = true;
                }
            }

        };

        // $scope.createApplication = function(id, name, platforms){ appService.createApplication($scope, id, name, platforms); };

        // ----------------------------------------------------------------------Object Service methods-----------------

        $scope.addObject = function() {
            objectService.addObject(appService.currentApplication);
            for (var i=0; i< objectService.currentObject.actionChains.length; i++) {
                var chain =  objectService.currentObject.actionChains[i];
                chain.actions = chain.actions.map(function (act) {
                    if(act.operandAttribute === "")
                        return {operandAction: act.operandAction, operator: act.operator};
                    if(act.operandAction === "")
                        return {operandAttribute: act.operandAttribute, operator: act.operator};
                });
            }
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.CREATE_OBJECT, angular.toJson(objectService.currentObject)),
                function () {},
                function () {alert("Failed to create object!")});
            $scope.hideArea("objectCreateArea");
        };

        $scope.isValidObject = function(object) {
            return objectService.isValidObject($scope, object);
        };

        $scope.deleteObject = function(object){
            objectService.deleteObject(appService.currentApplication, object);
            if (!appService.currentApplication.objects.length){
                $scope.showNoObjectMembersImage = true;
            }
            $scope.indexToShow = -1;
            if (object.name === designService.currentInstance.name){
                designService.designDisplayBehaviorPage();
            }
	        object["username"] = $scope.currentUser.username;
            object["applicationId"] = appService.currentApplication.id;
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.REMOVE_OBJECT, angular.toJson(object)),
                function () {},
                function () {alert("Failed to delete object!")});
        };

        $scope.addAttribute = function(){ objectService.addAttribute(); };

        $scope.removeAttribute = function($index){
            objectService.removeAttribute($index);
        };
        
        $scope.addAction = function(){ objectService.addAction(); };

        $scope.addActionChain = function(){ objectService.addActionChain(); };
        
        $scope.removeAction = function($index){ objectService.removeAction($index); };

        $scope.removeActionChain = function($index){ objectService.removeActionChain($index); };

        $scope.addActionChainLink = function(index){
            objectService.addActionChainLink(index);
        };

        $scope.removeActionChainLink = function(chainIndex, linkIndex){
            objectService.removeActionChainLink(chainIndex, linkIndex);
        };

        $scope.checkDisabled = function(){
            return objectService.checkDisabled();
        };

        $scope.getAttributeName = function(val){ objectService.getAttributeName(val); };

        $scope.addNewObject = function(){
            $scope.indexToShow = -1;
            objectService.addNewObject(appService.currentApplication.id, $scope.currentUser.username);
            $scope.showNoObjectMembersImage = !$scope.showNoObjectMembersImage;
            $scope.toggleArea("objectCreateArea");
        };

        $scope.getObjectAction = function(actionChainName, object){ return objectService.getObjectAction(actionChainName, object); };

        $scope.editObject = function(object){
            var all_behaviors = appService.currentApplication.behaviors;
            for (var i=0; i< all_behaviors.length; i++){
                if (all_behaviors[i].operandObject == object){
                    all_behaviors[i].operandObject = objectService.currentObject;
                    if (objectService.currentObject.attributes.indexOf(objectService.currentObject.operandAttribute) >= 0){
                        all_behaviors[i].operandAttribute = objectService.currentObject.operandAttribute;
                    } else {
                        alert("Warning: Operand in Behavior " + all_behaviors[i].name + " was deleted or replaced!");
                    }
                }
            }

            for (i=0; i< objectService.currentObject.actionChains.length; i++) {
                var chain =  objectService.currentObject.actionChains[i];
                chain.actions = chain.actions.map(function (act) {
                    if(act.operandAttribute === "" || act.operandAttribute == null)
                        return {operandAction: act.operandAction, operator: act.operator};
                    if(act.operandAction === "" || act.operandAction == null)
                        return {operandAttribute: act.operandAttribute, operator: act.operator};
                });
            }
            
            objectService.editObject(appService.currentApplication, object);

            objectService.currentObject["username"] = $scope.currentUser.username;
            objectService.currentObject["applicationId"] = appService.currentApplication.id;
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.UPDATE_OBJECT, angular.toJson(objectService.currentObject)),
                function () {},
                function () {alert("Failed to edit object!")});
            $scope.indexToShow = -1;

            if (designService.currentInstance.name === object.name){
                designService.designDisplayObjectPage(objectService.currentObject);
            }

        };

        $scope.showObjectEditArea = function(object){

            if ($scope.indexToShow != object.id) {
                $scope.indexToShow = object.id;
                objectService.currentObject = {
                    id: object.id,
                    name: object.name,
                    attributes: copyArray(object.attributes),
                    actions: copyArray(object.actions),
                    actionChains: copyArray(object.actionChains)
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

        $scope.performObjectAction = function(actionName, object, instance, dynamicValue){
            objectService.performObjectAction(actionName, object, instance, dynamicValue);
        };

        $scope.getOperatorListByType = function (type) {
            return objectService.getOperatorListByType(type);
        };

        $scope.getAttributeByName = function(name){
            var attr_names = $scope.getCurrentObject().attributes.map(function (a) {return a.name});
            var index = attr_names.indexOf(name);
            return $scope.getCurrentObject().attributes[index];
        };

        // ----------------------------------------------------------------------Behavior Service Methods---------------

        var behaviorObjToJSONOBJ = function(behaviorObj){
            var jsonObj = {};
            jsonObj["id"] = behaviorObj["id"];
            jsonObj["name"] = behaviorObj["name"];
            jsonObj["applicationId"] = behaviorObj["applicationId"];
            jsonObj["username"] = behaviorObj["username"];

            jsonObj["action"] = {};
            jsonObj["action"]["operandObject"] = behaviorObj["operandObject"];
            if(behaviorObj["operandAttribute"] === "")
                jsonObj["action"]["operandAttribute"] = behaviorObj["operandAttribute"]["name"];
            else
                jsonObj["action"]["actionChain"] = behaviorObj["actionChain"]["name"];
            if (behaviorObj["conditions"] === undefined)
                jsonObj["action"]["conditions"]=[];
            else {
                jsonObj["action"]["conditions"] = behaviorObj["conditions"];
            }
            jsonObj["action"]["operator"] = behaviorObj["operator"];
            jsonObj["action"]["operandType"] = behaviorObj["operandType"];
            jsonObj["action"]["operand2"] = behaviorObj["operand2"];
            return jsonObj;
        };

        $scope.addBehavior = function(){
            behaviorService.addBehavior(appService.getCurrentApplication(), $scope.currentUser.username);
            var jsonObj = behaviorObjToJSONOBJ(behaviorService.currentBehavior);

            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.CREATE_BEHAVIOR, angular.toJson(jsonObj)));
            $scope.hideArea("behaviorCreateArea");
        };

        $scope.isValidBehavior = function(behavior) {
            return behaviorService.isValidBehavior($scope, behavior);
        };

        $scope.getCurrentBehavior = function(){
            return behaviorService.currentBehavior;
        };

        $scope.editBehavior = function(behavior){
            behaviorService.editBehavior(appService.getCurrentApplication(), $scope.currentUser.username, behavior);
            // behavior["username"] = $scope.currentUser.username;
            // behavior["applicationId"] = appService.currentApplication.id;
            var jsonObj = behaviorObjToJSONOBJ(behaviorService.currentBehavior);
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.UPDATE_BEHAVIOR, angular.toJson(jsonObj)));
            $scope.indexToShow = -1;
        };

        $scope.addCondition = function(){behaviorService.addCondition();};

        $scope.removeCondition = function($index){behaviorService.removeCondition($index);};

        $scope.getBehaviorAction = function(object, actionName, conditions){return behaviorService.getBehaviorAction($scope, object, actionName, conditions);};

        $scope.showBehaviorEditArea = function(behavior){
            if ($scope.indexToShow != behavior.id) {
                $scope.indexToShow = behavior.id;
                behaviorService.currentBehavior = {
                    id: behavior.id,
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
                operandAttribute: {},
                conditions: []
            };
        };

        $scope.addNewBehavior = function(){
            $scope.indexToShow = -1;
            behaviorService.addNewBehavior();
            $scope.showNoBehaviorMembersImage = !$scope.showNoBehaviorMembersImage;
            $scope.toggleArea("behaviorCreateArea");
        };

        $scope.deleteBehavior = function(behavior){
            behaviorService.deleteBehavior(appService.currentApplication, behavior);
            if (!appService.currentApplication.behaviors.length){
                $scope.showNoBehaviorMembersImage = true;
            }
            $scope.indexToShow = -1;
            behavior["username"] = $scope.currentUser.username;
            behavior["applicationId"] = appService.currentApplication.id;
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.REMOVE_BEHAVIOR, angular.toJson(behavior)));
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

        // ----------------------------------------------------------------------Event Service Methods---------------
        $scope.addNewEvent = function(){
            eventService.addNewEvent();
            //$scope.showNoBehaviorMembersImage = !$scope.showNoBehaviorMembersImage;
            $scope.toggleArea("eventCreateArea");
        };

        $scope.getCurrentEvent = function(){
            return eventService.currentEvent;
        };

        $scope.addEvent = function(){
            eventService.addEvent(appService.getCurrentApplication(), $scope.currentUser.username);
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.CREATE_EVENT, angular.toJson(eventService.currentEvent)));
            $scope.hideArea("eventCreateArea");
        };

        $scope.showEventEditArea = function($event, event){
            $event.stopPropagation();
            if ($scope.indexToShow != event.id) {
                $scope.indexToShow = event.id;
                eventService.currentEvent = {
                    id: event.id,
                    name: event.name,
                    object: event.object,
                    attribute: event.attribute,
                    operator: event.operator,
                    value: event.value
                };
            } else {
                $scope.indexToShow = -1
            }
        };

        $scope.deleteEvent = function(event){
            eventService.deleteEvent(appService.currentApplication, event);
            /*if (!appService.currentApplication.events.length){
                $scope.showNoEventMembersImage = true;
            }*/
            $scope.indexToShow = -1;
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.REMOVE_EVENT, angular.toJson(event)));
        };

        $scope.hideEventEditArea = function(){
            $scope.indexToShow = -1;
            eventService.currentEvent = {
                id: '',
                name: '',
                object: {},
                attribute: {},
                operator: '',
                value: ''
            };
        };

        $scope.editEvent = function(event){
            eventService.editEvent(appService.getCurrentApplication(), event);
            $scope.acceptMessageResult(sendPOSTRequestPlainText(Paths.UPDATE_EVENT, angular.toJson(eventService.currentEvent)));
            $scope.indexToShow = -1;
        };

        // ----------------------------------------------------------------------Design Service Methods-----------------

        $scope.designDisplayObjectPage = function(object){
            designService.designDisplayObjectPage(object);
        };

        $scope.designDisplayBehaviorPage = function(){
            designService.designDisplayBehaviorPage();
        };

        $scope.getShowEmulatorMainPage = function(){
            return designService.getShowEmulatorMainPage()
        };

        $scope.getShowAddInstance = function(){
            return designService.getShowAddInstance();
        };

        $scope.gotoAppInstance = function(phoneNumber){
            designService.gotoAppInstance(phoneNumber);
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
            $scope.applicationBuildError = false;
            releaseService.applicationBuilt = false;
            $scope.message = "Building application...";
            $scope.applicationStates[application.id] = $scope.states.BUILDING;
            // releaseService.releaseBuildApplication($scope, application);
            var app = application;
            $scope.acceptMessageResult(sendPOSTRequest(Paths.BUILD_APP, angular.toJson(application)),
            function(result){
                $scope.applicationStates[app.id] = $scope.states.READY;
                releaseService.applicationBuilt = true;
                releaseService.applicationQRCode = new QRCode(document.getElementById("appQRImage"), result);
                $scope.qrLink = result;
            },
              function(){
                  releaseService.applicationBuilt = true;
                  $scope.applicationBuildError = true;
                  $scope.applicationStates[app.id] = $scope.states.READY;
              }
            );
        };

        $scope.getApplicationBuilt = function(){
            return releaseService.applicationBuilt;
        };

        $scope.getApplicationBuilt = function(){
            return releaseService.getApplicationBuilt();
        };

        // ----------------------------------------------------------------------User Management------------------------

        $scope.signUp = function (user) {
            if (user.password != $scope.confirmPassword){
                $scope.invalidSignupMessage = 'Passwords do not match!';
                return;
            }

            $scope.acceptMessageResult(
                sendPOSTRequest(Paths.CREATE_USER, angular.toJson(user)),
                function(){
                    $scope.message = "Signed Up in as: " + user.username;
                    $scope.invalidSignupMessage = '';
                    $scope.signupUser = {username: '', password: '', email: ''};
                    $scope.confirmPassword = '';
                    $scope.login(user);
                },
                function () {
                    $scope.invalidSignupMessage = "Invalid credentials!";
                }
            );
        };

        $scope.login = function(user){
            $scope.message = "Logging in as: " + user.username;
            $scope.acceptMessageResult(
                sendPOSTRequest(Paths.LOGIN, angular.toJson(user)),
                function(appsList){
                    $scope.currentUser = {
                        username: user.username,
                        password: user.password,
                        email: ''
                    };
                    var fixedResponse = appsList.replace(/\\'/g, "'");
                    $scope.applications = [];
                    var temp = JSON.parse(fixedResponse);
                    for (var i=0; i< temp.length; i++){
                        $scope.applications.push(JSON.parse(temp[i]));
                        $scope.applicationStates[$scope.applications[i].id] = $scope.states.READY;
                    }
                    if ($scope.applications.length > 0){
                        $scope.showAppsList();
                    } else {
                        $scope.backToMain();
                    }
                    $scope.invalidUsernameMessage = '';
                    $scope.loginUser={username: '', password: '', email: ''};
                },
                function () {
                    $scope.message = "Invalid User!";
                    $scope.invalidUsernameMessage = "Invalid username or password!";
                }
            );
        };

        // ----------------------------------------------------------------------Status Message-------------------------

        $scope.acceptMessageResult = function(result, success, fail){
            if (success == undefined){
                success = function(){};
            }
            if (fail == undefined){
                fail = function(){};
            }
            result.onreadystatechange = function(){
                if (result.readyState != 4 && result.status != 200){
                    $scope.message = "Error";
                    fail();
                    $scope.$apply();
                }
                else if (result.readyState == 4 && result.status == 200){
                    $scope.message = result.responseText;
                    success(result.responseText);
                    $scope.$apply();
                }
            };
        };
        //
        // $scope.isNumber = function(n) {
        //     return !isNaN(parseFloat(n)) && isFinite(n);
        // };

        $scope.compare = function(a,b) {
            if (a.name.toLowerCase() < b.name.toLowerCase())
                return -1;
            else if (a.name.toLowerCase() > b.name.toLowerCase())
                return 1;
            else
                return 0;
        };


        // Help Texts

        $scope.HT_object = 'Objects are templates for the data in your application. An Object consists of the Attributes that describe it and the Actions that can be performed on those Attributes.';
        $scope.HT_object_attribute = 'Attributes make an object what it is, Ex. Name, Speed, Age, etc.';
        $scope.HT_object_action = 'Actions can be performed on Attributes to manipulate them. Ex. Increase, Multiply, etc.';
        $scope.HT_object_action_chain = 'Action Chains can combine several Actions and Attributes in a series of more complicated calculations.';
        $scope.HT_object_action_chain_link = 'An Action Chain consists of Links which are an Attribute (or Action) and an Operation.';
        $scope.HT_behavior = 'Behavior defines calculations that can be performed on an Object.';
        $scope.HT_behavior_target = 'The Behavior Target is the Object data on which the calculations are based.';
        $scope.HT_behavior_condition = 'Conditions allow you to specify constraints on the data used for calculations.';
        $scope.HT_release = 'Releasing you application will generate fully-functional applications according to the platforms you choose. The Applications can then be downloaded and installed on your device.';
        $scope.HT_my_app = 'This page lists all the applications you created. From here you can edit the basic information of you applications or delete them. Click on "Go >" near an application to start working on it.';


    }]);