"use strict";

var _envVariables = require("./envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var _require = require("pg"),
    Pool = _require.Pool;

// Use connection pool to limit max active DB connections

var pool = new Pool({
  user: _envVariables2.default.DB_USER,
  host: _envVariables2.default.DB_HOST,
  database: _envVariables2.default.DB_NAME,
  password: _envVariables2.default.DB_PASSWORD,
  ssl: _envVariables2.default.DB_SSL,
  port: _envVariables2.default.DB_PORT,
  max: _envVariables2.default.DB_MAX_POOL_SIZE,
  idleTimeoutMillis: 30000,
  connectionTimeoutMillis: 2000
});

// Expose method, log query, initiate trace etc at single point later on.
module.exports = {
  query: function query(text, params) {
    return pool.query(text, params);
  }
};
//# sourceMappingURL=db.js.map