
// BigInt, a suite of routines for performing multiple-precision arithmetic in
// JavaScript. Copyright 1998-2003 David Shapiro. You may use, re-use, abuse,
// copy, and modify this code to your liking, but please keep this header.
// Thanks!
//
// Dave Shapiro
// dave@ohdave.com

// Tweaked by Ian Bunning
// Alterations:
// Fix bug in function biFromHex(s) to allow
// parsing of strings of length != 0 (mod 4)

// Max number = 10^16 - 2 = 9999999999999998;
//               2^53     = 9007199254740992;

var biRadixBase = 2;
var biRadixBits = 16;
var bitsPerDigit = biRadixBits;
var biRadix = 1 << 16; // = 2^16 = 65536
var biHalfRadix = biRadix >>> 1;
var biRadixSquared = biRadix * biRadix;
var maxDigitVal = biRadix - 1;
var maxInteger = 9999999999999998; 
var maxDigits = 20;
var dpl10 = 15;
var bigZero = new BigInt("");
var bigOne = new BigInt("");
bigOne.digits[0] = 1;
// The maximum number of digits in base 10 you can convert to an
// integer without JavaScript throwing up on you.

// lr10 = 10 ^ dpl10
var lr10 = biFromNumber(1000000000000000);

function BigInt(s){
	if (s == "") {
		// This is hard-wired for speed. If maxDigits changes, this will
		// have to be changed as well.
		this.digits = new Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	                        	0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0,0,0,0);
		this.isNeg = false;
		return;
	}
	this.isNeg = s.charAt(0) == '-';
	var i = Number(this.isNeg);
	var ls = s.length;
	// Skip leading zeros.
	while (i < ls && s.charAt(i) == '0') ++i;
	if (i == ls) return;
	var numDigits = ls - i;
	var fgl = numDigits % dpl10;
	if (fgl == 0) fgl = dpl10;
	var result = biFromNumber(Number(s.substr(i, fgl)));
	i += fgl;
	while (i < ls) {
		result = biAdd(biMultiply(result, lr10),
		               biFromNumber(Number(s.substr(i, dpl10))));
		i += dpl10;
	}
	this.digits = result.digits;
}

function biFromArray(a){
	var result = new BigInt("");
	result.digits = a.slice(0);
	return result;
}

function biFromNumber(i){
	var result = new BigInt("");
	result.isNeg = i < 0;
	i = Math.abs(i);
	var j = 0;
	while (i > 0) {
		result.digits[j++] = i & maxDigitVal;
		i = Math.floor(i / biRadix);
	}
	return result;
}

function reverseStr(s){
	var result = "";
	for (var i = s.length - 1; i > -1; --i) {
		result += s.charAt(i);
	}
	return result;
}
function digitToHex(n){
	var hexToChar = new Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	                          'a', 'b', 'c', 'd', 'e', 'f');

	var mask = 0xf;
	var result = "";
	for (i = 0; i < 4; ++i) {
		result += hexToChar[n & mask];
		n >>>= 4;
	}
	return reverseStr(result);
}

function biToHex(x){
	var result = "";
	var n = biNumDigits(x);
	for (var i = biNumDigits(x); i > -1; --i) {
		result += digitToHex(x.digits[i]);
	}
	return result;
}

function charToHex(c){
	var ZERO = 48;
	var NINE = ZERO + 9;
	var littleA = 97;
	var littleZ = littleA + 25;
	var bigA = 65;
	var bigZ = 65 + 25;
	var result;

	if (c >= ZERO && c <= NINE) {
		result = c - ZERO;
	} else if (c >= bigA && c <= bigZ) {
		result = 10 + c - bigA;
	} else if (c >= littleA && c <= littleZ) {
		result = 10 + c - littleA;
	} else {
		result = 0;
	}
	return result;
}

function hexToDigit(s){
	var result = 0;
	var sl = Math.min(s.length, 4);
	for (var i = 0; i < sl; ++i) {
		result <<= 4;
		result |= charToHex(s.charCodeAt(i))
	}
	return result;
}

function biFromHex(s){
	var result = new BigInt("");
	var sl = s.length;
	for (var i = sl, j = 0; i > 0; i -= 4, ++j) {
		result.digits[j] = hexToDigit(s.substr(Math.max(i - 4, 0), Math.min(i, 4)));
	}
	return result;
}
function biAdd(x, y){
	var result;

	if (x.isNeg != y.isNeg) {
		var tmp = biFromArray(y.digits);
		tmp.isNeg = x.isNeg;
		result = biSubtract(x, tmp);
	} else {
		result = new BigInt("");
		var c = 0;
		var n;
		for (var i = 0; i < x.digits.length; ++i) {
			n = x.digits[i] + y.digits[i] + c;
			result.digits[i] = n % biRadix;
			c = Number(n >= biRadix);
		}
		result.isNeg = x.isNeg;
	}
	return result;
}

