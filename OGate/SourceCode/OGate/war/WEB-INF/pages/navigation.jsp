<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%-- For intro tab --%>
<c:set var="nav_intro_med_highlight" value="highlight"/>
<c:set var="nav_intro_dark_highlight" value="highlight"/>
<c:set var="nav_intro_dark_med" value="med"/>
<c:set var="nav_intro_dark_shadow" value="shadow"/>
<c:set var="nav_intro_active" value="inactive"/>
<c:set var="nav_intro_selected" value=""/>
<c:set var="nav_intro_inactive" value="inactive"/>


<%-- For service tab --%>
<c:set var="nav_service_dark_highlight" value="highlight"/>
<c:set var="nav_service_dark_med" value="med"/>
<c:set var="nav_service_dark_shadow" value="shadow"/>


<c:set var="nav_service_active" value="inactive"/>
<c:set var="nav_service_selected" value=""/>


<c:set var="nav_product_before_selected" value="after_inactive"/>

<c:if test='${MainScreen == "Service"}'>
  <c:set var="nav_service_dark_highlight" value="dark" />
  <c:set var="nav_service_dark_med" value="dark" />
  <c:set var="nav_service_dark_shadow" value="dark" />

  <c:set var="nav_service_active" value="active" />
  <c:set var="nav_service_selected" value="-selected" />
  <c:set var="nav_product_before_selected" value="before_active" />
</c:if>

<c:if test='${MainScreen == "Introduction"}'>
  <c:set var="nav_intro_med_highlight" value="med"/>
  <c:set var="nav_intro_dark_highlight" value="dark" />
  <c:set var="nav_intro_dark_med" value="dark" />
  <c:set var="nav_intro_dark_shadow" value="dark" />

  <c:set var="nav_intro_active" value="active" />
  <c:set var="nav_intro_selected" value="-selected" />
  <c:set var="nav_intro_inactive" value="active"/>
</c:if>

<c:if test='${MainScreen == "Member"}'>
  <c:set var="nav_intro_med_highlight" value="med"/>
  <c:set var="nav_intro_dark_highlight" value="dark" />
  <c:set var="nav_intro_dark_med" value="dark" />
  <c:set var="nav_intro_dark_shadow" value="dark" />

  <c:set var="nav_intro_active" value="active" />
  <c:set var="nav_intro_selected" value="-selected" />
  <c:set var="nav_intro_inactive" value="active"/>
</c:if>

