package org.apache.sling.commons.jdp;

import org.apache.sling.commons.jdp.api.ReflectTestCase;
import org.apache.sling.commons.jdp.impl.DefaultJDPImplTest;
import org.apache.sling.commons.jdp.impl.PropertyHandlerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RepositoryUtilTest.class , ReflectTestCase.class , DefaultJDPImplTest.class , PropertyHandlerTest.class })
public class AllTests {

}