function biSubtract(x, y){
	var result;
	if (x.isNeg != y.isNeg) {
		var tmp = biFromArray(y.digits);
		tmp.isNeg = x.isNeg;
		result = biAdd(x, tmp);
	} else {
		result = new BigInt("");
		var n, c;
		c = 0;
		for (var i = 0; i < x.digits.length; ++i) {
			n = x.digits[i] - y.digits[i] + c;
			result.digits[i] = n % biRadix;
			// Stupid non-conforming modulus operation.
			if (result.digits[i] < 0) result.digits[i] += biRadix;
			c = 0 - Number(n < 0);
		}
		// Fix up the negative sign, if any.
		if (c == -1) {
			c = 0;
			for (var i = 0; i < x.digits.length; ++i) {
				n = 0 - result.digits[i] + c;
				result.digits[i] = n % biRadix;
				// Stupid non-conforming modulus operation.
				if (result.digits[i] < 0) result.digits[i] += biRadix;
				c = 0 - Number(n < 0);
			}
			// Result is opposite sign of arguments.
			result.isNeg = !x.isNeg;
		} else {
			// Result is same sign.
			result.isNeg = x.isNeg;
		}
	}
	return result;
}

function biNumDigits(x){
	var result = x.digits.length - 1;
	while (x.digits[result] == 0 && result > 0) --result;
	return result;
}

function biNumBits(x){
	var n = biNumDigits(x);
	var d = x.digits[n];
	var m = (n + 1) * bitsPerDigit;
	var result;
	for (result = m; result > m - bitsPerDigit; --result) {
		if ((d & 0x8000) != 0) break;
		d <<= 1;
	}
	return result;
}

function biMultiply(x, y){
	var result = new BigInt("");
	var c;
	var n = biNumDigits(x);
	var t = biNumDigits(y);
	var u, uv, k;

	for (var i = 0; i <= t; ++i) {
		c = 0;
		k = i;
		for (j = 0; j <= n; ++j, ++k) {
			uv = result.digits[k] + x.digits[j] * y.digits[i] + c;
			result.digits[k] = uv & maxDigitVal;
			c = uv >>> biRadixBits;
			//c = Math.floor(uv / biRadix);
		}
		result.digits[i + n + 1] = c;
	}
	// Someone give me a logical xor, please.
	result.isNeg = x.isNeg != y.isNeg;
	return result;
}

function biMultiplyDigit(x, y){
	var n, c, uv;

	result = new BigInt("");
	n = biNumDigits(x);
	c = 0;
	for (var j = 0; j <= n; ++j) {
		uv = result.digits[j] + x.digits[j] * y + c;
		result.digits[j] = uv & maxDigitVal;
		c = uv >>> biRadixBits;
	}
	result.digits[1 + n] = c;
	return result;
}

function arrayCopy(src, srcStart, dest, destStart, n){
	var m = Math.min(srcStart + n, src.length);
	for (var i = srcStart, j = destStart; i < m; ++i, ++j) {
		dest[j] = src[i];
	}
}

function biShiftLeft(x, n){
	var highBitMasks = new Array(0x0000, 0x8000, 0xC000, 0xE000, 0xF000, 0xF800,
                             0xFC00, 0xFE00, 0xFF00, 0xFF80, 0xFFC0, 0xFFE0,
                             0xFFF0, 0xFFF8, 0xFFFC, 0xFFFE, 0xFFFF);

	var digits = Math.floor(n / bitsPerDigit);
	var result = new BigInt("");
	arrayCopy(x.digits, 0, result.digits, digits, maxDigits - digits);
	var bits = n % bitsPerDigit;
	var rightBits = bitsPerDigit - bits;
	for (var i = maxDigits - 1, i1 = i - 1; i > 0; --i, --i1) {
		result.digits[i] = ((result.digits[i] << bits) & maxDigitVal) |
		                   ((result.digits[i1] & highBitMasks[bits]) >>>
		                    (rightBits));
	}
	result.digits[0] = ((result.digits[i] << bits) & maxDigitVal);
	result.isNeg = x.isNeg;
	return result;
}

function biShiftRight(x, n){
var lowBitMasks = new Array(0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F,
                            0x003F, 0x007F, 0x00FF, 0x01FF, 0x03FF, 0x07FF,
									 0x0FFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF);

	var digits = Math.floor(n / bitsPerDigit);
	var result = new BigInt("");
	arrayCopy(x.digits, digits, result.digits, 0, maxDigits - digits);
	var bits = n % bitsPerDigit;
	var leftBits = bitsPerDigit - bits;
	for (var i = 0, i1 = i + 1; i < maxDigits - 1; ++i, ++i1) {
		result.digits[i] = (result.digits[i] >>> bits) |
		                   ((result.digits[i1] & lowBitMasks[bits]) << leftBits);
	}
	result.digits[maxDigits - 1] >>>= bits;
	result.isNeg = x.isNeg;
	return result;
}

