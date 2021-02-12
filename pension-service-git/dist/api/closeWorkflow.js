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
  api.post("/_closeWorkflow", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var payloads, action, topic, workflowSearchResponse, businessService, isContingentBillGenerated, workflowResponse;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              payloads = [];
              action = "";
              topic = "";
              _context.next = 5;
              return (0, _utils.searchPensionWorkflow)(body.RequestInfo, body.ProcessInstances[0].tenantId, body.ProcessInstances[0].businessId);

            case 5:
              workflowSearchResponse = _context.sent;

              _logger2.default.debug("workflowSearchResponse", JSON.stringify(workflowSearchResponse));
              //currentState=workflowSearchResponse.ProcessInstances[0].state.state;     
              businessService = body.ProcessInstances[0].businessService;
              isContingentBillGenerated = body.ProcessInstances[0].isContingentBillGenerated;

              if (isContingentBillGenerated) {
                action = _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_CLOSE;
              } else {
                action = _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_SEND_BACK;
              }
              body.ProcessInstances[0].action = action;
              _context.next = 13;
              return (0, _create.addUUIDAndAuditDetailsCloseWorkflow)(body, workflowSearchResponse);

            case 13:
              body = _context.sent;
              _context.next = 16;
              return (0, _utils.createWorkFlow)(body);

            case 16:
              workflowResponse = _context.sent;
              _context.t0 = action;
              _context.next = _context.t0 === _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_CLOSE ? 20 : _context.t0 === _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_SEND_BACK ? 30 : 31;
              break;

            case 20:
              _context.t1 = businessService;
              _context.next = _context.t1 === _envVariables2.default.EGOV_PENSION_RRP_BUSINESS_SERVICE ? 23 : _context.t1 === _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE ? 25 : _context.t1 === _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE ? 27 : 29;
              break;

            case 23:
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_CLOSED;
              return _context.abrupt("break", 29);

            case 25:
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_CLOSED;
              return _context.abrupt("break", 29);

            case 27:
              topic = _envVariables2.default.KAFKA_TOPICS_SAVE_CLOSED;
              return _context.abrupt("break", 29);

            case 29:
              return _context.abrupt("break", 32);

            case 30:
              return _context.abrupt("break", 32);

            case 31:
              return _context.abrupt("break", 32);

            case 32:

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

            case 34:
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
//# sourceMappingURL=closeWorkflow.js.map