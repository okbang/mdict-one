/**
*Avoid using variable i (and other common names like j,k) in functions, this causes i variable of the calling page to be updated (bug jscript of IE5)
*
*/

var GET_PAGE=3660;
var root="Fms1Servlet?reqType=";
var grproot="../Fms1Servlet?reqType=";
/*
function trim(s) {
    var z, sRetVal = "";
    z = s.length - 1;
    while ((z >= 0) && trimChar(s.charAt(z))) {
        z--;
    }
    s = s.substring(0, z + 1); // trim blanks on the right
    z = 0;
    while ((z < s.length) && trimChar(s.charAt(z))) {
        z++;
    }
    return s.substring(z);
}
function trimChar(s) {
	return (s== ' '||s == '\n'||s == '\f'||s == '\r')
}
*/
function trim(str) {
    return str.replace(/^\s*|\s*$/g,"");
}

function monthConversion(sMonth) {
	var nMonth;
        if (sMonth.toLowerCase() == "jan")
        	nMonth = 1;
        else if (sMonth.toLowerCase() == "feb")
        	nMonth = 2;
        else if (sMonth.toLowerCase() == "mar")
        	nMonth = 3;
        else if (sMonth.toLowerCase() == "apr")
        	nMonth = 4;
        else if (sMonth.toLowerCase() == "may")
        	nMonth = 5;
        else if (sMonth.toLowerCase() == "jun")
        	nMonth = 6;
        else if (sMonth.toLowerCase() == "jul")
        	nMonth = 7;
        else if (sMonth.toLowerCase() == "aug")
        	nMonth = 8;
        else if (sMonth.toLowerCase() == "sep")
        	nMonth = 9;
        else if (sMonth.toLowerCase() == "oct")
        	nMonth = 10;
        else if (sMonth.toLowerCase() == "nov")
        	nMonth = 11;
        else if (sMonth.toLowerCase() == "dec")
        	nMonth = 12;
        else
        	nMonth = 0;
	return nMonth;
}

function isDate( s )
{
        var sDay, sMonth, sYear, nMonth, nDay, nYear, nSep1, nSep2;
        nSep1 = s.indexOf( "-" ); if ( nSep1 < 0 ) return false;
        nSep2 = s.lastIndexOf( "-" ); if ( nSep2 < 0 ) return false;
        if ( nSep1 == nSep2 ) return false;

        sDay = s.substring( 0, nSep1  );
        sMonth = s.substring( nSep1 + 1, nSep2 );
        sYear = s.substring( nSep2+1 );

        if ( !sMonth.length || !sDay.length || !sYear.length ) return false;
        if ( /*isNaN(sMonth) ||*/ isNaN(sDay) || isNaN(sYear) ) return false;
        if (!(sMonth.length==3) || !(sDay.length==2) || !(sYear.length==2)) return false;

        //nMonth = parseInt(sMonth,10);
        nMonth = monthConversion(sMonth);
        nDay = parseInt(sDay,10);
        nYear = parseInt(sYear,10);

        if ((nYear > 60) && (nYear <= 99))
        	nYear = 1900 + nYear;
        else if (nYear <= 60)
        	nYear = 2000 + nYear;
        
        if ( nMonth<=0 || nDay<=0 || nYear<=0 ) return false;
        if ( nMonth > 12 ) return false;
        if (nMonth==1 || nMonth==3 || nMonth==5 || nMonth==7 || nMonth==8 || nMonth==10 || nMonth==12 )
          if ( nDay > 31 ) return false;
        if (nMonth==4 || nMonth==6 || nMonth==9 || nMonth==11 )
          if ( nDay > 30 ) return false;
        if (nMonth==2) {
          if ( (nYear % 4 == 0) && (nYear % 100 != 0)) { // leap year
            if ( nDay > 29 ) return false;
          }
          else if (nYear % 100 == 0 )
          {
            if (nYear % 400 ==0) {
              if (nDay > 29) return false;
            }
            else if (nDay > 28) {
              return false;
            }
          }
          else if (nDay > 28 )
          {
            return false;
          }
        }
        return true;
} // isDate function

