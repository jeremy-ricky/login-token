package bi.wekapesa.projet.services;

import java.util.List;

import bi.wekapesa.projet.entities.AppRole;
import bi.wekapesa.projet.entities.AppUser;

public interface UserService {
	
	AppUser saveUser(AppUser appUser);
	AppRole saveRole(AppRole appRole);
	void addRoleToUser(String username, String roleName);
	AppUser loadUserByUsername(String username);
	List<AppUser> listUsers();
	

}