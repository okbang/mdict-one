/*
 * Copyright (c) 2009, FPT Software JSC
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *	- Neither the name of the FPT Software JSC nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 /*******************************************************************************
 * File:        StringVector.java
 * Author:      Nguyen Thai Son - FPT Software
 * Description: common utilities
 *              This util will do following tasks:
 *              - convert an integer to a string

 * Revisions:   2002.01.07 - Nguyen Thai Son - First written
 * Copyright:   Copyright (c) FPT Software. All rights reserved.
 /******************************************************************************/

package fpt.ncms.util.StringUtil;

import fpt.ncms.util.*;

public final class StringVector {
    private int m_nDim = 0;
    StringMatrix m_smxVector = null;

    /**************************************************************************
     Function: public StringVector(String strVector)
     Description: initiate a string vector from a string
     Parameters:
     - strVector[in]: a string
     Return:
     Author: Nguyen Thai Son
     Created date: 07 Jan 2002
     **************************************************************************/

    public StringVector(String strVector) {
        m_smxVector = new StringMatrix(strVector);
        m_nDim = m_smxVector.getNumberOfCols();
    }

    /**************************************************************************
     Function: public StringVector(int vectorDim)
     Description: initiate a string vector from a dimension
     Parameters:
     - vectorDim[in]: an integer
     Return:
     Author: Nguyen Thai Son
     Created date: 07 Jan 2002
     **************************************************************************/

    public StringVector(int vectorDim) {
        m_nDim = vectorDim;
        m_smxVector = new StringMatrix(1, vectorDim);
    }

    /**************************************************************************
     Function: public int getDim()
     Description: get size of the current vector
     Parameters: void
     Return: int
     Author: Nguyen Thai Son
     Created date: 07 Jan 2002
     **************************************************************************/
    public final int getDim() {
        return m_nDim;
    }

    public final String getCell(int i) {
        return m_smxVector.getCell(0, i);
    }

    public final boolean setCell(int i, String strdata) {
        return m_smxVector.setCell(0, i, strdata);
    }

    public final String toString() {
        return m_smxVector.toString();
    }

    public final int indexOf(String Key) {
        for (int i = 0; i < this.getDim(); i++) {
            if (this.getCell(i).equalsIgnoreCase(Key))
                return i;
        }
        return -1;
    }

    /**************************************************************************
     Function: public boolean hasMember(String key)
     Description: search for key whether or not.
     Parameters:
     - key: searching value
     Return: boolean
     Author: Nguyen Thai Son
     Created date: 07 Jan 2002
     **************************************************************************/

    public final boolean hasMember(String key) {
        for (int i = 0; i < this.getDim(); i++) {
            if (this.getCell(i).equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

}