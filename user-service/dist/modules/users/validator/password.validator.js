"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.PasswordStrengthValidator = void 0;
const common_1 = require("@nestjs/common");
const class_validator_1 = require("class-validator");
let PasswordStrengthValidator = class PasswordStrengthValidator {
    validate(password, args) {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        const isValid = passwordRegex.test(password);
        if (!isValid) {
            throw new common_1.HttpException({
                statusCode: common_1.HttpStatus.UNPROCESSABLE_ENTITY,
                message: 'Password must contain at least 8 characters, including an uppercase, lowercase, number, and special character.',
                error: 'WeakPassword',
            }, common_1.HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return true;
    }
    defaultMessage(args) {
        return 'Password must contain at least 8 characters, including an uppercase, lowercase, number, and special character.';
    }
};
exports.PasswordStrengthValidator = PasswordStrengthValidator;
exports.PasswordStrengthValidator = PasswordStrengthValidator = __decorate([
    (0, class_validator_1.ValidatorConstraint)({ name: 'PasswordStrength', async: false })
], PasswordStrengthValidator);
//# sourceMappingURL=password.validator.js.map