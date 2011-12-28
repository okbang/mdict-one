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
package openones.idict;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import rocky.common.PropertiesManager;

/**
 * Utility.
 * @author Thach Le
 */
public final class DictUtil {
    /** Logger. */
    private static final Logger LOG = Logger.getLogger("Util");

    /**
     * Avoid caller creates a new instance of Util.
     */
    private DictUtil() {

    }

    /** Repository path of dictionaries. */
    private static String dictRepo;

    static {
        try {
            Properties props = PropertiesManager.newInstanceFromProps("/app.properties");
            dictRepo = props.getProperty("DictRepo");
        } catch (IOException ex) {
            LOG.error("Load configuration /app.properties", ex);
        }
    }

    /**
     * Get repository path of dictionaries.
     * @return String of repository path
     */
    public static String getDictRepo() {
        return dictRepo;
    }
}
