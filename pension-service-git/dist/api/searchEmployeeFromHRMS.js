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

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _some = require("lodash/some");

var _some2 = _interopRequireDefault(_some);

var _search = require("../utils/search");

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
  api.post("/_searchEmployeeFromHRMS", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(request, res, next) {
      var response, queryObj, pensionEmployees, code, name, departments, hrmsResponse, hrmsEmployee, modifiedQueryDobNum, i, dob, dateOfSuperannuation, employee;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                Employees: []
              };
              queryObj = JSON.parse(JSON.stringify(request.query));

              /*
              let errors = validatePensionNotificationRegisterSearchModel(queryObj);
              if (errors.length > 0) {
                next({
                  errorType: "custom",
                  errorReponse: {
                    ResponseInfo: requestInfoToResponseInfo(
                      request.body.RequestInfo,
                      true
                    ),
                    Errors: errors
                  }
                });
                return;
              }
              */

              pensionEmployees = [];
              code = "";
              name = "";
              departments = "";


              if (queryObj.code) {
                code = queryObj.code;
              }
              if (queryObj.name) {
                name = queryObj.name;
              }
              if (queryObj.departments) {
                departments = queryObj.departments;
              }
              //let hrmsResponse = await getEmployeeDetails(request.body.RequestInfo, queryObj.tenantId);   
              _context.next = 11;
              return (0, _utils.getEmployeeDetails)(request.body.RequestInfo, queryObj.tenantId, code, name, departments);

            case 11:
              hrmsResponse = _context.sent;
              hrmsEmployee = hrmsResponse.Employees;
              /*
              if (queryObj.code) {
               hrmsEmployee=filter(hrmsEmployee,function(x){return x.code==queryObj.code;});  
              }
              if (queryObj.name) {      
               hrmsEmployee=hrmsEmployee.filter(s => String(s.user.name).toUpperCase().includes(String(queryObj.name).toUpperCase()));
              }
              */

              _logger2.default.debug(hrmsEmployee);

              modifiedQueryDobNum = (0, _utils.adjust530)(queryObj.dob);

              /*let modifiedQueryDob = new Date(Number(queryObj.dob));
              modifiedQueryDob.setHours(modifiedQueryDob.getHours() + 5);
              modifiedQueryDob.setMinutes(modifiedQueryDob.getMinutes() + 30);
              let modifiedQueryDobNum = Number(modifiedQueryDob);*/

              if (queryObj.dob) {
                //hrmsEmployee=filter(hrmsEmployee,function(x){return x.user.dob==queryObj.dob;}); 
                hrmsEmployee = (0, _filter2.default)(hrmsEmployee, function (x) {

                  if (x != null && x.user != null && x.user.dob != null) {
                    return x.user.dob == modifiedQueryDobNum;
                  } else {
                    return false;
                  }
                });
              }

              //let maxRetirementAge=envVariables.EGOV_PENSION_MAX_RETIREMENT_AGE;

              if (hrmsEmployee) {
                for (i = 0; i < hrmsEmployee.length; i++) {
                  dob = hrmsEmployee[i].user.dob;
                  //let actualDob=new Date(epochToYmd(intConversion(dob))); 
                  //let dorYYYYMMDD=`${actualDob.getFullYear()+maxRetirementAge}-${actualDob.getMonth()+1}-${actualDob.getDate()}`;    
                  //let dateOfRetirement=convertDateToEpoch(dorYYYYMMDD,"dob");

                  dateOfSuperannuation = hrmsEmployee[i].dateOfSuperannuation;
                  employee = {
                    pensionEmployeeId: "",
                    id: hrmsEmployee[i].id,
                    tenantId: hrmsEmployee[i].tenantId,
                    code: hrmsEmployee[i].code,
                    name: hrmsEmployee[i].user.name,
                    dob: dob,
                    dateOfJoining: hrmsEmployee[i].dateOfAppointment,
                    dateOfRetirement: dateOfSuperannuation,
                    dateOfDeath: null,
                    department: hrmsEmployee[i].assignments[hrmsEmployee[i].assignments.length - 1].department,
                    designation: hrmsEmployee[i].assignments[hrmsEmployee[i].assignments.length - 1].designation
                  };


                  pensionEmployees.push(employee);
                }
              }

              response.Employees = pensionEmployees;
              res.json(response);

            case 19:
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
//# sourceMappingURL=searchEmployeeFromHRMS.js.map