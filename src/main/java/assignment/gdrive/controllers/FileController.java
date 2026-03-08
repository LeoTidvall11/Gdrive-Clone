package assignment.gdrive.controllers;

import assignment.gdrive.assemblers.FileModelAssembler;
import assignment.gdrive.dtos.FileDTO;
import assignment.gdrive.models.FileModel;
import assignment.gdrive.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FileController {


    private final FileService fileService;
    private final FileModelAssembler fileAssembler;

    @PostMapping("/upload")
    public ResponseEntity<EntityModel<FileDTO>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folderName") String folderName) throws IOException {
        FileDTO savedFile = fileService.saveFile(file, folderName);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileAssembler.toModel(savedFile));
    }

    @GetMapping("/download/{folderName}/{fileName}")
    public ResponseEntity<byte[]> download(
            @PathVariable String folderName,
            @PathVariable String fileName)
    {
        FileModel file = fileService.downloadFile(folderName, fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file.getContent());
    }

    @DeleteMapping("/delete/{folderName}/{fileName}")
    public ResponseEntity<Void> delete(
            @PathVariable String folderName,
            @PathVariable String fileName) {
        fileService.deleteFile(folderName, fileName);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/rename/{folderName}/{fileName}")
    public ResponseEntity<EntityModel<FileDTO>> rename(
             @PathVariable String folderName,
             @PathVariable String fileName,
             @RequestParam String newName) {
        FileDTO updatedFile = fileService.renameFile(folderName, fileName, newName);
        return ResponseEntity.ok(fileAssembler.toModel(updatedFile));
    }

    @GetMapping("/in/{folderName}")
    public ResponseEntity<CollectionModel<EntityModel<FileDTO>>> findAllByFolder(@PathVariable String folderName) {
        List<FileDTO> files = fileService.findAllByFolder(folderName);

        CollectionModel<EntityModel<FileDTO>> model = fileAssembler.toCollectionModel(files);
        model.add(linkTo(methodOn(FileController.class).findAllByFolder(folderName)).withSelfRel());

        return ResponseEntity.ok(model);
    }
    @GetMapping("/my-files")
    public ResponseEntity<CollectionModel<EntityModel<FileDTO>>> getAllMyFiles() {
       List<FileDTO> files = fileService.findAllByUser();

       CollectionModel<EntityModel<FileDTO>> model = fileAssembler.toCollectionModel(files);
       model.add(linkTo(methodOn(FileController.class).getAllMyFiles()).withSelfRel());

        return ResponseEntity.ok(model);    }

}
