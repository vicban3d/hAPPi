/**
 * Created by Gila on 10/03/2016.
 */
main_module.service('appService',[function(){
    this.deleteApplication = function($scope, $event, application){
        $event.stopPropagation();
        delete $scope.applications[application.id];
        if (application == $scope.currentApplication){
            $scope.currentApplication = {};
            $scope.currentAppURL = '';
        }
        acceptMessageResult(sendPOSTRequest(Paths.REMOVE_APP, angular.toJson(application)));
    }
    var applicationConstructor = function(id, name, platforms, actions, behaviors){
        return {id: id, name: name, platforms: platforms, objects: actions, behaviors: behaviors};
    };

    var generateUUID = function() {
        var d = new Date().getTime();
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
    }

    this.addApplication = function($scope){
        if ($scope.applicationName == '' || $scope.applicationName =='Invalid Name!') {
            $scope.applicationName = 'Invalid Name!'
        }
        else{
            $scope.getPlatform();
            var appId = generateUUID();
            var newApplication = applicationConstructor(appId, $scope.applicationName, $scope.platforms, [],[]);
            $scope.currentApplication = newApplication;
            $scope.message = "Creating " + $scope.applicationName + "...";
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
    }

    this.editApplication = function($scope){
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
            this.updateApplication($scope.currentApplication.id, $scope.applicationName, $scope.platforms);
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
    }

    this.updateApplication = function(id, name, platforms){
        var newApplication = {
            id: id,
            name: name,
            platforms: platforms
        };
        acceptMessageResult(sendPOSTRequest(Paths.UPDATE_APP, angular.toJson(newApplication)));
    }

}]);