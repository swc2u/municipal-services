"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _express = require("express");

var _express2 = _interopRequireDefault(_express);

var _cors = require("cors");

var _cors2 = _interopRequireDefault(_cors);

var _morgan = require("morgan");

var _morgan2 = _interopRequireDefault(_morgan);

var _bodyParser = require("body-parser");

var _bodyParser2 = _interopRequireDefault(_bodyParser);

var _db = require("./db");

var _db2 = _interopRequireDefault(_db);

var _middleware = require("./middleware");

var _middleware2 = _interopRequireDefault(_middleware);

var _api = require("./api");

var _api2 = _interopRequireDefault(_api);

var _config = require("./config.json");

var _config2 = _interopRequireDefault(_config);

var _tracer = require("./middleware/tracer");

var _tracer2 = _interopRequireDefault(_tracer);

var _health = require("./utils/health");

var _health2 = _interopRequireDefault(_health);

var _envVariables = require("./envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _nodeSchedule = require("node-schedule");

var _nodeSchedule2 = _interopRequireDefault(_nodeSchedule);

var _filter = require("lodash/filter");

var _filter2 = _interopRequireDefault(_filter);

var _lte = require("lodash/lte");

var _lte2 = _interopRequireDefault(_lte);

var _gte = require("lodash/gte");

var _gte2 = _interopRequireDefault(_gte);

var _chain = require("lodash/chain");

var _chain2 = _interopRequireDefault(_chain);

var _producer = require("./kafka/producer");

var _producer2 = _interopRequireDefault(_producer);

var _utils = require("./utils");

var _search = require("./utils/search");

var _userService = require("./services/userService");

var _logger = require("./config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

require("babel-core/register");
require("babel-polyfill");
//import http from "http";

// import util from "util";

var swaggerUi = require("swagger-ui-express"),
    swaggerDocument = require("./swagger.json");
//const { createTerminus } = require("@godaddy/terminus");

// const validator = require('swagger-express-validator');

// const opts = {
//   schema:swaggerDocument, // Swagger schema
//   preserveResponseContentType: false, // Do not override responses for validation errors to always be JSON, default is true
//   returnRequestErrors: true, // Include list of request validation errors with response, default is false
//   returnResponseErrors: true, // Include list of response validation errors with response, default is false
//   validateRequest: true,
//   validateResponse: true,
//   requestValidationFn: (req, data, errors) => {
//     logger.debug(`failed request validation: ${req.method} ${req.originalUrl}\n ${util.inspect(errors)}`)
//   },
//   responseValidationFn: (req, data, errors) => {
//     logger.debug(`failed response validation: ${req.method} ${req.originalUrl}\n ${util.inspect(errors)}`)
//   },
//   async: true
// };

var app = (0, _express2.default)();
//app.server = http.createServer(app);

// Enable health checks and kubernetes shutdown hooks
//createTerminus(app.server, terminusOptions);

// logger
app.use((0, _morgan2.default)("dev"));

// 3rd party middleware
app.use((0, _cors2.default)({
  exposedHeaders: _config2.default.corsHeaders
}));

app.use(_bodyParser2.default.json({
  limit: _config2.default.bodyLimit
}));

app.use((0, _tracer2.default)());

app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerDocument));

// internal middleware
app.use((0, _middleware2.default)({ config: _config2.default, db: _db2.default }));

// app.use(validator(opts));

// api router

app.use("/", (0, _api2.default)({ config: _config2.default, db: _db2.default }));

//error handler middleware
app.use(function (err, req, res, next) {

  _logger2.default.error(err);
  if (!err.errorType) {
    res.status(err.status).json(err.data);
  } else if (err.errorType == "custom") {
    res.status(400).json(err.errorReponse);
  } else {
    res.status(500);
    res.send("Oops, something went wrong.");
  }
});

/* app.server.listen(envVariables.SERVER_PORT, () => {
  logger.debug(`Started on port ${app.server.address().port}`);
}); */

app.listen(_envVariables2.default.SERVER_PORT, function () {
  _logger2.default.debug("Started on port " + _envVariables2.default.SERVER_PORT);
});

