"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.mergeMonthlyPensionDrawn = exports.searchByMobileNumber = exports.mergeMigratedPensionerResults = exports.mergeSearchApplicationResults = exports.mergeSearchClosedApplicationResults = exports.mergeWorkflowAccessibilty = exports.mergePensionerFinalCalculatedBenefit = exports.mergePensionCalculationUpdateDetails = exports.mergePensionCalculationDetails = exports.mergeEmployeeOtherDetails = exports.mergeSearchEmployee = exports.mergeIsEmployeeExistInPensionModule = exports.mergeWorkflowHeader = exports.mergeWorkflowDocumentAuditSearchResults = exports.mergeEmployeeDisabilityResults = exports.mergePensionEmployeeResults = exports.mergeServiceHistoryResults = exports.mergeAssignmentResults = exports.mergeDependentResults = exports.mergeLeaveSearchResults = exports.mergePensionRevisionResults = exports.mergeSearchPensionRegisterResults = exports.mergeSearchPensionerForPensionRevisionResults = exports.mergeSearchPensionerResults = exports.mergeWorkflowDocumentSearchResults = exports.mergeEmployeeAssigmentResults = exports.mergeSearchResults = exports.booleanConversion = exports.floatConversion = exports.intConversion = undefined;

var _typeof2 = require("babel-runtime/helpers/typeof");

var _typeof3 = _interopRequireDefault(_typeof2);

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _findIndex = require("lodash/findIndex");

var _findIndex2 = _interopRequireDefault(_findIndex);

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _api = require("./api");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _userService = require("../services/userService");

var _userService2 = _interopRequireDefault(_userService);

var _omitBy = require("lodash/omitBy");

var _omitBy2 = _interopRequireDefault(_omitBy);

var _isNil = require("lodash/isNil");

var _isNil2 = _interopRequireDefault(_isNil);

var _utils = require("../utils");

var _encryption = require("../utils/encryption");

var _filter = require("lodash/filter");

var _filter2 = _interopRequireDefault(_filter);

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var requestInfo = {};

var intConversion = exports.intConversion = function intConversion(string) {
  return string ? parseInt(string) : null;
};

var floatConversion = exports.floatConversion = function floatConversion(string) {
  return string ? parseFloat(string) : null;
};

var booleanConversion = exports.booleanConversion = function booleanConversion(string) {
  return string ? string === "true" ? true : false : null;
};

var employeeRowMapper = function () {
  var _ref = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var employee;
    return _regenerator2.default.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            employee = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            employee.pensionNotificationRegisterId = row.pension_notification_register_id;
            employee.pensionEmployeeId = row.pension_employee_id;
            employee.id = row.employee_hrms_id;
            employee.code = row.employee_hrms_code;
            employee.name = row.name;
            employee.dob = Number(row.date_of_birth);
            //employee.designation = row.designation;
            employee.dateOfRetirement = Number(row.date_of_retirement);
            employee.tenantId = row.tenantid;
            employee.gender = row.gender;
            employee.employeeStatus = row.employee_status;
            employee.employeeType = row.employee_type;
            employee.dateOfAppointment = row.date_of_appointment ? Number(row.date_of_appointment) : null;
            employee.action = "INITIATE";
            return _context.abrupt("return", employee);

          case 15:
          case "end":
            return _context.stop();
        }
      }
    }, _callee, undefined);
  }));

  return function employeeRowMapper(_x) {
    return _ref.apply(this, arguments);
  };
}();

var assignmentRowMapper = function () {
  var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var assignment;
    return _regenerator2.default.wrap(function _callee2$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            assignment = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            assignment.position = Number(row.position);
            assignment.designation = row.designation;
            assignment.department = row.department;
            assignment.fromDate = Number(row.from_date);
            assignment.toDate = row.to_date ? Number(row.to_date) : null;
            assignment.govtOrderNumber = row.govt_order_no;
            assignment.tenantId = row.tenantid;
            assignment.reportingTo = row.reporting_to;
            assignment.isHOD = row.is_hod;
            assignment.isCurrentAssignment = row.is_current_assignment;
            assignment.isPrimaryAssignment = row.is_primary_assignment;

            return _context2.abrupt("return", assignment);

          case 13:
          case "end":
            return _context2.stop();
        }
      }
    }, _callee2, undefined);
  }));

  return function assignmentRowMapper(_x3) {
    return _ref2.apply(this, arguments);
  };
}();

var serviceHistoryRowMapper = function () {
  var _ref3 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee3(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var serviceHistory;
    return _regenerator2.default.wrap(function _callee3$(_context3) {
      while (1) {
        switch (_context3.prev = _context3.next) {
          case 0:
            serviceHistory = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            serviceHistory.serviceStatus = row.service_status;
            serviceHistory.serviceFrom = Number(row.service_from);
            serviceHistory.serviceTo = row.service_to ? Number(row.service_to) : null;
            serviceHistory.orderNo = row.order_no;
            serviceHistory.location = row.location;
            serviceHistory.tenantId = row.tenantid;
            serviceHistory.isCurrentPosition = row.is_current_position;

            return _context3.abrupt("return", serviceHistory);

          case 9:
          case "end":
            return _context3.stop();
        }
      }
    }, _callee3, undefined);
  }));

  return function serviceHistoryRowMapper(_x5) {
    return _ref3.apply(this, arguments);
  };
}();

var pensionEmployeeRowMapper = function () {
  var _ref4 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee4(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var employee;
    return _regenerator2.default.wrap(function _callee4$(_context4) {
      while (1) {
        switch (_context4.prev = _context4.next) {
          case 0:
            employee = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            employee.uuid = row.uuid;
            employee.tenantId = row.tenantid;
            employee.hrmsId = Number(row.employee_hrms_id);
            employee.code = row.employee_hrms_code;
            employee.name = row.name;
            employee.dob = intConversion(row.date_of_birth);
            employee.dateOfRetirement = intConversion(row.date_of_retirement);
            employee.hrmsUuid = row.employee_hrms_uuid;
            employee.salutation = row.salutation;
            employee.gender = row.gender;
            employee.employeeStatus = row.employee_status;
            employee.employeeType = row.employee_type;
            employee.department = row.department;
            employee.designation = row.designation;

            return _context4.abrupt("return", employee);

          case 16:
          case "end":
            return _context4.stop();
        }
      }
    }, _callee4, undefined);
  }));

  return function pensionEmployeeRowMapper(_x7) {
    return _ref4.apply(this, arguments);
  };
}();

var employeeDisabilityRowMapper = function () {
  var _ref5 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee5(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var employee;
    return _regenerator2.default.wrap(function _callee5$(_context5) {
      while (1) {
        switch (_context5.prev = _context5.next) {
          case 0:
            employee = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            employee.tenantId = row.tenantid;
            employee.severityOfDisability = row.severity_of_disability;
            employee.disabilityPercentage = row.disability_percentage != null ? Number(row.disability_percentage) : null;
            employee.dateOfInjury = row.date_of_injury != null ? Number(row.date_of_injury) : null;
            employee.injuryApplicationDate = row.injury_application_date != null ? Number(row.injury_application_date) : null;
            employee.woundExtraordinaryPension = row.wound_extraordinary_pension != null ? Number(row.wound_extraordinary_pension) : null;
            employee.attendantAllowanceGranted = row.attendant_allowance_granted != null ? row.attendant_allowance_granted : false;
            employee.comments = row.comments;
            return _context5.abrupt("return", employee);

          case 10:
          case "end":
            return _context5.stop();
        }
      }
    }, _callee5, undefined);
  }));

  return function employeeDisabilityRowMapper(_x9) {
    return _ref5.apply(this, arguments);
  };
}();

var workflowDocumentRowMapper = function () {
  var _ref6 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee6(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var document;
    return _regenerator2.default.wrap(function _callee6$(_context6) {
      while (1) {
        switch (_context6.prev = _context6.next) {
          case 0:
            document = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            document.pensionAttachmentId = row.uuid;
            document.fileStoreId = row.file_store_id;
            document.documentType = row.document_type;
            return _context6.abrupt("return", document);

          case 5:
          case "end":
            return _context6.stop();
        }
      }
    }, _callee6, undefined);
  }));

  return function workflowDocumentRowMapper(_x11) {
    return _ref6.apply(this, arguments);
  };
}();

var searchPensionerRowMapper = function () {
  var _ref7 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee7(row, mdms) {
    var mapper = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
    var mdmsBankDetails, pensioner, bankDetailsList;
    return _regenerator2.default.wrap(function _callee7$(_context7) {
      while (1) {
        switch (_context7.prev = _context7.next) {
          case 0:
            mdmsBankDetails = (0, _get2.default)(mdms, "MdmsRes.pension.BankDetails");
            pensioner = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            pensioner.pensionerId = row.uuid;
            pensioner.tenantId = row.tenantid;
            pensioner.name = row.name;
            pensioner.code = row.employee_hrms_code;
            pensioner.pensionerNumber = row.pensioner_number;
            pensioner.businessService = row.business_service;
            pensioner.dob = Number(row.date_of_birth);
            pensioner.gender = row.gender;
            pensioner.dateOfRetirement = Number(row.date_of_retirement);
            pensioner.dateOfDeath = row.date_of_death != null ? Number(row.date_of_death) : null;
            pensioner.dateOfAppointment = row.date_of_appointment != null ? Number(row.date_of_appointment) : null;
            pensioner.lpd = row.lpd != null ? Number(row.lpd) : null;
            pensioner.ltc = row.ltc != null ? Number(row.ltc) : null;
            pensioner.wef = row.wef != null ? Number(row.wef) : null;
            pensioner.claimantName = row.claimant_name;
            pensioner.claimantDob = row.claimant_dob;
            pensioner.address = row.address;

            bankDetailsList = (0, _filter2.default)(mdmsBankDetails, function (x) {
              return x.code == row.bank_details && row.bank_details != null && row.bank_details != "";
            });

            if (bankDetailsList.length > 0) {
              pensioner.bankDetails = bankDetailsList[0].name;
            }

            //pensioner.bankDetails = row.bank_details; 

            pensioner.bankAccountNumber = row.bank_account_number != null ? (0, _encryption.decrypt)(row.bank_account_number) : row.bank_account_number;
            pensioner.bankCode = row.bank_code;
            pensioner.bankIfsc = row.bank_ifsc;
            pensioner.designation = row.designation;
            pensioner.department = row.department;
            return _context7.abrupt("return", pensioner);

          case 27:
          case "end":
            return _context7.stop();
        }
      }
    }, _callee7, undefined);
  }));

  return function searchPensionerRowMapper(_x13, _x14) {
    return _ref7.apply(this, arguments);
  };
}();

