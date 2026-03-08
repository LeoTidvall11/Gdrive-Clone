package assignment.gdrive.assemblers;

import assignment.gdrive.controllers.FileController;
import assignment.gdrive.controllers.FolderController;
import assignment.gdrive.dtos.FolderResponse;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@Component
public class FolderResponseAssembler implements RepresentationModelAssembler <FolderResponse, EntityModel<FolderResponse>> {
    @Override
    @SneakyThrows
    public EntityModel<FolderResponse> toModel(FolderResponse response) {
        String name = response.folderName();

        return EntityModel.of(response,
                linkTo(methodOn(FolderController.class).getFolderContent(name)).withSelfRel(),
                linkTo(methodOn(FileController.class).upload(null, name)).withRel("upload_file"),
                linkTo(methodOn(FolderController.class).getAllFolders()).withRel("all-folders")
                );
    }
}
