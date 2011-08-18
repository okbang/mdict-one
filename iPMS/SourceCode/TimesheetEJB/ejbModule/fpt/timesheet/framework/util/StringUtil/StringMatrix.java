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
 * File:        StringMatrix.java
 * Author:      Nguyen Thai Son - FPT Software
 * Description: This util will do following tasks:
 *
 * Revisions:   2002.01.07 - Nguyen Thai Son - First written
 * Copyright:   Copyright (c) FPT Software. All rights reserved.
 /******************************************************************************/

package fpt.timesheet.framework.util.StringUtil;

import java.util.*;

import fpt.timesheet.framework.util.CommonUtil.*;
import fpt.timesheet.framework.util.StringUtil.*;
import org.apache.log4j.Logger;

public class StringMatrix implements java.io.Serializable {

	private static Logger logger = Logger.getLogger(StringMatrix .class.getName());
    
    int cols = 0;
    ArrayList Rows = null;

    public StringMatrix(String data) {
        ConvertToStrMatrix(data);
    }

    public StringMatrix() {
        cols = 0;
        Rows = new ArrayList();
    }

    public StringMatrix(int rows, int cols) {
        if ((rows > 0) && (cols > 0)) {
            this.cols = cols;
            Rows = new ArrayList();
            for (int i = 0; i < rows; i++) {
                ArrayList strRow = new ArrayList();
                for (int j = 0; j < cols; j++)
                    strRow.add("");
                Rows.add(strRow);
            }
        }
    }

    public boolean removeRow(int rowNum) {
        if (Rows == null)
            return false;
        if (Rows.remove(rowNum) != null)
            return true;
        return false;
    }

    public boolean addRow(StringVector rowVector) {
        if (rowVector == null)
            return false;
        if (Rows == null) {
            Rows = new ArrayList();
            this.cols = rowVector.getDim();
        }
        else if (this.getNumberOfRows() == 0) {
            this.cols = rowVector.getDim();
        }
        if (rowVector.getDim() != this.cols)
            return false;
        ArrayList strRow = new ArrayList();
        for (int i = 0; i < cols; i++)
            strRow.add(rowVector.getCell(i));
        return Rows.add(strRow);
    }

    public boolean insertRow(int rowNum, StringVector rowVector) {
        if (rowVector.getDim() != this.cols)
            return false;
        if (Rows == null)
            Rows = new ArrayList();
        ArrayList strRow = new ArrayList();
        for (int i = 0; i < cols; i++)
            strRow.add(rowVector.getCell(i));
        Rows.add(rowNum, strRow);
        return true;
    }

    private boolean ConvertToStrMatrix(String data) {
        int i,j,startIndex,endIndex;
        startIndex = 0;
        endIndex = data.indexOf(":", startIndex);
        int rows = CommonUtil.StrToInt(data.substring(startIndex, endIndex));
        startIndex = endIndex + 1;
        endIndex = data.indexOf(":", startIndex);
        cols = CommonUtil.StrToInt(data.substring(startIndex, endIndex));
        if ((rows == 0) || (cols == 0)) {
            Rows = null;
            return false;
        }
        Rows = new ArrayList();
        String strCell;
        int len;
        for (i = 0; i < rows; i++) {
            ArrayList strRow = new ArrayList();
            for (j = 0; j < cols; j++) {
                startIndex = endIndex + 1;
                endIndex = data.indexOf(":", startIndex);
                len = CommonUtil.StrToInt(data.substring(startIndex, endIndex));
                startIndex = endIndex + 1;
                endIndex = endIndex + len;
                strCell = data.substring(startIndex, endIndex + 1);
                strRow.add(strCell);
            }
            Rows.add(strRow);
        }
        return true;
    }

    public int getNumberOfRows() {
        if (Rows != null)
            return Rows.size();
        return 0;
    }

    public int getNumberOfCols() {
        return cols;
    }

    public String getCell(int row, int col) {
        if ((row >= this.getNumberOfRows()) || (row < 0)) {
            return "Invalid Row Index";
        }
        if ((col >= this.getNumberOfCols()) || (col < 0)) {
            return "Invalid Column Index";
        }
        ArrayList strRow = (ArrayList) Rows.get(row);
        return ((String) strRow.get(col));
    }

    public boolean setCell(int row, int col, String data) {
        if ((row >= this.getNumberOfRows()) || (row < 0)) {
            return false;
        }
        if ((col >= this.getNumberOfCols()) || (col < 0)) {
            return false;
        }
        ArrayList strRow = (ArrayList) Rows.get(row);
        if (data == null) {
            strRow.set(col, "");
            return true;
        }
        strRow.set(col, data);
        return true;
    }

    public String toString() {
//      String returnStr = CommonUtil.IntToStr(this.getNumberOfRows()) + ":" + CommonUtil.IntToStr(this.getNumberOfCols()) + ":";
		String returnStr = "";
        StringBuffer returnStrBuff = new StringBuffer(CommonUtil.IntToStr(this.getNumberOfRows()) + ":" + CommonUtil.IntToStr(this.getNumberOfCols()) + ":");
        int i,j;
        String tempStr;

        for (i = 0; i < this.getNumberOfRows(); i++) {
            ArrayList strRow = (ArrayList) Rows.get(i);
            for (j = 0; j < this.getNumberOfCols(); j++) {
                tempStr = (String) strRow.get(j);
//              returnStr += CommonUtil.IntToStr(tempStr.length()) + ":" + tempStr;
				returnStrBuff.append(CommonUtil.IntToStr(tempStr.length()) + ":" + tempStr);
            }
        }
        returnStr = returnStrBuff.toString();
        return returnStr;
    }

