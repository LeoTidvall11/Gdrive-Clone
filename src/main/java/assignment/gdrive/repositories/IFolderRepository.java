package assignment.gdrive.repositories;

import assignment.gdrive.models.FolderModel;
import assignment.gdrive.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IFolderRepository extends JpaRepository<FolderModel, UUID> {
    List<FolderModel> findAllByUserId(UUID userId);

    boolean existsByNameAndUserAndParentFolder(String name, UserModel user, FolderModel parentFolder);
    boolean existsByNameAndUserAndParentFolderIsNull(String name, UserModel user);



}
