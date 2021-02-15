"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var kafka = require("kafka-node");


var Producer = kafka.Producer;
var client = void 0;

if (process.env.NODE_ENV === "development") {
  client = new kafka.Client();
  _logger2.default.debug("local - ");
} else {
  client = new kafka.KafkaClient({ kafkaHost: _envVariables2.default.KAFKA_BROKER_HOST });
  _logger2.default.debug("cloud - ");
}

var producer = new Producer(client);

producer.on("ready", function () {
  _logger2.default.debug("Producer is ready");
});

producer.on("error", function (err) {
  _logger2.default.error("Producer is in error state");
  _logger2.default.error(err);
});

exports.default = producer;
//# sourceMappingURL=producer.js.map