    public boolean removeCheckedRows(StringVector checkedVector) {
        int numTemp = 0;
        for (int i = 0; i < checkedVector.getDim(); i++)
            if (checkedVector.getCell(i).equals("yes")) {
                this.removeRow(i - numTemp);
                numTemp++;
            }
        return true;
    }

    public boolean hasMember(String key, int colToCompare) {
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            if (this.getCell(i, colToCompare).equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    public int indexOf(String key, int colToCompare) {
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            if (this.getCell(i, colToCompare).equalsIgnoreCase(key)) {
                return i;
            }
        }
        return -1;
    }

    public void setCheckedRows(StringVector checkedRows, int col) {
        if (col > this.getNumberOfCols())
            return;
        if (checkedRows == null)
            return;
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            if (checkedRows.getCell(i).equals("yes"))
                this.setCell(i, col, "checked");
            else
                this.setCell(i, col, "");
        }
    }

    public StringVector getRow(int rowNum) {
        if (this.getNumberOfCols() == 0)
            return null;
        if (rowNum == 0)
            return null;
        if (rowNum > this.getNumberOfRows())
            return null;
        StringVector strVector = new StringVector(this.getNumberOfCols());
        for (int i = 0; i < this.getNumberOfCols(); i++) {
            strVector.setCell(i, this.getCell(rowNum, i));
        }
        return strVector;
    }

    public StringVector getColumn(int colNum) {
        if (this.getNumberOfRows() == 0)
            return null;
        if (colNum < 0)
            return null;
        if (colNum > this.getNumberOfCols())
            return null;
        StringVector strVector = new StringVector(this.getNumberOfRows());
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            strVector.setCell(i, this.getCell(i, colNum));
        }
        return strVector;
    }

    public StringMatrix getCheckedRows(int col) {
        if (col > this.getNumberOfCols())
            return null;
        if (this.getNumberOfRows() == 0)
            return null;
        int numCheckedRows = 0;
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            if ((this.getCell(i, col).equals("checked")) || (this.getCell(i, col).equals("yes"))) {
                numCheckedRows++;
            }
        }
        StringMatrix strMatrix = new StringMatrix(numCheckedRows, this.getNumberOfCols());
        int rowNum = 0;
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            if ((this.getCell(i, col).equals("checked")) || (this.getCell(i, col).equals("yes"))) {
                for (int j = 0; j < strMatrix.getNumberOfCols(); j++) {
                    strMatrix.setCell(rowNum, j, this.getCell(i, j));
                }
                rowNum++;
            }
        }
        return strMatrix;
    }

    public void removeCheckedRows(int col) {
        if (col > this.getNumberOfCols())
            return;
        if (this.getNumberOfRows() == 0)
            return;
        boolean stopFlag = false;
        int i = 0;
        while (!stopFlag) {
            if ((this.getCell(i, col).equals("checked")) || (this.getCell(i, col).equals("yes"))) {
                this.removeRow(i);
                continue;
            }
            if (i > this.getNumberOfRows())
                stopFlag = true;
            i++;
        }
    }

    public void sortByColumn(int colNum, boolean isDate) {
        if (Rows == null || Rows.size() == 0 || colNum >= cols)
            return;
        //sort rows
        for (int i = 0; i < Rows.size() - 1; i++) {
            ArrayList checkRow = (ArrayList) Rows.get(i);
            String biggest = (String) checkRow.get(colNum);
            if (isDate)
                biggest = biggest.substring(6) + biggest.substring(0, 2) + biggest.substring(3, 5);
            int biggestRowNum = i;
            for (int j = i + 1; j < Rows.size(); j++) {
                ArrayList singleRow = (ArrayList) Rows.get(j);
                String item = (String) singleRow.get(colNum);
                //if date type column mm/dd/yyyy format!
                if (isDate) {
                    item = item.substring(6) + item.substring(0, 2) + item.substring(3, 5);
                    /*
                    //this might be needed to sort any numeric field columns not sure for now!!
                                  if(Integer.parseInt(item)>Integer.parseInt(biggest)){
                                    biggest=item;
                                    biggestRowNum=j;
                                  }
                                  continue;
                    */
                }
                //other string type columns
                if (item.compareToIgnoreCase(biggest) > 0) {
                    biggest = item;
                    biggestRowNum = j;
                }
            }
            //swap the biggest and checkRow.
            if (biggestRowNum > i)
                swapRows(i, biggestRowNum);
        }
    }

    private void swapRows(int a, int b) {
        ArrayList tmpRow = (ArrayList) Rows.get(a);
        Rows.set(a, Rows.get(b));
        Rows.set(b, tmpRow);
    }

    public void sortByFloatColumn(int colNum) {
        if (Rows == null || Rows.size() == 0 || colNum >= cols)
            return;
        //sort rows
        for (int i = 0; i < Rows.size() - 1; i++) {
            ArrayList checkRow = (ArrayList) Rows.get(i);
            String biggest = (String) checkRow.get(colNum);
            String bigname = (String) checkRow.get(2);
            //int biggestRowNum = i;
            for (int j = i + 1; j < Rows.size(); j++) {
                ArrayList singleRow = (ArrayList) Rows.get(j);
                String item = (String) singleRow.get(colNum);
                String name = (String) singleRow.get(2);
                if (Float.parseFloat(item) > Float.parseFloat(biggest)) {
                    swapRows(i, j);
                    biggest = item;
                    bigname = (String) singleRow.get(2);
                }
                else {
                    if (Float.floatToIntBits(Float.parseFloat(item) * 100) == Float.floatToIntBits(Float.parseFloat(biggest) * 100)) {
                        if (bigname.compareToIgnoreCase(name) > 0) {
                            swapRows(i, j);
                            bigname = name;
                        }
                    }
                } //end elses
            }//end for
            //swap the biggest and checkRow.

        }//end for
    } //end function
}