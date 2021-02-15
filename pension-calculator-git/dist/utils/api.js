"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.httpGetRequest = exports.httpRequest = undefined;

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _httpClient = require("../config/httpClient");

var _httpClient2 = _interopRequireDefault(_httpClient);

var _index = require("./index");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var httpRequest = exports.httpRequest = function () {
  var _ref = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref2) {
    var hostURL = _ref2.hostURL,
        endPoint = _ref2.endPoint,
        _ref2$queryObject = _ref2.queryObject,
        queryObject = _ref2$queryObject === undefined ? [] : _ref2$queryObject,
        _ref2$requestBody = _ref2.requestBody,
        requestBody = _ref2$requestBody === undefined ? {} : _ref2$requestBody,
        _ref2$headers = _ref2.headers,
        headers = _ref2$headers === undefined ? [] : _ref2$headers,
        _ref2$customRequestIn = _ref2.customRequestInfo,
        customRequestInfo = _ref2$customRequestIn === undefined ? {} : _ref2$customRequestIn;
    var instance, errorReponse, response, responseStatus;
    return _regenerator2.default.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            instance = (0, _httpClient2.default)(hostURL);
            errorReponse = {};

            if (headers) instance.defaults = Object.assign(instance.defaults, {
              headers: headers
            });
            endPoint = (0, _index.addQueryArg)(endPoint, queryObject);
            _context.prev = 4;
            _context.next = 7;
            return instance.post(endPoint, requestBody);

          case 7:
            response = _context.sent;
            responseStatus = parseInt(response.status, 10);

            if (!(responseStatus === 200 || responseStatus === 201)) {
              _context.next = 11;
              break;
            }

            return _context.abrupt("return", response.data);

          case 11:
            _context.next = 16;
            break;

          case 13:
            _context.prev = 13;
            _context.t0 = _context["catch"](4);

            errorReponse = _context.t0.response;

          case 16:
            throw errorReponse;

          case 17:
          case "end":
            return _context.stop();
        }
      }
    }, _callee, undefined, [[4, 13]]);
  }));

  return function httpRequest(_x) {
    return _ref.apply(this, arguments);
  };
}();

var httpGetRequest = exports.httpGetRequest = function () {
  var _ref3 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(_ref4) {
    var hostURL = _ref4.hostURL,
        endPoint = _ref4.endPoint,
        _ref4$queryObject = _ref4.queryObject,
        queryObject = _ref4$queryObject === undefined ? [] : _ref4$queryObject,
        _ref4$requestBody = _ref4.requestBody,
        requestBody = _ref4$requestBody === undefined ? {} : _ref4$requestBody,
        _ref4$headers = _ref4.headers,
        headers = _ref4$headers === undefined ? [] : _ref4$headers,
        _ref4$customRequestIn = _ref4.customRequestInfo,
        customRequestInfo = _ref4$customRequestIn === undefined ? {} : _ref4$customRequestIn;
    var instance, errorReponse, response, responseStatus;
    return _regenerator2.default.wrap(function _callee2$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            instance = (0, _httpClient2.default)(hostURL);
            errorReponse = {};

            if (headers) instance.defaults = Object.assign(instance.defaults, {
              headers: headers
            });
            endPoint = (0, _index.addQueryArg)(endPoint, queryObject);
            _context2.prev = 4;
            _context2.next = 7;
            return instance.get(endPoint);

          case 7:
            response = _context2.sent;
            responseStatus = parseInt(response.status, 10);

            if (!(responseStatus === 200 || responseStatus === 201)) {
              _context2.next = 11;
              break;
            }

            return _context2.abrupt("return", response.data);

          case 11:
            _context2.next = 16;
            break;

          case 13:
            _context2.prev = 13;
            _context2.t0 = _context2["catch"](4);

            errorReponse = _context2.t0.response;

          case 16:
            throw errorReponse;

          case 17:
          case "end":
            return _context2.stop();
        }
      }
    }, _callee2, undefined, [[4, 13]]);
  }));

  return function httpGetRequest(_x2) {
    return _ref3.apply(this, arguments);
  };
}();
//# sourceMappingURL=api.js.map