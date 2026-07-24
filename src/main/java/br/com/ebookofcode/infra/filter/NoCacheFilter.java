package br.com.ebookofcode.infra.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Prevents the browser from caching JSF views. Without this, clicking the
 * browser "back" button after a logout would display a stale page (from bfcache)
 * whose JSF view state belongs to an invalidated session; resubmitting a form on
 * that page raises a ViewExpiredException. Forcing a fresh request makes the
 * "back" navigation re-fetch from the server instead.
 */
@WebFilter("*.xhtml")
public class NoCacheFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    var res = (HttpServletResponse) response;

    res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
    res.setHeader("Pragma", "no-cache");
    res.setDateHeader("Expires", 0);

    chain.doFilter(request, response);
  }
}
