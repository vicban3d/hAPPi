/**
 * Created by Gila on 10/03/2016.
 */

main_module.service('appService',[function(){

    this.currentApplication = {
        id: "",
        name: '',
        platforms: '',
        objects: [],
        behaviors: [],
        events: [],
        username: ''
    };

    this.getCurrentApplication = function(){
        return this.currentApplication;
    };

    this.isValidApplication = function($scope){
        if ($scope.applicationName == undefined){
            return true;
        }
        var all_apps = $scope.applications;
        $scope.message = $scope.applicationStates[currentApplication.id];
        for (var i=0; i< all_apps.length; i++) {
            if (all_apps[i].name.valueOf() == $scope.applicationName.valueOf())
            {
                $scope.createErrorMessage = "An Application by that name already exists!";
                return false;
            }
        }
        return true;
    };

    this.deleteApplication = function(applications, application){
        removeApplicationFromApplicationList(applications, application.id);
        this.currentApplication = {};
    };

    this.addApplication = function(applications){
        this.currentApplication.id = generateUUID();
        addApplicationToApplicationList(applications, this.currentApplication);
    };

    this.editApplication = function(applications, application){
        removeApplicationFromApplicationList(applications, application.id);
        addApplicationToApplicationList(applications, this.currentApplication);
    };

    var addApplicationToApplicationList = function(applications, application){
        applications.push(application);
    };

    var removeApplicationFromApplicationList = function(applications, applicationId){
        for(var i = applications.length - 1; i >= 0; i--){
            if(applications[i].id == applicationId){
                applications.splice(i,1);
            }
        }
    };

    this.getPlatformsArray = function(input){
        var platforms = [];
        if(input[0] == true)
            platforms.push("android");
        if(input[1] == true)
            platforms.push("ios");
        if(input[2] == true)
            platforms.push("wp8");
        return platforms;
    };

    this.getApplication = function($scope, application){
        this.currentApplication = application;
    };

}]);

