/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('releaseService',[function(){

    this.releaseBuildApplication = function($scope, application){
        $scope.acceptMessageResult(sendPOSTRequest(Paths.BUILD_APP, angular.toJson(application)));
        this.applicationQRCode = new QRCode(document.getElementById("appQRImage"), $scope.message);
        this.applicationBuilt = true;
    };

    this.getApplicationBuilt = function(){
        return this.applicationBuilt;
    }

}]);