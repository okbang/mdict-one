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
 
 /*
 * Created on Nov 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.fms1.chart;

import java.text.DecimalFormat;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import de.laures.cewolf.ChartPostProcessor;

/**
 * @author VuongNT
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LinesChartLabels implements ChartPostProcessor {

	/* (non-Javadoc)
	 * @see de.laures.cewolf.ChartPostProcessor#processChart(java.lang.Object, java.util.Map)
	 */
	public void processChart(Object chart, Map param) {
		// Auto-generated method stub
		CategoryPlot plot = (CategoryPlot) ((JFreeChart) chart).getPlot();
		plot.getDomainAxis().setLowerMargin(0.0);
		plot.getDomainAxis().setCategoryMargin(0.0);
		plot.getRangeAxis().setLowerMargin(0.1);
		plot.getRangeAxis().setUpperMargin(0.2);
		LineAndShapeRenderer renderer =(LineAndShapeRenderer)plot.getRenderer();
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0.00"));
		renderer.setItemLabelGenerator(generator);
		if (plot.getCategories().size()<20){
			renderer.setBaseItemLabelsVisible(true);
		}

		if (plot.getCategories().size()>15){
			plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_90);
		}

		renderer.setShapesFilled(true);
		renderer.setShapesVisible(true);
		renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
	}
}
