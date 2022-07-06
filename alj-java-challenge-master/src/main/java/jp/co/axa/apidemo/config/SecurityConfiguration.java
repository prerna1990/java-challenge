package jp.co.axa.apidemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String VIEWER = "VIEWER";
	public static final String EDITOR = "EDITOR";
	public static final String API_V_1_EMPLOYEES = "/api/v1/employees";
	public static final String API_V_1_EMPLOYEES1 = "/api/v1/employees/**";

	// Create 2 users for demo
	//All this config can be moved to Spring cloud
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication()
			.withUser("User").password("{noop}read_password").roles(VIEWER)
			.and()
			.withUser("Admin").password("{noop}editor_password").roles(VIEWER, EDITOR);

	}

	// Secure the endpoins with HTTP Basic authentication
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			//HTTP Basic authentication
			.httpBasic()
			.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.GET, API_V_1_EMPLOYEES).hasRole(VIEWER)
			.antMatchers(HttpMethod.GET, API_V_1_EMPLOYEES1).hasRole(VIEWER)
			.antMatchers(HttpMethod.POST, API_V_1_EMPLOYEES).hasRole(EDITOR)
			.antMatchers(HttpMethod.PUT, API_V_1_EMPLOYEES1).hasRole(EDITOR)
			.antMatchers(HttpMethod.DELETE, API_V_1_EMPLOYEES1).hasRole(EDITOR)
			.and()
			.csrf().disable()
			.headers().frameOptions().disable();
	}

}