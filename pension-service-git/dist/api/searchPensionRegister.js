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
  api.post("/_searchPensionRegister", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee3(request, res, next) {
      var response, processInstance, queryObj, searchPensionerResponse, pensioner, text, sqlQuery, pensionerFinalCalculatedBenefitDetails;
      return _regenerator2.default.wrap(function _callee3$(_context3) {
        while (1) {
          switch (_context3.prev = _context3.next) {
            case 0:
              response = {
                ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                ProcessInstances: []
              };
              processInstance = {
                pensioner: {},
                pensionRegister: [],
                pensionerFinalCalculatedBenefitDetails: {}
              };
              queryObj = JSON.parse(JSON.stringify(request.query));
              _context3.next = 5;
              return (0, _utils.searchPensioner)(request.body.RequestInfo, queryObj.tenantId, queryObj.pensionerNumber);

            case 5:
              searchPensionerResponse = _context3.sent;
              pensioner = searchPensionerResponse.Pensioners[0];

              processInstance.pensioner = pensioner;

              text = "SELECT basic_pension, pension_deductions, additional_pension, commuted_pension, commuted_value, family_pension_i, family_pension_ii, dcrg, net_deductions, final_calculated_pension, interim_relief, da, nqs_year, nqs_month, nqs_day, dues_deductions, compassionate_pension, compensation_pension, terminal_benefit, final_calculated_gratuity, family_pension_i_start_date, family_pension_i_end_date, family_pension_ii_start_date, ex_gratia, pensioner_family_pension, total_pension, provisional_pension, wound_extraordinary_pension, attendant_allowance FROM eg_pension_pensioner_final_calculated_benefit";

              if (!(0, _isEmpty2.default)(queryObj)) {
                text = text + " WHERE";
              }
              if (queryObj.tenantId) {
                text = text + " tenantid = '" + queryObj.tenantId + "'";
              }
              if (pensioner.pensionerId) {
                text = text + " AND pensioner_id = '" + pensioner.pensionerId + "'";
              }

              sqlQuery = text;
              pensionerFinalCalculatedBenefitDetails = {};

              db.query(sqlQuery, function () {
                var _ref3 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(err, dbRes) {
                  var pensionRegister;
                  return _regenerator2.default.wrap(function _callee2$(_context2) {
                    while (1) {
                      switch (_context2.prev = _context2.next) {
                        case 0:
                          if (!err) {
                            _context2.next = 4;
                            break;
                          }

                          _logger2.default.error(err.stack);
                          _context2.next = 20;
                          break;

                        case 4:
                          if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                            _context2.next = 10;
                            break;
                          }

                          _context2.next = 7;
                          return (0, _search.mergePensionerFinalCalculatedBenefit)(dbRes.rows, request.query, request.body.RequestInfo);

                        case 7:
                          _context2.t0 = _context2.sent;
                          _context2.next = 11;
                          break;

                        case 10:
                          _context2.t0 = {};

                        case 11:
                          pensionerFinalCalculatedBenefitDetails = _context2.t0;


                          processInstance.pensionerFinalCalculatedBenefitDetails = pensionerFinalCalculatedBenefitDetails;

                          text = "SELECT pr.tenantid, pr.effective_year, pr.effective_month, pr.pension_arrear, pr.fma, pr.miscellaneous, pr.over_payment, pr.income_tax, pr.cess, pr.basic_pension, pr.additional_pension, pr.commuted_pension, pr.net_deductions, pr.final_calculated_pension, pr.interim_relief, pr.da, pr.total_pension, pr.pension_deductions, pr.wound_extraordinary_pension, pr.attendant_allowance FROM eg_pension_register pr JOIN eg_pension_pensioner pp ON pr.pensioner_id=pp.uuid WHERE pr.active=true";

                          if (queryObj.tenantId) {
                            text = text + " and pr.tenantid = '" + queryObj.tenantId + "'";
                          }
                          if (pensioner.pensionerId) {
                            text = text + " AND pr.pensioner_id = '" + pensioner.pensionerId + "'";
                          }
                          if (queryObj.year) {
                            text = text + " and pr.effective_year = " + queryObj.year;
                          }

                          sqlQuery = text;
                          pensionRegister = [];


                          db.query(sqlQuery, function () {
                            var _ref4 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(err, dbRes) {
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
                                      return (0, _search.mergeSearchPensionRegisterResults)(dbRes.rows, request.query, request.body.RequestInfo);

                                    case 7:
                                      _context.t0 = _context.sent;
                                      _context.next = 11;
                                      break;

                                    case 10:
                                      _context.t0 = [];

                                    case 11:
                                      pensionRegister = _context.t0;

                                      processInstance.pensionRegister = pensionRegister;
                                      response.ProcessInstances.push(processInstance);
                                      res.json(response);

                                    case 15:
                                    case "end":
                                      return _context.stop();
                                  }
                                }
                              }, _callee, undefined);
                            }));

                            return function (_x6, _x7) {
                              return _ref4.apply(this, arguments);
                            };
                          }());

                        case 20:
                        case "end":
                          return _context2.stop();
                      }
                    }
                  }, _callee2, undefined);
                }));

                return function (_x4, _x5) {
                  return _ref3.apply(this, arguments);
                };
              }());

            case 15:
            case "end":
              return _context3.stop();
          }
        }
      }, _callee3, undefined);
    }));

    return function (_x, _x2, _x3) {
      return _ref2.apply(this, arguments);
    };
  }()));
  return api;
};
//# sourceMappingURL=searchPensionRegister.js.map