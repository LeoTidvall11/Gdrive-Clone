package assignment.gdrive.services;

import assignment.gdrive.models.FilesModel;
import assignment.gdrive.repositories.FilesRepository;
import assignment.gdrive.repositories.FoldersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilesService {

    private final FilesRepository filesRepository;
    private final FoldersRepository foldersRepository;

    public void save(MultipartFile file, UUID folderId) throws IOException {
        var folder = foldersRepository.findById(folderId)
                .orElseThrow();


        FilesModel newFile = new FilesModel();
        newFile.setName(file.getOriginalFilename());
        newFile.setContent(file.getBytes());
        newFile.setFolder(folder);

        filesRepository.save(newFile);

    }


    public FilesModel getFile(UUID fileId) {
        return filesRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File could not be found!"));
    }


    public void deleteFile(UUID fileId) {
        if (!filesRepository.existsById(fileId)) {
            throw new RuntimeException("Could not delete, file could not be found.");
        }
        filesRepository.deleteById(fileId);
    }

}