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
package rocky.htmlunit.webclient;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

/**
 * @author Thach Le
 */
public class TestWebClient {

    /**
     * This test tries to use HTML Unit to parse the content of web site.
     * Input the home page of a Google Group:
     * Open-Ones Output: Number of members of the group Open-Ones
     */
    @Test
    public void testWebClient() {
        final WebClient webClient = new WebClient();
        HtmlPage startPage;
        try {
            webClient.setJavaScriptEnabled(false);
            startPage = webClient.getPage("http://groups.google.com/group/open-ones");
            HtmlElement element = startPage.getElementById("moduleGroupInfo");
            ArrayList<Object> list = (ArrayList) element.getByXPath("div/table");
            HtmlTable table = (HtmlTable) list.get(0);
            // table.getRow(0).getByXPath("td")

            DomNodeList<DomNode> nodeList = table.getRow(0).getCell(0).getChildNodes();

            String nmNumberText;
            for (int i = 0; i < nodeList.getLength(); i++) {
                // System.out.println("" + Node.TEXT_NODE);
                if (nodeList.get(i).getNodeType() == Node.TEXT_NODE) {
                    System.out.println("type=" + nodeList.get(i).getNodeType() + "" + nodeList.get(i).getNodeValue());
                    // pattern: " xxx
                    nmNumberText = nodeList.get(i).getNodeValue();
                    System.out.println("Number of members of Open-Ones Group:" + nmNumberText);
                }
            }

            // Assert.assertEquals("HtmlUnit - Welcome to HtmlUnit", );
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }

    }
}
