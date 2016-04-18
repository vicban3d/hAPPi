/**
 * Created by Victor on 10/03/2016.
 */
main_module.service('releaseService',[function(){

    this.applicationBuilt = false;

    this.releaseBuildApplication = function($scope, application){
       
        var result = sendPOSTRequest(Paths.BUILD_APP, angular.toJson(application));
        result.onreadystatechange = function(){
            if (result.readyState != 4 && result.status != 200){
                $scope.updateStatus("Error");
                $timeout(function () {
                    $scope.updateStatus('');
                }, displayDuration);
                $scope.$apply();
            }
            else if (result.readyState == 4 && result.status == 200){
                this.applicationBuilt = true;
                this.applicationQRCode = new QRCode(document.getElementById("appQRImage"), result.responseText);
                $scope.message = this.applicationBuilt + "-" + result.responseText;
                $scope.$apply();
            }
        };
    };

    this.getApplicationBuilt = function(){
        return this.applicationBuilt;
    }

}]);