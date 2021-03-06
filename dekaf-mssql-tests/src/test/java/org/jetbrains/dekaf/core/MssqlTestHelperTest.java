package org.jetbrains.dekaf.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.dekaf.CommonIntegrationCase;
import org.jetbrains.dekaf.sql.SqlQuery;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * @author Leonid Bushuev from JetBrains
 **/
public class MssqlTestHelperTest extends CommonIntegrationCase {


  @Before
  public void connect() {
    DB.connect();
    TH.zapSchema();
  }


  @Test
  public void ensure_no_table() {
    TH.performCommand("create table Tab1(Col1 int)");
    assertThat(objectExists("Tab1")).isTrue();
    TH.ensureNoTableOrView("Tab1");
    assertThat(objectExists("Tab1")).isFalse();
  }

  @Test
  public void ensure_no_table_fk() {
    TH.performScript("create table T1(Id int primary key)",
                     "create table T2(Id int primary key references T1)",
                     "alter table T1 add foreign key (Id) references T2");
    assertThat(objectExists("T1")).isTrue();
    assertThat(objectExists("T2")).isTrue();
    TH.ensureNoTableOrView("T1","T2");
    assertThat(objectExists("T1")).isFalse();
    assertThat(objectExists("T2")).isFalse();
  }

  @Test
  public void zap_table() {
    TH.performCommand("create table Tab1(Col1 int)");
    assertThat(objectExists("Tab1")).isTrue();
    TH.zapSchema();
    assertThat(objectExists("Tab1")).isFalse();
  }

  @Test
  public void zap_procedure() {
    TH.performCommand("create procedure SimplePro1 as select 1984");
    assertThat(objectExists("SimplePro1")).isTrue();
    TH.zapSchema();
    assertThat(objectExists("SimplePro1")).isFalse();
  }

  @Test
  public void zap_function() {
    TH.performCommand("create function SimpleFun1() returns int begin return 1984; end");
    assertThat(objectExists("SimpleFun1")).isTrue();
    TH.zapSchema();
    assertThat(objectExists("SimpleFun1")).isFalse();
  }

  @Test
  public void zap_synonym() {
    TH.performScript("create table Tab1(Col1 int)",
                     "create synonym Syn1 for Tab1");
    assertThat(objectExists("Syn1")).isTrue();
    TH.zapSchema();
    assertThat(objectExists("Syn1")).isFalse();
  }

  @Test
  public void zap_alias_type() {
    TH.performScript("create type just_alias_type_33 from decimal(33)");
    assertThat(typeExists("just_alias_type_33")).isTrue();
    TH.zapSchema();
    assertThat(typeExists("just_alias_type_33")).isFalse();
  }



  private static final SqlQuery<Boolean> ourObjectExistsQuery =
      new SqlQuery<Boolean>("select 1 from sys.all_objects where name = ?", Layouts.existence());

  private static final SqlQuery<Boolean> ourTypeExistsQuery =
      new SqlQuery<Boolean>("select 1 from sys.types where name = ?", Layouts.existence());

  private static boolean objectExists(@NotNull final String sequenceName) {
    assert DB != null;

    Boolean exists =
        DB.inSession(new InSession<Boolean>() {
          @Override
          public Boolean run(@NotNull final DBSession session) {
            return session.query(ourObjectExistsQuery).withParams(sequenceName).run();
          }
        });
    return exists != null && exists;
  }

  private static boolean typeExists(@NotNull final String typeName) {
    assert DB != null;

    Boolean exists =
        DB.inSession(new InSession<Boolean>() {
          @Override
          public Boolean run(@NotNull final DBSession session) {
            return session.query(ourTypeExistsQuery).withParams(typeName).run();
          }
        });
    return exists != null && exists;
  }


}
