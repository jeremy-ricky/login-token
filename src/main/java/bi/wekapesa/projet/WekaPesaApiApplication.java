package bi.wekapesa.projet;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import bi.wekapesa.projet.entities.AppRole;
import bi.wekapesa.projet.entities.AppUser;
import bi.wekapesa.projet.services.UserService;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WekaPesaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WekaPesaApiApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner start(UserService userService) {
		return args -> {
			userService.saveRole(new AppRole(null, "USER"));
			userService.saveRole(new AppRole(null, "ADMIN"));
			userService.saveRole(new AppRole(null, "PRODUCT_MANAGER"));
			userService.saveRole(new AppRole(null, "COORDINATOR"));
			userService.saveRole(new AppRole(null, "SUPER_ADMIN"));
			
			userService.saveUser(new AppUser(null, "admin", "12345", false, new ArrayList<>()));
			userService.saveUser(new AppUser(null, "user1", "1234", false, new ArrayList<>()));
			userService.saveUser(new AppUser(null, "manager", "123456", false, new ArrayList<>()));
			userService.saveUser(new AppUser(null, "user2", "1234", false, new ArrayList<>()));
			
			userService.addRoleToUser("admin", "ADMIN");
			userService.addRoleToUser("admin", "USER");
			userService.addRoleToUser("user1", "USER");
			userService.addRoleToUser("user2", "USER");
			userService.addRoleToUser("manager", "SUPER_ADMIN");
			userService.addRoleToUser("manager", "PRODUCT_MANAGER");
			
		};
	}

}
