package br.com.ebookofcode.view.backoffice.author;

import br.com.ebookofcode.model.Author;
import br.com.ebookofcode.service.AuthorService;
import br.com.ebookofcode.view.BaseBean;
import br.com.ebookofcode.view.backoffice.utils.RequestFilter;
import br.com.ebookofcode.view.dto.FilterDTO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Named
@ViewScoped
public class AuthorBean extends BaseBean implements RequestFilter {

  private List<Author> authors;
  private int page = 0;
  private final int pageSize = 5;
  private long totalAuthors;
  private String emailSearch;
  private FilterDTO filter;

  private final Logger logger = LoggerFactory.getLogger(AuthorBean.class);

  @Inject
  private AuthorService authorService;

  @PostConstruct
  public void init() {
    totalAuthors = authorService.countTotalAuthors();
    filter = new FilterDTO();
    loadPageableAuthors(this.page, this.pageSize);
  }

  public void loadPageableAuthors(int page, int pageSize) {
    logger.info("Executing loadPageableAuthors with page: {} and pageSize: {}", page, pageSize);
    authors = authorService.findAuthorsPageable(page, pageSize);
  }

  public void nextPage() {
    logger.info("Executing nextPage with page: {} and pageSize: {}", page, pageSize);
    page++;
    loadPageableAuthors(this.page, this.pageSize);
  }

  public void previousPage() {
    logger.info("Executing previousPage with page: {} and pageSize: {}", page, pageSize);
    if (page > 0) page--;
    loadPageableAuthors(this.page, this.pageSize);
  }

  public boolean isFirstPage() {
    logger.info("Executing isFirstPage with page: {}", page);
    return page == 0;
  }

  public boolean isLastPage() {
    logger.info("Executing isLastPage with page: {} and totalAuthors: {} and pageSize: {}", page, totalAuthors, pageSize);
    return (page + 1L) * pageSize >= totalAuthors;
  }

  public List<Author> getAuthors() {
    return authors;
  }

  public void searchByEmail() {
    logger.info("searchByEmail with email: {}", emailSearch);
  }

  public String getEmailSearch() {
    return emailSearch;
  }

  public void setEmailSearch(String emailSearch) {
    this.emailSearch = emailSearch;
  }

  @Override
  public void filter() {
    logger.info("filterDTO: {}", filter);
    loadPageableAuthors(0, 1);
  }

  public FilterDTO getFilter() {
    return filter;
  }

  public void setFilter(FilterDTO filter) {
    this.filter = filter;
  }
}
