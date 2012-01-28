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
package openones.idict.portlet.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import openones.corewa.BaseOutForm;
import openones.corewa.ReqUtil;
import openones.idict.DictUtil;
import openones.idict.biz.DictBiz;
import openones.idict.portlet.form.DictInfo;
import openones.idict.portlet.form.LookupForm;
import openones.portlet.control.BaseControl;

import org.apache.log4j.Logger;

/**
 * Controller of Lookup screen.
 * @author Thach Le
 */
public class LookupControl extends BaseControl {
    /** Logger. */
    private static final Logger LOG = Logger.getLogger("LookupControl");

    /** Dictionary business layer. */
    private DictBiz dictBiz = new DictBiz(DictUtil.getDictRepo());

    /**
     * [Explain the description for this method here].
     * @param request client request
     * @param response response for client
     * @return instance of BaseOutForm contains objects to save into request or session.
     * @throws PortletException
     * @throws IOException
     * @see openones.portlet.control.BaseControl#init(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
     */
    @Override
    public BaseOutForm init(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        LOG.debug("init render.START");
        BaseOutForm outForm = new BaseOutForm();
        Collection<DictInfo> dictInfoColl = dictBiz.getDictInfoList();
        outForm.putSession("dictInfos", dictInfoColl);

        return outForm;
    }

    /**
     * [Explain the description for this method here].
     * @param request client request
     * @param reqMap map of submitted data
     * @param response response for client
     * @return output data
     * @throws PortletException
     * @throws IOException
     * @see openones.portlet.control.BaseControl#init(javax.portlet.ActionRequest, java.util.Map,
     *      javax.portlet.ActionResponse)
     */

    /**
     * Process event translate.
     * @param request client request
     * @param reqMap submitted data from client
     * @param response response for client
     * @return instance of BaseOutForm contains objects to save into request or session.
     * @throws PortletException
     * @throws IOException
     */
    public BaseOutForm translate(ActionRequest request, Map<String, Object> reqMap, ActionResponse response)
            throws PortletException, IOException {
        LookupForm lookupBean = (LookupForm) ReqUtil.getData(reqMap, LookupForm.class);

        LOG.debug("word=" + lookupBean.getWord() + ";selected dict=" + lookupBean.getSelectedDict());

        List<String> dictNames = new ArrayList<String>();
        dictNames.add(lookupBean.getSelectedDict());
        Collection<DictInfo> dictInfoColl = dictBiz.getMeaningByDict(lookupBean.getWord(), dictNames);

        BaseOutForm outForm = new BaseOutForm();
        outForm.putRequest("dictMeanings", dictInfoColl);
        //outForm.putRequest("formBean", lookupBean);
        request.setAttribute("formBean", lookupBean);

        return outForm;
    }

}
