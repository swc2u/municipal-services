'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.decrypt = exports.encrypt = undefined;

var _crypto = require('crypto');

var _crypto2 = _interopRequireDefault(_crypto);

var _envVariables = require('../envVariables');

var _envVariables2 = _interopRequireDefault(_envVariables);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var algorithm = 'aes-256-cbc';
//const key = crypto.randomBytes(32);
var key = _crypto2.default.createHash('sha256').update(String(_envVariables2.default.PENSION_ENCRYPTION_KEY)).digest('base64').substr(0, 32);
var iv = _crypto2.default.randomBytes(16);

/* export const encrypt = (text) => {
 let cipher = crypto.createCipheriv('aes-256-cbc', Buffer.from(key), iv);
 let encrypted = cipher.update(text);
 encrypted = Buffer.concat([encrypted, cipher.final()]);
 return encrypted.toString('hex');
}

export const decrypt = (text) => {
 
 let encryptedText = Buffer.from(text, 'hex');
 let decipher = crypto.createDecipheriv('aes-256-cbc', Buffer.from(key), iv);
 let decrypted = decipher.update(encryptedText);
 decrypted = Buffer.concat([decrypted, decipher.final()]);
 return decrypted.toString();
}
 */
var encrypt = exports.encrypt = function encrypt(text) {
  var cipher = _crypto2.default.createCipher(algorithm, Buffer.from(key));
  var encrypted = cipher.update(text);
  encrypted = Buffer.concat([encrypted, cipher.final()]);
  return encrypted.toString('hex');
};

var decrypt = exports.decrypt = function decrypt(text) {
  var encryptedText = Buffer.from(text, 'hex');
  var decipher = _crypto2.default.createDecipher(algorithm, Buffer.from(key));
  var decrypted = decipher.update(encryptedText);
  decrypted = Buffer.concat([decrypted, decipher.final()]);
  return decrypted.toString();
};
//# sourceMappingURL=encryption.js.map