package bi.wekapesa.projet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import bi.wekapesa.projet.entities.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
	
	AppRole findByRoleName(String roleName);
	
}
