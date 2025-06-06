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
exports.UsersService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../../prisma/prisma.service");
const argon = require("argon2");
const library_1 = require("@prisma/client/runtime/library");
const email_verification_service_1 = require("../../service/email-verification.service");
let UsersService = class UsersService {
    constructor(prisma, verificationToken) {
        this.prisma = prisma;
        this.verificationToken = verificationToken;
    }
    async register(createUserInput) {
        const hash = await argon.hash(createUserInput.password);
        try {
            const user = await this.prisma.exchangeUser.create({
                data: {
                    firstName: createUserInput.firstName,
                    lastName: createUserInput.lastName,
                    homeEmail: createUserInput.homeEmail,
                    exchangeEmail: createUserInput.exchangeEmail,
                    passwordHash: hash,
                    exchangeUni: createUserInput.exchangeUni,
                    homeUni: createUserInput.homeUni,
                    nationality: createUserInput.nationality,
                    exchangeStart: createUserInput.exchangeStart,
                    exchangeEnd: createUserInput.exchangeEnd,
                    dateOfBirth: createUserInput.dateOfBirth,
                    preferredLanguage: createUserInput.preferredLangauge,
                },
            });
            return user;
        }
        catch (err) {
            if (err instanceof library_1.PrismaClientKnownRequestError) {
                if (err.code === 'P2002') {
                    throw new common_1.ForbiddenException('Credentials taken');
                }
                if (err.code === 'P2004') {
                    throw new common_1.BadRequestException('One or more db constraint(s) have been provided');
                }
            }
            else {
                throw err;
            }
        }
    }
    async generateEmailVerification(userId) {
        const user = await this.prisma.exchangeUser.findUnique({
            where: { id: userId },
        });
        if (!user) {
            throw new common_1.NotFoundException('User not found');
        }
        if (user.homeEmailVerified && user.exchangeEmailVerified) {
            throw new common_1.UnprocessableEntityException('Account already verified');
        }
        const otpArray = await this.verificationToken.generateOtp(user.id);
        const otpHome = otpArray[0];
        const otpExchange = otpArray[1];
    }
    create() {
        return 'This action adds a new user';
    }
    findAll() {
        return `This action returns all users`;
    }
    findOne(id) {
        return `This action returns a #${id} user`;
    }
    update(id, updateUserInput) {
        return `This action updates a #${id} user`;
    }
    remove(id) {
        return `This action removes a #${id} user`;
    }
};
exports.UsersService = UsersService;
exports.UsersService = UsersService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService,
        email_verification_service_1.VerificationTokenService])
], UsersService);
//# sourceMappingURL=users.service.js.map