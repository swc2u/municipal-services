"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _package = require("../../package.json");

var _express = require("express");

var _saveEmployeeToPensionNotificationRegister = require("./saveEmployeeToPensionNotificationRegister");

var _saveEmployeeToPensionNotificationRegister2 = _interopRequireDefault(_saveEmployeeToPensionNotificationRegister);

var _searchPensionNotificationRegister = require("./searchPensionNotificationRegister");

var _searchPensionNotificationRegister2 = _interopRequireDefault(_searchPensionNotificationRegister);

var _processWorkflow = require("./processWorkflow");

var _processWorkflow2 = _interopRequireDefault(_processWorkflow);

var _searchWorkflow = require("./searchWorkflow");

var _searchWorkflow2 = _interopRequireDefault(_searchWorkflow);

var _searchEmployeeFromHRMS = require("./searchEmployeeFromHRMS");

var _searchEmployeeFromHRMS2 = _interopRequireDefault(_searchEmployeeFromHRMS);

var _getWorkflowAccessibility = require("./getWorkflowAccessibility");

var _getWorkflowAccessibility2 = _interopRequireDefault(_getWorkflowAccessibility);

var _claimWorkflow = require("./claimWorkflow");

var _claimWorkflow2 = _interopRequireDefault(_claimWorkflow);

var _releaseWorkflow = require("./releaseWorkflow");

var _releaseWorkflow2 = _interopRequireDefault(_releaseWorkflow);

var _saveEmployees = require("./saveEmployees");

var _saveEmployees2 = _interopRequireDefault(_saveEmployees);

var _getEpochForDate = require("./getEpochForDate");

var _getEpochForDate2 = _interopRequireDefault(_getEpochForDate);

var _convertDateToEpoch = require("./convertDateToEpoch");

var _convertDateToEpoch2 = _interopRequireDefault(_convertDateToEpoch);

var _searchEmployee = require("./searchEmployee");

var _searchEmployee2 = _interopRequireDefault(_searchEmployee);

var _closeWorkflow = require("./closeWorkflow");

var _closeWorkflow2 = _interopRequireDefault(_closeWorkflow);

var _calculateBenefit = require("./calculateBenefit");

var _calculateBenefit2 = _interopRequireDefault(_calculateBenefit);

var _checkDependentEligibilityForBenefit = require("./checkDependentEligibilityForBenefit");

var _checkDependentEligibilityForBenefit2 = _interopRequireDefault(_checkDependentEligibilityForBenefit);

var _getPensionEmployees = require("./getPensionEmployees");

var _getPensionEmployees2 = _interopRequireDefault(_getPensionEmployees);

var _searchPensioner = require("./searchPensioner");

var _searchPensioner2 = _interopRequireDefault(_searchPensioner);

var _searchPensionerForPensionRevision = require("./searchPensionerForPensionRevision");

var _searchPensionerForPensionRevision2 = _interopRequireDefault(_searchPensionerForPensionRevision);

var _updateRevisedPension = require("./updateRevisedPension");

var _updateRevisedPension2 = _interopRequireDefault(_updateRevisedPension);

var _createRevisedPension = require("./createRevisedPension");

var _createRevisedPension2 = _interopRequireDefault(_createRevisedPension);

var _createMonthlyPensionRegister = require("./createMonthlyPensionRegister");

var _createMonthlyPensionRegister2 = _interopRequireDefault(_createMonthlyPensionRegister);

var _searchWorkflowPaymentDetails = require("./searchWorkflowPaymentDetails");

var _searchWorkflowPaymentDetails2 = _interopRequireDefault(_searchWorkflowPaymentDetails);

var _getPensionRevisions = require("./getPensionRevisions");

var _getPensionRevisions2 = _interopRequireDefault(_getPensionRevisions);

var _closeWorkflowByUser = require("./closeWorkflowByUser");

var _closeWorkflowByUser2 = _interopRequireDefault(_closeWorkflowByUser);

var _searchPensionRegister = require("./searchPensionRegister");

var _searchPensionRegister2 = _interopRequireDefault(_searchPensionRegister);

var _pushManualRegisterToPensionNotificationRegister = require("./pushManualRegisterToPensionNotificationRegister");

var _pushManualRegisterToPensionNotificationRegister2 = _interopRequireDefault(_pushManualRegisterToPensionNotificationRegister);

var _saveEmployeeDisability = require("./saveEmployeeDisability");

var _saveEmployeeDisability2 = _interopRequireDefault(_saveEmployeeDisability);

var _getEmployeeDisability = require("./getEmployeeDisability");

var _getEmployeeDisability2 = _interopRequireDefault(_getEmployeeDisability);

var _pensionerPensionDiscontinuation = require("./pensionerPensionDiscontinuation");

var _pensionerPensionDiscontinuation2 = _interopRequireDefault(_pensionerPensionDiscontinuation);

var _searchClosedApplication = require("./searchClosedApplication");

var _searchClosedApplication2 = _interopRequireDefault(_searchClosedApplication);

var _initiateReComputation = require("./initiateReComputation");

var _initiateReComputation2 = _interopRequireDefault(_initiateReComputation);

var _searchApplication = require("./searchApplication");

var _searchApplication2 = _interopRequireDefault(_searchApplication);

var _pushEmployeesToPensionNotificationRegister = require("./pushEmployeesToPensionNotificationRegister");

var _pushEmployeesToPensionNotificationRegister2 = _interopRequireDefault(_pushEmployeesToPensionNotificationRegister);

var _saveMigratedPensioner = require("./saveMigratedPensioner");

var _saveMigratedPensioner2 = _interopRequireDefault(_saveMigratedPensioner);

var _updatePensionRevisionBulk = require("./updatePensionRevisionBulk");

var _updatePensionRevisionBulk2 = _interopRequireDefault(_updatePensionRevisionBulk);

var _searchPensionDisbursement = require("./searchPensionDisbursement");

var _searchPensionDisbursement2 = _interopRequireDefault(_searchPensionDisbursement);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = function (_ref) {
  var config = _ref.config,
      db = _ref.db;

  var api = (0, _express.Router)();

  api.use("/pension-services/v1", (0, _saveEmployeeToPensionNotificationRegister2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchPensionNotificationRegister2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _processWorkflow2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchWorkflow2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchEmployeeFromHRMS2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _getWorkflowAccessibility2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _claimWorkflow2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _releaseWorkflow2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _convertDateToEpoch2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _getEpochForDate2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _saveEmployees2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchEmployee2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _closeWorkflow2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _calculateBenefit2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _checkDependentEligibilityForBenefit2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _getPensionEmployees2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchPensioner2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchPensionerForPensionRevision2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _createRevisedPension2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _updateRevisedPension2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _createMonthlyPensionRegister2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchWorkflowPaymentDetails2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _getPensionRevisions2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _closeWorkflowByUser2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchPensionRegister2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _pushManualRegisterToPensionNotificationRegister2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _saveEmployeeDisability2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _getEmployeeDisability2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _pensionerPensionDiscontinuation2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchClosedApplication2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _initiateReComputation2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchApplication2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _pushEmployeesToPensionNotificationRegister2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _saveMigratedPensioner2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _updatePensionRevisionBulk2.default)({ config: config, db: db }));
  api.use("/pension-services/v1", (0, _searchPensionDisbursement2.default)({ config: config, db: db }));

  // perhaps expose some API metadata at the root
  api.get("/", function (req, res) {
    res.json({ version: _package.version });
  });

  return api;
};
//# sourceMappingURL=index.js.map