package com.omniversity.server.user.ProspectiveUser;

import com.omniversity.server.user.ProspectiveUser.dto.request.DesiredUniversityUpdateDto;
import com.omniversity.server.user.ProspectiveUser.dto.request.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.user.exception.UserAlreadyExistsException;
import com.omniversity.server.user.exception.WeakPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/prospective")
public class ProspectiveUserController {
    private final ProspectiveUserService prospectiveUserService;

    @Autowired
    public ProspectiveUserController(ProspectiveUserService prospectiveUserService) {
        this.prospectiveUserService = prospectiveUserService;
    }

    @PostMapping("/register")
    ResponseEntity registerProspectiveUser(@RequestBody ProspectiveUserRegistrationDto dto) {
        try {
            return new ResponseEntity<>(this.prospectiveUserService.registerProspectiveUser(dto), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (WeakPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}/desiredUni")
    ResponseEntity updateDesiredUniversity(@PathVariable Long id, @RequestBody DesiredUniversityUpdateDto dto) {
        try {
            return new ResponseEntity<>(this.prospectiveUserService.updateDesiredExchangeUniversity(dto, id), HttpStatus.OK);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
