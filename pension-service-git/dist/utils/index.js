"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.epochToDmy = exports.epochToYmd = exports.adjust530SubForMigration = exports.adjust530AddForDeathDate = exports.adjust530SubForYMD = exports.adjust530AddForDob = exports.adjust530AddForDeathRegistration = exports.adjust530AddForYMD = exports.adjust530 = exports.getEpochForDate = exports.convertDateToEpochForMigration = exports.convertDateToEpochForDeathDate = exports.convertDateToEpoch = exports.convertEpochToDate = exports.updatePensionRevisionBulk = exports.createMonthlyPensionRegister = exports.pushEmployeesToPensionNotificationRegister = exports.loginRequest = exports.calculateRevisedPension = exports.getDependentEligibilityForBenefit = exports.calculateBenefit = exports.getUserEventDetails = exports.createUserEventToRole = exports.createUserEventToUser = exports.getFileDetails = exports.getEmployeeDetails = exports.searchApplication = exports.searchClosedApplication = exports.getEmployeeDisability = exports.saveEmployeeToPensionNotificationRegister = exports.closeWorkflowByUser = exports.getPensionRevisions = exports.searchPensionerForPensionRevision = exports.searchPensioner = exports.getPensionEmployees = exports.searchPensionWorkflow = exports.releaseWorkFlow = exports.searchEmployee = exports.saveEmployees = exports.searchWorkflow = exports.createWorkFlow = exports.addQueryArg = exports.addIDGenId = exports.requestInfoToResponseInfo = exports.uuidv1 = undefined;

var _regenerator = require("babel-runtime/regenerator");

var _regenerator2 = _interopRequireDefault(_regenerator);

var _asyncToGenerator2 = require("babel-runtime/helpers/asyncToGenerator");

var _asyncToGenerator3 = _interopRequireDefault(_asyncToGenerator2);

var _axios = require("axios");

var _axios2 = _interopRequireDefault(_axios);

var _uniqBy = require("lodash/uniqBy");

var _uniqBy2 = _interopRequireDefault(_uniqBy);

var _uniq = require("lodash/uniq");

var _uniq2 = _interopRequireDefault(_uniq);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _findIndex = require("lodash/findIndex");

var _findIndex2 = _interopRequireDefault(_findIndex);

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _api = require("./api");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _linq = require("linq");

var _logger = require("../config/logger");

var _logger2 = _interopRequireDefault(_logger);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var uuidv1 = exports.uuidv1 = function uuidv1() {
  return require("uuid/v4")();
};

var requestInfoToResponseInfo = exports.requestInfoToResponseInfo = function requestInfoToResponseInfo(requestinfo, success) {
  var ResponseInfo = {
    apiId: "",
    ver: "",
    ts: 0,
    resMsgId: "",
    msgId: "",
    status: ""
  };
  ResponseInfo.apiId = requestinfo && requestinfo.apiId ? requestinfo.apiId : "";
  ResponseInfo.ver = requestinfo && requestinfo.ver ? requestinfo.ver : "";
  ResponseInfo.ts = requestinfo && requestinfo.ts ? requestinfo.ts : null;
  ResponseInfo.resMsgId = "uief87324";
  ResponseInfo.msgId = requestinfo && requestinfo.msgId ? requestinfo.msgId : "";
  ResponseInfo.status = success ? "successful" : "failed";

  return ResponseInfo;
};

var addIDGenId = exports.addIDGenId = function () {
  var _ref = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee(requestInfo, idRequests) {
    var requestBody, idGenResponse;
    return _regenerator2.default.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo,
              idRequests: idRequests
            };
            _context.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_IDGEN_HOST,
              endPoint: "" + _envVariables2.default.EGOV_IDGEN_CONTEXT_PATH + _envVariables2.default.EGOV_IDGEN_GENERATE_ENPOINT,
              requestBody: requestBody
            });

          case 3:
            idGenResponse = _context.sent;
            return _context.abrupt("return", (0, _get2.default)(idGenResponse, "idResponses[0].id"));

          case 5:
          case "end":
            return _context.stop();
        }
      }
    }, _callee, undefined);
  }));

  return function addIDGenId(_x, _x2) {
    return _ref.apply(this, arguments);
  };
}();

var addQueryArg = exports.addQueryArg = function addQueryArg(url) {
  var queries = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : [];

  if (url && url.includes("?")) {
    var urlParts = url.split("?");
    var path = urlParts[0];
    var queryParts = urlParts.length > 1 ? urlParts[1].split("&") : [];
    queries.forEach(function (query) {
      var key = query.key;
      var value = query.value;
      var newQuery = key + "=" + value;
      queryParts.push(newQuery);
    });
    var newUrl = path + "?" + queryParts.join("&");
    return newUrl;
  } else {
    return url;
  }
};

//Workflow Service
var createWorkFlow = exports.createWorkFlow = function () {
  var _ref2 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee2(body) {
    var processInstances, requestBody, workflowResponse;
    return _regenerator2.default.wrap(function _callee2$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            processInstances = body.ProcessInstances.map(function (processInstances) {
              return {
                tenantId: processInstances.tenantId,
                businessService: processInstances.businessService,
                businessId: processInstances.businessId, //applicationNumber
                action: processInstances.action,
                comment: processInstances.comment != null ? processInstances.comment : null,
                assignee: null,
                documents: processInstances.documents != null ? processInstances.documents : [],
                sla: 0,
                previousStatus: null,
                moduleName: processInstances.businessService
              };
            });
            requestBody = {
              RequestInfo: body.RequestInfo,
              ProcessInstances: processInstances
            };

            _logger2.default.debug("requestBody", JSON.stringify(requestBody));

            _context2.next = 5;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_WORKFLOW_HOST,
              endPoint: _envVariables2.default.EGOV_WORKFLOW_TRANSITION_ENDPOINT,
              requestBody: requestBody
            });

          case 5:
            workflowResponse = _context2.sent;
            return _context2.abrupt("return", workflowResponse);

          case 7:
          case "end":
            return _context2.stop();
        }
      }
    }, _callee2, undefined);
  }));

  return function createWorkFlow(_x4) {
    return _ref2.apply(this, arguments);
  };
}();

