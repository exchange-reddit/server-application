import { Module } from '@nestjs/common';
import { UsersService } from './users.service';
import { UsersResolver } from './users.resolver';
import { UserController } from './users.controller';

@Module({
  controllers: [UserController],
  providers: [UsersResolver, UsersService],
})
export class UsersModule {}
