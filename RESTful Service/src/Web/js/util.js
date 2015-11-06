/**
 * Created by victor on 11/6/2015.
 * Contains utility functions.
 */
function makeStruct(attributes) { // a general factory for structs
    var fields = attributes.split(' ');
    var count = fields.length;
    function constructor() {
        for (var i = 0; i < count; i++) {
            this[fields[i]] = arguments[i];
        }
    }
    return constructor;
}