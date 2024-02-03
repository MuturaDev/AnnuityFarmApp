package com.annuityfarm.annuityfarmapp.bottomsheet

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.annuityfarm.annuityfarmapp.*
import com.annuityfarm.annuityfarmapp.databinding.FragmentDetailedCalculatorResultBottomSheetDialogLayoutBinding
import com.annuityfarm.annuityfarmapp.ext.*
import com.annuityfarm.annuityfarmapp.fragments.PensionProjectionCalculatorFragment
import com.annuityfarm.annuityfarmapp.models.BottomSheetItem
import com.annuityfarm.annuityfarmapp.models.FragMessage
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import ir.androidexception.datatable.model.DataTableHeader
import ir.androidexception.datatable.model.DataTableRow
import java.io.StringReader
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


// TODO: Customize parameter argument names
//const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    BottomSheetDetailedCalculatorResultsDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class BottomSheetDetailedCalculatorResultsDialogFragment(val fragment: Fragment?, fragMessage: FragMessage?) : BottomSheetDialogFragment(),
    OnChartGestureListener,
    OnChartValueSelectedListener {

    private var _binding: FragmentDetailedCalculatorResultBottomSheetDialogLayoutBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedCalculatorResultBottomSheetDialogLayoutBinding.inflate(inflater, container, false)
        requireActivity().window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //setStyle(STYLE_NORMAL, R.style.bO)
        return binding.root
    }


    var projectionCalculatorList: List<PensionProjectionCalculatorFragment.ProjectCalculator>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       // val recyclerView: RecyclerView? = _binding?.list

        _binding?.btnCancel?.setOnClickListener {
            this@BottomSheetDetailedCalculatorResultsDialogFragment.dismiss()
        }

        val dt = _binding?.dt
        //not working
       val tf = Typeface.createFromAsset(fragment?.requireActivity()?.resources?.assets, "poppins_regular.ttf")
        val header = DataTableHeader.Builder()
       .item("Year",2)
       .item("Rate",2)
       .item("Interest",3)
       .item("Scheduled Deposit",5)
       .item("Extra Annual Deposit",5)
       .item("Balance",4)
       .item("Cumulative Contribution",5)
       .item("Cumulative Interest",5)
            .build()
        val rows: ArrayList<DataTableRow> = ArrayList()

        val projectionCalculator: MutableList<*> = getShared(Constants.PROJECTION_CALCULATOR,mutableListOf<PensionProjectionCalculatorFragment.ProjectCalculator>()::class.java )


        val gson = Gson()
       // https://stackoverflow.com/questions/11484353/gson-throws-malformedjsonexception
        val reader = JsonReader(StringReader(projectionCalculator.toString().trim()))
        reader.setLenient(true)
        // Additionally we need to se the Type then only it accepts List<Employee> which we sent here empTypeList
        // Additionally we need to se the Type then only it accepts List<Employee> which we sent here empTypeList
        val empTypeList: Type = object : TypeToken<ArrayList<PensionProjectionCalculatorFragment.ProjectCalculator?>?>() {}.type
        projectionCalculatorList = gson.fromJson(projectionCalculator.toString().trim(), empTypeList)
