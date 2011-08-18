/***********************************************
* AnyLink Drop Down Menu- © Dynamic Drive (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit http://www.dynamicdrive.com/ for full source code
***********************************************/

//Contents for menu 1
var menu1=new Array()
menu1[0]='<a href="javascript:void(0)" onclick="return menuClick(\'mnuAssignUser\',document.forms[0])">Assign User</a><IMG style="BACKGROUND-COLOR:#000000" height=1 width=150>'
menu1[1]='<a href="javascript:void(0)" onclick="return menuClick(\'mnuWorkProductSize\',document.forms[0])">Work Product</a><IMG style="BACKGROUND-COLOR:#000000" height=1 width=150>'
menu1[2]='<a href="javascript:void(0)" onclick="return menuClick(\'mnuSetupModuleArea\',document.forms[0])">Module</a><IMG style="BACKGROUND-COLOR:#000000" height=1 width=150>'
menu1[3]='<a href="javascript:void(0)" onclick="return menuClick(\'mnuSetupEnvironment\',document.forms[0])">Setup Environment</a>'

//Contents for menu 2, and so on
var menu2=new Array()
menu2[0]='<a href="javascript:void(0)" onclick="return menuClick(\'mnuManageView\',document.forms[0])">Manage Views</a><IMG style="BACKGROUND-COLOR:#000000" height=1 width=150>'
menu2[1]='<a href="javascript:void(0)" onclick="return menuClick(\'mnuNewDefect\',document.forms[0])">New Defect</a><IMG style="BACKGROUND-COLOR:#000000" height=1 width=150>'
menu2[2]='<a href="javascript:void(0)" onclick="return menuClick(\'mnuPlannedDefect\',document.forms[0])">Manage Planned Defect</a><IMG style="BACKGROUND-COLOR:#000000" height=1 width=150>'
menu2[3]='<a href="javascript:void(0)" onclick="return menuClick(\'mnuProjectSummary\',document.forms[0])">Project Summary</a><IMG style="BACKGROUND-COLOR:#000000" height=1 width=150>'
menu2[4]='<a href="javascript:void(0)" onclick="return menuClick(\'mnuImportDefect\',document.forms[0])">Import Defect</a>'

var menuwidth='230' //default menu width
//var menubgcolor='lightyellow'  //menu bgcolor
var menubgcolor='#E6E6E6'
var disappeardelay=150  //menu disappear speed onMouseout (in miliseconds)
var hidemenu_onclick="yes" //hide menu when user clicks within menu?

/////No further editting needed

var ie4=document.all
var ns6=document.getElementById&&!document.all

if (ie4||ns6)
    document.write('<div id="dropmenudiv" style="visibility:hidden;width:'+menuwidth+';background-color:'+menubgcolor+'" onMouseover="clearhidemenu()" onMouseout="dynamichide(event)"></div>')

function getposOffset(what, offsettype){
    var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
    var parentEl=what.offsetParent;
    while (parentEl!=null){
        totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
        parentEl=parentEl.offsetParent;
    }
    return totaloffset;
}


function showhide(obj, e, visible, hidden, menuwidth){
    if (ie4||ns6)
        dropmenuobj.style.left=dropmenuobj.style.top=-500
    if (menuwidth!=""){
        dropmenuobj.widthobj=dropmenuobj.style
        dropmenuobj.widthobj.width=menuwidth
    }
    if (e.type=="click" && obj.visibility==hidden || e.type=="mouseover")
        obj.visibility=visible
    else if (e.type=="click")
        obj.visibility=hidden
}

