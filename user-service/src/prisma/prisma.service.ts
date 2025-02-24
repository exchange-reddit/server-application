import { Injectable, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { PrismaClient } from '@prisma/client';

@Injectable()
export class PrismaService
  extends PrismaClient
  implements OnModuleInit, OnModuleDestroy
{
  async onModuleDestroy() {
    try {
      await this.$disconnect();
      console.log('✅ Successfully connected to the database');
    } catch (error) {
      console.error('❌ Database connection failed:', error);
    }
  }
  async onModuleInit() {
    try {
      await this.$connect();
      console.log('✅ Successfully connected to the database');
    } catch (error) {
      console.error('❌ Database connection failed:', error);
    }
  }
}
