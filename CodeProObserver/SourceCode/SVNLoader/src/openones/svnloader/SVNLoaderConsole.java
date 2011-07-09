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
package openones.svnloader;

import openones.svnloader.engine.SVNLoaderBiz;

import org.apache.log4j.Logger;


public class SVNLoaderConsole {
    private static Logger LOGGER = Logger.getLogger(SVNLoaderConsole.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        final String AT_URL = "-url";
        final String AT_USER = "-u";
        final String AT_PASSWD = "-p";
        final String AT_TEMPDIR = "-t";
        final String AT_PROCODE = "-pcode";
        final String AT_STARTREVISION = "-start";

        String url = null;
        String username = null;
        String password = null;
        String tempPath = null;
        String projectCode = null;
        long startRevision = -1;

        boolean isValidArg = true;
        LOGGER.debug("args=" + args);
        // Parsing the arguments
        if (args != null && args.length > 0) {

            String[] argValue;
            String key;
            String value;
            for (int i = 0; (i < args.length) && isValidArg; i++) {
                argValue = args[i].split("=");

                if (argValue.length == 2) {
                    key = argValue[0];
                    value = argValue[1];
                    if (AT_URL.equals(key)) {
                        // Get the configuration file in the next argument
                        url = value;
                    } else if (AT_USER.equals(key)) {
                        username = value;
                    } else if (AT_PASSWD.equals(key)) {
                        password = value;
                    } else if (AT_TEMPDIR.equals(key)) {
                        tempPath = value;
                    } else if (AT_PROCODE.equals(key)) {
                        projectCode = value;
                    } else if (AT_STARTREVISION.equals(key)) {
                        try {
                            startRevision = Long.parseLong(value);
                        } catch (NumberFormatException e) {
                            startRevision = -1;
                            LOGGER.error("Can not parse: " + value + " to long type.");
                            isValidArg = false;
                        }
                    } else {
                        isValidArg = false;
                    }
                }
                if (args.length == 1) {
                    isValidArg = false;
                }
            }
        }
        if (isValidArg) {
            LOGGER.info("Processing ...");
            SVNLoaderBiz svn2DBBiz = new SVNLoaderBiz(url, username, password, tempPath, projectCode, startRevision);
            svn2DBBiz.transfer();
            LOGGER.info("Completed!");
        } else {
            // Invalid arguments
            usage();
            System.exit(1);
        }
    }

    private static void usage() {
        System.out
                .println("ConsoleSVN2RDB -url=<SVN URL> -u=<username> -p=<password> -t=<TemplateDirPath> -pcode=<ProjectCode> -start=<StartRevision>");
    }

}
