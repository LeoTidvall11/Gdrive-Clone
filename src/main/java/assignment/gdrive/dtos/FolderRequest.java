package assignment.gdrive.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record FolderRequest(
        @NotBlank(message = "Folder name can't be empty")
        @Size(max = 255, message = "Folder name can't be empty")
        String name,
        UUID parentId) {
}
