"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _api = require("./api");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = function () {
  var _ref = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee() {
    var requestInfo = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    var tenantId = arguments[1];
    var index, requestBody, mdmsResponse;
    return _regenerator2.default.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            if (String(tenantId).includes(".")) {
              index = String(tenantId).indexOf(".");

              tenantId = String(tenantId).substring(0, index);
            }
            requestBody = {
              RequestInfo: requestInfo,
              MdmsCriteria: {
                tenantId: tenantId,
                moduleDetails: [{
                  moduleName: "pension",
                  masterDetails: [{ name: "employeeType" }, { name: "dependentsEligibilityForGratuity" }, { name: "dependentsEligibilityForPension" }, { name: "benefits" }, { name: "pensionRevision" }, { name: "notifications" }, { name: "DAPercentage" }, { name: "CommutationPercentage" }, { name: "CommutationMultiplier" }, { name: "IRPercentage" }, { name: "AdditionalPensionPercentage" }, { name: "PensionConfig" }]
                }]
              }
            };
            _context.next = 4;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_MDMS_HOST,
              endPoint: "" + _envVariables2.default.EGOV_MDMS_CONTEXT_PATH + _envVariables2.default.EGOV_MDMS_SEARCH_ENPOINT,
              requestBody: requestBody
            });

          case 4:
            mdmsResponse = _context.sent;
            return _context.abrupt("return", mdmsResponse);

          case 6:
          case "end":
            return _context.stop();
        }
      }
    }, _callee, undefined);
  }));

  return function () {
    return _ref.apply(this, arguments);
  };
}();
//# sourceMappingURL=mdmsData.js.map