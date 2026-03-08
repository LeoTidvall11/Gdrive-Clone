package assignment.gdrive.dtos;

import assignment.gdrive.models.FileModel;

import java.util.UUID;


public record FileDTO (UUID id, String name, String folderName, Long size) {

    public static FileDTO from(FileModel model) {
        long fileSize = (model.getContent() != null)
                ? model.getContent().length : 0;

        return new FileDTO(
                model.getId(),
                model.getName(),
                model.getFolder().getName(),
                fileSize
        );

    }
}
