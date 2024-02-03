package com.annuityfarm.annuityfarmapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.annuityfarm.annuityfarmapp.databinding.ActivityGraphViewLayoutBinding
import com.annuityfarm.annuityfarmapp.databinding.ActivityMainBinding
import com.annuityfarm.annuityfarmapp.ext.getShared
import com.annuityfarm.annuityfarmapp.ext.goToActivity
import com.annuityfarm.annuityfarmapp.ext.removeCurrencyFormat
import com.annuityfarm.annuityfarmapp.fragments.PensionProjectionCalculatorFragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.StringReader
import java.lang.reflect.Type

class GraphViewActivity : AppCompatActivity() ,
    OnChartGestureListener,
    OnChartValueSelectedListener {

    lateinit var binding: ActivityGraphViewLayoutBinding
    var projectionCalculatorList: List<PensionProjectionCalculatorFragment.ProjectCalculator>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphViewLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val projectionCalculator: MutableList<*> = getShared(Constants.PROJECTION_CALCULATOR,mutableListOf<PensionProjectionCalculatorFragment.ProjectCalculator>()::class.java )


        val gson = Gson()
        // https://stackoverflow.com/questions/11484353/gson-throws-malformedjsonexception
        val reader = JsonReader(StringReader(projectionCalculator.toString().trim()))
        reader.setLenient(true)
        // Additionally we need to se the Type then only it accepts List<Employee> which we sent here empTypeList
        // Additionally we need to se the Type then only it accepts List<Employee> which we sent here empTypeList
        val empTypeList: Type = object : TypeToken<ArrayList<PensionProjectionCalculatorFragment.ProjectCalculator?>?>() {}.type
        projectionCalculatorList = gson.fromJson(projectionCalculator.toString().trim(), empTypeList)

        initChart()
    }

    /////////////////////////////////////////////////////////GRAPH FOR BALANCE AND MY INVESTMENTS////////////////////////////////////////
    fun generateActualDataLine(): LineData {
        val values1: ArrayList<Entry> = ArrayList()
        val projectCalcualtor: PensionProjectionCalculatorFragment.ProjectCalculator? = projectionCalculatorList?.lastOrNull()

        if(projectCalcualtor != null)
//        for (i in 0..projectCalcualtor.lableForValueAfterXYears?.toInt()!!) {
//            values1.add(
//                Entry(
//                    i.toFloat(),
//                    ((Math.random() * 65).toInt() + 40).toFloat()
//                )
//            )
//        }

            projectionCalculatorList?.forEachIndexed { index, projectCalculator ->
                if(index != 0){
                    projectCalculator.amountYear?.removeCurrencyFormat()?.toFloat()?.let {
                        Entry(
                            it,
                            projectCalculator.amountBalance?.removeCurrencyFormat()?.toFloat()!!
                        )
                    }?.let {
                        values1.add(
                            it
                        )
                    }
                }
            }

        val d1 = LineDataSet(values1, "Balance")
        d1.lineWidth = 2.5f
        d1.circleRadius = 2.5f
        d1.highLightColor = Color.rgb(244, 117, 117)
        //d1.color = R.color.violet_200
        //d1.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0])
        d1.setDrawValues(false)


        val values2: ArrayList<Entry> = ArrayList()

        if(projectCalcualtor != null)
//        for (i in 0..projectCalcualtor.lableForValueAfterXYears?.toInt()!!) {
//            values2.add(Entry(i.toFloat(), values1[i].y - 30))
//        }
            projectionCalculatorList?.forEachIndexed { index, projectCalculator ->
                if(index != 0){
                    projectCalculator.amountYear?.removeCurrencyFormat()?.toFloat()?.let {
                        Entry(
                            it,
                            projectCalculator.amountCumulativeContribution?.removeCurrencyFormat()?.toFloat()!!
                        )
                    }?.let {
                        values2.add(
                            it
                        )
                    }
                }
            }
        val d2 = LineDataSet(values2, "My Investment")
        d2.lineWidth = 2.5f
        d2.circleRadius = 2.5f
        d2.highLightColor = Color.rgb(244, 117, 117)
//        d2.color = R.color.colorSuccess
//        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0])
        d2.color = ColorTemplate.VORDIPLOM_COLORS[0]
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0])
        d2.setDrawValues(false)
        val sets: ArrayList<ILineDataSet> = ArrayList()
        sets.add(d1)
        sets.add(d2)


        for ( iSet: ILineDataSet in sets) {
            val set : LineDataSet =  iSet as LineDataSet
            set.setDrawValues(!set.isDrawValuesEnabled)
        }

        binding.chart.setPinchZoom(true)
        binding.chart.isAutoScaleMinMaxEnabled = true
        binding.chart.notifyDataSetChanged()
        binding.chart.invalidate()

        return LineData(sets)
    }


    private fun  initChart(){

        binding.chart.description.isEnabled = false
        binding.chart.setDrawGridBackground(false)

        val xAxis: XAxis = binding.chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)

        binding.chart.axisRight.isEnabled = false


        val leftAxis: YAxis = binding.chart.axisLeft
        leftAxis.setLabelCount(5, false)
        leftAxis.setDrawGridLines(true)
        leftAxis.setDrawAxisLine(true)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        binding.chart.data = generateActualDataLine()

        binding.chart.animateXY(2000, 2000)


        //line 2
        binding.chart.setOnChartValueSelectedListener(this)

        // enable scaling and dragging
        binding.chart.isDragEnabled = true
        binding.chart.setScaleEnabled(true)

    }


    override fun onChartGestureStart(me: MotionEvent, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
        Log.i("Gesture", "START, x: " + me.x + ", y: " + me.y)
    }

    override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture) {
        Log.i("Gesture", "END, lastGesture: $lastPerformedGesture")

        // un-highlight values after the gesture is finished and no single-tap
        // if (lastPerformedGesture != ChartGesture.SINGLE_TAP) chart.highlightValues(null) // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    override fun onChartLongPressed(me: MotionEvent?) {
        Log.i("LongPress", "Chart long pressed.")
    }

    override fun onChartDoubleTapped(me: MotionEvent?) {
        Log.i("DoubleTap", "Chart double-tapped.")
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
        Log.i("SingleTap", "Chart single-tapped.")
    }

    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) {
        Log.i("Fling", "Chart fling. VelocityX: $velocityX, VelocityY: $velocityY")
    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
        Log.i("Scale / Zoom", "ScaleX: $scaleX, ScaleY: $scaleY")
    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
        Log.i("Translate / Move", "dX: $dX, dY: $dY")
    }


    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {}
}