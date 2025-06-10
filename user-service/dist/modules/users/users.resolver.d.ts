import { UsersService } from './users.service';
import { UpdateUserInput } from './dto/update-user.input';
export declare class UsersResolver {
    private readonly usersService;
    constructor(usersService: UsersService);
    createUser(): string;
    findAll(): string;
    findOne(id: number): string;
    updateUser(updateUserInput: UpdateUserInput): string;
    removeUser(id: number): string;
}
