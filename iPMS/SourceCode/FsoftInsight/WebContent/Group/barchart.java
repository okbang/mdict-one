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
 
 /**
* Stolen from http://javaboutique.internet.com/Jaek_Graph/
* heavily edited by Manu, PhuongNT, and Kevin Groeminne
* 6-nov-03
**/
import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.text.DecimalFormat;

/**
 * barchart
 */
public class barchart extends Applet {
    private String linesep= null;
    private Graph graph = null;
    private ScrollPane scrollPane= null;
    private ScrollableCanvas canvas= null;

    private Tooltip tooltip = null;
    private Util u= null;
    public Param param;
    public barchart() {}
	private class Param{
		public final int TYPE_CUBE=0;
		public final int TYPE_LINE=1;
		public final int TYPE_DOT=2;
		public String title=null;
		public boolean bAffTitle= false;
		public boolean bTooltip= false;
		public boolean bAffXGrid= false;
		public int graphType;
		public String xwidth=null;
		public boolean bAffLabelX= false;
		public boolean bAffLabelY= false;
        public boolean  bAffGrid= false ;
        public boolean bAffLegend= false;
		Param() {
			title= getParameter("TITLE");
			bAffTitle= parmBoolean("DRAW_TITLE", true);
			bTooltip = parmBoolean("TOOLTIP", true);
			bAffXGrid=parmBoolean("X_GRID", true);

			xwidth=getParameter("X_WIDTH");
			bAffLabelX= parmBoolean("X_LABEL", true);
			bAffLabelY= parmBoolean("Y_LABEL", true);
			bAffGrid= parmBoolean("Y_GRID", true);
			bAffLegend = parmBoolean("LEGEND", true);
			String strGraphType= getParameter("TYPE");
			if (strGraphType.equalsIgnoreCase("LINE"))
				graphType=TYPE_LINE;
			else if (strGraphType.equalsIgnoreCase("DOT"))
				graphType=TYPE_DOT;
			else
				graphType=TYPE_CUBE;
		}

		 private boolean parmBoolean(String s, boolean flag) {
		         String s1 = getParameter(s);
		         if ("YES".equals(s1)) {
		             return true;
		         }
		         if ("NO".equals(s1)) {
		             return false;
		         }
		         return flag;
   		}
	}



    public void init() {
		param=new Param();
        linesep = System.getProperty("line.separator");
        setBackground(Color.white);
		u = new Util();
        graph = new Graph();
        canvas = new ScrollableCanvas();
        setLayout(new BorderLayout());
        scrollPane = new ScrollPane();
        scrollPane.add(canvas);
        add("Center", scrollPane);
        if (param.bTooltip) {
            canvas.addMouseMotionListener(new mouseMot(graph.lines));
        }

    }

    private void draw_frame(Graphics g, Rectangle rectangle) {
        g.setColor(Color.black);
        g.drawLine(rectangle.x + graph.margext, rectangle.y + graph.margext,
                rectangle.x + graph.margext, rectangle.y + rectangle.height + graph.margext);
        g.drawLine(rectangle.x + graph.margext, rectangle.y + rectangle.height + graph.margext,
                rectangle.x + rectangle.width + graph.margext,
                rectangle.y + rectangle.height + graph.margext);
    }
    /**
     * ScrollableCanvas
     */
    private class ScrollableCanvas extends Canvas {
        public Dimension getPreferredSize() {
            if (graph == null ||(graph.totalDim == null)) {
                return new Dimension(0, 0);
            }
            return graph.totalDim;
        }

        public void paint(Graphics g) {
            graph.draw(g);
            if (tooltip != null) {
                tooltip.draw(g);
            }
        }
    }

    /**
     * mouseMot
     */
    public class mouseMot extends MouseMotionAdapter {
        private Lines lines;

        public mouseMot(Lines lines1) {
            lines = lines1;
        }

        public void mouseMoved(MouseEvent mouseevent) {
            if ((lines == null)||(lines.line == null)) {
                return;
            }
            Point point = mouseevent.getPoint();
            Tooltip tooltip1 = null;

            for (int i = 0; i < lines.line.length; i++) {
                Line line = lines.line[i];

                for (int j = 0; j < line.rect.length; j++) {
                    Rectangle rectangle = line.rect[j];

                    if (rectangle.contains(point)) {
                        if (tooltip1 == null) {
                            tooltip1 = new Tooltip(point);
                        }

                        tooltip1.add(Double.toString(line.value[j]), line.color);
                    }
                }
            }

            if (tooltip1 != null) {
                if ((tooltip == null) ||(!tooltip.equals(tooltip1))) {
                    tooltip = tooltip1;
                }

                canvas.repaint();
            }
            else if (tooltip != null) {
                tooltip = null;
                canvas.repaint();
            }
        }
    }

