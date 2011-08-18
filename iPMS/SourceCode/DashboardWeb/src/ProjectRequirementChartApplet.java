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

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class ProjectRequirementChartApplet extends java.applet.Applet {

    Font titleFont;
    FontMetrics titleFontMetrics;

    int columns;
    int values[];
    Object labels[];

    int max;
    double scale = 5;
    int maxLabelWidth;
    int barWidth;
    int barSpacing = 20;

    public synchronized void init() {

        String rs;

        titleFont = new java.awt.Font("Courier", Font.BOLD, 12);
        titleFontMetrics = getFontMetrics(titleFont);

        rs = getParameter("columns");
        if (rs == null) {
            columns = 0;
        }
        else {
            columns = Integer.parseInt(rs);
        }

        // get array of values
        values = new int[columns];
        labels = new String[columns];

        for (int i = 0; i < columns; i++) {

            // parse the value for this column
            rs = getParameter("C" + (i + 1));

            if (rs != null) {
                try {
                    values[i] = Integer.parseInt(rs);

                }
                catch (NumberFormatException e) {
                    values[i] = 0;
                }
            }

            // max = maximum of (values)
            if (values[i] > max) {
                max = values[i];
            }
            // parse the label for this column
            rs = getParameter("C" + (i + 1) + "_label");
            labels[i] = (rs == null) ? "" : rs;

            maxLabelWidth =
                    Math.max(titleFontMetrics.stringWidth(
                                (String) (labels[i])), maxLabelWidth);
        }

        barWidth = titleFont.getSize();
        resize(
                (int) (max * scale) + maxLabelWidth + 5,
                (columns * (barWidth + barSpacing)) + titleFont.getSize() + 10);
    }

    public synchronized void paint(Graphics g) {

        int i, j;
        int cx, cy;
        char l[] = new char[1];

        int topWidth;
        int deltaSpacing = 0;
        int total = 0;

        cx = maxLabelWidth + 10;
        topWidth = 10;
        cy = topWidth;

        // tinh scale:
        if (max > 0) {
            scale = (double) (getSize().width - cx - 50) / (double) (max + 10);
        }
        if (scale == 0) {
            scale = 0.1;
        }

        for (i = 0; i < columns; i++) {

            cy += barSpacing + barWidth;
            cx = maxLabelWidth + 10;

            // draw the labels
            g.setColor(Color.black);
            g.drawString(
                    (String) labels[i],
                    cx - maxLabelWidth - 5,
                    cy + titleFontMetrics.getAscent());

            g.fillRect(cx + 3, cy + 5, (int) (values[i] * scale), barWidth);

            //g.setColor((Color)(colors[i]));

            g.setColor(new Color(121, 121, 255));
            g.fillRect(cx, cy, (int) (values[i] * scale), barWidth);

            // value
            g.setColor(Color.black);
            g.drawString(
                    " " + values[i],
                    cx + (int) (values[i] * scale) + 5,
                    cy + titleFontMetrics.getAscent());
        }

        // Ve cot toa do

        cx = maxLabelWidth + 10;
        cy += barWidth + barWidth;
        // draw the line
        g.setColor(Color.white);
        // cot dung
        g.drawLine(maxLabelWidth + 5, topWidth + 10 + 5, maxLabelWidth + 5, cy);

        // cot ngang
        g.drawLine(maxLabelWidth + 5, cy, getSize().width - 50, cy);

        // danh so
        int step = 10;

        g.setColor(Color.black);

        if (scale <= 5) {
            step = 5 * step;
        }

        for (i = 0; i < (max + 10); i += step) {

            g.drawString(
                    " " + i,
                    maxLabelWidth + 5 + (int) (i * scale),
                    cy + 3 + titleFontMetrics.getAscent());
        }
    }
}