var searchPensionerPensionRevisionRowMapper = function () {
  var _ref8 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee8(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var revision;
    return _regenerator2.default.wrap(function _callee8$(_context8) {
      while (1) {
        switch (_context8.prev = _context8.next) {
          case 0:
            revision = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            revision.pensionRevisionId = row.uuid;
            revision.effectiveStartYear = intConversion(row.effective_start_year);
            revision.effectiveStartMonth = intConversion(row.effective_start_month);
            revision.effectiveEndYear = intConversion(row.effective_end_year);
            revision.effectiveEndMonth = intConversion(row.effective_end_month);
            revision.pensionArrear = row.pension_arrear != null ? Number(row.pension_arrear) : 0;
            //revision.medicalRelief = row.medical_relief!=null? Number(row.medical_relief):0;   
            revision.fma = row.fma != null ? Number(row.fma) : 0;
            revision.miscellaneous = row.miscellaneous != null ? Number(row.miscellaneous) : 0;
            revision.overPayment = row.over_payment != null ? Number(row.over_payment) : 0;
            revision.incomeTax = row.income_tax != null ? Number(row.income_tax) : 0;
            revision.cess = row.cess != null ? Number(row.cess) : 0;
            revision.basicPension = row.basic_pension != null ? Number(row.basic_pension) : 0;
            revision.da = row.da != null ? Number(row.da) : 0;
            revision.commutedPension = row.commuted_pension != null ? Number(row.commuted_pension) : 0;
            revision.netDeductions = row.net_deductions != null ? Number(row.net_deductions) : 0;
            revision.finalCalculatedPension = row.final_calculated_pension != null ? Number(row.final_calculated_pension) : 0;
            revision.additionalPension = row.additional_pension ? Number(row.additional_pension) : 0;
            revision.interimRelief = row.interim_relief ? Number(row.interim_relief) : 0;
            revision.totalPension = row.total_pension ? Number(row.total_pension) : 0;
            revision.pensionDeductions = row.pension_deductions ? Number(row.pension_deductions) : 0;
            revision.woundExtraordinaryPension = row.wound_extraordinary_pension ? Number(row.wound_extraordinary_pension) : 0;
            revision.attendantAllowance = row.attendant_allowance ? Number(row.attendant_allowance) : 0;
            revision.remarks = row.remarks;
            //revision.isEditEnabled =new Date(intConversion(row.effective_start_year),intConversion(row.effective_start_month),1)>new Date()?true:false;
            revision.isEditEnabled = Number(row.effective_start_year) < new Date().getFullYear() || Number(row.effective_start_year) == new Date().getFullYear() && Number(row.effective_start_month) < new Date().getMonth() + 1 ? false : true;
            return _context8.abrupt("return", revision);

          case 26:
          case "end":
            return _context8.stop();
        }
      }
    }, _callee8, undefined);
  }));

  return function searchPensionerPensionRevisionRowMapper(_x16) {
    return _ref8.apply(this, arguments);
  };
}();

var searchPensionRegisterRowMapper = function () {
  var _ref9 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee9(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var register;
    return _regenerator2.default.wrap(function _callee9$(_context9) {
      while (1) {
        switch (_context9.prev = _context9.next) {
          case 0:
            register = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            register.tenantId = row.tenantid;
            register.effectiveYear = intConversion(row.effective_year);
            register.effectiveMonth = intConversion(row.effective_month);
            register.pensionArrear = row.pension_arrear != null ? Number(row.pension_arrear) : 0;
            register.fma = row.fma != null ? Number(row.fma) : 0;
            register.miscellaneous = row.miscellaneous != null ? Number(row.miscellaneous) : 0;
            register.overPayment = row.over_payment != null ? Number(row.over_payment) : 0;
            register.incomeTax = row.income_tax != null ? Number(row.income_tax) : 0;
            register.cess = row.cess != null ? Number(row.cess) : 0;
            register.basicPension = row.basic_pension != null ? Number(row.basic_pension) : 0;
            register.additionalPension = row.additional_pension ? Number(row.additional_pension) : 0;
            register.commutedPension = row.commuted_pension != null ? Number(row.commuted_pension) : 0;
            register.netDeductions = row.net_deductions != null ? Number(row.net_deductions) : 0;
            register.finalCalculatedPension = row.final_calculated_pension != null ? Number(row.final_calculated_pension) : 0;
            register.interimRelief = row.interim_relief ? Number(row.interim_relief) : 0;
            register.da = row.da != null ? Number(row.da) : 0;
            register.totalPension = row.total_pension != null ? Number(row.total_pension) : 0;
            register.pensionDeductions = row.pension_deductions ? Number(row.pension_deductions) : 0;
            register.woundExtraordinaryPension = row.wound_extraordinary_pension != null ? Number(row.wound_extraordinary_pension) : 0;
            register.attendantAllowance = row.attendant_allowance != null ? Number(row.attendant_allowance) : 0;

            return _context9.abrupt("return", register);

          case 22:
          case "end":
            return _context9.stop();
        }
      }
    }, _callee9, undefined);
  }));

  return function searchPensionRegisterRowMapper(_x18) {
    return _ref9.apply(this, arguments);
  };
}();

var pensionRevisionRowMapper = function () {
  var _ref10 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee10(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var revision;
    return _regenerator2.default.wrap(function _callee10$(_context10) {
      while (1) {
        switch (_context10.prev = _context10.next) {
          case 0:
            revision = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            revision.tenantId = row.tenantid;
            revision.pensionRevisionId = row.uuid;
            revision.pensionerId = row.pensioner_id;
            revision.dateOfBirth = row.date_of_birth;
            revision.dateOfRetirement = row.date_of_retirement;
            revision.effectiveStartYear = intConversion(row.effective_start_year);
            revision.effectiveStartMonth = intConversion(row.effective_start_month);
            revision.effectiveEndYear = intConversion(row.effective_end_year);
            revision.effectiveEndMonth = intConversion(row.effective_end_month);
            revision.pensionArrear = row.pension_arrear != null ? Number(row.pension_arrear) : 0;
            revision.fma = row.fma != null ? Number(row.fma) : 0;
            revision.miscellaneous = row.miscellaneous != null ? Number(row.miscellaneous) : 0;
            revision.overPayment = row.over_payment != null ? Number(row.over_payment) : 0;
            revision.incomeTax = row.income_tax != null ? Number(row.income_tax) : 0;
            revision.cess = row.cess != null ? Number(row.cess) : 0;
            revision.basicPension = row.basic_pension != null ? Number(row.basic_pension) : 0;
            revision.commutedPension = row.commuted_pension != null ? Number(row.commuted_pension) : 0;
            revision.additionalPension = row.additional_pension != null ? Number(row.additional_pension) : 0;
            revision.netDeductions = row.net_deductions != null ? Number(row.net_deductions) : 0;
            revision.finalCalculatedPension = row.final_calculated_pension != null ? Number(row.final_calculated_pension) : 0;
            revision.interimRelief = row.interim_relief != null ? Number(row.interim_relief) : 0;
            revision.da = row.da != null ? Number(row.da) : 0;
            revision.totalPension = row.total_pension != null ? Number(row.total_pension) : 0;
            revision.pensionDeductions = row.pension_deductions != null ? Number(row.pension_deductions) : 0;
            revision.pensionerFinalCalculatedBenefitId = row.pensioner_final_calculated_benefit_id;
            revision.woundExtraOrdinaryPension = row.wound_extraordinary_pension != null ? Number(row.wound_extraordinary_pension) : 0;
            revision.attendantAllowance = row.attendant_allowance != null ? Number(row.attendant_allowance) : 0;

            return _context10.abrupt("return", revision);

          case 29:
          case "end":
            return _context10.stop();
        }
      }
    }, _callee10, undefined);
  }));

  return function pensionRevisionRowMapper(_x20) {
    return _ref10.apply(this, arguments);
  };
}();

var leaveRowMapper = function () {
  var _ref11 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee11(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var leave;
    return _regenerator2.default.wrap(function _callee11$(_context11) {
      while (1) {
        switch (_context11.prev = _context11.next) {
          case 0:
            leave = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            leave.leaveType = row.leave_type;
            leave.leaveFrom = Number(row.leave_from);
            leave.leaveTo = Number(row.leave_to);
            leave.leaveCount = row.leave_count;
            return _context11.abrupt("return", leave);

          case 6:
          case "end":
            return _context11.stop();
        }
      }
    }, _callee11, undefined);
  }));

  return function leaveRowMapper(_x22) {
    return _ref11.apply(this, arguments);
  };
}();

var dependentRowMapper = function () {
  var _ref12 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee12(row, mdms) {
    var mapper = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
    var mdmsBankDetails, dependent, bankDetailsList;
    return _regenerator2.default.wrap(function _callee12$(_context12) {
      while (1) {
        switch (_context12.prev = _context12.next) {
          case 0:
            mdmsBankDetails = (0, _get2.default)(mdms, "MdmsRes.pension.BankDetails");
            dependent = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            dependent.name = row.name;
            dependent.dob = Number(row.dob);
            dependent.address = row.address;
            dependent.mobileNumber = row.mobile_number;
            dependent.relationship = row.relationship;
            dependent.isDisabled = row.is_disabled;
            dependent.maritalStatus = row.marital_status;
            dependent.isHollyDependent = row.is_holly_dependent;
            dependent.noSpouseNoChildren = row.no_spouse_no_children;
            dependent.isGrandChildFromDeceasedSon = row.is_grandchild_from_deceased_son;
            dependent.isEligibleForGratuity = row.is_eligible_for_gratuity;
            dependent.isEligibleForPension = row.is_eligible_for_pension;
            dependent.gratuityPercentage = Number(row.gratuity_percentage);
            dependent.bankAccountNumber = row.bank_account_number != null ? (0, _encryption.decrypt)(row.bank_account_number) : row.bank_account_number;

            bankDetailsList = (0, _filter2.default)(mdmsBankDetails, function (x) {
              return x.code == row.bank_details && row.bank_details != null && row.bank_details != "";
            });

            if (bankDetailsList.length > 0) {
              dependent.bankName = bankDetailsList[0].name;
            }
            dependent.bankDetails = row.bank_details;
            dependent.bankCode = row.bank_code;
            dependent.bankIfsc = row.bank_ifsc;
            return _context12.abrupt("return", dependent);

          case 22:
          case "end":
            return _context12.stop();
        }
      }
    }, _callee12, undefined);
  }));

  return function dependentRowMapper(_x24, _x25) {
    return _ref12.apply(this, arguments);
  };
}();

var workflowDocumentAuditRowMapper = function () {
  var _ref13 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee13(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var documentAudit;
    return _regenerator2.default.wrap(function _callee13$(_context13) {
      while (1) {
        switch (_context13.prev = _context13.next) {
          case 0:
            documentAudit = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            documentAudit.documentType = row.document_type;
            documentAudit.state = row.state;
            documentAudit.comment = row.comment;
            documentAudit.createdBy = row.created_by;
            return _context13.abrupt("return", documentAudit);

          case 6:
          case "end":
            return _context13.stop();
        }
      }
    }, _callee13, undefined);
  }));

  return function workflowDocumentAuditRowMapper(_x27) {
    return _ref13.apply(this, arguments);
  };
}();

var workflowHeaderRowMapper = function () {
  var _ref14 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee14(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var workflowHeader;
    return _regenerator2.default.wrap(function _callee14$(_context14) {
      while (1) {
        switch (_context14.prev = _context14.next) {
          case 0:
            workflowHeader = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            workflowHeader.workflowHeaderId = row.uuid;
            workflowHeader.applicationDate = intConversion(row.application_date);
            //workflowHeader.dateOfContingent =row.date_of_contingent;
            return _context14.abrupt("return", workflowHeader);

          case 4:
          case "end":
            return _context14.stop();
        }
      }
    }, _callee14, undefined);
  }));

  return function workflowHeaderRowMapper(_x29) {
    return _ref14.apply(this, arguments);
  };
}();

