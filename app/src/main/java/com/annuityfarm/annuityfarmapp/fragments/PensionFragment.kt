package com.annuityfarm.annuityfarmapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annuityfarm.annuityfarmapp.*
import com.annuityfarm.annuityfarmapp.adapters.ActionListAdapter

import com.annuityfarm.annuityfarmapp.base.BaseFragment
import com.annuityfarm.annuityfarmapp.bottomsheet.BottomSheetItemListDialogFragment
import com.annuityfarm.annuityfarmapp.databinding.*
import com.annuityfarm.annuityfarmapp.ext.goToFragment
import com.annuityfarm.annuityfarmapp.ext.putShared
import com.annuityfarm.annuityfarmapp.interfaces.IPensionType
import com.annuityfarm.annuityfarmapp.models.BottomSheetItem
import com.annuityfarm.annuityfarmapp.models.FragMessage

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PensionFragment : BaseFragment(), IPensionType {

    private var _binding: FragmentPensionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentPensionBinding.inflate(inflater, container, false)
        return binding.root

    }

//    private fun btnActiveStatus(active: View, vararg other: View) {
//        for (view in other) {
//            if (active.id == view.id) {
//                view.setBackgroundResource(R.drawable.solid_violet_rounded_corner_bg)
//            } else {
//                view.setBackgroundResource(R.drawable.violet_rounded_corner_bg)
//            }
//        }
//    }

    private fun populateRecyclerView(){
        val recyclerView: RecyclerView = binding.recyclerviewPensionActions
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            //TODO: GET FROM THE API AND ASSIGN TO actionMutableList
            //actionList =  actionMutableList
            adapter = ActionListAdapter(this@PensionFragment, this@PensionFragment)
            adapter?.notifyDataSetChanged()
        }
    }

    //var actionMutableList: MutableList<PensionType>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionMutablelistParam = actionMutableList(requireActivity())

        bottomSheetItemMutableListParam = bottomSheetItemMutableList(requireActivity()) as ArrayList<BottomSheetItem>

//        val  argValue:Int? = arguments?.getInt(ARG_ITEM_COUNT)
        populateRecyclerView()

//        binding.btnPensionApplication.setOnClickListener {
           // findNavController().navigate(R.id.action_HomeFragment_to_PensionFragment)
//            btnActiveStatus(otherArray[0], *otherArray)
//            BottomSheetItemListDialogFragment.newInstance(30).show(requireActivity().supportFragmentManager, "dialog")
//        }

    }

    override fun onItemClicked(vararg messageParam: Any?) {
        messageParam.forEachIndexed { index, element ->
            when (index) {
                0 -> when (element) {
                    is Int -> actionMutablelistParam.forEachIndexed { i, pensionType ->
                        if(i == element){
                            pensionType.apply {
                                isActive = true
                                val fragMessage: FragMessage = FragMessage().apply {fragTitle = title; fragImage = activeImage  }
                                BottomSheetItemListDialogFragment.newInstance(bottomSheetItemMutableListParam, this@PensionFragment,
                                    fragMessage
                                ).show(requireActivity().supportFragmentManager, "dialog")
                                //toast("I am Clicked${title}")
                            }
                        }else{
                            pensionType.apply {
                                isActive = false
                                //toast("I am Clicked${title}")
                            }
                        }
                    }
                    is FragMessage -> {
                        putShared(Constants.FRAG_MESSAGE, element)
                        goToFragment(R.id.action_pension_fragment_to_subtype_fragment)

                        //element.title?.let { toast(it) }
                    }
                }
            }

        }

        populateRecyclerView()
    }

    override fun onResume() {

        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).apply {
                binding.toolbarTitle.apply { text = resources?.getString(R.string.pension) }
                binding.imgTitle.apply { gone();binding.imgAnnuityLogo.gone() }


                binding.btnPensionProjectionCalculator.let {
                    it.setOnClickListener {
                        putShared(
                            Constants.FRAG_MESSAGE,
                            FragMessage().apply {
                                fragTitle = "Pension Calculator"
                                fragImage = R.drawable.pension_projection_icon
                                message = null
                            })
                        //crashes when you use activity's goToFragment, ID OF THE VIEW DOES NOT EXIST IN THIS ACTIVITY
                        this@PensionFragment.goToFragment(R.id.action_pension_fragment_to_pension_project_calculator_fragment)
                    }
                    it.show()
                }
            }
        }

        super.onResume()
    }
    

    override fun onDestroyView() {
        super.onDestroyView()

        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).apply {
               // binding.toolbarTitle.apply { text = resources?.getString(R.string.pension) }
               // binding.imgTitle.apply { hide();binding.imgAnnuityLogo.hide() }

                binding.btnPensionProjectionCalculator.gone()
            }
        }

        _binding = null

    }
}