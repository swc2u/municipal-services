"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _defineProperty2 = require("babel-runtime/helpers/defineProperty");

var _defineProperty3 = _interopRequireDefault(_defineProperty2);

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

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function () {
  var api = (0, _express.Router)();
  api.post("/_calculateBenefit", asyncHandler(function () {
    var _ref = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref2, res, next) {
      var body = _ref2.body;

      var _pensionCalculationDe;

      var mdms, pensionBenefits, rules, employee, employeeType, benefits, notifications, notificationText, i, pensionCalculationDetails, employeeBenefits, response;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              _context.next = 2;
              return (0, _mdmsData2.default)(body.RequestInfo, body.ProcessInstances[0].tenantId);

            case 2:
              mdms = _context.sent;
              pensionBenefits = (0, _get2.default)(mdms, "MdmsRes.pension.benefits");
              rules = {
                benefits: pensionBenefits
              };
              employee = body.ProcessInstances[0].employee;
              //employee type should be as per rules

              employeeType = (0, _calculationManager.getEmployeeType)(employee, mdms);

              employee.employeeType = employeeType;

              benefits = (0, _calculationManager.calculateBenefit)(rules, employee, mdms);
              notifications = (0, _calculationManager.getNotifications)(employee, mdms);
              notificationText = "";

              for (i = 0; i < notifications.length; i++) {
                notificationText = "" + notificationText + notifications[i].notificationText + " ";
              }

              pensionCalculationDetails = (_pensionCalculationDe = {
                nqsYearSystem: (0, _calculationHelper.getNQSYear)(employee),
                nqsMonthSystem: (0, _calculationHelper.getNQSMonth)(employee),
                nqsDaySystem: (0, _calculationHelper.getNQSDay)(employee),
                basicPensionSystem: 0,
                pensionDeductionsSystem: 0,
                additionalPensionSystem: 0,
                commutedPensionSystem: 0,
                commutedValueSystem: 0,
                familyPensionISystem: 0,
                familyPensionIISystem: 0,
                dcrgSystem: 0,
                netDeductionsSystem: 0,
                totalPensionSystem: 0,
                finalCalculatedPensionSystem: 0,
                interimReliefSystem: 0,
                interimReliefLpdSystem: 0,
                daSystem: 0,
                daLpdSystem: 0,
                duesDeductionsSystem: 0,
                compassionatePensionSystem: 0,
                compensationPensionSystem: 0,
                terminalBenefitSystem: 0,
                finalCalculatedGratuitySystem: 0,
                familyPensionIStartDateSystem: null,
                familyPensionIEndDateSystem: null,
                familyPensionIIStartDateSystem: null,
                exGratiaSystem: 0,
                pensionerFamilyPensionSystem: 0,
                additionalPensionerFamilyPensionSystem: 0
              }, (0, _defineProperty3.default)(_pensionCalculationDe, "totalPensionSystem", 0), (0, _defineProperty3.default)(_pensionCalculationDe, "provisionalPensionSystem", 0), (0, _defineProperty3.default)(_pensionCalculationDe, "invalidPensionSystem", 0), (0, _defineProperty3.default)(_pensionCalculationDe, "woundExtraordinaryPensionSystem", 0), (0, _defineProperty3.default)(_pensionCalculationDe, "attendantAllowanceSystem", 0), (0, _defineProperty3.default)(_pensionCalculationDe, "gqsYearSystem", (0, _calculationHelper.getGQSYear)(employee)), (0, _defineProperty3.default)(_pensionCalculationDe, "gqsMonthSystem", (0, _calculationHelper.getGQSMonth)(employee)), (0, _defineProperty3.default)(_pensionCalculationDe, "gqsDaySystem", (0, _calculationHelper.getGQSDay)(employee)), (0, _defineProperty3.default)(_pensionCalculationDe, "notificationTextSystem", notificationText), _pensionCalculationDe);
              employeeBenefits = [];

              employeeBenefits.push({
                benefits: benefits,
                pensionCalculationDetails: pensionCalculationDetails //,
                //notifications: notifications

              });

              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                EmployeeBenefits: employeeBenefits
              };

              res.json(response);

            case 17:
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
//# sourceMappingURL=calculateBenefit.js.map