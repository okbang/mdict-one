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
package rocky.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The CpyRnFil - Copy Rename and Fill utility.
 * @author Thach Le, team members
 *
 */
public class CpyRnFill {

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            usage();
            System.exit(1);
        }
        
        String srcFile = args[0];
        String destFile = args[1];
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        
        String[] params;
        String key;
        String value;

        for (int i = 2; i < args.length; i++) {
            params = args[i].split("=");
            key = (params.length > 0) ? params[0] : null;
            
            if (key != null) {
                value = (params.length > 1) ? params[1] : Constant.BLANK;
                paramMap.put(key, value);
            }
        }
        
        // Parse the value of dest file name.
        String actDestFile = CommonUtil.formatPattern(destFile, paramMap);
        String contentFile;
        try {
            contentFile = CommonUtil.getContent(srcFile, Constant.DEF_ENCODE);
            String destContentFile = CommonUtil.formatPattern(contentFile, paramMap);
            CommonUtil.SaveFile(actDestFile, destContentFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void usage() {
        System.out.println("CpyRnFill <source file> <destination file> [key=value]\n"
                         + "destination file: file name with pattern xxxx${key}xxx\n");
    }
}
