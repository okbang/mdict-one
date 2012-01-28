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
package openones.idict.springmvcportlet.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.portlet.ActionResponse;

import openones.idict.DictUtil;
import openones.idict.biz.DictBiz;
import openones.idict.portlet.form.DictInfo;
import openones.idict.portlet.form.LookupForm;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
/**
 * @author Thach Le
 */

@Controller
@RequestMapping("VIEW")
@SessionAttributes("lookupForm")
public class LookupController {
    /** Logger. */
    private static Logger log = Logger.getLogger("LookupController");

    /** Dictionary business layer. */
    private DictBiz dictBiz = new DictBiz(DictUtil.getDictRepo());

    /**
     * Render phase.
     * @return initial data for Lookup form.
     */
    @RenderMapping
    public ModelAndView initLookup() {
        log.debug("initLookup.START");
        ModelAndView mav = new ModelAndView("sLookup"); // display sLookup.jsp
        Collection<DictInfo> dictInfoColl = dictBiz.getDictInfoList();

        LookupForm formBean = new LookupForm();
        // mav.addObject("dictInfos", dictInfoColl);
        formBean.setDictInfoList(new ArrayList<DictInfo>(dictInfoColl));
        mav.addObject("formBean", formBean);

        return mav;
    }

    @ModelAttribute("lookupForm")
    public LookupForm getCommandObject() {
        log.debug("getCommandObject.START");
        LookupForm formBean = new LookupForm();
        Collection<DictInfo> dictInfoColl = dictBiz.getDictInfoList();

        // mav.addObject("dictInfos", dictInfoColl);
        formBean.setDictInfoList(new ArrayList<DictInfo>(dictInfoColl));

        return formBean;
    }

    /**
     * Process submitted form by clicking "Translate" button.
     * @param formBean
     * @param result
     * @param status
     * @param response
     */
    @ActionMapping(params = "action=translate")
    public void translateWord(@ModelAttribute(value = "lookupForm") LookupForm formBean, BindingResult result,
            SessionStatus status, ActionResponse response) {
        log.debug("handleActionRequest.START");
        log.debug("word=" + formBean.getWord() + ";selected dict=" + formBean.getSelectedDict());

        if (!result.hasErrors()) {
            List<String> dictNames = new ArrayList<String>();
            dictNames.add(formBean.getSelectedDict());

            Collection<DictInfo> dictMeanings = dictBiz.getMeaningByDict(formBean.getWord(), dictNames);

            formBean.setDictMeanings(dictMeanings);
            // Prepare parameter to render phase
            response.setRenderParameter("action", "translate");
        } else {
            log.error("Error in binding result:" + result.getErrorCount());
        }
    }

    @RenderMapping(params = "action=translate")
    public String postTranslateWord() {
        log.debug("postTranslateWord.START");

        return "sLookup";
    }

}
