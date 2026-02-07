package assignment.gdrive.repositories;

import assignment.gdrive.models.FileModel;
import assignment.gdrive.models.FolderModel;
import assignment.gdrive.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IFileRepository extends JpaRepository<FileModel, UUID> {

    List <FileModel> findAllByFolder(FolderModel folder);
    List<FileModel> findAllByFolder_User(UserModel user);
    Optional<FileModel> findByNameAndFolder(String name, FolderModel folder);
    boolean existsByNameAndFolder (String name, FolderModel folder);
}
