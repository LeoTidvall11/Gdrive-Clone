package assignment.gdrive.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import assignment.gdrive.models.UserModel;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface IUserRepository extends JpaRepository<UserModel, UUID> {

    Optional <UserModel> findByUsername (String username);

    boolean existsByUsername(String username);

}