<form name="frmNavigation" action="main.do" method="post">
      <input type="hidden" name="screenId" value="Navigation" />
       <input type="hidden" name="eventId" value="" />
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr height="20" align="left" valign="top">
      <td width="99%" colspan="3" class=uportal-background-dark noWrap
        align=left valign="top" height="1">
        <table border=0 cellSpacing=0 cellPadding=0 height="1">
          <tr>
            <td class=uportal-background-med noWrap align=left
              height="1">
              <TABLE border=0 cellSpacing=0 cellPadding=0>
                <TR>
                  <TD class=uportal-background-${nav_intro_med_highlight} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1 height=1></TD>
                  <TD class=uportal-background-${nav_intro_dark_med} rowSpan=4 width=22><IMG alt="" src="pages/images/after_${nav_intro_inactive}_tab.gif" width=22 height=23></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_intro_dark_med} height=20 vAlign=center noWrap><IMG alt="" src="pages/images/transparent.gif" width=10
                    height=10> <A class=uportal-tab-text${nav_intro_selected} href="#" onclick='submitAction("frmNavigation","Introduction", "clickIntroduction")'>Giới thiệu</A><IMG alt="" src="pages/images/transparent.gif" width=10
                    height=10></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_intro_dark_shadow} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_intro_dark_highlight} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
              </TABLE></td>

            <td class=uportal-background-med noWrap align=left>
              <TABLE border=0 cellSpacing=0 cellPadding=0>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                  <TD class=uportal-background-med rowSpan=4 width=22><IMG alt="" src="pages/images/${nav_product_before_selected}_tab.gif" width=22 height=23></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-med height=20 vAlign=center noWrap><IMG alt=""
                    src="pages/images/transparent.gif" width=10
                    height=10> <A class=uportal-tab-text href="#">Sản phẩm</A><IMG alt="" src="pages/images/transparent.gif"
                    width=10 height=10></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-shadow noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
              </TABLE></td>
            <td>
              <TABLE border=0 cellSpacing=0 cellPadding=0>
                <TR>
                  <TD class=uportal-background-${nav_service_dark_highlight} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1  height=1></TD>
                  <TD class=uportal-background-med rowSpan=4 width=22><IMG alt="" src="pages/images/after_${nav_service_active}_tab.gif" width=22 height=23></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_service_dark_med} height=20 vAlign=center noWrap><IMG alt="" src="pages/images/transparent.gif" width=10 height=10><A class=uportal-tab-text${nav_service_selected} href="#" onclick='submitAction("frmNavigation","Navigation", "clickService")'>Dịch vụ</A>
                    <IMG alt="" src="pages/images/transparent.gif" width=10 height=10></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_service_dark_shadow} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1 height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_service_dark_highlight} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1 height=1></TD>
                </TR>
              </TABLE></td>
              <td>
              <TABLE border=0 cellSpacing=0 cellPadding=0>
                <TR>
                  <TD class=uportal-background-${nav_service_dark_highlight} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1  height=1></TD>
                  <TD class=uportal-background-med rowSpan=4 width=22><IMG alt="" src="pages/images/after_${nav_service_active}_tab.gif" width=22 height=23></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_service_dark_med} height=20 vAlign=center noWrap><IMG alt="" src="pages/images/transparent.gif" width=10 height=10><A class=uportal-tab-text${nav_service_selected} href="#" onclick='submitAction("frmNavigation","Navigation", "clickMember")'>Thành viên</A>
                    <IMG alt="" src="pages/images/transparent.gif" width=10 height=10></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_service_dark_shadow} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1 height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_service_dark_highlight} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1 height=1></TD>
                </TR>
              </TABLE></td>
            <td>
              <TABLE border=0 cellSpacing=0 cellPadding=0>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                  <TD class=uportal-background-med rowSpan=4 width=22><IMG
                    alt="" src="pages/images/after_inactive_tab.gif"
                    width=22 height=23></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-med height=20
                    vAlign=center noWrap><IMG alt=""
                    src="pages/images/transparent.gif" width=10
                    height=10> <A class=uportal-tab-text href="#">Diễn đàn</A><IMG alt="" src="pages/images/transparent.gif"
                    width=10 height=10></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-shadow noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
              </TABLE></td>
            <td>
              <TABLE border=0 cellSpacing=0 cellPadding=0>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                  <TD class=uportal-background-med rowSpan=4 width=22><IMG
                    alt="" src="pages/images/after_inactive_tab.gif"
                    width=22 height=23></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-med height=20
                    vAlign=center noWrap><IMG alt=""
                    src="pages/images/transparent.gif" width=10
                    height=10> <A class=uportal-tab-text href="#">Hoạt động</A><IMG alt="" src="pages/images/transparent.gif"
                    width=10 height=10></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-shadow noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
              </TABLE></td>
            <td>
              <TABLE border=0 cellSpacing=0 cellPadding=0>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                  <TD class=uportal-background-med rowSpan=4 width=22><IMG
                    alt="" src="pages/images/after_inactive_tab.gif"
                    width=22 height=23></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-med height=20
                    vAlign=center noWrap><IMG alt=""
                    src="pages/images/transparent.gif" width=10
                    height=10> <A class=uportal-tab-text href="#">Dự
                      án</A><IMG alt="" src="pages/images/transparent.gif"
                    width=10 height=10></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-shadow noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
              </TABLE></td>
            <TD class=uportal-background-med width="100%" noWrap
              align="right">
              <TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-channel-text height=20 vAlign=center
                    align="right" noWrap>Số thành viên: 60</TD>
                </TR>
                <TR>
                  <TD class=uportal-background-shadow noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
              </TABLE></TD>
          </tr>
        </table></td>
    </tr>
    </table>
    </form>