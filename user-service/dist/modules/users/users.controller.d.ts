import { UsersService } from './users.service';
import { CreateUserInput } from './dto';
import { ExchangeUser } from '@prisma/client';
export declare class UserController {
    private userService;
    constructor(userService: UsersService);
    register(dto: CreateUserInput): Promise<{
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
    generateEmailVerification(user: ExchangeUser): Promise<void>;
}
