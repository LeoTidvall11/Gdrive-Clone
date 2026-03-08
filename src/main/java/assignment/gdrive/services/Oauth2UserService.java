package assignment.gdrive.services;

import assignment.gdrive.models.UserModel;
import assignment.gdrive.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final IUserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oauth2user = super.loadUser(userRequest);
        Object id = oauth2user.getAttributes().get("id");
        Object login =  oauth2user.getAttributes().get("login");

        if (id == null || login == null) {
            throw new OAuth2AuthenticationException("Authentication failed");
        }
        String githubId = id.toString();
        String name = login.toString();

        Optional<UserModel> user = userRepository.findByGithubId(githubId);

        if (user.isEmpty()) {
            UserModel newUser = new UserModel();
            String finalName = name;

            if (userRepository.existsByUsername(name)) {
                finalName = name + "_" + githubId;
            }
            newUser.setUsername(finalName);
            newUser.setGithubId(githubId);
            userRepository.save(newUser);
        }
        return oauth2user;
        }

    }

