package assignment.gdrive.Repository;

import assignment.gdrive.models.FoldersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FoldersRepository extends JpaRepository<FoldersModel, UUID> {
    List<FoldersModel> findAllByUserId(UUID userId);
}
