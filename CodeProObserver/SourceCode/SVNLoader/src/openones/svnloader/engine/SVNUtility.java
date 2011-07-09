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

import java.io.File;

import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNUtility {
    /*
     * Initializes the library to work with a repository via different protocols.
     */
    public static void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();

        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();
    }

    /**
     * Build SVNRepository by url, username ,password params
     * 
     * @param url
     * @param username
     * @param password
     * @return
     * @throws SVNException
     */
    public static SVNRepository buildSVNRepository(String url, String username, String password) throws SVNException {
        SVNRepository repository = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
            repository.setAuthenticationManager(authManager);
        } catch (SVNException svnex) {
            throw svnex;
        }
        return repository;
    }

    /**
     * Replace a string with a pattern to relative path
     * 
     * @param str
     * @param pattern
     * @param replace
     * @return
     */
    public static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    /**
     * change a ChangedPath to relative path with root name
     * 
     * @param repository
     * @param pathSource
     * @return
     */
    public static String changeToRelativePath(SVNRepository repository, String pathSource) {
        String result = "";
        String URL;
        String fullPath;
        try {
            String root = repository.getRepositoryRoot(true).toDecodedString();
            fullPath = root + pathSource;
            URL = repository.getLocation().toDecodedString();
            // if full path not contant URl
            // return null
            if (fullPath.indexOf(URL) == -1) {
                return null;
            }
            result = replace(fullPath, URL, AppConstant.ROOTNAME);
        } catch (Exception ex) {
            return null;
        }
        return result;
    }

    /**
     * get a substring with index from end
     * 
     * @param fullPath
     * @param abortString
     * @return
     */
    public static String getSubStringFromEnd(String fullPath, String abortString) {
        int index = 0;
        String result = null;
        index = fullPath.lastIndexOf(abortString);
        if (index != -1) {
            result = fullPath.substring(0, index);
        }
        return result;
    }

    /**
     * Deletes all files and subdirectories under dir. Returns true if all deletions were successful. If a deletion
     * fails, the method stops attempting to delete and returns false.
     * 
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static boolean deletePath(String path) throws Exception {
        File dirLocal = new File(path);
        if (!dirLocal.exists()) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "Path ''{0}'' is not exists", path);
            throw new SVNException(err);
        }
        boolean success = deleteDir(dirLocal);
        if (!success) {
            throw new Exception("Can not delete path: " + path);
        }
        return success;
    }

    public static String trimCharAtEnd(String path, char endChar) {
        String result = String.copyValueOf(path.toCharArray());
        while (result.lastIndexOf(endChar) == (result.length() - 1)) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
