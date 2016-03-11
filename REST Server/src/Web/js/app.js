var main_module = angular.module('main', []);

main_module.controller('ctrl_main', ['appService', 'objectService', '$scope', '$timeout', '$sce',
    function(appService, objectService, $scope, $timeout) {

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

        $scope.getPlatform = function(){ appService.getPlatform(); };

        $scope.getCurrentApplication = function() { return appService.getCurrentApplication() };

        $scope.showCurrentPlatforms = function(){ appService.showCurrentPlatforms(); };

        $scope.deleteApplication = function($event, application){ appService.deleteApplication($scope, $event, application);};

        $scope.addApplication = function(){appService.addApplication($scope, $scope.applicationName, [$scope.android, $scope.ios, $scope.windowsPhone])};

        $scope.editApplication = function(){appService.editApplication($scope, $scope.applicationName, [$scope.android, $scope.ios, $scope.windowsPhone]);};

        $scope.removeApplicationFromAppList = function(id){appService.removeApplicationFromAppList($scope, id); };

        $scope.updateApplication = function(id, name, platforms){appService.updateApplication($scope, id,name,platforms)};

        $scope.showApplicationDetails = function(application){ appService.showApplicationDetails($scope, application) };

        $scope.getApplication = function(application){
           appService.getApplication($scope, application);
        };

        $scope.editApplicationDetails = function($event, application){
            appService.editApplicationDetails($scope, $event, application);
        };

        $scope.addNewApplication = function(){
            $scope.message = '';
            $scope.hideArea("applicationDetailsArea");
            $scope.showArea("applicationCreateArea")
        };


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

        $scope.isValidActionBehavior = function(val){ objectService.isValidActionBehavior(val) };

        $scope.addNewObject = function(){ objectService.addNewObject($scope); };

        $scope.isValidCondition = function(val){
            return val.attribute != '' && val.logicOperation != '' && val.operandObject!= '';
        };

        // Behavior Creation //
        $scope.addBehavior = function(){
            if ($scope.behaviorName == '' || $scope.behaviorName =='Invalid Name!'){
                $scope.behaviorName = 'Invalid Name!'
            }
            else {
                var newBehavior = {
                    name: $scope.behaviorName,
                    actions: $scope.all_acts_Behavior.filter($scope.isValidActionBehavior),
                    conditions: $scope.all_conditions.filter($scope.isValidCondition)
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
                acceptMessageResult(sendPOSTRequest(Paths.CREATE_BEHAVIOR, angular.toJson(newBehavior)));
            }
        };

        $scope.addCondition = function(){
            $scope.numOfConditions+=1;
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
                return function () {
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
            } else if (actionName == "Average"){
                return function (operand){
                    var index = object.attributes.map(function(a) {return a.name;}).indexOf(operand);
                    var result = 0;
                    var i = 0;
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
                    return result/$scope.instances[object.name].length;
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

        $scope.addNewCondition = function(){
            if ($scope.conditionName == '' || $scope.conditionName =='Invalid Name!'){ //TODO - check what invalid
                $scope.conditionName = 'Invalid Name!'
            }
            else {
                $scope.numOfConditions += 1;
                $scope.hideArea("actionsEditAreaObject");
                $scope.hideArea("actionsEditAreaBehavior");
            }
        };

        $scope.createApplication = function(id, name, platforms){
            appService.createApplication($scope, id, name, platforms);
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
            var index =  $scope.applications[appService.getCurrentApplication().id].behaviors.indexOf(behavior);
            $scope.applications[appService.getCurrentApplication().id].behaviors.splice(index, 1);
            if (behavior == $scope.currentBehavior){
                $scope.currentBehavior = {};
            }
            acceptMessageResult(sendPOSTRequest(Paths.REMOVE_BEHAVIOR, angular.toJson(behavior)));
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
        $scope.attribute_values = [];

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
            var action = getBehaviorAction(object, behavior.actions[0].operator);
            $scope.emulatorOutput = action(operand1);
        };

        $scope.addObjectToApplication = function(object){
            appService.addObjectToApplication($scope, object);
        };

        $scope.addBehaviorToApplication = function(behavior){
            appService.addBehaviorToApplication($scope, behavior);
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
        }


        // Release //
        $scope.releaseBuildApplication = function(application){
            acceptMessageResult(sendPOSTRequest(Paths.BUILD_APP, angular.toJson(application)),
                function(result)
                {
                    $scope.applicationQRCode = new QRCode(document.getElementById("appQRImage"), result);
                    $scope.applicationBuilt = true;
                    $scope.message = "Application built successfully!"
                });
            $scope.message = "Building application..."
        };
    }]);