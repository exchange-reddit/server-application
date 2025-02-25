/**
 * Purpose:
 * Generate OTP so that the user can verify their account
 */

import { UnprocessableEntityException } from '@nestjs/common';
import { PrismaService } from 'src/prisma/prisma.service';
import * as argon from 'argon2';
import { generateNewOtp } from 'src/utils/otp.utils';

export class VerificationTokenService {
  private readonly minRequestIntervalMinutes = 1;
  private readonly tokenExpirationMinutes = 15;
  constructor(private prisma: PrismaService) {}

  async generateOtp(userId: number, size = 6): Promise<string[]> {
    const now = new Date();

    // Check if the user is sending a spam request for a home token
    const recentHomeToken = await this.prisma.homeToken.findFirst({
      where: {
        userId: userId,
        createdAt: {
          gt: new Date(
            now.getTime() - this.minRequestIntervalMinutes * 60 * 1000,
          ),
        },
      },
    });

    // Check if the user is sending a spam request for an exchange token
    const recentExchangeToken = await this.prisma.exchangeToken.findFirst({
      where: {
        userId: userId,
        createdAt: {
          gt: new Date(
            now.getTime() - this.minRequestIntervalMinutes * 60 * 1000,
          ),
        },
      },
    });

    if (recentHomeToken || recentExchangeToken) {
      throw new UnprocessableEntityException(
        'Please wait a minute before requesting a new token',
      );
    }

    // Generate a random OTP value for home university
    const otpHome = generateNewOtp(size);
    // Hash the value of home token to keep it securely
    const hashedHomeToken = await argon.hash(otpHome);

    // Generate a random OTP value for exchange university
    const otpExchange = generateNewOtp(size);
    // Hash the value of exchange token to keep it securely
    const hashedExchangeToken = await argon.hash(otpExchange);

    // Save home university token entity to db
    await this.prisma.homeToken.create({
      data: {
        userId,
        token: hashedHomeToken,
        expiresAt: new Date(
          now.getTime() + this.tokenExpirationMinutes * 60 * 1000,
        ),
      },
    });

    // Save exchange university token entity to db
    await this.prisma.homeToken.create({
      data: {
        userId,
        token: hashedExchangeToken,
        expiresAt: new Date(
          now.getTime() + this.tokenExpirationMinutes * 60 * 1000,
        ),
      },
    });

    return [otpHome, otpExchange];
  }
}
