package com.annuityfarm.annuityfarmapp.adapters;

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.annuityfarm.annuityfarmapp.R
import com.annuityfarm.annuityfarmapp.base.BaseFragment
import com.annuityfarm.annuityfarmapp.databinding.ActionItemLayoutBinding
import com.annuityfarm.annuityfarmapp.interfaces.IPensionType


open class ActionListAdapter(private val myFragment: BaseFragment, private val iPensionType: IPensionType):
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ActionItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        myFragment.actionMutablelistParam[position].let { it ->
            holder.title.apply {text = it.title}

            //dint work
//            if (it.isActive == true) it.activeImage else it.inactiveImage ?.let { image ->
//                holder.image.apply { setImageResource(image) }
//                //holder.image.setImageResource(R.drawable.pension_withdrawals_icon_white)
//            }
            //dint work
//            holder.image.apply { if (it.isActive == true) it.activeImage else it.inactiveImage?.let { it1 ->
//                setImageResource(
//                    it1
//                )
//            } }

            if (it.isActive == true)
                it.activeImage?.let { it1 -> holder.image.setImageResource(it1) }

            if (it.isActive == false)
                it.inactiveImage?.let { it1 -> holder.image.setImageResource(it1) }




            if (it.isActive == true) R.drawable.solid_violet_rounded_corner_bg else R.drawable.violet_rounded_corner_bg.let {
                holder.btnAction.apply{background =  myFragment.requireActivity().getDrawable(it)}
            }

            holder.subTitle.apply {text = it.subTitle}

            if (it.isActive == true) R.color.white else R.color.violet_200.let {
                myFragment.requireActivity().getColor(it).let {
                    holder.subTitle.apply { setTextColor(it)}
                    holder.title.apply { setTextColor(it) }
                }
            }

            holder.btnAction.setOnClickListener{
                iPensionType.onItemClicked(position)
            }
        }


    }

    override fun getItemCount(): Int {
        return myFragment.actionMutablelistParam.size
    }
}

 open class ViewHolder  constructor(binding: ActionItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val title: TextView = binding.title
    val subTitle: TextView = binding.subTitle
    val image: ImageView = binding.image
     val btnAction:LinearLayout = binding.btnAction
}