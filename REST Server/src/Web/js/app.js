var main_module = angular.module('main', []);

main_module.controller('ctrl_main', ['appService', 'objectService', 'behaviorService', '$scope', '$timeout', '$sce',
    function(appService, objectService, behaviorService, $scope, $timeout) {

        // Visible gui components //
        $scope.areaFlags = [];
        $scope.areaFlags["titleArea"] = true;
        $scope.areaFlags["workArea"] = true;
        $scope.areaFlags["centralArea"] = true;
        $scope.areaFlags["sideArea"] = true;
        $scope.areaFlags["messageArea"] = false;
        $scope.areaFlags["menuArea"] = true;
        $scope.areaFlags["menuButtonsArea"] = true;
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
        $scope.areaFlags["designArea"] = false;
        $scope.areaFlags["releaseArea"] = false;
        $scope.areaFlags["conditionEditArea"] = false;

        $scope.areaFlags["frontPage"] = true;


        $scope.applicationBuilt = false;
        $scope.applicationQRCode = undefined;

        $scope.menuButtons = [
            {'label': 'Objects',    'function': function(){$scope.menuAddObjects()}},
            {'label': 'Behavior',   'function': function(){$scope.menuAddBehaviors()}},
            //{'label': 'Design',     'function': function(){$scope.menuDesign()}},
            {'label': 'Release',    'function': function(){$scope.menuRelease()}}
        ];

        $scope.basic_types = ["Number", "Text"];
        $scope.andOrOperator = ["Or", "And"];
        $scope.logicOperations = ["Greater Than", "Less Than", "Equal", "Not Equal"];
        $scope.operators = ['Increase By', 'Reduce By', 'Multiply By', 'Divide By', 'Change To'];
        $scope.behaviorOperators = ['Sum of All', 'Product of All', 'Maximum', 'Minimum'];

        $scope.numOfAttributes = 0;
        $scope.numOfConditions = 0;
        $scope.numOfActions = 0;
        $scope.currentObject = '';
        $scope.all_attrs = [];
        $scope.all_acts_Object = [];
        $scope.all_conditions = [];
        $scope.all_acts_Behavior = [];
        $scope.all_conditions = [];
        $scope.currentBehavior = '';

        $scope.instances = [];

        $scope.behaviorName = '';
        $scope.objectName = '';

        $scope.emulatorOutput = '';
        $scope.showBehaviors = true;
        $scope.showInstance = false;

        $scope.appications = {};

        $scope.currentInstance = '';
        $scope.attribute_values = [];

        // General Functions //
        $scope.menuHome = function(){
            $scope.hideAll();
            $scope.hideArea("menuButtonsArea");
            $scope.showArea("applicationListArea");
        };
        $scope.gotoApp = function(){
            $scope.hideArea("frontPage");
        }

        $scope.backToMain = function(){
            $scope.showArea("frontPage");
        };


        // ios
        $scope.iosImg = "ios1.jpg";
        $scope.iosImgEnable = "ios_enable.jpg";

        $scope.switchIosImg = 1;
        $scope.toggleIos = function(){
            $scope.switchIosImg = 3- $scope.switchIosImg;
        }

        // android
        $scope.androidImg = "android1.jpg";
        $scope.androidImgEnable = "android_enable.jpg";

        $scope.switchAndroidImg = 1;
        $scope.toggleAndroid = function(){
            $scope.switchAndroidImg = 3- $scope.switchAndroidImg;
        }

        // windows
        $scope.windowsImg = "windows1.jpg";
        $scope.windowsImgEnable = "windows_enable.jpg";

        $scope.switchWindowsImg = 1;
        $scope.toggleWindows = function(){
            $scope.switchWindowsImg = 3- $scope.switchWindowsImg;
        }

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

        $scope.acceptMessageResult = function(result, success){
            if (success == undefined){
                success = function(){};
            }
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
                    success(result.responseText);
                    $timeout(function () {
                        $scope.message = '';
                    }, 5000);
                    $scope.$apply();
                }
            };
        };

        $scope.getPlatform = function(){ appService.getPlatform(); };

        $scope.getCurrentApplication = function() { return appService.getCurrentApplication() };

        $scope.showCurrentPlatforms = function(){ appService.showCurrentPlatforms(); };

        $scope.deleteApplication = function($event, application){ appService.deleteApplication($scope, $event, application);};

        $scope.addApplication = function(){appService.addApplication($scope, $scope.applicationName, [$scope.android, $scope.ios, $scope.windowsPhone])};

        $scope.editApplication = function(){appService.editApplication($scope, $scope.applicationName, [$scope.android, $scope.ios, $scope.windowsPhone]);};

        $scope.removeApplicationFromAppList = function(id){appService.removeApplicationFromAppList($scope, id); };

        $scope.updateApplication = function(id, name, platforms){appService.updateApplication($scope, id,name,platforms)};

        $scope.showApplicationDetails = function(application){ appService.showApplicationDetails($scope, application) };

        $scope.getApplication = function(application){ appService.getApplication($scope, application); };

        $scope.editApplicationDetails = function($event, application){ appService.editApplicationDetails($scope, $event, application); };

        $scope.createApplication = function(id, name, platforms){ appService.createApplication($scope, id, name, platforms); };

        $scope.addNewApplication = function(){ appService.addNewApplication($scope); };

        // Object Creation //
        $scope.addObject = function() { objectService.addObject($scope); };

        $scope.deleteObject = function(object){ objectService.deleteObject($scope, object); };

        $scope.addAttribute = function(){ objectService.addAttribute($scope); };

        $scope.addCondition = function(){ objectService.addCondition($scope); };

        $scope.addActionObject = function(){ objectService.addActionObject($scope); };

        $scope.addNewAction = function(){ objectService.addNewAction($scope); };

        $scope.showObjectDetails = function(object){ objectService.showObjectDetails($scope, object); };

        $scope.isValidAttribute = function(val){ objectService.isValidAttribute(val); };

        $scope.getAttributeName = function(val){ objectService.getAttributeName(val); };

        $scope.isValidActionObject = function(val){ objectService.isValidActionObject(val); };

        $scope.addNewObject = function(){ objectService.addNewObject($scope); };

        $scope.isValidCondition = function(val){
            return val.attribute != '' && val.logicOperation != '' && val.operandObject!= '';
        };

        $scope.getObjectAction = function(actionName, operand2){ objectService.getObjectAction(actionName, operand2); };

        // Behavior Creation //
        $scope.isValidActionBehavior = function(val){ behaviorService.isValidActionBehavior(val) };

        $scope.addBehavior = function(){ behaviorService.addBehavior($scope); };

        $scope.addCondition = function(){
            $scope.numOfConditions+=1;
        };

        $scope.editBehaviorDetails = function(behavior){
            behaviorService.editBehaviorDetails($scope, behavior);
        };

        $scope.getBehaviorAction = function(object, actionName){
            behaviorService.getBehaviorAction($scope, object, actionName);
        };

        $scope.editBehaviorDetails = function(behavior){
            behaviorService.editApplication($scope, behavior);
        };

        $scope.addNewBehavior = function(){
            behaviorService.addNewBehavior($scope);
        };

        $scope.addNewCondition = function(){
            behaviorService.addNewCondition($scope);
        };

        $scope.deleteBehavior = function(behavior){
            behaviorService.deleteBehavior($scope, behavior);
        };

        $scope.showBehaviorDetails = function(behavior){
            behaviorService.showBehaviorDetails($scope, behavior);
        };

        $scope.addActionBehavior = function(){
            behaviorService.addActionBehavior($scope);
        };


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
            $scope.instances[$scope.currentInstance.name].push($scope.attribute_values);
            $scope.attribute_values = [];
        };

        $scope.removeInstance = function(id){
            $scope.instances[$scope.currentInstance.name].splice(parseInt(id),1);
        };

        $scope.performBehaviorAction = function(behavior){
            var object = behavior.actions[0].operandObject;
            var operand1 = behavior.actions[0].operandAttribute.name;
            var action = $scope.getBehaviorAction(object, behavior.actions[0].operator);
            $scope.emulatorOutput = action(operand1);
        };

        $scope.addObjectToApplication = function(object){
            appService.addObjectToApplication($scope, object);
        };

        $scope.addBehaviorToApplication = function(behavior){
            appService.addBehaviorToApplication($scope, behavior);
        };




        // Release //
        $scope.releaseBuildApplication = function(application){
            $scope.acceptMessageResult(sendPOSTRequest(Paths.BUILD_APP, angular.toJson(application)),
                function(result)
                {
                    $scope.applicationQRCode = new QRCode(document.getElementById("appQRImage"), result);
                    $scope.applicationBuilt = true;
                    $scope.message = "Application built successfully!"
                });
            $scope.message = "Building application..."
        };
    }]);