# Stage 1: Build the application
FROM node:18-alpine AS build

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies using npm ci
RUN npm install

# Copy the rest of the application files
COPY . .

# Run the build command
RUN npm run build

# Stage 2: Production environment
FROM node:18-alpine

# Set the working directory
WORKDIR /usr/src/app

# Copy only the production package.json and package-lock.json
COPY package*.json ./

# Install only production dependencies
RUN npm install --production

# Copy the built files from the build stage
# This is required because of nest/cli which is not recommended to be in production dependency
COPY --from=build /usr/src/app/dist /usr/src/app/dist

# Copy the prisma folder
COPY --from=build /usr/src/app/prisma /usr/src/app/prisma

# Generate the Prisma client
# This is required because prisma/cli somehow doesn't run prisma generate when it is supposed to
RUN npx prisma generate

# Expose the port the app will run on
EXPOSE 3000

# Command to run the app
CMD ["npm", "run", "start:prod"]
