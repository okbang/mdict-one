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
package openones.gate.biz;

import openones.gae.users.OUser;
import openones.gate.store.AuthorizationStore;
import rocky.common.CommonUtil;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author Thach Le
 *
 */
public class SessionBiz {
    private static OUser logonUser = null;
    private static String langCd = null;
    /**
     * Check the account has authorization for given module, screen, event.
     * @param account Account name of gmail.com or the email address.
     * @param moduleId
     * @param screenId
     * @param eventId
     * @return true if the account has authorization.
     */
    public static boolean isAuthorized(String account, String moduleId, String screenId, String eventId) {
        if (!CommonUtil.isNNandNB(account)) {
            return false;
        }

        String emailAddr = (account.indexOf("@") > -1) ? account : account + "@gmail.com";

        AuthorizationStore store = new AuthorizationStore();

        return store.isExisted(emailAddr, moduleId, screenId, eventId);
    }

    /**
     * Get wrapper of logon user.
     * @return
     */
    public static OUser getLogonUser() {
        if (logonUser != null) {
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();
            if (user != null) {
                logonUser = new OUser(user);
            }
        }
        return logonUser;
    }

    /**
     * [Give the description for method].
     * @return
     */
    public static String getLangCd() {
        if (langCd == null) {
            langCd = "vn";
        }
        return langCd;
    }
    
    public static void setLangCd(String langCode) {
        langCd = langCode;
    }
}
