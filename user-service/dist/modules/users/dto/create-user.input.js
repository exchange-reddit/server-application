"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.CreateUserInput = void 0;
const graphql_1 = require("@nestjs/graphql");
const client_1 = require("@prisma/client");
const class_validator_1 = require("class-validator");
const password_validator_1 = require("../validator/password.validator");
let CreateUserInput = class CreateUserInput {
};
exports.CreateUserInput = CreateUserInput;
__decorate([
    (0, graphql_1.Field)(() => String, { description: 'First Name' }),
    __metadata("design:type", String)
], CreateUserInput.prototype, "firstName", void 0);
__decorate([
    (0, graphql_1.Field)(() => String, { description: 'Last Name' }),
    __metadata("design:type", String)
], CreateUserInput.prototype, "lastName", void 0);
__decorate([
    (0, graphql_1.Field)(() => String, { description: 'Email of Home University' }),
    (0, class_validator_1.IsEmail)(),
    __metadata("design:type", String)
], CreateUserInput.prototype, "homeEmail", void 0);
__decorate([
    (0, graphql_1.Field)(() => String, { description: 'Email of Exchange University' }),
    (0, class_validator_1.IsEmail)(),
    __metadata("design:type", String)
], CreateUserInput.prototype, "exchangeEmail", void 0);
__decorate([
    (0, graphql_1.Field)(() => String, { description: 'Raw Password that the User Creates' }),
    (0, class_validator_1.IsString)(),
    (0, class_validator_1.IsNotEmpty)(),
    (0, class_validator_1.Validate)(password_validator_1.PasswordStrengthValidator),
    __metadata("design:type", String)
], CreateUserInput.prototype, "password", void 0);
__decorate([
    (0, graphql_1.Field)(() => String, { description: 'Exchange University of the User' }),
    (0, class_validator_1.IsString)(),
    __metadata("design:type", String)
], CreateUserInput.prototype, "exchangeUni", void 0);
__decorate([
    (0, graphql_1.Field)(() => String, { description: 'Home University of the User' }),
    (0, class_validator_1.IsString)(),
    __metadata("design:type", String)
], CreateUserInput.prototype, "homeUni", void 0);
__decorate([
    (0, graphql_1.Field)(() => String, { description: 'Nationality of the User' }),
    (0, class_validator_1.IsString)(),
    __metadata("design:type", String)
], CreateUserInput.prototype, "nationality", void 0);
__decorate([
    (0, graphql_1.Field)(() => Date, { description: 'Start date of Exchange Studies' }),
    (0, class_validator_1.IsDateString)(),
    __metadata("design:type", Date)
], CreateUserInput.prototype, "exchangeStart", void 0);
__decorate([
    (0, graphql_1.Field)(() => Date, { description: 'End date of Exchange Studies' }),
    (0, class_validator_1.IsDateString)(),
    __metadata("design:type", Date)
], CreateUserInput.prototype, "exchangeEnd", void 0);
__decorate([
    (0, graphql_1.Field)(() => Date, { description: 'Birth Day of User' }),
    (0, class_validator_1.IsDateString)(),
    __metadata("design:type", Date)
], CreateUserInput.prototype, "dateOfBirth", void 0);
__decorate([
    (0, graphql_1.Field)(() => client_1.Language, { description: 'Preferred Language of the User' }),
    __metadata("design:type", String)
], CreateUserInput.prototype, "preferredLangauge", void 0);
exports.CreateUserInput = CreateUserInput = __decorate([
    (0, graphql_1.InputType)()
], CreateUserInput);
//# sourceMappingURL=create-user.input.js.map