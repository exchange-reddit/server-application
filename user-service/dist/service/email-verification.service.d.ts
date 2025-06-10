import { PrismaService } from 'src/prisma/prisma.service';
export declare class VerificationTokenService {
    private prisma;
    private readonly minRequestIntervalMinutes;
    private readonly tokenExpirationMinutes;
    constructor(prisma: PrismaService);
    generateOtp(userId: number, size?: number): Promise<string[]>;
}
