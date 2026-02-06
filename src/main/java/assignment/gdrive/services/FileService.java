package assignment.gdrive.services;

import assignment.gdrive.Exceptions.ResourceNotFoundException;
import assignment.gdrive.Exceptions.UnauthorizedAccessException;
import assignment.gdrive.models.FileModel;
import assignment.gdrive.models.UserModel;
import assignment.gdrive.repositories.IFileRepository;
import assignment.gdrive.repositories.IFolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final IFileRepository fileRepository;
    private final IFolderRepository folderRepository;
    private final UserService userService;

    public void save(MultipartFile file, UUID folderId) throws IOException {
        var folder = folderRepository.findById(folderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Target folder not found with id : " + folderId));

        String fileName = file.getOriginalFilename();
        if (fileRepository.existsByNameAndFolder(fileName, folder)) {
            log.warn("File upload failed: '{}' already exists in folder '{}'", fileName, folder.getName());
            throw new FileAlreadyExistsException("A file named '" + fileName + "' already exists in this folder.");
        }

        FileModel newFile = new FileModel();
        newFile.setName(fileName);
        newFile.setContent(file.getBytes());
        newFile.setFolder(folder);

        fileRepository.save(newFile);
        log.info("Successfully uploaded file : '{}' to folder: '{}'", fileName, folder.getName());

    }


    public FileModel getFile(UUID fileId) {
        FileModel file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));

        UserModel currentUser = userService.getCurrentUser();

        if (!file.getFolder().getUser().getId().equals(currentUser.getId())) {
            log.warn("Unauthorized access attempt: {} tried to access file {}", currentUser.getUsername(), fileId);
            throw new UnauthorizedAccessException("You do not have access to this file");
        }

        return file;
    }

    public void deleteFile(UUID fileId) {
            FileModel file = fileRepository.findById(fileId)
                    .orElseThrow(()-> new ResourceNotFoundException("Could not delete. File with that name could not be found!"));

            fileRepository.delete(file);
            log.info("File '{}' was deleted", file.getName());
        }


    public FileModel renameFile(UUID fileId, String newName) {
        FileModel file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Rename failed: File not found"));

        log.info("Renaming file '{}' to '{}'", file.getName(), newName);
        file.setName(newName);
        return fileRepository.save(file);
    }

}