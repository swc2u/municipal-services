"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _express = require("express");

var _calculationManager = require("../utils/calculationManager");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _utils = require("../utils");

var _mdmsData = require("../utils/mdmsData");

var _mdmsData2 = _interopRequireDefault(_mdmsData);

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _set = require("lodash/set");

var _set2 = _interopRequireDefault(_set);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _filter = require("lodash/filter");

var _filter2 = _interopRequireDefault(_filter);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function () {
  var api = (0, _express.Router)();
  api.post("/_getDependentEligibilityForBenefit", asyncHandler(function () {
    var _ref = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref2, res, next) {
      var body = _ref2.body;

      var mdms, dependents, i, dependent, eligibility, _eligibility, _dependent, response;

      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              _context.next = 2;
              return (0, _mdmsData2.default)(body.RequestInfo, body.ProcessInstances[0].tenantId);

            case 2:
              mdms = _context.sent;
              dependents = body.ProcessInstances[0].dependents;


              for (i = 0; i < dependents.length; i++) {
                dependent = dependents[i];
                eligibility = null;

                eligibility = (0, _calculationManager.getDependentEligibilityForGratuity)(dependent, mdms);
                dependents[i].isEligibleForGratuity = eligibility == "TRUE" ? true : false;
              }

              i = 0;

            case 6:
              if (!(i < dependents.length)) {
                _context.next = 17;
                break;
              }

              _eligibility = null;

              if (!dependents[i].isEligibleForGratuity) {
                _context.next = 14;
                break;
              }

              _dependent = dependents[i];

              _eligibility = (0, _calculationManager.getDependentEligibilityForPension)(_dependent, mdms, dependents);

              if (!(_eligibility == "TRUE")) {
                _context.next = 14;
                break;
              }

              dependents[i].isEligibleForPension = true;
              return _context.abrupt("break", 17);

            case 14:
              i++;
              _context.next = 6;
              break;

            case 17:
              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                Dependents: dependents
              };

              res.json(response);

            case 19:
            case "end":
              return _context.stop();
          }
        }
      }, _callee, undefined);
    }));

    return function (_x, _x2, _x3) {
      return _ref.apply(this, arguments);
    };
  }()));
  return api;
};
//# sourceMappingURL=getDependentEligibilityForBenefit.js.map