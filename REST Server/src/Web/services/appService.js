/**
 * Created by Gila on 10/03/2016.
 */
main_module.service('appService',[function(){

    this.currentApplication =
    {
        id: "",
        name: this.applicationName,
        platforms: this.platforms,
        objects: [],
        behaviors: []
    };

    this.getCurrentApplication = function(){
        return this.currentApplication;
    };
    
    this.deleteApplication = function($scope, $event, application){
        $event.stopPropagation();
        delete $scope.applications[application.id];
        if (application == this.currentApplication){
            this.currentApplication = {};
            this.currentAppURL = '';
        }
       $scope.acceptMessageResult(sendPOSTRequest(Paths.REMOVE_APP, angular.toJson(application)));
    };
    
    this.applicationConstructor = function(id, name, platforms, actions, behaviors){
        return {id: id, name: name, platforms: platforms, objects: actions, behaviors: behaviors};
    };

    var generateUUID = function() {
        var d = new Date().getTime();
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
    };

    this.addApplication = function($scope, name, platforms){
        if (name == '' || name =='Invalid Name!') {
            $scope.applicationName = 'Invalid Name!'
        }
        else{
            platforms = this.getPlatform(platforms);
            var appId = generateUUID();
            var newApplication = this.applicationConstructor(appId, name, platforms, [],[]);
            this.currentApplication = newApplication;
            this.addAppToApplicationList($scope, newApplication);
            this.showApplicationDetails($scope, newApplication);
            $scope.acceptMessageResult(sendPOSTRequest(Paths.CREATE_APP, angular.toJson(newApplication)));
        }
    };

    this.editApplication = function($scope, name, platforms){
        if (name == '' || name =='Invalid Name!') {
            $scope.applicationName = 'Invalid Name!'
        }
        else{
            platforms = this.getPlatform(platforms);
            var newApplication = this.applicationConstructor(this.currentApplication.id, name, platforms,
                this.currentApplication.actions, this.currentApplication.behaviors);
            $scope.message = "Updating application...";
            $scope.showArea("messageArea");
            $scope.removeApplicationFromAppList($scope, this.currentApplication.id);
            this.addAppToApplicationList($scope, newApplication);
            $scope.acceptMessageResult(sendPOSTRequest(Paths.UPDATE_APP, angular.toJson(newApplication)));
            $scope.showArea("messageArea");
            $scope.showApplicationDetails(newApplication);
            this.currentApplication = newApplication;
            $scope.hideArea("applicationEditArea");
            $scope.showArea("centralArea");
            $scope.showArea("applicationListArea");
        }
    };

    this.addObjectToApplication = function($scope, object){
        $scope.applications[this.currentApplication.id].objects.push(object);
    };

    this.addBehaviorToApplication = function($scope, behavior){
        $scope.applications[this.currentApplication.id].behaviors.push(behavior);
    };

    this.addAppToApplicationList = function($scope, application){
        if ($scope.applications == undefined){
            $scope.applications = {};
        }
        if ($scope.applications[application.id] == undefined){
            $scope.applications[application.id] = {id:'',name:'',platforms:[],objects:[],behaviors:[]};
        }

        $scope.applications[application.id].id = application.id;
        $scope.applications[application.id].name = application.name;
        $scope.applications[application.id].platforms.push(application.platforms);
    };

    this.getPlatform = function(input){
        var platforms = [];
        if(input[0] == true)
            platforms.push("android");
        if(input[1] == true)
            platforms.push("ios");
        if(input[2] == true)
            platforms.push("wp8");
        return platforms;
    };

    this.showCurrentPlatforms = function(){
        for(var i=0; i<this.currentApplication.platforms.length; i++)
        {
            if(this.currentApplication.platforms[i] == "android")
                this.android = true;
            else if(this.currentApplication.platforms[i] == "ios")
                this.ios = true;
            else if(this.currentApplication.platforms[i] == "wp8")
                this.windowsPhone = true;
        }
    };

    this.showApplicationDetails = function($scope, application){
        if ($scope.areaFlags["applicationDetailsArea"] == false || application != $scope.currentApplication){
            $scope.currentApplication = application;
            $scope.hideArea("applicationCreateArea");
            $scope.hideArea("applicationEditArea");
            $scope.showArea("applicationDetailsArea");
        }
    };

    this.removeApplicationFromAppList = function($scope, id){
        for(var i = $scope.applications.length - 1; i >= 0; i--){
            if($scope.applications[i] == id){
                $scope.applications.splice(i,1);
            }
        }
    };

    this.getApplication = function($scope, application){
        if (this.showApplicationDetailsFlag == 1 || application != this.currentApplication){
            this.currentApplication = application;
            this.showApplicationDetailsFlag = 0
        } else{
            this.showApplicationDetailsFlag = 1
        }
        this.currentApplication = application;
        $scope.hideArea("applicationCreateArea");
        $scope.hideArea("applicationDetailsArea");
        $scope.hideArea("applicationListArea");
        $scope.showArea("menuButtonsArea");
    };

    this.editApplicationDetails = function($scope, $event, application){
        $event.stopPropagation();
        this.currentApplication = application;
        $scope.showCurrentPlatforms();
        this.applicationName = $scope.currentApplication.name;
        $scope.showArea("applicationEditArea");
        $scope.hideArea("applicationDetailsArea");
    };

}]);