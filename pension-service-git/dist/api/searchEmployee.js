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

var _orderBy = require("lodash/orderBy");

var _orderBy2 = _interopRequireDefault(_orderBy);

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_searchEmployee", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(request, res, next) {
      var queryObj, employee, assignments, serviceHistory, employees, sqlQueryAssignment, sqlQueryServiceHistory, text, sqlQuery;
      return _regenerator2.default.wrap(function _callee2$(_context2) {
        while (1) {
          switch (_context2.prev = _context2.next) {
            case 0:
              queryObj = JSON.parse(JSON.stringify(request.query));
              employee = void 0;
              assignments = [];
              serviceHistory = [];
              employees = [];

              //assignments

              text = "SELECT pea.tenantid, position, department, designation, from_date, to_date, govt_order_no, reporting_to, is_hod, is_current_assignment, is_primary_assignment from eg_pension_employee_assignment pea JOIN eg_pension_employee pe ON pea.pension_employee_id=pe.uuid WHERE pea.is_pension_applicable=true AND pea.active =true";
              if (queryObj.code) {
                text = text + " AND pe.employee_hrms_code = '" + queryObj.code + "'";
              }

              sqlQueryAssignment = text;

              /* db.query(sqlQueryAssignment, async (err, dbRes) => {
                if (err) {
                  logger.error(err.stack);
                } else {        
                  assignments =
                  dbRes.rows && !isEmpty(dbRes.rows)
                    ? await mergeAssignmentResults(
                        dbRes.rows,
                        request.query,
                        request.body.RequestInfo
                      )
                    : [];  
                    logger.debug("assignments",assignments);  
                    //employee.assignments=assignments;                                                      
                }
              });                 */

              //service history

              text = "SELECT pesh.tenantid, service_status, service_from, service_to, order_no, location, is_current_position from eg_pension_employee_service_history pesh JOIN eg_pension_employee pe ON pesh.pension_employee_id=pe.uuid WHERE pesh.active =true";
              if (queryObj.code) {
                text = text + " AND pe.employee_hrms_code = '" + queryObj.code + "'";
              }

              sqlQueryServiceHistory = text;

              /* db.query(sqlQueryServiceHistory, async (err, dbRes) => {
                if (err) {
                  logger.error(err.stack);
                } else {        
                  serviceHistory =
                  dbRes.rows && !isEmpty(dbRes.rows)
                    ? await mergeServiceHistoryResults(
                        dbRes.rows,
                        request.query,
                        request.body.RequestInfo
                      )
                    : []; 
                    
                   logger.debug("serviceHistory",serviceHistory);                 
                   //employee.serviceHistory=serviceHistory;                                              
                }
              });    */

              text = "select pe.uuid, pe.tenantid, pe.employee_hrms_id, pe.employee_hrms_code, pe.name, pe.date_of_birth, pe.date_of_retirement, pe.date_of_death, pe.employee_hrms_uuid, pe.salutation, pe.gender, pe.employee_status, pe.employee_type, pe.date_of_appointment, pecd.mobile_number, pecd.email_id, pecd.alt_contact_number, pecd.pan, pecd.aadhaar_number, pecd.permanent_address, pecd.permanent_city, pecd.permanent_pin_code, pecd.correspondence_address, pecd.correspondence_city, pecd.correspondence_pin_code, pecd.father_or_husband_name, pecd.blood_group, pecd.identification_mark from eg_pension_employee pe";

              text = text + " left join eg_pension_employee_contact_details pecd on pe.uuid = pecd.pension_employee_id and pecd.active =true";

              if (!(0, _isEmpty2.default)(queryObj)) {
                text = text + " where ";
              }
              /*
              if (queryObj.tenantId) {
                text = `${text} pe.tenantid = '${queryObj.tenantId}'`;
              }
              */
              if (queryObj.code) {
                text = text + " pe.employee_hrms_code = '" + queryObj.code + "'";
              }
              /*
              if (queryObj.name) {
                text = `${text} and pe.name = '${queryObj.name}'`;
              }            
              if (queryObj.dob) {
                text = `${text} and pe.date_of_birth = '${queryObj.dob}'`;
              }  
              */

              sqlQuery = text;


              sqlQuery = sqlQuery + ';' + sqlQueryAssignment + ';' + sqlQueryServiceHistory + ';';

              db.query(sqlQuery, function () {
                var _ref3 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(err, dbRes) {
                  var response;
                  return _regenerator2.default.wrap(function _callee$(_context) {
                    while (1) {
                      switch (_context.prev = _context.next) {
                        case 0:
                          if (!err) {
                            _context.next = 4;
                            break;
                          }

                          _logger2.default.error(err.stack);
                          _context.next = 34;
                          break;

                        case 4:
                          if (!(dbRes[0].rows && !(0, _isEmpty2.default)(dbRes[0].rows))) {
                            _context.next = 10;
                            break;
                          }

                          _context.next = 7;
                          return (0, _search.mergeSearchEmployee)(dbRes[0].rows, request.query, request.body.RequestInfo);

                        case 7:
                          _context.t0 = _context.sent;
                          _context.next = 11;
                          break;

                        case 10:
                          _context.t0 = {};

                        case 11:
                          employee = _context.t0;

                          if (!(dbRes[1].rows && !(0, _isEmpty2.default)(dbRes[1].rows))) {
                            _context.next = 18;
                            break;
                          }

                          _context.next = 15;
                          return (0, _search.mergeAssignmentResults)(dbRes[1].rows, request.query, request.body.RequestInfo);

                        case 15:
                          _context.t1 = _context.sent;
                          _context.next = 19;
                          break;

                        case 18:
                          _context.t1 = [];

                        case 19:
                          assignments = _context.t1;

                          if (!(dbRes[2].rows && !(0, _isEmpty2.default)(dbRes[2].rows))) {
                            _context.next = 26;
                            break;
                          }

                          _context.next = 23;
                          return (0, _search.mergeServiceHistoryResults)(dbRes[2].rows, request.query, request.body.RequestInfo);

                        case 23:
                          _context.t2 = _context.sent;
                          _context.next = 27;
                          break;

                        case 26:
                          _context.t2 = [];

                        case 27:
                          serviceHistory = _context.t2;

                          _logger2.default.debug("employee", JSON.stringify(employee));
                          employee.assignments = assignments;
                          employee.serviceHistory = serviceHistory;

                          employees.push(employee);
                          response = {
                            ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                            Employees: employees
                          };

                          res.json(response);

                        case 34:
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

            case 18:
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
//# sourceMappingURL=searchEmployee.js.map