function compareDate( sStartDate, sEndDate)
{
        //sStartDate > sEndDate -> return -1
        //sStartDate = sEndDate -> return 0
        //sStartDate < sEndDate -> return 1

        var f, startDay, startMonth, startYear, endMonth, endDay, endYear, n1, n2,n3, n4;
        n1 = sStartDate.indexOf( "-" );
        n2 = sStartDate.lastIndexOf( "-" );
        n3 = sEndDate.indexOf( "-" );
        n4 = sEndDate.lastIndexOf( "-" );

        startDay = parseInt( sStartDate.substring( 0, n1  ),10);
        //startMonth = parseInt( sStartDate.substring( n1 + 1, n2 ),10);
        startMonth = monthConversion( sStartDate.substring( n1 + 1, n2 ) );
        startYear= parseInt( sStartDate.substring( n2+1 ),10);

        endDay = parseInt( sEndDate.substring( 0, n3  ),10);
        //endMonth = parseInt( sEndDate.substring( n3+1, n4 ),10);
        endMonth = monthConversion( sEndDate.substring( n3+1, n4 ) );
        endYear = parseInt( sEndDate.substring( n4+1 ),10);
        
        date = new Date();


        if ( startYear > endYear ) return -1;
        if ( startYear < endYear ) return 1;
        // Now startYear == endYear
        if ( startMonth > endMonth ) return -1;
        if ( startMonth < endMonth ) return 1;
        // Now startMonth == endMonth
        if ( startDay > endDay ) return -1;
        if ( startDay < endDay ) return 1;
        return 0;


} // compareDate

function compareToToday(sDate)
{
        var d,n1,n2, nMonth, nYear, nDay, nCurrMonth, nCurrYear, nCurrDay;

        n1 = sDate.indexOf( "-" );
        n2 = sDate.lastIndexOf( "-" );
        nDay = parseInt(  sDate.substring( 0, n1  ),10 );
        //nMonth = parseInt(  sDate.substring( n1+1, n2  ),10);
        nMonth = monthConversion(  sDate.substring( n1+1, n2  ) );
        nYear = parseInt(  sDate.substring(n2+1),10);
        if ((nYear > 60) && (nYear <= 99))
        	nYear = 1900 + nYear;
        else if (nYear <= 60)
        	nYear = 2000 + nYear;
        d = new Date();
        nCurrMonth = (d.getMonth() + 1);
        nCurrDay = d.getDate() ;
        nCurrYear = d.getFullYear();        

        if ( nYear > nCurrYear ) return 1;
        if ( nYear < nCurrYear ) return -1;
        // Now nYear == nCurrYear
        if ( nMonth > nCurrMonth ) return 1;
        if ( nMonth < nCurrMonth ) return -1;
        // Now nMonth == nCurrMonth
        if ( nDay > nCurrDay ) return 1;
        if ( nDay < nCurrDay ) return -1;
        return 0;

}
//HuyNH2 add function 
	function getMonth(val) {
		
		var monthArray = new Array("JAN","FEB","MAR","APR","MAY","JUN",
			"JUL","AUG","SEP","OCT","NOV","DEC");
		for (var i=0; i<monthArray.length; i++) {
			if (monthArray[i].toUpperCase() == val.toUpperCase()) {
				return(i);
			}
		}
		return(-1);
	}
	// check data validation with dd-mon-yyyy format
	function checkDateWithFullYear(fld) {
		var success = true;
		var mo, day, yy, testDate;
		var val = fld;
		var re = new RegExp("[0-9]{2}[-][A-Z,a-z]{3}[-][0-9]{4}", "g");
		if (re.test(val)) {
			var delimChar = "-";
			var delim1 = val.indexOf(delimChar);
			var delim2 = val.lastIndexOf(delimChar);
			day = parseInt(val.substring(0,delim1),10);
			mo = getMonth(val.substring(delim1+1,delim2),10);
			yy = parseInt(val.substring(delim2+1),10);
			testDate = new Date(yy,mo,day);
			if (testDate.getDate() == day) {
				if (testDate.getMonth() == mo) {
					if (!testDate.getFullYear() == yy) {
						alert("Invalid year entry.");
						success = false;
					}
				}
				else {
					alert("Invalid month entry.");
					success = false;
				}
			}
			else {
				alert("Invalid day entry.");
				success = false;
			}
		}
		else {
			alert("Incorrect date format. Enter as dd-mmm-yyyy.");
			success = false;
		}
		return(success);
	}
//End 


 

