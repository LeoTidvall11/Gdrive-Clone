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

    /**
     * Registers a new user with a hashed password
     * @param username the desired username
     * @param rawPassword the desired password that's going to be hashed
     * @return UserModel of the newly created user
     * @throws UserAlreadyExistsException if there already is a user with that name
     */
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

    /**
     * Updates user information
     * @param newUsername new username
     * @param rawPassword new password
     * @return UserModel with updated information
     * @throws UserAlreadyExistsException if the new username is already taken
     */
    public UserModel updateUser(String newUsername, String rawPassword) {
        UserModel user = getCurrentUser();
                String currentUsername = user.getUsername();

        if (!currentUsername.equals(newUsername)) {
            if (IUserRepository.existsByUsername(newUsername)) {
                log.warn("Update failed: Username '{}' already taken", newUsername);
                throw new UserAlreadyExistsException("The username: " + newUsername + " is already taken");
            }
            user.setUsername(newUsername);
        }

        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        log.info("User '{}' (ID: {}) updated their profile", currentUsername, user.getId());
        return IUserRepository.save(user);
    }


    /**
     * Deletes the current user and all their data
     */
    public void deleteCurrentUser() {
        UserModel user = getCurrentUser();
        IUserRepository.delete(user);

        log.info("User: '{}' and all their data has been deleted", user.getUsername());

    }

    /**
     * Authenticates a user and creates a jwt token
     * @param username user's username
     * @param password user's password
     * @return Jwt token string
     * @throws ResourceNotFoundException if user not found
     * @throws UnauthorizedAccessException if password incorrect
     */
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

    /**
     * Retrieves the current user
     * @return UserModel of the current user
     * @throws UnauthorizedAccessException if the user isn't authenticated
     */
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







