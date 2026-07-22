package br.com.ebookofcode.utils;

import br.com.ebookofcode.view.dto.FileUploadDTO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

public class S3Utils {

  private static final String BUCKET_NAME = "ebook-of-code";
  private static final Region REGION = Region.US_EAST_1;
  private static final String FOLDER_PATH = "/";
  private static final String SEPARATOR = "_";

  public static String uploadFile(final FileUploadDTO fileUpload, final String folder, final String email) {
    try (InputStream inputStream = new ByteArrayInputStream(fileUpload.photo()); S3Client s3Client = buildS3Client()) {
      String key = folder
          .concat(FOLDER_PATH)
          .concat(fileUpload.fileName())
          .concat(SEPARATOR).concat(email);

      PutObjectRequest putRequest = PutObjectRequest.builder()
          .bucket(BUCKET_NAME)
          .key(key)
          .contentType(fileUpload.contentType())
          .build();

      s3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, fileUpload.size()));

      return key;
    } catch (Exception e) {
      throw new RuntimeException("Could not save file on S3 %s".formatted(fileUpload.fileName()), e);
    }
  }

  public static String generatePreSignedUrl(String key) {
    S3Presigner preSigner = S3Presigner.builder()
        .region(REGION)
        .endpointOverride(URI.create("http://localhost:4566"))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create("secret", "secret")))
        .serviceConfiguration(S3Configuration.builder()
            .pathStyleAccessEnabled(true)
            .build())
        .build();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(BUCKET_NAME)
        .key(key)
        .build();

    GetObjectPresignRequest preSignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofDays(7))
        .getObjectRequest(getObjectRequest)
        .build();

    URL presignedUrl = preSigner.presignGetObject(preSignRequest).url();

    preSigner.close();

    return presignedUrl.toString();
  }

  public static FileUploadDTO downloadPhoto(final String key) throws IOException {
    try (S3Client s3Client = buildS3Client()) {

      GetObjectRequest request = GetObjectRequest.builder()
          .bucket(BUCKET_NAME)
          .key(key)
          .build();

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(request);
      s3Object.transferTo(baos);

      GetObjectResponse metadata = s3Object.response();

      return new FileUploadDTO(
          baos.toByteArray(),
          key,
          metadata.contentType(),
          metadata.contentLength()
      );
    } catch (IOException | NoSuchKeyException e) {
      throw new IOException(String.format("Failed to read file from S3. Key: %s", key), e);
    }
  }

  private static S3Client buildS3Client() {
    return S3Client.builder()
        .region(REGION)
        .endpointOverride(URI.create("http://localhost:4566"))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create("secret", "secret")))
        .serviceConfiguration(S3Configuration.builder()
            .pathStyleAccessEnabled(true)
            .build())
        .build();
  }
}
