/**
 * Purpose:
 * Validate the strength of the password that the user has chosen.
 */
import { HttpException, HttpStatus } from '@nestjs/common';
import {
  ValidatorConstraint,
  ValidatorConstraintInterface,
  ValidationArguments,
} from 'class-validator';

@ValidatorConstraint({ name: 'PasswordStrength', async: false })
export class PasswordStrengthValidator implements ValidatorConstraintInterface {
  validate(password: string, args: ValidationArguments) {
    // Test to see if the password contains at least one uppercase, lowercase, number, special character, and at least have 8 charaters
    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    const isValid = passwordRegex.test(password);

    if (!isValid) {
      throw new HttpException(
        {
          statusCode: HttpStatus.UNPROCESSABLE_ENTITY,
          message:
            'Password must contain at least 8 characters, including an uppercase, lowercase, number, and special character.',
          error: 'WeakPassword',
        },
        HttpStatus.UNPROCESSABLE_ENTITY,
      );
    }

    return true;
  }

  defaultMessage(args: ValidationArguments) {
    return 'Password must contain at least 8 characters, including an uppercase, lowercase, number, and special character.';
  }
}
