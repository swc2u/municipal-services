"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _axios = require("axios");

var _axios2 = _interopRequireDefault(_axios);

var _logger = require("./logger");

var _logger2 = _interopRequireDefault(_logger);

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var createAxiosInstance = function createAxiosInstance(hostURL) {
  var instance = _axios2.default.create({
    baseURL: hostURL,
    headers: {
      "Content-Type": "application/json"
    }
  });

  // add interceptor to log before request is made
  instance.interceptors.request.use(function (config) {
    logRequest(config);
    return config;
  }, function (error) {
    logErrorResponse(error);
    // Do something with request error
    return Promise.reject(error);
  });

  //add interceptor to log after response is received
  instance.interceptors.response.use(function (response) {
    // Do something with response data
    logResponse(response);
    return response;
  }, function (error) {
    logErrorResponse(error);
    // Do something with response error
    return Promise.reject(error);
  });
  return instance;
};
function logRequest(config) {
  var url = config.url,
      method = config.method,
      data = config.data;

  if (_envVariables2.default.HTTP_CLIENT_DETAILED_LOGGING_ENABLED) {
    _logger2.default.info("Sending request to " + url + " with verb " + method + " with body " + JSON.stringify(data));
  } else {
    _logger2.default.info("Sending request to " + url + " with verb " + method);
  }
}

function logResponse(res) {
  var status = res.status,
      headers = res.headers,
      data = res.data,
      config = res.config;

  if (_envVariables2.default.HTTP_CLIENT_DETAILED_LOGGING_ENABLED && headers["content-type"] && headers["content-type"].includes("application/json")) {
    _logger2.default.info("Received from " + config.url + " response code " + status + " and body " + JSON.stringify(data) + ": ");
  } else {
    _logger2.default.info("Received response from " + config.url);
  }
}

function logErrorResponse(error) {
  // if (error.response) {
  //     // The request was made and the server responded with a status code
  //     // that falls out of the range of 2xx
  //     logger.debug(error.response.data);
  //     logger.debug(error.responsinstance;
  // );
  //     logger.debug(error.response.headers);
  //   } else if (error.request) {
  //     // The request was made but no response was received
  //     // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
  //     // http.ClientRequest in node.js
  //     logger.debug(error.request);
  //   } else {
  //     // Something happened in setting up the request that triggered an Error
  //     logger.debug('Error', error.message);
  //   }
  //   logger.debug(error.config);
  _logger2.default.debug(error);
}

exports.default = createAxiosInstance;
//# sourceMappingURL=httpClient.js.map