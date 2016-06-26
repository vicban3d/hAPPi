/**
 * Created by Gila-Ber on 18/04/2016.
 * 
 */
main_module.service('eventService',[function(){

    this.currentEvent = {
        id : '',
        name: '',
        operandObject: {},
        operandAttribute: {},
        operator: '',
        value: '',
        username: '',
        applicationId: ''
    };

    this.addNewEvent = function() {
        this.currentEvent =
        {
            id : '',
            name: '',
            object: {},
            attribute: {},
            operator: '',
            value: '',
            username: '',
            applicationId: ''
        };
    };

    this.addEvent = function(application, username){
        this.currentEvent.id = generateUUID();
        this.currentEvent.username = username;
        this.currentEvent.applicationId = application.id;
        addEventToApplication(application, this.currentEvent);
    };

    var addEventToApplication = function(application, event){
        application.events.push(event);
    };

    this.deleteEvent = function(application, event){
        removeEventFromApplication(application, event.name);
        this.currentEvent = {};
    };

    this.editEvent = function(application, event){
        removeEventFromApplication(application, event.name);
        addEventToApplication(application, this.currentEvent);
    };


    var removeEventFromApplication = function(application, eventName){
        for(var i = application.events.length - 1; i >= 0; i--){
            if(application.events[i].name == eventName){
                application.events.splice(i,1);
            }
        }
    };

}]);