function showObj(lId)
{
  var ob;ob=new Array;
  var appVer=parseInt(navigator.appVersion);
  var isNC=false,isN6=false,isIE=false;
  if (document.all && appVer >= 4) isIE=true; else
    if (document.getElementById && appVer > 4) isN6=true; else
      if (document.layers && appVer >= 4) isNC=true;
  if (isNC)
  {
    w_str = "document." + lId;ob[lId] = eval(w_str);
    if (!ob[lId]) ob[lId] = findHiddenObj(document, lId);
    if (ob[lId]) ob[lId].visibility = "show";
  }
  if (isN6)
  {
    ob[lId] = document.getElementById(lId);
    ob[lId].style.visibility = "visible";
  }
  if (isIE)
  {
    w_str = "document.all.item(\"" + lId + "\").style";ob[lId] = eval(w_str);
    ob[lId].visibility = "visible";
  }
}


function hideObj(lId)
{
  var ob;ob=new Array;
  var appVer=parseInt(navigator.appVersion);
  var isNC=false,isN6=false,isIE=false;
  if (document.all && appVer >= 4) isIE=true; else
    if (document.getElementById && appVer > 4) isN6=true; else
      if (document.layers && appVer >= 4) isNC=true;
  if (isNC)
  {
    w_str = "document." + lId;ob[lId] = eval(w_str);
    if (!ob[lId]) ob[lId] = findShownObj(document, lId);
    if (ob[lId]) ob[lId].visibility = "hide";
  }
  if (isN6)
  {
    ob[lId] = document.getElementById(lId);
    ob[lId].style.visibility = "hidden";
  }
  if (isIE)
  {
    w_str = "document.all.item(\"" + lId + "\").style";ob[lId] = eval(w_str);
    ob[lId].visibility = "hidden";
  }
}
function findHiddenObj(doc, lId)
{
  for (var i=0; i < doc.layers.length; i++)
  {
    var w_str = "doc.layers[i].document." + lId;
    var obj;obj=new Array;
    obj[lId] = eval(w_str);
    if (!obj[lId]) obj[lId] = findHiddenObj(doc.layers[i], lId);
    if (obj[lId]) return obj[lId];
  }
  return null;
}
function findShownObj(doc, lId)
{
  for (var i=0; i < doc.layers.length; i++)
  {
    var w_str = "doc.layers[i].document." + lId;
    var obj;obj=new Array;
    obj[lId] = eval(w_str);
    if (!obj[lId]) obj[lId] = findShownObj(doc.layers[i], lId);
    if (obj[lId]) return obj[lId];
  }
  return null;
}
// ImgPreload:
//
function ImgPreload()
{
  var appVer=parseInt(navigator.appVersion);
  var isNC=false,isN6=false,isIE=false;
  if (document.all && appVer >= 4) isIE=true; else
    if (document.getElementById && appVer > 4) isN6=true; else
      if (document.layers && appVer >= 4) isNC=true;
  if (isNC||isN6||isIE)
  {
    if (document.images)
    {
      var imgName = ImgPreload.arguments[0];
      var cnt;
      var yswImg = new Array();
      for (cnt = 1; cnt < ImgPreload.arguments.length; cnt++)
      {
        yswImg[ImgPreload.arguments[cnt]] = new Image();
        yswImg[ImgPreload.arguments[cnt]].src = ImgPreload.arguments[cnt];
      }
    }
  }
}
// ImgFind:
//
function ImgFind(doc, imgName)
{
  for (var i=0; i < doc.layers.length; i++)
  {
    var img = doc.layers[i].document.images[imgName];
    if (!img) img = ImgFind(doc.layers[i], imgName);
    if (img) return img;
  }
  return null;
}
// ImgSwap:
//
function ImgSwap(imgName, imgSrc)
{
  var appVer=parseInt(navigator.appVersion);
  var isNC=false,isN6=false,isIE=false;
  if (document.all && appVer >= 4) isIE=true; else
    if (document.getElementById && appVer > 4) isN6=true; else
      if (document.layers && appVer >= 4) isNC=true;
  if (isNC||isN6||isIE)
  {
    if (document.images)
    {
      var img = document.images[imgName];
      if (!img) img = ImgFind(document, imgName);
      if (img) img.src = imgSrc;
    }
  }
}
function jumpURL(url) 
{
  if (url != '')
  {
    window.location = url;
  }
}

function doIt(ref) 
{
  jumpURL("Fms1Servlet?reqType="+ref) 
}


