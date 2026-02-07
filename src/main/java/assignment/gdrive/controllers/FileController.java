package assignment.gdrive.controllers;

import assignment.gdrive.dtos.FileDTO;
import assignment.gdrive.models.FileModel;
import assignment.gdrive.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileDTO> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folderName") String folderName) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.saveFile(file, folderName));
    }

    @GetMapping("/download/{folderName}/{fileName}")
    public ResponseEntity<byte[]> download(
            @PathVariable String folderName,
            @PathVariable String fileName
    )
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
    public ResponseEntity<FileDTO> rename
            (@PathVariable String folderName,
             @PathVariable String fileName,
             @RequestParam String newName) {
        return ResponseEntity.ok(fileService.renameFile(folderName, fileName, newName));
    }

    @GetMapping("/in/{folderName}")
    public ResponseEntity<List<FileDTO>> findAllByFolder(@PathVariable String folderName) {
        return ResponseEntity.ok(fileService.findAllByFolder(folderName));
    }
    @GetMapping("/my-files")
    public ResponseEntity<List<FileDTO>> getAllMyFiles() {
        return ResponseEntity.ok(fileService.findAllByUser());
    }

}
