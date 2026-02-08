package assignment.gdrive.controllers;

import assignment.gdrive.dtos.FolderDTO;
import assignment.gdrive.dtos.FolderRequest;
import assignment.gdrive.dtos.FolderResponse;
import assignment.gdrive.services.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping("/create")
    public ResponseEntity<FolderDTO> createFolder(@Valid @RequestBody FolderRequest folderRequest){
       FolderDTO savedFolder = folderService.createFolder(
               folderRequest.name(),
               folderRequest.parentId());
       return ResponseEntity.status(HttpStatus.CREATED).body(savedFolder);
    }

    @GetMapping("/{folderName}/content")
    public ResponseEntity<FolderResponse> getFolderContent(@PathVariable String folderName) {
    return ResponseEntity.ok(folderService.getFolderContentByName(folderName));
    }

    @GetMapping("/all")
    public ResponseEntity<List<FolderDTO>> getAllFolders() {
        return ResponseEntity.ok(folderService.getMyFolders());
    }

    @PatchMapping("/{folderName}/rename")
    public ResponseEntity<FolderDTO> rename(@PathVariable String folderName, @RequestParam String newName) {
        return ResponseEntity.ok(folderService.renameFolder(folderName, newName));
    }

    @DeleteMapping("/{folderName}")
    public ResponseEntity<FolderDTO> deleteFolder(@PathVariable String folderName) {
        folderService.deleteFolder(folderName);
        return ResponseEntity.noContent().build();
    }

}
