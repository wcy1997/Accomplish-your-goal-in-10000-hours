package com.example.zl.ihour;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zl on 2016/11/23.
 */
public class ChartService {
    private GraphicalView mGraphicalView;
    private XYMultipleSeriesDataset dataset;// 数据集容器
    private XYMultipleSeriesRenderer multipleSeriesRenderer;// 渲染器容器

    private Context context;
    private XYSeries mSeries;// 单条曲线数据集

    SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");

    public ChartService(Context context) {
        this.context = context;
    }

    /**
     * 获取图表
     *
     * @return
     */
    public GraphicalView getGraphicalView(XYMultipleSeriesDataset dataset2) {
        mGraphicalView = ChartFactory.getBarChartView(context,
                dataset2, multipleSeriesRenderer, BarChart.Type.HEAPED);
        mGraphicalView.repaint();
        return mGraphicalView;
    }

    public GraphicalView getGraphicalView() {
        mGraphicalView = ChartFactory.getBarChartView(context,
                dataset, multipleSeriesRenderer, BarChart.Type.DEFAULT);
        return mGraphicalView;
    }

    /**
     * 获取数据集，及xy坐标的集合
     *
     * @param
     */
    public XYMultipleSeriesDataset setXYMultipleSeriesDataset(String[] titles,
                                                              ArrayList<Double> yValues) {
        dataset = new XYMultipleSeriesDataset();/* 创建图表数据集 */
        CategorySeries series = new CategorySeries(titles[0]);/* 创建一个 CategorySeries对象, 单个柱状图数据 */
        int seriesLength = yValues.size();
        for (int k = 0; k < seriesLength; k++) {
            series.add(yValues.get(k));                     /* 将具体的值设置给 CategorySeries对象, 单个柱状图数据 */
        }
        dataset.addSeries(series.toXYSeries());
        return dataset;

    }

    public void setXYMultipleSeriesDataset(String curveTitle) {
        dataset = new XYMultipleSeriesDataset();
        mSeries = new XYSeries(curveTitle);
        dataset.addSeries(mSeries);
    }

    protected void buildBarRenderer(int[] colors) {

        multipleSeriesRenderer = new XYMultipleSeriesRenderer(); /* 创建图表渲染器 */

        multipleSeriesRenderer.setMarginsColor(Color.WHITE);
        multipleSeriesRenderer.setGridColor(Color.BLACK);
        multipleSeriesRenderer.setYAxisMax(24);
        multipleSeriesRenderer.setYAxisMin(0);
        multipleSeriesRenderer.setXAxisMin(0);
        multipleSeriesRenderer.setXAxisMax(8);
        multipleSeriesRenderer.setYLabels(12);
        multipleSeriesRenderer.setBarSpacing(0.5);
        multipleSeriesRenderer.setZoomEnabled(false,false);
        multipleSeriesRenderer.setPanEnabled(true,false);
        multipleSeriesRenderer.setShowGrid(true);
        multipleSeriesRenderer.setXLabels(7);//设置X轴显示的刻度标签的个数
        multipleSeriesRenderer.setFitLegend(true);
        multipleSeriesRenderer.setAxisTitleTextSize(16);                                  /* 设置坐标轴标题字体大小 */
        multipleSeriesRenderer.setChartTitleTextSize(20);                                 /* 设置图表标题字体大小 */
        multipleSeriesRenderer.setLabelsTextSize(15);                                     /* 设置标签字体大小 */
        multipleSeriesRenderer.setLegendTextSize(15);                                     /* 设置说明文字字体大小 */
        int length = colors.length;                                          /* 获取图表中柱状图个数 */
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer r = new XYSeriesRenderer();              /* 单个柱状图渲染器 */
            r.setColor(colors[i]);                                            /* 为单个柱状图渲染器设置颜色 */
            multipleSeriesRenderer.addSeriesRenderer(r);                                    /* 将单个柱状图渲染器设置给图表渲染器 */
        }
    }




    /**
     * 坐标轴(渲染器) : 对曲线图渲染器进行配置, 主要配置坐标轴
     * @param title 图表的名称
     * @param xTitle x轴坐标的名称
     * @param yTitle y轴坐标的名称
     * @param xMin x轴最小值
     * @param xMax x轴最大值
     * @param yMin y轴最小值
     * @param yMax y轴最大值
     * @param axesColor 坐标轴的颜色
     * @param labelsColor 标签的颜色
     */
    protected void setChartSettings( String title, String xTitle,
                                     String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                     int labelsColor) {
        multipleSeriesRenderer.setChartTitle(title); /* 设置曲线图标题 */
        multipleSeriesRenderer.setXTitle(xTitle); /* 设置x轴标题 */
        multipleSeriesRenderer.setYTitle(yTitle); /* 设置y轴标题 */
        multipleSeriesRenderer.setXAxisMin(xMin); /* 设置x轴最小值 */
        multipleSeriesRenderer.setXAxisMax(xMax); /* 设置x轴最大值 */
        multipleSeriesRenderer.setYAxisMin(yMin); /* 设置y轴最小值 */
        multipleSeriesRenderer.setYAxisMax(yMax); /* 设置y轴最大值 */
        multipleSeriesRenderer.setAxesColor(axesColor); /* 设置坐标轴颜色 */
        multipleSeriesRenderer.setLabelsColor(labelsColor); /* 设置标签颜色 */
    }

    /**
     * 根据新加的数据，更新曲线，只能运行在主线程
     *
     */
//    public void updateChart(Date x, double y) {
//        mSeries.add(x, y);
//        mGraphicalView.repaint();//此处也可以调用invalidate()
//    }

    public void updateChart() {
        mGraphicalView.repaint();;//此处也可以调用invalidate()
    }
    /**
     * 添加新的数据，多组，更新曲线，只能运行在主线程
     * @param xList
     * @param yList
     */
//    public void updateChart(List<Double> xList, List<Double> yList) {
//        for (int i = 0; i < xList.size(); i++) {
//            mSeries.add(xList.get(i), yList.get(i));
//        }
//        mGraphicalView.repaint();//此处也可以调用invalidate()
//    }
}
