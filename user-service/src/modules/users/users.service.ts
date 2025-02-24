import {
  BadRequestException,
  ForbiddenException,
  Injectable,
} from '@nestjs/common';
import { CreateUserInput } from './dto/create-user.input';
import { UpdateUserInput } from './dto/update-user.input';
import { PrismaService } from 'src/prisma/prisma.service';
import * as argon from 'argon2';
import { PrismaClientKnownRequestError } from '@prisma/client/runtime/library';

@Injectable()
export class UsersService {
  constructor(private prisma: PrismaService) {}
  async register(createUserInput: CreateUserInput) {
    // Validate strength of password prior to continuation

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
