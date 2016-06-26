/**
 * Created by Gila on 10/03/2016.
 *
 */

main_module.service('appService',[function(){

    this.newApplication = function(){
        return {
            id: "",
            name: '',
            platforms: '',
            objects: [],
            behaviors: [],
            events: [],
            username: ''
        };
    };

    this.currentApplication = this.newApplication();

    this.addApplication = function(applications){
        this.currentApplication.id = generateUUID();
        this.addApplicationToApplicationList(applications, this.currentApplication);
    };

    this.editApplication = function(applications, application){
        this.removeApplicationFromApplicationList(applications, application.id);
        this.addApplicationToApplicationList(applications, this.currentApplication);
    };

    this.deleteApplication = function(applications, application){
        this.removeApplicationFromApplicationList(applications, application.id);
        this.currentApplication = {};
    };

    this.isValidApplication = function($scope, application){
        if ($scope.applicationName == undefined){
            return true;
        }
        var all_apps = $scope.applications;
        for (var i=0; i< all_apps.length; i++) {
            if (all_apps[i].name === $scope.applicationName)
            {
                $scope.applicationCreateErrorMessage = "An Application by that name already exists!";
                return false;
            }
        }
        return true;
    };

    this.getCurrentApplication = function(){
        return this.currentApplication;
    };

    this.setCurrentApplication = function($scope, application){
        this.currentApplication = application;
    };

    this.addApplicationToApplicationList = function(applications, application){
        applications.push(application);
    };

    this.removeApplicationFromApplicationList = function(applications, applicationId){
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
    
}]);

