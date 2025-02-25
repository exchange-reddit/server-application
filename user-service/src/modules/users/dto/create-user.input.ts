import { InputType, Field } from '@nestjs/graphql';
import { Language } from '@prisma/client';
import {
  IsDate,
  IsEmail,
  IsNotEmpty,
  IsString,
  Validate,
} from 'class-validator';
import { PasswordStrengthValidator } from '../validator/password.validator';

/**
 * Purpose:
 * A DTO to retreive user account information from the client side.
 * Things to Note:
 * Field IsAdmin is not implemented here as that would cause a security issue.
 * Instead, we should manually modify the DB to assign an admin privilege.
 */
@InputType()
export class CreateUserInput {
  @Field(() => String, { description: 'First Name' })
  firstName: string;

  @Field(() => String, { description: 'Last Name' })
  lastName: string;

  @Field(() => String, { description: 'Email of Home University' })
  @IsEmail()
  homeEmail: string;

  @Field(() => String, { description: 'Email of Exchange University' })
  @IsEmail()
  exchangeEmail: string;

  // Password validation method is implemented here to verify that it complies with our security conditions
  // For more information, please check password.validator.ts file
  @Field(() => String, { description: 'Raw Password that the User Creates' })
  @IsString()
  @IsNotEmpty()
  @Validate(PasswordStrengthValidator)
  password: string;

  @Field(() => String, { description: 'Exchange University of the User' })
  @IsString()
  exchangeUni: string;

  @Field(() => String, { description: 'Home University of the User' })
  @IsString()
  homeUni: string;

  @Field(() => String, { description: 'Nationality of the User' })
  @IsString()
  nationality: string;

  @Field(() => Date, { description: 'Start date of Exchange Studies' })
  @IsDate()
  exchangeStart: Date;

  @Field(() => Date, { description: 'End date of Exchange Studies' })
  @IsDate()
  exchangeEnd: Date;

  @Field(() => Date, { description: 'Birth Day of User' })
  @IsDate()
  dateOfBirth: Date;

  @Field(() => Language, { description: 'Preferred Language of the User' })
  preferredLangauge: Language;
}
