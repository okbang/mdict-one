<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<script type="text/javascript">
  var TAB_MANAGER_SEPARATOR = ":";
  // Two dimessional array of managers
  var managerArray;

  window.onload = function() {
      buildmanagersOfTab("frmTabSetting");
  }
  
  /**
   */
  function buildmanagersOfTab(frmName) {
    var frm = document.forms[frmName];
    var theSel = frm.selectedTab;
    
    managerArray = new Array(theSel.length);
    //alert("managersOfTab size 1:" + managersOfTab.length);
    managerArray = frm.managersOfTab.value.split(TAB_MANAGER_SEPARATOR);
    //alert("managersOfTab size 2:" + managersOfTab.length);
  }
  
  function insertTab(frmName) {
    var frm = document.forms[frmName];
    var theSel = frm.selectedTab;
    var newValue = frm.newTab.value;
    var newText = frm.newTab.value;

    if (theSel.length == 0) {
      var newOpt1 = new Option(newText, newValue);
      theSel.options[0] = newOpt1;
      theSel.selectedIndex = 0;
    } else if (theSel.selectedIndex != -1) {
      var selText = new Array();
      var selValues = new Array();
      var selIsSel = new Array();
      var newCount = -1;
      var newSelected = -1;
      var i;
      
      for (i=0; i < theSel.length; i++) {
        newCount++;
        
        if (newCount == theSel.selectedIndex) {
          selText[newCount] = newText;
          selValues[newCount] = newValue;
          selIsSel[newCount] = false;
          newCount++;
          newSelected = newCount;
        }
        selText[newCount] = theSel.options[i].text;
        selValues[newCount] = theSel.options[i].value;
        selIsSel[newCount] = theSel.options[i].selected;
      }
      
      for(i = 0; i <= newCount; i++) {
        var newOpt = new Option(selText[i], selValues[i]);
        theSel.options[i] = newOpt;
        theSel.options[i].selected = selIsSel[i];
      }
    }
  }
  
  /**
   *
   * @param frmName: Form name
   */
  function addTab(frmName) {
    var frm = document.forms[frmName];
    var theSel = frm.selectedTab;
    var newValue = frm.newTab.value;
    var newText = frm.newTab.value;
    
    if (theSel.length == 0) {
      var newOpt1 = new Option(newText, newValue);
      theSel.options[0] = newOpt1;
      theSel.selectedIndex = 0;
    }
    
    if (theSel.selectedIndex == -1) {
        theSel.selectedIndex = theSel.length; 
    }
    
    var selText = new Array();
    var selValues = new Array();
    var selIsSel = new Array();
    var newCount = -1;
    var newSelected = -1;
    var i;
    
    for (i = 0; i < theSel.length; i++) {
      newCount++;
      selText[newCount] = theSel.options[i].text;
      selValues[newCount] = theSel.options[i].value;
      selIsSel[newCount] = theSel.options[i].selected;
      
      if (newCount == theSel.selectedIndex) {
        newCount++;
        selText[newCount] = newText;
        selValues[newCount] = newValue;
        selIsSel[newCount] = false;
        newSelected = newCount - 1;
      }
    }
    
    for (i = 0; i <= newCount; i++) {
      var newOpt = new Option(selText[i], selValues[i]);
      theSel.options[i] = newOpt;
      theSel.options[i].selected = selIsSel[i];
    }
    
  }

  
  /**
   */
  function moveOptionsUp(frmName) {
      var frm = document.forms[frmName];
      var theSel = frm.selectedTab;

      for (var i = 1; i < theSel.length; i++) {
       var opt = theSel[i];
       if (opt.selected) {
           theSel.removeChild(opt);
           theSel.insertBefore(opt, theSel[i - 1]);
       }
      }
  }
  
  function moveOptionsDown(frmName) {
    var frm = document.forms[frmName];
    var theSel = frm.selectedTab;
    
    for (var i = theSel - 2; i >= 0; i--) {
       var opt = theSel[i];
       
       if (opt.selected) {
        var nextOpt = theSel[i + 1];
        opt = theSel.removeChild(opt);
        nextOpt = theSel.replaceChild(opt, nextOpt);
        theSel.insertBefore(nextOpt, opt);
       }
    }
   }
  
  function deleteTab(frmName) {
    var frm = document.forms[frmName];
    var theSel = frm.selectedTab;
    var selIndex = theSel.selectedIndex;
    if (selIndex != -1) {
      for (i = theSel.length-1; i>=0; i--) {
        if (theSel.options[i].selected) {
          theSel.options[i] = null;
        }
      }
      
      if (theSel.length > 0) {
        theSel.selectedIndex = selIndex == 0 ? 0 : selIndex - 1;
      }
    }
  }

  /**
   * Change tab.
   */
  function changeTab(frmName) {
    var frm = document.forms[frmName];
    var theSel = frm.selectedTab; 
    
    if (theSel.selectedIndex != -1) {
        //alert(managersOfTab[theSel.selectedIndex]);
        // Update the current email managers to 
        frm.emailManagers.value = managerArray[theSel.selectedIndex];
    }
  }
  
  /**
   * Update the list of email managers of the tab.
   */
  function updateManager(frmName) {
      var frm = document.forms[frmName];
      var theSel = frm.selectedTab; 
      
      if (theSel.selectedIndex != -1) {
          if (confirm("Bạn muốn cập nhật danh sách người quản lý tab '" + theSel.options[theSel.selectedIndex].text + "'?")) {
              managerArray[theSel.selectedIndex] = frm.emailManagers.value;
          }
      }
  }
  /**
   * Select all items of select before submit form.
   */
  function selectAllTab(frmName, delim) {
    var frm = document.forms[frmName];
    var theSel = frm.selectedTab;
    for (var i = 0; i< theSel.length; i++) {
        theSel[i].selected = true;
        
        // Save the tab key
        frm.tabKeys.value = (frm.tabKeys.value == '') ? theSel[i].value : 
                                                        frm.tabKeys.value + delim + theSel[i].value;
        
        // 
        frm.managersOfTab.value = managerArray.join(TAB_MANAGER_SEPARATOR);
    }
  }
  

