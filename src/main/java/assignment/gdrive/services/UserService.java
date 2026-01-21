package assignment.gdrive.services;

import assignment.gdrive.repositories.UserRepository;
import assignment.gdrive.models.UserModel;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserModel registerUser(String name, String rawPassword) {
        //felhantering för om användarnamnet redan är använt
        if (userRepository.findByName(name).isPresent()) {
            throw new RuntimeException("User with name " + name + " already exists");
        }

        //Skapar ny user model
        UserModel user = new UserModel();
        //Sätter namnet
        user.setName(name);

        //Hashar och sparar lösenord.
        String hashedPw = passwordEncoder.encode(rawPassword);
        user.setPasswordHash(hashedPw);

        //Sparar användaren
        userRepository.save(user);
        return user;

    }

    }


