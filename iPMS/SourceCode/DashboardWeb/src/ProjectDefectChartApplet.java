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

public class ProjectDefectChartApplet extends java.applet.Applet {
    Font titleFont;
    FontMetrics titleFontMetrics;

    int columns;
    int batch_nums;

    Object notes_label[];
    Object notes_color[];

    int values[];
    Object labels[];

    Color oneColor = new Color(121, 121, 255);
    Color twoColor = new Color(153, 51, 102);

    int max;
    int total = 0;
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

        rs = getParameter("batch_nums");
        if (rs == null) {
            batch_nums = 1;
        }
        else {
            batch_nums = Integer.parseInt(rs);
        }

        // get array of values

        values = new int[columns];
        labels = new String[columns];

        notes_label = new String[batch_nums];
        notes_color = new Color[batch_nums];

        for (int i = 0; i < batch_nums; i++) {
            rs = getParameter("C" + (i + 1) + "_note_label");
            notes_label[i] = (rs == null) ? "" : rs;
        }

        notes_color[0] = oneColor;
        notes_color[1] = twoColor;

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

            if ((i % batch_nums) == 0) {
                total = values[i];
            }
            else {
                total += values[i];
            }

            if (total > max) {
                max = total;
            }

            // parse the label for this column
            rs = getParameter("C" + (i + 1) + "_label");
            labels[i] = (rs == null) ? "" : rs;

            maxLabelWidth =
                    Math.max(titleFontMetrics.stringWidth(
                            (String)(labels[i])), maxLabelWidth);
        }

        barWidth = titleFont.getSize();
    }

    public synchronized void paint(Graphics g) {

        int i, j;
        int cx, cy;
        char l[] = new char[1];

        int topWidth = 0;
        int deltaSpacing = 0;

        cx = maxLabelWidth + 10;

        // tinh scale:
        if (max > 0) {
            scale = (double) (getSize().width - cx - 50) / (double) (max + 10);
        }

        if (scale == 0) {
            scale = 0.1;
        }

        // top - phan tren cua bieu do, gom title + comment

        topWidth = 10; // - barWidth;

        cy = topWidth;

        for (i = 0; i < columns; i++) {

            if ((i % batch_nums) == 0) {

                deltaSpacing = 0;
                total = values[i];

                // set the Y coordinate
                cy += barSpacing + barWidth;

                // set the X coordinate to be the getSize().width of the widest
                // label
                cx = maxLabelWidth + 10;

            }
            else {
                total += values[i];
                // set the X coordinate to be the getSize().width of the widest label
                cx += deltaSpacing;
            }

            // ?

            g.setColor(Color.black);

            if ((i % batch_nums) == 0) {
                // draw the labels
                g.drawString(
                        (String) labels[i],
                        cx - maxLabelWidth - 5,
                        cy + titleFontMetrics.getAscent());
            }

            g.fillRect(cx + 3, cy + 5, (int) (values[i] * scale), barWidth);

            deltaSpacing = (int) (values[i] * scale);

            if ((((i + 1) % batch_nums) == 0) || (i == (columns - 1))) {
                g.setColor(twoColor);
            }
            else {
                g.setColor(oneColor);
            }

            // draw the bar
            g.fillRect(cx, cy, (int) (values[i] * scale), barWidth);

            // last value & total value
            if ((((i + 1) % batch_nums) == 0) || (i == (columns - 1))) {

                g.setColor(twoColor);
                g.drawString(
                        Integer.toString(values[i]).trim(),
                        cx - 4 + (int) ((values[i] * scale) / 2),
                        cy - 1);

                g.setColor(Color.black);
                g.drawString(
                        "  " + total,
                        cx + (int) (values[i] * scale) + 5,
                        cy + titleFontMetrics.getAscent());
            }
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

        int maxVolume = max + 10;
        int step = 10;

        if (scale <= 5) {
            step = 5 * step;
        }

        g.setColor(Color.black);

        for (i = 0; i < maxVolume; i += step) {
            g.drawString(
                    " " + i,
                    maxLabelWidth + 5 + (int) (i * scale),
                    cy + 3 + titleFontMetrics.getAscent());
        }

        // draw the comment - strip line

        int noteDeltaX = 0;
        int noteWidth = 20;

        cy += barWidth;

        for (i = 0; i < batch_nums; i++) {
            g.setColor(Color.black);
            g.fillRect(cx + noteDeltaX + 3, cy + barSpacing + 5, noteWidth, barWidth);

            g.setColor((Color) (notes_color[i]));

            g.fillRect(cx + noteDeltaX, cy + barSpacing, noteWidth, barWidth);

            noteDeltaX += noteWidth + 5 + 2;

            g.setColor(Color.black);
            g.drawString(
                    (String) (notes_label[i]),
                    cx + noteDeltaX,
                    cy + barSpacing + barWidth);

            noteDeltaX += titleFontMetrics.stringWidth((String) (notes_label[i])) + 7;
        }
    }
}