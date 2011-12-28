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

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;

public class ExportReporterBaton implements ISVNReporterBaton {
    private long exportRevision;
    private static Logger LOGGER = Logger.getLogger(ExportReporterBaton.class);

    public ExportReporterBaton(long revision) {
        exportRevision = revision;
    }

    public void report(ISVNReporter reporter) throws SVNException {
        try {
            /*
             * Here empty working copy is reported.
             * 
             * ISVNReporter includes methods that allows to report mixed-rev working copy and even let server know that
             * some files or directories are locally missing or locked.
             */
            reporter.setPath("", null, exportRevision, SVNDepth.INFINITY, true);

            /*
             * Don't forget to finish the report!
             */
            reporter.finishReport();
        } catch (SVNException svne) {
            reporter.abortReport();
            LOGGER.debug("Report failed.");
        }
    }
}
