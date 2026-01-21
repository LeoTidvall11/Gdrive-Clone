package assignment.gdrive.repositories;

import assignment.gdrive.models.FilesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FilesRepository extends JpaRepository<FilesModel, UUID> {

    List<FilesModel> findAllByFolderId(UUID folderId);
}
