package br.com.ebookofcode.infra.security;

import br.com.ebookofcode.model.User;
import jakarta.security.enterprise.CallerPrincipal;

public class CustomPrincipal extends CallerPrincipal {

  private final User user;

  public CustomPrincipal(User user) {
    super(user.getName());
    this.user = user;
  }

  @Override
  public String getName() {
    return user.getName();
  }

  public String getEmail() {
    return user.getEmail();
  }

  public User getUser() {
    return user;
  }
}