var searchWorkflow = exports.searchWorkflow = function () {
  var _ref3 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee3(requestInfo, tenantId, businessIds) {
    var requestBody, workflowResponse;
    return _regenerator2.default.wrap(function _callee3$(_context3) {
      while (1) {
        switch (_context3.prev = _context3.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            _context3.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_WORKFLOW_HOST,
              endPoint: _envVariables2.default.EGOV_WORKFLOW_SEARCH_ENDPOINT + "?tenantId=" + tenantId + "&businessIds=" + businessIds + "&limit=" + _envVariables2.default.EGOV_WORKFLOW_DEFAULT_LIMIT,
              requestBody: requestBody
            });

          case 3:
            workflowResponse = _context3.sent;
            return _context3.abrupt("return", workflowResponse);

          case 5:
          case "end":
            return _context3.stop();
        }
      }
    }, _callee3, undefined);
  }));

  return function searchWorkflow(_x5, _x6, _x7) {
    return _ref3.apply(this, arguments);
  };
}();

//Pension Service
var saveEmployees = exports.saveEmployees = function () {
  var _ref4 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee4(body) {
    var requestBody, pensionResponse;
    return _regenerator2.default.wrap(function _callee4$(_context4) {
      while (1) {
        switch (_context4.prev = _context4.next) {
          case 0:
            requestBody = body;
            _context4.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_SAVE_EMPLOYEES_ENDPOINT,
              requestBody: requestBody
            });

          case 3:
            pensionResponse = _context4.sent;
            return _context4.abrupt("return", pensionResponse);

          case 5:
          case "end":
            return _context4.stop();
        }
      }
    }, _callee4, undefined);
  }));

  return function saveEmployees(_x8) {
    return _ref4.apply(this, arguments);
  };
}();

var searchEmployee = exports.searchEmployee = function () {
  var _ref5 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee5(requestInfo, tenantId, code) {
    var requestBody, employeeResponse;
    return _regenerator2.default.wrap(function _callee5$(_context5) {
      while (1) {
        switch (_context5.prev = _context5.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            _context5.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_SEARCH_EMPLOYEE_ENDPOINT + "?tenantId=" + tenantId + "&code=" + code,
              requestBody: requestBody
            });

          case 3:
            employeeResponse = _context5.sent;
            return _context5.abrupt("return", employeeResponse);

          case 5:
          case "end":
            return _context5.stop();
        }
      }
    }, _callee5, undefined);
  }));

  return function searchEmployee(_x9, _x10, _x11) {
    return _ref5.apply(this, arguments);
  };
}();

var releaseWorkFlow = exports.releaseWorkFlow = function () {
  var _ref6 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee6(body) {
    var processInstances, requestBody, releaseWorkFlowResponse;
    return _regenerator2.default.wrap(function _callee6$(_context6) {
      while (1) {
        switch (_context6.prev = _context6.next) {
          case 0:
            processInstances = body.ProcessInstances.map(function (processInstances) {
              return {
                businessId: processInstances.businessId //applicationNumber      
              };
            });
            requestBody = {
              RequestInfo: body.RequestInfo,
              ProcessInstances: processInstances
            };
            _context6.next = 4;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_RELEASE_WORKFLOW_ENDPOINT,
              requestBody: requestBody
            });

          case 4:
            releaseWorkFlowResponse = _context6.sent;
            return _context6.abrupt("return", releaseWorkFlowResponse);

          case 6:
          case "end":
            return _context6.stop();
        }
      }
    }, _callee6, undefined);
  }));

  return function releaseWorkFlow(_x12) {
    return _ref6.apply(this, arguments);
  };
}();

var searchPensionWorkflow = exports.searchPensionWorkflow = function () {
  var _ref7 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee7(requestInfo, tenantId, businessIds) {
    var requestBody, workflowResponse;
    return _regenerator2.default.wrap(function _callee7$(_context7) {
      while (1) {
        switch (_context7.prev = _context7.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            _context7.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_SEARCH_WORKFLOW_ENDPOINT + "?tenantId=" + tenantId + "&businessIds=" + businessIds,
              requestBody: requestBody
            });

          case 3:
            workflowResponse = _context7.sent;
            return _context7.abrupt("return", workflowResponse);

          case 5:
          case "end":
            return _context7.stop();
        }
      }
    }, _callee7, undefined);
  }));

  return function searchPensionWorkflow(_x13, _x14, _x15) {
    return _ref7.apply(this, arguments);
  };
}();

var getPensionEmployees = exports.getPensionEmployees = function () {
  var _ref8 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee8(requestInfo, tenantId, code, name, dob) {
    var requestBody, queryObj, employeeResponse;
    return _regenerator2.default.wrap(function _callee8$(_context8) {
      while (1) {
        switch (_context8.prev = _context8.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            queryObj = "tenantId=" + tenantId;

            if (code) {
              queryObj = queryObj + "&code=" + code;
            }
            if (name) {
              queryObj = queryObj + "&name=" + name;
            }
            if (dob) {
              queryObj = queryObj + "&dob=" + dob;
            }
            _context8.next = 7;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_GET_PENSION_EMPLOYEES_ENDPOINT + "?" + queryObj,
              requestBody: requestBody
            });

          case 7:
            employeeResponse = _context8.sent;
            return _context8.abrupt("return", employeeResponse);

          case 9:
          case "end":
            return _context8.stop();
        }
      }
    }, _callee8, undefined);
  }));

  return function getPensionEmployees(_x16, _x17, _x18, _x19, _x20) {
    return _ref8.apply(this, arguments);
  };
}();

