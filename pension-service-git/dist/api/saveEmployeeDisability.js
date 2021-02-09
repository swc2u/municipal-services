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

var _mdmsData = require("../utils/mdmsData");

var _mdmsData2 = _interopRequireDefault(_mdmsData);

var _message = require("../utils/message");

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _set = require("lodash/set");

var _set2 = _interopRequireDefault(_set);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _filter = require("lodash/filter");

var _filter2 = _interopRequireDefault(_filter);

var _search = require("../utils/search");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var asyncHandler = require("express-async-handler");

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();
  api.post("/_saveEmployeeDisability", asyncHandler(function () {
    var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(_ref3, res, next) {
      var body = _ref3.body;
      var payloads, mdms, mdmsDisability, disability, message, errors, disabilityPercentage, pensionEmployeeId, pensionResponse, pensionEmployeesList;
      return _regenerator2.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              payloads = [];
              //getting mdms data      

              //getting mdms data

              _context.next = 3;
              return (0, _mdmsData2.default)(body.RequestInfo, body.Employees[0].tenantId);

            case 3:
              mdms = _context.sent;
              mdmsDisability = (0, _get2.default)(mdms, "MdmsRes.pension.Disability");
              disability = (0, _filter2.default)(mdmsDisability, function (x) {
                return x.code == body.Employees[0].severityOfDisability;
              });

              if (!(disability.length == 0)) {
                _context.next = 12;
                break;
              }

              message = (0, _message.Message)();
              errors = message.DISABILITY_PERCENTAGE_NOT_SET;

              if (!(errors.length > 0)) {
                _context.next = 12;
                break;
              }

              next({
                errorType: "custom",
                errorReponse: {
                  ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                  Errors: errors
                }
              });
              return _context.abrupt("return");

            case 12:
              disabilityPercentage = Number(disability[0].value);

              body.Employees[0].disabilityPercentage = disabilityPercentage;

              pensionEmployeeId = "";
              _context.next = 17;
              return (0, _utils.getPensionEmployees)(body.RequestInfo, body.Employees[0].tenantId, body.Employees[0].code);

            case 17:
              pensionResponse = _context.sent;
              pensionEmployeesList = pensionResponse.Employees;

              if (pensionEmployeesList.length > 0) {
                pensionEmployeeId = pensionEmployeesList[0].uuid;
                body.Employees[0].uuid = pensionEmployeeId;
              }

              _context.next = 22;
              return (0, _create.addUUIDAndAuditDetailsDisabilityRegistration)(body);

            case 22:
              body = _context.sent;


              payloads.push({
                topic: _envVariables2.default.KAFKA_TOPICS_SAVE_DISABILITY_DETAILS,
                messages: JSON.stringify(body)
              });
              _producer2.default.send(payloads, function (err, data) {
                var response = {
                  ResponseInfo: (0, _utils.requestInfoToResponseInfo)(body.RequestInfo, true),
                  Employees: body.Employees
                };
                res.json(response);
              });

            case 25:
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
//# sourceMappingURL=saveEmployeeDisability.js.map