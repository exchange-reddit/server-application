import { Global, Module } from '@nestjs/common';
import { PrismaService } from './prisma.service';

// Declared as global so that all files can access this module.
@Global()
@Module({
  providers: [PrismaService],
  exports: [PrismaService],
})
export class PrismaModule {}
