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
  api.post("/_searchPensioner", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(request, res, next) {
      var response, queryObj, mdms, text, modifiedQueryDobNum, sqlQuery, pensioners;
      return _regenerator2.default.wrap(function _callee2$(_context2) {
        while (1) {
          switch (_context2.prev = _context2.next) {
            case 0:
              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                Pensioners: []
              };
              queryObj = JSON.parse(JSON.stringify(request.query));
              _context2.next = 4;
              return (0, _mdmsData2.default)(request.body.RequestInfo, queryObj.tenantId);

            case 4:
              mdms = _context2.sent;
              text = "SELECT pp.uuid, pp.tenantid, pp.pensioner_number, pp.business_service, pe.name, pe.employee_hrms_code, pe.date_of_birth, pe.gender, pe.date_of_retirement, pe.date_of_death, pe.date_of_appointment, ppad.lpd, ppad.ltc, ppad.wef,";

              text = text + " (SELECT name FROM eg_pension_dependent WHERE pension_employee_id=pp.pension_employee_id AND is_eligible_for_pension=true AND active=true) AS claimant_name,";
              text = text + " (SELECT dob FROM eg_pension_dependent WHERE pension_employee_id=pp.pension_employee_id AND is_eligible_for_pension=true AND active=true) AS claimant_dob,";
              text = text + " CASE WHEN pp.business_service='RRP_SERVICE'";
              text = text + " THEN (SELECT correspondence_address FROM eg_pension_employee_contact_details WHERE pension_employee_id=pp.pension_employee_id AND active=true)";
              text = text + " ELSE (SELECT address FROM eg_pension_dependent WHERE pension_employee_id=pp.pension_employee_id AND is_eligible_for_pension=true AND active=true)";
              text = text + " END AS address,";
              text = text + " CASE WHEN pp.business_service='RRP_SERVICE'";
              text = text + " THEN ppad.bank_address";
              text = text + " ELSE (SELECT bank_details FROM eg_pension_dependent WHERE pension_employee_id=pp.pension_employee_id AND is_eligible_for_pension=true AND active=true)";
              text = text + " END AS bank_details,";
              text = text + " CASE WHEN pp.business_service='RRP_SERVICE'";
              text = text + " THEN ppad.account_number";
              text = text + " ELSE (SELECT bank_account_number FROM eg_pension_dependent WHERE pension_employee_id=pp.pension_employee_id AND is_eligible_for_pension=true AND active=true)";
              text = text + " END AS bank_account_number,";
              text = text + " CASE WHEN pp.business_service='RRP_SERVICE'";
              text = text + " THEN ppad.bank_code";
              text = text + " ELSE (SELECT bank_code FROM eg_pension_dependent WHERE pension_employee_id=pp.pension_employee_id AND is_eligible_for_pension=true AND active=true)";
              text = text + " END AS bank_code,";
              text = text + " CASE WHEN pp.business_service='RRP_SERVICE'";
              text = text + " THEN ppad.bank_ifsc";
              text = text + " ELSE (SELECT bank_ifsc FROM eg_pension_dependent WHERE pension_employee_id=pp.pension_employee_id AND is_eligible_for_pension=true AND active=true)";
              text = text + " END AS bank_ifsc,";
              text = text + " pea.department, pea.designation";
              text = text + " FROM eg_pension_pensioner pp";
              text = text + " INNER JOIN eg_pension_employee pe ON pp.pension_employee_id=pe.uuid";
              text = text + " INNER JOIN eg_pension_pensioner_application_details ppad ON pp.uuid=ppad.pensioner_id";
              text = text + " INNER JOIN eg_pension_employee_assignment pea ON pea.pension_employee_id=pe.uuid AND pea.is_pension_applicable=true";

              text = text + " WHERE pp.active=true";

              if (queryObj.tenantId) {
                text = text + " AND pp.tenantid = '" + queryObj.tenantId + "'";
              }
              if (queryObj.pensionerNumber) {
                text = text + " AND pp.pensioner_number = '" + queryObj.pensionerNumber + "'";
              }
              if (queryObj.name) {
                text = text + " AND upper(pe.name) like '%" + String(queryObj.name).toUpperCase() + "%'";
              }
              if (queryObj.dob) {
                modifiedQueryDobNum = (0, _utils.adjust530)(queryObj.dob);
                //text = `${text} AND pe.date_of_birth = ${queryObj.dob}`;

                text = text + " AND pe.date_of_birth = " + modifiedQueryDobNum;
              }
              if (queryObj.departments) {
                text = text + " AND pea.department = '" + queryObj.departments + "'";
              }
              sqlQuery = text;
              pensioners = [];


              db.query(sqlQuery, function () {
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
                          _context.next = 14;
                          break;

                        case 4:
                          if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                            _context.next = 10;
                            break;
                          }

                          _context.next = 7;
                          return (0, _search.mergeSearchPensionerResults)(dbRes.rows, request.query, request.body.RequestInfo, mdms);

                        case 7:
                          _context.t0 = _context.sent;
                          _context.next = 11;
                          break;

                        case 10:
                          _context.t0 = [];

                        case 11:
                          pensioners = _context.t0;


                          response.Pensioners = pensioners;
                          res.json(response);

                        case 14:
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

            case 42:
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
//# sourceMappingURL=searchPensioner.js.map