function populate(controlToPopulate, itemArray, groupArray, valueArray) {
	var myElement;
	for (var i = controlToPopulate.options.length; i >= 0; i--) {
		controlToPopulate.options[i] = null;
	}
	for (var i = 0; i < itemArray.length; i++) {
		if (groupArray[i] == control.value) {
			myElement = document.createElement("option");
			myElement.value = valueArray[i];
			myElement.text = itemArray[i];
			controlToPopulate.add(myElement);
		}
	}
}
//loads the admin menu
function loadAdminMenu(specificMain,specificSub){
	loadMenu(specificMain,specificSub,"menuDefault");
}
//loads the project menu
function loadPrjMenu(specificMain,specificSub){
	loadMenu(specificMain,specificSub,"menuProject");
}
function loadGrpMenu(specificMain,specificSub){
	loadMenu(specificMain,specificSub,"menuGroup");
}
function loadSQAMenu(specificMain,specificSub){
	loadMenu(specificMain,specificSub,"menuSQA");
}
function loadOrgMenu(specificMain,specificSub){
	loadMenu(specificMain,specificSub,"menuOrg");
}
//only called from the home page
function loadBlankMenu(){
	var theAdress=new String(parent.frames['menu'].location);
	if (theAdress.indexOf("menuBlank")==-1)
		window.open("menuBlank.html", "menu");
	
}
function loadPrjMenuDis(specificMain,specificSub){
	//specificMain=(specificMain)?"?main='"+specificMain+"'":"";
	//specificSub=(specificSub)?"&sub='"+specificSub+"'":"";
	specificMain=(specificMain)?"&main='"+specificMain+"'":"";
	specificSub=(specificSub)?"&sub='"+specificSub+"'":"";
	window.open("Fms1Servlet?reqType="+GET_PAGE+"&page=header.jsp", "header");
	//window.open("menuProject.jsp"+specificMain+specificSub+"&mnuDisable=1", "menu");
	window.open("Fms1Servlet?reqType="+GET_PAGE+"&page=menuProject.jsp"+specificMain+specificSub+"&mnuDisable=1", "menu");
}
function loadMenu(specificMain,specificSub,menu){
	if (!specificMain){
		specificMain = "Home";
	}
	if (!specificSub){
		specificSub = '';
	}
	var theAdress=new String(parent.frames['menu'].location);
	if (theAdress.indexOf(menu)==-1){
		var dir=(window.location.pathname.indexOf('/Group/')!=-1)?"../":"";
		//specificMain=(specificMain)?"?main='"+specificMain+"'":"";
		//specificSub=(specificSub)?"&sub='"+specificSub+"'":"";	
		specificMain=(specificMain)?"&main='"+specificMain+"'":"";
		specificSub=(specificSub)?"&sub='"+specificSub+"'":"";
		window.open(dir+"Fms1Servlet?reqType="+GET_PAGE+"&page=header.jsp", "header");
		//window.open(dir+menu+".jsp"+specificMain+specificSub, "menu");
		window.open(dir+"Fms1Servlet?reqType="+GET_PAGE+"&page="+menu+".jsp"+specificMain+specificSub, "menu");
	}
	else{
		if (specificSub)
			parent.frames['menu'].selectedSub=specificSub;
		if (specificMain)
			parent.frames['menu'].selectedMain=specificMain;
	}
	if (menu=="menuGroup" ||menu=="menuOrg"||menu=="menuSQA"){
		var mnuname=parent.frames['menu'].MenuPCB;
		if (specificMain=="PCB report" && specificSub !="Search/Create")
			eval("parent.frames['menu']."+mnuname +"=new Array('PCB report','Group/pcbReport.jsp','',5);");
		else
			eval("parent.frames['menu']."+mnuname +"=new Array('PCB report','Group/pcbReport.jsp','',1);");
	}
	if(parent.frames['menu'].Go){
		parent.frames['menu'].Go();
	}
}
//input checking functions
function mandatoryFld(control,alert){
	if (trim(control.value)==""){
		window.alert(alert+" is mandatory");
		control.focus();
		return false;
	}
	return true;
}

