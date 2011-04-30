/**
 * Licensed to Open-Ones Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Open-Ones Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package test;

import junit.framework.Test;
import junit.framework.TestSuite;
import openones.corewa.ReqUtilTest;
import openones.corewa.config.ConfigUtilTest;
import openones.corewa.validate.CheckUtilTest;
import openones.corewa.validate.config.ValidateConfUtilTest;

/**
 * @author ThachLN
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getName());
        //$JUnit-BEGIN$
        suite.addTestSuite(CheckUtilTest.class);
        suite.addTestSuite(ConfigUtilTest.class);
        suite.addTestSuite(ValidateConfUtilTest.class);
        suite.addTestSuite(ReqUtilTest.class);
        //$JUnit-END$
        return suite;
    }

}
