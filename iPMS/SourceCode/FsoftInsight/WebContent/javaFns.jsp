<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.tools.*, com.fms1.web.* ,com.fms1.html.*"%>
function mandatoryFld(control,alert){
	if (trim(control.value)==""){
		window.alert(getParamText(new Array("<%=((LanguageChoose)session.getAttribute("LanguageChoose")).getMessage("fi.jsp.javaFns.ismandatory")%>",alert)));
		control.focus();
		return false;
	}
	return true;
}

function mandatorySelect(control,alert){
	if (control.value==0){
		window.alert(getParamText(new Array("<%=((LanguageChoose)session.getAttribute("LanguageChoose")).getMessage("fi.jsp.javaFns.ismandatory")%>",alert)));
		control.focus();
		return false;
	}
	return true;
}

function beforeTodayFld(control,alert){
	if (!dateFld(control,alert))
		return false;
	else if (control.value!=""){
		if (compareToToday(control.value)>0){
			window.alert(getParamText(new Array("<%=((LanguageChoose)session.getAttribute("LanguageChoose")).getMessage("fi.jsp.javaFns.MustBeBeforeOrEqualToToday")%>",alert)));
			control.focus();
			return false;
		}
		return true;
	}
	else
		return true;
}
function maxLength(control,alert,length){
	fieldLength =trim(control.value).length
	if (fieldLength>length){
		window.alert(getParamText(new Array("<%=((LanguageChoose)session.getAttribute("LanguageChoose")).getMessage("fi.jsp.javaFns.~PARAM1_NAME~islimitedto~PARAM2_NUM~characterscurrently~PARAM3_NUM~")%>",alert,length,fieldLength)));
		control.focus();
		return false;
	}
	return true;
}
function dateFld(control,alert){
	if((control.value !="")&&(!isDate(control.value))) {  	  
  	  	window.alert(getParamText(new Array("<%=((LanguageChoose)session.getAttribute("LanguageChoose")).getMessage("fi.jsp.javaFns.InvalidDateFormatFor~PARAM1_NAME~")%>",alert)));
  	 	control.focus();
  		return false;
  	 }
  	 return true;
}
function numberFld(control,alert){
	mystr =trim(control.value);
	if((mystr !="")&&( isNaN(mystr) || !goodNumberFormat(mystr) ) ) {  	  
  	  	window.alert(getParamText(new Array("<%=((LanguageChoose)session.getAttribute("LanguageChoose")).getMessage("fi.jsp.javaFns.InvalidNumberFor~PARAM1_NAME~")%>",alert)));
  	 	control.focus();
  		return false;
  	 }
  	 return true;
}
function integerFld(control,alert){
	if(!numberFld(control,alert)) {  	  
  		return false;
  	 }
  	 if ((trim(control.value) !="") && (control.value.indexOf(".")>=0)){
  	 	window.alert(getParamText(new Array("<%=((LanguageChoose)session.getAttribute("LanguageChoose")).getMessage("fi.jsp.javaFns.mustBeAnInteger")%>",alert)));
  	  	control.focus();
  	 	return false;
  	 }
  	 return true;
}
function positiveFld(control,alert){
	if(!numberFld(control,alert)) {  	  
  		return false;
  	 }
  	 if ((trim(control.value) !="") && (control.value <0)){
  	 	window.alert(getParamText(new Array("<%=((LanguageChoose)session.getAttribute("LanguageChoose")).getMessage("fi.jsp.javaFns.mustbeapositivenumber")%>",alert)));
  	 	control.focus();
  	 	return false;
  	 }
  	 return true;
}
function percentageFld(control,alert){
	if(!numberFld(control,alert)) {  	  
  		return false;
  	 }
  	 if ((trim(control.value) !="") && ((control.value <0)||(control.value >100))){
  	 	window.alert(getParamText(new Array("<%=((LanguageChoose)session.getAttribute("LanguageChoose")).getMessage("fi.jsp.javaFns.mustBeAPositiveNumberBelowOrEqualTo100")%>",alert)));
  	 	control.focus();
  	 	return false;
  	 }
  	 return true;
}