var isEmployeeExistInPensionModuleRowMapper = function () {
  var _ref15 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee15(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var isEmployeeExistInPensionModule;
    return _regenerator2.default.wrap(function _callee15$(_context15) {
      while (1) {
        switch (_context15.prev = _context15.next) {
          case 0:
            isEmployeeExistInPensionModule = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            isEmployeeExistInPensionModule.pensionEmployeeId = row.uuid;
            isEmployeeExistInPensionModule.code = row.employee_hrms_code;
            return _context15.abrupt("return", isEmployeeExistInPensionModule);

          case 4:
          case "end":
            return _context15.stop();
        }
      }
    }, _callee15, undefined);
  }));

  return function isEmployeeExistInPensionModuleRowMapper(_x31) {
    return _ref15.apply(this, arguments);
  };
}();

var searchEmployeeRowMapper = function () {
  var _ref16 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee16(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var employee;
    return _regenerator2.default.wrap(function _callee16$(_context16) {
      while (1) {
        switch (_context16.prev = _context16.next) {
          case 0:
            employee = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            employee.pensionEmployeeId = row.uuid;
            employee.uuid = row.employee_hrms_uuid;
            employee.tenantId = row.tenantid;
            employee.id = intConversion(row.employee_hrms_id), employee.code = row.employee_hrms_code;
            employee.employeeStatus = row.employee_status;
            employee.employeeType = row.employee_type;
            employee.dateOfAppointment = intConversion(row.date_of_appointment);
            employee.dateOfRetirement = intConversion(row.date_of_retirement);
            employee.dateOfDeath = row.date_of_death ? intConversion(row.date_of_death) : null;
            employee.assignments = [], employee.serviceHistory = [], employee.user = {
              id: intConversion(row.employee_hrms_id),
              uuid: row.employee_hrms_uuid,
              salutation: row.salutation,
              name: row.name,
              gender: row.gender,
              dob: intConversion(row.date_of_birth),
              tenantId: row.tenantid,
              mobileNumber: row.mobile_number ? row.mobile_number : null,
              emailId: row.email_id ? row.email_id : null,
              altContactNumber: row.alt_contact_number ? row.alt_contact_number : null,
              pan: row.pan ? row.pan : null,
              aadhaarNumber: row.aadhaar_number ? row.aadhaar_number : null,
              permanentAddress: row.permanent_address ? row.permanent_address : null,
              permanentCity: row.permanent_city ? row.permanent_city : null,
              permanentPinCode: row.permanent_pin_code ? row.permanent_pin_code : null,
              correspondenceAddress: row.correspondence_address ? row.correspondence_address : null,
              correspondenceCity: row.correspondence_city ? row.correspondence_city : null,
              correspondencePinCode: row.correspondence_pin_code ? row.correspondence_pin_code : null,
              fatherOrHusbandName: row.father_or_husband_name ? row.father_or_husband_name : null,
              bloodGroup: row.blood_group ? row.blood_group : null,
              identificationMark: row.identification_mark ? row.identification_mark : null
            };

            return _context16.abrupt("return", employee);

          case 12:
          case "end":
            return _context16.stop();
        }
      }
    }, _callee16, undefined);
  }));

  return function searchEmployeeRowMapper(_x33) {
    return _ref16.apply(this, arguments);
  };
}();

var employeeOtherDetailsRowMapper = function () {
  var _ref17 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee17(row, mdms) {
    var mapper = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
    var mdmsBankDetails, employeeOtherDetails, bankDetailsList;
    return _regenerator2.default.wrap(function _callee17$(_context17) {
      while (1) {
        switch (_context17.prev = _context17.next) {
          case 0:
            mdmsBankDetails = (0, _get2.default)(mdms, "MdmsRes.pension.BankDetails");
            employeeOtherDetails = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            employeeOtherDetails.state = row.workflow_state;
            employeeOtherDetails.ltc = Number(row.ltc);
            employeeOtherDetails.lpd = Number(row.lpd);
            employeeOtherDetails.pensionArrear = Number(row.pension_arrear);
            employeeOtherDetails.isDaMedicalAdmissible = row.is_da_medical_admissible;
            employeeOtherDetails.fma = Number(row.fma);
            employeeOtherDetails.medicalRelief = Number(row.medical_relief);
            employeeOtherDetails.miscellaneous = Number(row.miscellaneous);
            employeeOtherDetails.overPayment = Number(row.over_payment);
            employeeOtherDetails.incomeTax = Number(row.income_tax);
            employeeOtherDetails.cess = Number(row.cess);
            employeeOtherDetails.bankAddress = row.bank_address;
            bankDetailsList = (0, _filter2.default)(mdmsBankDetails, function (x) {
              return x.code == row.bank_address && row.bank_address != null && row.bank_address != "";
            });

            if (bankDetailsList.length > 0) {
              employeeOtherDetails.bankName = bankDetailsList[0].name;
            }
            employeeOtherDetails.accountNumber = row.account_number != null ? (0, _encryption.decrypt)(row.account_number) : row.account_number;
            employeeOtherDetails.bankCode = row.bank_code;
            employeeOtherDetails.bankIfsc = row.bank_ifsc;
            employeeOtherDetails.claimant = row.claimant;
            employeeOtherDetails.wef = row.wef && row.wef != 0 ? Number(row.wef) : null;
            employeeOtherDetails.dateOfContingent = row.date_of_contingent ? Number(row.date_of_contingent) : null;
            employeeOtherDetails.totalNoPayLeavesDays = Number(row.total_no_pay_leaves_days);
            employeeOtherDetails.dues = Number(row.dues);
            employeeOtherDetails.isEmploymentActive = row.is_employment_active;
            employeeOtherDetails.isConvictedSeriousCrimeOrGraveMisconduct = row.is_convicted_serious_crime_or_grave_misconduct;
            employeeOtherDetails.isAnyJudicialProceedingIsContinuing = row.is_any_judicial_proceeding_is_continuing;
            employeeOtherDetails.isAnyMisconductInsolvencyInefficiency = row.is_any_misconduct_insolvency_inefficiency;
            employeeOtherDetails.isEmployeeDiesInTerroristAttack = row.is_employee_dies_in_terrorist_attack;
            employeeOtherDetails.isEmployeeDiesInAccidentalDeath = row.is_employee_dies_in_accidental_death;
            employeeOtherDetails.isCommutationOpted = row.is_commutation_opted;
            employeeOtherDetails.reasonForRetirement = row.reason_for_retirement;
            employeeOtherDetails.isEligibleForPension = row.is_eligible_for_pension;
            employeeOtherDetails.isDuesPresent = row.is_dues_present;
            employeeOtherDetails.isDuesAmountDecided = row.is_dues_amount_decided;
            employeeOtherDetails.isTakenMonthlyPensionAndGratuity = row.is_taken_monthly_pension_and_gratuity;
            employeeOtherDetails.isTakenGratuityCommutationTerminalBenefit = row.is_taken_gratuity_commutation_terminal_benefit;
            employeeOtherDetails.isTakenCompensationPensionAndGratuity = row.is_taken_compensation_pension_and_gratuity;
            employeeOtherDetails.diesInExtremistsDacoitsSmugglerAntisocialAttack = row.dies_in_extremists_dacoits_smuggler_antisocial_attack;
            employeeOtherDetails.isCompassionatePensionGranted = row.is_compassionate_pension_granted;
            employeeOtherDetails.totalNoPayLeavesMonths = Number(row.total_no_pay_leaves_months);
            employeeOtherDetails.totalNoPayLeavesYears = Number(row.total_no_pay_leaves_years);
            employeeOtherDetails.noDuesForAvailGovtAccomodation = row.no_dues_for_avail_govt_accomodation;
            employeeOtherDetails.employeeGroup = row.employee_group;
            return _context17.abrupt("return", employeeOtherDetails);

          case 45:
          case "end":
            return _context17.stop();
        }
      }
    }, _callee17, undefined);
  }));

  return function employeeOtherDetailsRowMapper(_x35, _x36) {
    return _ref17.apply(this, arguments);
  };
}();

