package assignment.gdrive.Security;

import assignment.gdrive.models.UserModel;
import assignment.gdrive.repositories.IUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final IUserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException{


        if (!(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong");
            return;
        }

        Object id = oAuth2User.getAttribute("id");

        if (id == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not find id");
            return;
        }
        String githubId = id.toString();

        UserModel user = userRepository.findByGithubId(githubId).orElse(null);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "User not found");
            return;
        }

        String token = jwtService.generateToken(user.getId());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Login via GitHub succeeded!");
        responseBody.put("username", user.getUsername());
        responseBody.put("token", token);

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));


    }
    }


