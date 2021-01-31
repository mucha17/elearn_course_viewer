package pl.kniotes.elearn_course_viewer.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import pl.kniotes.elearn_course_viewer.Validators.KniotesJwtTokenValidator;

import java.util.Objects;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        @formatter:off
        httpSecurity.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll();
//                .authenticated()
//                .and()
//                .oauth2ResourceServer()
//                .jwt()
//                .decoder(jwtDecoder());
//        @formatter:on
    }

    @Autowired
    private Environment env;

    private JwtDecoder jwtDecoder() {
        String issuerLocation = Objects.requireNonNull(env.getProperty("app.oidc-issuer-location"));
        NimbusJwtDecoder decoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuerLocation);
        OAuth2TokenValidator<Jwt> defaultValidators = JwtValidators.createDefaultWithIssuer(Objects.requireNonNull(issuerLocation));
        OAuth2TokenValidator<Jwt> delegatingValidator = new DelegatingOAuth2TokenValidator<>(defaultValidators, new KniotesJwtTokenValidator());
        decoder.setJwtValidator(delegatingValidator);
        return decoder;
    }
}
