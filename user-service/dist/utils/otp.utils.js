"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.generateNewOtp = generateNewOtp;
const crypto = require("crypto");
function generateNewOtp(size = 6) {
    const max = Math.pow(10, size);
    const randomNumber = crypto.randomInt(0, max);
    return randomNumber.toString().padStart(size, '0');
}
//# sourceMappingURL=otp.utils.js.map