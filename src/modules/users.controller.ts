import { Controller, Post } from '@nestjs/common';
import { UsersService } from './users/users.service';

@Controller('auth')
export class UserController {
  constructor(private userService: UsersService) {}

  @Post('register')
  register() {
    return 'Registered.';
  }
}
