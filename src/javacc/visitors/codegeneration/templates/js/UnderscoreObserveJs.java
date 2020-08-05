package javacc.visitors.codegeneration.templates.js;

public class UnderscoreObserveJs implements JsFile {
  @Override
  public String getName() {
    return "lib/underscore-observe.js";
  }
  
  @Override
  public String getContent() {
    return "'use strict';\n" +
            "\n" +
            "(function(factory) {\n" +
            "    if (typeof module === 'object' && typeof module.exports === 'object') {\n" +
            "        module.exports = function(_) {\n" +
            "            factory(_);\n" +
            "        };\n" +
            "    } else {\n" +
            "        factory(_);\n" +
            "    }\n" +
            "}(function(_) {\n" +
            "\n" +
            "    var _detect_timeout;\n" +
            "    var _subjects = [];\n" +
            "    var _observables = [];\n" +
            "\n" +
            "    // ES5 Object support check from Modernizr\n" +
            "    var _es5_object_supported = !!(Object.keys &&\n" +
            "        Object.create &&\n" +
            "        Object.getPrototypeOf &&\n" +
            "        Object.getOwnPropertyNames &&\n" +
            "        Object.isSealed &&\n" +
            "        Object.isFrozen &&\n" +
            "        Object.isExtensible &&\n" +
            "        Object.getOwnPropertyDescriptor &&\n" +
            "        Object.defineProperty &&\n" +
            "        Object.defineProperties &&\n" +
            "        Object.seal &&\n" +
            "        Object.freeze &&\n" +
            "        Object.preventExtensions);\n" +
            "\n" +
            "    function ObservableArray(subject) {\n" +
            "        var _first_bind = true;\n" +
            "        var _old_subject = [];\n" +
            "        var _handlers = {\n" +
            "            generic: [],\n" +
            "            create: [],\n" +
            "            update: [],\n" +
            "            'delete': []\n" +
            "        };\n" +
            "\n" +
            "        function reset() {\n" +
            "            callGenericSubscribers();\n" +
            "            _old_subject = JSON.parse(JSON.stringify(subject));\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        function callGenericSubscribers() {\n" +
            "            _.each(_handlers.generic, function(f) {\n" +
            "                f(subject, _old_subject);\n" +
            "            });\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        function callCreateSubscribers(new_item, item_index) {\n" +
            "            _.each(_handlers.create, function(f) {\n" +
            "                f(new_item, item_index);\n" +
            "            });\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        function callUpdateSubscribers(new_item, old_item, item_index) {\n" +
            "            _.each(_handlers.update, function(f) {\n" +
            "                f(new_item, old_item, item_index);\n" +
            "            });\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        function callDeleteSubscribers(deleted_item, item_index) {\n" +
            "            _.each(_handlers['delete'], function(f) {\n" +
            "                f(deleted_item, item_index);\n" +
            "            });\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        function detectChanges() {\n" +
            "            var old_length = _old_subject.length;\n" +
            "            var new_length = subject.length;\n" +
            "\n" +
            "            if (old_length !== new_length || JSON.stringify(_old_subject) !==  JSON.stringify(subject)) {\n" +
            "                var max = Math.max(new_length, old_length) - 1;\n" +
            "\n" +
            "                for (var i = max; i >= 0; i--) {\n" +
            "                    var old_item = _old_subject[i];\n" +
            "                    var new_item = subject[i];\n" +
            "                    if (i > old_length - 1) {\n" +
            "                        callCreateSubscribers(new_item, i);\n" +
            "                    } else if (i > new_length - 1) {\n" +
            "                        callDeleteSubscribers(old_item, i);\n" +
            "                    } else if (!_.isEqual(new_item, old_item)) {\n" +
            "                        callUpdateSubscribers(new_item, old_item, i);\n" +
            "                    }\n" +
            "                }\n" +
            "\n" +
            "                reset();\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        /* ################################################################\n" +
            "           Array mutator methods\n" +
            "        ################################################################ */\n" +
            "\n" +
            "\n" +
            "        function overrideMethod(name, f) {\n" +
            "            // Use Object.defineProperty to prevent methods from appearing in\n" +
            "            // the subject's for in loop\n" +
            "            if (_es5_object_supported) {\n" +
            "                Object.defineProperty(subject, name, {\n" +
            "                    value: f\n" +
            "                });\n" +
            "            } else {\n" +
            "                subject[name] = f;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        // We need to augment all the standard Array mutator methods to notify\n" +
            "        // all observers in case of a change.\n" +
            "        //\n" +
            "        // https://developer.mozilla.org/en/JavaScript/Reference/Global_Objects/Array#Mutator_methods\n" +
            "\n" +
            "        // pop: Removes the last element from an array and returns that element.\n" +
            "        overrideMethod('pop', function() {\n" +
            "            detectChanges();\n" +
            "            var deleted_item = Array.prototype.pop.apply(this, arguments);\n" +
            "            var item_index = this.length;\n" +
            "            callDeleteSubscribers(deleted_item, item_index);\n" +
            "            reset();\n" +
            "            return deleted_item;\n" +
            "        });\n" +
            "\n" +
            "\n" +
            "        // push: Adds one or more elements to the end of an array and returns\n" +
            "        // the new length of the array.\n" +
            "        overrideMethod('push', function() {\n" +
            "            detectChanges();\n" +
            "            var new_item = arguments[0];\n" +
            "            var new_length = Array.prototype.push.apply(this, arguments);\n" +
            "            callCreateSubscribers(new_item, new_length - 1);\n" +
            "            reset();\n" +
            "            return new_length;\n" +
            "        });\n" +
            "\n" +
            "\n" +
            "        // reverse: Reverses the order of the elements of an array -- the first\n" +
            "        // becomes the last, and the last becomes the first.\n" +
            "        overrideMethod('reverse', function() {\n" +
            "            detectChanges();\n" +
            "            // Always use reverse loops when deleting stuff based on index\n" +
            "            for (var j = this.length - 1; j >= 0; j--) {\n" +
            "                callDeleteSubscribers(this[j], j);\n" +
            "            }\n" +
            "            var result = Array.prototype.reverse.apply(this, arguments);\n" +
            "            _.each(this, callCreateSubscribers);\n" +
            "            reset();\n" +
            "            return result;\n" +
            "        });\n" +
            "\n" +
            "\n" +
            "        // shift: Removes the first element from an array and returns that\n" +
            "        // element.\n" +
            "        overrideMethod('shift', function() {\n" +
            "            detectChanges();\n" +
            "            var deleted_item = Array.prototype.shift.apply(this, arguments);\n" +
            "            callDeleteSubscribers(deleted_item, 0);\n" +
            "            reset();\n" +
            "            return deleted_item;\n" +
            "        });\n" +
            "\n" +
            "\n" +
            "        // sort: Sorts the elements of an array.\n" +
            "        overrideMethod('sort', function() {\n" +
            "            detectChanges();\n" +
            "            // Always use reverse loops when deleting stuff based on index\n" +
            "            for (var j = this.length - 1; j >= 0; j--) {\n" +
            "                callDeleteSubscribers(this[j], j);\n" +
            "            }\n" +
            "            var result = Array.prototype.sort.apply(this, arguments);\n" +
            "            _.each(this, callCreateSubscribers);\n" +
            "            reset();\n" +
            "            return result;\n" +
            "        });\n" +
            "\n" +
            "\n" +
            "        // splice: Adds and/or removes elements from an array.\n" +
            "        overrideMethod('splice', function(i /*, length , insert */) {\n" +
            "            detectChanges();\n" +
            "            var insert = Array.prototype.slice.call(arguments, 2);\n" +
            "            var deleted = Array.prototype.splice.apply(this, arguments);\n" +
            "            // Always use reverse loops when deleting stuff based on index\n" +
            "            for (var j = deleted.length - 1; j >= 0; j--) {\n" +
            "                callDeleteSubscribers(deleted[j], i + j);\n" +
            "            }\n" +
            "            _.each(insert, function(new_item, k) {\n" +
            "                callCreateSubscribers(new_item, i + k);\n" +
            "            });\n" +
            "            reset();\n" +
            "            return deleted;\n" +
            "        });\n" +
            "\n" +
            "\n" +
            "        // unshift: Adds one or more elements to the front of an array and\n" +
            "        // returns the new length of the array.\n" +
            "        overrideMethod('unshift', function() {\n" +
            "            detectChanges();\n" +
            "            var new_length = Array.prototype.unshift.apply(this, arguments);\n" +
            "            _.each(arguments, function(new_item, i) {\n" +
            "                callCreateSubscribers(new_item, i);\n" +
            "            });\n" +
            "            reset();\n" +
            "            return new_length;\n" +
            "        });\n" +
            "\n" +
            "\n" +
            "        //setInterval(detectChanges, 250);\n" +
            "        //detectChanges();\n" +
            "\n" +
            "\n" +
            "        return {\n" +
            "            detectChanges: detectChanges,\n" +
            "            unbind: function(type, handler) {\n" +
            "                if (_.isUndefined(type) && _.isUndefined(handler)) {\n" +
            "                    _.each(_handlers, function(handler) {\n" +
            "                        handler.length = 0;\n" +
            "                    });\n" +
            "                } else if (_.isString(type) && _.isUndefined(handler)) {\n" +
            "                    _handlers[type].length = 0;\n" +
            "                } else if (_.isFunction(type) && _.isUndefined(handler)) {\n" +
            "                    handler = type;\n" +
            "                    type = 'generic';\n" +
            "                    _handlers[type] = _.without(_handlers[type], handler);\n" +
            "                } else if (_.isString(type) && _.isFunction(handler)) {\n" +
            "                    _handlers[type] = _.without(_handlers[type], handler);\n" +
            "                }\n" +
            "\n" +
            "            },\n" +
            "            bind: function(type, handler) {\n" +
            "                _handlers[type].push(handler);\n" +
            "                if (type === 'generic') {\n" +
            "                    handler(subject, _old_subject);\n" +
            "                } else if (type === 'create') {\n" +
            "                    _.each(subject, function(item, index) {\n" +
            "                        // Don't do this, it will add the current array as an\n" +
            "                        // extra argument:\n" +
            "                        //_.each(subject, handler);\n" +
            "                        handler(item, index);\n" +
            "                    });\n" +
            "                }\n" +
            "\n" +
            "                if (_first_bind) {\n" +
            "                    _old_subject = JSON.parse(JSON.stringify(subject));\n" +
            "                    _first_bind = false;\n" +
            "                }\n" +
            "            }\n" +
            "        };\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    _.mixin({\n" +
            "        observe: function(subject, type, f) {\n" +
            "            if (!_.isArray(subject)) {\n" +
            "                throw 'subject should be a array';\n" +
            "            }\n" +
            "\n" +
            "            if (_.isFunction(type)) {\n" +
            "                f = type;\n" +
            "                type = 'generic';\n" +
            "            }\n" +
            "\n" +
            "            var index = _.indexOf(_subjects, subject);\n" +
            "            if (index === -1) {\n" +
            "                index = _subjects.length;\n" +
            "                _subjects.push(subject);\n" +
            "                var observable = new ObservableArray(subject);\n" +
            "                _observables.push(observable);\n" +
            "            }\n" +
            "            _observables[index].bind(type, f);\n" +
            "\n" +
            "            scheduleDetectAllChanges();\n" +
            "            return subject;\n" +
            "        },\n" +
            "\n" +
            "\n" +
            "        unobserve: function(subject, type, f) {\n" +
            "            if (!arguments.length) {\n" +
            "                // _.unobserve() removes all observers\n" +
            "                _.each(_observables, function(observable) {\n" +
            "                    observable.unbind();\n" +
            "                });\n" +
            "                _subjects.length = 0;\n" +
            "                _observables.length = 0;\n" +
            "                return;\n" +
            "            }\n" +
            "\n" +
            "            if (!_.isArray(subject)) {\n" +
            "                throw 'subject should be a array';\n" +
            "            }\n" +
            "\n" +
            "            var index = _.indexOf(_subjects, subject);\n" +
            "            if (index === -1) {\n" +
            "                return;\n" +
            "            }\n" +
            "\n" +
            "            _observables[index].unbind(type, f);\n" +
            "            return subject;\n" +
            "        }\n" +
            "    });\n" +
            "\n" +
            "\n" +
            "    function detectAllChanges() {\n" +
            "        if (!_observables.length) {\n" +
            "            _detect_timeout = null;\n" +
            "            return;\n" +
            "        }\n" +
            "\n" +
            "        _.each(_observables, function(observable) {\n" +
            "            observable.detectChanges();\n" +
            "        });\n" +
            "\n" +
            "        scheduleDetectAllChanges();\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    function scheduleDetectAllChanges() {\n" +
            "        if (_detect_timeout) {\n" +
            "            clearTimeout(_detect_timeout);\n" +
            "        }\n" +
            "        _detect_timeout = setTimeout(detectAllChanges, 250);\n" +
            "    }\n" +
            "\n" +
            "}));";
  }
}