var searchPensioner = exports.searchPensioner = function () {
  var _ref9 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee9(requestInfo, tenantId, pensionerNumber, name, dob) {
    var requestBody, queryObj, response;
    return _regenerator2.default.wrap(function _callee9$(_context9) {
      while (1) {
        switch (_context9.prev = _context9.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            queryObj = "";


            if (tenantId) {
              queryObj = queryObj + "tenantId=" + tenantId;
            }
            if (pensionerNumber) {
              queryObj = queryObj + "&pensionerNumber=" + pensionerNumber;
            }
            if (name) {
              queryObj = queryObj + "&name=" + name;
            }
            if (dob) {
              queryObj = queryObj + "&dob=" + dob;
            }

            _context9.next = 8;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_SEARCH_PENSIONER_ENDPOINT + "?" + queryObj,
              requestBody: requestBody
            });

          case 8:
            response = _context9.sent;
            return _context9.abrupt("return", response);

          case 10:
          case "end":
            return _context9.stop();
        }
      }
    }, _callee9, undefined);
  }));

  return function searchPensioner(_x21, _x22, _x23, _x24, _x25) {
    return _ref9.apply(this, arguments);
  };
}();

var searchPensionerForPensionRevision = exports.searchPensionerForPensionRevision = function () {
  var _ref10 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee10(requestInfo, tenantId, pensionerNumber) {
    var requestBody, response;
    return _regenerator2.default.wrap(function _callee10$(_context10) {
      while (1) {
        switch (_context10.prev = _context10.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            _context10.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_SEARCH_PENSIONER_FOR_PENSION_REVISION_ENDPOINT + "?tenantId=" + tenantId + "&pensionerNumber=" + pensionerNumber,
              requestBody: requestBody
            });

          case 3:
            response = _context10.sent;
            return _context10.abrupt("return", response);

          case 5:
          case "end":
            return _context10.stop();
        }
      }
    }, _callee10, undefined);
  }));

  return function searchPensionerForPensionRevision(_x26, _x27, _x28) {
    return _ref10.apply(this, arguments);
  };
}();

var getPensionRevisions = exports.getPensionRevisions = function () {
  var _ref11 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee11(requestInfo, tenantId, pensionerNumber) {
    var requestBody, response;
    return _regenerator2.default.wrap(function _callee11$(_context11) {
      while (1) {
        switch (_context11.prev = _context11.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            _context11.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_GET_PENSION_REVISIONS_ENDPOINT + "?tenantId=" + tenantId + "&pensionerNumber=" + pensionerNumber,
              requestBody: requestBody
            });

          case 3:
            response = _context11.sent;
            return _context11.abrupt("return", response);

          case 5:
          case "end":
            return _context11.stop();
        }
      }
    }, _callee11, undefined);
  }));

  return function getPensionRevisions(_x29, _x30, _x31) {
    return _ref11.apply(this, arguments);
  };
}();

var closeWorkflowByUser = exports.closeWorkflowByUser = function () {
  var _ref12 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee12(body) {
    var processInstances, requestBody, pensionResponse;
    return _regenerator2.default.wrap(function _callee12$(_context12) {
      while (1) {
        switch (_context12.prev = _context12.next) {
          case 0:
            processInstances = body.ProcessInstances.map(function (processInstances) {
              return {
                tenantId: processInstances.tenantId,
                businessService: processInstances.businessService,
                businessId: processInstances.businessId,
                dateOfContingent: processInstances.employeeOtherDetails.dateOfContingent, //employeeOtherDetails object
                moduleName: processInstances.businessService,
                workflowHeaderId: "",
                pensionEmployeeId: "",
                pensionerId: "",
                employeeOtherDetailsAuditId: "",
                auditDetails: null
              };
            });
            requestBody = {
              RequestInfo: body.RequestInfo,
              ProcessInstances: processInstances
            };


            _logger2.default.debug(JSON.stringify(requestBody));

            _context12.next = 5;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_CLOSE_WORKFLOW_BY_USER_ENDPOINT,
              requestBody: requestBody
            });

          case 5:
            pensionResponse = _context12.sent;

            _logger2.default.debug(JSON.stringify(pensionResponse));
            return _context12.abrupt("return", pensionResponse);

          case 8:
          case "end":
            return _context12.stop();
        }
      }
    }, _callee12, undefined);
  }));

  return function closeWorkflowByUser(_x32) {
    return _ref12.apply(this, arguments);
  };
}();

var saveEmployeeToPensionNotificationRegister = exports.saveEmployeeToPensionNotificationRegister = function () {
  var _ref13 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee13(body) {
    var requestBody, pensionResponse;
    return _regenerator2.default.wrap(function _callee13$(_context13) {
      while (1) {
        switch (_context13.prev = _context13.next) {
          case 0:
            requestBody = body;
            _context13.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_SAVE_EMPLOYEE_TO_PENSION_NOTIFICATION_REGISTER_ENDPOINT,
              requestBody: requestBody
            });

          case 3:
            pensionResponse = _context13.sent;
            return _context13.abrupt("return", pensionResponse);

          case 5:
          case "end":
            return _context13.stop();
        }
      }
    }, _callee13, undefined);
  }));

  return function saveEmployeeToPensionNotificationRegister(_x33) {
    return _ref13.apply(this, arguments);
  };
}();

