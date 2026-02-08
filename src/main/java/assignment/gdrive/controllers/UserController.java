package assignment.gdrive.controllers;

import assignment.gdrive.dtos.RegisterRequest;
import assignment.gdrive.dtos.UserResponse;
import assignment.gdrive.models.UserModel;
import assignment.gdrive.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        UserModel savedUser = userService.registerUser(request.username(),  request.password());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(savedUser.getId(),savedUser.getUsername(),"User created"));

    }

    @PostMapping("/login")
        public ResponseEntity<Map<String, String>> login (@RequestBody RegisterRequest request){
            String token = userService.login(request.username(), request.password());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@RequestBody RegisterRequest request) {
        UserModel updatedUser = userService.updateUser(request.username(), request.password());
        return ResponseEntity.ok(new UserResponse(updatedUser.getId(), updatedUser.getUsername(), "Profile updated"));
    }

    @DeleteMapping("/me")
    public ResponseEntity <Void> deleteMe() {
    userService.deleteCurrentUser();
    return ResponseEntity.noContent().build();
    }


}
