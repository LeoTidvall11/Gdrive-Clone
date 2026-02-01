package assignment.gdrive.services;

import assignment.gdrive.dtos.FolderResponse;
import assignment.gdrive.models.FoldersModel;
import assignment.gdrive.models.UserModel;
import assignment.gdrive.repositories.FoldersRepository;
import assignment.gdrive.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FoldersRepository foldersRepository;
    private final UserRepository userRepository;

    public FoldersModel createFolder(String folderName, UUID userId, UUID parentFolderId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FoldersModel newFolder = new FoldersModel();
        newFolder.setName(folderName);
        newFolder.setUser(user);

        if (parentFolderId != null) {
            FoldersModel parent = foldersRepository.findById(parentFolderId)
                    .orElseThrow(() -> new RuntimeException("Parent folder not found"));
            newFolder.setParentFolder(parent);
        }
        return foldersRepository.save(newFolder);
    }

    public FolderResponse folderContent (UUID folderId) {
        FoldersModel folder = foldersRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder could not be found"));
        List<FolderResponse.SubFolderInfo> subFolders = folder.getSubFolders().stream()
                .map(f -> new FolderResponse.SubFolderInfo(f.getId(), f.getName()))
                .toList();

        List<FolderResponse.FileInfo> files = folder.getFiles().stream()
                .map(file -> new FolderResponse.FileInfo(file.getId(), file.getName()))
                .toList();

        return new FolderResponse(folder.getName(), subFolders, files);
    }

    public List<FoldersModel> getAllUserFolders(UUID userId) {
        return foldersRepository.findAllByUserId(userId);
    }
    }

