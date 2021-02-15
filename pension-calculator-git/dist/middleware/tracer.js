"use strict";

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var uuidv4 = require("uuid/v4");


var CORRELATION_ID_HEADER = "x-correlation-id";
var CORRELATION_ID_BODY = "correlationId";

var getCorrelationIdFromRequest = function getCorrelationIdFromRequest(req) {
  var correlationId = req.get(CORRELATION_ID_HEADER);

  if (typeof correlationId === "undefined") {
    if (req.body.RequestInfo && req.body.RequestInfo[CORRELATION_ID_BODY]) {
      correlationId = req.body.RequestInfo[CORRELATION_ID_BODY];
    }
  }

  if (typeof correlationId === "undefined") {
    correlationId = uuidv4();
    if (req.body.RequestInfo && req.body.RequestInfo[CORRELATION_ID_BODY]) {
      req.body.RequestInfo[CORRELATION_ID_BODY] = correlationId;
    }
  }

  return correlationId;
};

module.exports = function (options) {
  return function (req, res, next) {
    var obj = {};
    obj["CORRELATION-ID"] = getCorrelationIdFromRequest(req);

    _logger2.default.info("Received request URI: " + req.originalUrl, obj);

    if (_envVariables2.default.TRACER_ENABLE_REQUEST_LOGGING && req.method === "POST" && req.is("application/json") ? true : false) {
      _logger2.default.info("Request body - " + JSON.stringify(req.body), obj);
      _logger2.default.info("Request body - " + JSON.stringify(req.query), obj);
    }

    res.on("finish", function () {
      _logger2.default.info("Response code sent: " + this.statusCode, obj);
    });

    next();
  };
};
//# sourceMappingURL=tracer.js.map