function mandatorySelect(control,alert){
	if (control.value==0){
		window.alert(alert+" is mandatory");
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
			window.alert(alert+" must be before or equal to today");
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
		window.alert(alert+" is limited to "+length+" characters (currently: "+fieldLength+")");
		control.focus();
		return false;
	}
	return true;
}
function mandatoryDateFld(control,alert){
	if (!mandatoryFld(control,alert)){
		return false;
	}
	return dateFld(control,alert);
}
function dateFld(control,alert){
	if((control.value !="")&&(!isDate(control.value))) {  	  
  	  	window.alert("Invalid date format for "+alert);
  	 	control.focus();
  		return false;
  	 }
  	 return true;
}

//check that the number is not like 1.6E10 of hexa ...
function goodNumberFormat(mystr){
	var ret = true;
	for (x=0;x<mystr.length-1;x++){
		myChar=mystr.charAt(x);
		if ((myChar<"0" || myChar>"9" )&&(myChar != "-")&&(myChar != ".") )
			if(x == 0 && myChar == "+"){
				ret = true;
			}
			else {
				ret = false;
			}
	}
	return ret;
}
function numberFld(control,alert){
	mystr =trim(control.value);
	if((mystr !="")&&( isNaN(mystr) || !goodNumberFormat(mystr) ) ) {  	  
  	  	window.alert("Invalid number for "+alert);
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
        window.alert(alert+" must be an integer");
        control.focus();
        return false;
    }
    return true;
}
// Check positive float, double number
function positiveFld(control,alert){
    if(!numberFld(control,alert)) {  	  
        return false;
    }
    if ((trim(control.value) !="") && (control.value <0)){
        window.alert(alert+" must be a positive number");
        control.focus();
        return false;
    }
    return true;
}
// Check positive integer number
function positiveInt(control,alert){
    if (!positiveFld(control,alert) || !integerFld(control,alert)) {
        return false;
    }
    return true;
}
function percentageFld(control,alert){
	if(!numberFld(control,alert)) {  	  
  		return false;
  	 }
  	 if ((trim(control.value) !="") && ((control.value <0)||(control.value >100))){
  	 	window.alert(alert+" must be a positive number less than or equal to 100");
  	 	control.focus();
  	 	return false;
  	 }
  	 return true;
}
function setPointer(theRow, thePointerColor)
{
    if (thePointerColor == '' || typeof(theRow.style) == 'undefined') {
        return false;
    }
    if (typeof(document.getElementsByTagName) != 'undefined') {
        var theCells = theRow.getElementsByTagName('td');
    }
    else if (typeof(theRow.cells) != 'undefined') {
        var theCells = theRow.cells;
    }
    else {
        return false;
    }

    var rowCellsCnt = theCells.length;
    for (var c = 0; c < rowCellsCnt; c++) {
        theCells[c].style.backgroundColor = thePointerColor;
    }

    return true;
}
function refresh()
{
	thisPage = parent.frames['header'].location.href;
	parent.frames['header'].location = thisPage;
}
//solves the decimal bug in jscript like alert(0.1+0.1+0.1);
function fixstr(num) {
var decs=100;
var numv=num-0;
var sign=(numv>=0?1:-1); 
var numabs=numv*sign;
var naint=Math.floor(numabs);
var nacent=Math.round((numabs-naint)*decs);
if (nacent>=decs) {nacent=0; naint++;}
var nais=''+naint;
var nacs=(nacent<10?'0':'')+nacent;
if (naint+nacent==0) sign=1;
while (nacs.length!=0 && nacs.substring(nacs.length-1) == '0')
	nacs=nacs.substring(0,nacs.length-1)
if (nacs.length!=0)
	nacs ='.'+nacs
return (sign==1?'':'-')+nais+nacs;
}

function ignoreESCkey(){
	if (window.event.keyCode==27){
		window.event.returnValue=0;				
	}
}

function getParamText(args){
	var PARAM = "PARAM";
	var re;
	var i=0;
	var result = args[0];
	for (i=1; i< args.length; i++){
		re = new RegExp("~PARAM" +i+ "_[A-Z a-z 0-9_]+~","g");
		result = result.replace(re,args[i]);
	}
	return result;
}

/*  EMAIL ADDRESS VALIDATION
    Changes:  Sandeep V. Tamhankar (stamhankar@hotmail.com)
    The JavaScript Source!! http://javascript.internet.com
    1.1.2: Fixed a bug where trailing . in e-mail address was passing
            (the bug is actually in the weak regexp engine of the browser; I
            simplified the regexps to make it work).
    1.1.1: Removed restriction that countries must be preceded by a domain,
            so abc@host.uk is now legal.  However, there's still the 
            restriction that an address must end in a two or three letter
            word.
      1.1: Rewrote most of the function to conform more closely to RFC 822.
      1.0: Original*/