//        val numberOfElementInJson = emp.size
//        println("Total JSON Elements$numberOfElementInJson")
//        for (e in emp) {
//            System.out.println(e.getName())
//            System.out.println(e.getEmpId())
//        }



        projectionCalculatorList?.forEachIndexed { id, element ->
            element.apply {
                val row = DataTableRow.Builder()
                    .value(amountYear)
                    .value(amountRate)
                    .value(fragment?.let { amountInterest?.convertToCurrencyWithDeciaml(it).toString() })
                    .value(fragment?.let { amountScheduledDeposit?.convertToCurrencyWithDeciaml(it).toString() })
                    .value(fragment?.let { amountExtraAnnualDeposit?.convertToCurrencyWithDeciaml(it).toString() })
                    .value(fragment?.let { amountBalance?.convertToCurrencyWithDeciaml(it).toString() })
                    .value(fragment?.let { amountCumulativeContribution?.convertToCurrencyWithDeciaml(it).toString() })
                    .value(fragment?.let { amountCumulativeInterest?.convertToCurrencyWithDeciaml(it).toString() })
                    .build()
                rows.add(row)

                if(id == 0) {
                    binding.lblValueAfterXYears.text = ("Value After $lableForValueAfterXYears Years")


                    binding.txtValueAfterXYears.text = fragment?.let {
                        valueAfterXYears?.convertToCurrencyWithDeciaml(it).toString()
                    }
                    binding.txtTotalInvested.text = fragment?.let {
                        totalInvested?.convertToCurrencyWithDeciaml(it).toString()
                    }
                    binding.txtInterestEarned.text = fragment?.let {
                        InterestEarned?.convertToCurrencyWithDeciaml(it).toString()
                    }
                }

            }

        }

//        for (projectCalculator in ) {
////            val r = Random()
////            val random: Int = r.nextInt(i + 1)
////            val randomDiscount: Int = r.nextInt(20)
//
//        }
        dt?.typeface = tf
        dt?.header = header
        dt?.rows = rows
        dt?.rowTextSize = 12f
        dt?.headerTextSize = 15f
        dt?.inflate(binding.root.context)

        var clicked = true
        binding.expandCollapse.apply {
            setOnClickListener {
                if(clicked){
                    setImageResource(R.drawable.ic_collapse)
                    binding.summaryOfResults.collapse()
                    clicked = false
                }else{
                    setImageResource(R.drawable.ic_expand)
                    binding.summaryOfResults.expand()
                    clicked = true
                }
            }
        }


        initChart()

    }



    companion object {

        // TODO: Customize parameters
        fun newInstance(
            bottomSheetItemMutableListParam: ArrayList<BottomSheetItem>?,
            fragment: Fragment,
            fragMessage: FragMessage?
        ): BottomSheetDetailedCalculatorResultsDialogFragment =
            BottomSheetDetailedCalculatorResultsDialogFragment(fragment, fragMessage).apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ITEM_COUNT, bottomSheetItemMutableListParam)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /////////////////////////////////////////////////////////GRAPH FOR BALANCE AND MY INVESTMENTS////////////////////////////////////////

    private val colors = intArrayOf(
        ColorTemplate.VORDIPLOM_COLORS[0],
        ColorTemplate.VORDIPLOM_COLORS[1],
        ColorTemplate.VORDIPLOM_COLORS[2]
    )

    fun generateActualDataLine(): LineData{
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


        for ( iSet:ILineDataSet in sets) {
             val set :LineDataSet =  iSet as LineDataSet
            set.setDrawValues(!set.isDrawValuesEnabled)
        }

        binding.chart.setPinchZoom(true)
        binding.chart.isAutoScaleMinMaxEnabled = true
        binding.chart.notifyDataSetChanged()
        binding.chart.invalidate()

        return LineData(sets)
    }


    private fun  initChart(){

        binding.expandCollapseGraph.setOnClickListener {
            requireActivity().goToActivity(requireContext(), GraphViewActivity::class.java)
        }

        //line 1
        // apply styling
        // holder.chart.setValueTypeface(mTf);
        binding.chart.description.isEnabled = false
        binding.chart.setDrawGridBackground(false)

        val xAxis: XAxis = binding.chart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
       // xAxis.typeface = mTf
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)

        binding.chart.axisRight.isEnabled = false


        val leftAxis: YAxis = binding.chart.axisLeft
        //leftAxis.typeface = mTf
        leftAxis.setLabelCount(5, false)
        leftAxis.setDrawGridLines(true)
        leftAxis.setDrawAxisLine(true)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