var pensionCalculationDetailsRowMapper = function () {
  var _ref18 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee18(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var pensionCalculationDetails;
    return _regenerator2.default.wrap(function _callee18$(_context18) {
      while (1) {
        switch (_context18.prev = _context18.next) {
          case 0:
            pensionCalculationDetails = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            pensionCalculationDetails.basicPensionSystem = row.basic_pension_sytem != null ? Number(row.basic_pension_sytem) : null;
            pensionCalculationDetails.pensionDeductionsSystem = row.pension_deductions_system != null ? Number(row.pension_deductions_system) : null;
            pensionCalculationDetails.additionalPensionSystem = row.additional_pension_system != null ? Number(row.additional_pension_system) : null;
            pensionCalculationDetails.commutedPensionSystem = row.commuted_pension_system != null ? Number(row.commuted_pension_system) : null;
            pensionCalculationDetails.commutedValueSystem = row.commuted_value_system != null ? Number(row.commuted_value_system) : null;
            pensionCalculationDetails.familyPensionISystem = row.family_pension_i_system != null ? Number(row.family_pension_i_system) : null;
            pensionCalculationDetails.familyPensionIISystem = row.family_pension_ii_system != null ? Number(row.family_pension_ii_system) : null;
            pensionCalculationDetails.dcrgSystem = row.dcrg_system != null ? Number(row.dcrg_system) : null;
            pensionCalculationDetails.netDeductionsSystem = row.net_deductions_system != null ? Number(row.net_deductions_system) : null;
            pensionCalculationDetails.finalCalculatedPensionSystem = row.final_calculated_pension_system != null ? Number(row.final_calculated_pension_system) : null;
            pensionCalculationDetails.interimReliefSystem = row.interim_relief_system != null ? Number(row.interim_relief_system) : null;
            pensionCalculationDetails.daSystem = row.da_system != null ? Number(row.da_system) : null;
            pensionCalculationDetails.nqsYearSystem = row.nqs_year_system != null ? Number(row.nqs_year_system) : null;
            pensionCalculationDetails.nqsMonthSystem = row.nqs_month_system != null ? Number(row.nqs_month_system) : null;
            pensionCalculationDetails.nqsDaySystem = row.nqs_day_system != null ? Number(row.nqs_day_system) : null;
            pensionCalculationDetails.duesDeductionsSystem = row.dues_deductions_system != null ? Number(row.dues_deductions_system) : null;
            pensionCalculationDetails.compassionatePensionSystem = row.compassionate_pension_system != null ? Number(row.compassionate_pension_system) : null;
            pensionCalculationDetails.compensationPensionSystem = row.compensation_pension_system != null ? Number(row.compensation_pension_system) : null;
            pensionCalculationDetails.terminalBenefitSystem = row.terminal_benefit_system != null ? Number(row.terminal_benefit_system) : null;
            pensionCalculationDetails.finalCalculatedGratuitySystem = row.final_calculated_gratuity_system != null ? Number(row.final_calculated_gratuity_system) : null;
            pensionCalculationDetails.familyPensionIStartDateSystem = intConversion(row.family_pension_i_start_date_system);
            pensionCalculationDetails.familyPensionIEndDateSystem = intConversion(row.family_pension_i_end_date_system);
            pensionCalculationDetails.familyPensionIIStartDateSystem = intConversion(row.family_pension_ii_start_date_system);
            pensionCalculationDetails.exGratiaSystem = row.ex_gratia_system != null ? Number(row.ex_gratia_system) : null;
            pensionCalculationDetails.pensionerFamilyPensionSystem = row.pensioner_family_pension_system != null ? Number(row.pensioner_family_pension_system) : null;
            pensionCalculationDetails.totalPensionSystem = row.total_pension_system != null ? Number(row.total_pension_system) : null;
            pensionCalculationDetails.provisionalPensionSystem = row.provisional_pension_system != null ? Number(row.provisional_pension_system) : null;

            pensionCalculationDetails.interimReliefApplicable = row.interim_relief_applicable;
            pensionCalculationDetails.interimReliefExpression = row.interim_relief_expression;
            pensionCalculationDetails.basicPensionApplicable = row.basic_pension_applicable;
            pensionCalculationDetails.basicPensionExpression = row.basic_pension_expression;
            pensionCalculationDetails.provisionalPensionApplicable = row.provisional_pension_applicable;
            pensionCalculationDetails.provisionalPensionExpression = row.provisional_pension_expression;
            pensionCalculationDetails.compassionatePensionApplicable = row.compassionate_pension_applicable;
            pensionCalculationDetails.compassionatePensionExpression = row.compassionate_pension_expression;
            pensionCalculationDetails.compensationPensionApplicable = row.compensation_pension_applicable;
            pensionCalculationDetails.compensationPensionExpression = row.compensation_pension_expression;
            pensionCalculationDetails.commutedPensionApplicable = row.commuted_pension_applicable;
            pensionCalculationDetails.commutedPensionExpression = row.commuted_pension_expression;
            pensionCalculationDetails.familyPensionIApplicable = row.family_pension_i_applicable;
            pensionCalculationDetails.familyPensionIExpression = row.family_pension_i_expression;
            pensionCalculationDetails.familyPensionIIApplicable = row.family_pension_ii_applicable;
            pensionCalculationDetails.familyPensionIIExpression = row.family_pension_ii_expression;
            pensionCalculationDetails.daApplicable = row.da_applicable;
            pensionCalculationDetails.daExpression = row.da_expression;
            pensionCalculationDetails.additionalPensionApplicable = row.additional_pension_applicable;
            pensionCalculationDetails.additionalPensionExpression = row.additional_pension_expression;
            pensionCalculationDetails.totalPensionApplicable = row.total_pension_applicable;
            pensionCalculationDetails.totalPensionExpression = row.total_pension_expression;
            pensionCalculationDetails.pensionDeductionsApplicable = row.pension_deductions_applicable;
            pensionCalculationDetails.pensionDeductionsExpression = row.pension_deductions_expression;
            pensionCalculationDetails.netDeductionsApplicable = row.net_deductions_applicable;
            pensionCalculationDetails.netDeductionsExpression = row.net_deductions_expression;
            pensionCalculationDetails.finalCalculatedPensionApplicable = row.final_calculated_pension_applicable;
            pensionCalculationDetails.finalCalculatedPensionExpression = row.final_calculated_pension_expression;
            pensionCalculationDetails.commutationValueApplicable = row.commutation_value_applicable;
            pensionCalculationDetails.commutationValueExpression = row.commutation_value_expression;
            pensionCalculationDetails.dcrgApplicable = row.dcrg_applicable;
            pensionCalculationDetails.dcrgExpression = row.dcrg_expression;
            pensionCalculationDetails.terminalBenefitApplicable = row.terminal_benefit_applicable;
            pensionCalculationDetails.terminalBenefitExpression = row.terminal_benefit_expression;
            pensionCalculationDetails.duesDeductionsApplicable = row.dues_deductions_applicable;
            pensionCalculationDetails.duesDeductionsExpression = row.dues_deductions_expression;
            pensionCalculationDetails.finalCalculatedGratuityApplicable = row.final_calculated_gratuity_applicable;
            pensionCalculationDetails.finalCalculatedGratuityExpression = row.final_calculated_gratuity_expression;
            pensionCalculationDetails.exGratiaApplicable = row.ex_gratia_applicable;
            pensionCalculationDetails.exGratiaExpression = row.ex_gratia_expression;
            pensionCalculationDetails.pensionerFamilyPensionApplicable = row.pensioner_family_pension_applicable;
            pensionCalculationDetails.pensionerFamilyPensionExpression = row.pensioner_family_pension_expression;

            pensionCalculationDetails.invalidPensionSystem = row.invalid_pension_system != null ? Number(row.invalid_pension_system) : null;
            pensionCalculationDetails.woundExtraordinaryPensionSystem = row.wound_extraordinary_pension_system != null ? Number(row.wound_extraordinary_pension_system) : null;
            pensionCalculationDetails.attendantAllowanceSystem = row.attendant_allowance_system != null ? Number(row.attendant_allowance_system) : null;

            pensionCalculationDetails.invalidPensionApplicable = row.invalid_pension_applicable;
            pensionCalculationDetails.invalidPensionExpression = row.invalid_pension_expression;

            pensionCalculationDetails.woundExtraordinaryPensionApplicable = row.wound_extraordinary_pension_applicable;
            pensionCalculationDetails.woundExtraordinaryPensionExpression = row.wound_extraordinary_pension_expression;

            pensionCalculationDetails.attendantAllowanceApplicable = row.attendant_allowance_applicable;
            pensionCalculationDetails.attendantAllowanceExpression = row.attendant_allowance_expression;

            pensionCalculationDetails.gqsYearSystem = Number(row.gqs_year_system);
            pensionCalculationDetails.gqsMonthSystem = Number(row.gqs_month_system);
            pensionCalculationDetails.gqsDaySystem = Number(row.gqs_day_system);

            pensionCalculationDetails.notificationTextSystem = row.notification_text_system;
            pensionCalculationDetails.interimReliefLpdSystem = row.interim_relief_lpd_system != null ? Number(row.interim_relief_lpd_system) : null;

            return _context18.abrupt("return", pensionCalculationDetails);

          case 85:
          case "end":
            return _context18.stop();
        }
      }
    }, _callee18, undefined);
  }));

  return function pensionCalculationDetailsRowMapper(_x38) {
    return _ref18.apply(this, arguments);
  };
}();

var pensionCalculationUpdateDetailsRowMapper = function () {
  var _ref19 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee19(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var pensionCalculationUpdateDetails;
    return _regenerator2.default.wrap(function _callee19$(_context19) {
      while (1) {
        switch (_context19.prev = _context19.next) {
          case 0:
            pensionCalculationUpdateDetails = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            pensionCalculationUpdateDetails.basicPensionVerified = row.basic_pension_verified != null ? Number(row.basic_pension_verified) : null;
            pensionCalculationUpdateDetails.pensionDeductionsVerified = row.pension_deductions_verified != null ? Number(row.pension_deductions_verified) : null;
            pensionCalculationUpdateDetails.additionalPensionVerified = row.additional_pension_verified != null ? Number(row.additional_pension_verified) : null;
            pensionCalculationUpdateDetails.commutedPensionVerified = row.commuted_pension_verified != null ? Number(row.commuted_pension_verified) : null;
            pensionCalculationUpdateDetails.commutedValueVerified = row.commuted_value_verified != null ? Number(row.commuted_value_verified) : null;
            pensionCalculationUpdateDetails.familyPensionIVerified = row.family_pension_i_verified != null ? Number(row.family_pension_i_verified) : null;
            pensionCalculationUpdateDetails.familyPensionIIVerified = row.family_pension_ii_verified != null ? Number(row.family_pension_ii_verified) : null;
            pensionCalculationUpdateDetails.dcrgVerified = row.dcrg_verified != null ? Number(row.dcrg_verified) : null;
            pensionCalculationUpdateDetails.netDeductionsVerified = row.net_deductions_verified != null ? Number(row.net_deductions_verified) : null;
            pensionCalculationUpdateDetails.finalCalculatedPensionVerified = row.final_calculated_pension_verified != null ? Number(row.final_calculated_pension_verified) : null;
            pensionCalculationUpdateDetails.interimReliefVerified = row.interim_relief_verified != null ? Number(row.interim_relief_verified) : null;
            pensionCalculationUpdateDetails.daVerified = row.da_verified != null ? Number(row.da_verified) : null;
            pensionCalculationUpdateDetails.nqsYearVerified = row.nqs_year_verified != null ? Number(row.nqs_year_verified) : null;
            pensionCalculationUpdateDetails.nqsMonthVerified = row.nqs_month_verified != null ? Number(row.nqs_month_verified) : null;
            pensionCalculationUpdateDetails.nqsDayVerified = row.nqs_day_verified != null ? Number(row.nqs_day_verified) : null;
            pensionCalculationUpdateDetails.duesDeductionsVerified = row.dues_deductions_verified != null ? Number(row.dues_deductions_verified) : null;
            pensionCalculationUpdateDetails.compassionatePensionVerified = row.compassionate_pension_verified != null ? Number(row.compassionate_pension_verified) : null;
            pensionCalculationUpdateDetails.compensationPensionVerified = row.compensation_pension_verified != null ? Number(row.compensation_pension_verified) : null;
            pensionCalculationUpdateDetails.terminalBenefitVerified = row.terminal_benefit_verified != null ? Number(row.terminal_benefit_verified) : null;
            pensionCalculationUpdateDetails.finalCalculatedGratuityVerified = row.final_calculated_gratuity_verified != null ? Number(row.final_calculated_gratuity_verified) : null;
            //pensionCalculationUpdateDetails.additionalFamilyPensionIVerified = row.additional_family_pension_i_verified!=null? Number(row.additional_family_pension_i_verified):null;   
            //pensionCalculationUpdateDetails.additionalFamilyPensionIIVerified = row.additional_family_pension_ii_verified!=null? Number(row.additional_family_pension_ii_verified):null; 
            pensionCalculationUpdateDetails.familyPensionIStartDateVerified = intConversion(row.family_pension_i_start_date_verified);
            pensionCalculationUpdateDetails.familyPensionIEndDateVerified = intConversion(row.family_pension_i_end_date_verified);
            pensionCalculationUpdateDetails.familyPensionIIStartDateVerified = intConversion(row.family_pension_ii_start_date_verified);
            pensionCalculationUpdateDetails.exGratiaVerified = row.ex_gratia_verified != null ? Number(row.ex_gratia_verified) : null;
            pensionCalculationUpdateDetails.pensionerFamilyPensionVerified = row.pensioner_family_pension_verified != null ? Number(row.pensioner_family_pension_verified) : null;
            //pensionCalculationUpdateDetails.additionalPensionerFamilyPensionVerified = row.additional_pensioner_family_pension_verified!=null? Number(row.additional_pensioner_family_pension_verified):null;
            pensionCalculationUpdateDetails.totalPensionVerified = row.total_pension_verified != null ? Number(row.total_pension_verified) : null;
            pensionCalculationUpdateDetails.provisionalPensionVerified = row.provisional_pension_verified != null ? Number(row.provisional_pension_verified) : null;

            pensionCalculationUpdateDetails.invalidPensionVerified = row.invalid_pension_verified != null ? Number(row.invalid_pension_verified) : null;
            pensionCalculationUpdateDetails.woundExtraordinaryPensionVerified = row.wound_extraordinary_pension_verified != null ? Number(row.wound_extraordinary_pension_verified) : null;
            pensionCalculationUpdateDetails.attendantAllowanceVerified = row.attendant_allowance_verified != null ? Number(row.attendant_allowance_verified) : null;

            pensionCalculationUpdateDetails.gqsYearVerified = Number(row.gqs_year_verified);
            pensionCalculationUpdateDetails.gqsMonthVerified = Number(row.gqs_month_verified);
            pensionCalculationUpdateDetails.gqsDayVerified = Number(row.gqs_day_verified);

            pensionCalculationUpdateDetails.notificationTextVerified = row.notification_text_verified;
            return _context19.abrupt("return", pensionCalculationUpdateDetails);

          case 36:
          case "end":
            return _context19.stop();
        }
      }
    }, _callee19, undefined);
  }));

  return function pensionCalculationUpdateDetailsRowMapper(_x40) {
    return _ref19.apply(this, arguments);
  };
}();

