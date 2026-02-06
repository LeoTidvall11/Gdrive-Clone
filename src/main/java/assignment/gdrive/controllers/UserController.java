package assignment.gdrive.controllers;

import assignment.gdrive.dtos.RegisterRequest;
import assignment.gdrive.dtos.UserResponse;
import assignment.gdrive.models.UserModel;
import assignment.gdrive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request){
        UserModel savedUser = userService.registerUser(request.username(),  request.password());

        UserResponse response = new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                "User created successfully"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody RegisterRequest request){
        String token = userService.login(request.username(), request.password());
        return  ResponseEntity.ok(token);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String username,
            @RequestBody RegisterRequest request)
    {
        UserModel updatedUser = userService.updateUser(username, request.username(), request.password());

        UserResponse response = new UserResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                "User profile updated successfully"
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity <UserResponse> deleteUser(@PathVariable String username){
        userService.deleteUser(username);
       return ResponseEntity.noContent().build();
    }


}
