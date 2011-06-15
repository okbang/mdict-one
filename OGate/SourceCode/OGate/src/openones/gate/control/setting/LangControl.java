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
package openones.gate.control.setting;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import openones.corewa.BaseOutForm;
import openones.corewa.ReqUtil;
import openones.gate.Cons;
import openones.gate.biz.setting.LangSettingBiz;
import openones.gate.control.LayoutControl;
import openones.gate.form.ModuleListOutForm;
import openones.gate.form.TabModuleOutForm;
import openones.gate.form.setting.LangSettingForm;
import openones.gate.store.LangSettingStore;
import openones.gate.store.ModuleStore;
import openones.gate.store.dto.LangDTO;
import openones.gate.store.dto.ModuleDTO;
import openones.gate.util.DtoUtil;
import rocky.common.Constant;

import com.google.appengine.api.datastore.Text;

/**
 * @author Thach Le
 *
 */
public class LangControl extends LayoutControl {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Override
    public BaseOutForm procInit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("procInit.START");
        LangSettingForm form = new LangSettingForm();
        LangSettingBiz biz = new LangSettingBiz();
        
        //form.setLanguages(biz.getLangs());
        form.setLanguages("vn-Việt Nam" + Constant.LF +
                          "en-English");

        //ModuleBiz biz = new ModuleBiz(AuthorizationBiz.getLogonUser());
        //List<ModuleDTO> moduleList = biz.getModules();
        
        outForm.putRequest(K_FORM, form);
        return outForm;
    }

    /**
     * .
     * @param req
     * @param reqMap
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public BaseOutForm save(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("save.START");
        LangSettingForm form = (LangSettingForm) ReqUtil.getData(reqMap, LangSettingForm.class);


        LOG.info("languages="  + form.getLanguages());
        LangSettingBiz biz = new LangSettingBiz();
        biz.save(form);

        if (biz.save(form) > 0) {
            //outForm.setSaveResult(Cons.ActResult.OK);
            //introOutForm.setKey("IntroSaveOk");
            outForm.putRequest("DialogMessage", "Save succesfully");
        } else {
            //introOutForm.setSaveResult(Cons.ActResult.FAIL);
            //introOutForm.setKey("IntroSaveFail");
        }

        outForm.putRequest(K_FORM, form);

        LOG.finest("save.END");
        return outForm;
    }

    public BaseOutForm list(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("list.START");

        List<LangDTO> langDTOList = LangSettingStore.getLangs();
        LOG.info("moduleDTOList.size=" + langDTOList.size());
        outForm.putRequest(K_FORM, langDTOList);

        LOG.finest("list.END");
        return outForm;
    }

    public BaseOutForm delete(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("delete.START");
        String contentId = (String) reqMap.get("langId");
        Long contentKey = Long.valueOf(contentId);
        
        if (LangSettingStore.delete(contentKey)) {
            outForm.putRequest("deleteResult", "OK");
        } else {
            outForm.putRequest("deleteResult", "FAIL");
        }
        LOG.finest("delete.END");
        
        // refresh the list
        return list(req, reqMap, resp);
    }
}
