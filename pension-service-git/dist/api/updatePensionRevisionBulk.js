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

var _search = require("../utils/search");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _mdmsData = require("../utils/mdmsData");

var _mdmsData2 = _interopRequireDefault(_mdmsData);

var _create = require("../utils/create");

var _set = require("lodash/set");

var _set2 = _interopRequireDefault(_set);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _filter = require("lodash/filter");

var _filter2 = _interopRequireDefault(_filter);

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _calculationHelper = require("../utils/calculationHelper");

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
          var config = _ref.config,
              db = _ref.db;

          var api = (0, _express.Router)();
          api.post("/_updatePensionRevisionBulk", asyncHandler(function () {
                    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
                              var body = _ref3.body;
                              var payloads, tenantId, effectiveYear, effectiveMonth, modifyDA, modifyIR, modifyFMA, FMA, effetiveDate, pensionRevisionResponse, pensionRevisions, newPensionRevisions, oldPensionRevisions, i, pensionRevision, effectiveStartDate, effectiveEndDate, oldPensionRevision, newPensionRevision, updatedFMA, mdms, irPercentage, updatedIR, updatedAdditionalPension, additionalPensionPercentage, daPercentage, updatedDA, updatedCommutedPension, retirementStartedYear, updatedTotalPension, updatedNetPension;
                              return _regenerator2.default.wrap(function _callee$(_context) {
                                        while (1) {
                                                  switch (_context.prev = _context.next) {
                                                            case 0:
                                                                      payloads = [];
                                                                      tenantId = body.Parameters.tenantId;
                                                                      effectiveYear = Number(body.Parameters.effectiveYear);
                                                                      effectiveMonth = Number(body.Parameters.effectiveMonth);
                                                                      modifyDA = Boolean(body.Parameters.modifyDA);
                                                                      modifyIR = Boolean(body.Parameters.modifyIR);
                                                                      modifyFMA = Boolean(body.Parameters.modifyFMA);
                                                                      FMA = Number(body.Parameters.FMA);
                                                                      effetiveDate = new Date(effectiveYear, effectiveMonth - 1, 1);
                                                                      _context.next = 11;
                                                                      return (0, _utils.getPensionRevisions)(body.RequestInfo, body.Parameters.tenantId);

                                                            case 11:
                                                                      pensionRevisionResponse = _context.sent;
                                                                      pensionRevisions = pensionRevisionResponse.ProcessInstances[0].pensionRevision;

                                                                      _logger2.default.debug("pensionRevisions", pensionRevisions);
                                                                      newPensionRevisions = [];
                                                                      oldPensionRevisions = [];
                                                                      i = 0;

                                                            case 17:
                                                                      if (!(i < pensionRevisions.length)) {
                                                                                _context.next = 48;
                                                                                break;
                                                                      }

                                                                      pensionRevision = pensionRevisions[i];
                                                                      effectiveStartDate = new Date(pensionRevision.effectiveStartYear, pensionRevision.effectiveStartMonth - 1, 1);
                                                                      effectiveEndDate = pensionRevision.effectiveEndYear != null ? new Date(pensionRevision.effectiveEndYear, pensionRevision.effectiveEndMonth - 1, 1) : effetiveDate;
                                                                      oldPensionRevision = void 0;
                                                                      newPensionRevision = void 0;

                                                                      if (!(effetiveDate >= effectiveStartDate && effetiveDate <= effectiveEndDate)) {
                                                                                _context.next = 45;
                                                                                break;
                                                                      }

                                                                      oldPensionRevision = pensionRevision;
                                                                      newPensionRevision = pensionRevision;

                                                                      if (effectiveMonth == 1) {
                                                                                oldPensionRevision.effectiveEndMonth = 12;
                                                                      } else {
                                                                                oldPensionRevision.effectiveEndMonth = effectiveMonth - 1;
                                                                      }

                                                                      if (effectiveMonth == 1) {
                                                                                oldPensionRevision.effectiveEndYear = effectiveYear - 1;
                                                                      } else {
                                                                                oldPensionRevision.effectiveEndYear = effectiveYear;
                                                                      }

                                                                      updatedFMA = pensionRevision.fma;


                                                                      if (modifyFMA) {
                                                                                updatedFMA = FMA;
                                                                      }

                                                                      //newPensionRevision.fma = updatedFMA;

                                                                      _context.next = 32;
                                                                      return (0, _mdmsData2.default)(body.RequestInfo, tenantId);

                                                            case 32:
                                                                      mdms = _context.sent;
                                                                      irPercentage = (0, _calculationHelper.getIRPercentage)(effetiveDate, mdms);
                                                                      updatedIR = pensionRevision.interimRelief;


                                                                      if (modifyIR) {
                                                                                updatedIR = Math.round(pensionRevision.basicPension * irPercentage / 100);
                                                                      }

                                                                      updatedAdditionalPension = pensionRevision.additionalPension;
                                                                      additionalPensionPercentage = (0, _calculationHelper.getAdditionalPensionPercentageAfterRetirement)(new Date(Number(pensionRevision.dateOfBirth)), effetiveDate, mdms);


                                                                      updatedAdditionalPension = Math.ceil((pensionRevision.basicPension + updatedIR) * additionalPensionPercentage / 100);

                                                                      daPercentage = (0, _calculationHelper.getDAPercentage)(effetiveDate, mdms);
                                                                      updatedDA = pensionRevision.da;


                                                                      if (modifyDA) {
                                                                                updatedDA = Math.round((pensionRevision.basicPension + updatedIR + updatedAdditionalPension) * daPercentage / 100);
                                                                      }

                                                                      updatedCommutedPension = pensionRevision.commutedPension;


                                                                      if (pensionRevision.commutedPension > 0) {
                                                                                retirementStartedYear = (0, _calculationHelper.getYearDifference)(new Date(Number(pensionRevision.dateOfRetirement)), effetiveDate);


                                                                                if (retirementStartedYear > 15) {
                                                                                          updatedCommutedPension = 0;
                                                                                }
                                                                      }

                                                                      if (updatedFMA != pensionRevision.fma || updatedDA != pensionRevision.da || updatedIR != pensionRevision.interimRelief || updatedAdditionalPension != pensionRevision.additionalPension || updatedCommutedPension != pensionRevision.commutedPension || pensionRevision.effectiveStartYear == effectiveYear && pensionRevision.effectiveStartMonth == effectiveMonth) {
                                                                                updatedTotalPension = pensionRevision.basicPension + updatedDA - updatedCommutedPension + updatedAdditionalPension + updatedIR + updatedFMA + pensionRevision.miscellaneous + pensionRevision.woundExtraOrdinaryPension + pensionRevision.attendantAllowance;
                                                                                updatedNetPension = updatedTotalPension - pensionRevision.netDeductions;


                                                                                if (oldPensionRevision.effectiveStartYear == effectiveYear && oldPensionRevision.effectiveStartMonth == effectiveMonth) {} else {
                                                                                          oldPensionRevisions.push(oldPensionRevision);
                                                                                }

                                                                                newPensionRevisions.push({
                                                                                          tenantId: tenantId,
                                                                                          pensionerId: pensionRevision.pensionerId,
                                                                                          pensionRevisionId: pensionRevision.pensionRevisionId,
                                                                                          effectiveStartYear: effectiveYear,
                                                                                          effectiveStartMonth: effectiveMonth,
                                                                                          effectiveEndYear: null,
                                                                                          effectiveEndMonth: null,
                                                                                          pensionArrear: pensionRevision.pensionArrear,
                                                                                          fma: updatedFMA,
                                                                                          miscellaneous: pensionRevision.miscellaneous,
                                                                                          overPayment: pensionRevision.over_payment,
                                                                                          incomeTax: pensionRevision.income_tax,
                                                                                          cess: pensionRevision.cess,
                                                                                          basicPension: pensionRevision.basicPension,
                                                                                          commutedPension: updatedCommutedPension,
                                                                                          additionalPension: updatedAdditionalPension,
                                                                                          netDeductions: pensionRevision.netDeductions,
                                                                                          finalCalculatedPension: updatedNetPension,
                                                                                          active: true,
                                                                                          interimRelief: updatedIR,
                                                                                          da: updatedDA,
                                                                                          totalPension: updatedTotalPension,
                                                                                          pensionDeductions: pensionRevision.pensionDeductions,
                                                                                          pensionerFinalCalculatedBenefitId: pensionRevision.pensionerFinalCalculatedBenefitId,
                                                                                          woundExtraOrdinaryPension: pensionRevision.woundExtraOrdinaryPension,
                                                                                          attendantAllowance: pensionRevision.attendantAllowance

                                                                                });
                                                                      }

                                                            case 45:
                                                                      i++;
                                                                      _context.next = 17;
                                                                      break;

                                                            case 48:

                                                                      body.Parameters.oldPensionRevisions = oldPensionRevisions;
                                                                      body.Parameters.newPensionRevisions = newPensionRevisions;

                                                                      _context.next = 52;
                                                                      return (0, _create.addUUIDAndAuditDetailsCreatePensionRevisionBulk)(body);

                                                            case 52:
                                                                      body = _context.sent;


                                                                      payloads.push({
                                                                                topic: _envVariables2.default.KAFKA_TOPICS_CREATE_REVISED_PENSION_BULK,
                                                                                messages: JSON.stringify(body)
                                                                      });

                                                                      _producer2.default.send(payloads, function (err, data) {
                                                                                var response = {
                                                                                          ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                                                                                          ProcessInstances: body.Parameters
                                                                                };
                                                                                res.json(response);
                                                                      });

                                                            case 55:
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
//# sourceMappingURL=updatePensionRevisionBulk.js.map