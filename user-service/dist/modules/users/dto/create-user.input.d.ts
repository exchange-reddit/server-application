import { Language } from '@prisma/client';
export declare class CreateUserInput {
    firstName: string;
    lastName: string;
    homeEmail: string;
    exchangeEmail: string;
    password: string;
    exchangeUni: string;
    homeUni: string;
    nationality: string;
    exchangeStart: Date;
    exchangeEnd: Date;
    dateOfBirth: Date;
    preferredLangauge: Language;
}
