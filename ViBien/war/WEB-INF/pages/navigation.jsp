<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%-- For intro tab --%>
<c:set var="nav_intro_med_highlight" value="highlight"/>
<c:set var="nav_intro_dark_highlight" value="highlight"/>
<c:set var="nav_intro_dark_med" value="med"/>
<c:set var="nav_intro_dark_shadow" value="shadow"/>
<c:set var="nav_intro_active" value="after_inactive"/>
<c:set var="nav_intro_selected" value=""/>
<c:set var="nav_intro_inactive" value="inactive"/>

<%-- For menu tab --%>
<c:set var="nav_menu_dark_highlight" value="highlight"/>
<c:set var="nav_menu_dark_med" value="med"/>
<c:set var="nav_menu_dark_shadow" value="shadow"/>
<c:set var="nav_menu_active" value="after_inactive"/>
<c:set var="nav_menu_selected" value=""/>

<%-- For contact tab --%>
<c:set var="nav_contact_dark_highlight" value="highlight"/>
<c:set var="nav_contact_dark_med" value="med"/>
<c:set var="nav_contact_dark_shadow" value="shadow"/>
<c:set var="nav_contact_active" value="after_inactive"/>
<c:set var="nav_contact_selected" value=""/>

<c:if test='${MainScreen == "Intro"}'>
  <c:set var="nav_intro_med_highlight" value="med"/>
  <c:set var="nav_intro_dark_highlight" value="dark" />
  <c:set var="nav_intro_dark_med" value="dark" />
  <c:set var="nav_intro_dark_shadow" value="dark" />
  <c:set var="nav_intro_active" value="after_active" />
  <c:set var="nav_intro_selected" value="-selected" />
  <c:set var="nav_intro_inactive" value="active"/>
</c:if>

<c:if test='${MainScreen == "Menu"}'>
  <c:set var="nav_menu_dark_highlight" value="dark" />
  <c:set var="nav_menu_dark_med" value="dark" />
  <c:set var="nav_menu_dark_shadow" value="dark" />
  <c:set var="nav_menu_active" value="after_active" />
  <c:set var="nav_menu_selected" value="-selected" />
  <c:set var="nav_intro_active" value="before_active" />
</c:if>

<c:if test='${MainScreen == "Contact"}'>
  <c:set var="nav_contact_dark_highlight" value="dark" />
  <c:set var="nav_contact_dark_med" value="dark" />
  <c:set var="nav_contact_dark_shadow" value="dark" />
  <c:set var="nav_contact_active" value="after_active" />
  <c:set var="nav_contact_selected" value="-selected" />
  <c:set var="nav_menu_active" value="before_active" />
</c:if>

<form name="frmNavigation" action="navigation.do" method="POST">
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
                  <TD class=uportal-background-${nav_intro_dark_med} rowSpan=4 width=22><IMG alt="" src="pages/images/${nav_intro_active}_tab.gif" width=22 height=23></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_intro_dark_med} height=20 vAlign=center noWrap><IMG alt="" src="pages/images/transparent.gif" width=10
                    height=10> <A class=uportal-tab-text${nav_intro_selected} href="#" onclick='submitAction("frmNavigation","Intro", "clickIntro")'>Giới thiệu</A><IMG alt="" src="pages/images/transparent.gif" width=10
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

            <td>
              <TABLE border=0 cellSpacing=0 cellPadding=0>
                <TR>
                  <TD class=uportal-background-${nav_menu_dark_highlight} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1  height=1></TD>
                  <TD class=uportal-background-${nav_menu_dark_med} rowSpan=4 width=22><IMG alt="" src="pages/images/${nav_menu_active}_tab.gif" width=22 height=23></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_menu_dark_med} height=20 vAlign=center noWrap><IMG alt="" src="pages/images/transparent.gif" width=10 height=10>
                  <A class=uportal-tab-text${nav_menu_selected} href="#" onclick='submitAction("frmNavigation","Navigation", "clickMenu")'>Thực đơn</A>
                    <IMG alt="" src="pages/images/transparent.gif" width=10 height=10></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_menu_dark_shadow} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1 height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_menu_dark_highlight} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1 height=1></TD>
                </TR>
              </TABLE></td>
            <td>
              <TABLE border=0 cellSpacing=0 cellPadding=0>
                <TR>
                  <TD class=uportal-background-${nav_contact_dark_highlight} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1 height=1></TD>
                  <TD class=uportal-background-${nav_contact_dark_med} rowSpan=4 width=22><IMG alt="" src="pages/images/${nav_contact_active}_tab.gif" width=22 height=23></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_contact_dark_med} height=20 vAlign=center noWrap><IMG alt="" src="pages/images/transparent.gif" width=10
                    height=10> <A class=uportal-tab-text href="#" onclick='submitAction("frmNavigation","Navigation", "clickContact")'>Liên hệ</A><IMG alt="" src="pages/images/transparent.gif"
                    width=10 height=10></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_contact_dark_shadow} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1 height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-${nav_contact_dark_highlight} noWrap><IMG alt="" src="pages/images/transparent.gif" width=1 height=1></TD>
                </TR>
              </TABLE></td>
            <TD class=uportal-background-med width="100%" noWrap
              align="right">
              <TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-channel-text height=20 vAlign=center align="right" noWrap></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-shadow noWrap><IMG alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
              </TABLE></TD>
          </tr>
        </table></td>
    </tr>
    </table>
    </form>