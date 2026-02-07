package assignment.gdrive.services;

import assignment.gdrive.Exceptions.FolderAlreadyExistsException;
import assignment.gdrive.Exceptions.ResourceNotFoundException;
import assignment.gdrive.Exceptions.UnauthorizedAccessException;
import assignment.gdrive.dtos.FolderDTO;
import assignment.gdrive.dtos.FolderResponse;
import assignment.gdrive.models.FolderModel;
import assignment.gdrive.models.UserModel;
import assignment.gdrive.repositories.IFolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolderService {

    private final IFolderRepository folderRepository;
    private final UserService userService;

    public FolderDTO createFolder(String name, UUID parentId) {
        UserModel currentUser = userService.getCurrentUser();

        FolderModel parent = null;
        if (parentId != null) {
            parent = folderRepository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent folder not found"));

            if (!parent.getUser().getId().equals(currentUser.getId())){
                log.warn("Security alert. User: '{}' tried to create a folder in someone else folder", currentUser.getUsername());
                throw new UnauthorizedAccessException("You do not have permission to modify this folder");
            }

        }
        boolean exists = (parent == null)
                ? folderRepository.existsByNameAndUserAndParentFolderIsNull(name, currentUser)
                : folderRepository.existsByNameAndUserAndParentFolder(name, currentUser, parent);
        if (exists) {
            log.warn("Folder conflict: '{}' already exists for user {}", name, currentUser.getUsername());
            throw new FolderAlreadyExistsException("Folder '" + name + "' already exists here.");
        }
        FolderModel folder = new FolderModel();
        folder.setName(name);
        folder.setUser(currentUser);
        folder.setParentFolder(parent);
        folderRepository.save(folder);

        log.info("Created folder '{}' for user '{}'", name, currentUser.getUsername());
        return FolderDTO.from(folder);

    }

    public FolderResponse getFolderContentByName(String folderName) {
        UserModel currentUser = userService.getCurrentUser();

        return folderRepository.findByNameAndUser(folderName, currentUser)
                .map(FolderResponse::from)
                .orElseThrow(()-> new ResourceNotFoundException("Folder '" + folderName + "'not found"));
    }

    public  List<FolderDTO> getMyFolders() {
        UserModel currentUser = userService.getCurrentUser();
        return folderRepository.findAllByUserId(currentUser.getId()).stream()
                .map(FolderDTO::from)
                .toList();
    }

    public FolderDTO renameFolder(String currentName, String newName) {

        UserModel currentUser = userService.getCurrentUser();

        FolderModel folder = folderRepository.findByNameAndUser(currentName, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " + currentName));

        log.info("Renaming folder from '{}' to '{}' for user '{}'", currentName, newName, currentUser.getUsername());

        folder.setName(newName);
        folderRepository.save(folder);

        return FolderDTO.from(folder);
    }

    public FolderModel getFolderById(UUID id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
    }



}