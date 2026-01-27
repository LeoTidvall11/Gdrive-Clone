package assignment.gdrive.controllers;

import assignment.gdrive.dtos.FolderRequest;
import assignment.gdrive.models.FoldersModel;
import assignment.gdrive.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
