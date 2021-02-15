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
  api.post("/_searchPensionNotificationRegister", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(request, res, next) {
      var response, queryObj, errors, code, name, departments, hrmsResponse, hrmsEmployee, text, modifiedQueryDobNum, sqlQuery, pensionEmployees, employees;
      return _regenerator2.default.wrap(function _callee2$(_context2) {
        while (1) {
          switch (_context2.prev = _context2.next) {
            case 0:
              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                Employees: []
              };
              queryObj = JSON.parse(JSON.stringify(request.query));

              //getting mdms data
              //let mdms = await mdmsData(request.body.RequestInfo, queryObj.tenantId);


              errors = (0, _modelValidation.validatePensionNotificationRegisterSearchModel)(queryObj);

              if (!(errors.length > 0)) {
                _context2.next = 6;
                break;
              }

              next({
                errorType: "custom",
                errorReponse: {
                  ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                  Errors: errors
                }
              });
              return _context2.abrupt("return");

            case 6:
              code = "";
              name = "";
              departments = "";


              if (queryObj.code) {
                code = queryObj.code;
              }
              //  if (queryObj.name) {            
              //   name=queryObj.name;
              //  }
              if (queryObj.departments) {
                departments = queryObj.departments;
              }
              //let hrmsResponse = await getEmployeeDetails(request.body.RequestInfo, queryObj.tenantId);   
              _context2.next = 13;
              return (0, _utils.getEmployeeDetails)(request.body.RequestInfo, queryObj.tenantId, code, name, departments);

            case 13:
              hrmsResponse = _context2.sent;
              hrmsEmployee = hrmsResponse.Employees;
              text = "select pnr.uuid as pension_notification_register_id, pe.uuid as pension_employee_id, pe.employee_hrms_id, pe.employee_hrms_code, pe.name, pe.date_of_birth, pe.date_of_retirement, pe.tenantid, pe.gender, pe.employee_status, pe.employee_type, pe.date_of_appointment from eg_pension_employee pe join eg_pension_notification_register pnr on pe.uuid=pnr.pension_employee_id where pnr.active =true and (pnr.is_initiated is null or pnr.is_initiated =false)";


              if (queryObj.tenantId) {
                text = text + " and pe.tenantid = '" + queryObj.tenantId + "'";
              }
              if (queryObj.code) {
                text = text + " and pe.employee_hrms_code = '" + queryObj.code + "'";
              }
              if (queryObj.name) {
                text = text + " and upper(pe.name) like '%" + String(queryObj.name).toUpperCase() + "%'";
              }
              if (queryObj.dob) {
                modifiedQueryDobNum = (0, _utils.adjust530)(queryObj.dob);
                //text = `${text} and pe.date_of_birth = ${queryObj.dob}`;

                text = text + " and pe.date_of_birth = " + modifiedQueryDobNum;
              }
              if (queryObj.endDate) {
                /*
                let today=new Date();
                let startDate=`${today.getFullYear()}-${today.getMonth()+1}-${today.getDate()}`;  
                logger.debug(startDate);      
                let epochStartDate=Number(convertDateToEpoch(startDate,"dob"));        
                let epochEndDate=Number(convertDateToEpoch(queryObj.endDate,"dob"));        
                text = `${text} and pe.date_of_retirement >=${epochStartDate} and pe.date_of_retirement <=${epochEndDate}`;
                */
                text = text + " and pe.date_of_retirement <=" + Number(queryObj.endDate);
              }

              sqlQuery = text;
              pensionEmployees = [];
              employees = [];


              db.query(sqlQuery, function () {
                var _ref3 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(err, dbRes) {
                  var i, employeeFilter;
                  return _regenerator2.default.wrap(function _callee$(_context) {
                    while (1) {
                      switch (_context.prev = _context.next) {
                        case 0:
                          if (!err) {
                            _context.next = 4;
                            break;
                          }

                          _logger2.default.error(err.stack);
                          _context.next = 15;
                          break;

                        case 4:
                          if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                            _context.next = 10;
                            break;
                          }

                          _context.next = 7;
                          return (0, _search.mergeSearchResults)(dbRes.rows, request.query, request.body.RequestInfo);

                        case 7:
                          _context.t0 = _context.sent;
                          _context.next = 11;
                          break;

                        case 10:
                          _context.t0 = [];

                        case 11:
                          employees = _context.t0;


                          for (i = 0; i < employees.length; i++) {
                            employeeFilter = (0, _filter2.default)(hrmsEmployee, function (x) {
                              return x.code == employees[i].code;
                            });
                            //employee exist in hrms

                            if (employeeFilter.length > 0) {
                              employees[i].department = employeeFilter[0].assignments[employeeFilter[0].assignments.length - 1].department;
                              employees[i].designation = employeeFilter[0].assignments[employeeFilter[0].assignments.length - 1].designation;
                              pensionEmployees.push(employees[i]);
                            }
                          }
                          //response.Employees=employees;
                          response.Employees = pensionEmployees;
                          res.json(response);

                        case 15:
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

            case 25:
            case "end":
              return _context2.stop();
          }
        }
      }, _callee2, undefined);
    }));

    return function (_x, _x2, _x3) {
      return _ref2.apply(this, arguments);
    };
  }()));
  return api;
};
//# sourceMappingURL=searchPensionNotificationRegister.js.map