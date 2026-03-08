package assignment.gdrive.assemblers;

import assignment.gdrive.controllers.FileController;
import assignment.gdrive.dtos.FileDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class FileModelAssembler implements RepresentationModelAssembler<FileDTO, EntityModel<FileDTO>> {

    @Override
    public EntityModel<FileDTO> toModel(FileDTO file) {

        return EntityModel.of(file,
                linkTo(methodOn(FileController.class).download(file.folderName(), file.name())).withRel("download"),
                linkTo(methodOn(FileController.class).delete(file.folderName(), file.name())).withRel("delete"),
                linkTo(methodOn(FileController.class).rename(file.folderName(), file.name(), "")).withRel("rename"),
                linkTo(methodOn(FileController.class).getAllMyFiles()).withRel("all_my_files")
        );
    }
}