function emailCheck (emailStr) {
    /* The following pattern is used to check if the entered e-mail address
       fits the user@domain format.  It also is used to separate the username
       from the domain. */
    var emailPat=/^(.+)@(.+)$/
    /* The following string represents the pattern for matching all special
       characters.  We don't want to allow special characters in the address. 
       These characters include ( ) < > @ , ; : \ " . [ ]    */
    var specialChars="\\(\\)<>@,;:\\\\\\\"\\.\\[\\]"
    /* The following string represents the range of characters allowed in a 
       username or domainname.  It really states which chars aren't allowed. */
    var validChars="\[^\\s" + specialChars + "\]"
    /* The following pattern applies if the "user" is a quoted string (in
       which case, there are no rules about which characters are allowed
       and which aren't; anything goes).  E.g. "jiminy cricket"@disney.com
       is a legal e-mail address. */
    var quotedUser="(\"[^\"]*\")"
    /* The following pattern applies for domains that are IP addresses,
       rather than symbolic names.  E.g. joe@[123.124.233.4] is a legal
       e-mail address. NOTE: The square brackets are required. */
    var ipDomainPat=/^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/
    /* The following string represents an atom (basically a series of
       non-special characters.) */
    var atom=validChars + '+'
    /* The following string represents one word in the typical username.
       For example, in john.doe@somewhere.com, john and doe are words.
       Basically, a word is either an atom or quoted string. */
    var word="(" + atom + "|" + quotedUser + ")"
    // The following pattern describes the structure of the user
    var userPat=new RegExp("^" + word + "(\\." + word + ")*$")
    /* The following pattern describes the structure of a normal symbolic
       domain, as opposed to ipDomainPat, shown above. */
    var domainPat=new RegExp("^" + atom + "(\\." + atom +")*$")

    /* Finally, let's start trying to figure out if the supplied address is
       valid. */

    /* Begin with the coarse pattern to simply break up user@domain into
       different pieces that are easy to analyze. */
    var matchArray=emailStr.match(emailPat)
    if (matchArray==null) {
      /* Too many/few @'s or something; basically, this address doesn't
         even fit the general mould of a valid e-mail address. */
        alert("Email address seems incorrect (check @ and .'s)")
        return false
    }
    var user=matchArray[1]
    var domain=matchArray[2]

    // See if "user" is valid 
    if (user.match(userPat)==null) {
        // user is not valid
        alert("The username doesn't seem to be valid.")
        return false
    }

    /* if the e-mail address is at an IP address (as opposed to a symbolic
       host name) make sure the IP address is valid. */
    var IPArray=domain.match(ipDomainPat)
    if (IPArray!=null) {
        // this is an IP address
          for (var i=1;i<=4;i++) {
            if (IPArray[i]>255) {
                alert("Destination IP address is invalid!")
            return false
            }
        }
        return true
    }

    // Domain is symbolic name
    var domainArray=domain.match(domainPat)
    if (domainArray==null) {
        alert("The domain name doesn't seem to be valid.")
        return false
    }

    /* domain name seems valid, but now make sure that it ends in a
       three-letter word (like com, edu, gov) or a two-letter word,
       representing country (uk, nl), and that there's a hostname preceding 
       the domain or country. */

    /* Now we need to break up the domain to get a count of how many atoms
       it consists of. */
    var atomPat=new RegExp(atom,"g")
    var domArr=domain.match(atomPat)
    var len=domArr.length
    if (domArr[domArr.length-1].length<2 || 
        domArr[domArr.length-1].length>3) {
       // the address must end in a two letter or three letter word.
       alert("The address must end in a three-letter domain, or two letter country.")
       return false
    }

    // Make sure there's a host name preceding the domain.
    if (len<2) {
       var errStr="This address is missing a hostname!"
       alert(errStr)
       return false
    }

    // If we've gotten this far, everything's valid!
    return true;
}

