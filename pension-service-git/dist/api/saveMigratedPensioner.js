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

var _create = require("../utils/create");

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _set = require("lodash/set");

var _set2 = _interopRequireDefault(_set);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _search = require("../utils/search");

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_saveMigratedPensioner", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(_ref3, res, next) {
      var body = _ref3.body;
      var payloads, text, sqlQuery, pensioner;
      return _regenerator2.default.wrap(function _callee2$(_context2) {
        while (1) {
          switch (_context2.prev = _context2.next) {
            case 0:
              payloads = [];
              text = "SELECT \"Sl No\" As sl_no, \"Name\" As name, \"Employee Code\" AS code, \"Date of Birth\" AS date_of_birth, \"Gender\" AS gender, \"Date of Death (if applicable)\" AS date_of_death, \"Mobile Number\" AS mobile_number, \"Email\" As email, \"Address\" As address, \"Bank Details\" AS bank_details, \"Bank Account Number\" AS bank_account_number, \"Bank IFSC\" AS bank_ifsc, \"Bank Code\" AS bank_code, \"Employee Status\" AS employee_status, \"Employee Type\" AS employee_type, \"Employee Group\" As employee_group, \"Designation\" As designation, \"Department\" AS department, \"Date of Joining\" AS date_of_joining, \"Service End Date\" AS service_end_date, \"Date of Retirement\" AS date_of_retirement, \"Date of Commencement\" As date_of_contingent, \"Claimant Name\" AS claimant_name, \"Claimant Date of Birth\" As claimant_dob, \"Claimant Relationship\" AS claimant_relationship, \"Claimant Mobile Number\" As claimant_mobile_number, \"Claimant Adddress\" AS claimant_address, \"Claimant Bank Details\" AS claimant_bank_details, \"Claimant Bank Account Number\" As claimant_bank_account_number, \"Claimant Bank IFSC\" As claimant_bank_ifsc, \"Claimant Bank Code\" As claimant_bank_code, \"NQS Year\" AS nqs_year, \"NQS Month\" AS nqs_month, \"NQS Days\" AS nqs_days, \"LPD\" AS lpd, \"Commuted Value\" AS commuted_value, \"DCRG\" AS dcrg, \"DCRG Dues Deduction\" AS dcrg_dues_deductions, \"Net Gratuity\" AS net_gratuity, \"Terminal Benefit\" AS terminal_benefit, \"Family Pension I Start Date\" AS family_pension_i_start_date, \"Family Pension I End Date\" AS family_pension_i_end_date, \"Family Pension II Start Date\" AS family_pension_ii_start_date, \"Ex Gratia\" AS ex_gratia, \"LTC\" AS ltc, \"Whether DA Medical Admissible\" AS is_da_medical_admissible, \"Pensioner Number\" AS pensioner_number, \"Start Year\" As start_year, \"Start Month\" AS start_month, \"End Year\" AS end_year, \"End Month\" AS end_month, \"Basic Pension\" AS basic_pension, \"DA\" AS da, \"Commuted Pension\" AS commuted_pension, \"Additional Pension\" AS additional_pension, \"IR\" AS ir, \"FMA\" AS fma, \"Misc\" AS misc, \"Wound or Extraordinary Pension (in case of disability)\" AS wound_extraordinary_pension, \"Attendant Allowance (in case of disability)\" AS attendant_allowance, \"Total Pension\" AS total_pension, \"Over Payment\" AS over_payment, \"Income Tax\" AS income_tax, \"CESS\" AS cess, \"Pension Deductions\" AS pension_deductions, \"Net Deductions\" AS net_deductions, \"Net Pension\" AS net_pension, \"Bill Code\" AS bill_code FROM eg_pension_pensioner_migration_draft WHERE \"Sl No\" IS NOT NULL";
              sqlQuery = text;
              pensioner = [];

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
                          _context.next = 18;
                          break;

                        case 4:
                          if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                            _context.next = 10;
                            break;
                          }

                          _context.next = 7;
                          return (0, _search.mergeMigratedPensionerResults)(dbRes.rows, null, body.RequestInfo);

                        case 7:
                          _context.t0 = _context.sent;
                          _context.next = 11;
                          break;

                        case 10:
                          _context.t0 = [];

                        case 11:
                          pensioner = _context.t0;


                          body.Pensioner = pensioner;
                          _context.next = 15;
                          return (0, _create.addUUIDAndAuditDetailsMigratedPensioner)(body);

                        case 15:
                          body = _context.sent;


                          payloads.push({
                            topic: _envVariables2.default.KAFKA_TOPICS_SAVE_MIGRATED_PENSIONER,
                            messages: JSON.stringify(body)
                          });

                          _producer2.default.send(payloads, function (err, data) {
                            var response = {
                              ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                              Pensioner: body.Pensioner
                            };
                            res.json(response);
                          });

                        case 18:
                        case "end":
                          return _context.stop();
                      }
                    }
                  }, _callee, undefined);
                }));

                return function (_x4, _x5) {
                  return _ref4.apply(this, arguments);
                };
              }());

            case 5:
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
//# sourceMappingURL=saveMigratedPensioner.js.map