    /**
     * Line
     */
    private class Line {
        private int lineIndex;
        private double max_value;
        private double min_value;
        private double[] value;
        private Color color;
        private String name;
        private Rectangle[] rect;
        private boolean bAffBullet;

        Line(String s, Lines lines, int i) {
            lineIndex = i;
            name = getParameter(s + "NAME");
			bAffBullet = param.parmBoolean(s + "BULLET", true);
            String s1 = getParameter(s + "COLOR");

            if (s1 != null) {
                StringTokenizer stringtokenizer = new StringTokenizer(s1, ",");

                if (stringtokenizer.countTokens() == 3) {
                    int j = Integer.parseInt(stringtokenizer.nextToken());
                    int l = Integer.parseInt(stringtokenizer.nextToken());
                    int i1 = Integer.parseInt(stringtokenizer.nextToken());
                    color = new Color(j, l, i1);
                }
            }

            if (color == null) {
                color = lines.nextColor();
            }

            paramsValues(s);

            max_value = 0.0D;
            min_value = 0.0D;
            for (int k = 0; k < value.length; k++) {
                if (value[k] > max_value) {
                    max_value = value[k];
                }else if (value[k] < min_value) {
                    min_value = value[k];
                }
            }
        }

        private void paramsValues(String s) {
            int i;

            for (i = 0; getParameter(s + "V" + (i + 1)) != null; i++) {
            }

            rect = new Rectangle[i];
            value = new double[i];

            for (int j = 0; j < i; j++) {
                value[j] = (new Double(getParameter(s + "V" + (j + 1)))).doubleValue();
            }
        }

        private void draw_grid(Graphics g, Rectangle rectangle, int i,
            Lines lines) {
            if (!param.bAffXGrid) {
                return;
            }

            g.setColor(Color.lightGray);

            double d = lines.getYvalue(lines.mid_value, rectangle);

            for (int j = 0; j < value.length; j++) {
                int k = rectangle.x + (j * i);
                double d1 = lines.getYvalue(value[j], rectangle);
                g.drawLine(k + graph.margext, (int) (d + (double) graph.margext), k + graph.margext,
                        (int) (d1 + (double) graph.margext));
            }
        }

        private void draw_line(Graphics g, Rectangle rectangle, int i,
            Lines lines) {
            Point point = null;
            g.setColor(color);
            int maxIteration = value.length;
            int bottom = (int) lines.getYvalue(lines.mid_value, rectangle);
			int xval,rectWidth,rectLength;
            for (int j = 0; j < maxIteration; j++) {
                int k = rectangle.x + (j * i);
                double d = lines.getYvalue(value[j], rectangle);
                Point point1 = new Point(k, (int) d);
				xval=point1.x + graph.margext + ((lines.x_width / lines.n_serie) * (lineIndex));


                if (param.graphType==param.TYPE_CUBE) {
					rectWidth=(lines.x_width / lines.n_serie) - 1;
					rectLength=bottom - point1.y;
                    if (point == null) {
                        if (rectLength >= 0) {
                            g.fillRect(xval,point1.y + graph.margext,rectWidth,rectLength);
                            rect[j] = new Rectangle(xval,point1.y + graph.margext, rectWidth,rectLength);
                        }
                        else {
                            g.fillRect(xval,bottom + graph.margext, rectWidth,-rectLength);
                            rect[j] = new Rectangle(xval,bottom + graph.margext, rectWidth,-rectLength);
                        }
                    }
                }
                else if ((param.graphType==param.TYPE_LINE)||(param.graphType==param.TYPE_DOT)) {
					rect[j] = new Rectangle((point1.x - 2) + graph.margext, (point1.y - 2) + graph.margext, 4, 4);
                    if(bAffBullet)
	                    g.drawRect(rect[j].x, rect[j].y, rect[j].width, rect[j].height);
					//dot graph can display line if bullets are not specified
                    if ((param.graphType==param.TYPE_LINE||(!bAffBullet&&param.graphType==param.TYPE_DOT))&&(point != null) && (point1 != null)) {
                        g.drawLine(point.x + graph.margext, point.y + graph.margext,
                                point1.x + graph.margext, point1.y + graph.margext);
                    }

                    point = point1;
                }
            }
        }
    }

