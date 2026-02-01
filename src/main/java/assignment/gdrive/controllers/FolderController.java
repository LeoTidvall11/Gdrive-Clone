package assignment.gdrive.controllers;

import assignment.gdrive.dtos.FolderDTO;
import assignment.gdrive.dtos.FolderRequest;
import assignment.gdrive.dtos.FolderResponse;
import assignment.gdrive.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping("/create")
    public ResponseEntity<FolderDTO> createFolder(@RequestBody FolderRequest folderRequest){
        FolderDTO savedFolder = folderService.createFolder(
                folderRequest.name(),
                folderRequest.userId(),
                folderRequest.parentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFolder);


    }

    @GetMapping("/{folderId}/content")
    public ResponseEntity<FolderResponse> getFolderContent(@PathVariable UUID folderId) {
        FolderResponse content = folderService.getFolderContent(folderId);

        return ResponseEntity.ok(content);
    }

    @GetMapping("/{userId}/folders")
    public ResponseEntity<List<FolderDTO>> getAllFolders(@PathVariable UUID userId) {
        return ResponseEntity.ok(folderService.getAllFolders(userId));
    }

    @PatchMapping("/{folderId}/rename")
    public ResponseEntity<FolderDTO> rename(@PathVariable UUID folderId, @RequestParam String newName) {
        return ResponseEntity.ok(folderService.renameFolder(folderId, newName));
    }

}
