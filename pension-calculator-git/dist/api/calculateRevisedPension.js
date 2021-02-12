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

var _calculationHelper = require("../utils/calculationHelper");

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
  api.post("/_calculateRevisedPension", asyncHandler(function () {
    var _ref = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref2, res, next) {
      var body = _ref2.body;
      var mdms, pensionBenefits, pensionRevision, rules, benefits, i, processInstances, response;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              _context.next = 2;
              return (0, _mdmsData2.default)(body.RequestInfo, body.ProcessInstances[0].tenantId);

            case 2:
              mdms = _context.sent;
              pensionBenefits = (0, _get2.default)(mdms, "MdmsRes.pension.pensionRevision");
              pensionRevision = body.ProcessInstances[0].pensionRevision;
              rules = {
                benefits: pensionBenefits
              };
              benefits = (0, _calculationManager.calculateRevisedPension)(pensionBenefits, pensionRevision[0]);
              i = 0;

            case 8:
              if (!(i < benefits.length)) {
                _context.next = 21;
                break;
              }

              _context.t0 = String(benefits[i].benefitCode).toUpperCase();
              _context.next = _context.t0 === "TOTAL_PENSION" ? 12 : _context.t0 === "NET_DEDUCTION" ? 14 : _context.t0 === "FINAL_CALCULATED_PENSION" ? 16 : 18;
              break;

            case 12:
              pensionRevision[0].totalPension = benefits[i].finalBenefitValue;
              return _context.abrupt("break", 18);

            case 14:
              pensionRevision[0].netDeductions = benefits[i].finalBenefitValue;
              return _context.abrupt("break", 18);

            case 16:
              pensionRevision[0].finalCalculatedPension = benefits[i].finalBenefitValue;
              return _context.abrupt("break", 18);

            case 18:
              i++;
              _context.next = 8;
              break;

            case 21:
              processInstances = [];

              processInstances.push({
                pensionRevision: pensionRevision
              });

              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                ProcessInstances: processInstances
              };

              res.json(response);

            case 25:
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
//# sourceMappingURL=calculateRevisedPension.js.map