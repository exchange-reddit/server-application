import { ValidatorConstraintInterface, ValidationArguments } from 'class-validator';
export declare class PasswordStrengthValidator implements ValidatorConstraintInterface {
    validate(password: string, args: ValidationArguments): boolean;
    defaultMessage(args: ValidationArguments): string;
}
