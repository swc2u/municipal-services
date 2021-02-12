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

var _message = require("../utils/message");

var _notificaitonTemplateSMS = require("../utils/notificaitonTemplateSMS");

var _notificaitonTemplateSMS2 = _interopRequireDefault(_notificaitonTemplateSMS);

var _notificaitonTemplateEMAIL = require("../utils/notificaitonTemplateEMAIL");

var _notificaitonTemplateEMAIL2 = _interopRequireDefault(_notificaitonTemplateEMAIL);

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

var _encryption = require("../utils/encryption");

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_processWorkflow", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;

      var payloads, message, businessService, action, notificationPayLoadSMS, notificationPayLoadEMAIL, errorMessage, currentState, nextState, workflowSearchResponse, searchApplicationResponse, applicationList, _errorMessage, errors, pensionResponse, pensionEmployeesList, hrmsResponse, hrmsEmployee, pensionEmployeeId, assignments, i, serviceHistory, user, employee, _hrmsResponse, _hrmsEmployee, _pensionEmployeeId, dob, dateOfSuperannuation, _assignments, lastAssignments, lastAssignment, _serviceHistory, _user, _employee, workflowResponse, topic, eventResponse, releaseWorkFlowResponse, eventDescription, _releaseWorkFlowResponse, _releaseWorkFlowResponse2;

      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              payloads = [];
              message = (0, _message.Message)();
              businessService = body.ProcessInstances[0].businessService;
              action = body.ProcessInstances[0].action;
              notificationPayLoadSMS = void 0;
              notificationPayLoadEMAIL = void 0;
              errorMessage = "";
              currentState = "";
              nextState = "";

              if (!(action != _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_INITIATE)) {
                _context.next = 16;
                break;
              }

              _context.next = 12;
              return (0, _utils.searchWorkflow)(body.RequestInfo, body.ProcessInstances[0].tenantId, body.ProcessInstances[0].businessId);

            case 12:
              workflowSearchResponse = _context.sent;

              currentState = workflowSearchResponse.ProcessInstances[0].state.state;

              _context.next = 49;
              break;

            case 16:
              _context.next = 18;
              return (0, _utils.searchApplication)(body.RequestInfo, body.ProcessInstances[0].tenantId, body.ProcessInstances[0].employee.code);

            case 18:
              searchApplicationResponse = _context.sent;
              applicationList = searchApplicationResponse.Applications;

              if (!(applicationList.length > 0)) {
                _context.next = 29;
                break;
              }

              applicationList = (0, _filter2.default)(applicationList, function (x) {
                return x.state != "CLOSED" && x.state != "REJECTED";
              });

              if (!(applicationList.length > 0)) {
                _context.next = 29;
                break;
              }

              _errorMessage = message.PARALLEL_WORLFLOW_EXIST_INITIATE_NA;

              _errorMessage = _errorMessage.replace(/\{0}/g, String(applicationList[0].businessId));
              errors = _errorMessage;

              if (!(errors.length > 0)) {
                _context.next = 29;
                break;
              }

              next({
                errorType: "custom",
                errorReponse: {
                  ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                  Errors: errors
                }
              });
              return _context.abrupt("return");

            case 29:
              _context.t0 = businessService;
              _context.next = _context.t0 === _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE ? 32 : 49;
              break;

            case 32:
              _context.next = 34;
              return (0, _utils.getPensionEmployees)(body.RequestInfo, body.ProcessInstances[0].tenantId, body.ProcessInstances[0].employee.code);

            case 34:
              pensionResponse = _context.sent;
              pensionEmployeesList = pensionResponse.Employees;

              if (!(pensionEmployeesList.length > 0)) {
                _context.next = 44;
                break;
              }

              _context.next = 39;
              return (0, _utils.getEmployeeDetails)(body.RequestInfo, body.ProcessInstances[0].tenantId, body.ProcessInstances[0].employee.code);

            case 39:
              hrmsResponse = _context.sent;
              hrmsEmployee = hrmsResponse.Employees[0];


              if (hrmsEmployee) {
                pensionEmployeeId = pensionEmployeesList[0].uuid;
                assignments = [];

                if (hrmsEmployee.assignments && !(0, _isEmpty2.default)(hrmsEmployee.assignments)) {
                  assignments = hrmsEmployee.assignments;
                  for (i = 0; i < assignments.length; i++) {
                    assignments[i].id = (0, _utils.uuidv1)();
                    assignments[i].tenantId = hrmsEmployee.tenantId;
                    assignments[i].active = true;
                    assignments[i].pensionEmployeeId = pensionEmployeeId;
                  }
                }

                serviceHistory = [];

                if (hrmsEmployee.serviceHistory && !(0, _isEmpty2.default)(hrmsEmployee.serviceHistory)) {
                  serviceHistory = hrmsEmployee.serviceHistory;
                  for (i = 0; i < serviceHistory.length; i++) {
                    serviceHistory[i].id = (0, _utils.uuidv1)();
                    serviceHistory[i].tenantId = hrmsEmployee.tenantId;
                    serviceHistory[i].active = true;
                    serviceHistory[i].pensionEmployeeId = pensionEmployeeId;
                  }
                }

                user = hrmsEmployee.user;

                user.employeeContactDetailsId = (0, _utils.uuidv1)();
                user.dob = (0, _utils.adjust530AddForDob)(user.dob);
                user.tenantId = hrmsEmployee.tenantId;
                user.active = true;

                employee = {
                  pensionEmployeeId: pensionEmployeeId,
                  uuid: hrmsEmployee.uuid,
                  code: hrmsEmployee.code,
                  dateOfDeath: (0, _utils.adjust530AddForDeathRegistration)(body.ProcessInstances[0].employee.dateOfDeath),
                  tenantId: hrmsEmployee.tenantId,
                  assignments: assignments,
                  serviceHistory: serviceHistory,
                  user: user,
                  employeeAudit: {
                    pensionEmployeeAuditId: (0, _utils.uuidv1)()
                  },
                  auditDetails: {
                    createdBy: (0, _get2.default)(body.RequestInfo, "userInfo.uuid", ""),
                    lastModifiedBy: null,
                    createdDate: new Date().getTime(),
                    lastModifiedDate: null
                  }
                };

                body.ProcessInstances[0].employee = employee;
              }
              _context.next = 49;
              break;

            case 44:
              _context.next = 46;
              return (0, _utils.getEmployeeDetails)(body.RequestInfo, body.ProcessInstances[0].tenantId, body.ProcessInstances[0].employee.code);

            case 46:
              _hrmsResponse = _context.sent;
              _hrmsEmployee = _hrmsResponse.Employees[0];


              if (_hrmsEmployee) {

                //let maxRetirementAge=envVariables.EGOV_PENSION_MAX_RETIREMENT_AGE;              

                _pensionEmployeeId = (0, _utils.uuidv1)();
                dob = (0, _utils.adjust530AddForDob)(_hrmsEmployee.user.dob);
                //let actualDob=new Date(epochToYmd(intConversion(dob)));    
                //let dorYYYYMMDD=`${actualDob.getFullYear()+maxRetirementAge}-${actualDob.getMonth()+1}-${actualDob.getDate()}`;    
                //let dateOfRetirement=convertDateToEpoch(dorYYYYMMDD,"dob");  

                dateOfSuperannuation = _hrmsEmployee.dateOfSuperannuation;
                _assignments = [];

                if (_hrmsEmployee.assignments && !(0, _isEmpty2.default)(_hrmsEmployee.assignments)) {
                  _assignments = _hrmsEmployee.assignments;
                  lastAssignments = [];
                  lastAssignment = [];

                  if (_assignments.length > 1) {
                    _assignments = (0, _orderBy2.default)(_assignments, ['fromDate'], ['desc']);
                    lastAssignments = (0, _filter2.default)(_assignments, function (x) {
                      return x.fromDate == _assignments[0].fromDate;
                    });
                    if (lastAssignments.length > 1) {
                      lastAssignments = (0, _filter2.default)(lastAssignments, function (x) {
                        return x.isPrimaryAssignment == true;
                      });
                      if (lastAssignments.length > 0) {
                        lastAssignment.push(lastAssignments[0]);
                      } else {
                        lastAssignments = (0, _filter2.default)(_assignments, function (x) {
                          return x.fromDate == _assignments[0].fromDate;
                        });
                        lastAssignment.push(lastAssignments[0]);
                      }
                    }
                  } else {
                    lastAssignment = _assignments;
                  }
                  for (i = 0; i < _assignments.length; i++) {
                    if (_assignments[i].id == lastAssignment[0].id) {
                      _assignments[i].isPensionApplicable = true;
                    }
                    _assignments[i].id = (0, _utils.uuidv1)();
                    _assignments[i].tenantId = _hrmsEmployee.tenantId;
                    _assignments[i].active = true;
                    _assignments[i].pensionEmployeeId = _pensionEmployeeId;
                  }
                }

                _serviceHistory = [];

                if (_hrmsEmployee.serviceHistory && !(0, _isEmpty2.default)(_hrmsEmployee.serviceHistory)) {
                  _serviceHistory = _hrmsEmployee.serviceHistory;
                  for (i = 0; i < _serviceHistory.length; i++) {
                    _serviceHistory[i].id = (0, _utils.uuidv1)();
                    _serviceHistory[i].tenantId = _hrmsEmployee.tenantId;
                    _serviceHistory[i].active = true;
                    _serviceHistory[i].pensionEmployeeId = _pensionEmployeeId;
                  }
                }

                _user = _hrmsEmployee.user;

                _user.employeeContactDetailsId = (0, _utils.uuidv1)();
                _user.tenantId = _hrmsEmployee.tenantId;
                _user.active = true;

                _employee = {
                  pensionEmployeeId: _pensionEmployeeId,
                  id: _hrmsEmployee.id,
                  uuid: _hrmsEmployee.uuid,
                  code: _hrmsEmployee.code,
                  name: _hrmsEmployee.user.name,
                  dob: dob,
                  //dateOfRetirement: dateOfRetirement,
                  dateOfRetirement: dateOfSuperannuation,
                  dateOfDeath: (0, _utils.adjust530AddForDeathRegistration)(body.ProcessInstances[0].employee.dateOfDeath),
                  tenantId: _hrmsEmployee.tenantId,
                  salutation: _hrmsEmployee.user.salutation,
                  gender: _hrmsEmployee.user.gender,
                  employeeStatus: _hrmsEmployee.employeeStatus,
                  employeeType: _hrmsEmployee.employeeType,
                  dateOfAppointment: _hrmsEmployee.dateOfAppointment,
                  assignments: _assignments,
                  serviceHistory: _serviceHistory,
                  user: _user,
                  active: true,
                  employeeAudit: {
                    pensionEmployeeAuditId: (0, _utils.uuidv1)()
                  },
                  auditDetails: {
                    createdBy: (0, _get2.default)(body.RequestInfo, "userInfo.uuid", ""),
                    lastModifiedBy: null,
                    createdDate: new Date().getTime(),
                    lastModifiedDate: null
                  }
                };


                body.ProcessInstances[0].employee = _employee;
              }

            case 49:
              _context.next = 51;
              return (0, _create.addUUIDAndAuditDetails)(body, currentState);

            case 51:
              body = _context.sent;


              body.ProcessInstances[0].employeeOtherDetails.accountNumber = body.ProcessInstances[0].employeeOtherDetails.accountNumber != null ? (0, _encryption.encrypt)(body.ProcessInstances[0].employeeOtherDetails.accountNumber) : body.ProcessInstances[0].employeeOtherDetails.accountNumber;

              if (body.ProcessInstances[0].dependents) {

                for (i = 0; i < body.ProcessInstances[0].dependents.length; i++) {
                  body.ProcessInstances[0].dependents[i].bankAccountNumber = body.ProcessInstances[0].dependents[i].bankAccountNumber != null ? (0, _encryption.encrypt)(body.ProcessInstances[0].dependents[i].bankAccountNumber) : body.ProcessInstances[0].dependents[i].bankAccountNumber;
                }
              }

              workflowResponse = void 0;

              if (!(action != "")) {
                _context.next = 60;
                break;
              }

              _context.next = 58;
              return (0, _utils.createWorkFlow)(body);

            case 58:
              workflowResponse = _context.sent;
              //workflow transition        
              nextState = workflowResponse.ProcessInstances[0].state.state;

            case 60:
              body.ProcessInstances[0].workflowHeader.state = nextState != "" ? nextState : currentState;

              topic = "";
              eventResponse = void 0;
              _context.t1 = businessService;
              _context.next = _context.t1 === _envVariables2.default.EGOV_PENSION_RRP_BUSINESS_SERVICE ? 66 : _context.t1 === _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE ? 105 : _context.t1 === _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE ? 145 : 185;
              break;

            case 66:
              if (!(action == _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_INITIATE)) {
                _context.next = 70;
                break;
              }

              topic = _envVariables2.default.KAFKA_TOPICS_RRP_INITIATE;
              _context.next = 104;
              break;

            case 70:
              if (!(action != "")) {
                _context.next = 74;
                break;
              }

              _context.next = 73;
              return (0, _utils.releaseWorkFlow)(body);

            case 73:
              releaseWorkFlowResponse = _context.sent;

            case 74:
              _context.t2 = currentState;
              _context.next = _context.t2 === "INITIATED" ? 77 : _context.t2 === "PENDING_FOR_DETAILS_VERIFICATION" ? 80 : _context.t2 === "PENDING_FOR_DETAILS_REVIEW" ? 82 : _context.t2 === "PENDING_FOR_CALCULATION" ? 84 : _context.t2 === "PENDING_FOR_CALCULATION_VERIFICATION" ? 86 : _context.t2 === "PENDING_FOR_CALCULATION_APPROVAL" ? 87 : _context.t2 === "PENDING_FOR_CALCULATION_REVIEW" ? 89 : _context.t2 === "PENDING_FOR_APPROVAL" ? 91 : _context.t2 === "PENDING_FOR_AUDIT" ? 93 : _context.t2 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_ACCOUNTS_OFFICER" ? 95 : _context.t2 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_SENIOR_ASSISTANT" ? 97 : _context.t2 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_CLERK" ? 99 : 103;
              break;

            case 77:
              //PMS_DDO                        
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_RRP_INITIATED;
              if (action == _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_FORWARD) {
                //eventResponse=await createUserEventToUser(body,body.ProcessInstances[0].employee.tenantId,message.RRP_STARTED_USER_EVENT_NAME,message.RRP_STARTED_USER_EVENT_DESCRIPTION,body.ProcessInstances[0].employee.uuid); 
                eventDescription = message.RRP_STARTED_USER_EVENT_DESCRIPTION;

                eventDescription = eventDescription.replace(/\{0}/g, String(body.ProcessInstances[0].businessId));
                //eventResponse=createUserEventToUser(body,body.ProcessInstances[0].employee.tenantId,message.RRP_STARTED_USER_EVENT_NAME,eventDescription,body.ProcessInstances[0].employee.uuid); 
                notificationPayLoadSMS = (0, _notificaitonTemplateSMS2.default)(body.ProcessInstances[0]);
                notificationPayLoadEMAIL = (0, _notificaitonTemplateEMAIL2.default)(body.ProcessInstances[0]);
                _logger2.default.debug(notificationPayLoadSMS);
                _logger2.default.debug(notificationPayLoadEMAIL);
                payloads.push(notificationPayLoadSMS);
                payloads.push(notificationPayLoadEMAIL);
              }
              return _context.abrupt("break", 104);

            case 80:
              //ACCOUNTS_OFFICER
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_DETAILS_VERIFICATION;
              return _context.abrupt("break", 104);

            case 82:
              //SENIOR_ASSISTANT
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_DETAILS_REVIEW;
              return _context.abrupt("break", 104);

            case 84:
              //CLERK  
              if (action == _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_SEND_BACK) {
                topic = _envVariables2.default.KAFKA_TOPICS_CLEAR_BENEFIT;
              } else {
                topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_CALCULATION;
              }
              return _context.abrupt("break", 104);

            case 86:
              return _context.abrupt("break", 104);

            case 87:
              //ACCOUNTS_OFFICER      
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 104);

            case 89:
              //ACCOUNTS_OFFICER  
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_CALCULATION_REVIEW;
              return _context.abrupt("break", 104);

            case 91:
              //ADDITIONAL_COMMISSIONER    
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 104);

            case 93:
              //ACCOUNTS_OFFICER    
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 104);

            case 95:
              //ACCOUNTS_OFFICER                                            
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 104);

            case 97:
              //SENIOR_ASSISTANT                                            
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 104);

            case 99:
              _context.next = 101;
              return (0, _utils.closeWorkflowByUser)(body);

            case 101:
              //close workflow  
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 104);

            case 103:
              return _context.abrupt("break", 104);

            case 104:
              return _context.abrupt("break", 185);

            case 105:
              if (!(action === _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_INITIATE)) {
                _context.next = 109;
                break;
              }

              topic = _envVariables2.default.KAFKA_TOPICS_DOE_INITIATE;
              _context.next = 144;
              break;

            case 109:
              if (!(action != "")) {
                _context.next = 113;
                break;
              }

              _context.next = 112;
              return (0, _utils.releaseWorkFlow)(body);

            case 112:
              _releaseWorkFlowResponse = _context.sent;

            case 113:
              _context.t3 = currentState;
              _context.next = _context.t3 === "INITIATED" ? 116 : _context.t3 === "PENDING_FOR_DETAILS_VERIFICATION" ? 119 : _context.t3 === "PENDING_FOR_DETAILS_REVIEW" ? 121 : _context.t3 === "PENDING_FOR_CALCULATION" ? 123 : _context.t3 === "PENDING_FOR_CALCULATION_VERIFICATION" ? 125 : _context.t3 === "PENDING_FOR_CALCULATION_APPROVAL" ? 127 : _context.t3 === "PENDING_FOR_CALCULATION_REVIEW" ? 129 : _context.t3 === "PENDING_FOR_APPROVAL" ? 131 : _context.t3 === "PENDING_FOR_AUDIT" ? 133 : _context.t3 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_ACCOUNTS_OFFICER" ? 135 : _context.t3 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_SENIOR_ASSISTANT" ? 137 : _context.t3 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_CLERK" ? 139 : 143;
              break;

            case 116:
              //PMS_DDO                           
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_DEATH_INITIATED;
              if (action == _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_FORWARD) {
                (0, _notificaitonTemplateSMS2.default)(body.ProcessInstances[0]);
              }
              return _context.abrupt("break", 144);

            case 119:
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_DETAILS_VERIFICATION;
              return _context.abrupt("break", 144);

            case 121:
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_DETAILS_REVIEW;
              return _context.abrupt("break", 144);

            case 123:
              //CLERK              
              if (action == _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_SEND_BACK) {
                topic = _envVariables2.default.KAFKA_TOPICS_CLEAR_BENEFIT;
              } else {
                topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_CALCULATION;
              }
              return _context.abrupt("break", 144);

            case 125:
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 144);

            case 127:
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 144);

            case 129:
              //ACCOUNTS_OFFICER  
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_CALCULATION_REVIEW;
              return _context.abrupt("break", 144);

            case 131:
              //ADDITIONAL_COMMISSIONER   
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 144);

            case 133:
              //ACCOUNTS_OFFICER    
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 144);

            case 135:
              //ACCOUNTS_OFFICER                                            
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 144);

            case 137:
              //SENIOR_ASSISTANT                                            
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 144);

            case 139:
              _context.next = 141;
              return (0, _utils.closeWorkflowByUser)(body);

            case 141:
              //close workflow  
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 144);

            case 143:
              return _context.abrupt("break", 144);

            case 144:
              return _context.abrupt("break", 185);

            case 145:
              if (!(action === _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_INITIATE)) {
                _context.next = 149;
                break;
              }

              topic = _envVariables2.default.KAFKA_TOPICS_DOP_INITIATE;
              _context.next = 184;
              break;

            case 149:
              if (!(action != "")) {
                _context.next = 153;
                break;
              }

              _context.next = 152;
              return (0, _utils.releaseWorkFlow)(body);

            case 152:
              _releaseWorkFlowResponse2 = _context.sent;

            case 153:
              _context.t4 = currentState;
              _context.next = _context.t4 === "INITIATED" ? 156 : _context.t4 === "PENDING_FOR_DETAILS_VERIFICATION" ? 159 : _context.t4 === "PENDING_FOR_DETAILS_REVIEW" ? 161 : _context.t4 === "PENDING_FOR_CALCULATION" ? 163 : _context.t4 === "PENDING_FOR_CALCULATION_VERIFICATION" ? 165 : _context.t4 === "PENDING_FOR_CALCULATION_APPROVAL" ? 167 : _context.t4 === "PENDING_FOR_CALCULATION_REVIEW" ? 169 : _context.t4 === "PENDING_FOR_APPROVAL" ? 171 : _context.t4 === "PENDING_FOR_AUDIT" ? 173 : _context.t4 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_ACCOUNTS_OFFICER" ? 175 : _context.t4 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_SENIOR_ASSISTANT" ? 177 : _context.t4 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_CLERK" ? 179 : 183;
              break;

            case 156:
              //PMS_DDO                           
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_DEATH_INITIATED;
              if (action == _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_FORWARD) {
                (0, _notificaitonTemplateSMS2.default)(body.ProcessInstances[0]);
              }
              return _context.abrupt("break", 184);

            case 159:
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_DETAILS_VERIFICATION;
              return _context.abrupt("break", 184);

            case 161:
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_DETAILS_REVIEW;
              return _context.abrupt("break", 184);

            case 163:
              //CLERK              
              if (action == _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_SEND_BACK) {
                topic = _envVariables2.default.KAFKA_TOPICS_CLEAR_BENEFIT;
              } else {
                topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_CALCULATION;
              }
              return _context.abrupt("break", 184);

            case 165:
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 184);

            case 167:
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 184);

            case 169:
              //ACCOUNTS_OFFICER  
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_PENDING_FOR_CALCULATION_REVIEW;
              return _context.abrupt("break", 184);

            case 171:
              //ADDITIONAL_COMMISSIONER    
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 184);

            case 173:
              //ACCOUNTS_OFFICER     
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 184);

            case 175:
              //ACCOUNTS_OFFICER                                            
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 184);

            case 177:
              //SENIOR_ASSISTANT                                            
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 184);

            case 179:
              _context.next = 181;
              return (0, _utils.closeWorkflowByUser)(body);

            case 181:
              //close workflow  
              topic = _envVariables2.default.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE;
              return _context.abrupt("break", 184);

            case 183:
              return _context.abrupt("break", 184);

            case 184:
              return _context.abrupt("break", 185);

            case 185:

              payloads.push({
                topic: topic,
                messages: JSON.stringify(body)
              });
              _producer2.default.send(payloads, function (err, data) {
                var response = {
                  ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                  ProcessInstances: body.ProcessInstances
                };
                res.json(response);
              });

            case 187:
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
//# sourceMappingURL=processWorkflow.js.map