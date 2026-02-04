package assignment.gdrive.services;

import assignment.gdrive.models.FileModel;
import assignment.gdrive.repositories.IFileRepository;
import assignment.gdrive.repositories.IFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final IFileRepository filesRepository;
    private final IFolderRepository IFolderRepository;

    public void save(MultipartFile file, UUID folderId) throws IOException {
        var folder = IFolderRepository.findById(folderId)
                .orElseThrow();


        FileModel newFile = new FileModel();
        newFile.setName(file.getOriginalFilename());
        newFile.setContent(file.getBytes());
        newFile.setFolder(folder);

        filesRepository.save(newFile);

    }


    public FileModel getFile(UUID fileId) {
        return filesRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File could not be found!"));
    }


    public void deleteFile(UUID fileId) {
        if (!filesRepository.existsById(fileId)) {
            throw new RuntimeException("Could not delete, file could not be found.");
        }
        filesRepository.deleteById(fileId);
    }

    public FileModel renameFile(UUID fileId, String newName) {
        FileModel file = filesRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File could not be found"));
        file.setName(newName);
        return filesRepository.save(file);
    }

}