package assignment.gdrive.controllers;

import assignment.gdrive.models.FileModel;
import assignment.gdrive.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("folderId") UUID folderId) throws IOException {

        fileService.save(file, folderId);
        return "File saved!";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable UUID fileId){
        FileModel file = fileService.getFile(fileId);

        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getName()
                                + "\"")
                .body(file.getContent());
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity <String> delete (@PathVariable UUID fileId){
        fileService.deleteFile(fileId);
        return ResponseEntity.ok("File deleted");
    }

    @PatchMapping("/{fileId}/rename")
    public ResponseEntity<FileModel> rename(@PathVariable UUID fileId, @RequestParam String newName) {
        return ResponseEntity.ok(fileService.renameFile(fileId, newName));

    }
}
