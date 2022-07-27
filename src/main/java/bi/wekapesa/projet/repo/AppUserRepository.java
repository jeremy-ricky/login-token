package bi.wekapesa.projet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import bi.wekapesa.projet.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	
	AppUser findByUsername(String username);
	
}
