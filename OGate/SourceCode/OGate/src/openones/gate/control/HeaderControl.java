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
package openones.gate.control;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import openones.corewa.BaseOutForm;
import openones.gate.Cons;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author Thach Le
 * 
 */
public class HeaderControl extends LayoutControl {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());
//    private static int nmLogonUser = 0;
    
    public BaseOutForm googleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        BaseOutForm outForm = new BaseOutForm();
        UserService userService = UserServiceFactory.getUserService();

        //resp.setCharacterEncoding("utf8");
        outForm.setNextScreen(userService.createLoginURL(req.getRequestURI()));
        //outForm.setDispatched(true);
        //resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));

        // Assumption: user click on Login mean will try to login successfully
        updateNmLogonUser(req.getSession(), +1);

        return outForm;
    }
    public void googleLogout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = UserServiceFactory.getUserService();

        resp.sendRedirect(userService.createLogoutURL(req.getRequestURI()));

        HttpSession session = req.getSession();
        if (session != null) {
            session.removeAttribute(Cons.SK_USER);
            updateNmLogonUser(session, -1);
        }
    }

}
