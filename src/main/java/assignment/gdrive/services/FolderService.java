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
        return new FolderDTO(folder.getId(), folder.getName());

    }

    public FolderResponse getFolderContent(UUID folderId) {
        FolderModel folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));

        List<FolderResponse.SubFolderInfo> subFolders = folder.getSubFolders().stream()
                .map(f -> new FolderResponse.SubFolderInfo(f.getId(), f.getName()))
                .toList();

        List<FolderResponse.FileInfo> files = folder.getFiles().stream()
                .map(file -> new FolderResponse.FileInfo(file.getId(), file.getName()))
                .toList();

        return new FolderResponse(folder.getName(), subFolders, files);
    }

    public List<FolderDTO> getAllFolders(UUID userId) {
        return folderRepository.findAllByUserId(userId).stream()
                .map(f -> new FolderDTO(f.getId(), f.getName()))
                .toList();
    }

    public FolderDTO renameFolder(UUID folderId, String newName) {
        FolderModel folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));

        folder.setName(newName);
        folderRepository.save(folder);

        log.info("Renamed folder to '{}'", newName);
        return new FolderDTO(folder.getId(), folder.getName());
    }

    public FolderModel getFolderById(UUID id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
    }

}