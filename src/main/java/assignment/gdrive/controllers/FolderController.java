package assignment.gdrive.controllers;

import assignment.gdrive.dtos.FolderRequest;
import assignment.gdrive.dtos.FolderResponse;
import assignment.gdrive.models.FoldersModel;
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
    public ResponseEntity<FoldersModel> createFolder(@RequestBody FolderRequest folderRequest){
        FoldersModel savedFolder = folderService.createFolder(
                folderRequest.name(),
                folderRequest.userId(),
                folderRequest.parentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFolder);


    }

    @GetMapping("/{folderId}/content")
    public ResponseEntity<FolderResponse> getFolderContent(@PathVariable UUID folderId) {
        FolderResponse content = folderService.folderContent(folderId);

        return ResponseEntity.ok(content);
    }

    @GetMapping("/{userId}/folders")
    public ResponseEntity<List<FoldersModel>> getAllFolders (@PathVariable UUID userId){
        return ResponseEntity.ok(folderService.getAllUserFolders(userId));
    }

}
