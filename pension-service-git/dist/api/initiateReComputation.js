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

var _message = require("../utils/message");

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _set = require("lodash/set");

var _set2 = _interopRequireDefault(_set);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _filter = require("lodash/filter");

var _filter2 = _interopRequireDefault(_filter);

var _search = require("../utils/search");

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_initiateReComputation", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var payloads, message, tenantId, businessService, closedApplicationNumber, action, nextState, closedApplicationResponse, closedWorkflowHeaderId, pensionEmployeeId, employee, workflowResponse, topic;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              payloads = [];
              message = (0, _message.Message)();
              tenantId = body.ProcessInstances[0].tenantId;
              businessService = body.ProcessInstances[0].businessService;
              closedApplicationNumber = body.ProcessInstances[0].closedWorkflow.businessId;
              action = body.ProcessInstances[0].action;
              nextState = "";
              _context.next = 9;
              return (0, _utils.searchClosedApplication)(body.RequestInfo, tenantId, businessService, closedApplicationNumber);

            case 9:
              closedApplicationResponse = _context.sent;
              closedWorkflowHeaderId = closedApplicationResponse.ProcessInstances[0].workflowHeaderId;
              pensionEmployeeId = closedApplicationResponse.ProcessInstances[0].pensionEmployeeId;

              body.ProcessInstances[0].closedWorkflow.workflowHeaderId = closedWorkflowHeaderId;
              employee = {
                pensionEmployeeId: pensionEmployeeId
              };

              body.ProcessInstances[0].employee = employee;

              _context.next = 17;
              return (0, _create.addUUIDAndAuditDetailsInitiateReComputation)(body);

            case 17:
              body = _context.sent;
              _context.next = 20;
              return (0, _utils.createWorkFlow)(body);

            case 20:
              workflowResponse = _context.sent;

              nextState = workflowResponse.ProcessInstances[0].state.state;
              body.ProcessInstances[0].workflowHeader.state = nextState;

              topic = _envVariables2.default.KAFKA_TOPICS_INITIATE_RECOMPUTATION;


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

            case 26:
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
//# sourceMappingURL=initiateReComputation.js.map