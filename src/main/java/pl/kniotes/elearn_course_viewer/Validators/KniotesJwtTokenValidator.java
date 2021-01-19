package pl.kniotes.elearn_course_viewer.Validators;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Objects;

public class KniotesJwtTokenValidator implements OAuth2TokenValidator<Jwt> {

    private final String oidcAudience = "elearn";

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if(!jwt.getAudience().contains(Objects.requireNonNull(oidcAudience))) {
            OAuth2Error error = new OAuth2Error("invalid_token", "Oczekiwano audytorium: " + Objects.requireNonNull(oidcAudience), null);
            return OAuth2TokenValidatorResult.failure(error);
        }
        return OAuth2TokenValidatorResult.success();
    }

}
