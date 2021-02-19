"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var workflowTemplateInterface = function workflowTemplateInterface() {
  var pension = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

  var payload = {};
  var topic = "egov.core.notification.sms";
  var smsRequest = {
    mobileNumber: (0, _get2.default)(pension.employee.user, "mobileNumber")
  };
  var applicantName = (0, _get2.default)(pension.employee.user, "name");
  var applicationType = (0, _get2.default)(pension, "businessService");
  var applicationNumber = (0, _get2.default)(pension, "businessId");
  var action = (0, _get2.default)(pension, "action");
  switch (applicationType) {
    case _envVariables2.default.EGOV_PENSION_RRP_BUSINESS_SERVICE:
      switch (action) {
        case _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_FORWARD:
          {
            smsRequest["message"] = "Dear " + applicantName + ", Your Regular Retirement Pension application has been initiated. Your application number is " + applicationNumber + ".";
          }
          break;
      }
      break;
    case _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE:
      switch (action) {
        case _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_FORWARD:
          {
            smsRequest["message"] = "This is to inform dependents of " + applicantName + " that the application of death benefit has been initiated. Please note application number " + applicationNumber + " for future reference.";
          }
          break;
      }
      break;
    case _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE:
      switch (action) {
        case _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_FORWARD:
          {
            smsRequest["message"] = "This is to inform dependents of " + applicantName + " that the application of death benefit has been initiated. Please note application number " + applicationNumber + " for future reference.";
          }
          break;
      }
      break;
  }
  console.log("smsRequest", JSON.stringify(smsRequest));

  payload = {
    topic: topic,
    messages: JSON.stringify(smsRequest)
  };
  return payload;
};

exports.default = workflowTemplateInterface;
//# sourceMappingURL=notificaitonTemplate.js.map