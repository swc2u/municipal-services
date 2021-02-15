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
  api.post("/_searchApplication", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(request, res, next) {
      var response, text, queryObj, sqlQuery, applications;
      return _regenerator2.default.wrap(function _callee2$(_context2) {
        while (1) {
          switch (_context2.prev = _context2.next) {
            case 0:
              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                Applications: []
              };
              text = "SELECT pwh.uuid, pwh.tenantid, pwh.pension_employee_id, pwh.workflow_type, pwh.application_number, pwh.application_date, pwh.last_modified_date, pwh.workflow_state, pe.employee_hrms_code, pe.name FROM eg_pension_workflow_header pwh JOIN eg_pension_employee pe ON pwh.pension_employee_id=pe.uuid";

              if (request.query) {
                queryObj = JSON.parse(JSON.stringify(request.query));


                text = text + " WHERE";

                if (queryObj.tenantId) {
                  text = text + " pwh.tenantid = '" + queryObj.tenantId + "'";
                }
                if (queryObj.code) {
                  text = text + " AND pe.employee_hrms_code = '" + queryObj.code + "'";
                }
                if (queryObj.businessId) {
                  text = text + " AND pwh.application_number = '" + queryObj.businessId + "'";
                }
                if (queryObj.businessService) {
                  text = text + " AND pwh.workflow_type = '" + queryObj.businessService + "'";
                }
              }

              sqlQuery = text;
              applications = [];


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
                          return (0, _search.mergeSearchApplicationResults)(dbRes.rows, request.query, request.body.RequestInfo);

                        case 7:
                          _context.t0 = _context.sent;
                          _context.next = 11;
                          break;

                        case 10:
                          _context.t0 = [];

                        case 11:
                          applications = _context.t0;


                          response.Applications = applications;
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

            case 6:
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
//# sourceMappingURL=searchApplication.js.map