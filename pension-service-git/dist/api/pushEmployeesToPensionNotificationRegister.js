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

var _search = require("../utils/search");

var _userService = require("../services/userService.js");

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_pushEmployeesToPensionNotificationRegister", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var pensionNotApplicableFromYear, hrmsResponse, hrmsEmployees, pensionResponse, pensionEmployeesList, nextNMonths, today, createdDate, nextNMonthEndDate, queryResult, pensionEmployees, i, pensionEmployeesFilter, tenantId, dob, dateOfSuperannuation, actualDateOfSuperannuation, actualDob, employee, requestBody, response;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              pensionNotApplicableFromYear = _envVariables2.default.EGOV_PENSION_NOT_APPLICABLE_FROM_YEAR;
              _context.next = 3;
              return (0, _utils.getEmployeeDetails)(body.RequestInfo, _envVariables2.default.EGOV_PENSION_SCHEDULAR_TENANTID);

            case 3:
              hrmsResponse = _context.sent;
              hrmsEmployees = [];

              hrmsEmployees = hrmsResponse.Employees;
              hrmsEmployees = (0, _filter2.default)(hrmsEmployees, function (x) {
                return x.dateOfAppointment != null && new Date((0, _utils.epochToYmd)((0, _search.intConversion)(x.dateOfAppointment))).getFullYear() < pensionNotApplicableFromYear || x.serviceHistory != null && x.serviceHistory.length > 0 && x.serviceHistory[0].serviceFrom != null && new Date((0, _utils.epochToYmd)((0, _search.intConversion)(x.serviceHistory[0].serviceFrom))).getFullYear() < pensionNotApplicableFromYear;
              });

              _context.next = 9;
              return (0, _utils.getPensionEmployees)(body.RequestInfo, _envVariables2.default.EGOV_PENSION_SCHEDULAR_TENANTID);

            case 9:
              pensionResponse = _context.sent;
              pensionEmployeesList = pensionResponse.Employees;
              nextNMonths = _envVariables2.default.EGOV_PENSION_PNR_SCHEDULAR_NEXT_N_MONTHS;
              today = new Date();
              createdDate = (0, _utils.convertDateToEpoch)(today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate(), "dob");
              nextNMonthEndDate = new Date(today.getFullYear(), today.getMonth() + nextNMonths, today.getDate());
              queryResult = hrmsEmployees;
              pensionEmployees = [];


              for (i = 0; i < queryResult.length; i++) {
                if (queryResult[i].user != null && queryResult[i].user.dob != null) {
                  pensionEmployeesFilter = [];

                  if (pensionEmployeesList.length > 0) {
                    pensionEmployeesFilter = (0, _filter2.default)(pensionEmployeesList, function (x) {
                      return x.code == queryResult[i].code;
                    });
                  }

                  //employee not exist in pension module
                  if (pensionEmployeesFilter.length == 0) {
                    tenantId = queryResult[i].tenantId;
                    dob = queryResult[i].user.dob;
                    dateOfSuperannuation = queryResult[i].dateOfSuperannuation;
                    actualDateOfSuperannuation = new Date((0, _utils.epochToYmd)((0, _search.intConversion)(dateOfSuperannuation)));
                    actualDob = new Date((0, _utils.epochToYmd)((0, _search.intConversion)(dob)));


                    if (actualDateOfSuperannuation <= nextNMonthEndDate && actualDateOfSuperannuation > today) {
                      employee = {
                        pensionEmployeeId: (0, _utils.uuidv1)(),
                        tenantId: tenantId,
                        id: queryResult[i].id,
                        code: queryResult[i].code,
                        name: queryResult[i].user.name,
                        dob: dob,
                        //dateOfRetirement: dateOfRetirement,
                        dateOfRetirement: dateOfSuperannuation,
                        dateOfDeath: null,
                        uuid: queryResult[i].uuid,
                        salutation: queryResult[i].user.salutation,
                        gender: queryResult[i].user.gender,
                        employeeStatus: queryResult[i].employeeStatus,
                        employeeType: queryResult[i].employeeType,
                        dateOfAppointment: queryResult[i].dateOfAppointment,
                        pensionEmployeeAuditId: (0, _utils.uuidv1)(),
                        active: true,
                        notificationRegister: {
                          pensionNotificationRegisterId: (0, _utils.uuidv1)(),
                          isInitiated: null,
                          pensionNotificationRegisterAuditId: (0, _utils.uuidv1)()
                        },
                        auditDetails: {
                          createdBy: body.RequestInfo.userInfo.uuid,
                          lastModifiedBy: null,
                          createdDate: createdDate,
                          lastModifiedDate: null
                        }
                      };


                      pensionEmployees.push(employee);
                    }
                  }
                }
              }

              _logger2.default.debug("pensionEmployees", JSON.stringify(pensionEmployees));

              requestBody = {
                RequestInfo: body.RequestInfo,
                Employees: pensionEmployees
              };
              _context.next = 22;
              return (0, _utils.saveEmployeeToPensionNotificationRegister)(requestBody);

            case 22:
              response = _context.sent;

              res.json(response);

            case 24:
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
//# sourceMappingURL=pushEmployeesToPensionNotificationRegister.js.map