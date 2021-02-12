"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.addUUIDAndAuditDetailsCreatePensionRevisionBulk = exports.addUUIDAndAuditDetailsMigratedPensioner = exports.addUUIDAndAuditDetailsInitiateReComputation = exports.addUUIDAndAuditDetailsPensionerPensionDiscontinuation = exports.addUUIDAndAuditDetailsDisabilityRegistration = exports.addUUIDAndAuditDetailsCloseWorkflow = exports.addUUIDAndAuditDetailsCreateMonthlyPensionRegister = exports.addUUIDAndAuditDetailsUpdateRevisedPension = exports.addUUIDAndAuditDetailsCloseLastRevisedPension = exports.addUUIDAndAuditDetailsCreateRevisedPension = exports.addUUIDAndAuditDetailsClaimReleaseWorkflow = exports.addUUIDAndAuditDetails = undefined;

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _utils = require("../utils");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _filter = require("lodash/filter");

var _filter2 = _interopRequireDefault(_filter);

var _orderBy = require("lodash/orderBy");

var _orderBy2 = _interopRequireDefault(_orderBy);

var _userService = require("../services/userService");

var _userService2 = _interopRequireDefault(_userService);

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _search = require("./search");

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var addUUIDAndAuditDetails = exports.addUUIDAndAuditDetails = function () {
  var _ref = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(request) {
    var state = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : "";

    var ProcessInstances, RequestInfo, businessService, action, createdBy, createdDate, auditDetails, pensionEmployeeId, code, hrmsResponse, employee, assignments, lastAssignments, lastAssignment, i, serviceHistory, user, applicationNumber, workflowHeader, _state, employeeOtherDetails, pensionCalculationDetails, pensionCalculationUpdateDetails, _applicationNumber, _workflowHeader, _state2, _employeeOtherDetails, _pensionCalculationDetails, _pensionCalculationUpdateDetails, _applicationNumber2, employeeResponse, _employee, _workflowHeader2, _state3, _employeeOtherDetails2, _pensionCalculationDetails2, _pensionCalculationUpdateDetails2, documents, dependents, workflowHeaderAudit, j;

    return _regenerator2.default.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            ProcessInstances = request.ProcessInstances, RequestInfo = request.RequestInfo;
            businessService = ProcessInstances[0].businessService;
            action = ProcessInstances[0].action;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };
            _context.t0 = businessService;
            _context.next = _context.t0 === _envVariables2.default.EGOV_PENSION_RRP_BUSINESS_SERVICE ? 9 : _context.t0 === _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE ? 39 : _context.t0 === _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE ? 62 : 90;
            break;

          case 9:
            if (!(action == "INITIATE")) {
              _context.next = 38;
              break;
            }

            pensionEmployeeId = ProcessInstances[0].employee.pensionEmployeeId;
            code = ProcessInstances[0].employee.code;
            //fetch employee data from hrms 

            _context.next = 14;
            return (0, _utils.getEmployeeDetails)(request.RequestInfo, ProcessInstances[0].tenantId, ProcessInstances[0].employee.code);

          case 14:
            hrmsResponse = _context.sent;
            employee = hrmsResponse.Employees[0];


            if (employee) {
              //employee.pensionEmployeeId=ProcessInstances[i].employee.pensionEmployeeId;
              assignments = [];

              if (employee.assignments && !(0, _isEmpty2.default)(employee.assignments)) {
                assignments = employee.assignments;
                lastAssignments = [];
                lastAssignment = [];

                if (assignments.length > 1) {
                  assignments = (0, _orderBy2.default)(assignments, ['fromDate'], ['desc']);
                  lastAssignments = (0, _filter2.default)(assignments, function (x) {
                    return x.fromDate == assignments[0].fromDate;
                  });
                  if (lastAssignments.length > 1) {
                    lastAssignments = (0, _filter2.default)(lastAssignments, function (x) {
                      return x.isPrimaryAssignment == true;
                    });
                    if (lastAssignments.length > 0) {
                      lastAssignment.push(lastAssignments[0]);
                    } else {
                      lastAssignments = (0, _filter2.default)(assignments, function (x) {
                        return x.fromDate == assignments[0].fromDate;
                      });
                      lastAssignment.push(lastAssignments[0]);
                    }
                  }
                } else {
                  lastAssignment = assignments;
                }
                for (i = 0; i < assignments.length; i++) {
                  if (assignments[i].id == lastAssignment[0].id) {
                    assignments[i].isPensionApplicable = true;
                  }
                  assignments[i].id = (0, _utils.uuidv1)();
                  assignments[i].tenantId = employee.tenantId;
                  assignments[i].active = true;
                  assignments[i].pensionEmployeeId = ProcessInstances[0].employee.pensionEmployeeId;
                }
                //employee.assignments=assignments;
                //ProcessInstances[i].employee.assignments=assignments;

              }
              serviceHistory = [];

              if (employee.serviceHistory && !(0, _isEmpty2.default)(employee.serviceHistory)) {
                serviceHistory = employee.serviceHistory;
                for (i = 0; i < serviceHistory.length; i++) {
                  serviceHistory[i].id = (0, _utils.uuidv1)();
                  serviceHistory[i].tenantId = employee.tenantId;
                  serviceHistory[i].active = true;
                  serviceHistory[i].pensionEmployeeId = ProcessInstances[0].employee.pensionEmployeeId;
                }
                //employee.serviceHistory=serviceHistory;
                //ProcessInstances[i].employee.serviceHistory=serviceHistory;
              }

              user = employee.user;

              user.employeeContactDetailsId = (0, _utils.uuidv1)();
              user.tenantId = employee.tenantId;
              user.active = true;
              //employee.user=user;

              //ProcessInstances[i].employee.user=user;
              _logger2.default.debug("employee", JSON.stringify(employee));
              //ProcessInstances[i].employee=employee; 
              ProcessInstances[0].employee = {
                pensionEmployeeId: pensionEmployeeId,
                code: code,
                assignments: assignments,
                serviceHistory: serviceHistory,
                user: user
              };
            }
            //for loop should be replaced new alternative
            i = 0;

          case 18:
            if (!(i < ProcessInstances.length)) {
              _context.next = 38;
              break;
            }

            _context.next = 21;
            return (0, _utils.addIDGenId)(RequestInfo, [{
              idName: _envVariables2.default.EGOV_IDGEN_PENSION_RRP_APPLICATION_NUMBER_ID_NAME,
              tenantId: ProcessInstances[i].tenantId,
              format: _envVariables2.default.EGOV_RR_APPLICATION_FORMATE,
              count: 1
            }]);

          case 21:
            applicationNumber = _context.sent;


            ProcessInstances[i].businessId = applicationNumber;

            //workflowHeader object
            workflowHeader = {
              workflowHeaderId: (0, _utils.uuidv1)(),
              active: true,
              workflowHeaderAudit: {
                workflowHeaderAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].workflowHeader = workflowHeader;

            //notificationRegister object
            if (ProcessInstances[i].notificationRegister != null) {
              ProcessInstances[i].notificationRegister.pensionNotificationRegisterAuditId = (0, _utils.uuidv1)();
            }

            //state object
            _state = {
              state: ""
            };

            ProcessInstances[i].state = _state;

            //employeeOtherDetails object (default)
            employeeOtherDetails = {
              employeeOtherDetailsId: (0, _utils.uuidv1)(),
              ltc: 0,
              lpd: 0,
              pensionArrear: 0,
              isDaMedicalAdmissible: false,
              fma: 0,
              medicalRelief: 0,
              miscellaneous: 0,
              overPayment: 0,
              incomeTax: 0,
              cess: 0,
              bankAddress: "",
              accountNumber: "",
              claimant: "",
              wef: null,
              active: true,
              employeeOtherDetailsAudit: {
                employeeOtherDetailsAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].employeeOtherDetails = employeeOtherDetails;

            //pensionCalculationDetails object (default)
            pensionCalculationDetails = {
              pensionCalculationDetailsId: (0, _utils.uuidv1)(),
              basicPensionSystem: 0,
              pensionDeductionsSystem: 0,
              additionalPensionSystem: 0,
              commutedPensionSystem: 0,
              commutedValueSystem: 0,
              familyPensionISystem: 0,
              familyPensionIISystem: 0,
              dcrgSystem: 0,
              netDeductionsSystem: 0,
              finalCalculatedPensionSystem: 0,
              interimReliefSystem: 0,
              daSystem: 0,
              nqsYearSystem: 0,
              nqsMonthSystem: 0,
              nqsDaySystem: 0,
              duesDeductionsSystem: 0,
              compassionatePensionSystem: 0,
              compensationPensionSystem: 0,
              terminalBenefitSystem: 0,
              finalCalculatedGratuitySystem: 0,
              active: true,
              pensionCalculationDetailsAudit: {
                pensionCalculationDetailsAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].pensionCalculationDetails = pensionCalculationDetails;

            //pensionCalculationUpdateDetails object (default)
            pensionCalculationUpdateDetails = {
              basicPensionVerified: 0,
              pensionDeductionsVerified: 0,
              additionalPensionVerified: 0,
              commutedPensionVerified: 0,
              commutedValueVerified: 0,
              familyPensionIVerified: 0,
              familyPensionIIVerified: 0,
              dcrgVerified: 0,
              netDeductionsVerified: 0,
              finalCalculatedPensionVerified: 0,
              interimReliefVerified: 0,
              daVerified: 0,
              nqsYearVerified: 0,
              nqsMonthVerified: 0,
              nqsDayVerified: 0,
              duesDeductionsVerified: 0,
              compassionatePensionVerified: 0,
              compensationPensionVerified: 0,
              terminalBenefitVerified: 0,
              finalCalculatedGratuityVerified: 0
            };

            ProcessInstances[i].pensionCalculationUpdateDetails = pensionCalculationUpdateDetails;

            ProcessInstances[i].auditDetails = auditDetails;

          case 35:
            i++;
            _context.next = 18;
            break;

          case 38:
            return _context.abrupt("break", 90);

          case 39:
            if (!(action == "INITIATE")) {
              _context.next = 61;
              break;
            }

            i = 0;

          case 41:
            if (!(i < ProcessInstances.length)) {
              _context.next = 61;
              break;
            }

            _context.next = 44;
            return (0, _utils.addIDGenId)(RequestInfo, [{
              idName: _envVariables2.default.EGOV_IDGEN_PENSION_DOE_APPLICATION_NUMBER_ID_NAME,
              tenantId: ProcessInstances[i].tenantId,
              format: _envVariables2.default.EGOV_DE_APPLICATION_FORMATE,
              count: 1
            }]);

          case 44:
            _applicationNumber = _context.sent;

            ProcessInstances[i].businessId = _applicationNumber;

            //workflowHeader object
            _workflowHeader = {
              workflowHeaderId: (0, _utils.uuidv1)(),
              active: true,
              workflowHeaderAudit: {
                workflowHeaderAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].workflowHeader = _workflowHeader;

            //employee object                  
            ProcessInstances[i].employee.employeeAudit = {
              pensionEmployeeAuditId: (0, _utils.uuidv1)()
            };

            //state object
            _state2 = {
              state: ""
            };

            ProcessInstances[i].state = _state2;

            //employeeOtherDetails object
            _employeeOtherDetails = {
              employeeOtherDetailsId: (0, _utils.uuidv1)(),
              ltc: 0,
              lpd: 0,
              pensionArrear: 0,
              isDaMedicalAdmissible: false,
              fma: 0,
              medicalRelief: 0,
              miscellaneous: 0,
              overPayment: 0,
              incomeTax: 0,
              cess: 0,
              bankAddress: "",
              accountNumber: "",
              claimant: "",
              wef: null,
              active: true,
              employeeOtherDetailsAudit: {
                employeeOtherDetailsAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].employeeOtherDetails = _employeeOtherDetails;

            //pensionCalculationDetails object
            _pensionCalculationDetails = {
              pensionCalculationDetailsId: (0, _utils.uuidv1)(),
              basicPensionSystem: 0,
              pensionDeductionsSystem: 0,
              additionalPensionSystem: 0,
              commutedPensionSystem: 0,
              commutedValueSystem: 0,
              familyPensionISystem: 0,
              familyPensionIISystem: 0,
              dcrgSystem: 0,
              netDeductionsSystem: 0,
              finalCalculatedPensionSystem: 0,
              interimReliefSystem: 0,
              daSystem: 0,
              nqsYearSystem: 0,
              nqsMonthSystem: 0,
              nqsDaySystem: 0,
              duesDeductionsSystem: 0,
              compassionatePensionSystem: 0,
              compensationPensionSystem: 0,
              terminalBenefitSystem: 0,
              finalCalculatedGratuitySystem: 0,
              active: true,
              pensionCalculationDetailsAudit: {
                pensionCalculationDetailsAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].pensionCalculationDetails = _pensionCalculationDetails;

            _pensionCalculationUpdateDetails = {
              basicPensionVerified: 0,
              pensionDeductionsVerified: 0,
              additionalPensionVerified: 0,
              commutedPensionVerified: 0,
              commutedValueVerified: 0,
              familyPensionIVerified: 0,
              familyPensionIIVerified: 0,
              dcrgVerified: 0,
              netDeductionsVerified: 0,
              finalCalculatedPensionVerified: 0,
              interimReliefVerified: 0,
              daVerified: 0,
              nqsYearVerified: 0,
              nqsMonthVerified: 0,
              nqsDayVerified: 0,
              duesDeductionsVerified: 0,
              compassionatePensionVerified: 0,
              compensationPensionVerified: 0,
              terminalBenefitVerified: 0,
              finalCalculatedGratuityVerified: 0
            };

            ProcessInstances[i].pensionCalculationUpdateDetails = _pensionCalculationUpdateDetails;

            ProcessInstances[i].auditDetails = auditDetails;

          case 58:
            i++;
            _context.next = 41;
            break;

          case 61:
            return _context.abrupt("break", 90);

          case 62:
            if (!(action == "INITIATE")) {
              _context.next = 89;
              break;
            }

            i = 0;

          case 64:
            if (!(i < ProcessInstances.length)) {
              _context.next = 89;
              break;
            }

            _context.next = 67;
            return (0, _utils.addIDGenId)(RequestInfo, [{
              idName: _envVariables2.default.EGOV_IDGEN_PENSION_DOP_APPLICATION_NUMBER_ID_NAME,
              tenantId: ProcessInstances[i].tenantId,
              format: _envVariables2.default.EGOV_DP_APPLICATION_FORMATE,
              count: 1
            }]);

          case 67:
            _applicationNumber2 = _context.sent;

            ProcessInstances[i].businessId = _applicationNumber2;
            //fetch employee detaisl from pension module
            _context.next = 71;
            return (0, _utils.searchEmployee)(RequestInfo, ProcessInstances[i].tenantId, ProcessInstances[i].employee.code);

          case 71:
            employeeResponse = _context.sent;
            _employee = employeeResponse.Employees[0];
            //employee object       

            ProcessInstances[i].employee = {
              pensionEmployeeId: _employee.pensionEmployeeId,
              code: ProcessInstances[i].employee.code,
              dateOfDeath: ProcessInstances[i].employee.dateOfDeath,
              employeeAudit: {
                pensionEmployeeAuditId: (0, _utils.uuidv1)()
              }

              //workflowHeader object
            };_workflowHeader2 = {
              workflowHeaderId: (0, _utils.uuidv1)(),
              active: true,
              workflowHeaderAudit: {
                workflowHeaderAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].workflowHeader = _workflowHeader2;

            //employee object                  
            ProcessInstances[i].employee.employeeAudit = {
              pensionEmployeeAuditId: (0, _utils.uuidv1)()
            };

            //state object
            _state3 = {
              state: ""
            };

            ProcessInstances[i].state = _state3;

            //employeeOtherDetails object
            _employeeOtherDetails2 = {
              employeeOtherDetailsId: (0, _utils.uuidv1)(),
              ltc: 0,
              lpd: 0,
              pensionArrear: 0,
              isDaMedicalAdmissible: false,
              fma: 0,
              medicalRelief: 0,
              miscellaneous: 0,
              overPayment: 0,
              incomeTax: 0,
              cess: 0,
              bankAddress: "",
              accountNumber: "",
              claimant: "",
              wef: null,
              active: true,
              employeeOtherDetailsAudit: {
                employeeOtherDetailsAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].employeeOtherDetails = _employeeOtherDetails2;

            //pensionCalculationDetails object
            _pensionCalculationDetails2 = {
              pensionCalculationDetailsId: (0, _utils.uuidv1)(),
              basicPensionSystem: 0,
              pensionDeductionsSystem: 0,
              additionalPensionSystem: 0,
              commutedPensionSystem: 0,
              commutedValueSystem: 0,
              familyPensionISystem: 0,
              familyPensionIISystem: 0,
              dcrgSystem: 0,
              netDeductionsSystem: 0,
              finalCalculatedPensionSystem: 0,
              interimReliefSystem: 0,
              daSystem: 0,
              nqsYearSystem: 0,
              nqsMonthSystem: 0,
              nqsDaySystem: 0,
              duesDeductionsSystem: 0,
              compassionatePensionSystem: 0,
              compensationPensionSystem: 0,
              terminalBenefitSystem: 0,
              finalCalculatedGratuitySystem: 0,
              active: true,
              pensionCalculationDetailsAudit: {
                pensionCalculationDetailsAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].pensionCalculationDetails = _pensionCalculationDetails2;

            _pensionCalculationUpdateDetails2 = {
              basicPensionVerified: 0,
              pensionDeductionsVerified: 0,
              additionalPensionVerified: 0,
              commutedPensionVerified: 0,
              commutedValueVerified: 0,
              familyPensionIVerified: 0,
              familyPensionIIVerified: 0,
              dcrgVerified: 0,
              netDeductionsVerified: 0,
              finalCalculatedPensionVerified: 0,
              interimReliefVerified: 0,
              daVerified: 0,
              nqsYearVerified: 0,
              nqsMonthVerified: 0,
              nqsDayVerified: 0,
              duesDeductionsVerified: 0,
              compassionatePensionVerified: 0,
              compensationPensionVerified: 0,
              terminalBenefitVerified: 0,
              finalCalculatedGratuityVerified: 0
            };

            ProcessInstances[i].pensionCalculationUpdateDetails = _pensionCalculationUpdateDetails2;

            ProcessInstances[i].auditDetails = auditDetails;

          case 86:
            i++;
            _context.next = 64;
            break;

          case 89:
            return _context.abrupt("break", 90);

          case 90:

            if (action != _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_INITIATE) {
              //let leaves=[];
              documents = [];
              dependents = [];


              for (i = 0; i < ProcessInstances.length; i++) {

                //workflowHeader object
                workflowHeaderAudit = {
                  workflowHeaderAuditId: (0, _utils.uuidv1)()
                };

                ProcessInstances[i].workflowHeader.workflowHeaderAudit = workflowHeaderAudit;

                //documents
                if (ProcessInstances[i].documents) {
                  for (j = 0; j < ProcessInstances[i].documents.length; j++) {
                    if (ProcessInstances[i].documents[j].fileStoreId != "") {
                      documents.push({
                        pensionAttachmentId: (0, _utils.uuidv1)(),
                        workflowHeaderId: ProcessInstances[i].workflowHeader.workflowHeaderId,
                        tenantId: ProcessInstances[i].tenantId, //
                        documentType: ProcessInstances[i].documents[j].documentType,
                        fileStoreId: ProcessInstances[i].documents[j].fileStoreId,
                        documentUid: (0, _utils.uuidv1)(), //workflow service only
                        active: true,
                        state: state,
                        comment: ProcessInstances[i].documents[j].comment,
                        documentAudit: {
                          pensionAttachmentAuditId: (0, _utils.uuidv1)()
                        },
                        auditDetails: auditDetails
                      });
                    }
                  }
                }

                //employeeOtherDetails
                if (ProcessInstances[i].employeeOtherDetails) {
                  ProcessInstances[i].employeeOtherDetails.ltc = ProcessInstances[i].employeeOtherDetails.ltc ? Number(ProcessInstances[i].employeeOtherDetails.ltc) : 0, ProcessInstances[i].employeeOtherDetails.lpd = ProcessInstances[i].employeeOtherDetails.lpd ? Number(ProcessInstances[i].employeeOtherDetails.lpd) : 0, ProcessInstances[i].employeeOtherDetails.pensionArrear = ProcessInstances[i].employeeOtherDetails.pensionArrear ? Number(ProcessInstances[i].employeeOtherDetails.pensionArrear) : 0, ProcessInstances[i].employeeOtherDetails.isDaMedicalAdmissible = ProcessInstances[i].employeeOtherDetails.isDaMedicalAdmissible ? ProcessInstances[i].employeeOtherDetails.isDaMedicalAdmissible : false, ProcessInstances[i].employeeOtherDetails.fma = ProcessInstances[i].employeeOtherDetails.fma ? Number(ProcessInstances[i].employeeOtherDetails.fma) : 0, ProcessInstances[i].employeeOtherDetails.medicalRelief = ProcessInstances[i].employeeOtherDetails.medicalRelief ? Number(ProcessInstances[i].employeeOtherDetails.medicalRelief) : 0, ProcessInstances[i].employeeOtherDetails.miscellaneous = ProcessInstances[i].employeeOtherDetails.miscellaneous ? Number(ProcessInstances[i].employeeOtherDetails.miscellaneous) : 0, ProcessInstances[i].employeeOtherDetails.overPayment = ProcessInstances[i].employeeOtherDetails.overPayment ? Number(ProcessInstances[i].employeeOtherDetails.overPayment) : 0, ProcessInstances[i].employeeOtherDetails.incomeTax = ProcessInstances[i].employeeOtherDetails.incomeTax ? Number(ProcessInstances[i].employeeOtherDetails.incomeTax) : 0, ProcessInstances[i].employeeOtherDetails.cess = ProcessInstances[i].employeeOtherDetails.cess ? Number(ProcessInstances[i].employeeOtherDetails.cess) : 0, ProcessInstances[i].employeeOtherDetails.bankAddress = ProcessInstances[i].employeeOtherDetails.bankAddress ? ProcessInstances[i].employeeOtherDetails.bankAddress : null, ProcessInstances[i].employeeOtherDetails.accountNumber = ProcessInstances[i].employeeOtherDetails.accountNumber ? ProcessInstances[i].employeeOtherDetails.accountNumber : null, ProcessInstances[i].employeeOtherDetails.claimant = ProcessInstances[i].employeeOtherDetails.claimant ? ProcessInstances[i].employeeOtherDetails.claimant : null, ProcessInstances[i].employeeOtherDetails.wef = ProcessInstances[i].employeeOtherDetails.wef && ProcessInstances[i].employeeOtherDetails.wef != 0 ? Number(ProcessInstances[i].employeeOtherDetails.wef) : null,
                  //ProcessInstances[i].employeeOtherDetails.dateOfContingent=ProcessInstances[i].employeeOtherDetails.dateOfContingent && ProcessInstances[i].employeeOtherDetails.dateOfContingent!=0? Number(ProcessInstances[i].employeeOtherDetails.dateOfContingent):null,
                  ProcessInstances[i].employeeOtherDetails.totalNoPayLeavesDays = ProcessInstances[i].employeeOtherDetails.totalNoPayLeavesDays ? Number(ProcessInstances[i].employeeOtherDetails.totalNoPayLeavesDays) : 0, ProcessInstances[i].employeeOtherDetails.dues = ProcessInstances[i].employeeOtherDetails.dues ? Number(ProcessInstances[i].employeeOtherDetails.dues) : 0, ProcessInstances[i].employeeOtherDetails.isEmploymentActive = ProcessInstances[i].employeeOtherDetails.isEmploymentActive ? ProcessInstances[i].employeeOtherDetails.isEmploymentActive : false, ProcessInstances[i].employeeOtherDetails.isConvictedSeriousCrimeOrGraveMisconduct = ProcessInstances[i].employeeOtherDetails.isConvictedSeriousCrimeOrGraveMisconduct ? ProcessInstances[i].employeeOtherDetails.isConvictedSeriousCrimeOrGraveMisconduct : false, ProcessInstances[i].employeeOtherDetails.isAnyJudicialProceedingIsContinuing = ProcessInstances[i].employeeOtherDetails.isAnyJudicialProceedingIsContinuing ? ProcessInstances[i].employeeOtherDetails.isAnyJudicialProceedingIsContinuing : false, ProcessInstances[i].employeeOtherDetails.isAnyMisconductInsolvencyInefficiency = ProcessInstances[i].employeeOtherDetails.isAnyMisconductInsolvencyInefficiency ? ProcessInstances[i].employeeOtherDetails.isAnyMisconductInsolvencyInefficiency : false, ProcessInstances[i].employeeOtherDetails.isEmployeeDiesInTerroristAttack = ProcessInstances[i].employeeOtherDetails.isEmployeeDiesInTerroristAttack ? ProcessInstances[i].employeeOtherDetails.isEmployeeDiesInTerroristAttack : false, ProcessInstances[i].employeeOtherDetails.isEmployeeDiesInAccidentalDeath = ProcessInstances[i].employeeOtherDetails.isEmployeeDiesInAccidentalDeath ? ProcessInstances[i].employeeOtherDetails.isEmployeeDiesInAccidentalDeath : false, ProcessInstances[i].employeeOtherDetails.isCommutationOpted = ProcessInstances[i].employeeOtherDetails.isCommutationOpted ? ProcessInstances[i].employeeOtherDetails.isCommutationOpted : false, ProcessInstances[i].employeeOtherDetails.reasonForRetirement = ProcessInstances[i].employeeOtherDetails.reasonForRetirement ? ProcessInstances[i].employeeOtherDetails.reasonForRetirement : null, ProcessInstances[i].employeeOtherDetails.isEligibleForPension = ProcessInstances[i].employeeOtherDetails.isEligibleForPension ? ProcessInstances[i].employeeOtherDetails.isEligibleForPension : false, ProcessInstances[i].employeeOtherDetails.isDuesPresent = ProcessInstances[i].employeeOtherDetails.isDuesPresent ? ProcessInstances[i].employeeOtherDetails.isDuesPresent : false, ProcessInstances[i].employeeOtherDetails.isDuesAmountDecided = ProcessInstances[i].employeeOtherDetails.isDuesAmountDecided ? ProcessInstances[i].employeeOtherDetails.isDuesAmountDecided : false, ProcessInstances[i].employeeOtherDetails.isTakenMonthlyPensionAndGratuity = ProcessInstances[i].employeeOtherDetails.isTakenMonthlyPensionAndGratuity ? ProcessInstances[i].employeeOtherDetails.isTakenMonthlyPensionAndGratuity : false, ProcessInstances[i].employeeOtherDetails.isTakenGratuityCommutationTerminalBenefit = ProcessInstances[i].employeeOtherDetails.isTakenGratuityCommutationTerminalBenefit ? ProcessInstances[i].employeeOtherDetails.isTakenGratuityCommutationTerminalBenefit : false, ProcessInstances[i].employeeOtherDetails.isTakenCompensationPensionAndGratuity = ProcessInstances[i].employeeOtherDetails.isTakenCompensationPensionAndGratuity ? ProcessInstances[i].employeeOtherDetails.isTakenCompensationPensionAndGratuity : false, ProcessInstances[i].employeeOtherDetails.diesInExtremistsDacoitsSmugglerAntisocialAttack = ProcessInstances[i].employeeOtherDetails.diesInExtremistsDacoitsSmugglerAntisocialAttack ? ProcessInstances[i].employeeOtherDetails.diesInExtremistsDacoitsSmugglerAntisocialAttack : false, ProcessInstances[i].employeeOtherDetails.isCompassionatePensionGranted = ProcessInstances[i].employeeOtherDetails.isCompassionatePensionGranted ? ProcessInstances[i].employeeOtherDetails.isCompassionatePensionGranted : false, ProcessInstances[i].employeeOtherDetails.totalNoPayLeavesMonths = ProcessInstances[i].employeeOtherDetails.totalNoPayLeavesMonths ? Number(ProcessInstances[i].employeeOtherDetails.totalNoPayLeavesMonths) : 0, ProcessInstances[i].employeeOtherDetails.totalNoPayLeavesYears = ProcessInstances[i].employeeOtherDetails.totalNoPayLeavesYears ? Number(ProcessInstances[i].employeeOtherDetails.totalNoPayLeavesYears) : 0, ProcessInstances[i].employeeOtherDetails.noDuesForAvailGovtAccomodation = ProcessInstances[i].employeeOtherDetails.noDuesForAvailGovtAccomodation ? ProcessInstances[i].employeeOtherDetails.noDuesForAvailGovtAccomodation : false, ProcessInstances[i].employeeOtherDetails.employeeGroup = ProcessInstances[i].employeeOtherDetails.employeeGroup ? ProcessInstances[i].employeeOtherDetails.employeeGroup : null;
                  ProcessInstances[i].employeeOtherDetails.employeeOtherDetailsAudit = {
                    employeeOtherDetailsAuditId: (0, _utils.uuidv1)()
                  };
                }

                //pensionCalculationDetails
                if (ProcessInstances[i].pensionCalculationDetails) {
                  ProcessInstances[i].pensionCalculationDetails.basicPensionSystem = ProcessInstances[i].pensionCalculationDetails.basicPensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.basicPensionSystem) : null, ProcessInstances[i].pensionCalculationDetails.pensionDeductionsSystem = ProcessInstances[i].pensionCalculationDetails.pensionDeductionsSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.pensionDeductionsSystem) : null, ProcessInstances[i].pensionCalculationDetails.additionalPensionSystem = ProcessInstances[i].pensionCalculationDetails.additionalPensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.additionalPensionSystem) : null, ProcessInstances[i].pensionCalculationDetails.commutedPensionSystem = ProcessInstances[i].pensionCalculationDetails.commutedPensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.commutedPensionSystem) : null, ProcessInstances[i].pensionCalculationDetails.commutedValueSystem = ProcessInstances[i].pensionCalculationDetails.commutedValueSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.commutedValueSystem) : null, ProcessInstances[i].pensionCalculationDetails.familyPensionISystem = ProcessInstances[i].pensionCalculationDetails.familyPensionISystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.familyPensionISystem) : null, ProcessInstances[i].pensionCalculationDetails.familyPensionIISystem = ProcessInstances[i].pensionCalculationDetails.familyPensionIISystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.familyPensionIISystem) : null, ProcessInstances[i].pensionCalculationDetails.dcrgSystem = ProcessInstances[i].pensionCalculationDetails.dcrgSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.dcrgSystem) : null, ProcessInstances[i].pensionCalculationDetails.netDeductionsSystem = ProcessInstances[i].pensionCalculationDetails.netDeductionsSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.netDeductionsSystem) : null, ProcessInstances[i].pensionCalculationDetails.finalCalculatedPensionSystem = ProcessInstances[i].pensionCalculationDetails.finalCalculatedPensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.finalCalculatedPensionSystem) : null, ProcessInstances[i].pensionCalculationDetails.interimReliefSystem = ProcessInstances[i].pensionCalculationDetails.interimReliefSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.interimReliefSystem) : null, ProcessInstances[i].pensionCalculationDetails.daSystem = ProcessInstances[i].pensionCalculationDetails.daSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.daSystem) : null, ProcessInstances[i].pensionCalculationDetails.nqsYearSystem = ProcessInstances[i].pensionCalculationDetails.nqsYearSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.nqsYearSystem) : null, ProcessInstances[i].pensionCalculationDetails.nqsMonthSystem = ProcessInstances[i].pensionCalculationDetails.nqsMonthSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.nqsMonthSystem) : null, ProcessInstances[i].pensionCalculationDetails.nqsDaySystem = ProcessInstances[i].pensionCalculationDetails.nqsDaySystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.nqsDaySystem) : null, ProcessInstances[i].pensionCalculationDetails.duesDeductionsSystem = ProcessInstances[i].pensionCalculationDetails.duesDeductionsSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.duesDeductionsSystem) : null, ProcessInstances[i].pensionCalculationDetails.compassionatePensionSystem = ProcessInstances[i].pensionCalculationDetails.compassionatePensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.compassionatePensionSystem) : null, ProcessInstances[i].pensionCalculationDetails.compensationPensionSystem = ProcessInstances[i].pensionCalculationDetails.compensationPensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.compensationPensionSystem) : null, ProcessInstances[i].pensionCalculationDetails.terminalBenefitSystem = ProcessInstances[i].pensionCalculationDetails.terminalBenefitSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.terminalBenefitSystem) : null, ProcessInstances[i].pensionCalculationDetails.finalCalculatedGratuitySystem = ProcessInstances[i].pensionCalculationDetails.finalCalculatedGratuitySystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.finalCalculatedGratuitySystem) : null, ProcessInstances[i].pensionCalculationDetails.familyPensionIStartDateSystem = (0, _search.intConversion)(ProcessInstances[i].pensionCalculationDetails.familyPensionIStartDateSystem), ProcessInstances[i].pensionCalculationDetails.familyPensionIEndDateSystem = (0, _search.intConversion)(ProcessInstances[i].pensionCalculationDetails.familyPensionIEndDateSystem), ProcessInstances[i].pensionCalculationDetails.familyPensionIIStartDateSystem = (0, _search.intConversion)(ProcessInstances[i].pensionCalculationDetails.familyPensionIIStartDateSystem), ProcessInstances[i].pensionCalculationDetails.exGratiaSystem = ProcessInstances[i].pensionCalculationDetails.exGratiaSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.exGratiaSystem) : null, ProcessInstances[i].pensionCalculationDetails.pensionerFamilyPensionSystem = ProcessInstances[i].pensionCalculationDetails.pensionerFamilyPensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.pensionerFamilyPensionSystem) : null, ProcessInstances[i].pensionCalculationDetails.totalPensionSystem = ProcessInstances[i].pensionCalculationDetails.totalPensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.totalPensionSystem) : null, ProcessInstances[i].pensionCalculationDetails.provisionalPensionSystem = ProcessInstances[i].pensionCalculationDetails.provisionalPensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.provisionalPensionSystem) : null, ProcessInstances[i].pensionCalculationDetails.interimReliefApplicable = ProcessInstances[i].pensionCalculationDetails.interimReliefApplicable;
                  ProcessInstances[i].pensionCalculationDetails.interimReliefExpression = ProcessInstances[i].pensionCalculationDetails.interimReliefExpression;
                  ProcessInstances[i].pensionCalculationDetails.basicPensionApplicable = ProcessInstances[i].pensionCalculationDetails.basicPensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.basicPensionExpression = ProcessInstances[i].pensionCalculationDetails.basicPensionExpression;
                  ProcessInstances[i].pensionCalculationDetails.provisionalPensionApplicable = ProcessInstances[i].pensionCalculationDetails.provisionalPensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.provisionalPensionExpression = ProcessInstances[i].pensionCalculationDetails.provisionalPensionExpression;
                  ProcessInstances[i].pensionCalculationDetails.compassionatePensionApplicable = ProcessInstances[i].pensionCalculationDetails.compassionatePensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.compassionatePensionExpression = ProcessInstances[i].pensionCalculationDetails.compassionatePensionExpression;
                  ProcessInstances[i].pensionCalculationDetails.compensationPensionApplicable = ProcessInstances[i].pensionCalculationDetails.compensationPensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.compensationPensionExpression = ProcessInstances[i].pensionCalculationDetails.compensationPensionExpression;
                  ProcessInstances[i].pensionCalculationDetails.commutedPensionApplicable = ProcessInstances[i].pensionCalculationDetails.commutedPensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.commutedPensionExpression = ProcessInstances[i].pensionCalculationDetails.commutedPensionExpression;
                  ProcessInstances[i].pensionCalculationDetails.familyPensionIApplicable = ProcessInstances[i].pensionCalculationDetails.familyPensionIApplicable;
                  ProcessInstances[i].pensionCalculationDetails.familyPensionIExpression = ProcessInstances[i].pensionCalculationDetails.familyPensionIExpression;
                  ProcessInstances[i].pensionCalculationDetails.familyPensionIIApplicable = ProcessInstances[i].pensionCalculationDetails.familyPensionIIApplicable;
                  ProcessInstances[i].pensionCalculationDetails.familyPensionIIExpression = ProcessInstances[i].pensionCalculationDetails.familyPensionIIExpression;
                  ProcessInstances[i].pensionCalculationDetails.daApplicable = ProcessInstances[i].pensionCalculationDetails.daApplicable;
                  ProcessInstances[i].pensionCalculationDetails.daExpression = ProcessInstances[i].pensionCalculationDetails.daExpression;
                  ProcessInstances[i].pensionCalculationDetails.additionalPensionApplicable = ProcessInstances[i].pensionCalculationDetails.additionalPensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.additionalPensionExpression = ProcessInstances[i].pensionCalculationDetails.additionalPensionExpression;
                  ProcessInstances[i].pensionCalculationDetails.totalPensionApplicable = ProcessInstances[i].pensionCalculationDetails.totalPensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.totalPensionExpression = ProcessInstances[i].pensionCalculationDetails.totalPensionExpression;
                  ProcessInstances[i].pensionCalculationDetails.pensionDeductionsApplicable = ProcessInstances[i].pensionCalculationDetails.pensionDeductionsApplicable;
                  ProcessInstances[i].pensionCalculationDetails.pensionDeductionsExpression = ProcessInstances[i].pensionCalculationDetails.pensionDeductionsExpression;
                  ProcessInstances[i].pensionCalculationDetails.netDeductionsApplicable = ProcessInstances[i].pensionCalculationDetails.netDeductionsApplicable;
                  ProcessInstances[i].pensionCalculationDetails.netDeductionsExpression = ProcessInstances[i].pensionCalculationDetails.netDeductionsExpression;
                  ProcessInstances[i].pensionCalculationDetails.finalCalculatedPensionApplicable = ProcessInstances[i].pensionCalculationDetails.finalCalculatedPensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.finalCalculatedPensionExpression = ProcessInstances[i].pensionCalculationDetails.finalCalculatedPensionExpression;
                  ProcessInstances[i].pensionCalculationDetails.commutationValueApplicable = ProcessInstances[i].pensionCalculationDetails.commutationValueApplicable;
                  ProcessInstances[i].pensionCalculationDetails.commutationValueExpression = ProcessInstances[i].pensionCalculationDetails.commutationValueExpression;
                  ProcessInstances[i].pensionCalculationDetails.dcrgApplicable = ProcessInstances[i].pensionCalculationDetails.dcrgApplicable;
                  ProcessInstances[i].pensionCalculationDetails.dcrgExpression = ProcessInstances[i].pensionCalculationDetails.dcrgExpression;
                  ProcessInstances[i].pensionCalculationDetails.terminalBenefitApplicable = ProcessInstances[i].pensionCalculationDetails.terminalBenefitApplicable;
                  ProcessInstances[i].pensionCalculationDetails.terminalBenefitExpression = ProcessInstances[i].pensionCalculationDetails.terminalBenefitExpression;
                  ProcessInstances[i].pensionCalculationDetails.duesDeductionsApplicable = ProcessInstances[i].pensionCalculationDetails.duesDeductionsApplicable;
                  ProcessInstances[i].pensionCalculationDetails.duesDeductionsExpression = ProcessInstances[i].pensionCalculationDetails.duesDeductionsExpression;
                  ProcessInstances[i].pensionCalculationDetails.finalCalculatedGratuityApplicable = ProcessInstances[i].pensionCalculationDetails.finalCalculatedGratuityApplicable;
                  ProcessInstances[i].pensionCalculationDetails.finalCalculatedGratuityExpression = ProcessInstances[i].pensionCalculationDetails.finalCalculatedGratuityExpression;
                  ProcessInstances[i].pensionCalculationDetails.exGratiaApplicable = ProcessInstances[i].pensionCalculationDetails.exGratiaApplicable;
                  ProcessInstances[i].pensionCalculationDetails.exGratiaExpression = ProcessInstances[i].pensionCalculationDetails.exGratiaExpression;
                  ProcessInstances[i].pensionCalculationDetails.pensionerFamilyPensionApplicable = ProcessInstances[i].pensionCalculationDetails.pensionerFamilyPensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.pensionerFamilyPensionExpression = ProcessInstances[i].pensionCalculationDetails.pensionerFamilyPensionExpression;

                  ProcessInstances[i].pensionCalculationDetails.invalidPensionSystem = ProcessInstances[i].pensionCalculationDetails.invalidPensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.invalidPensionSystem) : null;
                  ProcessInstances[i].pensionCalculationDetails.woundExtraordinaryPensionSystem = ProcessInstances[i].pensionCalculationDetails.woundExtraordinaryPensionSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.woundExtraordinaryPensionSystem) : null;
                  ProcessInstances[i].pensionCalculationDetails.attendantAllowanceSystem = ProcessInstances[i].pensionCalculationDetails.attendantAllowanceSystem != null ? Number(ProcessInstances[i].pensionCalculationDetails.attendantAllowanceSystem) : null;

                  ProcessInstances[i].pensionCalculationDetails.invalidPensionApplicable = ProcessInstances[i].pensionCalculationDetails.invalidPensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.invalidPensionExpression = ProcessInstances[i].pensionCalculationDetails.invalidPensionExpression;

                  ProcessInstances[i].pensionCalculationDetails.woundExtraordinaryPensionApplicable = ProcessInstances[i].pensionCalculationDetails.woundExtraordinaryPensionApplicable;
                  ProcessInstances[i].pensionCalculationDetails.woundExtraordinaryPensionExpression = ProcessInstances[i].pensionCalculationDetails.woundExtraordinaryPensionExpression;

                  ProcessInstances[i].pensionCalculationDetails.attendantAllowanceApplicable = ProcessInstances[i].pensionCalculationDetails.attendantAllowanceApplicable;
                  ProcessInstances[i].pensionCalculationDetails.attendantAllowanceExpression = ProcessInstances[i].pensionCalculationDetails.attendantAllowanceExpression;

                  ProcessInstances[i].pensionCalculationDetails.gqsYearSystem = Number(ProcessInstances[i].pensionCalculationDetails.gqsYearSystem);
                  ProcessInstances[i].pensionCalculationDetails.gqsMonthSystem = Number(ProcessInstances[i].pensionCalculationDetails.gqsMonthSystem);
                  ProcessInstances[i].pensionCalculationDetails.gqsDaySystem = Number(ProcessInstances[i].pensionCalculationDetails.gqsDaySystem);

                  ProcessInstances[i].pensionCalculationDetails.pensionCalculationDetailsAudit = {
                    pensionCalculationDetailsAuditId: (0, _utils.uuidv1)() //eg_pension_calculation_details_audit table only, rest of the data for pensionCalculationDetails object comes from ui
                  };
                }

                //pensionCalculationUpdateDetails
                if (ProcessInstances[i].pensionCalculationUpdateDetails) {
                  ProcessInstances[i].pensionCalculationUpdateDetails.basicPensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.basicPensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.basicPensionVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.pensionDeductionsVerified = ProcessInstances[i].pensionCalculationUpdateDetails.pensionDeductionsVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.pensionDeductionsVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.additionalPensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.additionalPensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.additionalPensionVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.commutedPensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.commutedPensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.commutedPensionVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.commutedValueVerified = ProcessInstances[i].pensionCalculationUpdateDetails.commutedValueVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.commutedValueVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIVerified = ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIIVerified = ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIIVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIIVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.dcrgVerified = ProcessInstances[i].pensionCalculationUpdateDetails.dcrgVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.dcrgVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.netDeductionsVerified = ProcessInstances[i].pensionCalculationUpdateDetails.netDeductionsVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.netDeductionsVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.finalCalculatedPensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.finalCalculatedPensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.finalCalculatedPensionVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.interimReliefVerified = ProcessInstances[i].pensionCalculationUpdateDetails.interimReliefVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.interimReliefVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.daVerified = ProcessInstances[i].pensionCalculationUpdateDetails.daVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.daVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.nqsYearVerified = ProcessInstances[i].pensionCalculationUpdateDetails.nqsYearVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.nqsYearVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.nqsMonthVerified = ProcessInstances[i].pensionCalculationUpdateDetails.nqsMonthVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.nqsMonthVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.nqsDayVerified = ProcessInstances[i].pensionCalculationUpdateDetails.nqsDayVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.nqsDayVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.duesDeductionsVerified = ProcessInstances[i].pensionCalculationUpdateDetails.duesDeductionsVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.duesDeductionsVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.compassionatePensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.compassionatePensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.compassionatePensionVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.compensationPensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.compensationPensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.compensationPensionVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.terminalBenefitVerified = ProcessInstances[i].pensionCalculationUpdateDetails.terminalBenefitVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.terminalBenefitVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.finalCalculatedGratuityVerified = ProcessInstances[i].pensionCalculationUpdateDetails.finalCalculatedGratuityVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.finalCalculatedGratuityVerified) : null, ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIStartDateVerified = (0, _search.intConversion)(ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIStartDateVerified), ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIEndDateVerified = (0, _search.intConversion)(ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIEndDateVerified);
                  ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIIStartDateVerified = (0, _search.intConversion)(ProcessInstances[i].pensionCalculationUpdateDetails.familyPensionIIStartDateVerified);
                  ProcessInstances[i].pensionCalculationUpdateDetails.exGratiaVerified = ProcessInstances[i].pensionCalculationUpdateDetails.exGratiaVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.exGratiaVerified) : null;
                  ProcessInstances[i].pensionCalculationUpdateDetails.pensionerFamilyPensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.pensionerFamilyPensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.pensionerFamilyPensionVerified) : null;
                  ProcessInstances[i].pensionCalculationUpdateDetails.totalPensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.totalPensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.totalPensionVerified) : null;
                  ProcessInstances[i].pensionCalculationUpdateDetails.provisionalPensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.provisionalPensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.provisionalPensionVerified) : null;

                  ProcessInstances[i].pensionCalculationUpdateDetails.invalidPensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.invalidPensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.invalidPensionVerified) : null;
                  ProcessInstances[i].pensionCalculationUpdateDetails.woundExtraordinaryPensionVerified = ProcessInstances[i].pensionCalculationUpdateDetails.woundExtraordinaryPensionVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.woundExtraordinaryPensionVerified) : null;
                  ProcessInstances[i].pensionCalculationUpdateDetails.attendantAllowanceVerified = ProcessInstances[i].pensionCalculationUpdateDetails.attendantAllowanceVerified != null ? Number(ProcessInstances[i].pensionCalculationUpdateDetails.attendantAllowanceVerified) : null;

                  ProcessInstances[i].pensionCalculationUpdateDetails.gqsYearVerified = Number(ProcessInstances[i].pensionCalculationUpdateDetails.gqsYearVerified);
                  ProcessInstances[i].pensionCalculationUpdateDetails.gqsMonthVerified = Number(ProcessInstances[i].pensionCalculationUpdateDetails.gqsMonthVerified);
                  ProcessInstances[i].pensionCalculationUpdateDetails.gqsDayVerified = Number(ProcessInstances[i].pensionCalculationUpdateDetails.gqsDayVerified);
                }

                //dependents          
                if (ProcessInstances[i].dependents) {
                  for (j = 0; j < ProcessInstances[i].dependents.length; j++) {
                    dependents.push({
                      dependentId: (0, _utils.uuidv1)(),
                      tenantId: ProcessInstances[i].tenantId,
                      pensionEmployeeId: ProcessInstances[i].employee.pensionEmployeeId,
                      name: ProcessInstances[i].dependents[j].name,
                      dob: Number(ProcessInstances[i].dependents[j].dob),
                      address: ProcessInstances[i].dependents[j].address,
                      mobileNumber: ProcessInstances[i].dependents[j].mobileNumber,
                      relationship: ProcessInstances[i].dependents[j].relationship,
                      isDisabled: ProcessInstances[i].dependents[j].isDisabled ? ProcessInstances[i].dependents[j].isDisabled : false,
                      maritalStatus: ProcessInstances[i].dependents[j].maritalStatus ? ProcessInstances[i].dependents[j].maritalStatus : null,
                      isHollyDependent: ProcessInstances[i].dependents[j].isHollyDependent ? ProcessInstances[i].dependents[j].isHollyDependent : false,
                      noSpouseNoChildren: ProcessInstances[i].dependents[j].noSpouseNoChildren ? ProcessInstances[i].dependents[j].noSpouseNoChildren : false,
                      isGrandChildFromDeceasedSon: ProcessInstances[i].dependents[j].isGrandChildFromDeceasedSon ? ProcessInstances[i].dependents[j].isGrandChildFromDeceasedSon : false,
                      isEligibleForGratuity: ProcessInstances[i].dependents[j].isEligibleForGratuity ? ProcessInstances[i].dependents[j].isEligibleForGratuity : false,
                      isEligibleForPension: ProcessInstances[i].dependents[j].isEligibleForPension ? ProcessInstances[i].dependents[j].isEligibleForPension : false,
                      gratuityPercentage: ProcessInstances[i].dependents[j].gratuityPercentage ? Number(ProcessInstances[i].dependents[j].gratuityPercentage) : 0,
                      bankAccountNumber: ProcessInstances[i].dependents[j].bankAccountNumber,
                      bankDetails: ProcessInstances[i].dependents[j].bankDetails,
                      bankCode: ProcessInstances[i].dependents[j].bankCode,
                      bankIfsc: ProcessInstances[i].dependents[j].bankIfsc,
                      active: true,
                      dependentAudit: {
                        dependentAuditId: (0, _utils.uuidv1)()
                      },
                      auditDetails: auditDetails
                    });
                  }
                }

                ProcessInstances[i].documents = documents;
                ProcessInstances[i].dependents = dependents;
                ProcessInstances[i].auditDetails = auditDetails;
              }
            }

            request.ProcessInstances = ProcessInstances;
            return _context.abrupt("return", request);

          case 93:
          case "end":
            return _context.stop();
        }
      }
    }, _callee, undefined);
  }));

  return function addUUIDAndAuditDetails(_x) {
    return _ref.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsClaimReleaseWorkflow = exports.addUUIDAndAuditDetailsClaimReleaseWorkflow = function () {
  var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(request) {
    var ProcessInstances, RequestInfo, createdBy, createdDate, auditDetails, i, workflowHeader;
    return _regenerator2.default.wrap(function _callee2$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            ProcessInstances = request.ProcessInstances, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };

            for (i = 0; i < ProcessInstances.length; i++) {
              workflowHeader = {
                workflowHeaderAudit: {
                  workflowHeaderAuditId: (0, _utils.uuidv1)()
                }
              };

              ProcessInstances[i].workflowHeader = workflowHeader;
              ProcessInstances[i].auditDetails = auditDetails;
            }
            request.ProcessInstances = ProcessInstances;
            return _context2.abrupt("return", request);

          case 7:
          case "end":
            return _context2.stop();
        }
      }
    }, _callee2, undefined);
  }));

  return function addUUIDAndAuditDetailsClaimReleaseWorkflow(_x3) {
    return _ref2.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsCreateRevisedPension = exports.addUUIDAndAuditDetailsCreateRevisedPension = function () {
  var _ref3 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee3(request) {
    var ProcessInstances, RequestInfo, createdBy, createdDate, auditDetails, i, pensionRevision, j;
    return _regenerator2.default.wrap(function _callee3$(_context3) {
      while (1) {
        switch (_context3.prev = _context3.next) {
          case 0:
            ProcessInstances = request.ProcessInstances, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };


            for (i = 0; i < ProcessInstances.length; i++) {
              pensionRevision = ProcessInstances[i].pensionRevision; //new revised pension

              for (j = 0; j < pensionRevision.length; j++) {
                pensionRevision[j].tenantId = ProcessInstances[0].tenantId;
                pensionRevision[j].pensionerId = ProcessInstances[0].pensioner.pensionerId, pensionRevision[j].pensionRevisionId = (0, _utils.uuidv1)();
                pensionRevision[j].effectiveStartYear = Number(pensionRevision[j].effectiveStartYear);
                pensionRevision[j].effectiveStartMonth = Number(pensionRevision[j].effectiveStartMonth);
                pensionRevision[j].effectiveEndYear = null;
                pensionRevision[j].effectiveEndMonth = null;
                pensionRevision[j].pensionArrear = Number(pensionRevision[j].pensionArrear);
                //revisedPension[j].medicalRelief=Number( revisedPension[j].medicalRelief);
                pensionRevision[j].fma = Number(pensionRevision[j].fma);
                pensionRevision[j].miscellaneous = Number(pensionRevision[j].miscellaneous);
                pensionRevision[j].overPayment = Number(pensionRevision[j].overPayment);
                pensionRevision[j].incomeTax = Number(pensionRevision[j].incomeTax);
                pensionRevision[j].cess = Number(pensionRevision[j].cess);
                pensionRevision[j].basicPension = Number(pensionRevision[j].basicPension);
                pensionRevision[j].da = Number(pensionRevision[j].da);
                pensionRevision[j].commutedPension = Number(pensionRevision[j].commutedPension);
                pensionRevision[j].netDeductions = Number(pensionRevision[j].netDeductions);
                pensionRevision[j].finalCalculatedPension = Number(pensionRevision[j].finalCalculatedPension);
                pensionRevision[j].additionalPension = Number(pensionRevision[j].additionalPension);
                pensionRevision[j].interimRelief = Number(pensionRevision[j].interimRelief);
                pensionRevision[j].totalPension = Number(pensionRevision[j].totalPension);
                pensionRevision[j].pensionDeductions = Number(pensionRevision[j].pensionDeductions);
                pensionRevision[j].woundExtraordinaryPension = pensionRevision[j].woundExtraordinaryPension != null ? Number(pensionRevision[j].woundExtraordinaryPension) : null;
                pensionRevision[j].attendantAllowance = pensionRevision[j].attendantAllowance != null ? Number(pensionRevision[j].attendantAllowance) : null;
                pensionRevision[j].pensionerFinalCalculatedBenefitId = ProcessInstances[i].pensioner.pensionerFinalCalculatedBenefitId;
                pensionRevision[j].pensionRevisionAuditId = (0, _utils.uuidv1)();
                pensionRevision[j].auditDetails = auditDetails;
              }
              ProcessInstances[i].pensionRevision = pensionRevision;
            }
            request.ProcessInstances = ProcessInstances;
            return _context3.abrupt("return", request);

          case 7:
          case "end":
            return _context3.stop();
        }
      }
    }, _callee3, undefined);
  }));

  return function addUUIDAndAuditDetailsCreateRevisedPension(_x4) {
    return _ref3.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsCloseLastRevisedPension = exports.addUUIDAndAuditDetailsCloseLastRevisedPension = function () {
  var _ref4 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee4(request) {
    var ProcessInstances, RequestInfo, createdBy, createdDate, auditDetails, i, lastPensionRevision, pensionRevision, effectiveStartYear, effectiveStartMonth, lastRevisedPensionEffectiveEndYear, lastRevisedPensionEffectiveEndMonth, j;
    return _regenerator2.default.wrap(function _callee4$(_context4) {
      while (1) {
        switch (_context4.prev = _context4.next) {
          case 0:
            ProcessInstances = request.ProcessInstances, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };


            for (i = 0; i < ProcessInstances.length; i++) {
              lastPensionRevision = ProcessInstances[i].lastPensionRevision; //last revised pension

              pensionRevision = ProcessInstances[i].pensionRevision; //new or to be updated revised pension

              effectiveStartYear = Number(pensionRevision[0].effectiveStartYear);
              effectiveStartMonth = Number(pensionRevision[0].effectiveStartMonth);
              lastRevisedPensionEffectiveEndYear = void 0;
              lastRevisedPensionEffectiveEndMonth = void 0;

              if (effectiveStartMonth == 1) {
                lastRevisedPensionEffectiveEndYear = effectiveStartYear - 1;
                lastRevisedPensionEffectiveEndMonth = 12;
              } else {
                lastRevisedPensionEffectiveEndYear = effectiveStartYear;
                lastRevisedPensionEffectiveEndMonth = effectiveStartMonth - 1;
              }

              for (j = 0; j < lastPensionRevision.length; j++) {
                lastPensionRevision[j].effectiveEndYear = lastRevisedPensionEffectiveEndYear;
                lastPensionRevision[j].effectiveEndMonth = lastRevisedPensionEffectiveEndMonth;
                lastPensionRevision[j].pensionRevisionAuditId = (0, _utils.uuidv1)();
                lastPensionRevision[j].auditDetails = auditDetails;
              }
              ProcessInstances[i].lastPensionRevision = lastPensionRevision;
            }
            request.ProcessInstances = ProcessInstances;
            return _context4.abrupt("return", request);

          case 7:
          case "end":
            return _context4.stop();
        }
      }
    }, _callee4, undefined);
  }));

  return function addUUIDAndAuditDetailsCloseLastRevisedPension(_x5) {
    return _ref4.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsUpdateRevisedPension = exports.addUUIDAndAuditDetailsUpdateRevisedPension = function () {
  var _ref5 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee5(request) {
    var ProcessInstances, RequestInfo, createdBy, createdDate, auditDetails, i, pensionRevision, j;
    return _regenerator2.default.wrap(function _callee5$(_context5) {
      while (1) {
        switch (_context5.prev = _context5.next) {
          case 0:
            ProcessInstances = request.ProcessInstances, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };


            for (i = 0; i < ProcessInstances.length; i++) {
              pensionRevision = ProcessInstances[i].pensionRevision; //last revised pension

              for (j = 0; j < pensionRevision.length; j++) {
                pensionRevision[j].effectiveStartYear = Number(pensionRevision[j].effectiveStartYear);
                pensionRevision[j].effectiveStartMonth = Number(pensionRevision[j].effectiveStartMonth);
                pensionRevision[j].effectiveEndYear = null;
                pensionRevision[j].effectiveEndMonth = null;
                pensionRevision[j].pensionArrear = Number(pensionRevision[j].pensionArrear);
                //pensionRevision[j].medicalRelief=Number( pensionRevision[j].medicalRelief);
                pensionRevision[j].fma = Number(pensionRevision[j].fma);
                pensionRevision[j].miscellaneous = Number(pensionRevision[j].miscellaneous);
                pensionRevision[j].overPayment = Number(pensionRevision[j].overPayment);
                pensionRevision[j].incomeTax = Number(pensionRevision[j].incomeTax);
                pensionRevision[j].cess = Number(pensionRevision[j].cess);
                pensionRevision[j].basicPension = Number(pensionRevision[j].basicPension);
                pensionRevision[j].da = Number(pensionRevision[j].da);
                pensionRevision[j].commutedPension = Number(pensionRevision[j].commutedPension);
                pensionRevision[j].netDeductions = Number(pensionRevision[j].netDeductions);
                pensionRevision[j].finalCalculatedPension = Number(pensionRevision[j].finalCalculatedPension);
                pensionRevision[j].additionalPension = Number(pensionRevision[j].additionalPension);
                pensionRevision[j].interimRelief = Number(pensionRevision[j].interimRelief);
                pensionRevision[j].totalPension = Number(pensionRevision[j].totalPension);
                pensionRevision[j].pensionDeductions = Number(pensionRevision[j].pensionDeductions);
                pensionRevision[j].woundExtraordinaryPension = pensionRevision[j].woundExtraordinaryPension != null ? Number(pensionRevision[j].woundExtraordinaryPension) : null;
                pensionRevision[j].attendantAllowance = pensionRevision[j].attendantAllowance != null ? Number(pensionRevision[j].attendantAllowance) : null;
                pensionRevision[j].pensionRevisionAuditId = (0, _utils.uuidv1)();
                pensionRevision[j].auditDetails = auditDetails;
              }
              ProcessInstances[i].pensionRevision = pensionRevision;
            }
            request.ProcessInstances = ProcessInstances;
            return _context5.abrupt("return", request);

          case 7:
          case "end":
            return _context5.stop();
        }
      }
    }, _callee5, undefined);
  }));

  return function addUUIDAndAuditDetailsUpdateRevisedPension(_x6) {
    return _ref5.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsCreateMonthlyPensionRegister = exports.addUUIDAndAuditDetailsCreateMonthlyPensionRegister = function () {
  var _ref6 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee6(request) {
    var ProcessInstances, RequestInfo, createdBy, createdDate, auditDetails, i, j;
    return _regenerator2.default.wrap(function _callee6$(_context6) {
      while (1) {
        switch (_context6.prev = _context6.next) {
          case 0:
            ProcessInstances = request.ProcessInstances, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };


            for (i = 0; i < ProcessInstances.length; i++) {

              for (j = 0; j < ProcessInstances[i].pensionRegister.length; j++) {
                ProcessInstances[i].pensionRegister[j].pensionRegisterId = (0, _utils.uuidv1)();
                ProcessInstances[i].pensionRegister[j].pensionRegisterAuditId = (0, _utils.uuidv1)();
                ProcessInstances[i].pensionRegister[j].auditDetails = auditDetails;
              }

              ProcessInstances[i].auditDetails = auditDetails;
            }
            request.ProcessInstances = ProcessInstances;
            return _context6.abrupt("return", request);

          case 7:
          case "end":
            return _context6.stop();
        }
      }
    }, _callee6, undefined);
  }));

  return function addUUIDAndAuditDetailsCreateMonthlyPensionRegister(_x7) {
    return _ref6.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsCloseWorkflow = exports.addUUIDAndAuditDetailsCloseWorkflow = function () {
  var _ref7 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee7(request, workflowSearchResponse) {
    var ProcessInstances, RequestInfo, createdBy, createdDate, auditDetails, i, effectiveStartDate, provisionalPensionVerified, compassionatePensionVerified, compensationPensionVerified, invalidPensionVerified;
    return _regenerator2.default.wrap(function _callee7$(_context7) {
      while (1) {
        switch (_context7.prev = _context7.next) {
          case 0:
            ProcessInstances = request.ProcessInstances, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };
            i = 0;

          case 5:
            if (!(i < ProcessInstances.length)) {
              _context7.next = 51;
              break;
            }

            ProcessInstances[i].workflowHeaderId = workflowSearchResponse.ProcessInstances[0].workflowHeader.workflowHeaderId;
            ProcessInstances[i].pensionEmployeeId = workflowSearchResponse.ProcessInstances[0].employee.pensionEmployeeId;
            ProcessInstances[i].pensionerId = (0, _utils.uuidv1)(); //eg_pension_pensioner
            ProcessInstances[i].pensionerAuditId = (0, _utils.uuidv1)(); //eg_pension_pensioner_audit
            ProcessInstances[i].pensionerFinalCalculatedBenefitId = (0, _utils.uuidv1)(); //eg_pension_pensioner_final_calculated_benefit
            ProcessInstances[i].pensionRevisionId = (0, _utils.uuidv1)(); //eg_pension_revision
            ProcessInstances[i].pensionRevisionAuditId = (0, _utils.uuidv1)(); //eg_pension_revision_audit
            //ProcessInstances[i].workflowHeaderAuditId=uuidv1();//eg_pension_workflow_header_audit
            ProcessInstances[i].employeeOtherDetailsAuditId = (0, _utils.uuidv1)(); //eg_pension_employee_other_details_audit
            ProcessInstances[i].pensionerApplicationDetailsId = (0, _utils.uuidv1)(); //eg_pension_pensioner_application_details
            ProcessInstances[i].auditDetails = auditDetails;
            ProcessInstances[i].dependentId = null;
            ProcessInstances[i].effectiveEndYear = null;
            ProcessInstances[i].effectiveEndMonth = null;
            ProcessInstances[i].pensionArrear = workflowSearchResponse.ProcessInstances[0].employeeOtherDetails.pensionArrear;
            //ProcessInstances[i].medicalRelief=workflowSearchResponse.ProcessInstances[0].employeeOtherDetails.medicalRelief; 
            ProcessInstances[i].fma = workflowSearchResponse.ProcessInstances[0].employeeOtherDetails.fma;
            ProcessInstances[i].miscellaneous = workflowSearchResponse.ProcessInstances[0].employeeOtherDetails.miscellaneous;
            ProcessInstances[i].overPayment = workflowSearchResponse.ProcessInstances[0].employeeOtherDetails.overPayment;
            ProcessInstances[i].incomeTax = workflowSearchResponse.ProcessInstances[0].employeeOtherDetails.incomeTax;
            ProcessInstances[i].cess = workflowSearchResponse.ProcessInstances[0].employeeOtherDetails.cess;
            _context7.next = 27;
            return (0, _utils.addIDGenId)(RequestInfo, [{
              idName: _envVariables2.default.EGOV_IDGEN_PENSION_PENSIONER_NUMBER_ID_NAME,
              tenantId: ProcessInstances[i].tenantId,
              format: _envVariables2.default.EGOV_PN_APPLICATION_FORMATE,
              count: 1
            }]);

          case 27:
            ProcessInstances[i].pensionerNumber = _context7.sent;
            effectiveStartDate = new Date();
            _context7.t0 = workflowSearchResponse.ProcessInstances[0].businessService;
            _context7.next = _context7.t0 === _envVariables2.default.EGOV_PENSION_RRP_BUSINESS_SERVICE ? 32 : _context7.t0 === _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE ? 39 : _context7.t0 === _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE ? 42 : 45;
            break;

          case 32:
            provisionalPensionVerified = workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.provisionalPensionVerified != null ? Number(workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.provisionalPensionVerified) : 0;
            compassionatePensionVerified = workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.compassionatePensionVerified != null ? Number(workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.compassionatePensionVerified) : 0;
            compensationPensionVerified = workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.compensationPensionVerified != null ? Number(workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.compensationPensionVerified) : 0;
            invalidPensionVerified = workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.invalidPensionVerified != null ? Number(workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.invalidPensionVerified) : 0;

            if (provisionalPensionVerified > 0) {
              ProcessInstances[i].basicPension = provisionalPensionVerified;
            } else if (compassionatePensionVerified > 0) {
              ProcessInstances[i].basicPension = compassionatePensionVerified;
            } else if (compensationPensionVerified > 0) {
              ProcessInstances[i].basicPension = compensationPensionVerified;
            } else if (compensationPensionVerified > 0) {
              ProcessInstances[i].basicPension = compensationPensionVerified;
            } else if (invalidPensionVerified > 0) {
              ProcessInstances[i].basicPension = invalidPensionVerified;
            } else {
              ProcessInstances[i].basicPension = workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.basicPensionVerified != null ? Number(workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.basicPensionVerified) : 0;
            }
            effectiveStartDate = new Date((0, _utils.epochToYmd)((0, _search.intConversion)(workflowSearchResponse.ProcessInstances[0].employee.dateOfRetirement)));
            return _context7.abrupt("break", 45);

          case 39:
            ProcessInstances[i].basicPension = workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.familyPensionIVerified;
            effectiveStartDate = new Date((0, _utils.epochToYmd)((0, _search.intConversion)(workflowSearchResponse.ProcessInstances[0].employee.dateOfDeath)));
            return _context7.abrupt("break", 45);

          case 42:
            ProcessInstances[i].basicPension = workflowSearchResponse.ProcessInstances[0].pensionCalculationUpdateDetails.pensionerFamilyPensionVerified;
            effectiveStartDate = new Date((0, _utils.epochToYmd)((0, _search.intConversion)(workflowSearchResponse.ProcessInstances[0].employee.dateOfDeath)));
            return _context7.abrupt("break", 45);

          case 45:

            effectiveStartDate = new Date(effectiveStartDate.getFullYear(), effectiveStartDate.getMonth(), effectiveStartDate.getDate() + 1);
            ProcessInstances[i].effectiveStartYear = effectiveStartDate.getFullYear();
            ProcessInstances[i].effectiveStartMonth = effectiveStartDate.getMonth() + 1;

          case 48:
            i++;
            _context7.next = 5;
            break;

          case 51:
            request.ProcessInstances = ProcessInstances;

            return _context7.abrupt("return", request);

          case 53:
          case "end":
            return _context7.stop();
        }
      }
    }, _callee7, undefined);
  }));

  return function addUUIDAndAuditDetailsCloseWorkflow(_x8, _x9) {
    return _ref7.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsDisabilityRegistration = exports.addUUIDAndAuditDetailsDisabilityRegistration = function () {
  var _ref8 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee8(request) {
    var Employees, RequestInfo, createdBy, createdDate, auditDetails, i;
    return _regenerator2.default.wrap(function _callee8$(_context8) {
      while (1) {
        switch (_context8.prev = _context8.next) {
          case 0:
            Employees = request.Employees, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };


            for (i = 0; i < Employees.length; i++) {
              Employees[i].disabilityRegisterId = (0, _utils.uuidv1)();
              Employees[i].disabilityRegisterAuditId = (0, _utils.uuidv1)();
              Employees[i].auditDetails = auditDetails;
            }
            request.Employees = Employees;
            return _context8.abrupt("return", request);

          case 7:
          case "end":
            return _context8.stop();
        }
      }
    }, _callee8, undefined);
  }));

  return function addUUIDAndAuditDetailsDisabilityRegistration(_x10) {
    return _ref8.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsPensionerPensionDiscontinuation = exports.addUUIDAndAuditDetailsPensionerPensionDiscontinuation = function () {
  var _ref9 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee9(request) {
    var ProcessInstances, RequestInfo, createdBy, createdDate, auditDetails, i;
    return _regenerator2.default.wrap(function _callee9$(_context9) {
      while (1) {
        switch (_context9.prev = _context9.next) {
          case 0:
            ProcessInstances = request.ProcessInstances, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };


            for (i = 0; i < ProcessInstances.length; i++) {
              ProcessInstances[i].auditDetails = auditDetails;
            }

            request.ProcessInstances = ProcessInstances;
            return _context9.abrupt("return", request);

          case 7:
          case "end":
            return _context9.stop();
        }
      }
    }, _callee9, undefined);
  }));

  return function addUUIDAndAuditDetailsPensionerPensionDiscontinuation(_x11) {
    return _ref9.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsInitiateReComputation = exports.addUUIDAndAuditDetailsInitiateReComputation = function () {
  var _ref10 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee10(request) {
    var ProcessInstances, RequestInfo, createdBy, createdDate, auditDetails, i, idName, format, applicationNumber, workflowHeader, recomputationRegister, employeeOtherDetails, pensionCalculationDetails;
    return _regenerator2.default.wrap(function _callee10$(_context10) {
      while (1) {
        switch (_context10.prev = _context10.next) {
          case 0:
            ProcessInstances = request.ProcessInstances, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };
            i = 0;

          case 5:
            if (!(i < ProcessInstances.length)) {
              _context10.next = 36;
              break;
            }

            //let applicationFormat;
            idName = "";
            format = "";
            _context10.t0 = ProcessInstances[i].businessService;
            _context10.next = _context10.t0 === _envVariables2.default.EGOV_PENSION_RRP_BUSINESS_SERVICE ? 11 : _context10.t0 === _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE ? 14 : _context10.t0 === _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE ? 17 : 20;
            break;

          case 11:
            //applicationFormat=envVariables.EGOV_RR_APPLICATION_FORMATE;            
            idName = _envVariables2.default.EGOV_IDGEN_PENSION_RRP_APPLICATION_NUMBER_ID_NAME;
            format = _envVariables2.default.EGOV_RR_APPLICATION_FORMATE;
            return _context10.abrupt("break", 20);

          case 14:
            //applicationFormat=envVariables.EGOV_DE_APPLICATION_FORMATE;              
            idName = _envVariables2.default.EGOV_IDGEN_PENSION_DOE_APPLICATION_NUMBER_ID_NAME;
            format = _envVariables2.default.EGOV_DE_APPLICATION_FORMATE;
            return _context10.abrupt("break", 20);

          case 17:
            //applicationFormat=envVariables.EGOV_DP_APPLICATION_FORMATE;          
            idName = _envVariables2.default.EGOV_IDGEN_PENSION_DOP_APPLICATION_NUMBER_ID_NAME;
            format = _envVariables2.default.EGOV_DP_APPLICATION_FORMATE;
            return _context10.abrupt("break", 20);

          case 20:
            _context10.next = 22;
            return (0, _utils.addIDGenId)(RequestInfo, [{
              idName: idName,
              tenantId: ProcessInstances[i].tenantId,
              format: format,
              count: 1
            }]);

          case 22:
            applicationNumber = _context10.sent;

            ProcessInstances[i].businessId = applicationNumber;

            //workflowHeader object
            workflowHeader = {
              workflowHeaderId: (0, _utils.uuidv1)(),
              active: true,
              workflowHeaderAudit: {
                workflowHeaderAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].workflowHeader = workflowHeader;

            recomputationRegister = {
              recomputationRegisterId: (0, _utils.uuidv1)(),
              active: true
            };


            ProcessInstances[i].recomputationRegister = recomputationRegister;

            employeeOtherDetails = {
              employeeOtherDetailsId: (0, _utils.uuidv1)(),
              active: true,
              employeeOtherDetailsAudit: {
                employeeOtherDetailsAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].employeeOtherDetails = employeeOtherDetails;

            pensionCalculationDetails = {
              pensionCalculationDetailsId: (0, _utils.uuidv1)(),
              active: true,
              pensionCalculationDetailsAudit: {
                pensionCalculationDetailsAuditId: (0, _utils.uuidv1)()
              }

            };

            ProcessInstances[i].pensionCalculationDetails = pensionCalculationDetails;

            ProcessInstances[i].auditDetails = auditDetails;

          case 33:
            i++;
            _context10.next = 5;
            break;

          case 36:

            request.ProcessInstances = ProcessInstances;
            return _context10.abrupt("return", request);

          case 38:
          case "end":
            return _context10.stop();
        }
      }
    }, _callee10, undefined);
  }));

  return function addUUIDAndAuditDetailsInitiateReComputation(_x12) {
    return _ref10.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsMigratedPensioner = exports.addUUIDAndAuditDetailsMigratedPensioner = function () {
  var _ref11 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee11(request) {
    var Pensioner, RequestInfo, createdBy, createdDate, auditDetails, i;
    return _regenerator2.default.wrap(function _callee11$(_context11) {
      while (1) {
        switch (_context11.prev = _context11.next) {
          case 0:
            Pensioner = request.Pensioner, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };


            for (i = 0; i < Pensioner.length; i++) {
              if (Pensioner[i].code != null) {
                Pensioner[i].employeeId = (0, _utils.uuidv1)();
                Pensioner[i].employeeAuditId = (0, _utils.uuidv1)();
                Pensioner[i].employeeContactDetailsId = (0, _utils.uuidv1)();
                Pensioner[i].employeeAssignmentId = (0, _utils.uuidv1)();
                Pensioner[i].employeeServiceHistoryId = (0, _utils.uuidv1)();
                Pensioner[i].dependentId = (0, _utils.uuidv1)();
                Pensioner[i].dependentAuditId = (0, _utils.uuidv1)();
                Pensioner[i].pensionerApplicationDetailsId = (0, _utils.uuidv1)();
                Pensioner[i].pensionerId = (0, _utils.uuidv1)();
                Pensioner[i].pensionerAuditId = (0, _utils.uuidv1)();
                Pensioner[i].pensionerFinalCalculatedBenefitId = (0, _utils.uuidv1)();
              } else {}

              Pensioner[i].pensionRevisionId = (0, _utils.uuidv1)();
              Pensioner[i].pensionRevisionAuditId = (0, _utils.uuidv1)();
              Pensioner[i].auditDetails = auditDetails;
            }
            request.Pensioner = Pensioner;
            return _context11.abrupt("return", request);

          case 7:
          case "end":
            return _context11.stop();
        }
      }
    }, _callee11, undefined);
  }));

  return function addUUIDAndAuditDetailsMigratedPensioner(_x13) {
    return _ref11.apply(this, arguments);
  };
}();

