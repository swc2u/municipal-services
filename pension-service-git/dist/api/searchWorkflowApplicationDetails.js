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

var _orderBy = require("lodash/orderBy");

var _orderBy2 = _interopRequireDefault(_orderBy);

var _search = require("../utils/search");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_searchWorkflowApplicationDetails", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(request, res, next) {
      var queryObj, workflowSearchResponse, processInstances, applicationDetails, response;
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


              console.log("workflowSearchResponse", JSON.stringify(workflowSearchResponse));

              processInstances = workflowSearchResponse.ProcessInstances;
              applicationDetails = (0, _defineProperty3.default)({
                businessId: processInstances[0].businessId,
                name: processInstances[0].employee.user.name,
                dob: (0, _utils.epochToDmy)((0, _search.intConversion)(processInstances[0].employee.user.dob)),
                dateOfRetirement: (0, _utils.epochToDmy)((0, _search.intConversion)(processInstances[0].employee.dateOfRetirement)),
                permanentAddress: processInstances[0].employee.user.permanentAddress,
                permanentCity: processInstances[0].employee.user.permanentCity,
                permanentPinCode: processInstances[0].employee.user.permanentPinCode,
                serviceStatus: processInstances[0].employee.serviceHistory[0].serviceStatus,
                serviceFrom: (0, _utils.epochToDmy)((0, _search.intConversion)(processInstances[0].employee.serviceHistory[0].serviceFrom)),
                serviceTo: processInstances[0].employee.serviceHistory[0].serviceTo != null ? (0, _utils.epochToDmy)((0, _search.intConversion)(processInstances[0].employee.serviceHistory[0].serviceTo)) : null,
                reasonForRetirement: processInstances[0].employeeOtherDetails.reasonForRetirement,
                isEligibleForPension: processInstances[0].employeeOtherDetails.isEligibleForPension,
                isTakenMonthlyPensionAndGratuity: processInstances[0].employeeOtherDetails.isTakenMonthlyPensionAndGratuity,
                isTakenGratuityCommutationTerminalBenefit: processInstances[0].employeeOtherDetails.isTakenGratuityCommutationTerminalBenefit,
                isTakenCompensationPensionAndGratuity: processInstances[0].employeeOtherDetails.isTakenCompensationPensionAndGratuity,
                totalNoPayLeavesDays: processInstances[0].employeeOtherDetails.totalNoPayLeavesDays,
                totalNoPayLeavesMonths: processInstances[0].employeeOtherDetails.totalNoPayLeavesMonths,
                totalNoPayLeavesYears: processInstances[0].employeeOtherDetails.totalNoPayLeavesYears,
                lpd: processInstances[0].employeeOtherDetails.lpd,
                incomeTax: processInstances[0].employeeOtherDetails.incomeTax,
                overPayment: processInstances[0].employeeOtherDetails.overPayment,
                medicalRelief: processInstances[0].employeeOtherDetails.medicalRelief,
                miscellaneous: processInstances[0].employeeOtherDetails.miscellaneous,
                isDuesPresent: processInstances[0].employeeOtherDetails.isDuesPresent,
                isDuesAmountDecided: processInstances[0].employeeOtherDetails.isDuesAmountDecided,
                dues: processInstances[0].employeeOtherDetails.dues,
                isConvictedSeriousCrimeOrGraveMisconduct: processInstances[0].employeeOtherDetails.isConvictedSeriousCrimeOrGraveMisconduct,
                isAnyJudicialProceedingIsContinuing: processInstances[0].employeeOtherDetails.isAnyJudicialProceedingIsContinuing,
                isAnyMisconductInsolvencyInefficiency: processInstances[0].employeeOtherDetails.isAnyMisconductInsolvencyInefficiency,
                isCompassionatePensionGranted: processInstances[0].employeeOtherDetails.isCompassionatePensionGranted,
                isCommutationOpted: processInstances[0].employeeOtherDetails.isCommutationOpted
              }, "isCommutationOpted", processInstances[0].employeeOtherDetails.isCommutationOpted);
              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                ApplicationDetails: applicationDetails
              };


              res.json(response);

            case 9:
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
//# sourceMappingURL=searchWorkflowApplicationDetails.js.map