var getEmployeeDisability = exports.getEmployeeDisability = function () {
  var _ref14 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee14(requestInfo, tenantId, code) {
    var requestBody, response;
    return _regenerator2.default.wrap(function _callee14$(_context14) {
      while (1) {
        switch (_context14.prev = _context14.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            _context14.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_GET_EMPLOYEE_DISABILITY_ENDPOINT + "?tenantId=" + tenantId + "&code=" + code,
              requestBody: requestBody
            });

          case 3:
            response = _context14.sent;
            return _context14.abrupt("return", response);

          case 5:
          case "end":
            return _context14.stop();
        }
      }
    }, _callee14, undefined);
  }));

  return function getEmployeeDisability(_x34, _x35, _x36) {
    return _ref14.apply(this, arguments);
  };
}();

var searchClosedApplication = exports.searchClosedApplication = function () {
  var _ref15 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee15(requestInfo, tenantId, businessService, businessId) {
    var requestBody, queryObj, closedApplicationResponse;
    return _regenerator2.default.wrap(function _callee15$(_context15) {
      while (1) {
        switch (_context15.prev = _context15.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            queryObj = "tenantId=" + tenantId;


            if (businessService) {
              queryObj = queryObj + "&businessService=" + businessService;
            }
            if (businessId) {
              queryObj = queryObj + "&businessId=" + businessId;
            }

            _context15.next = 6;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_SEARCH_CLOSED_APPLICATION_ENDPOINT + "?" + queryObj,
              requestBody: requestBody
            });

          case 6:
            closedApplicationResponse = _context15.sent;
            return _context15.abrupt("return", closedApplicationResponse);

          case 8:
          case "end":
            return _context15.stop();
        }
      }
    }, _callee15, undefined);
  }));

  return function searchClosedApplication(_x37, _x38, _x39, _x40) {
    return _ref15.apply(this, arguments);
  };
}();

var searchApplication = exports.searchApplication = function () {
  var _ref16 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee16(requestInfo, tenantId, code, businessId, businessService) {
    var requestBody, queryObj, closedApplicationResponse;
    return _regenerator2.default.wrap(function _callee16$(_context16) {
      while (1) {
        switch (_context16.prev = _context16.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            queryObj = "";


            if (tenantId) {
              queryObj = queryObj + "tenantId=" + tenantId;
            }
            if (code) {
              queryObj = queryObj + "&code=" + code;
            }
            if (businessId) {
              queryObj = queryObj + "&businessId=" + businessId;
            }
            if (businessService) {
              queryObj = queryObj + "&businessService=" + businessService;
            }

            _context16.next = 8;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_SEARCH_APPLICATION_ENDPOINT + "?" + queryObj,
              requestBody: requestBody
            });

          case 8:
            closedApplicationResponse = _context16.sent;
            return _context16.abrupt("return", closedApplicationResponse);

          case 10:
          case "end":
            return _context16.stop();
        }
      }
    }, _callee16, undefined);
  }));

  return function searchApplication(_x41, _x42, _x43, _x44, _x45) {
    return _ref16.apply(this, arguments);
  };
}();

//HRMS Service
var getEmployeeDetails = exports.getEmployeeDetails = function () {
  var _ref17 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee17(requestInfo, tenantId) {
    var codes = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : "";
    var names = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : "";
    var departments = arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : "";
    var requestBody, query, employeeResponse;
    return _regenerator2.default.wrap(function _callee17$(_context17) {
      while (1) {
        switch (_context17.prev = _context17.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            query = "";

            if (codes != "") {
              query = query + "&codes=" + codes;
            }
            if (names != "") {
              query = query + "&names=" + names;
            }
            if (departments != "") {
              query = query + "&departments=" + departments;
            }
            _context17.next = 7;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_HRMS_HOST,
              endPoint: "" + _envVariables2.default.EGOV_HRMS_CONTEXT_PATH + _envVariables2.default.EGOV_HRMS_EMPLOYEE_SEARCH_ENDPOINT + "?limit=" + _envVariables2.default.EGOV_HRMS_DEFAULT_LIMIT + "&tenantId=" + tenantId + query,
              requestBody: requestBody
            });

          case 7:
            employeeResponse = _context17.sent;
            return _context17.abrupt("return", employeeResponse);

          case 9:
          case "end":
            return _context17.stop();
        }
      }
    }, _callee17, undefined);
  }));

  return function getEmployeeDetails(_x46, _x47) {
    return _ref17.apply(this, arguments);
  };
}();

//File Service
var getFileDetails = exports.getFileDetails = function () {
  var _ref18 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee18(tenantId, fileStoreIds, requestInfo) {
    var index, fileResponse;
    return _regenerator2.default.wrap(function _callee18$(_context18) {
      while (1) {
        switch (_context18.prev = _context18.next) {
          case 0:
            if (String(tenantId).includes(".")) {
              index = String(tenantId).indexOf(".");

              tenantId = String(tenantId).substring(0, index);
            }
            /*
            let headers = [];
            headers.push({
              "auth-token":requestInfo.authToken
              }    
            );
            */
            _context18.next = 3;
            return (0, _api.httpGetRequest)({
              hostURL: _envVariables2.default.EGOV_FILESTORE_HOST,
              endPoint: "" + _envVariables2.default.EGOV_FILESTORE_CONTEXT_PATH + _envVariables2.default.EGOV_FILESTORE_URL_ENDPOINT + "?tenantId=" + tenantId + "&fileStoreIds=" + fileStoreIds //,
              //headers
            });

          case 3:
            fileResponse = _context18.sent;
            return _context18.abrupt("return", fileResponse);

          case 5:
          case "end":
            return _context18.stop();
        }
      }
    }, _callee18, undefined);
  }));

  return function getFileDetails(_x51, _x52, _x53) {
    return _ref18.apply(this, arguments);
  };
}();

