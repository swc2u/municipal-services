"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var workflowTemplateInterfaceEMAIL = function workflowTemplateInterfaceEMAIL() {
  var pension = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

  var payload = {};
  var topic = "egov.core.notification.email";
  var emailRequest = {
    email: (0, _get2.default)(pension.employee.user, "emailId")
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
            emailRequest["subject"] = "Notification for Pension Application";
            emailRequest["body"] = "<table style=\"width: 601px;\">\n                    <tbody>\n                    <tr>\n                    <td style=\"width: 591px;\">Dear " + applicantName + ", <br /> <br /> Regular Retirement Pension application has been initiated. Application Number " + applicationNumber + " is for your future reference.<br /> <br /> Note: This is a system generated email. Please do not reply.<br /> <br /> Regards,<br /> Chandigarh Smart City Private Limited</td>\n                    </tr>\n                    </tbody>\n                    </table>";
          }
          break;
      }
      break;
    case _envVariables2.default.EGOV_PENSION_DOE_BUSINESS_SERVICE:
      switch (action) {
        case _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_FORWARD:
          {
            emailRequest["subject"] = "Notification for Death Benefit for employee";
            emailRequest["body"] = "<table style=\"width: 605px;\">\n              <tbody>\n              <tr>\n              <td style=\"width: 595px;\">Subject Line: Notification for Death Benefit for employee<br /> <br /> Dear Beneficiary of " + applicantName + ", <br /> <br /> Death benefit applicaton of &lt;employee name&gt; has been initiated. Application Number " + applicationNumber + " is for your future reference.<br /> <br /> Note: This is a system generated email. Please do not reply.<br /> <br /> Regards,<br /> Chandigarh Smart City Private Limited</td>\n              </tr>\n              </tbody>\n              </table>";
          }
          break;
      }
      break;
    case _envVariables2.default.EGOV_PENSION_DOP_BUSINESS_SERVICE:
      switch (action) {
        case _envVariables2.default.EGOV_PENSION_WORKFLOW_ACTION_FORWARD:
          {
            emailRequest["subject"] = "Notification for Death Benefit for pensioner";
            emailRequest["body"] = "<table style=\"width: 606px;\">\n              <tbody>\n              <tr>\n              <td style=\"width: 596px;\">Subject Line: Notification for Death Benefit for pensioner<br /> <br /> Dear Beneficiary of " + applicantName + ", <br /> <br /> Death benefit applicaton of " + applicantName + " has been initiated. Application Number " + applicationNumber + " is for your future reference.<br /> <br /> Note: This is a system generated email. Please do not reply.<br /> <br /> Regards,<br /> Chandigarh Smart City Private Limited</td>\n              </tr>\n              </tbody>\n              </table>\n              <p>&nbsp;</p>";
          }
          break;
      }
      break;
  }

  emailRequest["isHTML"] = true;
  _logger2.default.debug("emailRequest", JSON.stringify(emailRequest));

  payload = {
    topic: topic,
    messages: JSON.stringify(emailRequest)
  };
  return payload;
};

exports.default = workflowTemplateInterfaceEMAIL;
//# sourceMappingURL=notificaitonTemplateEMAIL.js.map