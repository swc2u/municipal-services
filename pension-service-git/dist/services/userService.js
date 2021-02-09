"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.userDetails = exports.searchUser = undefined;

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _extends2 = require("babel-runtime/helpers/extends");

var _extends3 = _interopRequireDefault(_extends2);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _api = require("../utils/api");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var searchUser = exports.searchUser = function () {
  var _ref = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(requestInfo, userSearchReqCriteria) {
    var requestBody, userSearchResponse, dobFormat;
    return _regenerator2.default.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            requestBody = (0, _extends3.default)({ RequestInfo: requestInfo }, userSearchReqCriteria);
            _context.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_USER_HOST,
              endPoint: "" + _envVariables2.default.EGOV_USER_CONTEXT_PATH + _envVariables2.default.EGOV_USER_SEARCH_ENDPOINT,
              requestBody: requestBody
            });

          case 3:
            userSearchResponse = _context.sent;
            dobFormat = "yyyy-MM-dd";

            userSearchResponse = parseResponse(userSearchResponse, dobFormat);

            return _context.abrupt("return", userSearchResponse);

          case 7:
          case "end":
            return _context.stop();
        }
      }
    }, _callee, undefined);
  }));

  return function searchUser(_x, _x2) {
    return _ref.apply(this, arguments);
  };
}();

var parseResponse = function parseResponse(userResponse, dobFormat) {
  var format1 = "dd-MM-yyyy HH:mm:ss";
  userResponse.user && userResponse.user.map(function (user) {
    user.createdDate = user.createdDate && dateToLong(user.createdDate, format1);
    user.lastModifiedDate = user.lastModifiedDate && dateToLong(user.lastModifiedDate, format1);
    user.dob = user.dob && dateToLong(user.dob, dobFormat);
    user.pwdExpiryDate = user.pwdExpiryDate && dateToLong(user.pwdExpiryDate, format1);
  });
  return userResponse;
};

var dateToLong = function dateToLong(date, format) {
  var epoch = null;
  var formattedDays = null;

  switch (format) {
    case "dd-MM-yyyy HH:mm:ss":
      formattedDays = date.split(/\D+/);
      epoch = new Date(formattedDays[2], formattedDays[1] - 1, formattedDays[0], formattedDays[3], formattedDays[4], formattedDays[5]);
      break;
    case "yyyy-MM-dd":
      formattedDays = date.split("-");
      epoch = new Date(formattedDays[0], formattedDays[1] - 1, formattedDays[2]);

      break;
    case "dd/MM/yyyy":
      formattedDays = date.split("/");
      epoch = new Date(formattedDays[2], formattedDays[1] - 1, formattedDays[0]);

      break;
  }
  return epoch.getTime();
};

var dobConvetion = function dobConvetion(date) {
  return typeof date == "string" ? date.split("-").reverse().join("/") : date;
};

var userDetails = exports.userDetails = function () {
  var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(requestInfo, accessToken) {
    var requestBody, userResponse;
    return _regenerator2.default.wrap(function _callee2$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            _context2.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_USER_HOST,
              endPoint: "" + _envVariables2.default.EGOV_USER_CONTEXT_PATH + _envVariables2.default.EGOV_USER_DETAILS_ENDPOINT + "?access_token=" + accessToken,

              requestBody: requestBody
            });

          case 3:
            userResponse = _context2.sent;
            return _context2.abrupt("return", userResponse);

          case 5:
          case "end":
            return _context2.stop();
        }
      }
    }, _callee2, undefined);
  }));

  return function userDetails(_x3, _x4) {
    return _ref2.apply(this, arguments);
  };
}();

exports.default = { searchUser: searchUser, userDetails: userDetails };
//# sourceMappingURL=userService.js.map