//        val rightAxis: YAxis = binding.chart.axisRight
//        //rightAxis.typeface = mTf
//        rightAxis.setLabelCount(5, false)
//        rightAxis.setDrawGridLines(false)
//        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        // set data

        // set data
        binding.chart.data = generateActualDataLine()

        // do not forget to refresh the chart
        // holder.chart.invalidate();

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        binding.chart.animateXY(2000, 2000)


        //line 2
        binding.chart.setOnChartValueSelectedListener(this)

//        binding.chart.setDrawGridBackground(false)
//        binding.chart.description.isEnabled = false
//        binding.chart.setDrawBorders(false)
//
//        binding.chart.axisLeft.isEnabled = false
//        binding.chart.axisRight.setDrawAxisLine(false)
//        binding.chart.axisRight.setDrawGridLines(false)
//        binding.chart.xAxis.setDrawAxisLine(false)
//        binding.chart.xAxis.setDrawGridLines(false)
//
//        // enable touch gestures
//        binding.chart.setTouchEnabled(true)
//
        // enable scaling and dragging
        binding.chart.isDragEnabled = true
        binding.chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
//        binding.chart.setPinchZoom(false)
//
//        binding.chart.animateXY(2000, 2000);

//        val l: Legend = binding.chart.legend
//        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//        l.orientation = Legend.LegendOrientation.VERTICAL
//        l.setDrawInside(false)

//        binding.chart.resetTracking()
//
//        val dataSets: ArrayList<ILineDataSet> = ArrayList()
//
//        for (z in 0..2) {
//            val values: ArrayList<Entry> = ArrayList()
//            for (i in 0 until 15) {
//                val `val`: Double = Math.random() * 9 + 3
//                values.add(Entry(i.toFloat(), `val`.toFloat()))
//            }
//            val d = LineDataSet(values, "DataSet " + (z + 1))
//            d.lineWidth = 2.5f
//            d.circleRadius = 4f
//            val color = colors[z % colors.size]
//            d.color = color
//            d.setCircleColor(color)
//            dataSets.add(d)
//        }
//
//        // make the first DataSet dashed
//        (dataSets[0] as LineDataSet).enableDashedLine(10f, 10f, 0f)
//        (dataSets[0] as LineDataSet).setColors(*ColorTemplate.VORDIPLOM_COLORS)
//        (dataSets[0] as LineDataSet).setCircleColors(*ColorTemplate.VORDIPLOM_COLORS)
//
//        val data = LineData(dataSets)
//        binding.chart.data = data
//        binding.chart.invalidate()
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Line data
     */
    private fun generateDataLine(): LineData{
        val values1: ArrayList<Entry> = ArrayList()
        for (i in 0..11) {
            values1.add(
                Entry(
                    i.toFloat(),
                    ((Math.random() * 65).toInt() + 40).toFloat()
                )
            )
        }
        val d1 = LineDataSet(values1, "Balance")
        d1.lineWidth = 2.5f
        d1.circleRadius = 4.5f
        d1.highLightColor = Color.rgb(244, 117, 117)
        d1.setDrawValues(false)


        val values2: ArrayList<Entry> = ArrayList()
        for (i in 0..11) {
            values2.add(Entry(i.toFloat(), values1[i].y - 30))
        }
        val d2 = LineDataSet(values2, "My Investment")
        d2.lineWidth = 2.5f
        d2.circleRadius = 4.5f
        d2.highLightColor = Color.rgb(244, 117, 117)
        d2.color = ColorTemplate.VORDIPLOM_COLORS[0]
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0])
        d2.setDrawValues(false)
        val sets: ArrayList<ILineDataSet> = ArrayList()
        sets.add(d1)
        sets.add(d2)
        return LineData(sets)
    }




    override fun onChartGestureStart(me: MotionEvent, lastPerformedGesture: ChartGesture?) {
        Log.i("Gesture", "START, x: " + me.x + ", y: " + me.y)
    }

    override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartGesture) {
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
