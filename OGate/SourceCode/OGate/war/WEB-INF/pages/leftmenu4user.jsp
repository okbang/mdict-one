<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
      alt="" size=12> </P></TD>
    <TD noWrap>
      <DIV align=right><IMG title="Expand All" onclick="expandAll(acc);return false;" alt="Expand All" align=absMiddle src="pages/images/expand-collapse.gif"> 
      <IMG title="Collapse All" onclick="collapseAll(acc);return false;" alt="Collapse All" align=absMiddle src="pages/images/collapse-expand.gif"> 
      <IMG title="Hide Menu" onclick=toggleNav(); alt="Hide Menu" 
      align=absMiddle 
      src="pages/images/control_rewind_blue.gif"></DIV></TD></TR></TBODY>
</TABLE>
</DIV>
<DIV style="BACKGROUND-COLOR: #f7f7fd; WIDTH: 100%; OVERFLOW: auto" id=mynav class=x-dock-panel>
<DIV id=linkpanel>
<DIV id=linkpanel-header style="display: block" class=selected title=pages/images/ed_link.gif>Liên kết</DIV>
 <DIV id=linkpanel-content class="x-dock-panel-body x-dock-panel-body-expanded">
   <A class=linkitem href="http://open-ones.blogspot.com" onclick="window.open('http://open-ones.blogspot.com');">${applicationScope.Blog}</A>
  <A class=linkitem href="http://groups.google.com/group/open-ones" onclick="window.open('http://groups.google.com/group/open-ones');">${applicationScope.Group}</A>
  <A class=linkitem href="http://open-ones.googlecode.com" onclick="window.open('http://open-ones.googlecode.com');">${applicationScope.GoogleCode}</A>
 </DIV>
</DIV>
<DIV id=modulepanel>
<DIV id=modulepanel-header title=pages/images/package.gif>Chức năng</DIV>
 <DIV id=modulepanel-content class="x-dock-panel-body x-dock-panel-body-expanded">
  <A class=linkitem href="/">Giới thiệu</A>
  <A class=linkitem href="/">Dịch vụ</A>
  <A class=linkitem href="/">Hoạt động</A>
  <A class=linkitem href="/">Thành viên</A> 
 </DIV>
</DIV>
<DIV id=profilepanel>
 <DIV id=profilepanel-header title=pages/images/wrench.gif>Hồ sơ cá nhân</DIV>  
 <DIV id=profilepanel-content class="x-dock-panel-body x-dock-panel-body-expanded">
   <A class=linkitem href="/">Thông tin chung</A>
   <A class=linkitem href="/">Cập nhật CV</A>
   <A class=linkitem href="/">Kết nối bạn bè</A>
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