</script>
<form name="frmTabSetting" action="setting.mod" method="post">
  <input type="hidden" name="screenId" value="TabSetting"/>
  <input type="hidden" name="eventId" value=""/>
  <%-- Store list of tab key with. Separator by a comma --%>
  <input type="hidden" name="tabKeys" value=""/>
  
  <%-- String of 2x array of managers of tab.
    Separator of array: :
    Separator of item in an array: comma or semi-comma or new-line
    Ex: m1@tab1;m12tab1:m1@tab2:
   --%>
  <input type="hidden" name="managersOfTab" value="${tabSettingForm.managersOfTab}"/>
  
  <table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td>Tabs:</td>
        <td>&nbsp;</td>
        <td>List of managers:</td>
        <td>&nbsp;</td>
    </tr>
        <tr align=left>
          <td width="132" rowspan="7">
            <select size="12" name="selectedTab" style="width: 120px" multiple="multiple" onclick='changeTab("frmTabSetting")'>
            <c:forEach var="tab" items="${tabSettingForm.tabFormList}">
                <option value="${tab.key}">${tab.name}</option>
            </c:forEach>
           </select>
          </td>
            <td width="35">&nbsp;</td>
            <td width="167" rowspan="7">
            <textarea name="emailManagers" cols="32" rows="12">${tabSettingForm.emailManagers}</textarea>
         </td>
            <td>&nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35">
            <img border="0" src="pages/images/to-top.png" width="24" height="24"></td>
            <td>
            &nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35"><img border="0" src="pages/images/up.png" width="24" height="24" onclick='moveOptionsUp("frmTabSetting")'></td>
            <td>&nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35"><img border="0" src="pages/images/down.png" width="24" height="24" onclick='moveOptionsDown("frmTabSetting")'></td>
            <td>&nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35">
            <img border="0" src="pages/images/to-bottom.png" width="24" height="24"></td>
            <td>
            &nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35">&nbsp; </td>
            <td>&nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35"><img border="0" src="pages/images/Delete18x18.gif" width="18" height="18" onclick='deleteTab("frmTabSetting")'></td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td colspan=2><input type="text" name="newTab" size="20"><input type="button" value="Add Tab" name="Add" onclick='addTab("frmTabSetting")'></td>
            <td colspan=2><input type="button" value="Update" name="Update" onclick='updateManager("frmTabSetting")'></td>
        </tr>
    </table>
    <p style="text-align: left">
    <input type="button" value="Submit" name="Submit" onclick='selectAllTab("frmTabSetting", ","); submitAction("frmTabSetting","TabSetting", "save");'>
    <input type="button" value="Cancel" name="Cancel"></p>
</form>