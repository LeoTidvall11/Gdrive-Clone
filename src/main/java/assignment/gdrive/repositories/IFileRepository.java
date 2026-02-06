package assignment.gdrive.repositories;

import assignment.gdrive.models.FileModel;
import assignment.gdrive.models.FolderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IFileRepository extends JpaRepository<FileModel, UUID> {

    List<FileModel> findAllByFolderId(UUID folderId);

    boolean existsByNameAndFolder (String name, FolderModel folder);
}