var rulePensionNotificationRegister = new _nodeSchedule2.default.RecurrenceRule();
rulePensionNotificationRegister.date = _envVariables2.default.EGOV_PENSION_PNR_SCHEDULAR_START_DATE;
rulePensionNotificationRegister.hour = _envVariables2.default.EGOV_PENSION_PNR_SCHEDULAR_START_HOURS;
rulePensionNotificationRegister.minute = _envVariables2.default.EGOV_PENSION_PNR_SCHEDULAR_START_MINUTES;

var j = _nodeSchedule2.default.scheduleJob(rulePensionNotificationRegister, (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee() {
  var loginResponse, requestInfo, userInfo, response, requestBody;
  return _regenerator2.default.wrap(function _callee$(_context) {
    while (1) {
      switch (_context.prev = _context.next) {
        case 0:
          _logger2.default.debug('Pension Notification Register Schedular Started.');

          _context.next = 3;
          return (0, _utils.loginRequest)(_envVariables2.default.EGOV_PENSION_SCHEDULAR_USERNAME, _envVariables2.default.EGOV_PENSION_SCHEDULAR_PASSWORD, _envVariables2.default.EGOV_PENSION_SCHEDULAR_GRANT_TYPE, _envVariables2.default.EGOV_PENSION_SCHEDULAR_SCOPE, _envVariables2.default.EGOV_PENSION_SCHEDULAR_TENANTID, _envVariables2.default.EGOV_PENSION_SCHEDULAR_USERTYPE);

        case 3:
          loginResponse = _context.sent;
          requestInfo = {
            apiId: _envVariables2.default.EGOV_PENSION_REQUESTINFO_API_ID,
            ver: _envVariables2.default.EGOV_PENSION_REQUESTINFO_VER,
            action: _envVariables2.default.EGOV_PENSION_REQUESTINFO_ACTION,
            did: _envVariables2.default.EGOV_PENSION_REQUESTINFO_DID,
            key: _envVariables2.default.EGOV_PENSION_REQUESTINFO_KEY,
            msgId: _envVariables2.default.EGOV_PENSION_REQUESTINFO_MSG_ID,
            requesterId: _envVariables2.default.EGOV_PENSION_REQUESTINFO_REQUSTER_ID,
            authToken: loginResponse.access_token
          };

          //appends userInfo in requestInfo object 

          _context.next = 7;
          return (0, _userService.userDetails)(requestInfo, loginResponse.access_token);

        case 7:
          userInfo = _context.sent;

          requestInfo.userInfo = userInfo;

          response = void 0;
          requestBody = {
            RequestInfo: requestInfo
          };
          _context.next = 13;
          return (0, _utils.pushEmployeesToPensionNotificationRegister)(requestBody);

        case 13:
          response = _context.sent;

          _logger2.default.debug(response);

        case 15:
        case "end":
          return _context.stop();
      }
    }
  }, _callee, undefined);
})));

var ruleCreatePensionRegister = new _nodeSchedule2.default.RecurrenceRule();
ruleCreatePensionRegister.date = _envVariables2.default.EGOV_PENSION_CREATE_PENSION_REGISTER_SCHEDULAR_START_DATE;
ruleCreatePensionRegister.hour = _envVariables2.default.EGOV_PENSION_CREATE_PENSION_REGISTER_SCHEDULAR_START_HOURS;
ruleCreatePensionRegister.minute = _envVariables2.default.EGOV_PENSION_CREATE_PENSION_REGISTER_SCHEDULAR_START_MINUTES;

