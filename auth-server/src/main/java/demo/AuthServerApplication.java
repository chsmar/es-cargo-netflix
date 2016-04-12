package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.security.Principal;

@SpringBootApplication
@EnableEurekaClient
@RestController
@EnableResourceServer
public class AuthServerApplication {

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Configuration
    @Order(-20)
    public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.formLogin()
                    .and()
                    .requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
                    .and()
                    .authorizeRequests().anyRequest().authenticated();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.parentAuthenticationManager(authenticationManager);
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class OAuthServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private DataSource dataSource;

        private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            endpoints.authorizationCodeServices(authorizationCodeServices())
                    .authenticationManager(authenticationManager).tokenStore(tokenStore())
                    .approvalStoreDisabled();
        }

        @Bean
        public JdbcTokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }

        @Bean
        protected AuthorizationCodeServices authorizationCodeServices() {
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security)
                throws Exception {
            security.passwordEncoder(passwordEncoder);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.jdbc(dataSource)
                    .passwordEncoder(passwordEncoder)
                    //.withClient("client")  //first time only. See https://github.com/spring-projects/spring-security-oauth/issues/420
                    //.authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token", "password", "implicit")
                    //.authorities("ROLE_CLIENT")
                    //.resourceIds("apis")
                    //.scopes("read")
                    //.secret("secret")
                    //.accessTokenValiditySeconds(300)
                    ;
        }
    }

    @Configuration
    @Order(Ordered.LOWEST_PRECEDENCE - 20)
    protected static class AuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private DataSource dataSource;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer = auth.jdbcAuthentication().dataSource(dataSource);

            //configurer.withUser("dave").password("secret").roles("USER"); //first time only
            //configurer.withUser("anil").password("password").roles("ADMIN");
        }
    }
}