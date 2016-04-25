/**
 * Created by Gila-Ber on 18/04/2016.
 */
main_module.service('eventService',[function(){

    this.currentEvent = {
        id : '',
        name: '',
        operandObject: {},
        operandAttribute: {},
        operator: '',
        value: ''
    };

    this.addNewEvent = function() {
        this.currentEvent =
        {
            id : '',
            name: '',
            object: {},
            attribute: {},
            operator: '',
            value: ''
        };
    };

    this.addEvent = function(application){
        this.currentEvent.id = generateUUID();
        addEventToApplication(application, this.currentEvent);
    };

    var addEventToApplication = function(application, event){
        alert(application.name);
        alert(application.behaviors)
        alert(application.events);
        application.events.push(event);
    };

    this.deleteEvent = function(application, event){
        removeEventFromApplication(application, event.name);
        this.currentEvent = {};
    };

    var removeEventFromApplication = function(application, eventName){
        for(var i = application.events.length - 1; i >= 0; i--){
            if(application.events[i].name == eventName){
                application.events.splice(i,1);
            }
        }
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