//User Event Service
var createUserEventToUser = exports.createUserEventToUser = function () {
  var _ref19 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee19(body, tenantId, eventName, eventDescription, uuid) {
    var today, fromDate, toDate, events, requestBody, eventResponse;
    return _regenerator2.default.wrap(function _callee19$(_context19) {
      while (1) {
        switch (_context19.prev = _context19.next) {
          case 0:
            today = new Date();
            fromDate = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + (today.getDate() + 1);
            toDate = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + (today.getDate() + 8);
            events = [];

            events.push({
              tenantId: tenantId,
              eventType: _envVariables2.default.EGOV_USER_EVENT_TYPE,
              description: eventDescription,
              name: eventName,
              source: _envVariables2.default.EGOV_USER_EVENT_NAME_SOURCE,
              actions: {
                actionUrls: []
              },
              eventDetails: {
                fromDate: convertDateToEpoch(fromDate, "dob"),
                toDate: convertDateToEpoch(toDate, "dob")
              },
              recepient: {
                toRoles: [],
                toUsers: [uuid]
              }
            });
            requestBody = {
              RequestInfo: body.RequestInfo,
              events: events
            };

            _logger2.default.debug("eventRequestBody", JSON.stringify(requestBody));

            _context19.next = 9;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_USER_EVENT_HOST,
              endPoint: "" + _envVariables2.default.EGOV_USER_EVENT_CONTEXT_PATH + _envVariables2.default.EGOV_USER_EVENT_CREATE_ENDPOINT,
              requestBody: requestBody
            });

          case 9:
            eventResponse = _context19.sent;
            return _context19.abrupt("return", eventResponse);

          case 11:
          case "end":
            return _context19.stop();
        }
      }
    }, _callee19, undefined);
  }));

  return function createUserEventToUser(_x54, _x55, _x56, _x57, _x58) {
    return _ref19.apply(this, arguments);
  };
}();

var createUserEventToRole = exports.createUserEventToRole = function () {
  var _ref20 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee20(body, tenantId, eventName, eventDescription, roleCode) {
    var today, fromDate, toDate, events, requestBody, eventResponse;
    return _regenerator2.default.wrap(function _callee20$(_context20) {
      while (1) {
        switch (_context20.prev = _context20.next) {
          case 0:
            today = new Date();
            fromDate = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + (today.getDate() + 1);
            toDate = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + (today.getDate() + 8);
            events = [];

            events.push({
              tenantId: tenantId,
              eventType: _envVariables2.default.EGOV_USER_EVENT_TYPE,
              description: eventDescription,
              name: eventName,
              source: _envVariables2.default.EGOV_USER_EVENT_NAME_SOURCE,
              actions: {
                actionUrls: []
              },
              eventDetails: {
                fromDate: convertDateToEpoch(fromDate, "dob"),
                toDate: convertDateToEpoch(toDate, "dob")
              },
              recepient: {
                toRoles: [roleCode],
                toUsers: []
              }
            });
            requestBody = {
              RequestInfo: body.RequestInfo,
              events: events
            };
            _context20.next = 8;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_USER_EVENT_HOST,
              endPoint: "" + _envVariables2.default.EGOV_USER_EVENT_CONTEXT_PATH + _envVariables2.default.EGOV_USER_EVENT_CREATE_ENDPOINT,
              requestBody: requestBody
            });

          case 8:
            eventResponse = _context20.sent;
            return _context20.abrupt("return", eventResponse);

          case 10:
          case "end":
            return _context20.stop();
        }
      }
    }, _callee20, undefined);
  }));

  return function createUserEventToRole(_x59, _x60, _x61, _x62, _x63) {
    return _ref20.apply(this, arguments);
  };
}();

var getUserEventDetails = exports.getUserEventDetails = function () {
  var _ref21 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee21(requestInfo, tenantId, recepients) {
    var requestBody, userEventResponse;
    return _regenerator2.default.wrap(function _callee21$(_context21) {
      while (1) {
        switch (_context21.prev = _context21.next) {
          case 0:
            requestBody = {
              RequestInfo: requestInfo
            };
            _context21.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_USER_EVENT_HOST,
              endPoint: "" + _envVariables2.default.EGOV_USER_EVENT_CONTEXT_PATH + _envVariables2.default.EGOV_USER_EVENT_SEARCH_ENDPOINT + "?tenantId=" + tenantId + "&recepients=" + recepients,
              requestBody: requestBody
            });

          case 3:
            userEventResponse = _context21.sent;
            return _context21.abrupt("return", userEventResponse);

          case 5:
          case "end":
            return _context21.stop();
        }
      }
    }, _callee21, undefined);
  }));

  return function getUserEventDetails(_x64, _x65, _x66) {
    return _ref21.apply(this, arguments);
  };
}();

