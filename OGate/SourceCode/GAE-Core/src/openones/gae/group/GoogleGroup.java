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
package openones.gae.group;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

/**
 * @author ThachLN
 *
 */
public class GoogleGroup {
    final static Logger LOG = Logger.getLogger("GoogleGroup");
    private String groupUrl = "http://groups.google.com/group/open-ones";

    /**
     * @param groupUrl
     */
    public GoogleGroup(String groupUrl) {
        super();
        this.groupUrl = groupUrl;
    }

    public int getNumOfMembers() {
        int nmMember = -1;
        final WebClient webClient = new WebClient();
        HtmlPage startPage;
        try {
            webClient.setJavaScriptEnabled(false);
            startPage = webClient.getPage(groupUrl);
            HtmlElement element = startPage.getElementById("moduleGroupInfo");
            ArrayList<Object> list = (ArrayList<Object>) element.getByXPath("div/table");
            HtmlTable table = (HtmlTable) list.get(0);

            DomNodeList<DomNode> nodeList = table.getRow(0).getCell(0).getChildNodes();

            String nmNumberText;
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.get(i).getNodeType() == Node.TEXT_NODE) {
                    // pattern: " xxx
                    nmNumberText = nodeList.get(i).getNodeValue();
                    //System.out.println("nmNumberText=" + nmNumberText);
                    nmMember = Integer.parseInt(nmNumberText.trim());
                    return nmMember;
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Get number of members from group at: " + groupUrl, ex);
        }
        return nmMember;

    }
}
