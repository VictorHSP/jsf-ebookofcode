package br.com.ebookofcode.view;

import br.com.ebookofcode.model.Category;
import br.com.ebookofcode.service.MenuService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class MenuCategoryBean implements Serializable {

  private List<Category> categories;

  @Inject
  private MenuService menuService;

  @PostConstruct
  public void init() {
    categories = menuService.findCategories();
  }


  public List<Category> getCategories() {
    return categories;
  }

  public String getCurrentYear() {
    return String.valueOf(java.time.Year.now().getValue());
  }
}
