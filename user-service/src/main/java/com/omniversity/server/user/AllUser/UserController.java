package com.omniversity.server.user.AllUser;

import com.omniversity.server.log.LogService;
import com.omniversity.server.log.dto.accountDeleteLogDto;
import com.omniversity.server.log.dto.pwChangeLogDto;
import com.omniversity.server.user.AllUser.dto.request.ChangeNationalityDto;
import com.omniversity.server.user.AllUser.dto.request.ChangePasswordDto;
import com.omniversity.server.user.AllUser.dto.request.DeleteUserDto;
import com.omniversity.server.user.exception.ChangedPasswordSameException;
import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.user.exception.PassedGracePeriodException;
import com.omniversity.server.user.exception.WrongPasswordException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * TODO:
 * - Guard condition to ensure user verification prior to registration
 * - Authentication method integration
 * - Add user posts and communities under account
 * - Function to secure the grace period of 2 weeks
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final LogService logService;

    @Autowired
    public UserController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @GetMapping("/checkID/{id}")
    ResponseEntity checkUserIdTaken(@PathVariable String id) {
        try {
            return new ResponseEntity<Boolean>(this.userService.checkUserNameTaken(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    ResponseEntity getUser(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(this.userService.getUser(id), HttpStatus.OK);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/pwChange/{id}")
    ResponseEntity changePW(@PathVariable Long id, @RequestBody ChangePasswordDto changePasswordDto, HttpServletRequest request) {
        try {
            // Please check the method changePassword() for further information
            pwChangeLogDto dto = userService.changePassword(changePasswordDto, id);
            // Log pw change request information (The dto here is the result of the changePassword method, and the request is the entire request sent by the user)
            logService.logPWChange(dto, request);
            return ResponseEntity.noContent().build();
        } catch (WrongPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ChangedPasswordSameException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/nationalityChange/{id}")
    ResponseEntity changeNationality(@PathVariable Long id, @RequestBody ChangeNationalityDto dto) {
        try {
            userService.changeNationality(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Your nationality has been changed successfully");
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (PassedGracePeriodException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    ResponseEntity deleteUserAccount(@RequestBody DeleteUserDto deleteUserDto, HttpServletRequest request) {
        try {
            accountDeleteLogDto dto = this.userService.deleteUser(deleteUserDto);
            // Log account deletion information (The dto here is the result of the deleteUser method)
            logService.logAccountDeletion(dto, request);
            return ResponseEntity.status(HttpStatus.OK).body(String.format("Account ID %s has been terminated as of %s", dto.updateUser(), LocalDate.now()));
        } catch (WrongPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
