/*
 * Copyright 2012 Six Dimensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.commons.proxy.core;

import org.apache.sling.commons.proxy.core.impl.DefaultJDPImplTest;
import org.apache.sling.commons.proxy.core.impl.PropertyHandlerTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RepositoryUtilTest.class , DefaultJDPImplTest.class , PropertyHandlerTest.class })
public class AllTests {
    
    @BeforeClass 
    public static void sleepForProfilerAttach() throws Exception {
        Thread.currentThread().sleep(1000 * 10);
    }
}