//Pension Rule Engine
var calculateBenefit = exports.calculateBenefit = function () {
  var _ref22 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee22(body) {
    var processInstances, requestBody, ruleEngineResponse;
    return _regenerator2.default.wrap(function _callee22$(_context22) {
      while (1) {
        switch (_context22.prev = _context22.next) {
          case 0:
            processInstances = body.ProcessInstances.map(function (processInstances) {
              return {
                tenantId: processInstances.tenantId,
                employee: processInstances.employee
              };
            });
            requestBody = {
              RequestInfo: body.RequestInfo,
              ProcessInstances: processInstances
            };

            _logger2.default.debug("requestBody", JSON.stringify(requestBody));

            _context22.next = 5;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_RULE_ENGINE_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_RULE_ENGINE_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_RULE_ENGINE_CALCULATE_BENEFIT_ENDPOINT,
              requestBody: requestBody
            });

          case 5:
            ruleEngineResponse = _context22.sent;
            return _context22.abrupt("return", ruleEngineResponse);

          case 7:
          case "end":
            return _context22.stop();
        }
      }
    }, _callee22, undefined);
  }));

  return function calculateBenefit(_x67) {
    return _ref22.apply(this, arguments);
  };
}();

var getDependentEligibilityForBenefit = exports.getDependentEligibilityForBenefit = function () {
  var _ref23 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee23(body) {
    var processInstances, requestBody, ruleEngineResponse;
    return _regenerator2.default.wrap(function _callee23$(_context23) {
      while (1) {
        switch (_context23.prev = _context23.next) {
          case 0:
            processInstances = body.ProcessInstances.map(function (processInstances) {
              return {
                tenantId: processInstances.tenantId,
                dependents: processInstances.dependents
              };
            });
            requestBody = {
              RequestInfo: body.RequestInfo,
              ProcessInstances: processInstances
            };

            _logger2.default.debug("requestBody", JSON.stringify(requestBody));

            _context23.next = 5;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_RULE_ENGINE_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_RULE_ENGINE_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_RULE_ENGINE_GET_DEPENDENT_ELIGIBILITY_FOR_BENEFIT_ENDPOINT,
              requestBody: requestBody
            });

          case 5:
            ruleEngineResponse = _context23.sent;
            return _context23.abrupt("return", ruleEngineResponse);

          case 7:
          case "end":
            return _context23.stop();
        }
      }
    }, _callee23, undefined);
  }));

  return function getDependentEligibilityForBenefit(_x68) {
    return _ref23.apply(this, arguments);
  };
}();

var calculateRevisedPension = exports.calculateRevisedPension = function () {
  var _ref24 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee24(body) {
    var processInstances, requestBody, ruleEngineResponse;
    return _regenerator2.default.wrap(function _callee24$(_context24) {
      while (1) {
        switch (_context24.prev = _context24.next) {
          case 0:
            processInstances = body.ProcessInstances.map(function (processInstances) {
              return {
                tenantId: processInstances.tenantId,
                pensionRevision: processInstances.pensionRevision
              };
            });
            requestBody = {
              RequestInfo: body.RequestInfo,
              ProcessInstances: processInstances
            };

            _logger2.default.debug("requestBody", JSON.stringify(requestBody));

            _context24.next = 5;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_RULE_ENGINE_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_RULE_ENGINE_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_RULE_ENGINE_CALCULATE_REVISED_PENSION_ENDPOINT,
              requestBody: requestBody
            });

          case 5:
            ruleEngineResponse = _context24.sent;
            return _context24.abrupt("return", ruleEngineResponse);

          case 7:
          case "end":
            return _context24.stop();
        }
      }
    }, _callee24, undefined);
  }));

  return function calculateRevisedPension(_x69) {
    return _ref24.apply(this, arguments);
  };
}();

//Schedular
var loginRequest = exports.loginRequest = function () {
  var _ref25 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee25(username, password, grant_type, scope, tenantId, userType) {
    var loginInstance, apiError, params, response, responseStatus, _error$response, data, status;

    return _regenerator2.default.wrap(function _callee25$(_context25) {
      while (1) {
        switch (_context25.prev = _context25.next) {
          case 0:
            loginInstance = _axios2.default.create({
              baseURL: _envVariables2.default.EGOV_USER_HOST, //window.location.origin,
              headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                Authorization: "Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0"
              }
            });
            apiError = "Api Error";
            params = new URLSearchParams();

            params.append("username", username);
            params.append("password", password);
            params.append("grant_type", grant_type);
            params.append("scope", scope);
            params.append("tenantId", tenantId);
            params.append("userType", userType);

            _context25.prev = 9;
            _context25.next = 12;
            return loginInstance.post("" + _envVariables2.default.EGOV_USER_CONTEXT_PATH + _envVariables2.default.EGOV_USER_GENERATE_ACCESS_TOKEN_ENDPOINT, params);

          case 12:
            response = _context25.sent;
            responseStatus = parseInt(response.status, 10);

            if (!(responseStatus === 200 || responseStatus === 201)) {
              _context25.next = 16;
              break;
            }

            return _context25.abrupt("return", response.data);

          case 16:
            _context25.next = 22;
            break;

          case 18:
            _context25.prev = 18;
            _context25.t0 = _context25["catch"](9);
            _error$response = _context25.t0.response, data = _error$response.data, status = _error$response.status;

            if (status === 400) {
              apiError = data.hasOwnProperty("error_description") && data.error_description || apiError;
            }

          case 22:
            throw new Error(apiError);

          case 23:
          case "end":
            return _context25.stop();
        }
      }
    }, _callee25, undefined, [[9, 18]]);
  }));

  return function loginRequest(_x70, _x71, _x72, _x73, _x74, _x75) {
    return _ref25.apply(this, arguments);
  };
}();

