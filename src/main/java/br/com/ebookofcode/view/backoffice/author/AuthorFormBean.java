package br.com.ebookofcode.view.backoffice.author;

import br.com.ebookofcode.model.Author;
import br.com.ebookofcode.service.AuthorService;
import br.com.ebookofcode.service.exceptions.BusinessException;
import br.com.ebookofcode.utils.MessageUtils;
import br.com.ebookofcode.view.BaseBean;
import br.com.ebookofcode.view.dto.FileUploadDTO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;
import java.io.IOException;

import org.omnifaces.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@ViewScoped
public class AuthorFormBean extends BaseBean {

  private Author author;
  private Long idAuthor;
  private byte[] previewPhoto;
  private String previewPhotoFileName;
  private String contentType;
  private Long size = 0L;
  private Part authorPhoto;

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthorFormBean.class);

  @Inject
  private AuthorService authorService;
  @Inject
  private MessageUtils messageUtils;

  @PostConstruct
  public void init() {
    if (idAuthor == null) {
      this.author = new Author();
    }
  }

  public void save() {
    try {
      LOGGER.info("Saving author...");
      authorService.save(
          author,
          new FileUploadDTO(previewPhoto, previewPhotoFileName, contentType, size)
      );

      cleanForm();

      messageUtils.info("Author saved successfully");
    } catch (BusinessException e) {
      LOGGER.warn(e.getMessage(), e);
      messageUtils.error(e.getErrorCode().getErrorMessage());
    }catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      messageUtils.error("Couldn't save the author");
    }
  }

  public void loadAuthorDetails() {
    if (idAuthor != null) {
      var author = authorService.findById(idAuthor);
      if (author == null) {
        messageUtils.error("Author not found");
        return;
      }

      if (author.getUrlPhoto() != null) {
        var photoSaved = authorService.findAuthorPhotoOnS3(author.getUrlPhoto());
        if (photoSaved != null) {
          previewPhoto = photoSaved.photo();
          contentType = photoSaved.contentType();
          previewPhotoFileName = photoSaved.fileName();
          size = photoSaved.size();
        }
      }

      setAuthor(author);
    }
  }

  private void cleanForm() {
    LOGGER.info("Cleaning form Author");
    author = new Author();
    previewPhotoFileName = null;
    contentType = null;
    size = 0L;
    authorPhoto = null;
    previewPhoto = null;
  }

  public void read() throws IOException {
    this.previewPhoto = Utils.toByteArray(getAuthorPhoto().getInputStream());
    this.previewPhotoFileName = getAuthorPhoto().getSubmittedFileName();
    this.contentType = getAuthorPhoto().getContentType();
    this.size = getAuthorPhoto().getSize();
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public byte[] getPreviewPhoto() {
    return previewPhoto;
  }

  public String getPreviewPhotoFileName() {
    return previewPhotoFileName;
  }

  public Part getAuthorPhoto() {
    return authorPhoto;
  }

  public void setAuthorPhoto(Part authorPhoto) {
    this.authorPhoto = authorPhoto;
  }

  public void setIdAuthor(Long idAuthor) {
    this.idAuthor = idAuthor;
  }

  public Long getIdAuthor() {
    return idAuthor;
  }
}
