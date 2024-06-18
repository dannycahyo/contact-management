package danny_dwi_cahyono.contact_management.repository;

import danny_dwi_cahyono.contact_management.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

}
