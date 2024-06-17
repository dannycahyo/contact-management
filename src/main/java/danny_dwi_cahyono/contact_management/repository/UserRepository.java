package danny_dwi_cahyono.contact_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import danny_dwi_cahyono.contact_management.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
