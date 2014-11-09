package org.jetbrains.dba.access;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.dba.Rdbms;
import org.jetbrains.dba.sql.SQL;

import javax.sql.DataSource;
import java.sql.Connection;



/**
 * MS SQL Server facade.
 */
public final class MssqlFacade extends BaseFacade {

  public MssqlFacade(@NotNull Rdbms rdbms,
                     @NotNull DataSource source,
                     @NotNull DBErrorRecognizer recognizer, @NotNull SQL sql) {
    super(rdbms, source, recognizer, sql);
  }


  @NotNull
  @Override
  protected MssqlSession createSessionForConnection(@NotNull Connection connection) {
    return new MssqlSession(this, connection, true);
  }

}
