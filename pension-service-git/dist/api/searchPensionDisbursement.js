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

var _encryption = require("../utils/encryption");

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_searchPensionDisbursement", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(request, res, next) {
      var queryObj, mdms, year, month, text, sqlQuery, pensionerDetails;
      return _regenerator2.default.wrap(function _callee2$(_context2) {
        while (1) {
          switch (_context2.prev = _context2.next) {
            case 0:
              queryObj = JSON.parse(JSON.stringify(request.query));

              /* let num = "1234567890";
              let en = encrypt(num);
               let en1="991862b209bbedda3975008854bac92a";
               let de = decrypt(en);
              let de1 = decrypt(en1); */

              _context2.next = 3;
              return (0, _mdmsData2.default)(request.body.RequestInfo, queryObj.tenantId);

            case 3:
              mdms = _context2.sent;
              year = (0, _filter2.default)(request.body.searchParams, function (x) {
                return x.name == "Year";
              })[0].input;
              month = (0, _filter2.default)(request.body.searchParams, function (x) {
                return x.name == "Month";
              })[0].input;
              text = "select pp.pensioner_number as pensioner_number , pe.name as name, final_calculated_pension,(case when pd.name is null or pd.name = '' then ppad.bank_address else pd.bank_details end) as bank_details,(case when pd.name is null or pd.name = '' then ppad.bank_code else pd.bank_code end) as bank_code,(case when pd.name is null or pd.name = '' then ppad.bank_ifsc else pd.bank_ifsc end) as bank_ifsc,(case when pd.name is null or pd.name = '' then ppad.account_number else pd.bank_account_number end) as bank_account_number from eg_pension_revision pr join eg_pension_pensioner pp on pr.pensioner_id=pp.uuid join eg_pension_employee pe on pp.pension_employee_id=pe.uuid  join eg_pension_pensioner_application_details ppad on ppad.pensioner_id = pp.uuid left join eg_pension_dependent pd on pd.pension_employee_id = pp.pension_employee_id where pp.active=true AND pr.active=true and 1=1 and cast(concat('" + year.toString() + "', lpad('" + month.toString() + "',2,'0')) as integer)>=cast(concat(cast(pr.effective_start_year as varchar), lpad(cast(pr.effective_start_month as varchar),2,'0')) as integer) and (pr.effective_end_year is not null and pr.effective_end_month is not null and cast(concat('" + year.toString() + "', lpad('" + month.toString() + "',2,'0')) as integer)<cast(concat(cast(pr.effective_end_year as varchar), lpad(cast(pr.effective_end_month as varchar),2,'0')) as integer) or (pr.effective_end_year is null and pr.effective_end_month is null))";
              /* if (!isEmpty(queryObj)) {
                text = `${text} WHERE`;
              }
              if (queryObj.tenantId) {
                text = `${text} tenantid = '${queryObj.tenantId}'`;
              } 
              if (pensioner.pensionerId) {
                text=`${text} AND pensioner_id = '${pensioner.pensionerId}'`;
              }
              */

              sqlQuery = text;
              pensionerDetails = {};

              db.query(sqlQuery, function () {
                var _ref3 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(err, dbRes) {
                  var reportData, i, response;
                  return _regenerator2.default.wrap(function _callee$(_context) {
                    while (1) {
                      switch (_context.prev = _context.next) {
                        case 0:
                          if (!err) {
                            _context.next = 4;
                            break;
                          }

                          _logger2.default.error(err.stack);
                          _context.next = 16;
                          break;

                        case 4:
                          if (!(dbRes.rows && !(0, _isEmpty2.default)(dbRes.rows))) {
                            _context.next = 10;
                            break;
                          }

                          _context.next = 7;
                          return (0, _search.mergeMonthlyPensionDrawn)(dbRes.rows, request.query, request.body.RequestInfo, mdms);

                        case 7:
                          _context.t0 = _context.sent;
                          _context.next = 11;
                          break;

                        case 10:
                          _context.t0 = {};

                        case 11:
                          pensionerDetails = _context.t0;
                          reportData = [];

                          for (i = 0; i < pensionerDetails.length; i++) {
                            reportData.push([pensionerDetails[i].pensionerNumber, pensionerDetails[i].name, pensionerDetails[i].bankDetails, pensionerDetails[i].bankIfsc, pensionerDetails[i].bankCode, pensionerDetails[i].bankAccountNumber, pensionerDetails[i].finalCalculatedPension]);
                          }

                          response = {
                            ResponseInfo: (0, _utils.requestInfoToResponseInfo)(request.body.RequestInfo, true),
                            reportResponses: [{
                              viewPath: null,
                              selectiveDownload: false,
                              reportHeader: [{
                                localisationRequired: false,
                                name: "pensioner_number",
                                label: "Pensioner Number",
                                type: "string",
                                defaultValue: null,
                                isMandatory: false,
                                isLocalisationRequired: false,
                                localisationPrefix: "",
                                showColumn: true,
                                total: false,
                                rowTotal: null,
                                columnTotal: null,
                                initialValue: null,
                                minValue: null,
                                maxValue: null
                              }, {
                                localisationRequired: false,
                                name: "name",
                                label: "Pensioner Name",
                                type: "string",
                                defaultValue: null,
                                isMandatory: false,
                                isLocalisationRequired: false,
                                localisationPrefix: "",
                                showColumn: true,
                                total: false,
                                rowTotal: null,
                                columnTotal: null,
                                initialValue: null,
                                minValue: null,
                                maxValue: null
                              }, {
                                localisationRequired: false,
                                name: "bank_details",
                                label: "Bank Name",
                                type: "string",
                                defaultValue: null,
                                isMandatory: false,
                                isLocalisationRequired: false,
                                localisationPrefix: "",
                                showColumn: true,
                                total: false,
                                rowTotal: null,
                                columnTotal: null,
                                initialValue: null,
                                minValue: null,
                                maxValue: null
                              }, {
                                localisationRequired: false,
                                name: "bank_ifsc",
                                label: "Bank IFSC",
                                type: "string",
                                defaultValue: null,
                                isMandatory: false,
                                isLocalisationRequired: false,
                                localisationPrefix: "",
                                showColumn: true,
                                total: false,
                                rowTotal: null,
                                columnTotal: null,
                                initialValue: null,
                                minValue: null,
                                maxValue: null
                              }, {
                                localisationRequired: false,
                                name: "bank_code",
                                label: "Bank Code",
                                type: "string",
                                defaultValue: null,
                                isMandatory: false,
                                isLocalisationRequired: false,
                                localisationPrefix: "",
                                showColumn: true,
                                total: false,
                                rowTotal: null,
                                columnTotal: null,
                                initialValue: null,
                                minValue: null,
                                maxValue: null
                              }, {
                                localisationRequired: false,
                                name: "bank_account_number",
                                label: "Bank Account Number",
                                type: "string",
                                defaultValue: null,
                                isMandatory: false,
                                isLocalisationRequired: false,
                                localisationPrefix: "",
                                showColumn: true,
                                total: false,
                                rowTotal: null,
                                columnTotal: null,
                                initialValue: null,
                                minValue: null,
                                maxValue: null
                              }, {
                                localisationRequired: false,
                                name: "final_calculated_pension",
                                label: "Net Pension",
                                type: "string",
                                defaultValue: null,
                                isMandatory: false,
                                isLocalisationRequired: false,
                                localisationPrefix: "",
                                showColumn: true,
                                total: true,
                                rowTotal: null,
                                columnTotal: null,
                                initialValue: null,
                                minValue: null,
                                maxValue: null
                              }],
                              ttl: null,
                              reportData: reportData
                            }]
                          };
                          //response.reportResponses.reportData=["abc","abc"];

                          res.json(response);

                        case 16:
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

            case 10:
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
//# sourceMappingURL=searchPensionDisbursement.js.map