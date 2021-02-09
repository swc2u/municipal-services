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

var _message = require("../utils/message");

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
  api.post("/_pushManualRegisterToPensionNotificationRegister", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var payloads, employees, pensionResponse, pensionEmployeesList, message, errors, hrmsResponse, hrmsEmployee, pensionEmployeeId, dob, dateOfSuperannuation, assignments, lastAssignments, lastAssignment, i, serviceHistory, employee, response;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              payloads = [];
              //getting mdms data      

              employees = [];

              //search employee in Pension module  

              _context.next = 4;
              return (0, _utils.getPensionEmployees)(body.RequestInfo, body.Employees[0].tenantId, body.Employees[0].code);

            case 4:
              pensionResponse = _context.sent;
              pensionEmployeesList = pensionResponse.Employees;

              if (!(pensionEmployeesList.length > 0)) {
                _context.next = 14;
                break;
              }

              message = (0, _message.Message)();
              errors = message.EMPLOYEE_EXIST_PUSH_MANUAL_REGISTER_TO_PNR_NA;

              if (!(errors.length > 0)) {
                _context.next = 12;
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

            case 12:
              _context.next = 33;
              break;

            case 14:
              _context.next = 16;
              return (0, _utils.getEmployeeDetails)(body.RequestInfo, body.Employees[0].tenantId, body.Employees[0].code);

            case 16:
              hrmsResponse = _context.sent;
              hrmsEmployee = hrmsResponse.Employees[0];

              if (!hrmsEmployee) {
                _context.next = 33;
                break;
              }

              //let maxRetirementAge=envVariables.EGOV_PENSION_MAX_RETIREMENT_AGE;              

              pensionEmployeeId = (0, _utils.uuidv1)();
              dob = hrmsEmployee.user.dob;
              //let actualDob=new Date(epochToYmd(intConversion(dob)));    
              //let dorYYYYMMDD=`${actualDob.getFullYear()+maxRetirementAge}-${actualDob.getMonth()+1}-${actualDob.getDate()}`;    
              //let dateOfRetirement=convertDateToEpoch(dorYYYYMMDD,"dob");   

              dateOfSuperannuation = hrmsEmployee.dateOfSuperannuation;
              assignments = [];

              if (hrmsEmployee.assignments && !(0, _isEmpty2.default)(hrmsEmployee.assignments)) {
                assignments = hrmsEmployee.assignments;
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

              /*
              let user=hrmsEmployee.user;
              user.employeeContactDetailsId=uuidv1();     
              user.tenantId=hrmsEmployee.tenantId;                 
              user.active=true; 
              */

              employee = {
                pensionEmployeeId: pensionEmployeeId,
                id: hrmsEmployee.id,
                uuid: hrmsEmployee.uuid,
                code: hrmsEmployee.code,
                name: hrmsEmployee.user.name,
                dob: dob,
                //dateOfRetirement: dateOfRetirement,
                dateOfRetirement: dateOfSuperannuation,
                dateOfDeath: null,
                tenantId: hrmsEmployee.tenantId,
                salutation: hrmsEmployee.user.salutation,
                gender: hrmsEmployee.user.gender,
                employeeStatus: hrmsEmployee.employeeStatus,
                employeeType: hrmsEmployee.employeeType,
                dateOfAppointment: hrmsEmployee.dateOfAppointment,
                assignments: assignments,
                serviceHistory: serviceHistory,
                //user: user,                 
                active: true,
                pensionEmployeeAuditId: (0, _utils.uuidv1)(),
                notificationRegister: {
                  pensionNotificationRegisterId: (0, _utils.uuidv1)(),
                  isInitiated: null,
                  pensionNotificationRegisterAuditId: (0, _utils.uuidv1)()
                },
                auditDetails: {
                  createdBy: (0, _get2.default)(body.RequestInfo, "userInfo.uuid", ""),
                  lastModifiedBy: null,
                  createdDate: new Date().getTime(),
                  lastModifiedDate: null
                }
              };


              employees.push(employee);
              body.Employees = employees;

              _context.next = 31;
              return (0, _utils.saveEmployeeToPensionNotificationRegister)(body);

            case 31:
              response = _context.sent;

              res.json(response);

            case 33:
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
//# sourceMappingURL=pushManualRegisterToPensionNotificationRegister.js.map