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


    /**
     * Creates a new folder for the current user
     * If parentId is provided it creates a subfolder.
     *
     * @param name of the Folder
     * @param parentId Optional id for the parent folder
     * @return FolderDto with details of the created folder
     * @throws ResourceNotFoundException if parent folder doesn't exist
     * @throws UnauthorizedAccessException if user doesn't own parent folder
     * @throws FolderAlreadyExistsException if folder name already exists in location
     */
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

    /**
     * Retrieves folder content by the name of the folder and current user
     * @param folderName name of the folder to retrieve
     * @return FolderResponse containing folder and its files and subfolders
     * @throws ResourceNotFoundException if folder not found
     */
    public FolderResponse getFolderContentByName(String folderName) {
        UserModel currentUser = userService.getCurrentUser();

        return folderRepository.findByNameAndUser(folderName, currentUser)
                .map(FolderResponse::from)
                .orElseThrow(()-> new ResourceNotFoundException("Folder '" + folderName + "'not found"));
    }

    /**
     * Gets all folder belonging to the current user
     * @return List of folderDTO representing the users folder
     */
    public  List<FolderDTO> getMyFolders() {
        UserModel currentUser = userService.getCurrentUser();
        return folderRepository.findAllByUserId(currentUser.getId()).stream()
                .map(FolderDTO::from)
                .toList();
    }

    /**
     * Renames an existing folder
     * @param currentName The name of the current folder
     * @param newName New name of the folder
     * @return FolderDTO for the updated folder info
     * @throws ResourceNotFoundException if the folder can't be found
     */
    public FolderDTO renameFolder(String currentName, String newName) {

        UserModel currentUser = userService.getCurrentUser();

        FolderModel folder = folderRepository.findByNameAndUser(currentName, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " + currentName));

        log.info("Renaming folder from '{}' to '{}' for user '{}'", currentName, newName, currentUser.getUsername());

        folder.setName(newName);
        folderRepository.save(folder);

        return FolderDTO.from(folder);
    }

    /**
     * Deletes a folder and all it's content
     * @param folderName the target folder to be deleted
     * @throws ResourceNotFoundException if folder not found
     */
    public void DeleteFolder(String folderName) {
        UserModel currentUser = userService.getCurrentUser();

        FolderModel folder = folderRepository.findByNameAndUser(folderName, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " + folderName));

        folderRepository.delete(folder);
        log.info("Deleted folder '{}' from user '{}'", folderName, currentUser.getUsername());
    }
}