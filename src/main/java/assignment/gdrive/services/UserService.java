package assignment.gdrive.services;

import assignment.gdrive.Exceptions.ResourceNotFoundException;
import assignment.gdrive.Exceptions.UnauthorizedAccessException;
import assignment.gdrive.Exceptions.UserAlreadyExistsException;
import assignment.gdrive.Security.JwtService;
import assignment.gdrive.repositories.IUserRepository;
import assignment.gdrive.models.UserModel;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository IUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserModel registerUser(String username, String rawPassword) {
        if (IUserRepository.existsByUsername(username)) {
          log.warn("Registration failed! Username {} is already in use", username);
          throw new UserAlreadyExistsException("username " + username + "is already taken");
        }

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        log.info("New user created: {} ", username);
        return IUserRepository.save(user);

    }

    public UserModel updateUser(String currentUsername, String newUsername, String rawPassword) {
        UserModel user = IUserRepository.findByUsername(currentUsername)
                .orElseThrow(() -> {
                    log.error("Update failed. Could not find user: '{}' ", currentUsername);
                    return new ResourceNotFoundException("User not found: " + currentUsername);
                });

        if (!currentUsername.equals(newUsername)) {
            if (IUserRepository.existsByUsername(newUsername)) {
                log.warn("Naming failed: '{}' already exists", newUsername);
                throw new UserAlreadyExistsException("The username: " + newUsername + " is already taken");
            }
            user.setUsername(newUsername);
        }

        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        log.info("User '{}' was successfully updated", user.getUsername());
        return IUserRepository.save(user);
    }



    public void deleteUser(String username) {
        UserModel user = IUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User '" + username + "' does not exist"));

        IUserRepository.delete(user);
        log.info("User '{}' removed", username);
    }

    public String login(String username, String password){
        UserModel user = IUserRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())){
            log.warn("Login failed. Wrong password for user: '{}'", username);
            throw new UnauthorizedAccessException("Invalid username or password");
        }
        log.info("User '{}' logged in successfully", username);
        return jwtService.generateToken(user.getId());


    }

    public UserModel getCurrentUser() {
        var authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("You must be logged in to do this");
        }
        return (UserModel) authentication.getPrincipal();
    }
}







