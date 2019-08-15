package io.github.malczuuu.cassey.http;

import io.github.malczuuu.problem4j.core.Problem;
import io.github.malczuuu.problem4j.core.ProblemException;

public class BadRequestException extends ProblemException {

  public BadRequestException(String detail) {
    super(Problem.builder().title("Bad Request").status(400).detail(detail).build());
  }
}
