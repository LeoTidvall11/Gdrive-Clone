package assignment.gdrive.Security;

import assignment.gdrive.models.UserModel;
import assignment.gdrive.repositories.IUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

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
    ) throws IOException, ServletException{
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Object id = oAuth2User.getAttribute("id");

        if (id == null) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "could not find id");
        return;
        }

        String githubId = id.toString();

        UserModel user = userRepository.findByGithubId(githubId)
                .orElseThrow(()-> new RuntimeException("user not found"));

        String token = jwtService.generateToken(user.getId());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Inloggning via GitHub lyckades!");
        responseBody.put("username", user.getUsername());
        responseBody.put("token", token);

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));


    }
    }


