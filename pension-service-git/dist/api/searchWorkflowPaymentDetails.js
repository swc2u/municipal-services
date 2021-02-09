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

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _set = require("lodash/set");

var _set2 = _interopRequireDefault(_set);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _filter = require("lodash/filter");

var _filter2 = _interopRequireDefault(_filter);

var _orderBy = require("lodash/orderBy");

var _orderBy2 = _interopRequireDefault(_orderBy);

var _search = require("../utils/search");

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_searchWorkflowPaymentDetails", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(request, res, next) {
      var queryObj, workflowSearchResponse, processInstances, businessService, businessId, paymentOrderNumber, date, day, month, formatted_date, bankAccountNumber, bankDetails, bankIfsc, dependents, dependentEligibleForPension, paymentDetails, benefits, pensionCalculationDetails, pensionCalculationUpdateDetails, response;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              //getting mdms data

              queryObj = JSON.parse(JSON.stringify(request.query));
              _context.next = 3;
              return (0, _utils.searchPensionWorkflow)(request.body.RequestInfo, queryObj.tenantId, queryObj.businessIds);

            case 3:
              workflowSearchResponse = _context.sent;


              _logger2.default.debug("workflowSearchResponse", JSON.stringify(workflowSearchResponse));

              processInstances = workflowSearchResponse.ProcessInstances;
              businessService = processInstances[0].businessService;
              businessId = processInstances[0].businessId;
              paymentOrderNumber = String(businessId).substring(0, 6);

              paymentOrderNumber = paymentOrderNumber + String(businessId).substring(String(businessId).length - 6, String(businessId).length);
              date = new Date();
              day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
              month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
              formatted_date = day + "/" + month + "/" + date.getFullYear();
              bankAccountNumber = void 0;
              bankDetails = void 0;
              bankIfsc = void 0;
              dependents = processInstances[0].dependents;
              dependentEligibleForPension = [];
              _context.t0 = businessService;
              _context.next = _context.t0 === _envVariables2.default.EGOV_PENSION_RRP_BUSINESS_SERVICE ? 22 : _context.t0 === _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE ? 26 : _context.t0 === _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE ? 31 : 36;
              break;

            case 22:
              bankAccountNumber = processInstances[0].employeeOtherDetails.accountNumber;
              bankDetails = processInstances[0].employeeOtherDetails.bankName;
              bankIfsc = processInstances[0].employeeOtherDetails.bankIfsc;
              return _context.abrupt("break", 36);

            case 26:
              dependentEligibleForPension = (0, _filter2.default)(dependents, function (x) {
                return x.isEligibleForPension == true;
              });
              bankAccountNumber = dependentEligibleForPension.length > 0 ? dependentEligibleForPension[0].bankAccountNumber : null;
              bankDetails = dependentEligibleForPension.length > 0 ? dependentEligibleForPension[0].bankName : null;
              bankIfsc = dependentEligibleForPension.length > 0 ? dependentEligibleForPension[0].bankIfsc : null;
              return _context.abrupt("break", 36);

            case 31:
              dependentEligibleForPension = (0, _filter2.default)(dependents, function (x) {
                return x.isEligibleForPension == true;
              });
              bankAccountNumber = dependentEligibleForPension.length > 0 ? dependentEligibleForPension[0].bankAccountNumber : null;
              bankDetails = dependentEligibleForPension.length > 0 ? dependentEligibleForPension[0].bankName : null;
              bankIfsc = dependentEligibleForPension.length > 0 ? dependentEligibleForPension[0].bankIfsc : null;
              return _context.abrupt("break", 36);

            case 36:
              paymentDetails = {
                businessId: processInstances[0].businessId,
                date: formatted_date,
                paymentOrderNumber: paymentOrderNumber,
                name: processInstances[0].employee.user.name,
                dob: (0, _utils.epochToDmy)((0, _search.intConversion)(processInstances[0].employee.user.dob)),
                designation: processInstances[0].employee.assignments[0].designation,
                department: processInstances[0].employee.assignments[0].department,
                dateOfRetirement: (0, _utils.epochToDmy)((0, _search.intConversion)(processInstances[0].employee.dateOfRetirement)),
                dateOfDeath: (0, _utils.epochToDmy)((0, _search.intConversion)(processInstances[0].employee.dateOfDeath)),
                permanentAddress: processInstances[0].employee.user.permanentAddress,
                permanentCity: processInstances[0].employee.user.permanentCity,
                permanentPinCode: processInstances[0].employee.user.permanentPinCode,
                fatherOrHusbandName: processInstances[0].employee.user.fatherOrHusbandName,
                dateOfAppointment: (0, _utils.epochToDmy)((0, _search.intConversion)(processInstances[0].employee.dateOfAppointment)),
                reasonForRetirement: processInstances[0].employeeOtherDetails.reasonForRetirement,
                lpd: processInstances[0].employeeOtherDetails.lpd,
                totalNoPayLeavesYears: processInstances[0].employeeOtherDetails.totalNoPayLeavesYears,
                totalNoPayLeavesMonths: processInstances[0].employeeOtherDetails.totalNoPayLeavesMonths,
                totalNoPayLeavesDays: processInstances[0].employeeOtherDetails.totalNoPayLeavesDays,
                accountNumber: bankAccountNumber,
                bankAddress: bankDetails,
                bankIfsc: bankIfsc,
                gqsYearVerified: processInstances[0].pensionCalculationUpdateDetails.gqsYearVerified == null ? 0 : processInstances[0].pensionCalculationUpdateDetails.gqsYearVerified,
                gqsMonthVerified: processInstances[0].pensionCalculationUpdateDetails.gqsMonthVerified == null ? 0 : processInstances[0].pensionCalculationUpdateDetails.gqsMonthVerified,
                gqsDayVerified: processInstances[0].pensionCalculationUpdateDetails.gqsDayVerified == null ? 0 : processInstances[0].pensionCalculationUpdateDetails.gqsDayVerified,
                nqsYearVerified: processInstances[0].pensionCalculationUpdateDetails.nqsYearVerified == null ? 0 : processInstances[0].pensionCalculationUpdateDetails.nqsYearVerified,
                nqsMonthVerified: processInstances[0].pensionCalculationUpdateDetails.nqsMonthVerified == null ? 0 : processInstances[0].pensionCalculationUpdateDetails.nqsMonthVerified,
                nqsDayVerified: processInstances[0].pensionCalculationUpdateDetails.nqsDayVerified == null ? 0 : processInstances[0].pensionCalculationUpdateDetails.nqsDayVerified,
                pensionEligibleDependentName: dependentEligibleForPension.length > 0 ? dependentEligibleForPension[0].name : null,
                benefits: []
              };
              benefits = [];
              pensionCalculationDetails = processInstances[0].pensionCalculationDetails;
              pensionCalculationUpdateDetails = processInstances[0].pensionCalculationUpdateDetails;

              if (pensionCalculationDetails.interimReliefApplicable) {
                benefits.push({
                  "code": "IR",
                  "name": "IR",
                  "value": pensionCalculationDetails.interimReliefLpdSystem,
                  "expression": ""
                });
              }

              if (pensionCalculationDetails.basicPensionApplicable) {
                benefits.push({
                  "code": "BASIC_PENSION",
                  "name": "Basic Pension",
                  "value": pensionCalculationUpdateDetails.basicPensionVerified,
                  "expression": pensionCalculationDetails.basicPensionExpression
                });
              }

              if (pensionCalculationDetails.commutedPensionApplicable) {
                benefits.push({
                  "code": "COMMUTED_PENSION",
                  "name": "Commuted Pension",
                  "value": pensionCalculationUpdateDetails.commutedPensionVerified,
                  "expression": pensionCalculationDetails.commutedPensionExpression
                });
              }

              if (pensionCalculationDetails.pensionDeductionsApplicable) {
                benefits.push({
                  "code": "PENSION_DEDUCTION",
                  "name": "Pension Deduction",
                  "value": pensionCalculationUpdateDetails.pensionDeductionsVerified,
                  "expression": pensionCalculationDetails.pensionDeductionsExpression
                });
              }

              if (pensionCalculationDetails.commutationValueApplicable) {
                benefits.push({
                  "code": "COMMUTATION_VALUE",
                  "name": "Commutation Value",
                  "value": pensionCalculationUpdateDetails.commutedValueVerified,
                  "expression": pensionCalculationDetails.commutationValueExpression
                });
              }

              if (pensionCalculationDetails.daApplicable) {
                benefits.push({
                  "code": "PENSION_DA",
                  "name": "DA",
                  "value": pensionCalculationUpdateDetails.daVerified,
                  "expression": pensionCalculationDetails.daExpression
                });
              }

              if (pensionCalculationDetails.dcrgApplicable) {
                benefits.push({
                  "code": "DCRG",
                  "name": "DCRG",
                  "value": pensionCalculationUpdateDetails.dcrgVerified,
                  "expression": pensionCalculationDetails.dcrgExpression
                });
              }

              if (pensionCalculationDetails.additionalPensionApplicable) {
                benefits.push({
                  "code": "ADDITIONAL_PENSION",
                  "name": "Additional Pension",
                  "value": pensionCalculationUpdateDetails.additionalPensionVerified,
                  "expression": pensionCalculationDetails.additionalPensionExpression
                });
              }

              if (pensionCalculationDetails.compassionatePensionApplicable) {
                benefits.push({
                  "code": "COMPASSIONATE_PENSION",
                  "name": "Compassionate Pension",
                  "value": pensionCalculationUpdateDetails.compassionatePensionVerified,
                  "expression": pensionCalculationDetails.compassionatePensionExpression
                });
              }

              if (pensionCalculationDetails.compensationPensionApplicable) {
                benefits.push({
                  "code": "COMPENSATION_PENSION",
                  "name": "Compensation Pension",
                  "value": pensionCalculationUpdateDetails.compensationPensionVerified,
                  "expression": pensionCalculationDetails.compensationPensionExpression
                });
              }

              if (pensionCalculationDetails.terminalBenefitApplicable) {
                benefits.push({
                  "code": "TERMINAL_BENEFIT",
                  "name": "Terminal Benefit",
                  "value": pensionCalculationUpdateDetails.terminalBenefitVerified,
                  "expression": pensionCalculationDetails.terminalBenefitExpression
                });
              }

              if (pensionCalculationDetails.duesDeductionsApplicable) {
                benefits.push({
                  "code": "DCRG_DUES_DEDUCTION",
                  "name": "DCRG Dues Deduction",
                  "value": pensionCalculationUpdateDetails.duesDeductionsVerified,
                  "expression": pensionCalculationDetails.duesDeductionsExpression
                });
              }

              if (pensionCalculationDetails.netDeductionsApplicable) {
                benefits.push({
                  "code": "NET_DEDUCTION",
                  "name": "Net Deduction",
                  "value": pensionCalculationUpdateDetails.netDeductionsVerified,
                  "expression": pensionCalculationDetails.netDeductionsExpression
                });
              }

              if (pensionCalculationDetails.totalPensionApplicable) {
                benefits.push({
                  "code": "TOTAL_PENSION",
                  "name": "Total Pension",
                  "value": pensionCalculationUpdateDetails.totalPensionVerified,
                  "expression": pensionCalculationDetails.totalPensionExpression
                });
              }

              if (pensionCalculationDetails.finalCalculatedPensionApplicable) {
                benefits.push({
                  "code": "FINAL_CALCULATED_PENSION",
                  "name": "Net Pension",
                  "value": pensionCalculationUpdateDetails.finalCalculatedPensionVerified,
                  "expression": pensionCalculationDetails.finalCalculatedPensionExpression
                });
              }

              if (pensionCalculationDetails.finalCalculatedGratuityApplicable) {
                benefits.push({
                  "code": "FINAL_CALCULATED_GRATUITY",
                  "name": "Net Gratuity",
                  "value": pensionCalculationUpdateDetails.finalCalculatedGratuityVerified,
                  "expression": pensionCalculationDetails.finalCalculatedGratuityExpression
                });
              }

              if (pensionCalculationDetails.familyPensionIApplicable) {
                benefits.push({
                  "code": "FAMILY_PENSION_1",
                  "name": "Family Pension I",
                  "value": pensionCalculationUpdateDetails.familyPensionIVerified,
                  "expression": pensionCalculationDetails.familyPensionIExpression
                });
              }

              if (pensionCalculationDetails.familyPensionIIApplicable) {
                benefits.push({
                  "code": "FAMILY_PENSION_2",
                  "name": "Family Pension II",
                  "value": pensionCalculationUpdateDetails.familyPensionIIVerified,
                  "expression": pensionCalculationDetails.familyPensionIIExpression
                });
              }

              if (pensionCalculationDetails.pensionerFamilyPensionApplicable) {
                benefits.push({
                  "code": "PENSIONER_FAMILY_PENSION",
                  "name": "Pensioner Family Pension",
                  "value": pensionCalculationUpdateDetails.pensionerFamilyPensionVerified,
                  "expression": pensionCalculationDetails.pensionerFamilyPensionExpression
                });
              }

              if (pensionCalculationDetails.exGratiaApplicable) {
                benefits.push({
                  "code": "EX_GRATIA",
                  "name": "Ex Gratia",
                  "value": pensionCalculationUpdateDetails.exGratiaVerified,
                  "expression": pensionCalculationDetails.exGratiaExpression
                });
              }

              if (pensionCalculationDetails.provisionalPensionApplicable) {
                benefits.push({
                  "code": "PROVISIONAL_PENSION",
                  "name": "Provisional Pension",
                  "value": pensionCalculationUpdateDetails.provisionalPensionVerified,
                  "expression": pensionCalculationDetails.provisionalPensionExpression
                });
              }

              if (pensionCalculationDetails.invalidPensionApplicable) {
                benefits.push({
                  "code": "INVALID_PENSION",
                  "name": "Invalid Pension",
                  "value": pensionCalculationUpdateDetails.invalidPensionVerified,
                  "expression": pensionCalculationDetails.invalidPensionExpression
                });
              }

              if (pensionCalculationDetails.woundExtraordinaryPensionApplicable) {
                benefits.push({
                  "code": "WOUND_EXTRAORDINARY_PENSION",
                  "name": "Wound or Extraordinary Pension",
                  "value": pensionCalculationUpdateDetails.woundExtraordinaryPensionVerified,
                  "expression": pensionCalculationDetails.woundExtraordinaryPensionExpression
                });
              }

              if (pensionCalculationDetails.attendantAllowanceApplicable) {
                benefits.push({
                  "code": "ATTENDANT_ALLOWANCE",
                  "name": "Attendant Allowance",
                  "value": pensionCalculationUpdateDetails.attendantAllowanceVerified,
                  "expression": pensionCalculationDetails.attendantAllowanceExpression
                });
              }

              paymentDetails.benefits = benefits;

              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                PaymentDetails: paymentDetails
              };


              res.json(response);

            case 67:
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
//# sourceMappingURL=searchWorkflowPaymentDetails.js.map