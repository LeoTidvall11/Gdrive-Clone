package assignment.gdrive.assemblers;

import assignment.gdrive.controllers.FolderController;
import assignment.gdrive.dtos.FolderDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FolderModelAssembler implements RepresentationModelAssembler<FolderDTO, EntityModel<FolderDTO>> {

    @Override
    public EntityModel<FolderDTO> toModel(FolderDTO folder) {
        return EntityModel.of(folder,
                linkTo(methodOn(FolderController.class).getFolderContent(folder.name())).withRel("contents"),
                linkTo(methodOn(FolderController.class).rename(folder.name(),"")).withRel("rename"),
                linkTo(methodOn(FolderController.class).deleteFolder(folder.name())).withRel("delete"),
                linkTo(methodOn(FolderController.class).getAllFolders()).withRel("all-folders")
                );
    }
}
