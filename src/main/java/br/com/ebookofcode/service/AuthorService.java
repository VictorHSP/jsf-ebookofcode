package br.com.ebookofcode.service;

import br.com.ebookofcode.model.Author;
import br.com.ebookofcode.repository.AuthorRepository;
import br.com.ebookofcode.service.exceptions.AuthorAlreadyExistsException;
import br.com.ebookofcode.service.exceptions.ErrorCodeEnum;
import br.com.ebookofcode.utils.S3Utils;
import br.com.ebookofcode.view.backoffice.author.AuthorBean;
import br.com.ebookofcode.view.dto.FileUploadDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@RequestScoped
public class AuthorService implements Serializable {

  @Inject
  private AuthorRepository authorRepository;

  private final Logger logger = LoggerFactory.getLogger(AuthorService.class);

  private static final String FOLDER_AUTHOR_PHOTO = "author-photo";

  public List<Author> findAuthorsPageable(final int page, final int pageSize) {
    return authorRepository.findPageableAuthor(page, pageSize);
  }

  @Transactional
  public void save(Author author, FileUploadDTO fileUpload) {
    validateIfAuthorAlreadyExists(author);

    if (fileUpload != null && fileUpload.size() > 0
        && fileUpload.fileName() != null && !fileUpload.fileName().equals(author.getUrlPhoto()) ) {
      var s3url = S3Utils.uploadFile(fileUpload, FOLDER_AUTHOR_PHOTO, author.getEmail());
      logger.info("Photo uploaded to S3: {}", S3Utils.generatePreSignedUrl(s3url));
      author.setUrlPhoto(s3url);
    }

    authorRepository.save(author);
  }

  private void validateIfAuthorAlreadyExists(Author author) {
    if (author.getId() == null && authorRepository.findByEmail(author.getEmail()).isPresent()) {
      throw new AuthorAlreadyExistsException(ErrorCodeEnum.ERROR_001);
    }
  }

  public Author findById(Long id) {
    return authorRepository.findById(id);
  }

  public FileUploadDTO findAuthorPhotoOnS3(String urlPhoto) {
      try {
          return S3Utils.downloadPhoto(urlPhoto);
      } catch (IOException e) {
          logger.error("Failed to download author photo from S3. URL: {}", urlPhoto, e);
      }

      return null;
  }

  public Long countTotalAuthors() {
    return authorRepository.countTotalAuthors();
  }
}
