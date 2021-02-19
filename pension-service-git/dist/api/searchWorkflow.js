"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _express = require("express");

var _utils = require("../utils");

var _search = require("../utils/search");

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _some = require("lodash/some");

var _some2 = _interopRequireDefault(_some);

var _modelValidation = require("../utils/modelValidation");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _mdmsData = require("../utils/mdmsData");

var _mdmsData2 = _interopRequireDefault(_mdmsData);

var _filter = require("lodash/filter");

var _filter2 = _interopRequireDefault(_filter);

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_searchWorkflow", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee8(request, res, next) {
      var response, queryObj, mdms, errors, workflowResponse, processInstances, currentState, documentsUpload, documentComment, actorAcccessLevel, textDocument, sqlQueryDocument, workflowDocuments, text, sqlQuery;
      return _regenerator2.default.wrap(function _callee8$(_context8) {
        while (1) {
          switch (_context8.prev = _context8.next) {
            case 0:
              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                ProcessInstances: [] //,
                //ApplicationDetails: {},
                //PaymentDetails: {}
              };
              queryObj = JSON.parse(JSON.stringify(request.query));

              //getting mdms data

              _context8.next = 4;
              return (0, _mdmsData2.default)(request.body.RequestInfo, queryObj.tenantId);

            case 4:
              mdms = _context8.sent;
              errors = (0, _modelValidation.validateWorkflowSearchModel)(queryObj);

              if (!(errors.length > 0)) {
                _context8.next = 9;
                break;
              }

              next({
                errorType: "custom",
                errorReponse: {
                  ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                  Errors: errors
                }
              });
              return _context8.abrupt("return");

            case 9:
              _context8.next = 11;
              return (0, _utils.searchWorkflow)(request.body.RequestInfo, queryObj.tenantId, queryObj.businessIds);

            case 11:
              workflowResponse = _context8.sent;
              processInstances = workflowResponse.ProcessInstances;
              currentState = "";

              if (processInstances != null && processInstances.length > 0) {
                currentState = processInstances[0].state.state;
              }
              documentsUpload = void 0;
              documentComment = void 0;
              actorAcccessLevel = {};
              _context8.t0 = currentState;
              _context8.next = _context8.t0 === "INITIATED" ? 21 : _context8.t0 === "PENDING_FOR_DETAILS_VERIFICATION" ? 25 : _context8.t0 === "PENDING_FOR_DETAILS_REVIEW" ? 29 : _context8.t0 === "PENDING_FOR_CALCULATION" ? 33 : _context8.t0 === "PENDING_FOR_CALCULATION_VERIFICATION" ? 37 : _context8.t0 === "PENDING_FOR_CALCULATION_APPROVAL" ? 41 : _context8.t0 === "PENDING_FOR_CALCULATION_REVIEW" ? 45 : _context8.t0 === "PENDING_FOR_APPROVAL" ? 49 : _context8.t0 === "PENDING_FOR_AUDIT" ? 53 : _context8.t0 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_ACCOUNTS_OFFICER" ? 57 : _context8.t0 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_SENIOR_ASSISTANT" ? 61 : _context8.t0 === "PENDING_FOR_CONTINGENT_BILL_PREPARATION_WITH_CLERK" ? 65 : 69;
              break;

            case 21:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: true,
                employeeLeaveUpdate: true,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = true;
              documentComment = false;
              return _context8.abrupt("break", 73);

            case 25:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,

                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = true;
              return _context8.abrupt("break", 73);

            case 29:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = true;
              return _context8.abrupt("break", 73);

            case 33:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: true,
                pensionDataUpdate: true
              };
              documentsUpload = false;
              documentComment = false;
              return _context8.abrupt("break", 73);

            case 37:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = false;
              return _context8.abrupt("break", 73);

            case 41:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = false;
              return _context8.abrupt("break", 73);

            case 45:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = true;
              return _context8.abrupt("break", 73);

            case 49:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = false;
              return _context8.abrupt("break", 73);

            case 53:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = false;
              return _context8.abrupt("break", 73);

            case 57:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = false;
              return _context8.abrupt("break", 73);

            case 61:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = false;
              return _context8.abrupt("break", 73);

            case 65:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = false;
              return _context8.abrupt("break", 73);

            case 69:
              actorAcccessLevel = {
                employeeOtherDetailsUpdate: false,
                employeeLeaveUpdate: false,
                pensionCalculation: false,
                pensionDataUpdate: false
              };
              documentsUpload = false;
              documentComment = false;
              return _context8.abrupt("break", 73);

            case 73:

              //fetch uploaded documents  
              textDocument = "select pa.uuid, pa.file_store_id, pa.document_type from eg_pension_attachment pa join eg_pension_workflow_header pwh on pa.workflow_header_id =pwh.uuid";


              if (!(0, _isEmpty2.default)(queryObj)) {
                textDocument = textDocument + " where ";
              }
              /*
              if (queryObj.tenantId) {
                textDocument = `${textDocument} pa.tenantid = '${queryObj.tenantId}'`;
              }
              */
              if (queryObj.businessIds) {
                textDocument = textDocument + " pwh.application_number = '" + queryObj.businessIds + "'";
              }
              textDocument = textDocument + " and pa.active=true";

              sqlQueryDocument = textDocument;
              workflowDocuments = [];


              db.query(sqlQueryDocument, function () {
                var _ref3 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(err, dbRes) {
                  return _regenerator2.default.wrap(function _callee$(_context) {
                    while (1) {
                      switch (_context.prev = _context.next) {
                        case 0:
                          if (!err) {
                            _context.next = 4;
                            break;
                          }

                          _logger2.default.error(err.stack);
                          _context.next = 12;
                          break;

                        case 4:
                          if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                            _context.next = 10;
                            break;
                          }

                          _context.next = 7;
                          return (0, _search.mergeWorkflowDocumentSearchResults)(dbRes.rows, request.query, request.body.RequestInfo);

                        case 7:
                          _context.t0 = _context.sent;
                          _context.next = 11;
                          break;

                        case 10:
                          _context.t0 = [];

                        case 11:
                          workflowDocuments = _context.t0;

                        case 12:
                        case "end":
                          return _context.stop();
                      }
                    }
                  }, _callee, undefined);
                }));

                return function (_x4, _x5) {
                  return _ref3.apply(this, arguments);
                };
              }());

              text = "select pe.employee_hrms_code, pe.date_of_retirement, pe.date_of_death, pwh.uuid, pwh.pension_employee_id, pwh.application_date from eg_pension_employee pe join eg_pension_workflow_header pwh on pe.uuid=pwh.pension_employee_id";


              if (!(0, _isEmpty2.default)(queryObj)) {
                text = text + " where ";
              }
              if (queryObj.tenantId) {
                text = text + " pwh.tenantid = '" + queryObj.tenantId + "'";
              }
              if (queryObj.businessIds) {
                text = text + " and pwh.application_number = '" + queryObj.businessIds + "'";
              }

              sqlQuery = text;


              db.query(sqlQuery, function () {
                var _ref4 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee7(err, dbRes) {
                  var i;
                  return _regenerator2.default.wrap(function _callee7$(_context7) {
                    while (1) {
                      switch (_context7.prev = _context7.next) {
                        case 0:
                          if (!err) {
                            _context7.next = 4;
                            break;
                          }

                          _logger2.default.error(err.stack);
                          _context7.next = 5;
                          break;

                        case 4:
                          return _context7.delegateYield( /*#__PURE__*/_regenerator2.default.mark(function _callee6() {
                            var pensionEmployeeId, workflowHeader, txtEmployeeOtherDetails, sqlEmployeeOtherDetails, employeeOtherDetails, txtPensionCalculation, sqlPensionCalculation, pensionCalculationDetails, pensionCalculationUpdateDetails, dependents, txtDependent, sqlQueryDependent, employeeResponse, employee, textDocumentHistory, sqlQueryDocumentHistory, documentAudit, documents, documentTypes, workflowDocument, fileStoreResponse, pensionAttachmentId, fileStoreId, url, _documentAudit, document, employeeDisabilityResponse, employeeDisability;

                            return _regenerator2.default.wrap(function _callee6$(_context6) {
                              while (1) {
                                switch (_context6.prev = _context6.next) {
                                  case 0:
                                    pensionEmployeeId = dbRes.rows[0].pension_employee_id;

                                    if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                                      _context6.next = 7;
                                      break;
                                    }

                                    _context6.next = 4;
                                    return (0, _search.mergeWorkflowHeader)(dbRes.rows, request.query, request.body.RequestInfo);

                                  case 4:
                                    _context6.t0 = _context6.sent;
                                    _context6.next = 8;
                                    break;

                                  case 7:
                                    _context6.t0 = {};

                                  case 8:
                                    workflowHeader = _context6.t0;


                                    //employeeOtherDetails 
                                    txtEmployeeOtherDetails = "select workflow_state, ltc, lpd, pension_arrear, is_da_medical_admissible, fma, medical_relief, miscellaneous, over_payment, income_tax, cess, bank_address, account_number, claimant, wef, total_no_pay_leaves_days, dues, is_employment_active, is_convicted_serious_crime_or_grave_misconduct, is_any_judicial_proceeding_is_continuing, is_any_misconduct_insolvency_inefficiency, is_employee_dies_in_terrorist_attack, is_employee_dies_in_accidental_death, is_commutation_opted, reason_for_retirement, is_eligible_for_pension, is_dues_present, is_dues_amount_decided, is_dues_amount_decided, is_taken_monthly_pension_and_gratuity, is_taken_gratuity_commutation_terminal_benefit, is_taken_compensation_pension_and_gratuity, dies_in_extremists_dacoits_smuggler_antisocial_attack, is_compassionate_pension_granted, total_no_pay_leaves_months, total_no_pay_leaves_years, no_dues_for_avail_govt_accomodation, employee_group, date_of_contingent, bank_code, bank_ifsc from eg_pension_employee_other_details";

                                    if (!(0, _isEmpty2.default)(queryObj)) {
                                      txtEmployeeOtherDetails = txtEmployeeOtherDetails + " where ";
                                    }

                                    if (workflowHeader.workflowHeaderId) {
                                      txtEmployeeOtherDetails = txtEmployeeOtherDetails + " workflow_header_id = '" + workflowHeader.workflowHeaderId + "'";
                                    }

                                    sqlEmployeeOtherDetails = txtEmployeeOtherDetails;
                                    employeeOtherDetails = {};


                                    db.query(sqlEmployeeOtherDetails, function () {
                                      var _ref5 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(err, dbRes) {
                                        return _regenerator2.default.wrap(function _callee2$(_context2) {
                                          while (1) {
                                            switch (_context2.prev = _context2.next) {
                                              case 0:
                                                if (!err) {
                                                  _context2.next = 4;
                                                  break;
                                                }

                                                _logger2.default.error(err.stack);
                                                _context2.next = 12;
                                                break;

                                              case 4:
                                                if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                                                  _context2.next = 10;
                                                  break;
                                                }

                                                _context2.next = 7;
                                                return (0, _search.mergeEmployeeOtherDetails)(dbRes.rows, request.query, request.body.RequestInfo, mdms);

                                              case 7:
                                                _context2.t0 = _context2.sent;
                                                _context2.next = 11;
                                                break;

                                              case 10:
                                                _context2.t0 = {};

                                              case 11:
                                                employeeOtherDetails = _context2.t0;

                                              case 12:
                                              case "end":
                                                return _context2.stop();
                                            }
                                          }
                                        }, _callee2, undefined);
                                      }));

                                      return function (_x8, _x9) {
                                        return _ref5.apply(this, arguments);
                                      };
                                    }());

                                    //pensionCalculationDetails 
                                    txtPensionCalculation = "select basic_pension_sytem, pension_deductions_system, additional_pension_system, commuted_pension_system, commuted_value_system, family_pension_i_system, family_pension_ii_system, dcrg_system, net_deductions_system, final_calculated_pension_system, basic_pension_verified, pension_deductions_verified, additional_pension_verified, commuted_pension_verified, commuted_value_verified, family_pension_i_verified, family_pension_ii_verified, dcrg_verified, net_deductions_verified, final_calculated_pension_verified, interim_relief_system, da_system, interim_relief_verified, da_verified, nqs_year_system, nqs_month_system, nqs_day_system, nqs_year_verified, nqs_month_verified, nqs_day_verified, dues_deductions_system, compassionate_pension_system, compensation_pension_system, terminal_benefit_system, dues_deductions_verified, compassionate_pension_verified, compensation_pension_verified, terminal_benefit_verified, final_calculated_gratuity_system, final_calculated_gratuity_verified, family_pension_i_start_date_system, family_pension_i_start_date_verified, family_pension_i_end_date_system, family_pension_i_end_date_verified, family_pension_ii_start_date_system, family_pension_ii_start_date_verified, ex_gratia_system, ex_gratia_verified, pensioner_family_pension_system, pensioner_family_pension_verified, total_pension_system, total_pension_verified, provisional_pension_system, provisional_pension_verified, interim_relief_applicable, interim_relief_expression, basic_pension_applicable, basic_pension_expression, provisional_pension_applicable, provisional_pension_expression, compassionate_pension_applicable, compassionate_pension_expression, compensation_pension_applicable, compensation_pension_expression, commuted_pension_applicable, commuted_pension_expression, family_pension_i_applicable, family_pension_i_expression, family_pension_ii_applicable, family_pension_ii_expression, da_applicable, da_expression, additional_pension_applicable, additional_pension_expression, total_pension_applicable, total_pension_expression, pension_deductions_applicable, pension_deductions_expression, net_deductions_applicable, net_deductions_expression, final_calculated_pension_applicable, final_calculated_pension_expression, commutation_value_applicable, commutation_value_expression, dcrg_applicable, dcrg_expression, terminal_benefit_applicable, terminal_benefit_expression, dues_deductions_applicable, dues_deductions_expression, final_calculated_gratuity_applicable, final_calculated_gratuity_expression, ex_gratia_applicable, ex_gratia_expression, pensioner_family_pension_applicable, pensioner_family_pension_expression, invalid_pension_system, wound_extraordinary_pension_system, attendant_allowance_system, invalid_pension_verified, wound_extraordinary_pension_verified, attendant_allowance_verified, invalid_pension_applicable, invalid_pension_expression, wound_extraordinary_pension_applicable, wound_extraordinary_pension_expression, attendant_allowance_applicable, attendant_allowance_expression, gqs_year_system, gqs_month_system, gqs_day_system, gqs_year_verified, gqs_month_verified, gqs_day_verified, notification_text_system, notification_text_verified, interim_relief_lpd_system from eg_pension_calculation_details";

                                    if (!(0, _isEmpty2.default)(queryObj)) {
                                      txtPensionCalculation = txtPensionCalculation + " where ";
                                    }
                                    if (workflowHeader.workflowHeaderId) {
                                      txtPensionCalculation = txtPensionCalculation + " workflow_header_id = '" + workflowHeader.workflowHeaderId + "'";
                                    }

                                    sqlPensionCalculation = txtPensionCalculation;
                                    pensionCalculationDetails = {};
                                    pensionCalculationUpdateDetails = {};


                                    db.query(sqlPensionCalculation, function () {
                                      var _ref6 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee3(err, dbRes) {
                                        return _regenerator2.default.wrap(function _callee3$(_context3) {
                                          while (1) {
                                            switch (_context3.prev = _context3.next) {
                                              case 0:
                                                if (!err) {
                                                  _context3.next = 4;
                                                  break;
                                                }

                                                _logger2.default.error(err.stack);
                                                _context3.next = 20;
                                                break;

                                              case 4:
                                                if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                                                  _context3.next = 10;
                                                  break;
                                                }

                                                _context3.next = 7;
                                                return (0, _search.mergePensionCalculationDetails)(dbRes.rows, request.query, request.body.RequestInfo);

                                              case 7:
                                                _context3.t0 = _context3.sent;
                                                _context3.next = 11;
                                                break;

                                              case 10:
                                                _context3.t0 = {};

                                              case 11:
                                                pensionCalculationDetails = _context3.t0;

                                                if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                                                  _context3.next = 18;
                                                  break;
                                                }

                                                _context3.next = 15;
                                                return (0, _search.mergePensionCalculationUpdateDetails)(dbRes.rows, request.query, request.body.RequestInfo);

                                              case 15:
                                                _context3.t1 = _context3.sent;
                                                _context3.next = 19;
                                                break;

                                              case 18:
                                                _context3.t1 = {};

                                              case 19:
                                                pensionCalculationUpdateDetails = _context3.t1;

                                              case 20:
                                              case "end":
                                                return _context3.stop();
                                            }
                                          }
                                        }, _callee3, undefined);
                                      }));

                                      return function (_x10, _x11) {
                                        return _ref6.apply(this, arguments);
                                      };
                                    }());

                                    //dependents
                                    dependents = [];
                                    txtDependent = "SELECT name, dob, address, mobile_number, relationship, is_disabled, marital_status, is_holly_dependent, no_spouse_no_children, is_grandchild_from_deceased_son, is_eligible_for_gratuity, is_eligible_for_pension, gratuity_percentage, bank_account_number, bank_details, bank_code, bank_ifsc FROM eg_pension_dependent";

                                    if (!(0, _isEmpty2.default)(queryObj)) {
                                      txtDependent = txtDependent + " WHERE";
                                    }
                                    /*
                                    if (queryObj.tenantId) {
                                      txtDependent = `${txtDependent} tenantid = '${queryObj.tenantId}'`;
                                    }
                                    */
                                    if (pensionEmployeeId) {
                                      txtDependent = txtDependent + " pension_employee_id = '" + pensionEmployeeId + "'";
                                    }
                                    txtDependent = txtDependent + " AND active = true";

                                    sqlQueryDependent = txtDependent;


                                    db.query(sqlQueryDependent, function () {
                                      var _ref7 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee4(err, dbRes) {
                                        return _regenerator2.default.wrap(function _callee4$(_context4) {
                                          while (1) {
                                            switch (_context4.prev = _context4.next) {
                                              case 0:
                                                if (!err) {
                                                  _context4.next = 4;
                                                  break;
                                                }

                                                _logger2.default.error(err.stack);
                                                _context4.next = 12;
                                                break;

                                              case 4:
                                                if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                                                  _context4.next = 10;
                                                  break;
                                                }

                                                _context4.next = 7;
                                                return (0, _search.mergeDependentResults)(dbRes.rows, request.query, request.body.RequestInfo, mdms);

                                              case 7:
                                                _context4.t0 = _context4.sent;
                                                _context4.next = 11;
                                                break;

                                              case 10:
                                                _context4.t0 = [];

                                              case 11:
                                                dependents = _context4.t0;

                                              case 12:
                                              case "end":
                                                return _context4.stop();
                                            }
                                          }
                                        }, _callee4, undefined);
                                      }));

                                      return function (_x12, _x13) {
                                        return _ref7.apply(this, arguments);
                                      };
                                    }());

                                    //fetch employee details from pension module   

                                    _context6.next = 31;
                                    return (0, _utils.searchEmployee)(request.body.RequestInfo, queryObj.tenantId, dbRes.rows[0].employee_hrms_code);

                                  case 31:
                                    employeeResponse = _context6.sent;
                                    employee = employeeResponse.Employees[0];

                                    /*
                                    //leaves
                                    let textLeave ="select leave_type, leave_from, leave_to, leave_count from tbl_pension_employee_leave";
                                     if (!isEmpty(queryObj)) {
                                      textLeave = textLeave + " where ";
                                    }
                                    if (queryObj.tenantId) {
                                      textLeave = `${textLeave} tenantid = '${queryObj.tenantId}'`;
                                    }
                                    if (dbRes.rows[0].pension_employee_id) {
                                      textLeave = `${textLeave} and pension_employee_id = '${dbRes.rows[0].pension_employee_id}'`;
                                    }             
                                    textLeave = `${textLeave} and active=true`;              
                                          
                                    let sqlQueryLeave=textLeave;
                                    
                                    let leaves=[];
                                     db.query(sqlQueryLeave, async (err, dbRes) => {
                                      if (err) {
                                        logger.error(err.stack);
                                      } 
                                      else {
                                        leaves=dbRes.rows && !isEmpty(dbRes.rows)
                                            ? await mergeLeaveSearchResults(
                                                dbRes.rows,
                                                request.query,
                                                request.body.RequestInfo
                                              )
                                            : []; 
                                         const leaveTypes=get(mdms,"MdmsRes.pension.EmployeeLeaveType");      
                                        for (var i = 0; i < leaves.length; i++) {
                                          let leave=filter(leaveTypes,function(x){return x.code===leaves[i].leaveType;});                                    
                                          leaves[i].leaveTypeName=leave.length>0?leave[0].name:"";
                                        }
                                                          
                                      }
                                    });         
                                    */

                                    //document audit

                                    textDocumentHistory = "SELECT pau.document_type, pau.state, pau.comment, pau.created_by from eg_pension_attachment_audit pau join eg_pension_attachment pa on pau.pension_attachment_id=pa.uuid";


                                    if (!(0, _isEmpty2.default)(queryObj)) {
                                      textDocumentHistory = textDocumentHistory + " WHERE ";
                                    }
                                    if (queryObj.tenantId) {
                                      textDocumentHistory = textDocumentHistory + " pa.tenantid = '" + queryObj.tenantId + "'";
                                    }
                                    /*           
                                    if (workflowDocuments[i].pensionAttachmentId) {
                                      textDocumentHistory = `${textDocumentHistory} AND pa.document_type = '${workflowDocuments[i].documentType}'`;
                                    } 
                                    */
                                    if (workflowHeader.workflowHeaderId) {
                                      textDocumentHistory = textDocumentHistory + " and pa.workflow_header_id = '" + workflowHeader.workflowHeaderId + "'";
                                    }
                                    textDocumentHistory = textDocumentHistory + " and pa.active = true";

                                    sqlQueryDocumentHistory = textDocumentHistory;
                                    documentAudit = []; //uploaded documents history

                                    db.query(sqlQueryDocumentHistory, function () {
                                      var _ref8 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee5(err, dbRes) {
                                        var i, workflowDocumentAudit;
                                        return _regenerator2.default.wrap(function _callee5$(_context5) {
                                          while (1) {
                                            switch (_context5.prev = _context5.next) {
                                              case 0:
                                                if (!err) {
                                                  _context5.next = 4;
                                                  break;
                                                }

                                                _logger2.default.error(err.stack);
                                                _context5.next = 13;
                                                break;

                                              case 4:
                                                if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                                                  _context5.next = 10;
                                                  break;
                                                }

                                                _context5.next = 7;
                                                return (0, _search.mergeWorkflowDocumentAuditSearchResults)(dbRes.rows, request.query, request.body.RequestInfo);

                                              case 7:
                                                _context5.t0 = _context5.sent;
                                                _context5.next = 11;
                                                break;

                                              case 10:
                                                _context5.t0 = [];

                                              case 11:
                                                documentAudit = _context5.t0;

                                                for (i = 0; i < workflowDocuments.length; i++) {
                                                  workflowDocumentAudit = (0, _filter2.default)(documentAudit, function (x) {
                                                    return x.documentType == workflowDocuments[i].documentType;
                                                  });

                                                  workflowDocuments[i].documentAudit = workflowDocumentAudit;
                                                }

                                              case 13:
                                              case "end":
                                                return _context5.stop();
                                            }
                                          }
                                        }, _callee5, undefined);
                                      }));

                                      return function (_x14, _x15) {
                                        return _ref8.apply(this, arguments);
                                      };
                                    }());

                                    documents = [];
                                    documentTypes = [];
                                    _context6.t1 = processInstances[0].businessService;
                                    _context6.next = _context6.t1 === _envVariables2.default.EGOV_PENSION_RRP_BUSINESS_SERVICE ? 46 : _context6.t1 === _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE ? 48 : _context6.t1 === _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE ? 50 : 52;
                                    break;

                                  case 46:
                                    documentTypes = (0, _get2.default)(mdms, "MdmsRes.pension.DocumentType_RRP");
                                    return _context6.abrupt("break", 52);

                                  case 48:
                                    documentTypes = (0, _get2.default)(mdms, "MdmsRes.pension.DocumentType_DOE");
                                    return _context6.abrupt("break", 52);

                                  case 50:
                                    documentTypes = (0, _get2.default)(mdms, "MdmsRes.pension.DocumentType_DOP");
                                    return _context6.abrupt("break", 52);

                                  case 52:
                                    documentTypes = (0, _filter2.default)(documentTypes, function (x) {
                                      return x.active == true;
                                    });

                                    i = 0;

                                  case 54:
                                    if (!(i < documentTypes.length)) {
                                      _context6.next = 74;
                                      break;
                                    }

                                    //find the document in uploaded documents
                                    workflowDocument = (0, _filter2.default)(workflowDocuments, function (x) {
                                      return x.documentType == documentTypes[i].code;
                                    });
                                    fileStoreResponse = void 0;
                                    pensionAttachmentId = "";
                                    fileStoreId = "";
                                    url = "";
                                    _documentAudit = [];

                                    if (!(workflowDocument.length > 0)) {
                                      _context6.next = 69;
                                      break;
                                    }

                                    pensionAttachmentId = workflowDocument[0].pensionAttachmentId;
                                    fileStoreId = workflowDocument[0].fileStoreId;
                                    _context6.next = 66;
                                    return (0, _utils.getFileDetails)(queryObj.tenantId, workflowDocument[0].fileStoreId);

                                  case 66:
                                    fileStoreResponse = _context6.sent;

                                    if (!(0, _isEmpty2.default)(fileStoreResponse) && fileStoreResponse.fileStoreIds.length > 0) {
                                      url = fileStoreResponse.fileStoreIds[0].url;
                                      if (url.indexOf(",") >= 0) {
                                        url = url.split(",")[0];
                                      }
                                    }
                                    _documentAudit = workflowDocument[0].documentAudit;

                                  case 69:
                                    document = {
                                      pensionAttachmentId: pensionAttachmentId,
                                      fileStoreId: fileStoreId,
                                      documentType: documentTypes[i].code,
                                      //documentTypeName: documentTypes[i].name,
                                      isMandatory: documentTypes[i].isMandatory,
                                      isMandatoryForCommutation: documentTypes[i].isMandatoryForCommutation,
                                      isMandatoryForNoGovtAccomodation: documentTypes[i].isMandatoryForNoGovtAccomodation,
                                      url: url,
                                      comment: "",
                                      documentsUpload: documentsUpload,
                                      documentComment: documentComment,
                                      documentAudit: _documentAudit

                                    };


                                    documents.push(document);

                                  case 71:
                                    i++;
                                    _context6.next = 54;
                                    break;

                                  case 74:
                                    _context6.next = 76;
                                    return (0, _utils.getEmployeeDisability)(request.body.RequestInfo, queryObj.tenantId, employee.code);

                                  case 76:
                                    employeeDisabilityResponse = _context6.sent;
                                    employeeDisability = {
                                      disabilityPercentage: employeeDisabilityResponse.Employees.length > 0 ? employeeDisabilityResponse.Employees[0].disabilityPercentage : null,
                                      woundExtraordinaryPension: employeeDisabilityResponse.Employees.length > 0 ? employeeDisabilityResponse.Employees[0].woundExtraordinaryPension : null,
                                      attendantAllowanceGranted: employeeDisabilityResponse.Employees.length > 0 ? employeeDisabilityResponse.Employees[0].attendantAllowanceGranted : false
                                    };

                                    for (i = 0; i < processInstances.length; i++) {
                                      processInstances[i].comment = "";
                                      processInstances[i].documents = documents;
                                      processInstances[i].workflowHeader = workflowHeader;
                                      processInstances[i].employee = employee;
                                      processInstances[i].employeeOtherDetails = employeeOtherDetails;
                                      //processInstances[i].leaves= leaves;                      
                                      processInstances[i].pensionCalculationDetails = pensionCalculationDetails;
                                      processInstances[i].pensionCalculationUpdateDetails = pensionCalculationUpdateDetails;
                                      processInstances[i].dependents = dependents;
                                      processInstances[i].actorAcccessLevel = actorAcccessLevel;
                                      processInstances[i].employeeDisability = employeeDisability;
                                    }
                                    /*
                                    let applicationDetails={
                                      businessId: processInstances[0].businessId,
                                      name: processInstances[0].employee.user.name,
                                      dob: epochToDmy(intConversion(processInstances[0].employee.user.dob)) ,
                                      dateOfRetirement: epochToDmy(intConversion(processInstances[0].employee.dateOfRetirement)) ,                
                                      permanentAddress: processInstances[0].employee.user.permanentAddress,
                                      permanentCity: processInstances[0].employee.user.permanentCity,
                                      permanentPinCode: processInstances[0].employee.user.permanentPinCode,
                                      serviceStatus: processInstances[0].employee.serviceHistory[0].serviceStatus,
                                      serviceFrom: epochToDmy(intConversion( processInstances[0].employee.serviceHistory[0].serviceFrom)) ,                
                                      serviceTo: processInstances[0].employee.serviceHistory[0].serviceTo!=null? epochToDmy(intConversion( processInstances[0].employee.serviceHistory[0].serviceTo)):null,
                                      reasonForRetirement: processInstances[0].employeeOtherDetails.reasonForRetirement,
                                      isEligibleForPension: processInstances[0].employeeOtherDetails.isEligibleForPension,
                                      isTakenMonthlyPensionAndGratuity: processInstances[0].employeeOtherDetails.isTakenMonthlyPensionAndGratuity,
                                      isTakenGratuityCommutationTerminalBenefit: processInstances[0].employeeOtherDetails.isTakenGratuityCommutationTerminalBenefit,
                                      isTakenCompensationPensionAndGratuity: processInstances[0].employeeOtherDetails.isTakenCompensationPensionAndGratuity,
                                      totalNoPayLeaves: processInstances[0].employeeOtherDetails.totalNoPayLeaves,
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
                                      isCommutationOpted: processInstances[0].employeeOtherDetails.isCommutationOpted,
                                      isCommutationOpted: processInstances[0].employeeOtherDetails.isCommutationOpted,
                                    }
                                     let paymentDetails={         
                                      businessId: processInstances[0].businessId,       
                                      name: processInstances[0].employee.user.name,  
                                      dob: epochToDmy(intConversion(processInstances[0].employee.user.dob)) ,
                                      dateOfRetirement: epochToDmy(intConversion(processInstances[0].employee.dateOfRetirement)) ,                             
                                      permanentAddress: processInstances[0].employee.user.permanentAddress,
                                      permanentCity: processInstances[0].employee.user.permanentCity,
                                      permanentPinCode: processInstances[0].employee.user.permanentPinCode,
                                      serviceFrom: epochToDmy(intConversion( processInstances[0].employee.serviceHistory[0].serviceFrom)) ,                                
                                      lpd: processInstances[0].employeeOtherDetails.lpd,                
                                      nqsYearVerified: processInstances[0].pensionCalculationUpdateDetails.nqsYearVerified,
                                      nqsMonthVerified: processInstances[0].pensionCalculationUpdateDetails.nqsMonthVerified,
                                      nqsDayVerified: processInstances[0].pensionCalculationUpdateDetails.nqsDayVerified,
                                      finalCalculatedPensionVerified: processInstances[0].pensionCalculationUpdateDetails.finalCalculatedPensionVerified,
                                      dcrgVerified: processInstances[0].pensionCalculationUpdateDetails.dcrgVerified,
                                      accountNumber: processInstances[0].employeeOtherDetails.accountNumber,
                                      bankAddress: processInstances[0].employeeOtherDetails.bankAddress
                                    }
                                    */

                                    response.ProcessInstances = processInstances;
                                    //response.ApplicationDetails=applicationDetails;  
                                    //response.PaymentDetails=paymentDetails;
                                    res.json(response);

                                  case 81:
                                  case "end":
                                    return _context6.stop();
                                }
                              }
                            }, _callee6, undefined);
                          })(), "t0", 5);

                        case 5:
                        case "end":
                          return _context7.stop();
                      }
                    }
                  }, _callee7, undefined);
                }));

                return function (_x6, _x7) {
                  return _ref4.apply(this, arguments);
                };
              }());

            case 86:
            case "end":
              return _context8.stop();
          }
        }
      }, _callee8, undefined);
    }));

    return function (_x, _x2, _x3) {
      return _ref2.apply(this, arguments);
    };
  }()));
  return api;
};
//# sourceMappingURL=searchWorkflow.js.map