var pensionerFinalCalculatedBenefitRowMapper = function () {
  var _ref20 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee20(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var pensionerFinalCalculatedBenefitDetails;
    return _regenerator2.default.wrap(function _callee20$(_context20) {
      while (1) {
        switch (_context20.prev = _context20.next) {
          case 0:
            pensionerFinalCalculatedBenefitDetails = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            pensionerFinalCalculatedBenefitDetails.basicPension = row.basic_pension != null ? Number(row.basic_pension) : null;
            pensionerFinalCalculatedBenefitDetails.pensionDeductions = row.pension_deductions != null ? Number(row.pension_deductions) : null;
            pensionerFinalCalculatedBenefitDetails.additionalPension = row.additional_pension != null ? Number(row.additional_pension) : null;
            pensionerFinalCalculatedBenefitDetails.commutedPension = row.commuted_pension != null ? Number(row.commuted_pension) : null;
            pensionerFinalCalculatedBenefitDetails.commutedValue = row.commuted_value != null ? Number(row.commuted_value) : null;
            pensionerFinalCalculatedBenefitDetails.familyPensionI = row.family_pension_i != null ? Number(row.family_pension_i) : null;
            pensionerFinalCalculatedBenefitDetails.familyPensionII = row.family_pension_ii != null ? Number(row.family_pension_ii) : null;
            pensionerFinalCalculatedBenefitDetails.dcrg = row.dcrg != null ? Number(row.dcrg) : null;
            pensionerFinalCalculatedBenefitDetails.netDeductions = row.net_deductions != null ? Number(row.net_deductions) : null;
            pensionerFinalCalculatedBenefitDetails.finalCalculatedPension = row.final_calculated_pension != null ? Number(row.final_calculated_pension) : null;
            pensionerFinalCalculatedBenefitDetails.interimRelief = row.interim_relief != null ? Number(row.interim_relief) : null;
            pensionerFinalCalculatedBenefitDetails.da = row.da != null ? Number(row.da) : null;
            pensionerFinalCalculatedBenefitDetails.nqsYear = row.nqs_year != null ? Number(row.nqs_year) : null;
            pensionerFinalCalculatedBenefitDetails.nqsMonth = row.nqs_month != null ? Number(row.nqs_month) : null;
            pensionerFinalCalculatedBenefitDetails.nqsDay = row.nqs_day != null ? Number(row.nqs_day) : null;
            pensionerFinalCalculatedBenefitDetails.duesDeductions = row.dues_deductions != null ? Number(row.dues_deductions) : null;
            pensionerFinalCalculatedBenefitDetails.compassionatePension = row.compassionate_pension != null ? Number(row.compassionate_pension) : null;
            pensionerFinalCalculatedBenefitDetails.compensationPension = row.compensation_pension != null ? Number(row.compensation_pension) : null;
            pensionerFinalCalculatedBenefitDetails.terminalBenefit = row.terminal_benefit != null ? Number(row.terminal_benefit) : null;
            pensionerFinalCalculatedBenefitDetails.finalCalculatedGratuity = row.final_calculated_gratuity != null ? Number(row.final_calculated_gratuity) : null;
            //pensionerFinalCalculatedBenefitDetails.additionalFamilyPensionI = row.additional_family_pension_i!=null? Number(row.additional_family_pension_i):null;   
            //pensionerFinalCalculatedBenefitDetails.additionalFamilyPensionII = row.additional_family_pension_ii!=null? Number(row.additional_family_pension_ii):null; 
            pensionerFinalCalculatedBenefitDetails.familyPensionIStartDate = intConversion(row.family_pension_i_start_date);
            pensionerFinalCalculatedBenefitDetails.familyPensionIEndDate = intConversion(row.family_pension_i_end_date);
            pensionerFinalCalculatedBenefitDetails.familyPensionIIStartDate = intConversion(row.family_pension_ii_start_date);
            pensionerFinalCalculatedBenefitDetails.exGratia = row.ex_gratia != null ? Number(row.ex_gratia) : null;
            pensionerFinalCalculatedBenefitDetails.pensionerFamilyPension = row.pensioner_family_pension != null ? Number(row.pensioner_family_pension) : null;
            //pensionerFinalCalculatedBenefitDetails.additionalPensionerFamilyPension = row.additional_pensioner_family_pension!=null? Number(row.additional_pensioner_family_pension):null;
            pensionerFinalCalculatedBenefitDetails.totalPension = row.total_pension != null ? Number(row.total_pension) : null;
            pensionerFinalCalculatedBenefitDetails.provisionalPension = row.provisional_pension != null ? Number(row.provisional_pension) : null;
            pensionerFinalCalculatedBenefitDetails.woundExtraordinaryPension = row.wound_extraordinary_pension != null ? Number(row.wound_extraordinary_pension) : null;
            pensionerFinalCalculatedBenefitDetails.attendantAllowance = row.attendant_allowance != null ? Number(row.attendant_allowance) : null;
            pensionerFinalCalculatedBenefitDetails.invalidPension = row.invalid_pension != null ? Number(row.invalid_pension) : null;

            return _context20.abrupt("return", pensionerFinalCalculatedBenefitDetails);

          case 32:
          case "end":
            return _context20.stop();
        }
      }
    }, _callee20, undefined);
  }));

  return function pensionerFinalCalculatedBenefitRowMapper(_x42) {
    return _ref20.apply(this, arguments);
  };
}();

var workflowAccessibiltyRowMapper = function () {
  var _ref21 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee21(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var workflowAccessibilty;
    return _regenerator2.default.wrap(function _callee21$(_context21) {
      while (1) {
        switch (_context21.prev = _context21.next) {
          case 0:
            workflowAccessibilty = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            workflowAccessibilty.code = row.employee_hrms_code;
            workflowAccessibilty.assignee = row.assignee;
            workflowAccessibilty.assigneeName = "";
            workflowAccessibilty.isClaimEnabled = false;
            workflowAccessibilty.isReleaseEnabled = false;
            workflowAccessibilty.isViewEnabled = false;
            return _context21.abrupt("return", workflowAccessibilty);

          case 8:
          case "end":
            return _context21.stop();
        }
      }
    }, _callee21, undefined);
  }));

  return function workflowAccessibiltyRowMapper(_x44) {
    return _ref21.apply(this, arguments);
  };
}();

var mergeSearchResults = exports.mergeSearchResults = function () {
  var _ref22 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee22(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, employee, index;
    return _regenerator2.default.wrap(function _callee22$(_context22) {
      while (1) {
        switch (_context22.prev = _context22.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context22.next = 20;
              break;
            }

            employee = {};
            index = (0, _findIndex2.default)(result, { id: response[i].employee_hrms_id });

            if (!(index != -1)) {
              _context22.next = 13;
              break;
            }

            _context22.next = 9;
            return employeeRowMapper(response[i], result[index]);

          case 9:
            employee = _context22.sent;

            result[index] = employee;
            _context22.next = 17;
            break;

          case 13:
            _context22.next = 15;
            return employeeRowMapper(response[i]);

          case 15:
            employee = _context22.sent;

            result.push(employee);

          case 17:
            i++;
            _context22.next = 3;
            break;

          case 20:
            return _context22.abrupt("return", result);

          case 21:
          case "end":
            return _context22.stop();
        }
      }
    }, _callee22, undefined);
  }));

  return function mergeSearchResults(_x46) {
    return _ref22.apply(this, arguments);
  };
}();

var mergeEmployeeAssigmentResults = exports.mergeEmployeeAssigmentResults = function () {
  var _ref23 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee23(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, employee, index;
    return _regenerator2.default.wrap(function _callee23$(_context23) {
      while (1) {
        switch (_context23.prev = _context23.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context23.next = 20;
              break;
            }

            employee = {};
            index = (0, _findIndex2.default)(result, { id: response[i].employee_hrms_id });

            if (!(index != -1)) {
              _context23.next = 13;
              break;
            }

            _context23.next = 9;
            return employeeRowMapper(response[i], result[index]);

          case 9:
            employee = _context23.sent;

            result[index] = employee;
            _context23.next = 17;
            break;

          case 13:
            _context23.next = 15;
            return employeeRowMapper(response[i]);

          case 15:
            employee = _context23.sent;

            result.push(employee);

          case 17:
            i++;
            _context23.next = 3;
            break;

          case 20:
            return _context23.abrupt("return", result);

          case 21:
          case "end":
            return _context23.stop();
        }
      }
    }, _callee23, undefined);
  }));

  return function mergeEmployeeAssigmentResults(_x48) {
    return _ref23.apply(this, arguments);
  };
}();

var mergeWorkflowDocumentSearchResults = exports.mergeWorkflowDocumentSearchResults = function () {
  var _ref24 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee24(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, document;
    return _regenerator2.default.wrap(function _callee24$(_context24) {
      while (1) {
        switch (_context24.prev = _context24.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context24.next = 12;
              break;
            }

            document = {};
            _context24.next = 7;
            return workflowDocumentRowMapper(response[i]);

          case 7:
            document = _context24.sent;

            result.push(document);

          case 9:
            i++;
            _context24.next = 3;
            break;

          case 12:
            return _context24.abrupt("return", result);

          case 13:
          case "end":
            return _context24.stop();
        }
      }
    }, _callee24, undefined);
  }));

  return function mergeWorkflowDocumentSearchResults(_x50) {
    return _ref24.apply(this, arguments);
  };
}();

var mergeSearchPensionerResults = exports.mergeSearchPensionerResults = function () {
  var _ref25 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee25(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var mdms = arguments[3];
    var result, i, pensioner;
    return _regenerator2.default.wrap(function _callee25$(_context25) {
      while (1) {
        switch (_context25.prev = _context25.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context25.next = 12;
              break;
            }

            pensioner = {};
            _context25.next = 7;
            return searchPensionerRowMapper(response[i], mdms);

          case 7:
            pensioner = _context25.sent;

            result.push(pensioner);

          case 9:
            i++;
            _context25.next = 3;
            break;

          case 12:
            return _context25.abrupt("return", result);

          case 13:
          case "end":
            return _context25.stop();
        }
      }
    }, _callee25, undefined);
  }));

  return function mergeSearchPensionerResults(_x52) {
    return _ref25.apply(this, arguments);
  };
}();

