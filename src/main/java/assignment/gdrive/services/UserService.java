package assignment.gdrive.services;

import assignment.gdrive.repositories.IUserRepository;
import assignment.gdrive.models.UserModel;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository IUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserModel registerUser(String name, String rawPassword) {
        if (IUserRepository.findByName(name).isPresent()) {
            throw new RuntimeException("User with name " + name + " already exists");
        }

        UserModel user = new UserModel();

        user.setName(name);

        String hashedPw = passwordEncoder.encode(rawPassword);

        user.setPasswordHash(hashedPw);

        IUserRepository.save(user);
        return user;

    }

    public UserModel updateUser(String currentUsername, String newUsername, String rawPassword) {
        UserModel user = IUserRepository.findByName(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));

        if (!currentUsername.equals(newUsername)) {
            if (IUserRepository.findByName(newUsername).isPresent()) {
                throw new RuntimeException("The username " + newUsername + " is already taken");
            }
            user.setName(newUsername);
        }

        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        return IUserRepository.save(user);
    }


    public void deleteUser(String name) {
        {
            UserModel user = IUserRepository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("User with name " + name + " does not exist"));

            IUserRepository.delete(user);
        }
    }

}