var pushEmployeesToPensionNotificationRegister = exports.pushEmployeesToPensionNotificationRegister = function () {
  var _ref26 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee26(body) {
    var requestBody, response;
    return _regenerator2.default.wrap(function _callee26$(_context26) {
      while (1) {
        switch (_context26.prev = _context26.next) {
          case 0:
            requestBody = body;
            _context26.next = 3;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_PUSH_EMPLOYEES_TO_PENSION_NOTIFICATION_REGISTER_ENDPOINT,
              requestBody: requestBody
            });

          case 3:
            response = _context26.sent;
            return _context26.abrupt("return", response);

          case 5:
          case "end":
            return _context26.stop();
        }
      }
    }, _callee26, undefined);
  }));

  return function pushEmployeesToPensionNotificationRegister(_x76) {
    return _ref26.apply(this, arguments);
  };
}();

var createMonthlyPensionRegister = exports.createMonthlyPensionRegister = function () {
  var _ref27 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee27(body) {
    var processInstances, requestBody, response;
    return _regenerator2.default.wrap(function _callee27$(_context27) {
      while (1) {
        switch (_context27.prev = _context27.next) {
          case 0:
            processInstances = [];

            processInstances.push({
              tenantId: _envVariables2.default.EGOV_PENSION_SCHEDULAR_TENANTID,
              effectiveYear: new Date().getFullYear(),
              effectiveMonth: new Date().getMonth() + 1
            });
            requestBody = {
              RequestInfo: body.RequestInfo,
              ProcessInstances: processInstances
            };
            _context27.next = 5;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_CREATE_MONTHLY_PENSION_REGISTER_ENDPOINT,
              requestBody: requestBody
            });

          case 5:
            response = _context27.sent;
            return _context27.abrupt("return", response);

          case 7:
          case "end":
            return _context27.stop();
        }
      }
    }, _callee27, undefined);
  }));

  return function createMonthlyPensionRegister(_x77) {
    return _ref27.apply(this, arguments);
  };
}();

var updatePensionRevisionBulk = exports.updatePensionRevisionBulk = function () {
  var _ref28 = (0, _asyncToGenerator3.default)( /*#__PURE__*/_regenerator2.default.mark(function _callee28(body) {
    var parameters, requestBody, response;
    return _regenerator2.default.wrap(function _callee28$(_context28) {
      while (1) {
        switch (_context28.prev = _context28.next) {
          case 0:
            parameters = {
              tenantId: _envVariables2.default.EGOV_PENSION_SCHEDULAR_TENANTID,
              effectiveYear: new Date().getFullYear(),
              effectiveMonth: new Date().getMonth() + 1,
              modifyDA: _envVariables2.default.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_IS_DA_MODIFIABLE,
              modifyIR: _envVariables2.default.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_IS_IR_MODIFIABLE,
              modifyFMA: _envVariables2.default.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_IS_FMA_MODIFIABLE,
              FMA: Number(_envVariables2.default.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_MODIFIED_FMA)
            };
            requestBody = {
              RequestInfo: body.RequestInfo,
              Parameters: parameters
            };
            _context28.next = 4;
            return (0, _api.httpRequest)({
              hostURL: _envVariables2.default.EGOV_PENSION_HOST,
              endPoint: "" + _envVariables2.default.EGOV_PENSION_CONTEXT_PATH + _envVariables2.default.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_ENDPOINT,
              requestBody: requestBody
            });

          case 4:
            response = _context28.sent;
            return _context28.abrupt("return", response);

          case 6:
          case "end":
            return _context28.stop();
        }
      }
    }, _callee28, undefined);
  }));

  return function updatePensionRevisionBulk(_x78) {
    return _ref28.apply(this, arguments);
  };
}();

var convertEpochToDate = exports.convertEpochToDate = function convertEpochToDate(dateEpoch) {
  var dateFromApi = new Date(dateEpoch);
  var month = dateFromApi.getMonth() + 1;
  var day = dateFromApi.getDate();
  var year = dateFromApi.getFullYear();
  month = (month > 9 ? "" : "0") + month;
  day = (day > 9 ? "" : "0") + day;
  return day + "/" + month + "/" + year;
};

var convertDateToEpoch = exports.convertDateToEpoch = function convertDateToEpoch(dateString) {
  var dayStartOrEnd = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : "dayend";

  //example input format : "2018-10-02"
  try {

    var parts = dateString.match(/(\d{4})-(\d{1,2})-(\d{1,2})/);
    var DateObj = new Date(Date.UTC(parts[1], parts[2] - 1, parts[3]));
    DateObj.setMinutes(DateObj.getMinutes() + DateObj.getTimezoneOffset());

    if (dayStartOrEnd === "dayend") {
      DateObj.setHours(DateObj.getHours() + 24);
      DateObj.setSeconds(DateObj.getSeconds() - 1);
    }
    var et = DateObj.getTime();

    //let etAdjusted = adjust530SubForYMD(et);

    return et;
  } catch (e) {
    return dateString;
  }
};

var convertDateToEpochForDeathDate = exports.convertDateToEpochForDeathDate = function convertDateToEpochForDeathDate(dateString) {
  var dayStartOrEnd = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : "dayend";

  //example input format : "2018-10-02"
  try {

    var parts = dateString.match(/(\d{4})-(\d{1,2})-(\d{1,2})/);
    var DateObj = new Date(Date.UTC(parts[1], parts[2] - 1, parts[3]));
    DateObj.setMinutes(DateObj.getMinutes() + DateObj.getTimezoneOffset());

    if (dayStartOrEnd === "dayend") {
      DateObj.setHours(DateObj.getHours() + 24);
      DateObj.setSeconds(DateObj.getSeconds() - 1);
    }
    var et = DateObj.getTime();

    var etAdjustedForDeathDate = adjust530AddForDeathDate(et);

    return etAdjustedForDeathDate;
  } catch (e) {
    return dateString;
  }
};

