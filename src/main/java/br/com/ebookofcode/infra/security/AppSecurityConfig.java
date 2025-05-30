package br.com.ebookofcode.infra.security;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;

@CustomFormAuthenticationMechanismDefinition(
    loginToContinue = @LoginToContinue(
        loginPage = "/login.xhtml",
        errorPage = "/login.xhtml?error=true",
        useForwardToLogin = false
    )
)
@DeclareRoles({"ADMIN", "CUSTOMER"})
@ApplicationScoped
public class AppSecurityConfig {}
