"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _package = require("../../package.json");

var _express = require("express");

var _calculateBenefit = require("./calculateBenefit");

var _calculateBenefit2 = _interopRequireDefault(_calculateBenefit);

var _getDependentEligibilityForBenefit = require("./getDependentEligibilityForBenefit");

var _getDependentEligibilityForBenefit2 = _interopRequireDefault(_getDependentEligibilityForBenefit);

var _calculateRevisedPension = require("./calculateRevisedPension");

var _calculateRevisedPension2 = _interopRequireDefault(_calculateRevisedPension);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = function () {
  var api = (0, _express.Router)();

  api.use("/pension-calculator/v1", (0, _calculateBenefit2.default)());
  api.use("/pension-calculator/v1", (0, _getDependentEligibilityForBenefit2.default)());
  api.use("/pension-calculator/v1", (0, _calculateRevisedPension2.default)());
  // perhaps expose some API metadata at the root
  api.get("/", function (req, res) {
    res.json({ version: _package.version });
  });

  return api;
};
//# sourceMappingURL=index.js.map