var mergeSearchPensionerForPensionRevisionResults = exports.mergeSearchPensionerForPensionRevisionResults = function () {
  var _ref26 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee26(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];

    var pensionRevision, result, i, _pensionRevision;

    return _regenerator2.default.wrap(function _callee26$(_context26) {
      while (1) {
        switch (_context26.prev = _context26.next) {
          case 0:
            requestInfo = reqInfo;
            pensionRevision = {
              tenantId: response[0].tenantid,
              pensioner: {
                pensionerId: response[0].pensioner_id,
                pensionEmployeeId: response[0].pension_employee_id,
                //workflowHeaderId: response[0].workflow_header_id,
                pensionerFinalCalculatedBenefitId: response[0].pensioner_final_calculated_benefit_id,
                businessService: response[0].business_service,
                pensionerNumber: response[0].pensioner_number,
                name: response[0].name
              },
              pensionRevision: []
            };
            result = [];
            i = 0;

          case 4:
            if (!(i < response.length)) {
              _context26.next = 13;
              break;
            }

            _pensionRevision = {};
            _context26.next = 8;
            return searchPensionerPensionRevisionRowMapper(response[i]);

          case 8:
            _pensionRevision = _context26.sent;

            result.push(_pensionRevision);

          case 10:
            i++;
            _context26.next = 4;
            break;

          case 13:
            pensionRevision.pensionRevision = result;

            return _context26.abrupt("return", pensionRevision);

          case 15:
          case "end":
            return _context26.stop();
        }
      }
    }, _callee26, undefined);
  }));

  return function mergeSearchPensionerForPensionRevisionResults(_x54) {
    return _ref26.apply(this, arguments);
  };
}();

var mergeSearchPensionRegisterResults = exports.mergeSearchPensionRegisterResults = function () {
  var _ref27 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee27(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, register;
    return _regenerator2.default.wrap(function _callee27$(_context27) {
      while (1) {
        switch (_context27.prev = _context27.next) {
          case 0:
            requestInfo = reqInfo;

            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context27.next = 12;
              break;
            }

            register = {};
            _context27.next = 7;
            return searchPensionRegisterRowMapper(response[i]);

          case 7:
            register = _context27.sent;

            result.push(register);

          case 9:
            i++;
            _context27.next = 3;
            break;

          case 12:
            return _context27.abrupt("return", result);

          case 13:
          case "end":
            return _context27.stop();
        }
      }
    }, _callee27, undefined);
  }));

  return function mergeSearchPensionRegisterResults(_x56) {
    return _ref27.apply(this, arguments);
  };
}();

var mergePensionRevisionResults = exports.mergePensionRevisionResults = function () {
  var _ref28 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee28(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, pensionRevision;
    return _regenerator2.default.wrap(function _callee28$(_context28) {
      while (1) {
        switch (_context28.prev = _context28.next) {
          case 0:
            requestInfo = reqInfo;

            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context28.next = 12;
              break;
            }

            pensionRevision = {};
            _context28.next = 7;
            return pensionRevisionRowMapper(response[i]);

          case 7:
            pensionRevision = _context28.sent;

            result.push(pensionRevision);

          case 9:
            i++;
            _context28.next = 3;
            break;

          case 12:
            return _context28.abrupt("return", result);

          case 13:
          case "end":
            return _context28.stop();
        }
      }
    }, _callee28, undefined);
  }));

  return function mergePensionRevisionResults(_x58) {
    return _ref28.apply(this, arguments);
  };
}();

var mergeLeaveSearchResults = exports.mergeLeaveSearchResults = function () {
  var _ref29 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee29(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, leave;
    return _regenerator2.default.wrap(function _callee29$(_context29) {
      while (1) {
        switch (_context29.prev = _context29.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context29.next = 12;
              break;
            }

            leave = {};
            _context29.next = 7;
            return leaveRowMapper(response[i]);

          case 7:
            leave = _context29.sent;

            result.push(leave);

          case 9:
            i++;
            _context29.next = 3;
            break;

          case 12:
            return _context29.abrupt("return", result);

          case 13:
          case "end":
            return _context29.stop();
        }
      }
    }, _callee29, undefined);
  }));

  return function mergeLeaveSearchResults(_x60) {
    return _ref29.apply(this, arguments);
  };
}();

var mergeDependentResults = exports.mergeDependentResults = function () {
  var _ref30 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee30(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var mdms = arguments[3];
    var result, i, dependent;
    return _regenerator2.default.wrap(function _callee30$(_context30) {
      while (1) {
        switch (_context30.prev = _context30.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context30.next = 12;
              break;
            }

            dependent = {};
            _context30.next = 7;
            return dependentRowMapper(response[i], mdms);

          case 7:
            dependent = _context30.sent;

            result.push(dependent);

          case 9:
            i++;
            _context30.next = 3;
            break;

          case 12:
            return _context30.abrupt("return", result);

          case 13:
          case "end":
            return _context30.stop();
        }
      }
    }, _callee30, undefined);
  }));

  return function mergeDependentResults(_x62) {
    return _ref30.apply(this, arguments);
  };
}();

var mergeAssignmentResults = exports.mergeAssignmentResults = function () {
  var _ref31 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee31(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, assignment;
    return _regenerator2.default.wrap(function _callee31$(_context31) {
      while (1) {
        switch (_context31.prev = _context31.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context31.next = 12;
              break;
            }

            assignment = {};
            _context31.next = 7;
            return assignmentRowMapper(response[i]);

          case 7:
            assignment = _context31.sent;

            result.push(assignment);

          case 9:
            i++;
            _context31.next = 3;
            break;

          case 12:
            return _context31.abrupt("return", result);

          case 13:
          case "end":
            return _context31.stop();
        }
      }
    }, _callee31, undefined);
  }));

  return function mergeAssignmentResults(_x64) {
    return _ref31.apply(this, arguments);
  };
}();

var mergeServiceHistoryResults = exports.mergeServiceHistoryResults = function () {
  var _ref32 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee32(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, serviceHistory;
    return _regenerator2.default.wrap(function _callee32$(_context32) {
      while (1) {
        switch (_context32.prev = _context32.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context32.next = 12;
              break;
            }

            serviceHistory = {};
            _context32.next = 7;
            return serviceHistoryRowMapper(response[i]);

          case 7:
            serviceHistory = _context32.sent;

            result.push(serviceHistory);

          case 9:
            i++;
            _context32.next = 3;
            break;

          case 12:
            return _context32.abrupt("return", result);

          case 13:
          case "end":
            return _context32.stop();
        }
      }
    }, _callee32, undefined);
  }));

  return function mergeServiceHistoryResults(_x66) {
    return _ref32.apply(this, arguments);
  };
}();

var mergePensionEmployeeResults = exports.mergePensionEmployeeResults = function () {
  var _ref33 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee33(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, employee;
    return _regenerator2.default.wrap(function _callee33$(_context33) {
      while (1) {
        switch (_context33.prev = _context33.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context33.next = 12;
              break;
            }

            employee = {};
            _context33.next = 7;
            return pensionEmployeeRowMapper(response[i]);

          case 7:
            employee = _context33.sent;

            result.push(employee);

          case 9:
            i++;
            _context33.next = 3;
            break;

          case 12:
            return _context33.abrupt("return", result);

          case 13:
          case "end":
            return _context33.stop();
        }
      }
    }, _callee33, undefined);
  }));

  return function mergePensionEmployeeResults(_x68) {
    return _ref33.apply(this, arguments);
  };
}();

var mergeEmployeeDisabilityResults = exports.mergeEmployeeDisabilityResults = function () {
  var _ref34 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee34(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, employee;
    return _regenerator2.default.wrap(function _callee34$(_context34) {
      while (1) {
        switch (_context34.prev = _context34.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context34.next = 12;
              break;
            }

            employee = {};
            _context34.next = 7;
            return employeeDisabilityRowMapper(response[i]);

          case 7:
            employee = _context34.sent;

            result.push(employee);

          case 9:
            i++;
            _context34.next = 3;
            break;

          case 12:
            return _context34.abrupt("return", result);

          case 13:
          case "end":
            return _context34.stop();
        }
      }
    }, _callee34, undefined);
  }));

  return function mergeEmployeeDisabilityResults(_x70) {
    return _ref34.apply(this, arguments);
  };
}();

var mergeWorkflowDocumentAuditSearchResults = exports.mergeWorkflowDocumentAuditSearchResults = function () {
  var _ref35 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee35(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, documentAudit, createdBy, userResponse;
    return _regenerator2.default.wrap(function _callee35$(_context35) {
      while (1) {
        switch (_context35.prev = _context35.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context35.next = 12;
              break;
            }

            documentAudit = {};
            _context35.next = 7;
            return workflowDocumentAuditRowMapper(response[i]);

          case 7:
            documentAudit = _context35.sent;

            result.push(documentAudit);

          case 9:
            i++;
            _context35.next = 3;
            break;

          case 12:
            i = 0;

          case 13:
            if (!(i < result.length)) {
              _context35.next = 23;
              break;
            }

            createdBy = "";
            _context35.next = 17;
            return searchUser(reqInfo, result[i].createdBy);

          case 17:
            userResponse = _context35.sent;


            createdBy = userResponse.name;

            result[i].createdBy = createdBy;

          case 20:
            i++;
            _context35.next = 13;
            break;

          case 23:
            return _context35.abrupt("return", result);

          case 24:
          case "end":
            return _context35.stop();
        }
      }
    }, _callee35, undefined);
  }));

  return function mergeWorkflowDocumentAuditSearchResults(_x72) {
    return _ref35.apply(this, arguments);
  };
}();

var mergeWorkflowHeader = exports.mergeWorkflowHeader = function () {
  var _ref36 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee36(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result;
    return _regenerator2.default.wrap(function _callee36$(_context36) {
      while (1) {
        switch (_context36.prev = _context36.next) {
          case 0:
            requestInfo = reqInfo;
            _context36.next = 3;
            return workflowHeaderRowMapper(response[0]);

          case 3:
            result = _context36.sent;
            ;
            return _context36.abrupt("return", result);

          case 6:
          case "end":
            return _context36.stop();
        }
      }
    }, _callee36, undefined);
  }));

  return function mergeWorkflowHeader(_x74) {
    return _ref36.apply(this, arguments);
  };
}();

var mergeIsEmployeeExistInPensionModule = exports.mergeIsEmployeeExistInPensionModule = function () {
  var _ref37 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee37(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result;
    return _regenerator2.default.wrap(function _callee37$(_context37) {
      while (1) {
        switch (_context37.prev = _context37.next) {
          case 0:
            requestInfo = reqInfo;
            _context37.next = 3;
            return isEmployeeExistInPensionModuleRowMapper(response[0]);

          case 3:
            result = _context37.sent;
            ;
            return _context37.abrupt("return", result);

          case 6:
          case "end":
            return _context37.stop();
        }
      }
    }, _callee37, undefined);
  }));

  return function mergeIsEmployeeExistInPensionModule(_x76) {
    return _ref37.apply(this, arguments);
  };
}();

