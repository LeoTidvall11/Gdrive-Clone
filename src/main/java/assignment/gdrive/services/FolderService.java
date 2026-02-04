package assignment.gdrive.services;

import assignment.gdrive.dtos.FolderDTO;
import assignment.gdrive.dtos.FolderResponse;
import assignment.gdrive.models.FolderModel;
import assignment.gdrive.models.UserModel;
import assignment.gdrive.repositories.IFolderRepository;
import assignment.gdrive.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final IFolderRepository IFolderRepository;
    private final IUserRepository IUserRepository;

    public FolderDTO createFolder(String folderName, UUID userId, UUID parentFolderId) {
        UserModel user = IUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FolderModel newFolder = new FolderModel();
        newFolder.setName(folderName);
        newFolder.setUser(user);

        if (parentFolderId != null) {
            FolderModel parent = IFolderRepository.findById(parentFolderId)
                    .orElseThrow(() -> new RuntimeException("Parent folder not found"));
            newFolder.setParentFolder(parent);
        }
        FolderModel saved = IFolderRepository.save(newFolder);
        return new FolderDTO(saved.getId(), saved.getName());
    }

    public FolderResponse getFolderContent (UUID folderId) {
        FolderModel folder = IFolderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder could not be found"));

        List<FolderResponse.SubFolderInfo> subFolders = folder.getSubFolders().stream()
                .map(f -> new FolderResponse.SubFolderInfo(f.getId(), f.getName()))
                .toList();

        List<FolderResponse.FileInfo> files = folder.getFiles().stream()
                .map(file -> new FolderResponse.FileInfo(file.getId(), file.getName()))
                .toList();

        return new FolderResponse(folder.getName(), subFolders, files);
    }

    public List<FolderDTO> getAllFolders(UUID userId) {
        List<FolderModel> folders = IFolderRepository.findAllByUserId(userId);

        return folders.stream()
                .map(f -> new FolderDTO(f.getId(), f.getName()))
                .toList();
    }

    public FolderDTO renameFolder(UUID folderId, String newName) {
        FolderModel folder = IFolderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder could not be found"));
                folder.setName(newName);
                IFolderRepository.save(folder);

        return new FolderDTO(folder.getId(), folder.getName());
    }

}

