"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.VerificationTokenService = void 0;
const common_1 = require("@nestjs/common");
const argon = require("argon2");
const otp_utils_1 = require("../utils/otp.utils");
class VerificationTokenService {
    constructor(prisma) {
        this.prisma = prisma;
        this.minRequestIntervalMinutes = 1;
        this.tokenExpirationMinutes = 15;
    }
    async generateOtp(userId, size = 6) {
        const now = new Date();
        const recentHomeToken = await this.prisma.homeToken.findFirst({
            where: {
                userId: userId,
                createdAt: {
                    gt: new Date(now.getTime() - this.minRequestIntervalMinutes * 60 * 1000),
                },
            },
        });
        const recentExchangeToken = await this.prisma.exchangeToken.findFirst({
            where: {
                userId: userId,
                createdAt: {
                    gt: new Date(now.getTime() - this.minRequestIntervalMinutes * 60 * 1000),
                },
            },
        });
        if (recentHomeToken || recentExchangeToken) {
            throw new common_1.UnprocessableEntityException('Please wait a minute before requesting a new token');
        }
        const otpHome = (0, otp_utils_1.generateNewOtp)(size);
        const hashedHomeToken = await argon.hash(otpHome);
        const otpExchange = (0, otp_utils_1.generateNewOtp)(size);
        const hashedExchangeToken = await argon.hash(otpExchange);
        await this.prisma.homeToken.create({
            data: {
                userId,
                token: hashedHomeToken,
                expiresAt: new Date(now.getTime() + this.tokenExpirationMinutes * 60 * 1000),
            },
        });
        await this.prisma.homeToken.create({
            data: {
                userId,
                token: hashedExchangeToken,
                expiresAt: new Date(now.getTime() + this.tokenExpirationMinutes * 60 * 1000),
            },
        });
        return [otpHome, otpExchange];
    }
}
exports.VerificationTokenService = VerificationTokenService;
//# sourceMappingURL=email-verification.service.js.map