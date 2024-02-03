package com.annuityfarm.annuityfarmapp.bottomsheet

import android.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annuityfarm.annuityfarmapp.bottomsheet.*
import com.annuityfarm.annuityfarmapp.databinding.*
import com.annuityfarm.annuityfarmapp.ext.goToFragment
import com.annuityfarm.annuityfarmapp.ext.toast
import com.annuityfarm.annuityfarmapp.fragments.AnnuityFragment
import com.annuityfarm.annuityfarmapp.fragments.IncomeDrawDownFragment
import com.annuityfarm.annuityfarmapp.fragments.PensionFragment
import com.annuityfarm.annuityfarmapp.fragments.SubtypeFragment
import com.annuityfarm.annuityfarmapp.interfaces.IPensionType
import com.annuityfarm.annuityfarmapp.models.BottomSheetItem
import com.annuityfarm.annuityfarmapp.models.FragMessage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    BottomSheetItemListDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class BottomSheetItemListDialogFragment(val fragment: Fragment?, fragMessage: FragMessage?) : BottomSheetDialogFragment() {

  //override fun getTheme(): Int = R.style.
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
//    fun expendDialog(activity: FragmentActivity?, logTag: String, performOnError: () -> Unit){
//        try {
//            val bottomSheet = dialog!!.findViewById(R.id.design_bottom_sheet) as View
//            val behavior = BottomSheetBehavior.from(bottomSheet)
//            val displayMetrics = DisplayMetrics()
//            requireActivity().windowManager!!.defaultDisplay!!.getMetrics(displayMetrics)
//            behavior.peekHeight = displayMetrics.heightPixels
//        } catch (e: NullPointerException) {
//            Log.d(logTag, e.message ?: "NPE in onResume")
//            performOnError()
//        }
//    }


//    override fun onItemClicked(vararg messageParam: Any?) {
//        messageParam.forEachIndexed { index, element ->
//            when (index) {
//                0 -> (element as? Int?).let {
//                    if (it != null) {
//                        bottomSheetItemMutableListParam?.forEachIndexed { index, _ ->
//                            if(index == it){
//
//                                //(requireParentFragment() as PensionFragment).onItemClicked((requireParentFragment() as PensionFragment).fragMessage)
//                                bottomSheetItem.title?.let { it1 -> requireActivity().toast(it1) }
//                                //goToFragment(R.id.action_pension_fragment_to_subtype_fragment)
//                               // SubtypeFragment.newInstance("","");
//                                requireActivity().supportFragmentManager.beginTransaction().replace(requireActivity().findViewById(R.id.nav_host_fragment_content_main),SubtypeFragment.newInstance("",""), "fragment");
//
//                            }
//                        }
//                    }
//                }
//            }
//
//        }
//    }

    private var _binding: FragmentItemListDialogBinding? = null

    private var myFragment: Fragment?  = fragment
    private var myfragMessage: FragMessage? = fragMessage
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentItemListDialogBinding.inflate(inflater, container, false)
        requireActivity().window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //setStyle(STYLE_NORMAL, R.style.bO)
        return binding.root

    }

    var bottomSheetItemMutableListParam: ArrayList<BottomSheetItem>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView? = _binding?.list
        recyclerView?.layoutManager = LinearLayoutManager(context)



        bottomSheetItemMutableListParam = arguments?.getSerializable(ARG_ITEM_COUNT) as ArrayList<BottomSheetItem>?

        recyclerView?.adapter = bottomSheetItemMutableListParam?.let {
            BottomSheetItemAdapter(it
            )
        }
    }

    private inner class ViewHolder  constructor(binding: FragmentItemListDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

         //val text: TextView = binding.text
            val item_icon: AppCompatImageView = binding.itemIcon
            val itemTitle: TextView = binding.itemTitle
            val btn_provider:LinearLayout = binding.btnProvider;
    }

    private inner class BottomSheetItemAdapter(private val providers: java.util.ArrayList<BottomSheetItem>) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                FragmentItemListDialogItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            providers[position].let { bottomSheetItem ->
                holder.item_icon.apply { bottomSheetItem.image?.let { it1 -> setImageResource(it1) } }
                holder.itemTitle.apply { text = bottomSheetItem.title }
                holder.btn_provider.setOnClickListener {

                    val fragMessage:FragMessage = FragMessage().apply {
                        fragTitle = myfragMessage?.fragTitle
                        fragImage = myfragMessage?.fragImage
                        message = bottomSheetItem
                    }

                    if(myFragment is AnnuityFragment){
                        (myFragment as AnnuityFragment).onItemClicked(fragMessage)
                        this@BottomSheetItemListDialogFragment.dismiss()
                    }

                    if(myFragment is PensionFragment){
                        (myFragment as PensionFragment).onItemClicked(fragMessage)
                        this@BottomSheetItemListDialogFragment.dismiss()
                    }

                    if(myFragment is SubtypeFragment){
                        (myFragment as SubtypeFragment).onItemClicked(fragMessage)
                        this@BottomSheetItemListDialogFragment.dismiss()
                    }
                }
            }


        }

        override fun getItemCount(): Int {
            return providers.size
        }
    }

    companion object {

        // TODO: Customize parameters
        fun newInstance(bottomSheetItemMutableListParam: ArrayList<BottomSheetItem>, fragment: Fragment,fragMessage: FragMessage?): BottomSheetItemListDialogFragment =
            BottomSheetItemListDialogFragment(fragment,fragMessage).apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ITEM_COUNT, bottomSheetItemMutableListParam)
                }
            }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}