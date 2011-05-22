if (top.location != location) {
    top.location.href = document.location.href;
}
function $(d){
	return document.getElementById(d);
}

// set or get the current display style of the div
function dsp(d,v){
	if(v===undefined){
		return d.style.display;
	}else{
		d.style.display=v;
	}
}

// Collapse Initializer
function cl(d){
	dsp(d,'none');		
	var searchables = d.getElementsByTagName('a');
	for (var i=0; i< searchables.length; i++){
		searchables[i].style.display="";
	}  
	d.expanded = 0;
}

//Expand Initializer
function ex(d){
	d.expanded = 1;
	var searchables = d.getElementsByTagName('a');
	for (var i=0; i< searchables.length; i++){
		searchables[i].style.display="";
	}  
	dsp(d,'block');
}
function collapseAll(a){
	for(var i=0;i<a.length;i++){
		cl(a[i]);
	}
}

function expandAll(a){
	for(var i=0;i<a.length;i++){
		ex(a[i]);
	}
}

function addStyle(n,v){
	n.className = n.className + ' ' + v;
}
// Removes Classname from the given div.
function remStyle(n,v){
	var s=n.className.split(/\s+/);
	for(var p=0;p<s.length;p++){
		if(s[p]==v){
			s.splice(p,1);
			n.className=s.join(' ');
			break;
		}
	}
}
//Accordian Initializer
function Accordian(d,s,css){
	// get all the elements that have id as content
	var l=$(d).getElementsByTagName('div');
	var c=[];
	for(var i=0;i<l.length;i++){
		var h=l[i].id;
		if(h.substr(h.indexOf('-')+1,h.length)=='content'){c.push($(h));}
	}
	var sel=null;
	//then search through headers
	for(i=0;i<l.length;i++){
		h=l[i].id;
		if(h.substr(h.indexOf('-')+1,h.length)=='header'){
			d=$(h.substr(0,h.indexOf('-'))+'-content');
			h=$(h);
			if (d){
				d.style.display='none';
				d.style.overflow='hidden';
				
				if(h.className.match('selected')){ sel=h;}
				// set the onclick function for each header.
				h.onclick = function(){
				   var e = $(this.id.substr(0,this.id.indexOf('-'))+'-content');
				   e.tmp = e.expanded;
				   collapseAll(this.c);
				   (e.tmp)? cl(e) : ex(e);
				};
			}
                        h.className = css;
                        h.c=c;
                        h.innerHTML = '<table cellspacing="0"><tr><td width="100%"><div style="background-image: url('+ h.title +'); background-repeat: no-repeat; background-position: 5px 50%; padding-left: 28px;" class="x-dock-panel x-layout-panel-hd-text x-dock-panel-title-text">'+ h.innerHTML +'</div></td><td><div class="x-layout-tools-button x-layout-tools-button-inner x-layout-collapse-south" onMouseOver="addStyle(this, \'x-layout-tools-button-over\');" onMouseOut="remStyle(this, \'x-layout-tools-button-over\');">&nbsp;</div></td></tr></table>';
                        h.title = '';
		}
	}
	if(sel){sel.onclick();}
	return c;
}

// searching within panel
function searchPanels(acc, searchBoxEl) {
	var aEl;
	var found;
	var searchables;
	var val = searchBoxEl.value;
	// ignore if length is 1 or 2
	if(val.length < 3) {
	   collapseAll(acc);	
	} else {
	   var re = new RegExp('.*' + val + '.*', 'i');
	   for (var j=0; j < acc.length; j++){
	      found = false;
	      searchables = acc[j].getElementsByTagName('a');

	      for (var i=0; i < searchables.length; i++){
		     aEl = searchables[i];
		     if(aEl.innerHTML.match(re)) {
		        found = true;
		        aEl.style.display="";
		     } else {
		        aEl.style.display="none";
		     }	
		  }
		  (found)? dsp(acc[j],'block') : dsp(acc[j],'none');
	   }
	}	    
}	      

