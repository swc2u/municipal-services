"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _express = require("express");

var _producer = require("../kafka/producer");

var _producer2 = _interopRequireDefault(_producer);

var _utils = require("../utils");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _mdmsData = require("../utils/mdmsData");

var _mdmsData2 = _interopRequireDefault(_mdmsData);

var _create = require("../utils/create");

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _set = require("lodash/set");

var _set2 = _interopRequireDefault(_set);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _orderBy = require("lodash/orderBy");

var _orderBy2 = _interopRequireDefault(_orderBy);

var _search = require("../utils/search");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_checkDependentEligibilityForGratuity", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var i, eligibilityRespone, dependents, processInstances, response;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:

              for (i = 0; i < body.ProcessInstances[0].dependents.length; i++) {
                body.ProcessInstances[0].dependents[i].dob = (0, _utils.epochToYmd)((0, _search.intConversion)(body.ProcessInstances[0].dependents[i].dob));
                body.ProcessInstances[0].dependents[i].isDisabled = body.ProcessInstances[0].dependents[i].isDisabled ? body.ProcessInstances[0].dependents[i].isDisabled : false;
                body.ProcessInstances[0].dependents[i].maritalStatus = body.ProcessInstances[0].dependents[i].maritalStatus ? body.ProcessInstances[0].dependents[i].maritalStatus : false;
                body.ProcessInstances[0].dependents[i].isHollyDependent = body.ProcessInstances[0].dependents[i].isHollyDependent ? body.ProcessInstances[0].dependents[i].isHollyDependent : false;
                body.ProcessInstances[0].dependents[i].noSpouseNoChildren = body.ProcessInstances[0].dependents[i].noSpouseNoChildren ? body.ProcessInstances[0].dependents[i].noSpouseNoChildren : false;
                body.ProcessInstances[0].dependents[i].isGrandChildFromDeceasedSon = body.ProcessInstances[0].dependents[i].isGrandChildFromDeceasedSon ? body.ProcessInstances[0].dependents[i].isGrandChildFromDeceasedSon : false;
                body.ProcessInstances[0].dependents[i].isEligibleForGratuity = false;
                body.ProcessInstances[0].dependents[i].isEligibleForPension = false;
              }

              _context.next = 3;
              return (0, _utils.getDependentEligibilityForGratuity)(body);

            case 3:
              eligibilityRespone = _context.sent;


              console.log("eligibilityRespone", JSON.stringify(eligibilityRespone));
              dependents = eligibilityRespone.Dependents;

              for (i = 0; i < dependents.length; i++) {
                dependents[i].dob = (0, _utils.convertDateToEpoch)(dependents[i].dob, "dob");
              }

              processInstances = [];

              processInstances.push({
                dependents: dependents
              });

              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                ProcessInstances: processInstances
              };


              res.json(response);

            case 11:
            case "end":
              return _context.stop();
          }
        }
      }, _callee, undefined);
    }));

    return function (_x, _x2, _x3) {
      return _ref2.apply(this, arguments);
    };
  }()));
  return api;
};
//# sourceMappingURL=checkDependentEligibilityForGratuity.js.map