    /**
     * Lines
     */
    private class Lines {
        private double max_value;
        private double min_value;
        private double mid_value;
        private int x_max_count;
        public int x_width;
        public int n_values;
        private Line[] line;
        private int n_color;
        private Color[] colors;
        public int n_serie;

        public Lines() {
            n_color = 0;
            colors = (new Color[] {
                    Color.blue, Color.green, Color.red, Color.cyan, Color.yellow,
                    Color.magenta, Color.orange, Color.pink
                });
            max_value = 0.0D;
            min_value = 0.0D;
            mid_value = 0.0D;
            x_max_count = 0;
            x_width =(param.xwidth == null)? 12:Integer.parseInt(param.xwidth);
            n_values = 0;
            if (x_width < 1) {
                x_width = 12;
            }

            for (n_serie = 0;
                    getParameter("L" + (n_serie + 1) + "_NAME") != null;
                    n_serie++) {
            }

            line = new Line[n_serie];

            for (int j = 0; j < line.length; j++) {
                line[j] = new Line("L" + (j + 1) + "_", this, j);

                if (line[j].value.length > x_max_count) {
                    x_max_count = line[j].value.length;
                }

                if (line[j].max_value > max_value) {
                    max_value = line[j].max_value;
                }

                if (line[j].min_value < min_value) {
                    min_value = line[j].min_value;
                }

                if (line[j].value.length > n_values) {
                    n_values = line[j].value.length;
                }
            }
        }

        public Color nextColor() {
            return colors[n_color++ % colors.length];
        }

        public int getWidth() {
            return (x_max_count) * x_width;
        }

        public void draw(Graphics g, Rectangle rectangle) {
            for (int i = 0; i < line.length; i++) {
                line[i].draw_grid(g, rectangle, x_width, this);
            }

            for (int j = 0; j < line.length; j++) {
                line[j].draw_line(g, rectangle, x_width, this);
            }
        }

        public double getYvalue(double d, Rectangle rectangle) {
            return (double) (rectangle.y + rectangle.height) -
                    (((double) rectangle.height / (max_value - min_value)) * (d - min_value));
        }
    }

    /**
     * Title
     */
    private class Title {
        private Rectangle rect;
        public Title() {}

        public int getWidth() {
                return (!param.bAffTitle || (rect == null)) ?0:rect.width;
        }

        public int getHeight() {
                return (!param.bAffTitle || (rect == null))?0:(rect.height + 5);
        }

        private void setGraph(Graphics g) {
            g.setColor(Color.darkGray);
            u.deriveFont(g, 1, 11);
        }

        private void calc(Graphics g) {
            if ((param.title != null) && param.bAffTitle) {
                setGraph(g);
                rect = u.calcTextBounds(g, param.title);
            }
        }

        private void draw(Graphics g, Rectangle rectangle) {
            if ((param.title == null) || !param.bAffTitle) {
                return;
            }

            setGraph(g);

            int i = ((rectangle.width - rect.width) / 2) + rectangle.x;

            if (i < rectangle.x) {
                i = rectangle.x;
            }

            g.drawString(param.title, i + graph.margext, rect.height + graph.margext);
        }
    }

    /**
     * YLabels
     */
    private class YLabels {
        private Object[] label;
        private int[] width;
        private int max_width;
        DecimalFormat decFormat;

        public YLabels() {
            max_width = 0;
            decFormat = new DecimalFormat("0");
        }

        public int getWidth() {
            if (!param.bAffLabelY) {
                return 0;
            }
            else {
                return max_width + 5;
            }
        }

        private void setGraphs(Graphics g) {
            u.deriveFont(g, 0, 11);
            g.setColor(Color.lightGray);
        }

        private void calc(Graphics g, double d, double d1, int i) {
            setGraphs(g);

            if ((d < 0.0D) && (d1 > 0.0D)) {
                label = u.arrayAddObject(label, new Double(0.0D));
            }

            for (int j = 0; j <= 5; j++) {
                label = u.arrayAddObject(label, new Double(d + j * (d1 - d) / 5));
            }

            //label = u.arrayAddObject(label, new Double(d));
            //label = u.arrayAddObject(label, new Double(d1));

            max_width = 0;
            width = new int[label.length];

            for (int j = 0; j < label.length; j++) {
                Rectangle rectangle = u.calcTextBounds(g, decFormat.format(label[j]));
                width[j] = rectangle.width;

                if (width[j] > max_width) {
                    max_width = width[j];
                }
            }
        }

