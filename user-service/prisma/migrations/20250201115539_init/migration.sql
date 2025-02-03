-- CreateEnum
CREATE TYPE "Language" AS ENUM ('ENGLISH', 'KOREAN', 'SPANISH');

-- CreateTable
CREATE TABLE "ExchangeUser" (
    "id" SERIAL NOT NULL,
    "firstName" TEXT NOT NULL,
    "lastName" TEXT NOT NULL,
    "homeEmail" TEXT NOT NULL,
    "exchangeEmail" TEXT NOT NULL,
    "passwordHash" TEXT NOT NULL,
    "isAdmin" BOOLEAN NOT NULL DEFAULT false,
    "exchangeUni" TEXT NOT NULL,
    "homeUni" TEXT NOT NULL,
    "nationality" TEXT NOT NULL,
    "exchangeStart" TIMESTAMP(3) NOT NULL,
    "exchangeEnd" TIMESTAMP(3) NOT NULL,
    "dateOfBirth" TIMESTAMP(3) NOT NULL,
    "preferredLanguage" "Language" NOT NULL DEFAULT 'ENGLISH',
    "isActive" BOOLEAN NOT NULL DEFAULT true,

    CONSTRAINT "ExchangeUser_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "ExchangeUser_homeEmail_key" ON "ExchangeUser"("homeEmail");

-- CreateIndex
CREATE UNIQUE INDEX "ExchangeUser_exchangeEmail_key" ON "ExchangeUser"("exchangeEmail");
