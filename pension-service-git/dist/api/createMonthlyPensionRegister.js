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

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_createMonthlyPensionRegister", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var payloads, processInstance, processInstances, tenantId, effectiveYear, effectiveMonth, effetiveDate, pensionRevisionResponse, pensionRevisions, pensionRegister, i, pensionRevision, effectiveStartDate, effectiveEndDate;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              payloads = [];
              processInstance = {
                tenantId: body.ProcessInstances[0].tenantId,
                effectiveYear: body.ProcessInstances[0].effectiveYear,
                effectiveMonth: body.ProcessInstances[0].effectiveMonth,
                pensionRegister: []
              };
              processInstances = [];

              processInstances.push(processInstance);
              body.ProcessInstances = processInstances;

              tenantId = body.ProcessInstances[0].tenantId;
              effectiveYear = Number(body.ProcessInstances[0].effectiveYear);
              effectiveMonth = Number(body.ProcessInstances[0].effectiveMonth);
              effetiveDate = new Date(effectiveYear, effectiveMonth, 1);
              _context.next = 11;
              return (0, _utils.getPensionRevisions)(body.RequestInfo, body.ProcessInstances[0].tenantId);

            case 11:
              pensionRevisionResponse = _context.sent;
              pensionRevisions = pensionRevisionResponse.ProcessInstances[0].pensionRevision;

              _logger2.default.debug("pensionRevisions", pensionRevisions);
              pensionRegister = [];


              for (i = 0; i < pensionRevisions.length; i++) {
                pensionRevision = pensionRevisions[i];
                effectiveStartDate = new Date(pensionRevision.effectiveStartYear, pensionRevision.effectiveStartMonth, 1);
                effectiveEndDate = pensionRevision.effectiveEndYear != null ? new Date(pensionRevision.effectiveEndYear, pensionRevision.effectiveEndMonth, 1) : effetiveDate;


                if (effetiveDate >= effectiveStartDate && effetiveDate <= effectiveEndDate) {
                  pensionRegister.push({
                    tenantId: tenantId,
                    //pensionerId: pensionRevision.pensionerId,
                    pensionRevisionId: pensionRevision.pensionRevisionId,
                    effectiveYear: effectiveYear,
                    effectiveMonth: effectiveMonth
                  });
                }
              }

              body.ProcessInstances[0].pensionRegister = pensionRegister;

              _context.next = 19;
              return (0, _create.addUUIDAndAuditDetailsCreateMonthlyPensionRegister)(body);

            case 19:
              body = _context.sent;


              payloads.push({
                topic: _envVariables2.default.KAFKA_TOPICS_CREATE_MONTHLY_PENSION_REGISTER,
                messages: JSON.stringify(body)
              });
              _producer2.default.send(payloads, function (err, data) {
                var response = {
                  ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                  ProcessInstances: body.ProcessInstances
                };
                res.json(response);
              });

            case 22:
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
//# sourceMappingURL=createMonthlyPensionRegister.js.map