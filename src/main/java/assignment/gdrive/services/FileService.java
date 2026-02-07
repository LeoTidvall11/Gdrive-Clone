package assignment.gdrive.services;

import assignment.gdrive.Exceptions.FileAlreadyExistsException;
import assignment.gdrive.Exceptions.ResourceNotFoundException;
import assignment.gdrive.dtos.FileDTO;
import assignment.gdrive.models.FileModel;
import assignment.gdrive.models.FolderModel;
import assignment.gdrive.models.UserModel;
import assignment.gdrive.repositories.IFileRepository;
import assignment.gdrive.repositories.IFolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final IFileRepository fileRepository;
    private final IFolderRepository folderRepository;
    private final UserService userService;

    public FileDTO saveFile(MultipartFile file, String folderName) throws IOException {
        UserModel currentUser = userService.getCurrentUser();

        FolderModel folder = folderRepository.findByNameAndUser(folderName, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Target folder not found" + folderName));

        String fileName = file.getOriginalFilename();

        if (fileRepository.existsByNameAndFolder(fileName, folder)) {
            log.warn("File upload failed: '{}' already exists in folder '{}'", fileName, folder.getName());
            throw new FileAlreadyExistsException("A file named '" + fileName + "' already exists in this folder.");
        }

        FileModel newFile = new FileModel();
        newFile.setName(fileName);
        newFile.setContent(file.getBytes());
        newFile.setFolder(folder);

        FileModel savedFile = fileRepository.save(newFile);
        log.info("Successfully uploaded file : '{}' to folder: '{}'", fileName, folder.getName());

        return FileDTO.from(savedFile);

    }

    public List<FileDTO> findAllByFolder(String folderName) {
        UserModel currentUser = userService.getCurrentUser();

        FolderModel folder = folderRepository.findByNameAndUser(folderName, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Target folder not found" + folderName));

        return fileRepository.findAllByFolder(folder).stream()
                .map(FileDTO::from)
                .toList();
    }

    public List <FileDTO> findAllByUser() {
        UserModel currentUser = userService.getCurrentUser();

        return fileRepository.findAllByFolder_User(currentUser)
                .stream()
                .map(FileDTO::from)
                .toList();


    }


    public FileModel downloadFile (String folderName, String fileName) {
        UserModel currentUser = userService.getCurrentUser();

        FolderModel folder = folderRepository.findByNameAndUser(folderName, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " +  folderName));

        return fileRepository.findByNameAndFolder(fileName, folder)
                .orElseThrow(() -> new ResourceNotFoundException("File: "+ fileName + " not found in folder: " + folderName));
    }


    public void deleteFile(String folderName, String fileName) {
        UserModel currentUser = userService.getCurrentUser();
        FolderModel folder = folderRepository.findByNameAndUser(folderName, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " +  folderName));

        FileModel file = fileRepository.findByNameAndFolder(fileName, folder)
                .orElseThrow(() -> new ResourceNotFoundException("File: "+ fileName + " not found in folder: " + folderName));
        fileRepository.delete(file);
        log.info("File: '{}' deleted by: '{}'", fileName, currentUser.getUsername());

        }


    public FileDTO renameFile (String folderName, String oldName, String newName) {
        UserModel currentUser = userService.getCurrentUser();

        FolderModel folder = folderRepository.findByNameAndUser(folderName, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " +  folderName));

        FileModel file = fileRepository.findByNameAndFolder(oldName, folder)
                .orElseThrow(() -> new ResourceNotFoundException("File: "+ oldName + " not found in folder: " + folderName));

        file.setName(newName);
        return FileDTO.from(fileRepository.save(file));
        }

}