"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

// Implement health checks [kubernetes]
//  - Check DB Connectivity
//  - Check Kafka Connectivity [TBD]

var onHealthCheck = function () {
  var _ref = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee() {
    return _regenerator2.default.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            return _context.abrupt("return", _db2.default.query("SELECT 1"));

          case 1:
          case "end":
            return _context.stop();
        }
      }
    }, _callee, this);
  }));

  return function onHealthCheck() {
    return _ref.apply(this, arguments);
  };
}();

// Delay shutdown for service cleanup via kubernetes


var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

var _db = require("../db");

var _db2 = _interopRequireDefault(_db);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function beforeShutdown() {
  // given your readiness probes run every 5 second
  // may be worth using a bigger number so you won't
  // run into any race conditions
  return new Promise(function (resolve) {
    setTimeout(resolve, 5000);
  });
}

var options = {
  // healtcheck options
  healthChecks: {
    "/health": onHealthCheck // a promise returning function indicating service health
  },

  // cleanup options
  timeout: 5000, // [optional = 1000] number of milliseconds before forcefull exiting
  beforeShutdown: beforeShutdown, // [optional] called before the HTTP server starts its shutdown

  logger: _logger2.default.error // [optional] logger function to be called with errors
};

exports.default = options;
//# sourceMappingURL=health.js.map