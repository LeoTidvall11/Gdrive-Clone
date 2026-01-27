package assignment.gdrive.dtos;

import java.util.UUID;

public record FolderRequest(
        String name,
        UUID userId,
        UUID parentId) {
}
