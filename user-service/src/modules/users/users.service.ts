import {
  BadRequestException,
  ForbiddenException,
  Injectable,
  NotFoundException,
  UnprocessableEntityException,
} from '@nestjs/common';
import { CreateUserInput } from './dto/create-user.input';
import { UpdateUserInput } from './dto/update-user.input';
import { PrismaService } from 'src/prisma/prisma.service';
import * as argon from 'argon2';
import { PrismaClientKnownRequestError } from '@prisma/client/runtime/library';
import { VerificationTokenService } from 'src/service/email-verification.service';

@Injectable()
export class UsersService {
  constructor(
    private prisma: PrismaService,
    private verificationToken: VerificationTokenService,
  ) {}
  async register(createUserInput: CreateUserInput) {
    // Hash the password that the user passed.
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
    } catch (err) {
      // If the error originates from Prisma, handle them based on the codes.
      // For more information, please check https://www.prisma.io/docs/orm/reference/error-reference
      if (err instanceof PrismaClientKnownRequestError) {
        if (err.code === 'P2002') {
          throw new ForbiddenException('Credentials taken');
        }

        if (err.code === 'P2004') {
          throw new BadRequestException(
            'One or more db constraint(s) have been provided',
          );
        }
      } else {
        throw err;
      }
    }
  }

  async generateEmailVerification(userId: number) {
    // Check if user account exists in db
    const user = await this.prisma.exchangeUser.findUnique({
      where: { id: userId },
    });

    // If the user does not exist, throw an exception.
    if (!user) {
      throw new NotFoundException('User not found');
    }

    // If both of the emails are already verified, throw an exception.
    if (user.homeEmailVerified && user.exchangeEmailVerified) {
      throw new UnprocessableEntityException('Account already verified');
    }
    // Generate OTPs for Home University and Exchange University
    const otpArray = await this.verificationToken.generateOtp(user.id);

    // Separate the otp into two parts
    const otpHome = otpArray[0];
    const otpExchange = otpArray[1];

    // Make HTTP request to notification service (email)
  }

  create() {
    return 'This action adds a new user';
  }

  findAll() {
    return `This action returns all users`;
  }

  findOne(id: number) {
    return `This action returns a #${id} user`;
  }

  update(id: number, updateUserInput: UpdateUserInput) {
    return `This action updates a #${id} user`;
  }

  remove(id: number) {
    return `This action removes a #${id} user`;
  }
}
