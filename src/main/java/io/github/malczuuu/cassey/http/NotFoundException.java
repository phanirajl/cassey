package io.github.malczuuu.cassey.http;

import io.github.malczuuu.problem4j.core.Problem;
import io.github.malczuuu.problem4j.core.ProblemException;

public class NotFoundException extends ProblemException {

  public NotFoundException(String detail) {
    super(Problem.builder().title("Not Found").status(404).detail(detail).build());
  }
}
