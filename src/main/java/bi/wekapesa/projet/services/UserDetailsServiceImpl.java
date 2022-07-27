package bi.wekapesa.projet.services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bi.wekapesa.projet.entities.AppUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private UserService userService;

	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = userService.loadUserByUsername(username);
		if(appUser == null) {
			throw new UsernameNotFoundException(" ======= L'utilisateur n'existe pas dans la base des donnees");

		}else {
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			appUser.getAppRoles().forEach(role -> {
				authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
			});
			
			return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), authorities);
		}
		
		
	}

}
