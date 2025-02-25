import { Body, Controller, Get, Post } from '@nestjs/common';
import { UsersService } from './users.service';
import { CreateUserInput } from './dto';
import { ExchangeUser } from '@prisma/client';

@Controller('auth')
export class UserController {
  constructor(private userService: UsersService) {}

  @Post('register')
  register(@Body() dto: CreateUserInput) {
    return this.userService.register(dto);
  }

  // Send verification otp to the user
  @Get('verification-otp')
  async generateEmailVerification(user: ExchangeUser) {
    await this.userService.generateEmailVerification(user.id);
  }
}
