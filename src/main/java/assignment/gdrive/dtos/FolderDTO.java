package assignment.gdrive.dtos;

import assignment.gdrive.models.FolderModel;

import java.util.UUID;

public record FolderDTO (
        UUID id, String name
){
    public static FolderDTO from(FolderModel model) {
        return new FolderDTO(model.getId(), model.getName());
    }
}

