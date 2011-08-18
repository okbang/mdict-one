/*
 * Copyright (c) 2009, FPT Software JSC
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *	- Neither the name of the FPT Software JSC nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 package fpt.dashboard.bean.ProjectManagement;

import java.util.ArrayList;

public class ProjectListModelBean
{
	String group = "all";
  String type = "0";
  String status="0";
  String cate="0";

  //Collection of Project
  private ArrayList ModelObjectsList = null;
  private int NumberOfElements = 0;

  //Collection of Group
  private ArrayList ModelGroupObjectsList = null;
  private int NumberOfGroupElements = 0;

  public ProjectListModelBean() {
    // Contructor
  }

  public void setGroup(String str){
    this.group = str;
  }

  public String getGroup(){
    return this.group;
  }

  public void setType(String str){
    this.type = str;
  }

  public String getType(){
    return this.type;
  }

  public int getNumberOfElements(){
    if (ModelObjectsList == null)
      return 0;
    return ModelObjectsList.size();
  }

  public int getNumberOfGroupElements(){
    if (ModelGroupObjectsList == null)
      return 0;
    return ModelGroupObjectsList.size();
  }


  public boolean addProject(ProjectInfoBean data){
    if (ModelObjectsList == null)
      ModelObjectsList = new ArrayList();
    return  ModelObjectsList.add(data);
  }

  public ProjectInfoBean getProject(int index){
    if (index <  ModelObjectsList.size())
      return (ProjectInfoBean)  ModelObjectsList.get(index);
    else
      return null;
  }


  public boolean addGroupItem(String data){
    if (ModelGroupObjectsList == null)
      ModelGroupObjectsList = new ArrayList();
    return  ModelGroupObjectsList.add(data);
  }

  public String getGroupItem(int index){
    if (index <  ModelGroupObjectsList.size())
      return (String) ModelGroupObjectsList.get(index);
    else
      return null;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getCate() {
    return cate;
  }
  public void setCate(String cate) {
    this.cate = cate;
  }
}

