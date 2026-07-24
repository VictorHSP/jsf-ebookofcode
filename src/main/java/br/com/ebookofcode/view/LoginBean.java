package br.com.ebookofcode.view;

import br.com.ebookofcode.infra.security.CustomPrincipal;
import br.com.ebookofcode.vo.RolesEnum;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Named
@RequestScoped
public class LoginBean implements Serializable {

  private String username;
  private String password;

  @Inject
  private SecurityContext securityContext;

  public String login() {
    var facesContext = FacesContext.getCurrentInstance();
    var request      = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    var response     = (HttpServletResponse) facesContext.getExternalContext().getResponse();

    var authenticationStatus = securityContext.authenticate(request, response,
        AuthenticationParameters.withParams()
            .credential(new UsernamePasswordCredential(getUsername(), getPassword())));

    return switch (authenticationStatus) {
      case SEND_CONTINUE -> {
        facesContext.responseComplete();
        yield null;
      }
      case SUCCESS -> "index.xhtml?faces-redirect=true";
      case SEND_FAILURE -> "login.xhtml?error=true";
      default -> null;
    };
  }

  public String checkAlreadyLoggedIn() {
    var principal = securityContext.getCallerPrincipal();

    if (!(principal instanceof CustomPrincipal)) {
      return null;
    }

    if (securityContext.isCallerInRole(RolesEnum.ADMIN.name())) {
      return "/admin/index.xhtml?faces-redirect=true";
    }

    return "/customer/index.xhtml?faces-redirect=true";
  }

  public String logout() throws ServletException {
    var context = FacesContext.getCurrentInstance();
    var request = (HttpServletRequest) context.getExternalContext().getRequest();

    request.logout();
    request.getSession().invalidate();

    return "/login.xhtml?faces-redirect=true";
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
