package danny_dwi_cahyono.contact_management.repository;

import danny_dwi_cahyono.contact_management.entity.Address;
import danny_dwi_cahyono.contact_management.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findAllByContact(Contact contact);

    Optional<Address> findFirstByContactAndId(Contact contact, String addressId);
}
