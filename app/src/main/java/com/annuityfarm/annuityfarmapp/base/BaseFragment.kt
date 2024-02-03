package com.annuityfarm.annuityfarmapp.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.annuityfarm.annuityfarmapp.R
import com.annuityfarm.annuityfarmapp.models.BottomSheetItem
import com.annuityfarm.annuityfarmapp.models.PensionType

open class BaseFragment : Fragment(){

    lateinit var actionMutablelistParam:MutableList<PensionType>
    lateinit var bottomSheetItemMutableListParam: ArrayList<BottomSheetItem>

    fun actionMutableList(myContext: Context):MutableList<PensionType>{
        return mutableListOf(
            PensionType().apply {
                activeImage = R.drawable.pension_application_icon_white
                inactiveImage = R.drawable.pension_application_icon
                title = myContext.resources.getString(R.string.pension_application)
                subTitle = myContext.getString(R.string.make_an_application_to_a_provider_of_your_choice)
                isActive = false
            },
            PensionType().apply {
                activeImage = R.drawable.pension_transfer_icon_white
                inactiveImage = R.drawable.pension_transfer_icon_violet
                title = myContext.getString(R.string.pension_transfer)
                subTitle = myContext.getString(R.string.make_a_transfer_from_your_provider_to_another)
                isActive = false
            },
            PensionType().apply {
                activeImage = R.drawable.pension_withdrawals_icon_white
                inactiveImage = R.drawable.pension_withdrawals
                title = myContext.getString(R.string.pension_withdrawals)
                subTitle = myContext.getString(R.string.make_a_withdrawal_of_your_moneys)
                isActive = false
            })
    }

    fun bottomSheetItemMutableList(myContext: Context):MutableList<BottomSheetItem>{
        return mutableListOf(
            BottomSheetItem().apply {
                image = R.drawable.provider1
                title = "Sanlam Kenya plc"
                isActive = false
            },
            BottomSheetItem().apply {
                image = R.drawable.provider2
                title = "AIG Kenya Insurance Company"
                isActive = false
            },
            BottomSheetItem().apply {
                image = R.drawable.provider3
                title = "Jubilee Insurance Company"
                isActive = false
            })
    }




}