        private void draw(Graphics g, Lines lines, Rectangle rectangle) {
            setGraphs(g);

            int i = rectangle.y + rectangle.height;

            for (int j = 0; j < label.length; j++) {
                double d = lines.getYvalue(((Double) label[j]).doubleValue(),
                        rectangle);
                int k = rectangle.x - width[j] - 2;

                if (param.bAffLabelY) {
                    g.drawString(decFormat.format(label[j]), k + graph.margext,
                            (int) (d + 5D + (double) graph.margext));
                }

                if (param.bAffGrid) {
                    g.drawLine(rectangle.x + graph.margext, (int) (d + (double) graph.margext),
                            rectangle.x + rectangle.width + graph.margext,
                            (int) (d + (double) graph.margext));
                }
            }
        }
    }

    /**
     * XLabels
     */
    private class XLabels {
        protected String[] label;
        protected int max_height;

        public XLabels() {
            int i;
            for (i = 0; getParameter("LBL" + (i + 1)) != null; i++) {
            }

            label = new String[i];
            max_height = 0;

            for (int j = 0; j < label.length; j++) {
                label[j] = getParameter("LBL" + (j + 1));
            }
        }

        public int getHeight() {
            if (!param.bAffLabelX) {
                return 0;
            }
            else {
                return max_height + 5;
            }
        }

        public void setGraph(Graphics g, int i) {
            u.deriveFont(g, 0, i);
            g.setColor(Color.darkGray);
        }

        public void calc(Graphics g) {
            if (!param.bAffLabelX) {
                return;
            }

            setGraph(g, 11);

            for (int i = 0; i < label.length; i++) {
                int j = label[i].length();
                int k = 0;

                for (int l = 0; l < j; l++) {
                    Rectangle rectangle = u.calcTextBounds(g, label[i].substring(l, l + 1));
                    k += -rectangle.y;
                }

                if (k > max_height) {
                    max_height = k;
                }
            }
        }

        public void draw(Graphics g, Rectangle rectangle, int i) {
            if (!param.bAffLabelX) {
                return;
            }

            setGraph(g, 11);

            for (int j = 0; j < label.length; j++) {
                int k = (j * i) + rectangle.x;
                int l = rectangle.y + rectangle.height + 2;
                int i1 = label[j].length();

                for (int j1 = 0; j1 < i1; j1++) {
                    String s = label[j].substring(j1, j1 + 1);
                    Rectangle rectangle1 = u.calcTextBounds(g, s);

                    if (j1 == 0) {
                        l -= rectangle1.y;
                    }

                    g.drawString(s, ((graph.margext + k) - (rectangle1.width / 2)),
                            l + graph.margext - (j1 * rectangle1.y));
                }
            }
        }
    }

    /**
     * Legend
     */
    private class Legend {
        private int max_width;
        private int[] width;

        public Legend() {

        }

        public int getWidth() {
            if (!param.bAffLegend) {
                return 0;
            }
            else {
                return max_width + 5;
            }
        }

        public void setGraph(Graphics g, Line line) {
            g.setColor(line.color);
            u.deriveFont(g, 0, 11);
        }

        public void calc(Graphics g, Lines lines) {
            if (!param.bAffLegend) {
                return;
            }

            width = new int[lines.line.length];

            for (int i = 0; i < lines.line.length; i++) {
                if (lines.line[i].name != null) {
                    setGraph(g, lines.line[i]);

                    Rectangle rectangle = u.calcTextBounds(g, lines.line[i].name);
                    width[i] = rectangle.width;

                    if ((width[i] + 2) > max_width) {
                        max_width = width[i] + 2;
                    }
                }
            }
        }

        private void draw(Graphics g, Lines lines, Rectangle rectangle) {
            if (!param.bAffLegend) {
                return;
            }

            int i = rectangle.y + 11;
            int j = rectangle.x + rectangle.width + 5;

            g.drawRect(j + graph.margext, rectangle.y + graph.margext,
                    max_width + 4 * graph.margext, 11 * lines.line.length + graph.margext);

            for (int k = 0; k < lines.line.length; k++) {
                if (lines.line[k].name != null) {
                    setGraph(g, lines.line[k]);
                    g.fillRect(j + 2 * graph.margext, i, graph.margext, graph.margext);
                    g.drawString(lines.line[k].name, j + 4 * graph.margext, i + graph.margext);
                    i += 11;
                }
            }
        }
    }

