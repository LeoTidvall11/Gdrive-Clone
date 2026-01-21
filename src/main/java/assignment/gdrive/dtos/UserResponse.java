package assignment.gdrive.dtos;


import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String message
)
{

}