function iecompattest(){
    return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function clearbrowseredge(obj, whichedge){
    var edgeoffset=0
    if (whichedge=="rightedge"){
        var windowedge=ie4 && !window.opera? iecompattest().scrollLeft+iecompattest().clientWidth-15 : window.pageXOffset+window.innerWidth-15
        dropmenuobj.contentmeasure=dropmenuobj.offsetWidth
        if (windowedge-dropmenuobj.x < dropmenuobj.contentmeasure)
            edgeoffset=dropmenuobj.contentmeasure-obj.offsetWidth
    }
    else{
        var windowedge=ie4 && !window.opera? iecompattest().scrollTop+iecompattest().clientHeight-15 : window.pageYOffset+window.innerHeight-18
        dropmenuobj.contentmeasure=dropmenuobj.offsetHeight
        if (windowedge-dropmenuobj.y < dropmenuobj.contentmeasure)
            edgeoffset=dropmenuobj.contentmeasure+obj.offsetHeight
    }
    return edgeoffset
}

function populatemenu(what){
    if (ie4||ns6)
        dropmenuobj.innerHTML=what.join("")
}


function dropdownmenu(obj, e, menucontents, menuwidth){
    if (window.event) event.cancelBubble=true
    else if (e.stopPropagation) e.stopPropagation()
        clearhidemenu()
    dropmenuobj=document.getElementById? document.getElementById("dropmenudiv") : dropmenudiv
    populatemenu(menucontents)

    if (ie4||ns6){
        showhide(dropmenuobj.style, e, "visible", "hidden", menuwidth)
        dropmenuobj.x=getposOffset(obj, "left")
        dropmenuobj.y=getposOffset(obj, "top")
        dropmenuobj.style.left=dropmenuobj.x-clearbrowseredge(obj, "rightedge")+"px"
        dropmenuobj.style.top=dropmenuobj.y-clearbrowseredge(obj, "bottomedge")+obj.offsetHeight+"px"
    }

    return clickreturnvalue()
}

function clickreturnvalue(){
    if (ie4||ns6) return false
    else return true
}

function contains_ns6(a, b) {
    while (b.parentNode)
    if ((b = b.parentNode) == a)
        return true;
    return false;
}

function dynamichide(e){
    if (ie4&&!dropmenuobj.contains(e.toElement))
        delayhidemenu()
    else if (ns6&&e.currentTarget!= e.relatedTarget&& !contains_ns6(e.currentTarget, e.relatedTarget))
        delayhidemenu()
}

function hidemenu(e){
    if (typeof dropmenuobj!="undefined"){
        if (ie4||ns6)
            dropmenuobj.style.visibility="hidden"
    }
}

function delayhidemenu(){
    if (ie4||ns6)
        delayhide=setTimeout("hidemenu()",disappeardelay)
}

function clearhidemenu(){
    if (typeof delayhide!="undefined")
        clearTimeout(delayhide)
}

if (hidemenu_onclick=="yes")
    document.onclick=hidemenu

function menuClick(name, form) {
    form.onsubmit="";
    form.target="";
    //Validate date values if this form contains them
    clearInvalidControls(form);
    if (name==null){
        return clickreturnvalue();
    }
    else if (name == "mnuAssignUser")
    {
        form.hidAction.value = "PE";
        form.hidActionDetail.value = "AssignList";
        AssignUserHandler(form);
    }
    else if (name == "mnuSetupModuleArea")
    {
        form.hidAction.value = "PE";
        form.hidActionDetail.value = "ModuleList";
        SetupModuleAreaHandler(form);
        //allow show the screen but but disable sub-functions
    }
    else if (name == "mnuProjectSummary")
    {
        form.hidActionDetail.value = "ReportWeekly";
        form.hidAction.value = "DM";
        form.submit();
    }
    else if (name == "mnuImportDefect")
    {
        form.hidActionDetail.value = "ImportDefect";
        form.hidAction.value = "DM";
        form.submit();
    }
    else if (name == "mnuSetupEnvironment")
    {
        form.hidAction.value = "SE";
        form.hidActionDetail.value = "SetupEnvironment"
        SetupEnvironment(form);
    }
    else if (name == "mnuWorkProductSize")
    {
        form.hidAction.value = "PE";
        form.hidActionDetail.value = "WorkProductList";
        WorkProductSizeHandler(form);
        //allow show the screen but but disable sub-functions
    }
    else if (name == "mnuPlannedDefect")
    {
        form.hidAction.value = "PE";
        form.hidActionDetail.value = "ManagePlannedDefect";
        PlannedDefectHandler(form);
        //allow show the screen but but disable sub-functions
    }

    else if (name == "mnuManageView")
    {
        form.hidAction.value = "DM";
        form.hidActionDetail.value = "QueryListing";
        form.submit();
    }
    else if (name == "mnuNewDefect")
    {
        form.hidAction.value = "DM";
        form.hidActionDetail.value = "AddDefect";
        form.submit();
    }
    else if (name == "mnuLogout")
    {
        form.hidAction.value = "DMSHomePage";
        form.submit();
    }
    else if (name == "mnuHelp")
    {
        var sFeature = "status=no, menu=no,scrollbars=yes";
        var requirementchart_wd = window.open("Guideline_Defect Management System.doc","Help",sFeature);
    }

    return clickreturnvalue()
}

//Validate date values if this form contains them
function clearInvalidControls(form) {
    if ((form.txtFromDate != null) && (form.txtFromDate.value.length > 0)) {
        if (!isDate(form.txtFromDate)) {
            form.txtFromDate.value = "";
        }
    }
    if ((form.txtToDate != null) && (form.txtToDate.value.length > 0)) {
        if (!isDate(form.txtToDate)) {
            form.txtToDate.value = "";
        }
    }
}

/* Check roles, allow post form or not*/

function AssignUserHandler(form) {
	alert('This function is deprecated and moved to Fsoft Insight. Please update in Fsoft Insight/WO/Team');
	/*
	alert('This function is deprecated and moved to FSoft Ingisht. Please update in FSoft Insight/WO/Team or Dashboard/Project/UpdateAssignment.minh');
    var Role = form.CheckRole.value;
    if (Role !=null) {
        if (Role.substring(2,3) != '1') {
            alert('Unauthorized access.You are not Project Leader');
        }
        else {
            //form.submit();
        }
    }
    */
}

function SetupEnvironment(form) {
    var Role = form.Role.value;
    if (Role !=null) {
        if (Role.substring(4,5) != '1') {
            alert('Unauthorized access.You are not SQA');
        }
        else {
            form.submit();
        }
    }
}

function SetupModuleAreaHandler(form) {
	alert('This function is deprecated and moved to Fsoft Insight');
	/*
    var Role = form.CheckRole.value;
    if (Role !=null) {
        if (Role.substring(2,3) != '1') {
            alert('Sorry, unauthorized access. You are not Project Leader');
        }
        else {
        	alert('This function is removed to FSoftInsight, please relogin FSoftInsight ');
        	
            //form.submit();
        }
    }
    */
}

function WorkProductSizeHandler(form) {
	/*
    var Role = form.CheckRole.value;
    if (Role !=null) {
        if (Role.substring(2,3) != '1') {
            alert('Sorry, unauthorized access. You are not Project Leader');
        }
        else {
            //form.submit();
            alert('This function is removed to FSoftInsight, please relogin FSoftInsight ');
            
        }
    }
    */
    alert('This function is deprecated and moved to Fsoft Insight');
}

function PlannedDefectHandler(form) {
	alert('This function is deprecated and moved to Fsoft Insight.');
	/*
    var Role = form.CheckRole.value;
    if (Role !=null) {
        if (Role.substring(2,3) != '1') {
            alert('Sorry, unauthorized access. You are not Project Leader');
        }
        else {
            //form.submit();
            alert('This function is removed to FSoftInsight, please relogin FSoftInsight ');
        }
    }
    */
}