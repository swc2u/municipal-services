"use strict";

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var kafka = require("kafka-node");


var Consumer = kafka.Consumer;
var client = new kafka.KafkaClient({
  kafkaHost: _envVariables2.default.KAFKA_BROKER_HOST
});

var consumer = new Consumer(client, [{ topic: "SMS", offset: 0 }], {
  autoCommit: false
});

consumer.on("message", function (message) {
  _logger2.default.debug(message.value);
});

consumer.on("error", function (err) {
  _logger2.default.error("Error:", err);
});

consumer.on("offsetOutOfRange", function (err) {
  _logger2.default.error("offsetOutOfRange:", err);
});
//# sourceMappingURL=consumer.js.map