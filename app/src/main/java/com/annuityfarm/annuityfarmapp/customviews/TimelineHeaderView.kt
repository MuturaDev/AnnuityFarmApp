package com.annuityfarm.annuityfarmapp.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.annuityfarm.annuityfarmapp.R


class TimelineHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {
    var btnNext:Button? = null

    init {



        val view = LayoutInflater.from(context).inflate(R.layout.step_root, this, true)
        orientation = VERTICAL

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TimelineHeaderView_Attr, 0, 0)
            val stepName = resources.getText(typedArray

                .getResourceId(R.styleable.TimelineHeaderView_Attr_StepName, R.string.step_name))
            val stepTitle = resources.getText(typedArray
                .getResourceId(R.styleable.TimelineHeaderView_Attr_StepTitle, R.string.step_title))

            view.findViewById<TextView>(R.id.stepName).text = stepName
            view.findViewById<TextView>(R.id.stepTitle).text = stepTitle

            val contentContainer = resources.getLayout(typedArray.getResourceId(R.styleable.TimelineHeaderView_Attr_contentContainer, R.layout.step1_layout))

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(contentContainer, null)
            view.findViewById<LinearLayout>(R.id.contentContainer).apply {
                addView(rowView, this!!.childCount - 1)
            }

            btnNext = view.findViewById(R.id.btnNext)

            typedArray.recycle()
        }
    }

    fun getNext(): Button? {
        return btnNext
    }

}