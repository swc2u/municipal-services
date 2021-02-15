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

var _message = require("../utils/message");

var _set = require("lodash/set");

var _set2 = _interopRequireDefault(_set);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_saveEmployeeToPensionNotificationRegister", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var payloads, message;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              payloads = [];
              message = (0, _message.Message)();

              //let userEventToUserResponse=await createUserEventToUser(body,body.Employees[0].tenantId,message.EMPLOYEE_PUSHED_TO_PNR_USER_EVENT_NAME,message.EMPLOYEE_PUSHED_TO_PNR_USER_EVENT_DESCRIPTION,body.Employees[0].uuid); 

              if (body.Employees.length > 0) {
                //let userEventToRoleResponse=await createUserEventToRole(body,body.Employees[0].tenantId,message.PNR_GENERATED_USER_EVENT_NAME,message.PNR_GENERATED_USER_EVENT_DESCRIPTION,envVariables.EGOV_MONTHLY_PNR_GENERATED_USER_EVENT_ROLE); 

              }

              payloads.push({
                topic: _envVariables2.default.KAFKA_TOPICS_SAVE_EMPLOYEE_TO_PENSION_NOTIFICATION_REGISTER,
                messages: JSON.stringify(body)
              });
              _producer2.default.send(payloads, function (err, data) {
                var response = {
                  ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                  Employees: body.Employees
                };
                res.json(response);
              });

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
//# sourceMappingURL=saveEmployeeToPensionNotificationRegister.js.map