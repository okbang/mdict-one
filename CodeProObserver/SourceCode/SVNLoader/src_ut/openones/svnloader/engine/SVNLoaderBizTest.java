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
package openones.svnloader.engine;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SVNLoaderBizTest {

    @Test
    public void testTransfer0001() {
        try {
            String username = "";
            String password = "";
            String url = "file:///I:/Projects/LunarCal/Wip/Source/LunarCal";
            String path = "D:/Temp/News";
            SVNLoaderBiz svn2DBBiz = new SVNLoaderBiz(url, username, password, path, "LunarCal", -1);
            svn2DBBiz.transfer();

            assertTrue("Check the result in the database!", true);
        } catch (Throwable th) {
            th.printStackTrace();
            fail(th.getMessage());
        }
    }

    @Test
    public void testTransfer0002_ProjectList() {
        try {
            String username = "";
            String password = "";
            String url = "https://open-ones.googlecode.com/svn/trunk/ProjectList";
            String path = "D:/Temp/News/ProjectList";
            SVNLoaderBiz svn2DBBiz = new SVNLoaderBiz(url, username, password, path, "ProjectList", -1);
            svn2DBBiz.transfer();

            assertTrue("Check the result in the database!", true);
        } catch (Throwable th) {
            th.printStackTrace();
            fail(th.getMessage());
        }
    }
    @Test
    public void testTransfer0002_GoogleCode() {
        try {
            String username = "";
            String password = "";
            String url = "http://open-ones.googlecode.com/svn/trunk/CoreWa";
            String path = "D:/Temp/News";
            SVNLoaderBiz svn2DBBiz = new SVNLoaderBiz(url, username, password, path, "CoreWa", -1);
            svn2DBBiz.transfer();

            assertTrue("Check the result in the database!", true);
        } catch (Throwable th) {
            th.printStackTrace();
            fail(th.getMessage());
        }
    }
}
