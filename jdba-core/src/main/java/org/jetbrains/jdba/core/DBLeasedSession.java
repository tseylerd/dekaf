package org.jetbrains.jdba.core;

/**
 * Leased session.
 *
 * @author Leonid Bushuev from JetBrains
 */
public interface DBLeasedSession extends DBSession  {

  /**
   * Checks whether the session is really alive.
   *
   * <p>
   *   If the check is successful, just returns the ping duration.
   * </p>
   * <p>
   *   If the check failed, closes the session and throws an exception.
   * </p>
   *
   * @return ping duration (in milliseconds).
   *
   * @see #isClosed()
   */
  long ping();

  /**
   * Check whether the session is closed.
   * @return  is closed.
   *
   * @see #ping()
   * @see #close()
   */
  boolean isClosed();

  /**
   * Closes the session.
   *
   * <p>
   *   When session is already closed, just does nothing.
   * </p>
   *
   * @see #isClosed()
   */
  void close();

}