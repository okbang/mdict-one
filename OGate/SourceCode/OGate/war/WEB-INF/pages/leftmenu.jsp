<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<form name="frmMenu" action="menu.do">
  <input type="hidden" name="screenId" value="Menu"/>
  <input type="hidden" name="eventId" value=""/>
  <input type="hidden" name="menuId" value=""/>
  <input type="hidden" name="tabKey" value=""/>
  <input type="hidden" name="moduleId" value=""/>

<DIV style="POSITION: absolute; WIDTH: 208px; HEIGHT: 400px; TOP: 100px; LEFT: 0px" id=leftPanel class="x-layout-panel x-layout-panel-west x-layout-panel-body">
<DIV style="WIDTH: 208px; HEIGHT: 100%; OVERFLOW: visible" id=west class=x-layout-active-content>
    <DIV id=mylogo>
        <TABLE style="BORDER-BOTTOM: #a9bfd3 1px solid; BORDER-TOP: #ffffff 1px solid" cellSpacing=0 cellPadding=0 width="100%">
          <TBODY>
          <TR>
            <TD>Thực đơn</TD></TR>
           </TBODY>
        </TABLE>
    </DIV>
<DIV id=mytoolbar class="x-dock-panel x-dock-panel-text x-dock-panel-title-expanded x-layout-panel-hd">
<TABLE cellSpacing=0 cellPadding=1>
  <TBODY>
  <TR>
    <TD width="100%">
      <P>&nbsp;&nbsp;Tìm&nbsp;&nbsp; <INPUT style="PADDING-BOTTOM: 1px; MARGIN: 0pt; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 1px" 
      id=find-field title="Nhập ít nhất 3 ký tự để tìm kiếm chức năng." 
      onkeyup="searchPanels(acc, this);" 
      alt="" 
      size=12> </P></TD>
    <TD noWrap>
      <DIV align=right><IMG title="Expand All" onclick="expandAll(acc);return false;" alt="Expand All" align=absMiddle src="pages/images/expand-collapse.gif"> 
      <IMG title="Collapse All" onclick="collapseAll(acc);return false;" alt="Collapse All" align=absMiddle src="pages/images/collapse-expand.gif"> 
      <IMG title="Hide Menu" onclick=toggleNav(); alt="Hide Menu" 
      align=absMiddle src="pages/images/control_rewind_blue.gif"></DIV></TD></TR></TBODY>
</TABLE>
</DIV>
<DIV style="BACKGROUND-COLOR: #f7f7fd; WIDTH: 100%; OVERFLOW: auto" id=mynav class=x-dock-panel>
<DIV id=accpanel>
<DIV id=accpanel-header class=selected title=pages/images/package.gif>Nội dung tab</DIV>
 <DIV id=accpanel-content class="x-dock-panel-body x-dock-panel-body-expanded">
  <c:forEach var="tab" items="${moduleTabs}">
    <A class=linkitem href="/" onclick='submitMenu("frmMenu","Menu", "selectItem", "${tab.id}", "${tab.key}")' title="${tab.name}">${tab.name}</A>
  </c:forEach>
 </DIV>
</DIV>
<DIV id=accpanel2>
 <DIV id=accpanel2-header title=pages/images/wrench.gif>Cấu hình chung</DIV>  
 <DIV id=accpanel2-content class="x-dock-panel-body x-dock-panel-body-expanded">
   <A class=linkitem href="/" onclick='submitMenu("frmMenu","Menu", "ciTabSetting", "tabSetting", "tabSetting")' title="Cấu hình các thanh tab">Hệ thống tab</A>
   <A class=linkitem href="/" onclick='submitMenu("frmMenu","Menu", "ciAccSetting", "accSetting", "tabSetting")' title="Thiết lập quyền để quản lý các module">Tài khoản quản trị</A>
   <A class=linkitem href="/" onclick='submitMenu("frmMenu","Menu", "ciLangSetting", "langSetting", "tabSetting")' title="Cấu hình ngôn ngữ">Ngôn ngữ</A>
 </DIV>
</DIV>
<DIV id=accpanel3>
 <DIV id=accpanel3-header title=pages/images/layout.gif>Layout</DIV>  
 <DIV id=accpanel3-content class="x-dock-panel-body x-dock-panel-body-expanded">
   <A class=linkitem href="/" onclick='submitMenuLayout("frmMenu","PartEdit", "changeLogo", "moduleLogo", "Logo")' title="Cấu hình logo">Logo</A>
   <A class=linkitem href="/" onclick='submitMenuLayout("frmMenu","PartEdit", "changeSlogan", "moduleSlogan", "Sologan")' title="Cấu hình Slogan">Slogan</A>
   <A class=linkitem href="/" onclick='submitMenuLayout("frmMenu","PartEdit", "changeSponsor", "moduleSponsor", "Sponsor")' title="Cập nhật đơn vị tài trợ">Đơn vị tài trợ</A>
   <A class=linkitem href="/" onclick='submitMenuLayout("frmMenu","PartEdit", "changeHotNews", "moduleHotNews", "HostNew")' title="Cập nhật tin nổi bật">Tin nổi bật</A>
   <A class=linkitem href="/" onclick='submitMenuLayout("frmMenu","PartEdit", "changeLeftAdv", "moduleLeftAdv", "LeftAdv")' title="Cập nhật quảng cáo bên trái">Quảng cáo bên trái</A>
   <A class=linkitem href="/" onclick='submitMenuLayout("frmMenu","PartEdit", "changeLink", "moduleLink", "Link")' title="Cập nhật liên kết">Liên kết</A>
   <A class=linkitem href="/" onclick='submitMenuLayout("frmMenu","PartEdit", "changeRightAdv", "moduleRightAdv", "RightAdv")' title="Cập nhật quảng cáo bên phải">Quảng cáo bên phải</A>
   <A class=linkitem href="/" onclick='submitMenuLayout("frmMenu","PartEdit", "changeFooter", "moduleFooter", "Footer")' title="Cập nhật footer">Cập nhật footer</A>
 </DIV>
</DIV>

</DIV></DIV></DIV>

<!-- collapsed bar //-->
<DIV style="WIDTH: 20px; DISPLAY: none; HEIGHT: 400px; VISIBILITY: visible; TOP: 100px; LEFT: 2px" 
     id=collapsedNav class="x-layout-collapsed x-layout-collapsed-west" 
     onmouseover="addStyle(this, 'x-layout-collapsed-over');" 
     onmouseout="remStyle(this, 'x-layout-collapsed-over');" onclick=toggleNav();>
  <DIV class=x-layout-collapsed-tools>
    <DIV class=x-layout-ctools-inner>
      <DIV id=ext-gen19 class=x-layout-tools-button>
        <DIV class="x-layout-tools-button-inner x-layout-expand-west"></DIV>
      </DIV>
    </DIV>
  </DIV>
</DIV>
<DIV style="HEIGHT: 400px; VISIBILITY: visible; TOP: 100px; LEFT: 210px" id=ext-gen7-split class="x-layout-split x-layout-split-west x-splitbar-h x-layout-split-h" onclick=toggleNav();>
</DIV>
</form>