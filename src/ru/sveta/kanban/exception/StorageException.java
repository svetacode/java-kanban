package ru.sveta.kanban.exception;

import java.io.Serial;

public class StorageException extends RuntimeException{

  @Serial
  private static final long serialVersionUID = -4612797896420115994L;

  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
