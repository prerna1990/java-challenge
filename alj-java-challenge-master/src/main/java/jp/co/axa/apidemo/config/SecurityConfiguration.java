package jp.co.axa.apidemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	// Create 2 users for demo
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication()
			.withUser("User").password("{noop}read_password").roles("VIEWER")
			.and()
			.withUser("Admin").password("{noop}editor_password").roles("VIEWER", "EDITOR”");

	}

	// Secure the endpoins with HTTP Basic authentication
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			//HTTP Basic authentication
			.httpBasic()
			.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.GET,"/api/v1/employees").hasRole(("VIEWER"))
			//.antMatchers(HttpMethod.GET, "/employees/**").hasRole("USER")
			//.antMatchers(HttpMethod.POST, "/api/v1/employees").hasRole("ROLE_EDITOR")
			//.antMatchers(HttpMethod.PUT, "/employees/**").hasRole("ADMIN")
			//.antMatchers(HttpMethod.PATCH, "/employees/**").hasRole("ADMIN")
			//.antMatchers(HttpMethod.DELETE, "/employees/**").hasRole("ADMIN")
			.and()
			.csrf().disable()
			.headers().frameOptions().disable();
			//.formLogin().disable();
	}

}