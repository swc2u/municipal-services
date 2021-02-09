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

var _create = require("../utils/create");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_releaseWorkflow", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var payloads, topic;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              payloads = [];
              _context.next = 3;
              return (0, _create.addUUIDAndAuditDetailsClaimReleaseWorkflow)(body);

            case 3:
              body = _context.sent;
              topic = _envVariables2.default.KAFKA_TOPICS_RELEASE_WORKFLOW;


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

            case 7:
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
//# sourceMappingURL=releaseWorkflow.js.map