var convertDateToEpochForMigration = exports.convertDateToEpochForMigration = function convertDateToEpochForMigration(dateString) {
  var dayStartOrEnd = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : "dayend";

  //example input format : "2018-10-02"
  try {

    var parts = dateString.match(/(\d{4})-(\d{1,2})-(\d{1,2})/);
    var DateObj = new Date(Date.UTC(parts[1], parts[2] - 1, parts[3]));
    DateObj.setMinutes(DateObj.getMinutes() + DateObj.getTimezoneOffset());

    if (dayStartOrEnd === "dayend") {
      DateObj.setHours(DateObj.getHours() + 24);
      DateObj.setSeconds(DateObj.getSeconds() - 1);
    }
    var et = DateObj.getTime();

    var etAdjusted = adjust530SubForMigration(et);

    return etAdjusted;
  } catch (e) {
    return dateString;
  }
};

var getEpochForDate = exports.getEpochForDate = function getEpochForDate(date) {
  var dateSplit = date.split("/");
  return new Date(dateSplit[2], dateSplit[1] - 1, dateSplit[0]).getTime();
};

var adjust530 = exports.adjust530 = function adjust530(actualDateNum) {
  /* let modifiedDate = new Date(Number(actualDateNum));
  if(modifiedDate.getHours()>0){
  modifiedDate.setHours(modifiedDate.getHours() + 5);
  modifiedDate.setMinutes(modifiedDate.getMinutes() + 30);
  }
  let modifiedDateNum = Number(modifiedDate);
  return modifiedDateNum; */
  return actualDateNum;
};

var adjust530AddForYMD = exports.adjust530AddForYMD = function adjust530AddForYMD(actualDateNum) {
  /* let modifiedDate = new Date(Number(actualDateNum));
  if(modifiedDate.getHours()>0){
  modifiedDate.setHours(modifiedDate.getHours() + 5);
  modifiedDate.setMinutes(modifiedDate.getMinutes() + 30);
  }
  let modifiedDateNum = Number(modifiedDate);
  return modifiedDateNum;  */
  return actualDateNum;
};

var adjust530AddForDeathRegistration = exports.adjust530AddForDeathRegistration = function adjust530AddForDeathRegistration(actualDateNum) {
  var modifiedDate = new Date(Number(actualDateNum));
  if (modifiedDate.getHours() > 0) {
    modifiedDate.setHours(modifiedDate.getHours() + 5);
    modifiedDate.setMinutes(modifiedDate.getMinutes() + 30);
  }
  var modifiedDateNum = Number(modifiedDate);
  return modifiedDateNum;
  //return actualDateNum;
};

var adjust530AddForDob = exports.adjust530AddForDob = function adjust530AddForDob(actualDateNum) {
  var modifiedDate = new Date(Number(actualDateNum));
  if (modifiedDate.getHours() > 0) {
    modifiedDate.setHours(modifiedDate.getHours() + 5);
    modifiedDate.setMinutes(modifiedDate.getMinutes() + 30);
  }
  var modifiedDateNum = Number(modifiedDate);
  return modifiedDateNum;
  //return actualDateNum;
};

var adjust530SubForYMD = exports.adjust530SubForYMD = function adjust530SubForYMD(actualDateNum) {
  /* let modifiedDate = new Date(Number(actualDateNum));
  if(modifiedDate.getHours()==0){
  modifiedDate.setHours(modifiedDate.getHours() - 5);
  modifiedDate.setMinutes(modifiedDate.getMinutes() - 30);
  }
  let modifiedDateNum = Number(modifiedDate);
  return modifiedDateNum;  */
  return actualDateNum;
};

var adjust530AddForDeathDate = exports.adjust530AddForDeathDate = function adjust530AddForDeathDate(actualDateNum) {
  /* let modifiedDate = new Date(Number(actualDateNum));
  if(modifiedDate.getHours()==0){
  modifiedDate.setHours(modifiedDate.getHours() + 18);
  modifiedDate.setMinutes(modifiedDate.getMinutes() + 30);
  }
  let modifiedDateNum = Number(modifiedDate);
  return modifiedDateNum;  */
  return actualDateNum;
};

var adjust530SubForMigration = exports.adjust530SubForMigration = function adjust530SubForMigration(actualDateNum) {
  /* let modifiedDate = new Date(Number(actualDateNum));
  if(modifiedDate.getHours()==0){
  modifiedDate.setHours(modifiedDate.getHours() - 5);
  modifiedDate.setMinutes(modifiedDate.getMinutes() - 30);
  }
  //let modifiedDateNum = Number(modifiedDate); */
  //return modifiedDateNum;  
  return actualDateNum;
};

var epochToYmd = exports.epochToYmd = function epochToYmd(et) {
  // Return null if et already null
  if (!et) return null;
  // Return the same format if et is already a string (boundary case)
  if (typeof et === "string") return et;

  var etAdjusted = adjust530AddForYMD(et);
  var date = new Date(etAdjusted);
  //let hours = date.getHours();
  //if(hours>0){
  //date = addDays(date, 1);
  //}
  var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
  // date = `${date.getFullYear()}-${month}-${day}`;
  var formatted_date = date.getFullYear() + "-" + month + "-" + day;
  return formatted_date;
};

var epochToDmy = exports.epochToDmy = function epochToDmy(et) {
  // Return null if et already null
  if (!et) return null;
  // Return the same format if et is already a string (boundary case)
  if (typeof et === "string") return et;
  var date = new Date(et);
  var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
  // date = `${date.getFullYear()}-${month}-${day}`;
  var formatted_date = day + "/" + month + "/" + date.getFullYear();
  return formatted_date;
};
//# sourceMappingURL=index.js.map