var mergeSearchEmployee = exports.mergeSearchEmployee = function () {
  var _ref38 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee38(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result;
    return _regenerator2.default.wrap(function _callee38$(_context38) {
      while (1) {
        switch (_context38.prev = _context38.next) {
          case 0:
            requestInfo = reqInfo;
            _context38.next = 3;
            return searchEmployeeRowMapper(response[0]);

          case 3:
            result = _context38.sent;
            ;
            return _context38.abrupt("return", result);

          case 6:
          case "end":
            return _context38.stop();
        }
      }
    }, _callee38, undefined);
  }));

  return function mergeSearchEmployee(_x78) {
    return _ref38.apply(this, arguments);
  };
}();

var mergeEmployeeOtherDetails = exports.mergeEmployeeOtherDetails = function () {
  var _ref39 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee39(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var mdms = arguments[3];
    var result;
    return _regenerator2.default.wrap(function _callee39$(_context39) {
      while (1) {
        switch (_context39.prev = _context39.next) {
          case 0:
            requestInfo = reqInfo;
            _context39.next = 3;
            return employeeOtherDetailsRowMapper(response[0], mdms);

          case 3:
            result = _context39.sent;
            ;
            return _context39.abrupt("return", result);

          case 6:
          case "end":
            return _context39.stop();
        }
      }
    }, _callee39, undefined);
  }));

  return function mergeEmployeeOtherDetails(_x80) {
    return _ref39.apply(this, arguments);
  };
}();

var mergePensionCalculationDetails = exports.mergePensionCalculationDetails = function () {
  var _ref40 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee40(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result;
    return _regenerator2.default.wrap(function _callee40$(_context40) {
      while (1) {
        switch (_context40.prev = _context40.next) {
          case 0:
            requestInfo = reqInfo;
            _context40.next = 3;
            return pensionCalculationDetailsRowMapper(response[0]);

          case 3:
            result = _context40.sent;
            ;
            return _context40.abrupt("return", result);

          case 6:
          case "end":
            return _context40.stop();
        }
      }
    }, _callee40, undefined);
  }));

  return function mergePensionCalculationDetails(_x82) {
    return _ref40.apply(this, arguments);
  };
}();

var mergePensionCalculationUpdateDetails = exports.mergePensionCalculationUpdateDetails = function () {
  var _ref41 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee41(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result;
    return _regenerator2.default.wrap(function _callee41$(_context41) {
      while (1) {
        switch (_context41.prev = _context41.next) {
          case 0:
            requestInfo = reqInfo;
            _context41.next = 3;
            return pensionCalculationUpdateDetailsRowMapper(response[0]);

          case 3:
            result = _context41.sent;
            ;
            return _context41.abrupt("return", result);

          case 6:
          case "end":
            return _context41.stop();
        }
      }
    }, _callee41, undefined);
  }));

  return function mergePensionCalculationUpdateDetails(_x84) {
    return _ref41.apply(this, arguments);
  };
}();

var mergePensionerFinalCalculatedBenefit = exports.mergePensionerFinalCalculatedBenefit = function () {
  var _ref42 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee42(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result;
    return _regenerator2.default.wrap(function _callee42$(_context42) {
      while (1) {
        switch (_context42.prev = _context42.next) {
          case 0:
            requestInfo = reqInfo;
            _context42.next = 3;
            return pensionerFinalCalculatedBenefitRowMapper(response[0]);

          case 3:
            result = _context42.sent;
            ;
            return _context42.abrupt("return", result);

          case 6:
          case "end":
            return _context42.stop();
        }
      }
    }, _callee42, undefined);
  }));

  return function mergePensionerFinalCalculatedBenefit(_x86) {
    return _ref42.apply(this, arguments);
  };
}();

var mergeWorkflowAccessibilty = exports.mergeWorkflowAccessibilty = function () {
  var _ref43 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee43(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, assigneeName, userResponse;
    return _regenerator2.default.wrap(function _callee43$(_context43) {
      while (1) {
        switch (_context43.prev = _context43.next) {
          case 0:
            requestInfo = reqInfo;

            _context43.next = 3;
            return workflowAccessibiltyRowMapper(response[0]);

          case 3:
            result = _context43.sent;
            ;

            if (!result.assignee) {
              _context43.next = 17;
              break;
            }

            assigneeName = "";
            _context43.next = 9;
            return searchUser(reqInfo, result.assignee);

          case 9:
            userResponse = _context43.sent;

            assigneeName = userResponse.name;
            result.assigneeName = assigneeName;
            result.isClaimEnabled = false;
            result.isReleaseEnabled = result.assignee === requestInfo.userInfo.uuid ? true : false;
            result.isViewEnabled = result.assignee === requestInfo.userInfo.uuid ? true : false;
            _context43.next = 21;
            break;

          case 17:
            result.assignee = "";
            result.isClaimEnabled = true;
            result.isReleaseEnabled = false;
            result.isViewEnabled = false;

          case 21:
            return _context43.abrupt("return", result);

          case 22:
          case "end":
            return _context43.stop();
        }
      }
    }, _callee43, undefined);
  }));

  return function mergeWorkflowAccessibilty(_x88) {
    return _ref43.apply(this, arguments);
  };
}();

var mergeSearchClosedApplicationResults = exports.mergeSearchClosedApplicationResults = function () {
  var _ref44 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee44(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, application;
    return _regenerator2.default.wrap(function _callee44$(_context44) {
      while (1) {
        switch (_context44.prev = _context44.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context44.next = 12;
              break;
            }

            application = {};
            _context44.next = 7;
            return searchClosedApplicationRowMapper(response[i]);

          case 7:
            application = _context44.sent;

            result.push(application);

          case 9:
            i++;
            _context44.next = 3;
            break;

          case 12:
            return _context44.abrupt("return", result);

          case 13:
          case "end":
            return _context44.stop();
        }
      }
    }, _callee44, undefined);
  }));

  return function mergeSearchClosedApplicationResults(_x90) {
    return _ref44.apply(this, arguments);
  };
}();

var searchClosedApplicationRowMapper = function () {
  var _ref45 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee45(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var application;
    return _regenerator2.default.wrap(function _callee45$(_context45) {
      while (1) {
        switch (_context45.prev = _context45.next) {
          case 0:
            application = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            application.workflowHeaderId = row.uuid;
            application.tenantId = row.tenantid;
            application.businessService = row.workflow_type;
            application.businessId = row.application_number;
            application.applicationDate = intConversion(row.application_date);
            application.lastModifiedDate = intConversion(row.last_modified_date);
            application.state = row.workflow_state;
            application.recomputedBusinessId = row.recomputed_application_number;
            application.pensionEmployeeId = row.pension_employee_id;
            application.code = row.employee_hrms_code;
            application.name = row.name;
            return _context45.abrupt("return", application);

          case 13:
          case "end":
            return _context45.stop();
        }
      }
    }, _callee45, undefined);
  }));

  return function searchClosedApplicationRowMapper(_x92) {
    return _ref45.apply(this, arguments);
  };
}();

var mergeSearchApplicationResults = exports.mergeSearchApplicationResults = function () {
  var _ref46 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee46(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, application;
    return _regenerator2.default.wrap(function _callee46$(_context46) {
      while (1) {
        switch (_context46.prev = _context46.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context46.next = 12;
              break;
            }

            application = {};
            _context46.next = 7;
            return searchApplicationRowMapper(response[i]);

          case 7:
            application = _context46.sent;

            result.push(application);

          case 9:
            i++;
            _context46.next = 3;
            break;

          case 12:
            return _context46.abrupt("return", result);

          case 13:
          case "end":
            return _context46.stop();
        }
      }
    }, _callee46, undefined);
  }));

  return function mergeSearchApplicationResults(_x94) {
    return _ref46.apply(this, arguments);
  };
}();

var searchApplicationRowMapper = function () {
  var _ref47 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee47(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var application;
    return _regenerator2.default.wrap(function _callee47$(_context47) {
      while (1) {
        switch (_context47.prev = _context47.next) {
          case 0:
            application = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            application.workflowHeaderId = row.uuid;
            application.tenantId = row.tenantid;
            application.businessService = row.workflow_type;
            application.businessId = row.application_number;
            application.applicationDate = intConversion(row.application_date);
            application.lastModifiedDate = intConversion(row.last_modified_date);
            application.state = row.workflow_state;
            application.pensionEmployeeId = row.pension_employee_id;
            application.code = row.employee_hrms_code;
            application.name = row.name;
            return _context47.abrupt("return", application);

          case 12:
          case "end":
            return _context47.stop();
        }
      }
    }, _callee47, undefined);
  }));

  return function searchApplicationRowMapper(_x96) {
    return _ref47.apply(this, arguments);
  };
}();

var mergeMigratedPensionerResults = exports.mergeMigratedPensionerResults = function () {
  var _ref48 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee48(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var result, i, assignment;
    return _regenerator2.default.wrap(function _callee48$(_context48) {
      while (1) {
        switch (_context48.prev = _context48.next) {
          case 0:
            requestInfo = reqInfo;
            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context48.next = 12;
              break;
            }

            assignment = {};
            _context48.next = 7;
            return migratedPensionerRowMapper(response[i]);

          case 7:
            assignment = _context48.sent;

            result.push(assignment);

          case 9:
            i++;
            _context48.next = 3;
            break;

          case 12:
            return _context48.abrupt("return", result);

          case 13:
          case "end":
            return _context48.stop();
        }
      }
    }, _callee48, undefined);
  }));

  return function mergeMigratedPensionerResults(_x98) {
    return _ref48.apply(this, arguments);
  };
}();

