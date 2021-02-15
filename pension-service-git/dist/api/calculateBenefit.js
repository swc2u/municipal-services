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

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_calculateBenefit", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var tenantId, businessService, businessId, workflowSearchResponse, processInstance, reasonForRetirement, employee, serviceHistory, i, dependents, benefitRespone, benefits, pensionCalculationDetails, processInstances, response;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              //getting mdms data


              tenantId = body.ProcessInstances[0].tenantId;
              businessService = body.ProcessInstances[0].businessService;
              businessId = body.ProcessInstances[0].businessId;
              _context.next = 5;
              return (0, _utils.searchPensionWorkflow)(body.RequestInfo, tenantId, businessId);

            case 5:
              workflowSearchResponse = _context.sent;


              _logger2.default.debug("workflowSearchResponse", JSON.stringify(workflowSearchResponse));

              processInstance = workflowSearchResponse.ProcessInstances[0];
              reasonForRetirement = "";
              _context.t0 = businessService;
              _context.next = _context.t0 === _envVariables2.default.EGOV_PENSION_RRP_BUSINESS_SERVICE ? 12 : _context.t0 === _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE ? 14 : _context.t0 === _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE ? 16 : 18;
              break;

            case 12:
              reasonForRetirement = processInstance.employeeOtherDetails.reasonForRetirement;
              return _context.abrupt("break", 18);

            case 14:
              reasonForRetirement = "DEATH_AS_EMPLOYEE";
              return _context.abrupt("break", 18);

            case 16:
              reasonForRetirement = "DEATH_AS_PENSIONER";
              return _context.abrupt("break", 18);

            case 18:
              employee = {
                dob: (0, _utils.epochToYmd)((0, _search.intConversion)(processInstance.employee.user.dob)),
                employeeType: processInstance.employee.employeeType,
                dateOfRetirement: (0, _utils.epochToYmd)((0, _search.intConversion)(processInstance.employee.dateOfRetirement)),
                dateOfDeath: processInstance.employee.dateOfDeath && processInstance.employee.dateOfDeath != 0 ? (0, _utils.epochToYmd)((0, _search.intConversion)(processInstance.employee.dateOfDeath)) : null,
                reasonForRetirement: reasonForRetirement, //processInstance.employeeOtherDetails.reasonForRetirement, 
                lastDesignation: "",
                totalNoPayLeavesDays: processInstance.employeeOtherDetails.totalNoPayLeavesDays != null ? Number(processInstance.employeeOtherDetails.totalNoPayLeavesDays) : 0,
                totalNoPayLeavesMonths: processInstance.employeeOtherDetails.totalNoPayLeavesMonths != null ? Number(processInstance.employeeOtherDetails.totalNoPayLeavesMonths) : 0,
                totalNoPayLeavesYears: processInstance.employeeOtherDetails.totalNoPayLeavesYears != null ? Number(processInstance.employeeOtherDetails.totalNoPayLeavesYears) : 0,
                lpd: processInstance.employeeOtherDetails.lpd != null ? Number(processInstance.employeeOtherDetails.lpd) : 0,
                fma: processInstance.employeeOtherDetails.fma != null ? Number(processInstance.employeeOtherDetails.fma) : 0,
                dues: processInstance.employeeOtherDetails.dues != null ? Number(processInstance.employeeOtherDetails.dues) : 0,
                medicalRelief: processInstance.employeeOtherDetails.medicalRelief != null ? Number(processInstance.employeeOtherDetails.medicalRelief) : 0,
                miscellaneous: processInstance.employeeOtherDetails.miscellaneous != null ? Number(processInstance.employeeOtherDetails.miscellaneous) : 0,
                overPayment: processInstance.employeeOtherDetails.overPayment != null ? Number(processInstance.employeeOtherDetails.overPayment) : 0,
                incomeTax: processInstance.employeeOtherDetails.incomeTax != null ? Number(processInstance.employeeOtherDetails.incomeTax) : 0,
                cess: processInstance.employeeOtherDetails.cess != null ? Number(processInstance.employeeOtherDetails.cess) : 0,
                isCommutationOpted: processInstance.employeeOtherDetails.isCommutationOpted,
                isEmploymentActive: processInstance.employeeOtherDetails.isEmploymentActive,
                isConvictedSeriousCrimeOrGraveMisconduct: processInstance.employeeOtherDetails.isConvictedSeriousCrimeOrGraveMisconduct,
                isAnyJudicialProceedingIsContinuing: processInstance.employeeOtherDetails.isAnyJudicialProceedingIsContinuing,
                isAnyMisconductInsolvencyInefficiency: processInstance.employeeOtherDetails.isAnyMisconductInsolvencyInefficiency,
                isEmployeeDiesInTerroristAttack: processInstance.employeeOtherDetails.isEmployeeDiesInTerroristAttack,
                isEmployeeDiesInAccidentalDeath: processInstance.employeeOtherDetails.isEmployeeDiesInAccidentalDeath,
                isDuesPresent: processInstance.employeeOtherDetails.isDuesPresent,
                isDuesAmountDecided: processInstance.employeeOtherDetails.isDuesAmountDecided,
                isTakenMonthlyPensionAndGratuity: processInstance.employeeOtherDetails.isTakenMonthlyPensionAndGratuity,
                isTakenGratuityCommutationTerminalBenefit: processInstance.employeeOtherDetails.isTakenGratuityCommutationTerminalBenefit,
                isTakenCompensationPensionAndGratuity: processInstance.employeeOtherDetails.isTakenCompensationPensionAndGratuity,
                diesInExtremistsDacoitsSmugglerAntisocialAttack: processInstance.employeeOtherDetails.diesInExtremistsDacoitsSmugglerAntisocialAttack,
                isCompassionatePensionGranted: processInstance.employeeOtherDetails.isCompassionatePensionGranted,
                noDuesForAvailGovtAccomodation: processInstance.employeeOtherDetails.noDuesForAvailGovtAccomodation,
                employeeGroup: processInstance.employeeOtherDetails.employeeGroup,
                employeeDisability: {
                  disabilityPercentage: processInstance.employeeDisability.disabilityPercentage != null ? Number(processInstance.employeeDisability.disabilityPercentage) : 0,
                  woundExtraordinaryPension: processInstance.employeeDisability.woundExtraordinaryPension != null ? Number(processInstance.employeeDisability.woundExtraordinaryPension) : 0,
                  attendantAllowanceGranted: processInstance.employeeDisability.attendantAllowanceGranted != null ? processInstance.employeeDisability.attendantAllowanceGranted : false
                },
                serviceHistory: [],
                dependents: []
              };
              serviceHistory = [];

              for (i = 0; i < processInstance.employee.serviceHistory.length; i++) {
                serviceHistory.push({
                  serviceFrom: (0, _utils.epochToYmd)((0, _search.intConversion)(processInstance.employee.serviceHistory[i].serviceFrom)),
                  serviceTo: processInstance.employee.serviceHistory[i].serviceTo ? (0, _utils.epochToYmd)((0, _search.intConversion)(processInstance.employee.serviceHistory[i].serviceTo)) : null
                });
              }
              employee.serviceHistory = serviceHistory;

              dependents = [];

              if (processInstance.dependents) {
                for (i = 0; i < processInstance.dependents.length; i++) {
                  dependents.push({
                    name: processInstance.dependents[i].name,
                    dob: (0, _utils.epochToYmd)((0, _search.intConversion)(processInstance.dependents[i].dob)),
                    address: processInstance.dependents[i].address,
                    mobileNumber: processInstance.dependents[i].mobileNumber,
                    relationship: processInstance.dependents[i].relationship,
                    isDisabled: processInstance.dependents[i].isDisabled,
                    maritalStatus: processInstance.dependents[i].maritalStatus,
                    isHollyDependent: processInstance.dependents[i].isHollyDependent,
                    noSpouseNoChildren: processInstance.dependents[i].noSpouseNoChildren,
                    isGrandChildFromDeceasedSon: processInstance.dependents[i].isGrandChildFromDeceasedSon,
                    isEligibleForGratuity: processInstance.dependents[i].isEligibleForGratuity,
                    isEligibleForPension: processInstance.dependents[i].isEligibleForPension,
                    gratuityPercentage: processInstance.dependents[i].gratuityPercentage
                  });
                }
              }
              employee.dependents = dependents;

              /*
                let assignments=[];
              for (var i = 0; i < processInstance.employee.assignments.length; i++) {                    
                  assignments.push({
                    fromDate:  epochToYmd(intConversion(processInstance.employee.assignments[i].fromDate)),
                    toDate: processInstance.employee.assignments[i].toDate? epochToYmd(intConversion(processInstance.employee.assignments[i].toDate)):null
                  });
                } 
              assignments=orderBy(processInstance.employee.assignments,['fromDate'],['desc']);
              let lastAssignment=assignments[0];
              employee.lastDesignation=lastAssignment.designation;
              */
              employee.lastDesignation = "";

              body.ProcessInstances[0].employee = employee;
              body.ProcessInstances[0].employee.businessService = businessService;

              _context.next = 30;
              return (0, _utils.calculateBenefit)(body);

            case 30:
              benefitRespone = _context.sent;

              _logger2.default.debug("benefitRespone", JSON.stringify(benefitRespone));

              benefits = benefitRespone.EmployeeBenefits[0].benefits;
              pensionCalculationDetails = benefitRespone.EmployeeBenefits[0].pensionCalculationDetails;
              //let notifications=benefitRespone.EmployeeBenefits[0].notifications;

              i = 0;

            case 35:
              if (!(i < benefits.length)) {
                _context.next = 148;
                break;
              }

              _context.t1 = String(benefits[i].benefitCode).toUpperCase();
              _context.next = _context.t1 === "IR" ? 39 : _context.t1 === "PENSION_IR" ? 41 : _context.t1 === "BASIC_PENSION" ? 45 : _context.t1 === "COMMUTED_PENSION" ? 49 : _context.t1 === "PENSION_DEDUCTION" ? 53 : _context.t1 === "COMMUTATION_VALUE" ? 57 : _context.t1 === "DA" ? 61 : _context.t1 === "PENSION_DA" ? 63 : _context.t1 === "DCRG" ? 67 : _context.t1 === "ADDITIONAL_PENSION" ? 71 : _context.t1 === "COMPASSIONATE_PENSION" ? 75 : _context.t1 === "COMPENSATION_PENSION" ? 79 : _context.t1 === "TERMINAL_BENEFIT" ? 83 : _context.t1 === "DCRG_DUES_DEDUCTION" ? 87 : _context.t1 === "NET_DEDUCTION" ? 91 : _context.t1 === "TOTAL_PENSION" ? 95 : _context.t1 === "FINAL_CALCULATED_PENSION" ? 99 : _context.t1 === "FINAL_CALCULATED_GRATUITY" ? 103 : _context.t1 === "FAMILY_PENSION_1" ? 107 : _context.t1 === "FAMILY_PENSION_2" ? 111 : _context.t1 === "PENSIONER_FAMILY_PENSION" ? 115 : _context.t1 === "FAMILY_PENSION_1_START_DATE" ? 119 : _context.t1 === "FAMILY_PENSION_1_END_DATE" ? 121 : _context.t1 === "FAMILY_PENSION_2_START_DATE" ? 123 : _context.t1 === "EX_GRATIA" ? 125 : _context.t1 === "PROVISIONAL_PENSION" ? 129 : _context.t1 === "INVALID_PENSION" ? 133 : _context.t1 === "WOUND_EXTRAORDINARY_PENSION" ? 137 : _context.t1 === "ATTENDANT_ALLOWANCE" ? 141 : 145;
              break;

            case 39:
              pensionCalculationDetails.interimReliefLpdSystem = benefits[i].finalBenefitValue;
              //pensionCalculationDetails.interimReliefApplicable=benefits[i].benefitApplicable;
              //pensionCalculationDetails.interimReliefExpression=benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 41:
              pensionCalculationDetails.interimReliefSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.interimReliefApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.interimReliefExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 45:
              pensionCalculationDetails.basicPensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.basicPensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.basicPensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 49:
              pensionCalculationDetails.commutedPensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.commutedPensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.commutedPensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 53:
              pensionCalculationDetails.pensionDeductionsSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.pensionDeductionsApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.pensionDeductionsExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 57:
              pensionCalculationDetails.commutedValueSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.commutationValueApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.commutationValueExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 61:
              pensionCalculationDetails.daLpdSystem = benefits[i].finalBenefitValue;
              return _context.abrupt("break", 145);

            case 63:
              pensionCalculationDetails.daSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.daApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.daExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 67:
              pensionCalculationDetails.dcrgSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.dcrgApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.dcrgExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 71:
              pensionCalculationDetails.additionalPensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.additionalPensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.additionalPensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 75:
              pensionCalculationDetails.compassionatePensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.compassionatePensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.compassionatePensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 79:
              pensionCalculationDetails.compensationPensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.compensationPensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.compensationPensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 83:
              pensionCalculationDetails.terminalBenefitSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.terminalBenefitApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.terminalBenefitExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 87:
              pensionCalculationDetails.duesDeductionsSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.duesDeductionsApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.duesDeductionsExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 91:
              pensionCalculationDetails.netDeductionsSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.netDeductionsApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.netDeductionsExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 95:
              pensionCalculationDetails.totalPensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.totalPensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.totalPensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 99:
              pensionCalculationDetails.finalCalculatedPensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.finalCalculatedPensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.finalCalculatedPensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 103:
              pensionCalculationDetails.finalCalculatedGratuitySystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.finalCalculatedGratuityApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.finalCalculatedGratuityExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 107:
              pensionCalculationDetails.familyPensionISystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.familyPensionIApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.familyPensionIExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 111:
              pensionCalculationDetails.familyPensionIISystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.familyPensionIIApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.familyPensionIIExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 115:
              pensionCalculationDetails.pensionerFamilyPensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.pensionerFamilyPensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.pensionerFamilyPensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 119:
              pensionCalculationDetails.familyPensionIStartDateSystem = benefits[i].finalBenefitValue != null ? (0, _search.intConversion)((0, _utils.convertDateToEpochForDeathDate)(benefits[i].finalBenefitValue, "dob")) : null;
              return _context.abrupt("break", 145);

            case 121:
              pensionCalculationDetails.familyPensionIEndDateSystem = benefits[i].finalBenefitValue != null ? (0, _search.intConversion)((0, _utils.convertDateToEpochForDeathDate)(benefits[i].finalBenefitValue, "dob")) : null;
              return _context.abrupt("break", 145);

            case 123:
              pensionCalculationDetails.familyPensionIIStartDateSystem = benefits[i].finalBenefitValue != null ? (0, _search.intConversion)((0, _utils.convertDateToEpochForDeathDate)(benefits[i].finalBenefitValue, "dob")) : null;
              return _context.abrupt("break", 145);

            case 125:
              pensionCalculationDetails.exGratiaSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.exGratiaApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.exGratiaExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 129:
              pensionCalculationDetails.provisionalPensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.provisionalPensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.provisionalPensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 133:
              pensionCalculationDetails.invalidPensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.invalidPensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.invalidPensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 137:
              pensionCalculationDetails.woundExtraordinaryPensionSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.woundExtraordinaryPensionApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.woundExtraordinaryPensionExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 141:
              pensionCalculationDetails.attendantAllowanceSystem = benefits[i].finalBenefitValue;
              pensionCalculationDetails.attendantAllowanceApplicable = benefits[i].benefitApplicable;
              pensionCalculationDetails.attendantAllowanceExpression = benefits[i].benefitFormulaExpression;
              return _context.abrupt("break", 145);

            case 145:
              i++;
              _context.next = 35;
              break;

            case 148:

              /*
              let notificationText="";
              for (var i = 0; i < notifications.length; i++) {    
                notificationText=`${notificationText}${notifications[i].notificationText} `
              }
              */

              processInstances = [];

              processInstances.push({
                pensionCalculationDetails: pensionCalculationDetails,
                pensionCalculationUpdateDetails: {
                  nqsYearVerified: pensionCalculationDetails.nqsYearSystem,
                  nqsMonthVerified: pensionCalculationDetails.nqsMonthSystem,
                  nqsDayVerified: pensionCalculationDetails.nqsDaySystem,
                  basicPensionVerified: pensionCalculationDetails.basicPensionSystem,
                  pensionDeductionsVerified: pensionCalculationDetails.pensionDeductionsSystem,
                  additionalPensionVerified: pensionCalculationDetails.additionalPensionSystem,
                  commutedPensionVerified: pensionCalculationDetails.commutedPensionSystem,
                  commutedValueVerified: pensionCalculationDetails.commutedValueSystem,
                  familyPensionIVerified: pensionCalculationDetails.familyPensionISystem,
                  familyPensionIIVerified: pensionCalculationDetails.familyPensionIISystem,
                  dcrgVerified: pensionCalculationDetails.dcrgSystem,
                  netDeductionsVerified: pensionCalculationDetails.netDeductionsSystem,
                  totalPensionVerified: pensionCalculationDetails.totalPensionSystem,
                  finalCalculatedPensionVerified: pensionCalculationDetails.finalCalculatedPensionSystem,
                  interimReliefVerified: pensionCalculationDetails.interimReliefSystem,
                  daVerified: pensionCalculationDetails.daSystem,
                  duesDeductionsVerified: pensionCalculationDetails.duesDeductionsSystem,
                  compassionatePensionVerified: pensionCalculationDetails.compassionatePensionSystem,
                  compensationPensionVerified: pensionCalculationDetails.compensationPensionSystem,
                  terminalBenefitVerified: pensionCalculationDetails.terminalBenefitSystem,
                  finalCalculatedGratuityVerified: pensionCalculationDetails.finalCalculatedGratuitySystem,
                  familyPensionIStartDateVerified: pensionCalculationDetails.familyPensionIStartDateSystem,
                  familyPensionIEndDateVerified: pensionCalculationDetails.familyPensionIEndDateSystem,
                  familyPensionIIStartDateVerified: pensionCalculationDetails.familyPensionIIStartDateSystem,
                  exGratiaVerified: pensionCalculationDetails.exGratiaSystem,
                  pensionerFamilyPensionVerified: pensionCalculationDetails.pensionerFamilyPensionSystem,
                  provisionalPensionVerified: pensionCalculationDetails.provisionalPensionSystem,
                  invalidPensionVerified: pensionCalculationDetails.invalidPensionSystem,
                  woundExtraordinaryPensionVerified: pensionCalculationDetails.woundExtraordinaryPensionSystem,
                  attendantAllowanceVerified: pensionCalculationDetails.attendantAllowanceSystem,
                  gqsYearVerified: pensionCalculationDetails.gqsYearSystem,
                  gqsMonthVerified: pensionCalculationDetails.gqsMonthSystem,
                  gqsDayVerified: pensionCalculationDetails.gqsDaySystem,
                  notificationTextVerified: pensionCalculationDetails.notificationTextSystem,
                  interimReliefLpdVerified: pensionCalculationDetails.interimReliefLpdSystem,
                  daLpdVerified: pensionCalculationDetails.daLpdSystem //,
                  //notifications: {
                  //  notificationText: notificationText
                  //}
                } });

              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                ProcessInstances: processInstances
              };


              res.json(response);

            case 152:
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
//# sourceMappingURL=calculateBenefit.js.map