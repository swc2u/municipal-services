"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _defineProperty2 = require("babel-runtime/helpers/defineProperty");

var _defineProperty3 = _interopRequireDefault(_defineProperty2);

var _envVariables;

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var envVariables = (_envVariables = {
  // DB configurations  
  DB_USER: process.env.DB_USER || "postgres",
  DB_PASSWORD: process.env.DB_PASSWORD || "postgres",
  DB_HOST: process.env.DB_HOST || "127.0.0.1",
  DB_NAME: process.env.DB_NAME || "devdb",
  DB_SSL: process.env.DB_SSL || false,
  DB_PORT: process.env.DB_PORT || 5435,
  DB_MAX_POOL_SIZE: process.env.DB_MAX_POOL_SIZE || "5",

  //server configurations
  SERVER_PORT: process.env.SERVER_PORT || "8086",

  //kafka configurations
  KAFKA_BROKER_HOST: process.env.KAFKA_BROKER_HOST || "localhost:9092",

  //pension related configurations
  KAFKA_TOPICS_SAVE_EMPLOYEE_TO_PENSION_NOTIFICATION_REGISTER: process.env.KAFKA_TOPICS_SAVE_EMPLOYEE_TO_PENSION_NOTIFICATION_REGISTER || "SAVE-EMPLOYEE-TO-PENSION-NOTIFICATION-REGISTER",
  KAFKA_TOPICS_RRP_INITIATE: process.env.KAFKA_TOPICS_RRP_INITIATE || "RRP_INITIATE",
  KAFKA_TOPICS_SAVE_RRP_INITIATED: process.env.KAFKA_TOPICS_SAVE_RRP_INITIATED || "SAVE_RRP_INITIATED",
  KAFKA_TOPICS_SAVE_PENDING_FOR_DETAILS_VERIFICATION: process.env.KAFKA_TOPICS_SAVE_PENDING_FOR_DETAILS_VERIFICATION || "SAVE_PENDING_FOR_DETAILS_VERIFICATION",
  KAFKA_TOPICS_SAVE_PENDING_FOR_DETAILS_REVIEW: process.env.KAFKA_TOPICS_SAVE_PENDING_FOR_DETAILS_REVIEW || "SAVE_PENDING_FOR_DETAILS_REVIEW",
  KAFKA_TOPICS_SAVE_PENDING_FOR_CALCULATION: process.env.KAFKA_TOPICS_SAVE_PENDING_FOR_CALCULATION || "SAVE_PENDING_FOR_CALCULATION",
  KAFKA_TOPICS_SAVE_PENDING_FOR_CALCULATION_REVIEW: process.env.KAFKA_TOPICS_SAVE_PENDING_FOR_CALCULATION_REVIEW || "SAVE_PENDING_FOR_CALCULATION_REVIEW",
  KAFKA_TOPICS_SAVE_RRP_PENDING_FOR_APPROVAL: process.env.KAFKA_TOPICS_SAVE_RRP_PENDING_FOR_APPROVAL || "SAVE_RRP_PENDING_FOR_APPROVAL",
  KAFKA_TOPICS_SAVE_RRP_PENDING_FOR_AUDIT: process.env.KAFKA_TOPICS_SAVE_RRP_PENDING_FOR_AUDIT || "SAVE_RRP_PENDING_FOR_AUDIT",
  KAFKA_TOPICS_SAVE_CLOSED: process.env.KAFKA_TOPICS_SAVE_CLOSED || "SAVE_CLOSED",
  KAFKA_TOPICS_CLAIM_WORKFLOW: process.env.KAFKA_TOPICS_CLAIM_WORKFLOW || "CLAIM_WORKFLOW",
  KAFKA_TOPICS_RELEASE_WORKFLOW: process.env.KAFKA_TOPICS_RELEASE_WORKFLOW || "RELEASE_WORKFLOW",
  KAFKA_TOPICS_SAVE_EMPLOYEES: process.env.KAFKA_TOPICS_SAVE_EMPLOYEES || "SAVE_EMPLOYEES",
  KAFKA_TOPICS_DOE_INITIATE: process.env.KAFKA_TOPICS_DOE_INITIATE || "DOE_INITIATE",
  KAFKA_TOPICS_SAVE_DEATH_INITIATED: process.env.KAFKA_TOPICS_SAVE_DEATH_INITIATED || "SAVE_DEATH_INITIATED",
  KAFKA_TOPICS_DOP_INITIATE: process.env.KAFKA_TOPICS_DOP_INITIATE || "DOP_INITIATE",
  KAFKA_TOPICS_CREATE_REVISED_PENSION: process.env.KAFKA_TOPICS_CREATE_REVISED_PENSION || "CREATE_REVISED_PENSION",
  KAFKA_TOPICS_UPDATE_REVISED_PENSION: process.env.KAFKA_TOPICS_UPDATE_REVISED_PENSION || "UPDATE_REVISED_PENSION",
  KAFKA_TOPICS_CREATE_MONTHLY_PENSION_REGISTER: process.env.KAFKA_TOPICS_CREATE_MONTHLY_PENSION_REGISTER || "CREATE_MONTHLY_PENSION_REGISTER",
  KAFKA_TOPICS_SAVE_DISABILITY_DETAILS: process.env.KAFKA_TOPICS_SAVE_DISABILITY_DETAILS || "SAVE_DISABILITY_DETAILS",
  KAFKA_TOPICS_PENSIONER_PENSION_DISCONTINUATION: process.env.KAFKA_TOPICS_PENSIONER_PENSION_DISCONTINUATION || "PENSIONER_PENSION_DISCONTINUATION",
  KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE: process.env.KAFKA_TOPICS_UPDATE_PENSION_WORKFLOW_STATE || "UPDATE_PENSION_WORKFLOW_STATE",
  KAFKA_TOPICS_INITIATE_RECOMPUTATION: process.env.KAFKA_TOPICS_INITIATE_RECOMPUTATION || "INITIATE_RECOMPUTATION",
  KAFKA_TOPICS_SAVE_MIGRATED_PENSIONER: process.env.KAFKA_TOPICS_SAVE_MIGRATED_PENSIONER || "SAVE_MIGRATED_PENSIONER",
  KAFKA_TOPICS_CLEAR_BENEFIT: process.env.KAFKA_TOPICS_CLEAR_BENEFIT || "CLEAR_BENEFIT",
  KAFKA_TOPICS_CREATE_REVISED_PENSION_BULK: process.env.KAFKA_TOPICS_CREATE_REVISED_PENSION_BULK || "CREATE_REVISED_PENSION_BULK",

  EGOV_PENSION_RRP_BUSINESS_SERVICE: process.env.EGOV_PENSION_RRP_BUSINESS_SERVICE || "RRP_SERVICE", //Regular Retirement Pension
  EGOV_PENSION_DOE_BUSINESS_SERVICE: process.env.EGOV_PENSION_DOE_BUSINESS_SERVICE || "DOE_SERVICE", //Death of an employee  
  EGOV_PENSION_DOP_BUSINESS_SERVICE: process.env.EGOV_PENSION_DOP_BUSINESS_SERVICE || "DOP_SERVICE", //Death of a pensioner  

  EGOV_PENSION_WORKFLOW_ACTION_INITIATE: process.env.EGOV_PENSION_WORKFLOW_ACTION_INITIATE || "INITIATE",
  EGOV_PENSION_WORKFLOW_ACTION_FORWARD: process.env.EGOV_PENSION_WORKFLOW_ACTION_FORWARD || "FORWARD",
  EGOV_PENSION_WORKFLOW_ACTION_SEND_BACK: process.env.EGOV_PENSION_WORKFLOW_ACTION_SEND_BACK || "SEND_BACK",
  EGOV_PENSION_WORKFLOW_ACTION_CLOSE: process.env.EGOV_PENSION_WORKFLOW_ACTION_CLOSE || "CLOSE",

  //pension service configurations
  EGOV_PENSION_HOST: process.env.EGOV_PENSION_HOST || "http://localhost:8086",
  EGOV_PENSION_CONTEXT_PATH: process.env.EGOV_PENSION_CONTEXT_PATH || "/pension-services",
  EGOV_PENSION_SAVE_EMPLOYEES_ENDPOINT: process.env.EGOV_PENSION_SAVE_EMPLOYEES_ENDPOINT || "/v1/_saveEmployees",
  EGOV_PENSION_SEARCH_EMPLOYEE_ENDPOINT: process.env.EGOV_PENSION_SEARCH_EMPLOYEE_ENDPOINT || "/v1/_searchEmployee",
  EGOV_PENSION_RELEASE_WORKFLOW_ENDPOINT: process.env.EGOV_PENSION_RELEASE_WORKFLOW_ENDPOINT || "/v1/_releaseWorkflow",
  EGOV_PENSION_SEARCH_WORKFLOW_ENDPOINT: process.env.EGOV_PENSION_SEARCH_WORKFLOW_ENDPOINT || "/v1/_searchWorkflow",
  EGOV_PENSION_GET_PENSION_EMPLOYEES_ENDPOINT: process.env.EGOV_PENSION_GET_PENSION_EMPLOYEES_ENDPOINT || "/v1/_getPensionEmployees",
  EGOV_PENSION_SEARCH_PENSIONER_FOR_PENSION_REVISION_ENDPOINT: process.env.EGOV_PENSION_SEARCH_PENSIONER_FOR_PENSION_REVISION_ENDPOINT || "/v1/_searchPensionerForPensionRevision",
  EGOV_PENSION_GET_PENSION_REVISIONS_ENDPOINT: process.env.EGOV_PENSION_GET_PENSION_REVISIONS_ENDPOINT || "/v1/_getPensionRevisions",
  EGOV_PENSION_CLOSE_WORKFLOW_BY_USER_ENDPOINT: process.env.EGOV_PENSION_CLOSE_WORKFLOW_BY_USER_ENDPOINT || "/v1/_closeWorkflowByUser",
  EGOV_PENSION_SAVE_EMPLOYEE_TO_PENSION_NOTIFICATION_REGISTER_ENDPOINT: process.env.EGOV_PENSION_SAVE_EMPLOYEE_TO_PENSION_NOTIFICATION_REGISTER_ENDPOINT || "/v1/_saveEmployeeToPensionNotificationRegister",
  EGOV_PENSION_GET_EMPLOYEE_DISABILITY_ENDPOINT: process.env.EGOV_PENSION_GET_EMPLOYEE_DISABILITY_ENDPOINT || "/v1/_getEmployeeDisability",
  EGOV_PENSION_SEARCH_CLOSED_APPLICATION_ENDPOINT: process.env.EGOV_PENSION_SEARCH_CLOSED_APPLICATION_ENDPOINT || "/v1/_searchClosedApplication",
  EGOV_PENSION_SEARCH_APPLICATION_ENDPOINT: process.env.EGOV_PENSION_SEARCH_APPLICATION_ENDPOINT || "/v1/_searchApplication",
  EGOV_PENSION_SEARCH_PENSIONER_ENDPOINT: process.env.EGOV_PENSION_SEARCH_PENSIONER_ENDPOINT || "/v1/_searchPensioner",
  EGOV_PENSION_PUSH_EMPLOYEES_TO_PENSION_NOTIFICATION_REGISTER_ENDPOINT: process.env.EGOV_PENSION_PUSH_EMPLOYEES_TO_PENSION_NOTIFICATION_REGISTER_ENDPOINT || "/v1/_pushEmployeesToPensionNotificationRegister",
  EGOV_PENSION_CREATE_MONTHLY_PENSION_REGISTER_ENDPOINT: process.env.EGOV_PENSION_CREATE_MONTHLY_PENSION_REGISTER_ENDPOINT || "/v1/_createMonthlyPensionRegister",
  EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_ENDPOINT: process.env.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_ENDPOINT || "/v1/_updatePensionRevisionBulk"

}, (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_HOST", process.env.EGOV_PENSION_HOST || "http://localhost:8086"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_RULE_ENGINE_HOST", process.env.EGOV_PENSION_RULE_ENGINE_HOST || "http://localhost:8097"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_RULE_ENGINE_CONTEXT_PATH", process.env.EGOV_PENSION_RULE_ENGINE_CONTEXT_PATH || "/pension-calculator"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_RULE_ENGINE_CALCULATE_BENEFIT_ENDPOINT", process.env.EGOV_PENSION_RULE_ENGINE_CALCULATE_BENEFIT_ENDPOINT || "/v1/_calculateBenefit"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_RULE_ENGINE_GET_DEPENDENT_ELIGIBILITY_FOR_BENEFIT_ENDPOINT", process.env.EGOV_PENSION_RULE_ENGINE_GET_DEPENDENT_ELIGIBILITY_FOR_BENEFIT_ENDPOINT || "/v1/_getDependentEligibilityForBenefit"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_RULE_ENGINE_CALCULATE_REVISED_PENSION_ENDPOINT", process.env.EGOV_PENSION_RULE_ENGINE_CALCULATE_REVISED_PENSION_ENDPOINT || "/v1/_calculateRevisedPension"), (0, _defineProperty3.default)(_envVariables, "TRACER_ENABLE_REQUEST_LOGGING", process.env.TRACER_ENABLE_REQUEST_LOGGING || false), (0, _defineProperty3.default)(_envVariables, "HTTP_CLIENT_DETAILED_LOGGING_ENABLED", process.env.HTTP_CLIENT_DETAILED_LOGGING_ENABLED || false), (0, _defineProperty3.default)(_envVariables, "EGOV_WORKFLOW_HOST", process.env.EGOV_WORKFLOW_HOST || "http://localhost:8089"), (0, _defineProperty3.default)(_envVariables, "EGOV_WORKFLOW_TRANSITION_ENDPOINT", process.env.EGOV_WORKFLOW_TRANSITION_ENDPOINT || "/egov-workflow-v2/egov-wf/process/_transition"), (0, _defineProperty3.default)(_envVariables, "EGOV_WORKFLOW_SEARCH_ENDPOINT", process.env.EGOV_WORKFLOW_SEARCH_ENDPOINT || "/egov-workflow-v2/egov-wf/process/_search"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_HOST", process.env.EGOV_USER_HOST || "http://localhost:8998"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_CONTEXT_PATH", process.env.EGOV_USER_CONTEXT_PATH || "/user"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_GENERATE_ACCESS_TOKEN_ENDPOINT", process.env.EGOV_USER_GENERATE_ACCESS_TOKEN_ENDPOINT || "/oauth/token"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_SEARCH_ENDPOINT", process.env.EGOV_USER_SEARCH_ENDPOINT || "/_search"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_DETAILS_ENDPOINT", process.env.EGOV_USER_DETAILS_ENDPOINT || "/_details"), (0, _defineProperty3.default)(_envVariables, "EGOV_IDGEN_HOST", process.env.EGOV_IDGEN_HOST || "http://localhost:8088"), (0, _defineProperty3.default)(_envVariables, "EGOV_IDGEN_CONTEXT_PATH", process.env.EGOV_IDGEN_CONTEXT_PATH || "/egov-idgen"), (0, _defineProperty3.default)(_envVariables, "EGOV_IDGEN_GENERATE_ENPOINT", process.env.EGOV_IDGEN_GENERATE_ENPOINT || "/id/_generate"), (0, _defineProperty3.default)(_envVariables, "EGOV_IDGEN_PENSION_RRP_APPLICATION_NUMBER_ID_NAME", process.env.EGOV_IDGEN_PENSION_RRP_APPLICATION_NUMBER_ID_NAME || "pension.rrp.application.number"), (0, _defineProperty3.default)(_envVariables, "EGOV_IDGEN_PENSION_DOE_APPLICATION_NUMBER_ID_NAME", process.env.EGOV_IDGEN_PENSION_DOE_APPLICATION_NUMBER_ID_NAME || "pension.doe.application.number"), (0, _defineProperty3.default)(_envVariables, "EGOV_IDGEN_PENSION_DOP_APPLICATION_NUMBER_ID_NAME", process.env.EGOV_IDGEN_PENSION_DOP_APPLICATION_NUMBER_ID_NAME || "pension.dop.application.number"), (0, _defineProperty3.default)(_envVariables, "EGOV_IDGEN_PENSION_PENSIONER_NUMBER_ID_NAME", process.env.EGOV_IDGEN_PENSION_PENSIONER_NUMBER_ID_NAME || "pension.pensioner.number"), (0, _defineProperty3.default)(_envVariables, "EGOV_RR_APPLICATION_FORMATE", process.env.EGOV_RR_APPLICATION_FORMATE || "CH-RR-[cy:yyyy-MM-dd]-[SEQ_EG_PENSION_RR_APL]"), (0, _defineProperty3.default)(_envVariables, "EGOV_DE_APPLICATION_FORMATE", process.env.EGOV_DE_APPLICATION_FORMATE || "CH-DE-[cy:yyyy-MM-dd]-[SEQ_EG_PENSION_DE_APL]"), (0, _defineProperty3.default)(_envVariables, "EGOV_DP_APPLICATION_FORMATE", process.env.EGOV_DP_APPLICATION_FORMATE || "CH-DP-[cy:yyyy-MM-dd]-[SEQ_EG_PENSION_DP_APL]"), (0, _defineProperty3.default)(_envVariables, "EGOV_PN_APPLICATION_FORMATE", process.env.EGOV_PN_APPLICATION_FORMATE || "CH-PN-[SEQ_EG_PENSION_PN_APL]"), (0, _defineProperty3.default)(_envVariables, "EGOV_MDMS_HOST", process.env.EGOV_MDMS_HOST || "http://localhost:8094"), (0, _defineProperty3.default)(_envVariables, "EGOV_MDMS_CONTEXT_PATH", process.env.EGOV_MDMS_CONTEXT_PATH || "/egov-mdms-service/v1"), (0, _defineProperty3.default)(_envVariables, "EGOV_MDMS_SEARCH_ENPOINT", process.env.EGOV_MDMS_SEARCH_ENPOINT || "/_search"), (0, _defineProperty3.default)(_envVariables, "EGOV_HRMS_HOST", process.env.EGOV_HRMS_HOST || "http://localhost:9999"), (0, _defineProperty3.default)(_envVariables, "EGOV_HRMS_CONTEXT_PATH", process.env.EGOV_HRMS_CONTEXT_PATH || "/egov-hrms/employees"), (0, _defineProperty3.default)(_envVariables, "EGOV_HRMS_EMPLOYEE_SEARCH_ENDPOINT", process.env.EGOV_HRMS_EMPLOYEE_SEARCH_ENDPOINT || "/_search"), (0, _defineProperty3.default)(_envVariables, "EGOV_FILESTORE_HOST", process.env.EGOV_FILESTORE_HOST || "http://localhost:8083"), (0, _defineProperty3.default)(_envVariables, "EGOV_FILESTORE_CONTEXT_PATH", process.env.EGOV_FILESTORE_CONTEXT_PATH || "/filestore"), (0, _defineProperty3.default)(_envVariables, "EGOV_FILESTORE_URL_ENDPOINT", process.env.EGOV_FILESTORE_URL_ENDPOINT || "/v1/files/url"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_EVENT_HOST", process.env.EGOV_USER_EVENT_HOST || "http://localhost:8999"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_EVENT_CONTEXT_PATH", process.env.EGOV_USER_EVENT_CONTEXT_PATH || "/egov-user-event"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_EVENT_CREATE_ENDPOINT", process.env.EGOV_USER_EVENT_CREATE_ENDPOINT || "/v1/events/_create"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_EVENT_SEARCH_ENDPOINT", process.env.EGOV_USER_EVENT_SEARCH_ENDPOINT || "/v1/events/_search"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_EVENT_TYPE", process.env.EGOV_USER_EVENT_TYPE || "SYSTEMGENERATED"), (0, _defineProperty3.default)(_envVariables, "EGOV_USER_EVENT_NAME_SOURCE", process.env.EGOV_USER_EVENT_NAME_SOURCE || "webapp"), (0, _defineProperty3.default)(_envVariables, "EGOV_MONTHLY_PNR_GENERATED_USER_EVENT_ROLE", process.env.EGOV_MONTHLY_PNR_GENERATED_USER_EVENT_ROLE || "DDO"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_REQUESTINFO_API_ID", process.env.EGOV_PENSION_REQUESTINFO_API_ID || "Rainmaker"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_REQUESTINFO_VER", process.env.EGOV_PENSION_REQUESTINFO_VER || ".01"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_REQUESTINFO_ACTION", process.env.EGOV_PENSION_REQUESTINFO_ACTION || ""), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_REQUESTINFO_DID", process.env.EGOV_PENSION_REQUESTINFO_DID || "1"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_REQUESTINFO_KEY", process.env.EGOV_PENSION_REQUESTINFO_KEY || ""), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_REQUESTINFO_MSG_ID", process.env.EGOV_PENSION_REQUESTINFO_MSG_ID || "20170310130900|en_IN"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_REQUESTINFO_REQUSTER_ID", process.env.EGOV_PENSION_REQUESTINFO_REQUSTER_ID || ""), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_PNR_SCHEDULAR_START_DATE", process.env.EGOV_PENSION_PNR_SCHEDULAR_START_DATE || 16), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_PNR_SCHEDULAR_START_HOURS", process.env.EGOV_PENSION_PNR_SCHEDULAR_START_HOURS || 20), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_PNR_SCHEDULAR_START_MINUTES", process.env.EGOV_PENSION_PNR_SCHEDULAR_START_MINUTES || 18), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_SCHEDULAR_USERNAME", process.env.EGOV_PENSION_SCHEDULAR_USERNAME || "EMP-248430-000117"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_SCHEDULAR_PASSWORD", process.env.EGOV_PENSION_SCHEDULAR_PASSWORD || "P@ssw0rd"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_SCHEDULAR_GRANT_TYPE", process.env.EGOV_PENSION_SCHEDULAR_GRANT_TYPE || "password"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_SCHEDULAR_SCOPE", process.env.EGOV_PENSION_SCHEDULAR_SCOPE || "read"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_SCHEDULAR_TENANTID", process.env.EGOV_PENSION_SCHEDULAR_TENANTID || "ch.chandigarh"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_SCHEDULAR_USERTYPE", process.env.EGOV_PENSION_SCHEDULAR_USERTYPE || "EMPLOYEE"), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_PNR_SCHEDULAR_NEXT_N_MONTHS", process.env.EGOV_PENSION_PNR_SCHEDULAR_NEXT_N_MONTHS || 120), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_MAX_RETIREMENT_AGE", process.env.EGOV_PENSION_MAX_RETIREMENT_AGE || 60), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_NOT_APPLICABLE_FROM_YEAR", process.env.EGOV_PENSION_NOT_APPLICABLE_FROM_YEAR || 2004), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_CREATE_PENSION_REGISTER_SCHEDULAR_START_DATE", process.env.EGOV_PENSION_CREATE_PENSION_REGISTER_SCHEDULAR_START_DATE || 5), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_CREATE_PENSION_REGISTER_SCHEDULAR_START_HOURS", process.env.EGOV_PENSION_CREATE_PENSION_REGISTER_SCHEDULAR_START_HOURS || 19), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_CREATE_PENSION_REGISTER_SCHEDULAR_START_MINUTES", process.env.EGOV_PENSION_CREATE_PENSION_REGISTER_SCHEDULAR_START_MINUTES || 50), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_START_DATE", process.env.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_START_DATE || 8), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_START_HOURS", process.env.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_START_HOURS || 6), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_START_MINUTES", process.env.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_START_MINUTES || 25), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_IS_DA_MODIFIABLE", process.env.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_IS_DA_MODIFIABLE || true), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_IS_IR_MODIFIABLE", process.env.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_IS_IR_MODIFIABLE || true), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_IS_FMA_MODIFIABLE", process.env.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_IS_FMA_MODIFIABLE || false), (0, _defineProperty3.default)(_envVariables, "EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_MODIFIED_FMA", process.env.EGOV_PENSION_UPDATE_PENSION_REVISION_BULK_SCHEDULAR_MODIFIED_FMA || 500), (0, _defineProperty3.default)(_envVariables, "EGOV_WORKFLOW_DEFAULT_OFFSET", process.env.EGOV_WORKFLOW_DEFAULT_OFFSET || 0), (0, _defineProperty3.default)(_envVariables, "EGOV_WORKFLOW_DEFAULT_LIMIT", process.env.EGOV_WORKFLOW_DEFAULT_LIMIT || 100), (0, _defineProperty3.default)(_envVariables, "EGOV_WORKFLOW_MAX_OFFSET", process.env.EGOV_WORKFLOW_MAX_OFFSET || 100), (0, _defineProperty3.default)(_envVariables, "EGOV_HRMS_DEFAULT_LIMIT", process.env.EGOV_HRMS_DEFAULT_LIMIT || 1000), (0, _defineProperty3.default)(_envVariables, "PENSION_ENCRYPTION_KEY", process.env.PENSION_ENCRYPTION_KEY || 'abcdefghijklmnop'), (0, _defineProperty3.default)(_envVariables, "LOGGING_LEVEL", process.env.LOGGING_LEVEL || "debug"), _envVariables);
exports.default = envVariables;
//# sourceMappingURL=envVariables.js.map