var i = _nodeSchedule2.default.scheduleJob(ruleCreatePensionRegister, (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2() {
  var loginResponse, requestInfo, userInfo, response, requestBody;
  return _regenerator2.default.wrap(function _callee2$(_context2) {
    while (1) {
      switch (_context2.prev = _context2.next) {
        case 0:
          _logger2.default.debug('Pension Register Schedular Started.');
          _context2.next = 3;
          return (0, _utils.loginRequest)(_envVariables2.default.EGOV_PENSION_SCHEDULAR_USERNAME, _envVariables2.default.EGOV_PENSION_SCHEDULAR_PASSWORD, _envVariables2.default.EGOV_PENSION_SCHEDULAR_GRANT_TYPE, _envVariables2.default.EGOV_PENSION_SCHEDULAR_SCOPE, _envVariables2.default.EGOV_PENSION_SCHEDULAR_TENANTID, _envVariables2.default.EGOV_PENSION_SCHEDULAR_USERTYPE);

        case 3:
          loginResponse = _context2.sent;
          requestInfo = {
            apiId: _envVariables2.default.EGOV_PENSION_REQUESTINFO_API_ID,
            ver: _envVariables2.default.EGOV_PENSION_REQUESTINFO_VER,
            action: _envVariables2.default.EGOV_PENSION_REQUESTINFO_ACTION,
            did: _envVariables2.default.EGOV_PENSION_REQUESTINFO_DID,
            key: _envVariables2.default.EGOV_PENSION_REQUESTINFO_KEY,
            msgId: _envVariables2.default.EGOV_PENSION_REQUESTINFO_MSG_ID,
            requesterId: _envVariables2.default.EGOV_PENSION_REQUESTINFO_REQUSTER_ID,
            authToken: loginResponse.access_token
          };

          //appends userInfo in requestInfo object 

          _context2.next = 7;
          return (0, _userService.userDetails)(requestInfo, loginResponse.access_token);

        case 7:
          userInfo = _context2.sent;

          requestInfo.userInfo = userInfo;

          response = void 0;
          requestBody = {
            RequestInfo: requestInfo,
            ProcessInstances: []
          };
          _context2.next = 13;
          return (0, _utils.createMonthlyPensionRegister)(requestBody);

        case 13:
          response = _context2.sent;

          _logger2.default.debug(response);

        case 15:
        case "end":
          return _context2.stop();
      }
    }
  }, _callee2, undefined);
})));

var ruleUpdatePensionRevisionBulk = new _nodeSchedule2.default.RecurrenceRule();
ruleUpdatePensionRevisionBulk.date = _envVariables2.default.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_START_DATE;
ruleUpdatePensionRevisionBulk.hour = _envVariables2.default.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_START_HOURS;
ruleUpdatePensionRevisionBulk.minute = _envVariables2.default.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_START_MINUTES;

var i = _nodeSchedule2.default.scheduleJob(ruleUpdatePensionRevisionBulk, (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee3() {
  var loginResponse, requestInfo, userInfo, response, requestBody;
  return _regenerator2.default.wrap(function _callee3$(_context3) {
    while (1) {
      switch (_context3.prev = _context3.next) {
        case 0:
          _logger2.default.debug('Pension Revision Bulk Schedular Started.');
          _context3.next = 3;
          return (0, _utils.loginRequest)(_envVariables2.default.EGOV_PENSION_SCHEDULAR_USERNAME, _envVariables2.default.EGOV_PENSION_SCHEDULAR_PASSWORD, _envVariables2.default.EGOV_PENSION_SCHEDULAR_GRANT_TYPE, _envVariables2.default.EGOV_PENSION_SCHEDULAR_SCOPE, _envVariables2.default.EGOV_PENSION_SCHEDULAR_TENANTID, _envVariables2.default.EGOV_PENSION_SCHEDULAR_USERTYPE);

        case 3:
          loginResponse = _context3.sent;
          requestInfo = {
            apiId: _envVariables2.default.EGOV_PENSION_REQUESTINFO_API_ID,
            ver: _envVariables2.default.EGOV_PENSION_REQUESTINFO_VER,
            action: _envVariables2.default.EGOV_PENSION_REQUESTINFO_ACTION,
            did: _envVariables2.default.EGOV_PENSION_REQUESTINFO_DID,
            key: _envVariables2.default.EGOV_PENSION_REQUESTINFO_KEY,
            msgId: _envVariables2.default.EGOV_PENSION_REQUESTINFO_MSG_ID,
            requesterId: _envVariables2.default.EGOV_PENSION_REQUESTINFO_REQUSTER_ID,
            authToken: loginResponse.access_token
          };

          //appends userInfo in requestInfo object 

          _context3.next = 7;
          return (0, _userService.userDetails)(requestInfo, loginResponse.access_token);

        case 7:
          userInfo = _context3.sent;

          requestInfo.userInfo = userInfo;

          response = void 0;
          requestBody = {
            RequestInfo: requestInfo,
            Parameters: {}
          };
          _context3.next = 13;
          return (0, _utils.updatePensionRevisionBulk)(requestBody);

        case 13:
          response = _context3.sent;

          _logger2.default.debug(response);

        case 15:
        case "end":
          return _context3.stop();
      }
    }
  }, _callee3, undefined);
})));

exports.default = app;
//# sourceMappingURL=index.js.map