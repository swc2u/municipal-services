"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

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

var _logger = require("./config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

require("babel-core/register");
require("babel-polyfill");
//import http from "http";


var swaggerUi = require("swagger-ui-express"),
    swaggerDocument = require("./swagger.json");

var _require = require("@godaddy/terminus"),
    createTerminus = _require.createTerminus;

// const validator = require('swagger-express-validator');

// const opts = {
//   schema:swaggerDocument, // Swagger schema
//   preserveResponseContentType: false, // Do not override responses for validation errors to always be JSON, default is true
//   returnRequestErrors: true, // Include list of request validation errors with response, default is false
//   returnResponseErrors: true, // Include list of response validation errors with response, default is false
//   validateRequest: true,
//   validateResponse: true,
//   requestValidationFn: (req, data, errors) => {
//     console.log(`failed request validation: ${req.method} ${req.originalUrl}\n ${util.inspect(errors)}`)
//   },
//   responseValidationFn: (req, data, errors) => {
//     console.log(`failed response validation: ${req.method} ${req.originalUrl}\n ${util.inspect(errors)}`)
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
app.use((0, _middleware2.default)());

// app.use(validator(opts));

// api router

app.use("/", (0, _api2.default)());

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
});
 */

app.listen(_envVariables2.default.SERVER_PORT, function () {
  _logger2.default.debug("Started on port " + _envVariables2.default.SERVER_PORT);
});

exports.default = app;
//# sourceMappingURL=index.js.map