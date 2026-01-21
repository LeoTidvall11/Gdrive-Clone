package assignment.gdrive.controllers;

import assignment.gdrive.dtos.RegisterRequest;
import assignment.gdrive.dtos.UserResponse;
import assignment.gdrive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor//Magi från lombok
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request){
        userService.registerUser(request.username(), request.password());

        UserResponse response = new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                "Användarkonto har skapats framgångsrikt"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


}
