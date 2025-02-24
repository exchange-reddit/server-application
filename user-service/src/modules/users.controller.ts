import { Body, Controller, Post } from '@nestjs/common';
import { UsersService } from './users/users.service';
import { CreateUserInput } from './users/dto';

@Controller('auth')
export class UserController {
  constructor(private userService: UsersService) {}

  @Post('register')
  register(@Body() dto: CreateUserInput) {
    return this.userService.register(dto);
  }
}
