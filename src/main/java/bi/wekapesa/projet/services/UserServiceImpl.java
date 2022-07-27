package bi.wekapesa.projet.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bi.wekapesa.projet.entities.AppRole;
import bi.wekapesa.projet.entities.AppUser;
import bi.wekapesa.projet.repo.AppRoleRepository;
import bi.wekapesa.projet.repo.AppUserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
		super();
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AppUser saveUser(AppUser appUser) {
		appUser.setStatut(false);
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
		return appUserRepository.save(appUser);
	}

	@Override
	public AppRole saveRole(AppRole appRole) {
		return appRoleRepository.save(appRole);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser appUser = appUserRepository.findByUsername(username);
		AppRole appRole = appRoleRepository.findByRoleName(roleName);
		appUser.getAppRoles().add(appRole);
		
	}

	@Override
	public AppUser loadUserByUsername(String username) {
		return appUserRepository.findByUsername(username);
	}

	@Override
	public List<AppUser> listUsers() {
		return appUserRepository.findAll();
	}

	

}
