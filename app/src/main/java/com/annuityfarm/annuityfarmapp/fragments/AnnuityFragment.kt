package com.annuityfarm.annuityfarmapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.annuityfarm.annuityfarmapp.R

import androidx.navigation.fragment.findNavController
import com.annuityfarm.annuityfarmapp.Constants
import com.annuityfarm.annuityfarmapp.Constants.Companion.FRAG_MESSAGE
import com.annuityfarm.annuityfarmapp.base.BaseFragment
import com.annuityfarm.annuityfarmapp.bottomsheet.BottomSheetItemListDialogFragment
import com.annuityfarm.annuityfarmapp.databinding.*
import com.annuityfarm.annuityfarmapp.ext.goToFragment
import com.annuityfarm.annuityfarmapp.ext.putShared
import com.annuityfarm.annuityfarmapp.ext.toast
import com.annuityfarm.annuityfarmapp.interfaces.IPensionType
import com.annuityfarm.annuityfarmapp.models.BottomSheetItem
import com.annuityfarm.annuityfarmapp.models.FragMessage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AnnuityFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnnuityFragment : BaseFragment() , IPensionType{




    private var _binding: FragmentAnnuityBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnnuityBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnnuityFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AnnuityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnAnnuity.setOnClickListener {
//            findNavController().navigate(R.id.action_AnnuityFragment_to_HomeFragment)
//        }
        bottomSheetItemMutableListParam = bottomSheetItemMutableList(requireActivity()) as ArrayList<BottomSheetItem>

        binding.requestQuote.setOnClickListener{
            BottomSheetItemListDialogFragment.newInstance(bottomSheetItemMutableListParam, this@AnnuityFragment, null).show(requireActivity().supportFragmentManager, "dialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                                BottomSheetItemListDialogFragment.newInstance(bottomSheetItemMutableListParam, this@AnnuityFragment,fragMessage).show(requireActivity().supportFragmentManager, "dialog")
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
                        putShared(FRAG_MESSAGE, element)
                        goToFragment(R.id.action_pension_fragment_to_subtype_fragment)
                        //element.title?.let { toast(it) }
                    }
                }
            }

        }

        // populateRecyclerView()
    }
}