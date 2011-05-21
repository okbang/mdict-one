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
import openones.corewa.control.BaseControl;
import openones.gate.Cons;
import openones.gate.intro.form.IntroOutForm;
import openones.gate.store.IntroStore;
import openones.gate.store.dto.ModuleDTO;
import openones.gate.util.DtoUtil;

import com.google.appengine.api.datastore.Text;

/**
 * @author Thach Le
 *
 */
public class IntroControl extends BaseControl {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());
    
    @Override
    public BaseOutForm procInit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("procInit.START");
        IntroOutForm introOutForm = new IntroOutForm();
        
        introOutForm.setContent(IntroStore.getLastContent());
        outForm.putRequest("introForm", introOutForm);
        
        LOG.finest("procInit.END");
        return outForm;
    }

    public BaseOutForm save(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("save.START");
        IntroOutForm introOutForm = new IntroOutForm();
        
        LOG.info("content="  + reqMap.get("content"));
        Text introContent = new Text((String) reqMap.get("content"));
        ModuleDTO intro = new ModuleDTO(introContent);
        
        if (IntroStore.save(intro )) {
            introOutForm.setSaveResult(Cons.ActResult.OK);
            introOutForm.setMsgKey("IntroSaveOk");
        } else {
            introOutForm.setSaveResult(Cons.ActResult.FAIL);
            introOutForm.setMsgKey("IntroSaveFail");
        }
        
        // Keep the content in the out form
        introOutForm.setContent(intro.getStringContent());
        
        outForm.putRequest("introForm", introOutForm);

        LOG.finest("save.END");
        return outForm;
    }
    
    public BaseOutForm list(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("list.START");
        
        List<ModuleDTO> ModuleDTOList = IntroStore.getContents();
        List<IntroOutForm> outFormList = DtoUtil.dto2IntroFormList(ModuleDTOList);
        outForm.putRequest("intros", outFormList);

        LOG.finest("list.END");
        return outForm;
    }

    public BaseOutForm delete(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("delete.START");
        String contentId = (String) reqMap.get("contentId");
        Long contentKey = Long.valueOf(contentId);
        
        if (IntroStore.delete(contentKey)) {
            outForm.putRequest("deleteResult", "OK");
        } else {
            outForm.putRequest("deleteResult", "FAIL");
        }
        
        // refresh the list
        outForm.putRequest("intros", IntroStore.getContents());

        LOG.finest("delete.END");
        return outForm;
    }
}
