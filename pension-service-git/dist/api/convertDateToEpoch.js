"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _express = require("express");

var _utils = require("../utils");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_convertDateToEpoch", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(request, res, next) {
      var queryObj, epochDate;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              /*let response = {
                ResponseInfo: requestInfoToResponseInfo(request.body.RequestInfo, true),
                Epoch: []
              };*/

              queryObj = JSON.parse(JSON.stringify(request.query));
              _context.next = 3;
              return (0, _utils.convertDateToEpoch)(queryObj.date, "dob");

            case 3:
              epochDate = _context.sent;

              //response.Epoch.push(epochDate);
              //res.json(response);  
              res.json(epochDate);

            case 5:
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
//# sourceMappingURL=convertDateToEpoch.js.map