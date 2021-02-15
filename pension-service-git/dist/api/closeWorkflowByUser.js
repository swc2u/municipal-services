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
  api.post("/_closeWorkflowByUser", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var payloads, topic, workflowSearchResponse, businessService;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              payloads = [];
              topic = "";
              _context.next = 4;
              return (0, _utils.searchPensionWorkflow)(body.RequestInfo, body.ProcessInstances[0].tenantId, body.ProcessInstances[0].businessId);

            case 4:
              workflowSearchResponse = _context.sent;

              _logger2.default.debug("workflowSearchResponse", JSON.stringify(workflowSearchResponse));
              //currentState=workflowSearchResponse.ProcessInstances[0].state.state;     
              businessService = body.ProcessInstances[0].businessService;
              _context.next = 9;
              return (0, _create.addUUIDAndAuditDetailsCloseWorkflow)(body, workflowSearchResponse);

            case 9:
              body = _context.sent;
              _context.t0 = businessService;
              _context.next = _context.t0 === _envVariables2.default.EGOV_PENSION_RRP_BUSINESS_SERVICE ? 13 : _context.t0 === _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE ? 15 : _context.t0 === _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE ? 17 : 19;
              break;

            case 13:
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_CLOSED;
              return _context.abrupt("break", 19);

            case 15:
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_CLOSED;
              return _context.abrupt("break", 19);

            case 17:
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_CLOSED;
              return _context.abrupt("break", 19);

            case 19:

              payloads.push({
                topic: topic,
                messages: JSON.stringify(body)
              });
              _producer2.default.send(payloads, function (err, data) {
                var response = {
                  ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                  ProcessInstances: body.ProcessInstances
                };
                res.json(response);
              });

            case 21:
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
//# sourceMappingURL=closeWorkflowByUser.js.map