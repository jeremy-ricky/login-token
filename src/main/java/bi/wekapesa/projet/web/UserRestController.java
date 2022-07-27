package bi.wekapesa.projet.web;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bi.wekapesa.projet.entities.AppRole;
import bi.wekapesa.projet.entities.AppUser;
import bi.wekapesa.projet.filters.SecurityParams;
import bi.wekapesa.projet.services.UserService;

@RestController
public class UserRestController {
	
	private UserService userService;

	public UserRestController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping(path = "/users")
	@PostAuthorize("hasAuthority('ADMIN')")
	public List<AppUser> appUsers(){
		return userService.listUsers();
	}
	
	@PostMapping(path = "/addUser")
	@PostAuthorize("hasAuthority('ADMIN')")
	public AppUser saveUser(@RequestBody AppUser appUser) {
		return userService.saveUser(appUser);
	}
	
	@PostMapping(path = "/addRole")
	@PostAuthorize("hasAuthority('ADMIN')")
	public AppRole saveRole(@RequestBody AppRole appRole) {
		return userService.saveRole(appRole);
	}
	
	@PostMapping(path = "/addRoleToUser")
	@PostAuthorize("hasAuthority('SUPER_ADMIN')")
	public void saveRoleToUser(@RequestBody RoleToUserForm roleToUserForm) {
		userService.addRoleToUser(roleToUserForm.getUsername(), roleToUserForm.getRoleName());
	}
	
	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException{
		String authorizationToken = request.getHeader(SecurityParams.JWT_HEADER_NAME);
		if(authorizationToken != null && authorizationToken.startsWith(SecurityParams.HEADER_PREFIX)) {
			try {
				String refresh_token = authorizationToken.substring(SecurityParams.HEADER_PREFIX.length());
				Algorithm algorithm = Algorithm.HMAC256(SecurityParams.SECRET.getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				AppUser appUser = userService.loadUserByUsername(username);
				
				String access_token = JWT.create()
						.withSubject(appUser.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + SecurityParams.EXPIRATION_ACCESS))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", appUser.getAppRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList()))
						.sign(algorithm);
				
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
				
			} catch (Exception e) {
				response.setHeader("error", e.getMessage());
				//response.sendError(HttpServletResponse.SC_FORBIDDEN);
				//response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				Map<String, String> error = new HashMap<>();
				error.put("erreur_message", e.getMessage());
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		}else {
			throw new RuntimeException("Refresh token is missing");
		}
		
	}
	
	@GetMapping(path = "/profile")
	@PostAuthorize("hasAuthority('USER')")
	public AppUser profile(Principal principal) {
		return userService.loadUserByUsername(principal.getName());
	}
	

}


class RoleToUserForm{
	
	private String username;
	private String roleName;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	
}
