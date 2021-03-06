package javacc.visitors.codegeneration.templates.js;

import java.io.File;

public class UnderscoreJs implements JsFile {
  @Override
  public String getName() {
    return "lib/underscore.js";
  }
  
  @Override
  public String getContent() {
    return "//     Underscore.js 1.2.1\n" +
            "//     (c) 2011 Jeremy Ashkenas, DocumentCloud Inc.\n" +
            "//     Underscore is freely distributable under the MIT license.\n" +
            "//     Portions of Underscore are inspired or borrowed from Prototype,\n" +
            "//     Oliver Steele's Functional, and John Resig's Micro-Templating.\n" +
            "//     For all details and documentation:\n" +
            "//     http://documentcloud.github.com/underscore\n" +
            "\n" +
            "(function() {\n" +
            "\n" +
            "  // Baseline setup\n" +
            "  // --------------\n" +
            "\n" +
            "  // Establish the root object, `window` in the browser, or `global` on the server.\n" +
            "  var root = this;\n" +
            "\n" +
            "  // Save the previous value of the `_` variable.\n" +
            "  var previousUnderscore = root._;\n" +
            "\n" +
            "  // Establish the object that gets returned to break out of a loop iteration.\n" +
            "  var breaker = {};\n" +
            "\n" +
            "  // Save bytes in the minified (but not gzipped) version:\n" +
            "  var ArrayProto = Array.prototype, ObjProto = Object.prototype, FuncProto = Function.prototype;\n" +
            "\n" +
            "  // Create quick reference variables for speed access to core prototypes.\n" +
            "  var slice            = ArrayProto.slice,\n" +
            "      unshift          = ArrayProto.unshift,\n" +
            "      toString         = ObjProto.toString,\n" +
            "      hasOwnProperty   = ObjProto.hasOwnProperty;\n" +
            "\n" +
            "  // All **ECMAScript 5** native function implementations that we hope to use\n" +
            "  // are declared here.\n" +
            "  var\n" +
            "    nativeForEach      = ArrayProto.forEach,\n" +
            "    nativeMap          = ArrayProto.map,\n" +
            "    nativeReduce       = ArrayProto.reduce,\n" +
            "    nativeReduceRight  = ArrayProto.reduceRight,\n" +
            "    nativeFilter       = ArrayProto.filter,\n" +
            "    nativeEvery        = ArrayProto.every,\n" +
            "    nativeSome         = ArrayProto.some,\n" +
            "    nativeIndexOf      = ArrayProto.indexOf,\n" +
            "    nativeLastIndexOf  = ArrayProto.lastIndexOf,\n" +
            "    nativeIsArray      = Array.isArray,\n" +
            "    nativeKeys         = Object.keys,\n" +
            "    nativeBind         = FuncProto.bind;\n" +
            "\n" +
            "  // Create a safe reference to the Underscore object for use below.\n" +
            "  var _ = function(obj) { return new wrapper(obj); };\n" +
            "\n" +
            "  // Export the Underscore object for **Node.js** and **\"CommonJS\"**, with\n" +
            "  // backwards-compatibility for the old `require()` API. If we're not in\n" +
            "  // CommonJS, add `_` to the global object.\n" +
            "  if (typeof exports !== 'undefined') {\n" +
            "    if (typeof module !== 'undefined' && module.exports) {\n" +
            "      exports = module.exports = _;\n" +
            "    }\n" +
            "    exports._ = _;\n" +
            "  } else if (typeof define === 'function' && define.amd) {\n" +
            "    // Register as a named module with AMD.\n" +
            "    define('underscore', function() {\n" +
            "      return _;\n" +
            "    });\n" +
            "  } else {\n" +
            "    // Exported as a string, for Closure Compiler \"advanced\" mode.\n" +
            "    root['_'] = _;\n" +
            "  }\n" +
            "\n" +
            "  // Current version.\n" +
            "  _.VERSION = '1.2.1';\n" +
            "\n" +
            "  // Collection Functions\n" +
            "  // --------------------\n" +
            "\n" +
            "  // The cornerstone, an `each` implementation, aka `forEach`.\n" +
            "  // Handles objects with the built-in `forEach`, arrays, and raw objects.\n" +
            "  // Delegates to **ECMAScript 5**'s native `forEach` if available.\n" +
            "  var each = _.each = _.forEach = function(obj, iterator, context) {\n" +
            "    if (obj == null) return;\n" +
            "    if (nativeForEach && obj.forEach === nativeForEach) {\n" +
            "      obj.forEach(iterator, context);\n" +
            "    } else if (obj.length === +obj.length) {\n" +
            "      for (var i = 0, l = obj.length; i < l; i++) {\n" +
            "        if (i in obj && iterator.call(context, obj[i], i, obj) === breaker) return;\n" +
            "      }\n" +
            "    } else {\n" +
            "      for (var key in obj) {\n" +
            "        if (hasOwnProperty.call(obj, key)) {\n" +
            "          if (iterator.call(context, obj[key], key, obj) === breaker) return;\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  };\n" +
            "\n" +
            "  // Return the results of applying the iterator to each element.\n" +
            "  // Delegates to **ECMAScript 5**'s native `map` if available.\n" +
            "  _.map = function(obj, iterator, context) {\n" +
            "    var results = [];\n" +
            "    if (obj == null) return results;\n" +
            "    if (nativeMap && obj.map === nativeMap) return obj.map(iterator, context);\n" +
            "    each(obj, function(value, index, list) {\n" +
            "      results[results.length] = iterator.call(context, value, index, list);\n" +
            "    });\n" +
            "    return results;\n" +
            "  };\n" +
            "\n" +
            "  // **Reduce** builds up a single result from a list of values, aka `inject`,\n" +
            "  // or `foldl`. Delegates to **ECMAScript 5**'s native `reduce` if available.\n" +
            "  _.reduce = _.foldl = _.inject = function(obj, iterator, memo, context) {\n" +
            "    var initial = memo !== void 0;\n" +
            "    if (obj == null) obj = [];\n" +
            "    if (nativeReduce && obj.reduce === nativeReduce) {\n" +
            "      if (context) iterator = _.bind(iterator, context);\n" +
            "      return initial ? obj.reduce(iterator, memo) : obj.reduce(iterator);\n" +
            "    }\n" +
            "    each(obj, function(value, index, list) {\n" +
            "      if (!initial) {\n" +
            "        memo = value;\n" +
            "        initial = true;\n" +
            "      } else {\n" +
            "        memo = iterator.call(context, memo, value, index, list);\n" +
            "      }\n" +
            "    });\n" +
            "    if (!initial) throw new TypeError(\"Reduce of empty array with no initial value\");\n" +
            "    return memo;\n" +
            "  };\n" +
            "\n" +
            "  // The right-associative version of reduce, also known as `foldr`.\n" +
            "  // Delegates to **ECMAScript 5**'s native `reduceRight` if available.\n" +
            "  _.reduceRight = _.foldr = function(obj, iterator, memo, context) {\n" +
            "    if (obj == null) obj = [];\n" +
            "    if (nativeReduceRight && obj.reduceRight === nativeReduceRight) {\n" +
            "      if (context) iterator = _.bind(iterator, context);\n" +
            "      return memo !== void 0 ? obj.reduceRight(iterator, memo) : obj.reduceRight(iterator);\n" +
            "    }\n" +
            "    var reversed = (_.isArray(obj) ? obj.slice() : _.toArray(obj)).reverse();\n" +
            "    return _.reduce(reversed, iterator, memo, context);\n" +
            "  };\n" +
            "\n" +
            "  // Return the first value which passes a truth test. Aliased as `detect`.\n" +
            "  _.find = _.detect = function(obj, iterator, context) {\n" +
            "    var result;\n" +
            "    any(obj, function(value, index, list) {\n" +
            "      if (iterator.call(context, value, index, list)) {\n" +
            "        result = value;\n" +
            "        return true;\n" +
            "      }\n" +
            "    });\n" +
            "    return result;\n" +
            "  };\n" +
            "\n" +
            "  // Return all the elements that pass a truth test.\n" +
            "  // Delegates to **ECMAScript 5**'s native `filter` if available.\n" +
            "  // Aliased as `select`.\n" +
            "  _.filter = _.select = function(obj, iterator, context) {\n" +
            "    var results = [];\n" +
            "    if (obj == null) return results;\n" +
            "    if (nativeFilter && obj.filter === nativeFilter) return obj.filter(iterator, context);\n" +
            "    each(obj, function(value, index, list) {\n" +
            "      if (iterator.call(context, value, index, list)) results[results.length] = value;\n" +
            "    });\n" +
            "    return results;\n" +
            "  };\n" +
            "\n" +
            "  // Return all the elements for which a truth test fails.\n" +
            "  _.reject = function(obj, iterator, context) {\n" +
            "    var results = [];\n" +
            "    if (obj == null) return results;\n" +
            "    each(obj, function(value, index, list) {\n" +
            "      if (!iterator.call(context, value, index, list)) results[results.length] = value;\n" +
            "    });\n" +
            "    return results;\n" +
            "  };\n" +
            "\n" +
            "  // Determine whether all of the elements match a truth test.\n" +
            "  // Delegates to **ECMAScript 5**'s native `every` if available.\n" +
            "  // Aliased as `all`.\n" +
            "  _.every = _.all = function(obj, iterator, context) {\n" +
            "    var result = true;\n" +
            "    if (obj == null) return result;\n" +
            "    if (nativeEvery && obj.every === nativeEvery) return obj.every(iterator, context);\n" +
            "    each(obj, function(value, index, list) {\n" +
            "      if (!(result = result && iterator.call(context, value, index, list))) return breaker;\n" +
            "    });\n" +
            "    return result;\n" +
            "  };\n" +
            "\n" +
            "  // Determine if at least one element in the object matches a truth test.\n" +
            "  // Delegates to **ECMAScript 5**'s native `some` if available.\n" +
            "  // Aliased as `any`.\n" +
            "  var any = _.some = _.any = function(obj, iterator, context) {\n" +
            "    iterator = iterator || _.identity;\n" +
            "    var result = false;\n" +
            "    if (obj == null) return result;\n" +
            "    if (nativeSome && obj.some === nativeSome) return obj.some(iterator, context);\n" +
            "    each(obj, function(value, index, list) {\n" +
            "      if (result |= iterator.call(context, value, index, list)) return breaker;\n" +
            "    });\n" +
            "    return !!result;\n" +
            "  };\n" +
            "\n" +
            "  // Determine if a given value is included in the array or object using `===`.\n" +
            "  // Aliased as `contains`.\n" +
            "  _.include = _.contains = function(obj, target) {\n" +
            "    var found = false;\n" +
            "    if (obj == null) return found;\n" +
            "    if (nativeIndexOf && obj.indexOf === nativeIndexOf) return obj.indexOf(target) != -1;\n" +
            "    found = any(obj, function(value) {\n" +
            "      return value === target;\n" +
            "    });\n" +
            "    return found;\n" +
            "  };\n" +
            "\n" +
            "  // Invoke a method (with arguments) on every item in a collection.\n" +
            "  _.invoke = function(obj, method) {\n" +
            "    var args = slice.call(arguments, 2);\n" +
            "    return _.map(obj, function(value) {\n" +
            "      return (method.call ? method || value : value[method]).apply(value, args);\n" +
            "    });\n" +
            "  };\n" +
            "\n" +
            "  // Convenience version of a common use case of `map`: fetching a property.\n" +
            "  _.pluck = function(obj, key) {\n" +
            "    return _.map(obj, function(value){ return value[key]; });\n" +
            "  };\n" +
            "\n" +
            "  // Return the maximum element or (element-based computation).\n" +
            "  _.max = function(obj, iterator, context) {\n" +
            "    if (!iterator && _.isArray(obj)) return Math.max.apply(Math, obj);\n" +
            "    if (!iterator && _.isEmpty(obj)) return -Infinity;\n" +
            "    var result = {computed : -Infinity};\n" +
            "    each(obj, function(value, index, list) {\n" +
            "      var computed = iterator ? iterator.call(context, value, index, list) : value;\n" +
            "      computed >= result.computed && (result = {value : value, computed : computed});\n" +
            "    });\n" +
            "    return result.value;\n" +
            "  };\n" +
            "\n" +
            "  // Return the minimum element (or element-based computation).\n" +
            "  _.min = function(obj, iterator, context) {\n" +
            "    if (!iterator && _.isArray(obj)) return Math.min.apply(Math, obj);\n" +
            "    if (!iterator && _.isEmpty(obj)) return Infinity;\n" +
            "    var result = {computed : Infinity};\n" +
            "    each(obj, function(value, index, list) {\n" +
            "      var computed = iterator ? iterator.call(context, value, index, list) : value;\n" +
            "      computed < result.computed && (result = {value : value, computed : computed});\n" +
            "    });\n" +
            "    return result.value;\n" +
            "  };\n" +
            "\n" +
            "  // Shuffle an array.\n" +
            "  _.shuffle = function(obj) {\n" +
            "    var shuffled = [], rand;\n" +
            "    each(obj, function(value, index, list) {\n" +
            "      if (index == 0) {\n" +
            "        shuffled[0] = value;\n" +
            "      } else {\n" +
            "        rand = Math.floor(Math.random() * (index + 1));\n" +
            "        shuffled[index] = shuffled[rand];\n" +
            "        shuffled[rand] = value;\n" +
            "      }\n" +
            "    });\n" +
            "    return shuffled;\n" +
            "  };\n" +
            "\n" +
            "  // Sort the object's values by a criterion produced by an iterator.\n" +
            "  _.sortBy = function(obj, iterator, context) {\n" +
            "    return _.pluck(_.map(obj, function(value, index, list) {\n" +
            "      return {\n" +
            "        value : value,\n" +
            "        criteria : iterator.call(context, value, index, list)\n" +
            "      };\n" +
            "    }).sort(function(left, right) {\n" +
            "      var a = left.criteria, b = right.criteria;\n" +
            "      return a < b ? -1 : a > b ? 1 : 0;\n" +
            "    }), 'value');\n" +
            "  };\n" +
            "\n" +
            "  // Groups the object's values by a criterion. Pass either a string attribute\n" +
            "  // to group by, or a function that returns the criterion.\n" +
            "  _.groupBy = function(obj, val) {\n" +
            "    var result = {};\n" +
            "    var iterator = _.isFunction(val) ? val : function(obj) { return obj[val]; };\n" +
            "    each(obj, function(value, index) {\n" +
            "      var key = iterator(value, index);\n" +
            "      (result[key] || (result[key] = [])).push(value);\n" +
            "    });\n" +
            "    return result;\n" +
            "  };\n" +
            "\n" +
            "  // Use a comparator function to figure out at what index an object should\n" +
            "  // be inserted so as to maintain order. Uses binary search.\n" +
            "  _.sortedIndex = function(array, obj, iterator) {\n" +
            "    iterator || (iterator = _.identity);\n" +
            "    var low = 0, high = array.length;\n" +
            "    while (low < high) {\n" +
            "      var mid = (low + high) >> 1;\n" +
            "      iterator(array[mid]) < iterator(obj) ? low = mid + 1 : high = mid;\n" +
            "    }\n" +
            "    return low;\n" +
            "  };\n" +
            "\n" +
            "  // Safely convert anything iterable into a real, live array.\n" +
            "  _.toArray = function(iterable) {\n" +
            "    if (!iterable)                return [];\n" +
            "    if (iterable.toArray)         return iterable.toArray();\n" +
            "    if (_.isArray(iterable))      return slice.call(iterable);\n" +
            "    if (_.isArguments(iterable))  return slice.call(iterable);\n" +
            "    return _.values(iterable);\n" +
            "  };\n" +
            "\n" +
            "  // Return the number of elements in an object.\n" +
            "  _.size = function(obj) {\n" +
            "    return _.toArray(obj).length;\n" +
            "  };\n" +
            "\n" +
            "  // Array Functions\n" +
            "  // ---------------\n" +
            "\n" +
            "  // Get the first element of an array. Passing **n** will return the first N\n" +
            "  // values in the array. Aliased as `head`. The **guard** check allows it to work\n" +
            "  // with `_.map`.\n" +
            "  _.first = _.head = function(array, n, guard) {\n" +
            "    return (n != null) && !guard ? slice.call(array, 0, n) : array[0];\n" +
            "  };\n" +
            "\n" +
            "  // Returns everything but the last entry of the array. Especcialy useful on\n" +
            "  // the arguments object. Passing **n** will return all the values in\n" +
            "  // the array, excluding the last N. The **guard** check allows it to work with\n" +
            "  // `_.map`.\n" +
            "  _.initial = function(array, n, guard) {\n" +
            "    return slice.call(array, 0, array.length - ((n == null) || guard ? 1 : n));\n" +
            "  };\n" +
            "\n" +
            "  // Get the last element of an array. Passing **n** will return the last N\n" +
            "  // values in the array. The **guard** check allows it to work with `_.map`.\n" +
            "  _.last = function(array, n, guard) {\n" +
            "    return (n != null) && !guard ? slice.call(array, array.length - n) : array[array.length - 1];\n" +
            "  };\n" +
            "\n" +
            "  // Returns everything but the first entry of the array. Aliased as `tail`.\n" +
            "  // Especially useful on the arguments object. Passing an **index** will return\n" +
            "  // the rest of the values in the array from that index onward. The **guard**\n" +
            "  // check allows it to work with `_.map`.\n" +
            "  _.rest = _.tail = function(array, index, guard) {\n" +
            "    return slice.call(array, (index == null) || guard ? 1 : index);\n" +
            "  };\n" +
            "\n" +
            "  // Trim out all falsy values from an array.\n" +
            "  _.compact = function(array) {\n" +
            "    return _.filter(array, function(value){ return !!value; });\n" +
            "  };\n" +
            "\n" +
            "  // Return a completely flattened version of an array.\n" +
            "  _.flatten = function(array, shallow) {\n" +
            "    return _.reduce(array, function(memo, value) {\n" +
            "      if (_.isArray(value)) return memo.concat(shallow ? value : _.flatten(value));\n" +
            "      memo[memo.length] = value;\n" +
            "      return memo;\n" +
            "    }, []);\n" +
            "  };\n" +
            "\n" +
            "  // Return a version of the array that does not contain the specified value(s).\n" +
            "  _.without = function(array) {\n" +
            "    return _.difference(array, slice.call(arguments, 1));\n" +
            "  };\n" +
            "\n" +
            "  // Produce a duplicate-free version of the array. If the array has already\n" +
            "  // been sorted, you have the option of using a faster algorithm.\n" +
            "  // Aliased as `unique`.\n" +
            "  _.uniq = _.unique = function(array, isSorted, iterator) {\n" +
            "    var initial = iterator ? _.map(array, iterator) : array;\n" +
            "    var result = [];\n" +
            "    _.reduce(initial, function(memo, el, i) {\n" +
            "      if (0 == i || (isSorted === true ? _.last(memo) != el : !_.include(memo, el))) {\n" +
            "        memo[memo.length] = el;\n" +
            "        result[result.length] = array[i];\n" +
            "      }\n" +
            "      return memo;\n" +
            "    }, []);\n" +
            "    return result;\n" +
            "  };\n" +
            "\n" +
            "  // Produce an array that contains the union: each distinct element from all of\n" +
            "  // the passed-in arrays.\n" +
            "  _.union = function() {\n" +
            "    return _.uniq(_.flatten(arguments, true));\n" +
            "  };\n" +
            "\n" +
            "  // Produce an array that contains every item shared between all the\n" +
            "  // passed-in arrays. (Aliased as \"intersect\" for back-compat.)\n" +
            "  _.intersection = _.intersect = function(array) {\n" +
            "    var rest = slice.call(arguments, 1);\n" +
            "    return _.filter(_.uniq(array), function(item) {\n" +
            "      return _.every(rest, function(other) {\n" +
            "        return _.indexOf(other, item) >= 0;\n" +
            "      });\n" +
            "    });\n" +
            "  };\n" +
            "\n" +
            "  // Take the difference between one array and another.\n" +
            "  // Only the elements present in just the first array will remain.\n" +
            "  _.difference = function(array, other) {\n" +
            "    return _.filter(array, function(value){ return !_.include(other, value); });\n" +
            "  };\n" +
            "\n" +
            "  // Zip together multiple lists into a single array -- elements that share\n" +
            "  // an index go together.\n" +
            "  _.zip = function() {\n" +
            "    var args = slice.call(arguments);\n" +
            "    var length = _.max(_.pluck(args, 'length'));\n" +
            "    var results = new Array(length);\n" +
            "    for (var i = 0; i < length; i++) results[i] = _.pluck(args, \"\" + i);\n" +
            "    return results;\n" +
            "  };\n" +
            "\n" +
            "  // If the browser doesn't supply us with indexOf (I'm looking at you, **MSIE**),\n" +
            "  // we need this function. Return the position of the first occurrence of an\n" +
            "  // item in an array, or -1 if the item is not included in the array.\n" +
            "  // Delegates to **ECMAScript 5**'s native `indexOf` if available.\n" +
            "  // If the array is large and already in sort order, pass `true`\n" +
            "  // for **isSorted** to use binary search.\n" +
            "  _.indexOf = function(array, item, isSorted) {\n" +
            "    if (array == null) return -1;\n" +
            "    var i, l;\n" +
            "    if (isSorted) {\n" +
            "      i = _.sortedIndex(array, item);\n" +
            "      return array[i] === item ? i : -1;\n" +
            "    }\n" +
            "    if (nativeIndexOf && array.indexOf === nativeIndexOf) return array.indexOf(item);\n" +
            "    for (i = 0, l = array.length; i < l; i++) if (array[i] === item) return i;\n" +
            "    return -1;\n" +
            "  };\n" +
            "\n" +
            "  // Delegates to **ECMAScript 5**'s native `lastIndexOf` if available.\n" +
            "  _.lastIndexOf = function(array, item) {\n" +
            "    if (array == null) return -1;\n" +
            "    if (nativeLastIndexOf && array.lastIndexOf === nativeLastIndexOf) return array.lastIndexOf(item);\n" +
            "    var i = array.length;\n" +
            "    while (i--) if (array[i] === item) return i;\n" +
            "    return -1;\n" +
            "  };\n" +
            "\n" +
            "  // Generate an integer Array containing an arithmetic progression. A port of\n" +
            "  // the native Python `range()` function. See\n" +
            "  // [the Python documentation](http://docs.python.org/library/functions.html#range).\n" +
            "  _.range = function(start, stop, step) {\n" +
            "    if (arguments.length <= 1) {\n" +
            "      stop = start || 0;\n" +
            "      start = 0;\n" +
            "    }\n" +
            "    step = arguments[2] || 1;\n" +
            "\n" +
            "    var len = Math.max(Math.ceil((stop - start) / step), 0);\n" +
            "    var idx = 0;\n" +
            "    var range = new Array(len);\n" +
            "\n" +
            "    while(idx < len) {\n" +
            "      range[idx++] = start;\n" +
            "      start += step;\n" +
            "    }\n" +
            "\n" +
            "    return range;\n" +
            "  };\n" +
            "\n" +
            "  // Function (ahem) Functions\n" +
            "  // ------------------\n" +
            "\n" +
            "  // Reusable constructor function for prototype setting.\n" +
            "  var ctor = function(){};\n" +
            "\n" +
            "  // Create a function bound to a given object (assigning `this`, and arguments,\n" +
            "  // optionally). Binding with arguments is also known as `curry`.\n" +
            "  // Delegates to **ECMAScript 5**'s native `Function.bind` if available.\n" +
            "  // We check for `func.bind` first, to fail fast when `func` is undefined.\n" +
            "  _.bind = function bind(func, context) {\n" +
            "    var bound, args;\n" +
            "    if (func.bind === nativeBind && nativeBind) return nativeBind.apply(func, slice.call(arguments, 1));\n" +
            "    if (!_.isFunction(func)) throw new TypeError;\n" +
            "    args = slice.call(arguments, 2);\n" +
            "    return bound = function() {\n" +
            "      if (!(this instanceof bound)) return func.apply(context, args.concat(slice.call(arguments)));\n" +
            "      ctor.prototype = func.prototype;\n" +
            "      var self = new ctor;\n" +
            "      var result = func.apply(self, args.concat(slice.call(arguments)));\n" +
            "      if (Object(result) === result) return result;\n" +
            "      return self;\n" +
            "    };\n" +
            "  };\n" +
            "\n" +
            "  // Bind all of an object's methods to that object. Useful for ensuring that\n" +
            "  // all callbacks defined on an object belong to it.\n" +
            "  _.bindAll = function(obj) {\n" +
            "    var funcs = slice.call(arguments, 1);\n" +
            "    if (funcs.length == 0) funcs = _.functions(obj);\n" +
            "    each(funcs, function(f) { obj[f] = _.bind(obj[f], obj); });\n" +
            "    return obj;\n" +
            "  };\n" +
            "\n" +
            "  // Memoize an expensive function by storing its results.\n" +
            "  _.memoize = function(func, hasher) {\n" +
            "    var memo = {};\n" +
            "    hasher || (hasher = _.identity);\n" +
            "    return function() {\n" +
            "      var key = hasher.apply(this, arguments);\n" +
            "      return hasOwnProperty.call(memo, key) ? memo[key] : (memo[key] = func.apply(this, arguments));\n" +
            "    };\n" +
            "  };\n" +
            "\n" +
            "  // Delays a function for the given number of milliseconds, and then calls\n" +
            "  // it with the arguments supplied.\n" +
            "  _.delay = function(func, wait) {\n" +
            "    var args = slice.call(arguments, 2);\n" +
            "    return setTimeout(function(){ return func.apply(func, args); }, wait);\n" +
            "  };\n" +
            "\n" +
            "  // Defers a function, scheduling it to run after the current call stack has\n" +
            "  // cleared.\n" +
            "  _.defer = function(func) {\n" +
            "    return _.delay.apply(_, [func, 1].concat(slice.call(arguments, 1)));\n" +
            "  };\n" +
            "\n" +
            "  // Returns a function, that, when invoked, will only be triggered at most once\n" +
            "  // during a given window of time.\n" +
            "  _.throttle = function(func, wait) {\n" +
            "    var context, args, timeout, throttling, more;\n" +
            "    var whenDone = _.debounce(function(){ more = throttling = false; }, wait);\n" +
            "    return function() {\n" +
            "      context = this; args = arguments;\n" +
            "      var later = function() {\n" +
            "        timeout = null;\n" +
            "        if (more) func.apply(context, args);\n" +
            "        whenDone();\n" +
            "      };\n" +
            "      if (!timeout) timeout = setTimeout(later, wait);\n" +
            "      if (throttling) {\n" +
            "        more = true;\n" +
            "      } else {\n" +
            "        func.apply(context, args);\n" +
            "      }\n" +
            "      whenDone();\n" +
            "      throttling = true;\n" +
            "    };\n" +
            "  };\n" +
            "\n" +
            "  // Returns a function, that, as long as it continues to be invoked, will not\n" +
            "  // be triggered. The function will be called after it stops being called for\n" +
            "  // N milliseconds.\n" +
            "  _.debounce = function(func, wait) {\n" +
            "    var timeout;\n" +
            "    return function() {\n" +
            "      var context = this, args = arguments;\n" +
            "      var later = function() {\n" +
            "        timeout = null;\n" +
            "        func.apply(context, args);\n" +
            "      };\n" +
            "      clearTimeout(timeout);\n" +
            "      timeout = setTimeout(later, wait);\n" +
            "    };\n" +
            "  };\n" +
            "\n" +
            "  // Returns a function that will be executed at most one time, no matter how\n" +
            "  // often you call it. Useful for lazy initialization.\n" +
            "  _.once = function(func) {\n" +
            "    var ran = false, memo;\n" +
            "    return function() {\n" +
            "      if (ran) return memo;\n" +
            "      ran = true;\n" +
            "      return memo = func.apply(this, arguments);\n" +
            "    };\n" +
            "  };\n" +
            "\n" +
            "  // Returns the first function passed as an argument to the second,\n" +
            "  // allowing you to adjust arguments, run code before and after, and\n" +
            "  // conditionally execute the original function.\n" +
            "  _.wrap = function(func, wrapper) {\n" +
            "    return function() {\n" +
            "      var args = [func].concat(slice.call(arguments));\n" +
            "      return wrapper.apply(this, args);\n" +
            "    };\n" +
            "  };\n" +
            "\n" +
            "  // Returns a function that is the composition of a list of functions, each\n" +
            "  // consuming the return value of the function that follows.\n" +
            "  _.compose = function() {\n" +
            "    var funcs = slice.call(arguments);\n" +
            "    return function() {\n" +
            "      var args = slice.call(arguments);\n" +
            "      for (var i = funcs.length - 1; i >= 0; i--) {\n" +
            "        args = [funcs[i].apply(this, args)];\n" +
            "      }\n" +
            "      return args[0];\n" +
            "    };\n" +
            "  };\n" +
            "\n" +
            "  // Returns a function that will only be executed after being called N times.\n" +
            "  _.after = function(times, func) {\n" +
            "    return function() {\n" +
            "      if (--times < 1) { return func.apply(this, arguments); }\n" +
            "    };\n" +
            "  };\n" +
            "\n" +
            "  // Object Functions\n" +
            "  // ----------------\n" +
            "\n" +
            "  // Retrieve the names of an object's properties.\n" +
            "  // Delegates to **ECMAScript 5**'s native `Object.keys`\n" +
            "  _.keys = nativeKeys || function(obj) {\n" +
            "    if (obj !== Object(obj)) throw new TypeError('Invalid object');\n" +
            "    var keys = [];\n" +
            "    for (var key in obj) if (hasOwnProperty.call(obj, key)) keys[keys.length] = key;\n" +
            "    return keys;\n" +
            "  };\n" +
            "\n" +
            "  // Retrieve the values of an object's properties.\n" +
            "  _.values = function(obj) {\n" +
            "    return _.map(obj, _.identity);\n" +
            "  };\n" +
            "\n" +
            "  // Return a sorted list of the function names available on the object.\n" +
            "  // Aliased as `methods`\n" +
            "  _.functions = _.methods = function(obj) {\n" +
            "    var names = [];\n" +
            "    for (var key in obj) {\n" +
            "      if (_.isFunction(obj[key])) names.push(key);\n" +
            "    }\n" +
            "    return names.sort();\n" +
            "  };\n" +
            "\n" +
            "  // Extend a given object with all the properties in passed-in object(s).\n" +
            "  _.extend = function(obj) {\n" +
            "    each(slice.call(arguments, 1), function(source) {\n" +
            "      for (var prop in source) {\n" +
            "        if (source[prop] !== void 0) obj[prop] = source[prop];\n" +
            "      }\n" +
            "    });\n" +
            "    return obj;\n" +
            "  };\n" +
            "\n" +
            "  // Fill in a given object with default properties.\n" +
            "  _.defaults = function(obj) {\n" +
            "    each(slice.call(arguments, 1), function(source) {\n" +
            "      for (var prop in source) {\n" +
            "        if (obj[prop] == null) obj[prop] = source[prop];\n" +
            "      }\n" +
            "    });\n" +
            "    return obj;\n" +
            "  };\n" +
            "\n" +
            "  // Create a (shallow-cloned) duplicate of an object.\n" +
            "  _.clone = function(obj) {\n" +
            "    if (!_.isObject(obj)) return obj;\n" +
            "    return _.isArray(obj) ? obj.slice() : _.extend({}, obj);\n" +
            "  };\n" +
            "\n" +
            "  // Invokes interceptor with the obj, and then returns obj.\n" +
            "  // The primary purpose of this method is to \"tap into\" a method chain, in\n" +
            "  // order to perform operations on intermediate results within the chain.\n" +
            "  _.tap = function(obj, interceptor) {\n" +
            "    interceptor(obj);\n" +
            "    return obj;\n" +
            "  };\n" +
            "\n" +
            "  // Internal recursive comparison function.\n" +
            "  function eq(a, b, stack) {\n" +
            "    // Identical objects are equal. `0 === -0`, but they aren't identical.\n" +
            "    // See the Harmony `egal` proposal: http://wiki.ecmascript.org/doku.php?id=harmony:egal.\n" +
            "    if (a === b) return a !== 0 || 1 / a == 1 / b;\n" +
            "    // A strict comparison is necessary because `null == undefined`.\n" +
            "    if ((a == null) || (b == null)) return a === b;\n" +
            "    // Unwrap any wrapped objects.\n" +
            "    if (a._chain) a = a._wrapped;\n" +
            "    if (b._chain) b = b._wrapped;\n" +
            "    // Invoke a custom `isEqual` method if one is provided.\n" +
            "    if (_.isFunction(a.isEqual)) return a.isEqual(b);\n" +
            "    if (_.isFunction(b.isEqual)) return b.isEqual(a);\n" +
            "    // Compare object types.\n" +
            "    var typeA = typeof a;\n" +
            "    if (typeA != typeof b) return false;\n" +
            "    // Optimization; ensure that both values are truthy or falsy.\n" +
            "    if (!a != !b) return false;\n" +
            "    // `NaN` values are equal.\n" +
            "    if (_.isNaN(a)) return _.isNaN(b);\n" +
            "    // Compare string objects by value.\n" +
            "    var isStringA = _.isString(a), isStringB = _.isString(b);\n" +
            "    if (isStringA || isStringB) return isStringA && isStringB && String(a) == String(b);\n" +
            "    // Compare number objects by value.\n" +
            "    var isNumberA = _.isNumber(a), isNumberB = _.isNumber(b);\n" +
            "    if (isNumberA || isNumberB) return isNumberA && isNumberB && +a == +b;\n" +
            "    // Compare boolean objects by value. The value of `true` is 1; the value of `false` is 0.\n" +
            "    var isBooleanA = _.isBoolean(a), isBooleanB = _.isBoolean(b);\n" +
            "    if (isBooleanA || isBooleanB) return isBooleanA && isBooleanB && +a == +b;\n" +
            "    // Compare dates by their millisecond values.\n" +
            "    var isDateA = _.isDate(a), isDateB = _.isDate(b);\n" +
            "    if (isDateA || isDateB) return isDateA && isDateB && a.getTime() == b.getTime();\n" +
            "    // Compare RegExps by their source patterns and flags.\n" +
            "    var isRegExpA = _.isRegExp(a), isRegExpB = _.isRegExp(b);\n" +
            "    if (isRegExpA || isRegExpB) {\n" +
            "      // Ensure commutative equality for RegExps.\n" +
            "      return isRegExpA && isRegExpB &&\n" +
            "             a.source == b.source &&\n" +
            "             a.global == b.global &&\n" +
            "             a.multiline == b.multiline &&\n" +
            "             a.ignoreCase == b.ignoreCase;\n" +
            "    }\n" +
            "    // Ensure that both values are objects.\n" +
            "    if (typeA != 'object') return false;\n" +
            "    // Arrays or Arraylikes with different lengths are not equal.\n" +
            "    if (a.length !== b.length) return false;\n" +
            "    // Objects with different constructors are not equal.\n" +
            "    if (a.constructor !== b.constructor) return false;\n" +
            "    // Assume equality for cyclic structures. The algorithm for detecting cyclic\n" +
            "    // structures is adapted from ES 5.1 section 15.12.3, abstract operation `JO`.\n" +
            "    var length = stack.length;\n" +
            "    while (length--) {\n" +
            "      // Linear search. Performance is inversely proportional to the number of\n" +
            "      // unique nested structures.\n" +
            "      if (stack[length] == a) return true;\n" +
            "    }\n" +
            "    // Add the first object to the stack of traversed objects.\n" +
            "    stack.push(a);\n" +
            "    var size = 0, result = true;\n" +
            "    // Deep compare objects.\n" +
            "    for (var key in a) {\n" +
            "      if (hasOwnProperty.call(a, key)) {\n" +
            "        // Count the expected number of properties.\n" +
            "        size++;\n" +
            "        // Deep compare each member.\n" +
            "        if (!(result = hasOwnProperty.call(b, key) && eq(a[key], b[key], stack))) break;\n" +
            "      }\n" +
            "    }\n" +
            "    // Ensure that both objects contain the same number of properties.\n" +
            "    if (result) {\n" +
            "      for (key in b) {\n" +
            "        if (hasOwnProperty.call(b, key) && !(size--)) break;\n" +
            "      }\n" +
            "      result = !size;\n" +
            "    }\n" +
            "    // Remove the first object from the stack of traversed objects.\n" +
            "    stack.pop();\n" +
            "    return result;\n" +
            "  }\n" +
            "\n" +
            "  // Perform a deep comparison to check if two objects are equal.\n" +
            "  _.isEqual = function(a, b) {\n" +
            "    return eq(a, b, []);\n" +
            "  };\n" +
            "\n" +
            "  // Is a given array, string, or object empty?\n" +
            "  // An \"empty\" object has no enumerable own-properties.\n" +
            "  _.isEmpty = function(obj) {\n" +
            "    if (_.isArray(obj) || _.isString(obj)) return obj.length === 0;\n" +
            "    for (var key in obj) if (hasOwnProperty.call(obj, key)) return false;\n" +
            "    return true;\n" +
            "  };\n" +
            "\n" +
            "  // Is a given value a DOM element?\n" +
            "  _.isElement = function(obj) {\n" +
            "    return !!(obj && obj.nodeType == 1);\n" +
            "  };\n" +
            "\n" +
            "  // Is a given value an array?\n" +
            "  // Delegates to ECMA5's native Array.isArray\n" +
            "  _.isArray = nativeIsArray || function(obj) {\n" +
            "    return toString.call(obj) == '[object Array]';\n" +
            "  };\n" +
            "\n" +
            "  // Is a given variable an object?\n" +
            "  _.isObject = function(obj) {\n" +
            "    return obj === Object(obj);\n" +
            "  };\n" +
            "\n" +
            "  // Is a given variable an arguments object?\n" +
            "  if (toString.call(arguments) == '[object Arguments]') {\n" +
            "    _.isArguments = function(obj) {\n" +
            "      return toString.call(obj) == '[object Arguments]';\n" +
            "    };\n" +
            "  } else {\n" +
            "    _.isArguments = function(obj) {\n" +
            "      return !!(obj && hasOwnProperty.call(obj, 'callee'));\n" +
            "    };\n" +
            "  }\n" +
            "\n" +
            "  // Is a given value a function?\n" +
            "  _.isFunction = function(obj) {\n" +
            "    return toString.call(obj) == '[object Function]';\n" +
            "  };\n" +
            "\n" +
            "  // Is a given value a string?\n" +
            "  _.isString = function(obj) {\n" +
            "    return toString.call(obj) == '[object String]';\n" +
            "  };\n" +
            "\n" +
            "  // Is a given value a number?\n" +
            "  _.isNumber = function(obj) {\n" +
            "    return toString.call(obj) == '[object Number]';\n" +
            "  };\n" +
            "\n" +
            "  // Is the given value `NaN`?\n" +
            "  _.isNaN = function(obj) {\n" +
            "    // `NaN` is the only value for which `===` is not reflexive.\n" +
            "    return obj !== obj;\n" +
            "  };\n" +
            "\n" +
            "  // Is a given value a boolean?\n" +
            "  _.isBoolean = function(obj) {\n" +
            "    return obj === true || obj === false || toString.call(obj) == '[object Boolean]';\n" +
            "  };\n" +
            "\n" +
            "  // Is a given value a date?\n" +
            "  _.isDate = function(obj) {\n" +
            "    return toString.call(obj) == '[object Date]';\n" +
            "  };\n" +
            "\n" +
            "  // Is the given value a regular expression?\n" +
            "  _.isRegExp = function(obj) {\n" +
            "    return toString.call(obj) == '[object RegExp]';\n" +
            "  };\n" +
            "\n" +
            "  // Is a given value equal to null?\n" +
            "  _.isNull = function(obj) {\n" +
            "    return obj === null;\n" +
            "  };\n" +
            "\n" +
            "  // Is a given variable undefined?\n" +
            "  _.isUndefined = function(obj) {\n" +
            "    return obj === void 0;\n" +
            "  };\n" +
            "\n" +
            "  // Utility Functions\n" +
            "  // -----------------\n" +
            "\n" +
            "  // Run Underscore.js in *noConflict* mode, returning the `_` variable to its\n" +
            "  // previous owner. Returns a reference to the Underscore object.\n" +
            "  _.noConflict = function() {\n" +
            "    root._ = previousUnderscore;\n" +
            "    return this;\n" +
            "  };\n" +
            "\n" +
            "  // Keep the identity function around for default iterators.\n" +
            "  _.identity = function(value) {\n" +
            "    return value;\n" +
            "  };\n" +
            "\n" +
            "  // Run a function **n** times.\n" +
            "  _.times = function (n, iterator, context) {\n" +
            "    for (var i = 0; i < n; i++) iterator.call(context, i);\n" +
            "  };\n" +
            "\n" +
            "  // Escape a string for HTML interpolation.\n" +
            "  _.escape = function(string) {\n" +
            "    return (''+string).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/\"/g, '&quot;').replace(/'/g, '&#x27;').replace(/\\//g,'&#x2F;');\n" +
            "  };\n" +
            "\n" +
            "  // Add your own custom functions to the Underscore object, ensuring that\n" +
            "  // they're correctly added to the OOP wrapper as well.\n" +
            "  _.mixin = function(obj) {\n" +
            "    each(_.functions(obj), function(name){\n" +
            "      addToWrapper(name, _[name] = obj[name]);\n" +
            "    });\n" +
            "  };\n" +
            "\n" +
            "  // Generate a unique integer id (unique within the entire client session).\n" +
            "  // Useful for temporary DOM ids.\n" +
            "  var idCounter = 0;\n" +
            "  _.uniqueId = function(prefix) {\n" +
            "    var id = idCounter++;\n" +
            "    return prefix ? prefix + id : id;\n" +
            "  };\n" +
            "\n" +
            "  // By default, Underscore uses ERB-style template delimiters, change the\n" +
            "  // following template settings to use alternative delimiters.\n" +
            "  _.templateSettings = {\n" +
            "    evaluate    : /<%([\\s\\S]+?)%>/g,\n" +
            "    interpolate : /<%=([\\s\\S]+?)%>/g,\n" +
            "    escape      : /<%-([\\s\\S]+?)%>/g\n" +
            "  };\n" +
            "\n" +
            "  // JavaScript micro-templating, similar to John Resig's implementation.\n" +
            "  // Underscore templating handles arbitrary delimiters, preserves whitespace,\n" +
            "  // and correctly escapes quotes within interpolated code.\n" +
            "  _.template = function(str, data) {\n" +
            "    var c  = _.templateSettings;\n" +
            "    var tmpl = 'var __p=[],print=function(){__p.push.apply(__p,arguments);};' +\n" +
            "      'with(obj||{}){__p.push(\\'' +\n" +
            "      str.replace(/\\\\/g, '\\\\\\\\')\n" +
            "         .replace(/'/g, \"\\\\'\")\n" +
            "         .replace(c.escape, function(match, code) {\n" +
            "           return \"',_.escape(\" + code.replace(/\\\\'/g, \"'\") + \"),'\";\n" +
            "         })\n" +
            "         .replace(c.interpolate, function(match, code) {\n" +
            "           return \"',\" + code.replace(/\\\\'/g, \"'\") + \",'\";\n" +
            "         })\n" +
            "         .replace(c.evaluate || null, function(match, code) {\n" +
            "           return \"');\" + code.replace(/\\\\'/g, \"'\")\n" +
            "                              .replace(/[\\r\\n\\t]/g, ' ') + \"__p.push('\";\n" +
            "         })\n" +
            "         .replace(/\\r/g, '\\\\r')\n" +
            "         .replace(/\\n/g, '\\\\n')\n" +
            "         .replace(/\\t/g, '\\\\t')\n" +
            "         + \"');}return __p.join('');\";\n" +
            "    var func = new Function('obj', '_', tmpl);\n" +
            "    return data ? func(data, _) : function(data) { return func(data, _) };\n" +
            "  };\n" +
            "\n" +
            "  // The OOP Wrapper\n" +
            "  // ---------------\n" +
            "\n" +
            "  // If Underscore is called as a function, it returns a wrapped object that\n" +
            "  // can be used OO-style. This wrapper holds altered versions of all the\n" +
            "  // underscore functions. Wrapped objects may be chained.\n" +
            "  var wrapper = function(obj) { this._wrapped = obj; };\n" +
            "\n" +
            "  // Expose `wrapper.prototype` as `_.prototype`\n" +
            "  _.prototype = wrapper.prototype;\n" +
            "\n" +
            "  // Helper function to continue chaining intermediate results.\n" +
            "  var result = function(obj, chain) {\n" +
            "    return chain ? _(obj).chain() : obj;\n" +
            "  };\n" +
            "\n" +
            "  // A method to easily add functions to the OOP wrapper.\n" +
            "  var addToWrapper = function(name, func) {\n" +
            "    wrapper.prototype[name] = function() {\n" +
            "      var args = slice.call(arguments);\n" +
            "      unshift.call(args, this._wrapped);\n" +
            "      return result(func.apply(_, args), this._chain);\n" +
            "    };\n" +
            "  };\n" +
            "\n" +
            "  // Add all of the Underscore functions to the wrapper object.\n" +
            "  _.mixin(_);\n" +
            "\n" +
            "  // Add all mutator Array functions to the wrapper.\n" +
            "  each(['pop', 'push', 'reverse', 'shift', 'sort', 'splice', 'unshift'], function(name) {\n" +
            "    var method = ArrayProto[name];\n" +
            "    wrapper.prototype[name] = function() {\n" +
            "      method.apply(this._wrapped, arguments);\n" +
            "      return result(this._wrapped, this._chain);\n" +
            "    };\n" +
            "  });\n" +
            "\n" +
            "  // Add all accessor Array functions to the wrapper.\n" +
            "  each(['concat', 'join', 'slice'], function(name) {\n" +
            "    var method = ArrayProto[name];\n" +
            "    wrapper.prototype[name] = function() {\n" +
            "      return result(method.apply(this._wrapped, arguments), this._chain);\n" +
            "    };\n" +
            "  });\n" +
            "\n" +
            "  // Start chaining a wrapped Underscore object.\n" +
            "  wrapper.prototype.chain = function() {\n" +
            "    this._chain = true;\n" +
            "    return this;\n" +
            "  };\n" +
            "\n" +
            "  // Extracts the result from a wrapped and chained object.\n" +
            "  wrapper.prototype.value = function() {\n" +
            "    return this._wrapped;\n" +
            "  };\n" +
            "\n" +
            "}).call(this);";
  }
}
