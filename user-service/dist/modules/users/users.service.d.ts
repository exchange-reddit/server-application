import { CreateUserInput } from './dto/create-user.input';
import { UpdateUserInput } from './dto/update-user.input';
import { PrismaService } from 'src/prisma/prisma.service';
import { VerificationTokenService } from 'src/service/email-verification.service';
export declare class UsersService {
    private prisma;
    private verificationToken;
    constructor(prisma: PrismaService, verificationToken: VerificationTokenService);
    register(createUserInput: CreateUserInput): Promise<{
        id: number;
        firstName: string;
        lastName: string;
        homeEmail: string;
        exchangeEmail: string;
        passwordHash: string;
        isAdmin: boolean;
        exchangeUni: string;
        homeUni: string;
        nationality: string;
        exchangeStart: Date;
        exchangeEnd: Date;
        dateOfBirth: Date | null;
        preferredLanguage: import(".prisma/client").$Enums.Language;
        isActive: boolean;
        homeEmailVerified: boolean;
        exchangeEmailVerified: boolean;
    } | undefined>;
    generateEmailVerification(userId: number): Promise<void>;
    create(): string;
    findAll(): string;
    findOne(id: number): string;
    update(id: number, updateUserInput: UpdateUserInput): string;
    remove(id: number): string;
}
