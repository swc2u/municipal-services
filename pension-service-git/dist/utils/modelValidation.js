"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.validateWorkflowSearchModel = exports.validatePensionNotificationRegisterSearchModel = undefined;

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _some = require("lodash/some");

var _some2 = _interopRequireDefault(_some);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var pensionNotificationRegisterSearchSchema = require("../model/pensionNotificationRegisterSearch.js");
var workflowSearchSchema = require("../model/workflowSearch");

var getAjvInstance = function getAjvInstance() {
  var Ajv = require("ajv");
  var ajv = new Ajv({ allErrors: true });
  return ajv;
};

var validatePensionNotificationRegisterSearchModel = exports.validatePensionNotificationRegisterSearchModel = function validatePensionNotificationRegisterSearchModel(data) {
  var ajv = getAjvInstance();
  var validate = ajv.compile(pensionNotificationRegisterSearchSchema);
  var valid = validate(data);
  var errors = [];
  if (!valid) {
    errors = validate.errors;
  }
  return errors;
};

var validateWorkflowSearchModel = exports.validateWorkflowSearchModel = function validateWorkflowSearchModel(data) {
  var ajv = getAjvInstance();
  var validate = ajv.compile(workflowSearchSchema);
  var valid = validate(data);
  var errors = [];
  if (!valid) {
    errors = validate.errors;
  }
  return errors;
};
//# sourceMappingURL=modelValidation.js.map