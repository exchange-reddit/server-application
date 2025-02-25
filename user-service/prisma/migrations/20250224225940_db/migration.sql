-- AlterTable
ALTER TABLE "ExchangeUser" ADD COLUMN     "exchangeEmailVerified" BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN     "homeEmailVerified" BOOLEAN NOT NULL DEFAULT false;