function biMultiplyByRadixPower(x, n){
	var result = new BigInt("");
	arrayCopy(x.digits, 0, result.digits, n, maxDigits - n);
	return result;
}
function biCompare(x, y){
	if (x.isNeg != y.isNeg) {
		return 1 - 2 * Number(x.isNeg);
	}
	for (var i = maxDigits - 1; i >= 0; --i) {
		if (x.digits[i] != y.digits[i]) {
			if (x.isNeg) {
				return 1 - 2 * Number(x.digits[i] > y.digits[i]);
			} else {
				return 1 - 2 * Number(x.digits[i] < y.digits[i]);
			}
		}
	}
	return 0;
}

function biDivideModulo(x, yorig){
	var nb = biNumBits(x);
	var tb = biNumBits(yorig);
	if (nb < tb) {
		// |x| < |y|
		var q, r;
		if (x.isNeg) {
			q = biFromArray(bigOne.digits);
			q.isNeg = !yorig.isNeg;
			x.isNeg = false;
			yorig.isNeg = false;
			r = biSubtract(yorig, x);
			// Restore signs, 'cause they're passed by reference.
			x.isNeg = true;
			yorig.isNeg = !q.isNeg
		} else {
			q = new BigInt("");
			r = biFromArray(x.digits);
			r.isNeg = x.isNeg;
		}
		return new Array(q, r);
	}

	//var n = biNumDigits(x);
	//var t = biNumDigits(y);
	var q = new BigInt("");
	var r = biFromArray(x.digits);
	var y = biFromArray(yorig.digits);

	// Normalize Y.
	var t = Math.ceil(tb / bitsPerDigit) - 1;
	var lambda = 0;
	while (y.digits[t] < biHalfRadix) {
		y = biShiftLeft(y, 1);
		++lambda;
		++tb;
		t = Math.ceil(tb / bitsPerDigit) - 1;
	}
	// Shift r over to keep the quotient constant. We'll shift the
	// remainder back at the end.
	r = biShiftLeft(r, lambda);
	nb += lambda; // Update the bit count for x.
	var n = Math.ceil(nb / bitsPerDigit) - 1;

	var b = biMultiplyByRadixPower(y, n - t);
	while (biCompare(r, b) != -1) {
		++q.digits[n - t];
		r = biSubtract(r, b);
	}
	for (var i = n; i > t; --i) {
		if (r.digits[i] == y.digits[t]) {
			q.digits[i - t - 1] = maxDigitVal;
		} else {
			q.digits[i - t - 1] = Math.floor((r.digits[i] * biRadix +
			                                  r.digits[i - 1]) / y.digits[t]);
		}

		var c1 = q.digits[i - t - 1] *
		         ((y.digits[t] * biRadix) + y.digits[t - 1]);
		var c2 = (r.digits[i] * biRadixSquared) +
		         ((r.digits[i - 1] * biRadix) + r.digits[i - 2]);
		while (c1 > c2) {
			--q.digits[i - t - 1];
			c1 = q.digits[i - t - 1] *
		        ((y.digits[t] * biRadix) | y.digits[t - 1]);
			c2 = (r.digits[i] * biRadix * biRadix) +
		        ((r.digits[i - 1] * biRadix) + r.digits[i - 2]);
		}

		b = biMultiplyByRadixPower(y, i - t - 1);
		r = biSubtract(r, biMultiplyDigit(b, q.digits[i - t - 1]));
		if (r.isNeg) {
			r = biAdd(r, b);
			--q.digits[i - t - 1];
		}
	}
	r = biShiftRight(r, lambda);
	// Fiddle with the signs and stuff to make sure that 0 <= r < y.
	q.isNeg = x.isNeg != yorig.isNeg;
	if (x.isNeg) {
		if (yorig.isNeg) {
			q = biAdd(q, bigOne);
		} else {
			q = biSubtract(q, bigOne);
		}
		y = biShiftRight(y, lambda);
		r = biSubtract(y, r);
	}
	// Check for the unbelievably stupid degenerate case of r == -0.
	if (r.digits[0] ==0 && biNumDigits(r) == 0) r.isNeg = false;

	return new Array(q, r);
}
function biModulo(x, y){
	return biDivideModulo(x, y)[1];
}

function biMultiplyMod(x, y, m){
	return biModulo(biMultiply(x, y), m);
}

function biPowMod(x, y, m){
	var result = new BigInt("");
	result.digits[0] = 1;
	var a = biFromArray(x.digits);
	a.isNeg = x.isNeg;
	var k = biFromArray(y.digits);
	while (true) {
		if ((k.digits[0] & 1) != 0) 
			result = biMultiplyMod(result, a, m);
		k = biShiftRight(k, 1);
		if (k.digits[0] == 0 && biNumDigits(k) == 0) break;
		a = biMultiplyMod(a, a, m);
	}
	return result;
}

