package io.github.malczuuu.cassey.http;

import com.datastax.oss.driver.shaded.guava.common.base.Charsets;
import com.datastax.oss.driver.shaded.guava.common.io.Resources;
import java.io.IOException;

public class CommonProblems {

  public static CommonProblems load() throws IOException {
    String notFound =
        Resources.toString(Resources.getResource("problems/not_found.json"), Charsets.UTF_8);
    String internalServerError =
        Resources.toString(
            Resources.getResource("problems/internal_server_error.json"), Charsets.UTF_8);
    return new CommonProblems(notFound, internalServerError);
  }

  private final String notFoundBody;
  private final String internalServerErrorBody;

  private CommonProblems(String notFoundBody, String internalServerErrorBody) {
    this.notFoundBody = notFoundBody;
    this.internalServerErrorBody = internalServerErrorBody;
  }

  public String notFound() {
    return notFoundBody;
  }

  public String internalServerError() {
    return internalServerErrorBody;
  }
}