var addUUIDAndAuditDetailsCreatePensionRevisionBulk = exports.addUUIDAndAuditDetailsCreatePensionRevisionBulk = function () {
  var _ref12 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee12(request) {
    var Parameters, RequestInfo, createdBy, createdDate, auditDetails, j;
    return _regenerator2.default.wrap(function _callee12$(_context12) {
      while (1) {
        switch (_context12.prev = _context12.next) {
          case 0:
            Parameters = request.Parameters, RequestInfo = request.RequestInfo;
            createdBy = (0, _get2.default)(RequestInfo, "userInfo.uuid", "");
            createdDate = new Date().getTime();
            auditDetails = {
              createdBy: createdBy,
              lastModifiedBy: null,
              createdDate: createdDate,
              lastModifiedDate: null
            };


            for (j = 0; j < Parameters.newPensionRevisions.length; j++) {
              Parameters.newPensionRevisions[j].pensionRevisionId = (0, _utils.uuidv1)();
              Parameters.newPensionRevisions[j].pensionRevisionAuditId = (0, _utils.uuidv1)();
              Parameters.newPensionRevisions[j].auditDetails = auditDetails;
            }

            for (j = 0; j < Parameters.oldPensionRevisions.length; j++) {
              //ProcessInstances[i].oldPensionRevisions[j].pensionRevisionId=uuidv1();    
              Parameters.oldPensionRevisions[j].pensionRevisionAuditId = (0, _utils.uuidv1)();
              Parameters.oldPensionRevisions[j].auditDetails = auditDetails;
            }

            request.Parameters = Parameters;
            return _context12.abrupt("return", request);

          case 8:
          case "end":
            return _context12.stop();
        }
      }
    }, _callee12, undefined);
  }));

  return function addUUIDAndAuditDetailsCreatePensionRevisionBulk(_x14) {
    return _ref12.apply(this, arguments);
  };
}();
//# sourceMappingURL=create.js.map