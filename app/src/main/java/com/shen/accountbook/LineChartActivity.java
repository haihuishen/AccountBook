package com.shen.accountbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("shen","是空的1");
        setContentView(R.layout.activity_line_chart);

        // 从字面上看savedInstanceState，是保存实例状态的。实际上，savedInstanceState也就是保存Activity的状态的。
        // activity 结束时，就会保存
//        if (savedInstanceState == null) {
//            // 拿到fragment管理器 , 将"PlaceholderFragment" new出来，提交给"R.id.container"
//            Log.d("shen","是空的");
//            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
//        }
        Log.d("shen","是空的2");
        //getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
    }

    /**
     * A fragment containing a line chart.
     */
    public static class PlaceholderFragment extends Fragment {

        private LineChartView chart;
        private LineChartData data;
        /** 添加了第几条线(用于在数组中找到画哪一条线)*/
        private int numberOfLines = 1;
        /** 最多4条线*/
        private int maxNumberOfLines = 4;
        /** 每一条线12个点*/
        private int numberOfPoints = 12;

        /** 这个应该是放置随机数的!*/
        float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

        //Axes ['æksiːz]  轴线;轴心;坐标轴
        //Shape  [ʃeɪp] 形状
        //Point [pɒɪnt] 点
        //Cubic ['kjuːbɪk] 立方体;立方的
        //Label ['leɪb(ə)l] 标签

        /** 显示/隐藏"坐标轴" true:显示坐标轴/false:隐藏坐标轴*/
        private boolean hasAxes = true;
        /** 显示/隐藏"坐标轴名称" true:显示/false:隐藏*/
        private boolean hasAxesNames = true;
        /** true:连线  false：不连线(只有点)*/
        private boolean hasLines = true;
        /** true:显示"节点(峰)"  false:去掉"节点(峰)"--就不可点击了*/
        private boolean hasPoints = true;
        private ValueShape shape = ValueShape.CIRCLE;
        /** 区域的填充颜色(图形和坐标轴包围的区域) false:不填充/true:填充*/
        private boolean isFilled = false;
        /** 在"节点(峰点)"中显示"文本(峰点值)" false:不显示/true:显示*/
        private boolean hasLabels = false;
        /** false:折线/true:滑线*/
        private boolean isCubic = false;
        /** 点击"节点(峰点)"时，是否有"节点标签" true:点击时显示"峰点标签值"/ false:点击时不显示"峰点标签值"*/
        private boolean hasLabelForSelected = false;
        /** 让"节点(峰点)"变色--变一种*/
        private boolean pointsHaveDifferentColor;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //hasMenu——如果这是真的,显示fragment菜单项
            setHasOptionsMenu(true);
            //第一个参数传入布局的资源ID，生成fragment视图，
            // 第二个参数是视图的父视图，通常我们需要父视图来正确配置组件。
            // 第三个参数告知布局生成器是否将生成的视图添加给父视图。
            View rootView = inflater.inflate(R.layout.fragment_line_chart_month, container, false);

            chart = (LineChartView) rootView.findViewById(R.id.chart);
            // 这个应该是"点击峰点"
            // 实现一下我们实例的"接口中的某个方法"(内容根据我们需要的写)
            chart.setOnValueTouchListener(new ValueTouchListener());

            // Generate some random values. --生成一些随机值
            generateValues();

            // 根据随机的值，画点，处理生成"图表"--全局变量：chart
            generateData();

            // Disable viewport recalculations, see toggleCubic() method for more info.
            // 禁用窗口重新计算,看到toggleCubic更多信息()方法。
            chart.setViewportCalculationEnabled(false);

            resetViewport();

            return rootView;
        }

        // MENU -- 标题栏：将R.menu.line_chart 填充成  目录
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.line_chart, menu);
        }

        // 使用的最多方法是重写activity类的 onOptionsItemSelected(MenuItem)回调方法，
        // 每当有菜单项被点击时，android就会调用该方法，并传入被点击菜单项。
        //         //对没有处理的事件，交给父类来处理
        //          return super.onOptionsItemSelected(item);
        //     //返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
        //          return true;
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_reset) {                  // 重置：回到初始状态
                reset();
                generateData();
                return true;
            }
            if (id == R.id.action_add_line) {               // 添加"折线"：最多4条
                addLineToData();
                return true;
            }
            if (id == R.id.action_toggle_lines) {           // 连线/不连线
                toggleLines();
                return true;
            }
            if (id == R.id.action_toggle_points) {          // 显示"节点(峰)"/去掉"节点(峰)"--就不可点击了
                togglePoints();
                return true;
            }
            if (id == R.id.action_toggle_cubic) {           // 折线/滑线
                toggleCubic();
                return true;
            }
            if (id == R.id.action_toggle_area) {            // 区域的填充颜色(图形和坐标轴包围的区域)
                toggleFilled();
                return true;
            }
            if (id == R.id.action_point_color) {            // 让"节点(峰点)"变色--变一种
                togglePointColor();
                return true;
            }
            if (id == R.id.action_shape_circles) {         // 将"节点(峰点)"变成"圆形"
                setCircles();
                return true;
            }
            if (id == R.id.action_shape_square) {         // 将"节点(峰点)"变成"正方形"
                setSquares();
                return true;
            }
            if (id == R.id.action_shape_diamond) {         // 将"节点(峰点)"变成"菱形"
                setDiamonds();
                return true;
            }
            if (id == R.id.action_toggle_labels) {          // 在"节点(峰点)"中显示"文本(峰点值)"
                toggleLabels();
                return true;
            }
            if (id == R.id.action_toggle_axes) {            // 显示/隐藏"坐标轴"
                toggleAxes();
                return true;
            }
            if (id == R.id.action_toggle_axes_names) {      // 显示/隐藏"坐标轴名称"
                toggleAxesNames();
                return true;
            }
            if (id == R.id.action_animate) {                // 切换"曲线图"时，使用动画效果
                prepareDataAnimation();
                // 开始动画
                chart.startDataAnimation();
                return true;
            }
            if (id == R.id.action_toggle_selection_mode) {      // 点击"节点(峰点)"时，是否有"节点标签"
                toggleLabelForSelected();

                Toast.makeText(getActivity(),
                        "Selection mode set to " + chart.isValueSelectionEnabled() + " select any point.",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.action_toggle_touch_zoom) {          // 是否允许，触摸放大
                chart.setZoomEnabled(!chart.isZoomEnabled());
                Toast.makeText(getActivity(), "IsZoomEnabled " + chart.isZoomEnabled(), Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.action_zoom_both) {                  // 触摸放大，x和y都可以/都不可以
                chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
                return true;
            }
            if (id == R.id.action_zoom_horizontal) {            // 触摸放大，x可以/不可以
                chart.setZoomType(ZoomType.HORIZONTAL);
                return true;
            }
            if (id == R.id.action_zoom_vertical) {               // 触摸放大，y可以/不可以
                chart.setZoomType(ZoomType.VERTICAL);
                return true;
            }
            //对没有处理的事件，交给父类来处理
            return super.onOptionsItemSelected(item);
        }

        /** 生成一些随机值(用于画点的;y轴坐标;x轴默认递增) <br>
         * 存放到 ： randomNumbersTab[i][j] <br>
         *  4 条 折线              maxNumberOfLines <br>
         *  每条折线是12个点       numberOfPoints <br>
         */
        private void generateValues() {
            for (int i = 0; i < maxNumberOfLines; ++i) {
                for (int j = 0; j < numberOfPoints; ++j) {
                    randomNumbersTab[i][j] = (float) Math.random() * 100f;
                }
            }
        }

        /** 重置*/
        private void reset() {
            numberOfLines = 1;

            hasAxes = true;
            hasAxesNames = true;
            hasLines = true;
            hasPoints = true;
            shape = ValueShape.CIRCLE;
            isFilled = false;
            hasLabels = false;
            isCubic = false;
            hasLabelForSelected = false;
            pointsHaveDifferentColor = false;

            chart.setValueSelectionEnabled(hasLabelForSelected);
            resetViewport();
        }

        /** 重置窗口高度范围(0,100) */
        private void resetViewport() {
            // Reset viewport height range to (0,100)
            // 重置窗口高度范围(0,100)
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = 0;
            v.top = 100;
            v.left = 0;
            v.right = numberOfPoints - 1;
            chart.setMaximumViewport(v);
            chart.setCurrentViewport(v);
        }

        /**
         * 根据随机的值，画点，处理生成"图表"--全局变量：chart
         */
        private void generateData() {

            List<Line> lines = new ArrayList<Line>();
            for (int i = 0; i < numberOfLines; ++i) {

                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j < numberOfPoints; ++j) {
                    values.add(new PointValue(j, randomNumbersTab[i][j]));
                }

                Line line = new Line(values);
                line.setColor(ChartUtils.COLORS[i]);
                line.setShape(shape);
                line.setCubic(isCubic);
                line.setFilled(isFilled);
                line.setHasLabels(hasLabels);
                line.setHasLabelsOnlyForSelected(hasLabelForSelected);
                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                if (pointsHaveDifferentColor){
                    line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                }
                lines.add(line);
            }

            data = new LineChartData(lines);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Axis X");
                    axisY.setName("Axis Y");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            data.setBaseValue(Float.NEGATIVE_INFINITY);
            chart.setLineChartData(data);

        }

        /**
         * Adds lines to data, after that data should be set again with<br>
         * 将行添加到数据,这些数据应该设置后再用<br>
         * {@link LineChartView#setLineChartData(LineChartData)}. Last 4th line has non-monotonically x values.<br>
         */
        private void addLineToData() {
            if (data.getLines().size() >= maxNumberOfLines) {
                Toast.makeText(getActivity(), "Samples app uses max 4 lines!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                ++numberOfLines;
            }

            generateData();
        }

        /** 连线或不连线*/
        private void toggleLines() {
            hasLines = !hasLines;

            generateData();
        }

        /** 显示"节点(峰)"/去掉"节点(峰)"--就不可点击了*/
        private void togglePoints() {
            hasPoints = !hasPoints;

            generateData();
        }

        /** 折线/滑线*/
        private void toggleCubic() {
            isCubic = !isCubic;

            generateData();

            if (isCubic) {
                // It is good idea to manually set a little higher max viewport for cubic lines because sometimes line
                // go above or below max/min. To do that use Viewport.inest() method and pass negative value as dy
                // parameter or just set top and bottom values manually.
                // In this example I know that Y values are within (0,100) range so I set viewport height range manually
                // to (-5, 105).
                // To make this works during animations you should use Chart.setViewportCalculationEnabled(false) before
                // modifying viewport.
                // Remember to set viewport after you call setLineChartData().
                final Viewport v = new Viewport(chart.getMaximumViewport());
                v.bottom = -5;
                v.top = 105;
                // You have to set max and current viewports separately.
                chart.setMaximumViewport(v);
                // I changing current viewport with animation in this case.
                chart.setCurrentViewportWithAnimation(v);
            } else {
                // If not cubic restore viewport to (0,100) range.
                final Viewport v = new Viewport(chart.getMaximumViewport());
                v.bottom = 0;
                v.top = 100;

                // You have to set max and current viewports separately.
                // In this case, if I want animation I have to set current viewport first and use animation listener.
                // Max viewport will be set in onAnimationFinished method.
                chart.setViewportAnimationListener(new ChartAnimationListener() {

                    @Override
                    public void onAnimationStarted() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationFinished() {
                        // Set max viewpirt and remove listener.
                        chart.setMaximumViewport(v);
                        chart.setViewportAnimationListener(null);

                    }
                });
                // Set current viewpirt with animation;
                chart.setCurrentViewportWithAnimation(v);
            }

        }

        /** 区域的填充颜色(图形和坐标轴包围的区域)*/
        private void toggleFilled() {
            isFilled = !isFilled;

            generateData();
        }

        /** 让"节点(峰点)"变色--变一种*/
        private void togglePointColor() {
            pointsHaveDifferentColor = !pointsHaveDifferentColor;

            generateData();
        }

        /** 将"节点(峰点)"变成"圆形"*/
        private void setCircles() {
            shape = ValueShape.CIRCLE;

            generateData();
        }

        /** 将"节点(峰点)"变成"方形"*/
        private void setSquares() {
            shape = ValueShape.SQUARE;

            generateData();
        }

        /** 将"节点(峰点)"变成"菱形"*/
        private void setDiamonds() {
            shape = ValueShape.DIAMOND;

            generateData();
        }

        /** 在"节点(峰点)"中显示"文本(峰点值)"*/
        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }

            generateData();
        }

        /** 点击"节点(峰点)"时，是否有"节点标签"*/
        private void toggleLabelForSelected() {
            hasLabelForSelected = !hasLabelForSelected;

            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelForSelected) {
                hasLabels = false;
            }

            generateData();
        }

        /** 显示/隐藏"坐标轴"*/
        private void toggleAxes() {
            hasAxes = !hasAxes;

            generateData();
        }

        /** 显示/隐藏"坐标轴名称"*/
        private void toggleAxesNames() {
            hasAxesNames = !hasAxesNames;

            generateData();
        }

        /**
         * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
         * method(don't confuse with View.animate()). If you operate on data that was set before you don't have to call
         * {@link LineChartView#setLineChartData(LineChartData)} again.<br>
         *
         *  切换"曲线图"
         */
        private void prepareDataAnimation() {
            for (Line line : data.getLines()) {
                for (PointValue value : line.getValues()) {
                    // Here I modify target only for Y values but it is OK to modify X targets as well.
                    value.setTarget(value.getX(), (float) Math.random() * 100);
                }
            }
        }

        /**
         * 点击"峰点"后
         */
        private class ValueTouchListener implements LineChartOnValueSelectListener {

            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }
    }
}
