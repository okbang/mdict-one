	//common parameters for all menus
	
	var LowBgColor='#C3D4E4';		//	Background color when mouse is not over
	var LowSubBgColor=LowBgColor;			// Background color when mouse is not over on subs
	var HighBgColor='#0066FF';			// Background color when mouse is over
	var HighSubBgColor=HighBgColor;			// Background color when mouse is over on subs
	var FontLowColor='black';			// Font color when mouse is not over
	var FontSubLowColor=FontLowColor;			// Font color subs when mouse is not over
	var FontHighColor='white';			// Font color when mouse is over
	var FontSubHighColor=FontHighColor;			// Font color subs when mouse is over
	var BorderColor='#919791';			// Border color
	var BorderSubColor=BorderColor;			// Border color for subs
	var SelectedColor="#96b4cf";
	var BorderWidth=1;				// Border width
	var BorderBtwnElmnts=1;				// Border between elements 1 or 0
	var FontFamily="verdana, geneva, arial, helvetica, sans-serif";	// Font family menu items
	var FontSize="9";					// Font size menu items
	var FontBold=1;					// Bold menu items 1 or 0
	var FontItalic=0;				// Italic menu items 1 or 0
	var MenuTextCentered='left';			// Item text position 'left', 'center' or 'right'
	var MenuCentered='left';			// Menu horizontal position 'left', 'center' or 'right'
	var MenuVerticalCentered='top';			// Menu vertical position 'top', 'middle','bottom' or static
	var ChildOverlap=0;				// horizontal overlap child/ parent
	var ChildVerticalOverlap=0;			// vertical overlap child/ parent
	var StartTop=0;					// Menu offset x coordinate
	var StartLeft=1;				// Menu offset y coordinate
	var VerCorrect=0;				// Multiple frames y correction
	var HorCorrect=0;				// Multiple frames x correction
	var LeftPaddng=10;				// Left padding
	var TopPaddng=1;				// Top padding
	var FirstLineHorizontal=0;			// SET TO 1 FOR HORIZONTAL MENU, 0 FOR VERTICAL
	var MenuFramesVertical=1;			// Frames in cols or rows 1 or 0
	var DissapearDelay=1000;			// delay before menu folds in
	var TakeOverBgColor=0;			// Menu frame takes over background color subitem frame
	var FirstLineFrame='menu';			// Frame where first level appears
	var SecLineFrame='main';			// Frame where sub levels appear
	var DocTargetFrame='main';			// Frame where target documents appear
	var TargetLoc='';				// span id for relative positioning
	var HideTop=0;				// Hide first level when loading new document 1 or 0
	var MenuWrap=1;				// enables/ disables menu wrap 1 or 0
	var RightToLeft=0;				// enables/ disables right to left unfold 1 or 0
	var UnfoldsOnClick=0;			// Level 1 unfolds onclick/ onmouseover
	var WebMasterCheck=0;			// menu tree checking on or off 1 or 0
	var ShowArrow=1;				// Uses arrow gifs when 1
	var KeepHilite=1;				// Keep selected path highligthed
	var Arrws=['image/tri.gif',5,10,'image/tridown.gif',10,5,'image/trileft.gif',5,10];	// Arrow source, width and height
	var selectedSub="";
	var selectedMain="";
function BeforeStart(){return}
function AfterBuild(){return}
function BeforeFirstOpen(){return}
function AfterCloseAll(){return}
var URL="Fms1Servlet?reqType=";
var mnuWdth =138;