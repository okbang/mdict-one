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
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import openones.corewa.BaseOutForm;
import openones.gate.Cons;
import openones.gate.form.ModuleListOutForm;
import openones.gate.form.TabModuleOutForm;
import openones.gate.store.ModuleStore;
import openones.gate.store.dto.ModuleContentDTO;
import openones.gate.store.dto.ModuleDTO;
import openones.gate.util.DtoUtil;
import rocky.common.Constant;

import com.google.appengine.api.datastore.Text;

/**
 * @author Thach Le
 *
 */
public class ModuleIntroEditorControl extends OGateBaseControl {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Override
    public BaseOutForm procInit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("procInit.START");
        TabModuleOutForm tabModuleOutForm = new TabModuleOutForm();

        String moduleId = req.getParameter(K_TABID);
        String tabKey = req.getParameter(K_TABKEY);
        
        LOG.finest("moduleId=" + moduleId + ";tabKey=" + tabKey);
        Text moduleContent = ModuleStore.getLastModuleContent(moduleId);
        String content = (moduleContent != null ? moduleContent.getValue() : Constant.BLANK);  

        tabModuleOutForm.setContent(content);
        outForm.putRequest(K_TABMODULE, tabModuleOutForm);
        outForm.putRequest(K_TABKEY, tabKey);
        
        // Store the the tabKey in the session
        req.getSession().setAttribute(K_TABKEY, Long.valueOf(tabKey));

        LOG.finest("procInit.END");
        return outForm;
    }

    /**
     * Process to display Edit screen of content of Tab.
     * @param req
     * @param reqMap
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public BaseOutForm edit(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("edit.START");
        TabModuleOutForm tabModuleOutForm = new TabModuleOutForm();
        outForm = new BaseOutForm();

        String menuId = req.getParameter(K_MENUID);
        Long tabKey = (Long) req.getSession().getAttribute(K_TABKEY);
        String moduleId = menuId;
        LOG.finest("menuId=" + menuId + ";tabKey=" + tabKey);
        ModuleDTO tabModule = ModuleStore.getModuleByKey(tabKey);
        
        Text tabModuleContent = ModuleStore.getLastModuleContent(moduleId);
        String content = (tabModuleContent != null ? tabModuleContent.getValue() : Constant.BLANK);  

        // Keep the content in the out form
        tabModuleOutForm.setContent(content);

        //setMainScreen("EditModuleIntro");
        outForm.putRequest(K_TABMODULE, tabModuleOutForm);
        outForm.putRequest(K_MENUID, moduleId);
        outForm.putRequest(K_TABNAME, tabModule.getName());
        //outForm.putRequest(K_TABKEY, tabKey);

        LOG.finest("edit.END");
        return outForm;
    }

    /**
     * Process to save the content of Tab.
     * @param req
     * @param reqMap
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public BaseOutForm save(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("save.START");
        TabModuleOutForm tabModuleOutForm = new TabModuleOutForm();
        String content = (String) reqMap.get("content");
        Long tabKey = (Long) req.getSession().getAttribute(K_TABKEY);
        
        LOG.info("tabKey=" + tabKey + ";content="  + content);
        String tabModuleId = (String) reqMap.get(K_TABMODULEID);
        String tabModuleName = (String) reqMap.get("tabModuleName");

        if (ModuleStore.saveContent(tabKey, new Text(content))) {
            tabModuleOutForm.setSaveResult(Cons.ActResult.OK);
            //introOutForm.setKey("IntroSaveOk");
        } else {
            tabModuleOutForm.setSaveResult(Cons.ActResult.FAIL);
            //introOutForm.setKey("IntroSaveFail");
        }

        // Keep the content in the out form
        tabModuleOutForm.setContent(content);

        outForm.putRequest(K_TABMODULE, tabModuleOutForm);

        LOG.finest("save.END");
        return outForm;
    }
    
    public BaseOutForm list(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("list.START");

        // Get tabKey from the session
        Long tabKey = (Long) req.getSession().getAttribute(K_TABKEY);

        ModuleDTO module = ModuleStore.getModuleByKey(tabKey);

        String moduleId = module.getId();

        List<ModuleContentDTO> moduleConentList = ModuleStore.getModuleContents(moduleId);

        LOG.info("moduleConentList.size=" + moduleConentList.size());
        List<TabModuleOutForm> outFormList = DtoUtil.dto2TabModuleFormList(moduleConentList);
        LOG.info("outFormList.size=" + outFormList.size());

        ModuleListOutForm moduleList = new ModuleListOutForm();
        moduleList.setModuleType(module.getType() + Cons.UNDER_SCORE + module.getName());
        moduleList.setModuleList(outFormList);
        
        outForm.putRequest("modules", moduleList);

        LOG.finest("list.END");
        return outForm;
    }

    /**
     * Delete content of Tab Module.
     * @param req
     * @param reqMap
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public BaseOutForm delete(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("delete.START");
        String contentId = (String) reqMap.get("contentId");
        Long contentKey = Long.valueOf(contentId);

        if (ModuleStore.deleteContent(contentKey)) {
            outForm.putRequest("deleteResult", "OK");
        } else {
            outForm.putRequest("deleteResult", "FAIL");
        }
        LOG.finest("delete.END");

        // refresh the list
        return list(req, reqMap, resp);
    }
}