/* Get form element by ID */
function getFormElement(id,doc) {
    var i,elem;
    if (!doc) {
        doc = document;
    }
    if (!(elem = doc[id]) && doc.all) {
        elem = doc.all[id];
    }
    for(i = 0; !elem && i < doc.forms.length;i++) {
        elem = doc.forms[i][id];
    }
    for(i = 0; (!elem && doc.layers && i < doc.layers.length); i++) {
        elem = getFormElement(id, doc.layers[i].document);
    }
    if(!elem && document.getElementById) {
        elem = document.getElementById(id);
    }
    return elem;
}

/*
parentId: for LOC by stage page, it is stage name
iconId: displaying icon
childNodes: numbers of child nodes of current element
openCloseControl: + is for expanded and - is for collapsed
*/
function collapseExpand(parentId, iconId, childNodes, openCloseControl) {
    var isOpenned = getFormElement(openCloseControl).value;
    var rowHide = "none";
    var rowDisplay = document.all ? "block" : "table-row";
    var i;
    //alert("isOpenned:" + isOpenned + ";getFormElement(iconId).src:" + getFormElement(iconId).src);
    if (childNodes > 0) {
        if (isOpenned == "-") {
            for (i=0;i<childNodes;i++) {
                getFormElement(parentId + "_" + (i+1)).style.display = rowHide;
            }
            getFormElement(iconId).src = "image/IconPlus.gif";
            getFormElement(openCloseControl).value = "+";
        }
        else {
            for (i=0;i<childNodes;i++) {
                getFormElement(parentId + "_" + (i+1)).style.display = rowDisplay;
            }
            getFormElement(iconId).src = "image/IconMinus.gif";
            getFormElement(openCloseControl).value = "-";
        }
    }
}

/* 23-Jul-2007: Fixed this function to compatible with IE 7.
 */
function appendOption(ctrlSelect, strValue, strText, strLabel, bSelected) {
    var optNew = document.createElement("option");
    var txtNode = document.createTextNode(strText);
    optNew.setAttribute("value",strValue);
    // strLabel is avoided for compatible with IE 7.
    optNew.appendChild(txtNode);
    ctrlSelect.appendChild(optNew);
    if (bSelected == true) {
        optNew.selected = true;
    }
}

// To use this function, the HTML form should include languages.js in order to use languages array.
function appendLanguages(combo, isAll, excludeValue) {
    if (isAll) {
        // fill all languages
        for (var i=0;i<all_lang.length;i++) {
            if (excludeValue != all_lang[i][0]) { // Avoid this option
                appendOption(combo, all_lang[i][0], all_lang[i][1], null, false);
            }
        }
    }
    else {
        // fill common languages
        for (var i=0;i<common_lang.length;i++) {
            if (excludeValue != common_lang[i][0]) { // Avoid this option
                appendOption(combo, common_lang[i][0], common_lang[i][1], null, false);
            }
        }
    }
}

// Reset (remove options from startIndex) then fill languages into combo box
function resetAndFillLanguages(combo, isAll, excludeValue, startIndex) {
    if (combo) {
        while (combo.options.length > startIndex) {  // Clear combo box
            combo.options[startIndex]=null;
        }
        appendLanguages(combo, isAll, excludeValue);
    }
}

function fillMoreRiskSources(combo, isAll, excludeValue, startIndex) {
    if (combo) {
        while (combo.options.length > startIndex) {  // Clear combo box
            combo.options[startIndex]=null;
        }
        appendRiskSources(combo, isAll, excludeValue);
    }
}

function appendRiskSources(combo, isAll, excludeValue) {
    if (isAll) {
        // fill other Risk
        for (var i=0;i<other_riskSource.length;i++) {
            if (excludeValue != other_riskSource[i][0]) { // Avoid this option
                appendOption(combo, other_riskSource[i][0], other_riskSource[i][1], null, false);
            }
        }
    }
    else {
        // fill common languages
    }
}

// HaiMM add this function: Get month by duration
function monthsBetween(thisDate, thatDate) {
	if (thisDate> thatDate) {
		return monthsBetween(thatDate, thisDate);
	}
	var number = 0;
	if (thatDate.getFullYear()> thisDate.getFullYear()) {
		number = number + (thatDate.getFullYear() - thisDate.getFullYear() - 1) * 12;
	} else {
		return thatDate.getMonth() - thisDate.getMonth();
	}
	if (thatDate.getMonth()> thisDate.getMonth()) {
		number = number + 12 + thatDate.getMonth() - thisDate.getMonth();
	} else {
		number = number + (12 - thisDate.getMonth()) + thatDate.getMonth();
	}
	return number;
}
