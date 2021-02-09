"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var _require = require("winston"),
    createLogger = _require.createLogger,
    format = _require.format,
    transports = _require.transports;

var logger = createLogger({
  level: _envVariables2.default.LOGGING_LEVEL,
  format: format.combine(format.timestamp({ format: "YYYY-MM-DD HH:mm:ss.SSSZZ" }), format.json()),
  transports: [new transports.Console()]
});

exports.default = logger;
//# sourceMappingURL=logger.js.map