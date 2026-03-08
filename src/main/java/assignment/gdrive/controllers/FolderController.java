package assignment.gdrive.controllers;

import assignment.gdrive.assemblers.FolderModelAssembler;
import assignment.gdrive.assemblers.FolderResponseAssembler;
import assignment.gdrive.dtos.FolderDTO;
import assignment.gdrive.dtos.FolderRequest;
import assignment.gdrive.dtos.FolderResponse;
import assignment.gdrive.services.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("api/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;
    private final FolderModelAssembler folderAssembler;
    private final FolderResponseAssembler folderResponseAssembler;

    @PostMapping("/create")
    public ResponseEntity<EntityModel<FolderDTO>> createFolder(@Valid @RequestBody FolderRequest folderRequest){
       FolderDTO savedFolder = folderService.createFolder(
               folderRequest.name(),
               folderRequest.parentId());
       return ResponseEntity.status(HttpStatus.CREATED).body(folderAssembler.toModel(savedFolder));
    }

    @GetMapping("/{folderName}/content")
    public ResponseEntity<EntityModel<FolderResponse>> getFolderContent(@PathVariable String folderName) {
        FolderResponse content = folderService.getFolderContentByName(folderName);

        return ResponseEntity.ok(folderResponseAssembler.toModel(content));
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<FolderDTO>>> getAllFolders() {
        List<FolderDTO> folders = folderService.getMyFolders();
        CollectionModel<EntityModel<FolderDTO>> model = folderAssembler.toCollectionModel(folders);
        model.add(linkTo(methodOn(FolderController.class).getAllFolders()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @PatchMapping("/{folderName}/rename")
    public ResponseEntity<EntityModel<FolderDTO>> rename(@PathVariable String folderName, @RequestParam String newName) {
        FolderDTO updatedFolder = folderService.renameFolder(folderName, newName);
        return ResponseEntity.ok(folderAssembler.toModel(updatedFolder));
    }

    @DeleteMapping("/{folderName}")
    public ResponseEntity<Void> deleteFolder(@PathVariable String folderName) {
        folderService.deleteFolder(folderName);
        return ResponseEntity.noContent().build();
    }

}