    /**
     * Tooltip
     */
    private class Tooltip {
        private Object[] liste;
        private Point pt;

        public Tooltip(Point point) {
            liste = null;
            pt = point;
        }

        public void add(String s, Color color) {
            Object[] aobj = new Object[2];
            aobj[0] = s;
            aobj[1] = color.darker();
            liste = u.arrayAddObject(liste, ((Object) (aobj)));
        }

        public boolean equals(Tooltip tooltip1) {
            if ((pt.x != tooltip1.pt.x) || (pt.y != tooltip1.pt.y)||(liste == null)||(liste.length != tooltip1.liste.length)) {
                return false;
            }
            for (int i = 0; i < liste.length; i++) {
                Object[] aobj = (Object[]) liste[i];
                Object[] aobj1 = (Object[]) tooltip1.liste[i];
                String s = (String) aobj[0];
                String s1 = (String) aobj1[0];

                if (!s.equals(s1)) {
                    return false;
                }
            }

            return true;
        }

        public void draw(Graphics g) {
            if (liste == null) {
                return;
            }

            u.deriveFont(g, 0, 10);

            int i = pt.y;
            int j = pt.x + 10;

            for (int k = 0; k < liste.length; k++) {
                Object[] aobj = (Object[]) liste[k];
                String s = (String) aobj[0];
                Rectangle rectangle = u.calcTextBounds(g, s);
                rectangle.setLocation(j, i + rectangle.y);
                g.setColor(new Color(240, 240, 240));
                g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                g.setColor((Color) aobj[1]);
                g.drawString(s, j, i);
                i -= rectangle.height;
            }
        }
    }

    /**
     * Graph
     */
    private class Graph {
        private Title title=new Title();
        private Lines lines =new Lines();
        private YLabels ylabels =new YLabels();
        private XLabels xlabels =new XLabels();
        private Legend legend=new Legend();
        protected int margext= 6;
        private boolean bCalculated= false;
        protected Rectangle rGraph;
        protected Dimension totalDim;

        public Graph() {}

        public void calc(Graphics g) {
            Font font = g.getFont();
          	title.calc(g);
            xlabels.calc(g);

            int i = getSize().height - title.getHeight() - xlabels.getHeight() - (margext * 2) - 20;
            if (i < 40) {
                i = 40;
            }
            int j = i + title.getHeight() + xlabels.getHeight() + (margext * 2);
            ylabels.calc(g, lines.min_value, lines.max_value, i);
            legend.calc(g, lines);

            int k = ylabels.getWidth() + lines.getWidth() + legend.getWidth() + (margext * 2) + 30;
            rGraph = new Rectangle(ylabels.getWidth(), title.getHeight(), lines.getWidth(), i);
            totalDim = new Dimension(k, j);
            g.setFont(font);
            bCalculated = true;
            scrollPane.doLayout();
        }

        public void draw(Graphics g) {
            if (!bCalculated) {
                calc(g);
            }

            Font font = g.getFont();
            title.draw(g, rGraph);
            xlabels.draw(g, rGraph, lines.x_width);
            ylabels.draw(g, lines, rGraph);
            legend.draw(g, lines, rGraph);
            lines.draw(g, rGraph);
            draw_frame(g, rGraph);
            g.setFont(font);
        }
    }

    /**
     * Util
     */
    private class Util {
        Util() {
        }

        public Rectangle calcTextBounds(Graphics g, String s) {
            FontMetrics fontmetrics = g.getFontMetrics();
            return new Rectangle(0, -fontmetrics.getMaxAscent(), fontmetrics.stringWidth(s),
                    fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent());
        }

        public void deriveFont(Graphics g, int i, int j) {
            g.setFont(new Font(g.getFont().getName(), i, j));
        }

        public Object[] arrayAddObject(Object[] aobj, Object obj) {
            Object[] aobj1;

            if (aobj == null) {
                aobj1 = new Object[1];
            }
            else {
                aobj1 = new Object[aobj.length + 1];
                for (int i = 0; i < aobj.length; i++) {
                    aobj1[i] = aobj[i];
                }
            }
            aobj1[aobj1.length - 1] = obj;

            return aobj1;
        }
    }
}
