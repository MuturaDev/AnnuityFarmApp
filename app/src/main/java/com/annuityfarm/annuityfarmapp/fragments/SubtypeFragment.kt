package com.annuityfarm.annuityfarmapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.annuityfarm.annuityfarmapp.*
import com.annuityfarm.annuityfarmapp.Constants.Companion.FRAG_MESSAGE
import com.annuityfarm.annuityfarmapp.base.BaseFragment
import com.annuityfarm.annuityfarmapp.bottomsheet.BottomSheetItemListDialogFragment
import com.annuityfarm.annuityfarmapp.customviews.TimelineHeaderView
import com.annuityfarm.annuityfarmapp.databinding.FragmentSubtypeBinding
import com.annuityfarm.annuityfarmapp.ext.getShared
import com.annuityfarm.annuityfarmapp.ext.goToActivity
import com.annuityfarm.annuityfarmapp.ext.goToFragment
import com.annuityfarm.annuityfarmapp.ext.putShared
import com.annuityfarm.annuityfarmapp.interfaces.IPensionType
import com.annuityfarm.annuityfarmapp.libraries.documentscanner.MenuActivity
import com.annuityfarm.annuityfarmapp.models.BottomSheetItem
import com.annuityfarm.annuityfarmapp.models.FragMessage
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.StringReader
import java.lang.reflect.Type

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SubtypeFragment : BaseFragment(), IPensionType {

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

    private var _binding: FragmentSubtypeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSubtypeBinding.inflate(inflater, container, false)
        return binding.root

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IncomeDrawDownFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubtypeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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

//    private fun populateRecyclerView(){
//        val recyclerView: RecyclerView = binding.recyclerviewPensionActions
//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(context)
//            //TODO: GET FROM THE API AND ASSIGN TO actionMutableList
//            //actionList =  actionMutableList
//            adapter = ActionListAdapter(this@PensionFragment, this@PensionFragment)
//            adapter?.notifyDataSetChanged()
//        }
//    }

    //var actionMutableList: MutableList<PensionType>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(requireActivity() is MainActivity)
            (requireActivity() as MainActivity).apply {

                binding.btnScan.apply {
                    setOnClickListener {
                        goToActivity(requireContext(), MenuActivity::class.java)
                    }
                    show()
                }
            }

        actionMutablelistParam = actionMutableList(requireActivity())

        bottomSheetItemMutableListParam = bottomSheetItemMutableList(requireActivity()) as ArrayList<BottomSheetItem>

//        val  argValue:Int? = arguments?.getInt(ARG_ITEM_COUNT)
       // populateRecyclerView()

//        binding.btnPensionApplication.setOnClickListener {
//            // findNavController().navigate(R.id.action_HomeFragment_to_PensionFragment)
//            btnActiveStatus(otherArray[0], *otherArray)
//            BottomSheetItemListDialogFragment.newInstance(30).show(requireActivity().supportFragmentManager, "dialog")
//        }

        val fragMessage: FragMessage = getShared(FRAG_MESSAGE, FragMessage::class.java)
        val title = (fragMessage.message as LinkedTreeMap<*, *>)["title"].toString()
        if(requireActivity() is MainActivity)
            (requireActivity() as MainActivity).apply {
                binding.toolbarTitle.apply { text  = fragMessage.fragTitle}
                binding.toolbarSubTitle.apply { text = title;show()}
                binding.imgTitle.apply { fragMessage.fragImage?.let { it1 -> setBackgroundResource(it1);show();binding.imgAnnuityLogo.gone() } }
                binding.btnPensionProjectionCalculator.gone()
            }

        val allStep = arrayOf(binding.step1,binding.step2,binding.step3,binding.step4,binding.step5,binding.step6)

        initStepsVisibility(
            allStep,
            arrayOf(binding.step1)
        )

        binding.step1.btnNext?.setOnClickListener {
            initStepsVisibility(
                allStep,
                arrayOf(binding.step2)
            )

            binding.step1.btnNext?.gone()
        }

        binding.step2.btnNext?.setOnClickListener {
            initStepsVisibility(
                allStep,
                arrayOf(binding.step3)
            )

            binding.step2.btnNext?.gone()
        }

        binding.step3.btnNext?.setOnClickListener {
            initStepsVisibility(
                allStep,
                arrayOf(binding.step4)
            )

            binding.step3.btnNext?.gone()
        }

        binding.step4.btnNext?.setOnClickListener {
            initStepsVisibility(
                allStep,
                arrayOf(binding.step5)
            )

            binding.step4.btnNext?.gone()
        }

        binding.step5.btnNext?.setOnClickListener {
            initStepsVisibility(
                allStep,
                arrayOf(binding.step6)
            )

            binding.step5.btnNext?.gone()
        }

        binding.step3.btnNext?.gone()

    }




    private fun initStepsVisibility(allSteps: Array<TimelineHeaderView>, makeStepsVisible: Array<TimelineHeaderView>){
        allSteps.forEachIndexed { i, _ ->
            makeStepsVisible.forEachIndexed { j, _ ->
                 if(allSteps[i] == makeStepsVisible[j]) makeStepsVisible[j].expand()
                 else if (allSteps[i].visibility != View.VISIBLE) allSteps[i].collapse()
            }
        }
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
                                BottomSheetItemListDialogFragment.newInstance(bottomSheetItemMutableListParam, this@SubtypeFragment,fragMessage).show(requireActivity().supportFragmentManager, "dialog")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        if(requireActivity() is MainActivity)
            (requireActivity() as MainActivity).apply {
                binding.toolbarSubTitle.apply {gone()}
                binding.btnScan.gone()
            }
    }
}