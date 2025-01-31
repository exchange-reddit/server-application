# Use an official Node.js runtime as a parent image
FROM node:18-alpine

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json (or yarn.lock) to the container
COPY package*.json ./

# Install dependencies
RUN npm install --production

# Copy the rest of the application files into the container
COPY . .

# Build the application (optional, if you have a build step)
RUN npm run build

# Expose the port your application runs on (default is 3000)
EXPOSE 3000

# Command to run the application
CMD ["npm", "run", "start:prod"]
