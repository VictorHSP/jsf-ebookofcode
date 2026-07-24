package br.com.ebookofcode.view;

import br.com.ebookofcode.infra.security.CustomPrincipal;
import br.com.ebookofcode.model.User;
import br.com.ebookofcode.vo.RolesEnum;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;

import java.io.Serializable;

@Named
@SessionScoped
public class CurrentUserBean implements Serializable {

    @Inject
    private SecurityContext securityContext;

    public User getInfo() {
        var principal = securityContext.getCallerPrincipal();
        if (principal instanceof CustomPrincipal cp) {
            return cp.getUser();
        }

        return null;
    }

    public boolean isLoggedIn() {
        return getInfo() != null;
    }

    public boolean isAdmin() {
        return this.securityContext.isCallerInRole(RolesEnum.ADMIN.name());
    }
}