function showTab(tab, url, update){
   if (typeof curTab == "undefined"){ curTab = "tab1"; }
   if (curTab != tab) {
      $(curTab).className = ""; 
      $(curTab +"-iframe").style.display = "none"; 
	  if($(curTab +"-nav"))$(curTab +"-nav").style.display = "none"; 
   }

   if($(tab +"-nav"))$(tab +"-nav").style.display = "block"; 
   $(tab +"-iframe").style.display = "block"; 
   //Update or initiate tab URL
   if (update || $(tab +"-iframe").src.indexOf('layout/blank.html') >= 0) { $(tab +"-iframe").src = url; }
   $(tab).className="on"; 
   curTab = tab;	  
   return false;
}

function resize()
{
	var x = 0;
	var y = 0;
	if (self.innerHeight) // all except Explorer
	{
		x = self.innerWidth;
		y = self.innerHeight;
	}
	else if (document.documentElement && document.documentElement.clientHeight) // Explorer 6 Strict Mode
	{
		x = document.documentElement.clientWidth;
		y = document.documentElement.clientHeight;
	}
	else if (document.body) // other Explorers
	{
		x = document.body.clientWidth;
		y = document.body.clientHeight;
	}
	if (x && y)
	{
		if ($('leftPanel').style.display != "none" && $('leftPanel').offsetWidth != 210){ $('leftPanel').style.width = "210px"; }
		if ($('collapsedNav').style.display != "none" && $('collapsedNav').offsetWidth != 22){ $('collapsedNav').style.width = "22px"; }
	    $('rightPanel').style.width = ($('leftPanel').style.display == 'none')? (x - $('collapsedNav').offsetWidth - 4) : (x - $('leftPanel').offsetWidth - 5);
	    
	    <!-- Slogan, sponsor, login panel -->
	    $('headerTopPanel').style.width = (x - $('headerlogo').offsetWidth - 1);

	    <!-- Hot news panel -->
	    $('headerBottomPanel').style.width = (x - $('headerlogo').offsetWidth - 1);
	    
		$('iframeWrapper').style.height = y - $('topBar').offsetHeight - $('tabContainer').offsetHeight - 2;
		$('mynav').style.height = y - $('mylogo').offsetHeight - $('mytoolbar').offsetHeight;
	}
}
function toggleNav(){
   if ($('leftPanel').style.display != "none" ){
      $('leftPanel').style.display = $('ext-gen7-split').style.display = "none"; 
	  $('collapsedNav').style.display = "block";
	  $('rightPanel').style.left ='26px';
   } else {
      $('leftPanel').style.display = $('ext-gen7-split').style.display = "block"; 
	  $('collapsedNav').style.display = "none";
	  $('rightPanel').style.left ='215px';
   }
   resize();
   return false;
}
var acc;
function init(){
   acc = new Accordian("mynav",5, "x-dock-panel x-unselectable x-layout-panel-hd x-dock-panel-title x-window-header-text x-dock-panel-title-expanded");	
   if (navigator.appVersion.match("MSIE")) { $("tabWrapper").style.overflowX = "hidden"; }
   resize();
   var mask = $('loading-mask');
   var msg = $('loading-msg');
   if(mask && msg) { mask.style.display=msg.style.display="none"; }

   // handle links
   $('mynav').onclick = function(e) { 
	    var target;
		if (!e) { e = window.event; }
		if (e.target) { target = e.target; }
		else if (e.srcElement) { target = e.srcElement; }
		if (target.nodeType == 3) { target = target.parentNode; }// defeat Safari bug
		
		if (target.className != "linkitem" || target.onclick) { return false; }	// element already has onclick defined
		if (target.href) showTab('tab1', target.href, 1);
		return false;
   };
   
} 
window.onload = init;
window.onresize = resize;
