/**
 * Created by Gila on 21/03/2016.
 */
main_module.controller('signupCtrl',['$scope',
    function($scope){
        this.username="";
        this.password = "";
        this.email = "";

        this.generateUUID = function() {
            var d = new Date().getTime();
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
        };

        this.addUser = function(){
            var json = "{ \"username\" : \"" + this.username + "\", ";
            json += "\"password\" : \"" + this.password + "\", ";
            json += "\"email\" : \"" + this.email + "\" }";
            sendPOSTRequest(Paths.CREATE_USER, json);
            location.href = "http://localhost:9998/hAPPi/main";
        }
    }]);