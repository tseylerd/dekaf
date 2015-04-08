package org.jetbrains.jdba.postgre;

import org.jetbrains.jdba.core.BaseIntegrationCase;
import org.jetbrains.jdba.core.TestEnvironment;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * @author Leonid Bushuev from JetBrains
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

                            PostgreFacadeTest.class

})
public class PostgreIntegrationTests {

  @BeforeClass
  public static void setupDB() {
    TestEnvironment.setup(new PostgreServiceFactory(), org.postgresql.Driver.class, null);
    BaseIntegrationCase.connectToDB();
  }

}