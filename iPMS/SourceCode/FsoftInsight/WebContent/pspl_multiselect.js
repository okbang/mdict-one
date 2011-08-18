/*******************************************************************/
/*                                                                 */
/* Copyright (c) 2006-2008 Possible Solutions                      */
/* Version 1.0.2																									 */
/*                                                                 */
/** **************************************************************** */
var valueTemp = '';
function aH(aK, av, az, ax, aM, aJ, aO, as, ag, aq) {
	aT = this;
	this.aQ = aK;
	this.f = av;
	this.T = az;
	this.O = ax;
	this.aa = aM;
	this.af = aJ;
	this.ai = aO;
	this.ae = as;
	this.al = ag;
	this.ah = aq;
	this.H = false;
	this.t = document.getElementById(this.f);
	this.U = false;
	this.aw = function() {
		this.t.style.display = "none";
		J = document.getElementById(this.f + '_box');
		aB = '<span id="'
				+ this.f
				+ '_optionDiv" class="'
				+ this.ai
				+ '" ></span><span class="innerContainerPSPL"><input readonly="readonly" id="'
				+ this.f + '_text" name="' + this.f
				+ '_text" type="text" value="" class="' + this.af + '" />';
		aA = '<img id="' + this.f + '_arrow" class="' + this.aa + '" src="'
				+ this.O + '" title="Click to Select" />';
		aN = '</span>';
		J.innerHTML += aB + aA + aN;
		this.t = document.getElementById(this.f);
	};
	this.ac = function() {
		A = this.t.options;
		B = '';
		for (g = 0; g < A.length; g++) {
			if (A[g].selected == true) {
				B += A[g].innerHTML + ',';
			}
		}
		this.I.value = B;
		valueTemp = B ; 
	};
	this.an = function() {
		aU = document.body.offsetTop;
		aL = document.body.offsetLeft;
		if (this.H == false) {
			this.H = true;
			this.l.style.top = aD(this.I) + this.I.offsetHeight + 'px';
			this.l.style.left = aE(this.J) + aL + 'px';
			this.l.style.display = 'block';
			this.l.style.height = '150px';
			this.l.style.width = '220px';
			this.ac();
		} else {
			this.Q();
		}
	};
	this.Q = function() {
		this.H = false;
		this.l.style.top = -2000 + 'px';
		this.l.style.left = -2000 + 'px';
		this.l.style.display = 'none';
	};
	this.aI = function() {
		A = this.t.options;
		this.l = document.getElementById(this.f + '_optionDiv');
		this.l.style.width = '220px';
		B = '<table id="' + this.f + '_table" cellpadding="0" cellspacing="0" border="0" width="100%" >';
		for (g = 0; g < A.length; g++) {
			if (A[g].selected == true) {
				B += '<tr class="row"><td id="' + this.f + '_td_' + g
						+ '" nowrap class="' + this.ah + '">' + A[g].innerHTML
						+ '</td></tr>';
			} else {
				B += '<tr class="row"><td id="' + this.f + '_td_' + g
						+ '" nowrap class="' + this.ae + '">' + A[g].innerHTML
						+ '</td></tr>';
			}
		}
		B += '</table>';
		this.l.innerHTML = B;
		this.l.style.display = 'block';
		this.l.style.height = '150px';
		this.P = document.getElementById(this.f + '_table');
		if (this.P.scrollWidth > parseInt(this.l.style.width)) {
			this.l.style.width = '220px';
			
		}
		this.l.style.height = this.P.scrollHeight + 'px';
		aR = this;
	};
	this.at = function(g) {
		if (this.t.options[g].selected == true) {
			this.t.options[g].selected = false;
		} else {
			this.t.options[g].selected = true;
		}
		this.ac();
	};
	this.ad = function(index, ao) {
		if (this.t.options[index].selected == true) {
			this.v[index].className = aq;
		} else {
			if (ao == "selected") {
				this.v[index].className = ag;
			} else if (ao == "hover") {
				this.v[index].className = ag;
			} else {
				this.v[index].className = as;
			}
		}
		this.v[index].refresh;
	}
};
function ay(d) {
	c[d].C.onclick = function() {
		c[d].an();
	};
	c[d].I.onclick = function() {
		c[d].an();
	};
	c[d].C.onmouseover = function() {
		c[d].C.src = c[d].T;
	};
	c[d].C.onmouseout = function() {
		c[d].C.src = c[d].O;
	};
	c[d].J.onclick = function(aS) {
		c[d].U = true;
	};
	for (r = 0; r < c[d].t.options.length; r++) {
		c[d].v[r] = document.getElementById(c[d].f + '_td_' + r);
		eval('c[' + d + '].v[' + r + '].onclick = function(){ c[' + d
				+ '].at( ' + r + ' ); c[' + d + '].ad( ' + r
				+ ',\'selected\' ); }');
		eval('c[' + d + '].v[ ' + r + '].onmouseover = function(){  c[' + d
				+ '].ad(' + r + ', \'hover\' ); }');
		eval('c[' + d + '].v[ ' + r + '].onmouseout = function(){  c[' + d
				+ '].ad( ' + r + ', \'normal\' ); }');
	}
};
function aD(ap, L) {
	var L = true;
	var F = ap;
	var ab = ap.offsetTop;
	while (F.offsetParent != null) {
		F = F.offsetParent;
		ab += F.offsetTop;
		G = parseInt(K(F, "border-top-width"));
		if (G > 0) {
			ab += G;
		}
		if (L == true && K(F, "position") == "relative") {
			break;
		}
	}
	return ab;
};
function aE(am) {
	var L = true;
	var D = am;
	var V = am.offsetLeft;
	while (D.offsetParent != null) {
		D = D.offsetParent;
		V += D.offsetLeft;
		G = parseInt(K(D, "border-left-width"));
		if (G > 0) {
			V += G;
		}
		if (L == true && K(D, "position") == "relative") {
			break;
		}
	}
	return V;
};
function K(R, aj) {
	if (R.currentStyle) {
		var M = aj.match(/\w[^-]*/g);
		var s = M[0];
		for ( var i = 1; i < M.length; ++i) {
			s += M[i].replace(/\w/, M[i].charAt(0).toUpperCase());
		}
		return R.currentStyle[s]
	} else if (document.defaultView.getComputedStyle) {
		return document.defaultView.getComputedStyle(R, null).getPropertyValue(
				aj);
	} else {
		return 0;
	}
};
function aP() {
};
c = Array();
ak = false;
function Init(au, T, O, aa, af, ai, ae, al, ah) {
	c[c.length] = new aH(c.length, au, T, O, aa, af, ai, ae, al, ah);
	c[c.length - 1].aw();
	c[c.length - 1].J = document.getElementById(c[c.length - 1].f + '_box');
	c[c.length - 1].I = document.getElementById(c[c.length - 1].f + '_text');
	c[c.length - 1].C = document.getElementById(c[c.length - 1].f + '_arrow');
	c[c.length - 1].l = document
			.getElementById(c[c.length - 1].f + '_optionDiv');
	c[c.length - 1].v = new Array();
	c[c.length - 1].ac();
};
function aC() {
	if (ak == false) {
		ak = true;
		for (y = 0; y < c.length; y++) {
			c[y].aI();
			ay(y);
		}
	}
};
function aF() {
	for (o = 0; o < c.length; o++) {
		if (c[o].U == false) {
			c[o].Q();
		}
		c[o].U = false;
	}
};
function aG() {
	for (o = 0; o < c.length; o++) {
		if (c[o].H == true) {
			c[o].Q();
			c[o].an();
		}
	}
};
function setValueElement(element,index){
	document.getElementsByName(element)[index].value = valueTemp; 
}
//document.onmouseover = aC;
//document.body.onload = aC;

document.onclick = aF;
window.onresize = aG;