var migratedPensionerRowMapper = function () {
  var _ref49 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee49(row) {
    var mapper = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var pensioner;
    return _regenerator2.default.wrap(function _callee49$(_context49) {
      while (1) {
        switch (_context49.prev = _context49.next) {
          case 0:
            pensioner = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            pensioner.slNo = Number(row.sl_no);
            pensioner.name = row.name;
            pensioner.code = row.code;
            pensioner.dateOfBirth = row.date_of_birth != "NA" ? intConversion((0, _utils.convertDateToEpochForMigration)(String(row.date_of_birth).split("/").reverse().join("-"), "dob")) : null;
            pensioner.gender = row.gender;
            pensioner.dateOfDeath = row.date_of_death != "NA" ? intConversion((0, _utils.convertDateToEpochForMigration)(String(row.date_of_death).split("/").reverse().join("-"), "dob")) : null;
            pensioner.mobileNumber = row.mobile_number;
            pensioner.email = row.email;
            pensioner.address = row.address;
            pensioner.bankDetails = row.bank_details;
            pensioner.bankAccountNumber = row.bank_account_number != null ? (0, _encryption.encrypt)(row.bank_account_number) : row.bank_account_number;
            pensioner.bankIfsc = row.bank_ifsc;
            pensioner.bankCode = row.bank_code;
            pensioner.employeeStatus = row.employee_status;
            pensioner.employeeType = row.employee_type;
            pensioner.employeeGroup = row.employee_group;
            pensioner.designation = row.designation;
            pensioner.department = row.department;
            pensioner.dateOfJoining = row.date_of_joining != "NA" ? intConversion((0, _utils.convertDateToEpochForMigration)(String(row.date_of_joining).split("/").reverse().join("-"), "dob")) : null;
            pensioner.serviceEndDate = row.service_end_date != "NA" ? intConversion((0, _utils.convertDateToEpochForMigration)(String(row.service_end_date).split("/").reverse().join("-"), "dob")) : null;
            pensioner.dateOfRetirement = row.date_of_retirement != "NA" ? intConversion((0, _utils.convertDateToEpochForMigration)(String(row.date_of_retirement).split("/").reverse().join("-"), "dob")) : null;
            pensioner.dateOfContingent = row.date_of_contingent != "NA" ? intConversion((0, _utils.convertDateToEpochForMigration)(String(row.date_of_contingent).split("/").reverse().join("-"), "dob")) : null;
            pensioner.claimantName = row.claimant_name != "NA" ? row.claimant_name : null;
            pensioner.claimantDob = row.claimant_dob != "NA" ? intConversion((0, _utils.convertDateToEpochForMigration)(String(row.claimant_dob).split("/").reverse().join("-"), "dob")) : null;
            pensioner.claimantRelationship = row.claimant_relationship != "NA" ? row.claimant_relationship : null;
            pensioner.claimantMobileNumber = row.claimant_mobile_number != "NA" ? row.claimant_mobile_number : null;
            pensioner.claimantAddress = row.claimant_address != "NA" ? row.claimant_address : null;
            pensioner.claimantBankDetails = row.claimant_bank_details != "NA" ? row.claimant_bank_details : null;
            pensioner.claimantBankAccountNumber = row.claimant_bank_account_number != "NA" && row.claimant_bank_account_number != null ? (0, _encryption.encrypt)(row.claimant_bank_account_number) : null;
            pensioner.claimantBankIfsc = row.claimant_bank_ifsc != "NA" ? row.claimant_bank_ifsc : null;
            pensioner.claimantBankCode = row.claimant_bank_code != "NA" ? row.claimant_bank_code : null;
            pensioner.nqsYear = row.nqs_year != "NA" ? Number(row.nqs_year) : null;
            pensioner.nqsMonth = row.nqs_month != "NA" ? Number(row.nqs_month) : null;
            pensioner.nqsDays = row.nqs_days != "NA" ? Number(row.nqs_days) : null;
            pensioner.lpd = row.lpd != "NA" ? Number(row.lpd) : null;
            pensioner.commutedValue = row.commuted_value != "NA" ? Number(row.commuted_value) : null;
            pensioner.dcrg = row.dcrg != "NA" ? Number(row.dcrg) : null;
            pensioner.dcrgDuesDeductions = row.dcrg_dues_deductions != "NA" ? Number(row.dcrg_dues_deductions) : null;
            pensioner.netGratuity = row.net_gratuity != "NA" ? Number(row.net_gratuity) : null;
            pensioner.terminalBenefit = row.terminal_benefit != "NA" ? Number(row.terminal_benefit) : null;
            pensioner.familyPensionIStartDate = row.family_pension_i_start_date != "NA" ? intConversion((0, _utils.convertDateToEpochForMigration)(String(row.family_pension_i_start_date).split("/").reverse().join("-"), "dob")) : null;
            pensioner.familyPensionIEndDate = row.family_pension_i_end_date != "NA" ? intConversion((0, _utils.convertDateToEpochForMigration)(String(row.family_pension_i_end_date).split("/").reverse().join("-"), "dob")) : null;
            pensioner.familyPensionIIStartDate = row.family_pension_ii_start_date != "NA" ? intConversion((0, _utils.convertDateToEpochForMigration)(String(row.family_pension_ii_start_date).split("/").reverse().join("-"), "dob")) : null;
            pensioner.exGratia = row.ex_gratia != "NA" ? Number(row.ex_gratia) : null;
            pensioner.ltc = row.ltc != "NA" ? Number(row.ltc) : null;
            pensioner.isDaMedicalAdmissible = row.is_da_medical_admissible == "TRUE" ? true : false;
            pensioner.pensionerNumber = row.pensioner_number != "NA" ? row.pensioner_number : null;
            pensioner.startYear = row.start_year != "NA" ? Number(row.start_year) : null;
            pensioner.startMonth = row.start_month != "NA" ? Number(row.start_month) : null;
            pensioner.endYear = row.end_year != "NA" && row.end_year != "" && row.end_year != null ? Number(row.end_year) : null;
            pensioner.endMonth = row.end_month != "NA" && row.end_month != "" && row.end_month != null ? Number(row.end_month) : null;
            pensioner.basicPension = row.basic_pension != "NA" ? Number(row.basic_pension) : null;
            pensioner.da = row.da != "NA" ? Number(row.da) : null;
            pensioner.commutedPension = row.commuted_pension != "NA" ? Number(row.commuted_pension) : null;
            pensioner.additionalPension = row.additional_pension != "NA" ? Number(row.additional_pension) : null;
            pensioner.ir = row.ir != "NA" ? Number(row.ir) : null;
            pensioner.fma = row.fma != "NA" ? Number(row.fma) : null;
            pensioner.misc = row.misc != "NA" ? Number(row.misc) : null;
            pensioner.woundExtraordinaryPension = row.wound_extraordinary_pension != "NA" ? Number(row.wound_extraordinary_pension) : null;
            pensioner.attendantAllowance = row.attendant_allowance != "NA" ? Number(row.attendant_allowance) : null;
            pensioner.totalPension = row.total_pension != "NA" ? Number(row.total_pension) : null;
            pensioner.overPayment = row.over_payment != "NA" ? Number(row.over_payment) : null;
            pensioner.incomeTax = row.income_tax != "NA" ? Number(row.income_tax) : null;
            pensioner.cess = row.cess != "NA" ? Number(row.cess) : null;
            pensioner.pensionDeductions = row.pension_deductions != "NA" ? Number(row.pension_deductions) : null;
            pensioner.netDeductions = row.net_deductions != "NA" ? Number(row.net_deductions) : null;
            pensioner.netPension = row.net_pension != "NA" ? Number(row.net_pension) : null;
            pensioner.billCode = row.bill_code == "TRUE" ? true : false;
            return _context49.abrupt("return", pensioner);

          case 70:
          case "end":
            return _context49.stop();
        }
      }
    }, _callee49, undefined);
  }));

  return function migratedPensionerRowMapper(_x100) {
    return _ref49.apply(this, arguments);
  };
}();

var removeEmpty = function removeEmpty(obj) {
  Object.keys(obj).forEach(function (key) {
    if (obj[key] && (0, _typeof3.default)(obj[key]) === "object") removeEmpty(obj[key]);else if (obj[key] == null) delete obj[key];
  });
};

var searchUser = function () {
  var _ref50 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee50(requestInfo, uuid) {
    var userSearchReqCriteria, userSearchResponse, users;
    return _regenerator2.default.wrap(function _callee50$(_context50) {
      while (1) {
        switch (_context50.prev = _context50.next) {
          case 0:
            userSearchReqCriteria = {};
            userSearchResponse = {};

            userSearchReqCriteria.uuid = [uuid];
            _context50.next = 5;
            return _userService2.default.searchUser(requestInfo, userSearchReqCriteria);

          case 5:
            userSearchResponse = _context50.sent;
            users = (0, _get2.default)(userSearchResponse, "user", []);
            return _context50.abrupt("return", users.length ? users[0] : {});

          case 8:
          case "end":
            return _context50.stop();
        }
      }
    }, _callee50, undefined);
  }));

  return function searchUser(_x102, _x103) {
    return _ref50.apply(this, arguments);
  };
}();

var searchByMobileNumber = exports.searchByMobileNumber = function () {
  var _ref51 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee51(mobileNumber, tenantId) {
    var userSearchReqCriteria, userSearchResponse;
    return _regenerator2.default.wrap(function _callee51$(_context51) {
      while (1) {
        switch (_context51.prev = _context51.next) {
          case 0:
            userSearchReqCriteria = {};

            userSearchReqCriteria.userType = "CITIZEN";
            userSearchReqCriteria.tenantId = tenantId;
            userSearchReqCriteria.mobileNumber = mobileNumber;
            _context51.next = 6;
            return _userService2.default.searchUser(requestInfo, userSearchReqCriteria);

          case 6:
            userSearchResponse = _context51.sent;
            return _context51.abrupt("return", userSearchResponse);

          case 8:
          case "end":
            return _context51.stop();
        }
      }
    }, _callee51, undefined);
  }));

  return function searchByMobileNumber(_x104, _x105) {
    return _ref51.apply(this, arguments);
  };
}();

var mergeMonthlyPensionDrawn = exports.mergeMonthlyPensionDrawn = function () {
  var _ref52 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee52(response) {
    var query = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var reqInfo = arguments[2];
    var mdms = arguments[3];
    var result, i, pensionRevision;
    return _regenerator2.default.wrap(function _callee52$(_context52) {
      while (1) {
        switch (_context52.prev = _context52.next) {
          case 0:
            requestInfo = reqInfo;

            result = [];
            i = 0;

          case 3:
            if (!(i < response.length)) {
              _context52.next = 12;
              break;
            }

            pensionRevision = {};
            _context52.next = 7;
            return monthlyPensionDrawnRowMapper(response[i], mdms);

          case 7:
            pensionRevision = _context52.sent;

            result.push(pensionRevision);

          case 9:
            i++;
            _context52.next = 3;
            break;

          case 12:
            return _context52.abrupt("return", result);

          case 13:
          case "end":
            return _context52.stop();
        }
      }
    }, _callee52, undefined);
  }));

  return function mergeMonthlyPensionDrawn(_x106) {
    return _ref52.apply(this, arguments);
  };
}();

var monthlyPensionDrawnRowMapper = function () {
  var _ref53 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee53(row, mdms) {
    var mapper = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
    var mdmsBankDetails, pension, bankDetailsList;
    return _regenerator2.default.wrap(function _callee53$(_context53) {
      while (1) {
        switch (_context53.prev = _context53.next) {
          case 0:
            mdmsBankDetails = (0, _get2.default)(mdms, "MdmsRes.pension.BankDetails");
            pension = (0, _isEmpty2.default)(mapper) ? {} : mapper;

            pension.pensionerNumber = row.pensioner_number;
            pension.name = row.name;
            pension.finalCalculatedPension = intConversion(Number(row.final_calculated_pension));
            bankDetailsList = (0, _filter2.default)(mdmsBankDetails, function (x) {
              return x.code == row.bank_details && row.bank_details != null && row.bank_details != "";
            });

            if (bankDetailsList.length > 0) {
              pension.bankDetails = bankDetailsList[0].name;
            }

            //pension.bankDetails = row.bank_details; 
            pension.bankCode = row.bank_code;
            pension.bankIfsc = row.bank_ifsc;
            pension.bankAccountNumber = row.bank_account_number != null ? (0, _encryption.decrypt)(row.bank_account_number) : row.bank_account_number;
            return _context53.abrupt("return", pension);

          case 11:
          case "end":
            return _context53.stop();
        }
      }
    }, _callee53, undefined);
  }));

  return function monthlyPensionDrawnRowMapper(_x108, _x109) {
    return _ref53.apply(this, arguments);
  };
}();
//# sourceMappingURL=search.js.map