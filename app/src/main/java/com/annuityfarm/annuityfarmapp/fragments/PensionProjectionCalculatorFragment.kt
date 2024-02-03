package com.annuityfarm.annuityfarmapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.TextView
import com.annuityfarm.annuityfarmapp.*
import com.annuityfarm.annuityfarmapp.Constants.Companion.FRAG_MESSAGE
import com.annuityfarm.annuityfarmapp.Constants.Companion.PROJECTION_CALCULATOR
import com.annuityfarm.annuityfarmapp.base.BaseFragment
import com.annuityfarm.annuityfarmapp.bottomsheet.BottomSheetDetailedCalculatorResultsDialogFragment
import com.annuityfarm.annuityfarmapp.bottomsheet.BottomSheetItemListDialogFragment
import com.annuityfarm.annuityfarmapp.databinding.FragmentPensionProjectionCalculatorLayoutBinding
import com.annuityfarm.annuityfarmapp.ext.*
import com.annuityfarm.annuityfarmapp.interfaces.IPensionType
import com.annuityfarm.annuityfarmapp.models.BottomSheetItem
import com.annuityfarm.annuityfarmapp.models.FragMessage
import com.annuityfarm.annuityfarmapp.models.InsuranceProvider
import com.github.mikephil.charting.charts.LineChart
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.lang.Math.pow
import java.text.DecimalFormat
import kotlin.math.floor


enum class DepositFrequency(val value: Int){
    Additional_Annual_Investments(0),
    ANNUALLY(1),
    SEMI_ANNUALLY(2),
    QUARTERLY(4),
    BI_MONTHLY(6),
    MONTHLY(12),
    SEMI_MONTHLY(24),
    BI_WEEKLY(26),
    WEEKLY(52),
    DAILY(365)
}

class PensionProjectionCalculatorFragment : BaseFragment(), IPensionType {

    private var selectedInsuranceProvider: InsuranceProvider? = null

     val mutableInsuranceProviderList: MutableList<InsuranceProvider> = mutableListOf(
         InsuranceProvider().apply {
             id = 1
             name = "Insurance Provider"
             average = null
             guaranteedRate = null
             grossNet = ""
         },
        InsuranceProvider().apply {
             id = 1
             name = "Sanlam Life"
             average = 9.11
             guaranteedRate = 5.00
             grossNet = "Net"
        },
        InsuranceProvider().apply {
            id = 2
            name = "Britam"
            average = 10.7
            guaranteedRate = 5.00
            grossNet = "Gross"
        },
        InsuranceProvider().apply {
            id = 3
            name = "Jubilee"
            average = 10.2
            guaranteedRate = 4.00
            grossNet = "Gross"
        },
        InsuranceProvider().apply {
            id = 4
            name = "APA"
            average = 10.0
            guaranteedRate = 4.00
            grossNet = "Gross"
        },
        InsuranceProvider().apply {
            id = 5
            name = "CIC"
            average = 8.9
            guaranteedRate = 4.00
            grossNet = "Net"
        }

    )

    //Savings Plan Inputs
    val currency: String = "Kshs."
    val percentage: String = "%"
    var yearsToInvest: Int = 24
    var initialInvestment: Double = 90000.00
    var expectedXXInterestRate: Double = 9.00//%
    //Scheduled Deposits
    var depositAmount: Double = 10000.00
    var currentDepositFrequency: DepositFrequency = DepositFrequency.Additional_Annual_Investments
    var addionalAnnualInvestments: Double = 0.00

    //Summary of Results
    //Estimated Future Value
    var valueAfterXXYears: Double? = null
    //Gain/Loss Summary
    var totalInvested: Double? = null//=IF(ISBLANK(E4)," - ",SUM(OFFSET(F24,1,0,E4+1,1)))
    var interestEarned: Double? = null//=IF(ISBLANK(E4)," - ",SUM(OFFSET(D24,2,0,$E$4,1)))


    //Random Rates
    var randomRates: Boolean = false
    var min: Double = 10.00//%
    var max: Double = 12.00//%
    var average:Double = 9.00//%//=IF(ISBLANK(E4)," - ",AVERAGE(OFFSET(C24,2,0,E4,1)))

    //i order to have years starting from zero


    var rates:Array<Double>? = null
    var years:Array<Int>? = null

    private fun init(){

        yearsToInvest = binding.txtYearsToInvest.text.toString().let { if (it.isEmpty()) 1 else it.toInt()} + 1//24

        var count = 0
        //tables columns
        years = Array(yearsToInvest) { i ->
            i.let {
                if (count == i) {
                    count++
                    i
                } else {
                    count++
                    i + 1
                }

            }
        }

        //=IF(ISERROR(A26),NA(),IF(randrate,$H$15+RAND()*($H$16-$H$15),$E$6))
        rates = Array(yearsToInvest) { i ->
            if (years!!.size > i) {
                if (i == 0) {
                    0.00
                } else {
                    //=IF(ISERROR(A26),NA(),IF(randrate,$H$15+RAND()*($H$16-$H$15),$E$6))
                    //$H$15+
                    //RAND()*
                    //($H$16
                    //-$H$15)
                    //,$E$6
                    if (randomRates) {
                        //$H$15+RAND()*($H$16-$H$15)
                        if (binding.txtMin.text.toString().isNotEmpty() &&  binding.txtMax.text.toString().isNotEmpty())    (min + (floor(Math.random() * 1000000000) / 1000000000) * (max - min))
                        else expectedXXInterestRate
                    } else {
                        expectedXXInterestRate
                    }
                }
            } else {
                0.0
            }
        }


        average = floor(
            (if (randomRates) (sum(
                rates!!,
                1,
                yearsToInvest - 1
            ) / (yearsToInvest - 1)) else expectedXXInterestRate) * 100
        ) / 100
        binding.txtAverage.setText(average.toString())

    }

    private fun calculatePensionProjection(){

        init()

        val scheduledDeposit = Array(yearsToInvest) { i ->
            if (years!!.size > i) {
                if (i == 0) {
                    initialInvestment
                } else {
                    //$E$11+$E$9*deposits_per_year)
                    floor(
                        (addionalAnnualInvestments + depositAmount * computeNper(
                            currentDepositFrequency
                        )) * 100
                    ) / 100
                }
            } else {
                0.00
            }
        }


        //=IF(ISERROR(A49),NA(),H48+F49+G49+D49)
        val balances = Array(yearsToInvest) { i ->
            if (years!!.size > i) {
                if (i == 0) {
                    initialInvestment.toDouble()
                } else {
                    0.00
                }
            } else {
                0.00
            }
        }


        val extraAnnualDeposits = Array(yearsToInvest) { i -> addionalAnnualInvestments }

        val interest = Array(yearsToInvest) { i ->
            if (years!!.size > i) {
                //FV(
                //rate = ((1+C26/compound_period)^(compound_period/deposits_per_year))-1,
                //nper=deposits_per_year,
                //pmt= -$E$9,
                //pv= -H25)
                //-$E$9*deposits_per_year-H25
                //years[i]

                if (i == 0) {
                    0.00
                } else {

                    //=FV(((1+C26/compound_period)^(compound_period/deposits_per_year))-1,deposits_per_year,-$E$9,-H25)-$E$9*deposits_per_year-H25
                    //=FV(((1+C26/compound_period)^(compound_period/deposits_per_year))-1,deposits_per_year,-$E$9,-H25)-$E$9*deposits_per_year-H25)
                    i.let {
                        //calculate interest
                        val rate: Double = computeRate(
                            currentDepositFrequency,
                            rates!![if (it > 0) it else 0].toDouble()
                        );
                        //println(rate)
                        val nper: Int = computeNper(currentDepositFrequency)
                        val pmt: Double = depositAmount
                        val pv: Double = balances[if (it > 0) it - 1 else 0]
                        // println(0.00.roundTo(fv(0.09,1,10000.00,90000.00)-10000*1-90000));
                        //=-$E$9*deposits_per_year-H26
                        // println(0.00.roundTo(fv(0.09,1,10000.00,1348528.09)-10000*1-1348528.09));
                        val interest: Double = fv(rate, nper, pmt, pv) - pmt * nper - pv
                        //convertToCurrency(interest)
                        //calculate balance
                        balances[if (it > 0) it else 0] = floor(
                            (balances[if (it > 0) it - 1 else 0] + scheduledDeposit[if (it > 0) it else 0] +
                                    extraAnnualDeposits[if (it > 0) it else 0] + (floor(
                                interest * 100
                            ) / 100)) * 100
                        ) / 100

                        floor(interest * 100) / 100  //return
                    }
                }
            } else {
                0.00
            }
        }


        val cumulativeContribution = Array(yearsToInvest) { i ->
            if (years!!.size > i) {
                if (i == 0) {
                    0.00
                } else {
                    floor(
                        (sum(scheduledDeposit, 0, i) + sum(
                            extraAnnualDeposits,
                            0,
                            i
                        )) * 100
                    ) / 100
                }
            } else {
                0.00
            }
        }

        val cumulativeInterest = Array(yearsToInvest) { i ->
            if (years!!.size > i) {
                if (i == 0) {
                    0.00
                } else {
                    floor((sum(interest, 0, i)) * 100) / 100
                }
            } else {
                0.00
            }
        }


        // println(0.00.roundTo(fv(0.09,1,10000.00,90000.00)-10000*1-90000));
        // println(0.00.roundTo(fv(0.09,1,10000.00,1348528.09)-10000*1-1348528.09));

        //enumValues<DepositFrequency>().forEach{
        //    println("$it ${it.value}")
        // }


        //years.forEach{
        //    println(it)
        //}

        //Draw Table
        val pensionProjection = mutableListOf<ProjectCalculator>()
        years!!.forEachIndexed { index, element ->

            pensionProjection.add(ProjectCalculator().apply {
                amountYear = "${if(element == 0) "" else element}".removeEmpty()
                amountRate =  (if((floor(rates!![index] * 100) / 100) == 0.00) "" else "${(floor(rates!![index] * 100) / 100)}%").removeEmpty()
                amountInterest = (if(interest[index] == 0.00) "" else "$currency ${interest[index]}").removeEmpty()
                amountScheduledDeposit = ("$currency ${scheduledDeposit[index]}").removeEmpty()
                amountExtraAnnualDeposit = ("$currency ${extraAnnualDeposits[index]}").removeEmpty()
                amountBalance = ("$currency ${(balances[index])}").removeEmpty()
                amountCumulativeContribution = (if(cumulativeContribution[index] == 0.00) "" else "$currency ${cumulativeContribution[index]}").removeEmpty()
                amountCumulativeInterest =  (if(cumulativeInterest[index] == 0.00) "" else "$currency ${cumulativeInterest[index]}").removeEmpty()

                valueAfterXYears = ("$currency ${(balances[years!!.size-1])}").removeEmpty()
                lableForValueAfterXYears = years!![years!!.size-1].toString()
                totalInvested = ("$currency ${sum(scheduledDeposit, 0, years!!.size-1)}").removeEmpty()
                InterestEarned = ("$currency ${(cumulativeInterest[years!!.size-1])}").removeEmpty()
            })



        }

        putShared(PROJECTION_CALCULATOR, pensionProjection)
    }

   open class ProjectCalculator{
        var amountYear:String? = null
        var amountRate:String? = null
        var amountInterest:String? = null
        var amountScheduledDeposit:String? = null
        var amountExtraAnnualDeposit:String? = null
        var amountBalance:String? = null
        var amountCumulativeContribution:String? = null
        var amountCumulativeInterest:String? = null

        var valueAfterXYears:String? = null
        var lableForValueAfterXYears:String? = null
        var totalInvested:String? = null
        var InterestEarned:String? = null
    }


//    private fun Double.roundTo(value: Double): Double {
//        val df = DecimalFormat("#.##")
//        df.roundingMode = RoundingMode.CEILING
//        return df.format(value).toDouble()
//    }

    //https://www.experts-exchange.com/articles/1948/A-Guide-to-the-PMT-FV-IPMT-and-PPMT-Functions.html
    private fun fv(r: Double, nper: Int, c: Double, pv: Double): Double {
        val fv:Double = ((c * (pow(1 + r, nper.toDouble()) - 1) / r + pv
                * pow(1 + r, nper.toDouble())))
        return floor(fv * 100) / 100
    }



    private fun computeNper(currentDepositFrequency:DepositFrequency):Int{
        return currentDepositFrequency.value
    }

    private fun computeRate(currentDepositFrequency:DepositFrequency,rateConstant:Double):Double{
        //=(((1+C49/compound_period)^(compound_period/deposits_per_year))-1)
        val compound_period:Int = computeNper(currentDepositFrequency)
        val deposits_per_year:Int = computeNper(currentDepositFrequency)

        val rate:Double = pow((1+(rateConstant/100)/compound_period),(compound_period/deposits_per_year).toDouble())-1
        return rate//NOTE: DONT ROUND OFF TO TWO DECIMAL PLACES
    }


    private fun sum(array:Array<Double>, from:Int, to:Int):Double{
        var sum = 0.0
        for (i in from..to) {
            sum += array[i]
            //println(array[i])
        }

        return sum
    }

    private fun resetCurrencyFormat(inputValue: String): String {
        return inputValue.replace(currency, "").replace(",","")
    }

    fun setCurrencyFormat(input: TextInputEditText, inputValue:String, watcher:Any?) : String {

            if (inputValue.isNotEmpty()) {

                if (watcher is TextWatcher)
                    input.removeTextChangedListener(watcher)

                val unFormattedInputValue: String = resetCurrencyFormat(inputValue)


                val formattedInputValue: String = convertToCurrencyWithoutDeciaml(unFormattedInputValue.toDouble())

                input.setText(formattedInputValue)
                //escape the last two digits
                input.setSelection(formattedInputValue.length)

                if (watcher is TextWatcher)
                    input.addTextChangedListener(watcher)

                return formattedInputValue
            }
        return inputValue
    }




    private fun convertToCurrencyWithoutDeciaml(value: Double): String {
        val myFormatter = DecimalFormat("#,###")
        return myFormatter.format(value).toString()
    }


    private var _binding: FragmentPensionProjectionCalculatorLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentPensionProjectionCalculatorLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionMutablelistParam = actionMutableList(requireActivity())

        bottomSheetItemMutableListParam =
            bottomSheetItemMutableList(requireActivity()) as ArrayList<BottomSheetItem>

        val fragMessage: FragMessage = getShared(FRAG_MESSAGE, FragMessage::class.java)
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).apply {
                binding.toolbarTitle.apply { text = fragMessage.fragTitle }
                binding.imgTitle.apply {
                    fragMessage.fragImage?.let { it1 ->
                        setBackgroundResource(
                            it1
                        );show();binding.imgAnnuityLogo.gone()
                    }
                }
                binding.btnPensionProjectionCalculator.gone()
            }
        }

        binding.btnRandomRates.setOnClickListener {

            if (binding.imagUncheckedRandomRates.visibility == View.VISIBLE){
                binding.layoutRandomRates.show()
                randomRates = true

                binding.scroll.let {
                    it.post { it.fullScroll(View.FOCUS_DOWN) }
                }

                binding.imgCheckedRandomRates.show()
                binding.imagUncheckedRandomRates.gone()//hide self
            }else{
                if (binding.imgCheckedRandomRates.visibility == View.VISIBLE){
                    binding.layoutRandomRates.gone()
                    randomRates = false

                    binding.imagUncheckedRandomRates.show()
                    binding.imgCheckedRandomRates.gone()//hide self
                }
            }

        }



        binding.spInsuranceProvider.apply{
            adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,mutableInsuranceProviderList)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                    selectedInsuranceProvider = mutableInsuranceProviderList.let {  insuranceProviderList ->
                        var returnValue:InsuranceProvider? = null
                        for (insuranceProvider in insuranceProviderList) {
                            if (adapterView.selectedItem.toString() == insuranceProvider.name &&
                                !insuranceProvider.name.equals("Insurance Provider")){
                                returnValue = insuranceProvider;break
                            }
                        }
                        returnValue
                    }

                   selectedInsuranceProvider?.let {
                        //toast(it.name)
                       selectedInsuranceProvider!!.average.toString().let {
                           expectedXXInterestRate =  if (it.isEmpty()) 0.00 else it.toDouble()
                           binding.txtExpectedAnnualInterestRate.setText(it)
                       }

                    }


                }
                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }
        }

        val mutableDepositFrequencyList = mutableListOf<String>()
        enumValues<DepositFrequency>().forEachIndexed{
            index,depositFrequency ->
            run {
                if (index == 0) {
                    mutableDepositFrequencyList.add(getResourceString(R.array.deposit_frequency, true))
                } else {
                    mutableDepositFrequencyList.add(depositFrequency.name.removeHyphen())
                }
            }
        }

        binding.spDepositFrequency.apply{
            adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,mutableDepositFrequencyList)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                    currentDepositFrequency = enumValues<DepositFrequency>().let {  depositFrequencyList ->
                        for (depositFrequency in depositFrequencyList) {
                            val firstValue = adapterView.selectedItem.toString()
                            val secondValue = depositFrequency.name.removeHyphen()
                            if (firstValue.equals(secondValue, ignoreCase = true)  &&
                                !firstValue.equals(getResourceString(R.array.deposit_frequency, true), ignoreCase = true)
                            ){
                                currentDepositFrequency = depositFrequency
                                break
                            }
                        }
                        currentDepositFrequency
                    }

                   // toast(currentDepositFrequency.name)

                }
                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }
        }


        binding.txtInitialInvestment.let { txtInitialInvestment ->
            txtInitialInvestment.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setCurrencyFormat(txtInitialInvestment,s.toString(), this)
                }

            })
        }

        binding.txtDepositAmount.let { txtInitialInvestment ->
            txtInitialInvestment.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setCurrencyFormat(txtInitialInvestment,s.toString(), this)
                }

            })
        }

        binding.txtAdditionalAnnualInvestments.let { txtInitialInvestment ->
            txtInitialInvestment.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setCurrencyFormat(txtInitialInvestment,s.toString(), this)
                }

            })
        }

        binding.txtMin.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty() && binding.txtMax.text.toString().isNotEmpty()) {
                    init()
                }
            }

        })
        binding.txtMax.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.toString().isNotEmpty() && binding.txtMin.text.toString().isNotEmpty()) {
                    init()
                }
            }

        })


        binding.txtYearsToInvest.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty())
                    validateYearToInvest().let {
                        if(it.isNotEmpty()){
                            binding.txtYearsToInvest.snack(it, Snackbar.LENGTH_LONG)
                        }else{
                            init()
                        }
                    }
            }

        })


        binding.btnCalculate.setOnClickListener{ v ->
            v.hideKeyboard()
            yearsToInvest = binding.txtYearsToInvest.text.toString().let { if (it.isEmpty()) 1 else it.toInt()} + 1//24
            initialInvestment = binding.txtInitialInvestment.text.toString().let { if (it.isEmpty()) 0.00 else resetCurrencyFormat(it).toDouble()}.toString().toDouble()//90000.00
            depositAmount = binding.txtDepositAmount.text.toString().let { if (it.isEmpty()) 0.00 else resetCurrencyFormat(it).toDouble()}.toString().toDouble()//10000.00
            addionalAnnualInvestments = binding.txtAdditionalAnnualInvestments.text.toString().let { if (it.isEmpty()) 0.00 else resetCurrencyFormat(it).toDouble()}.toString().toDouble()
            min = binding.txtMin.text.toString().let { if (it.isEmpty()) 0.00 else it.toDouble()}//10.00//%
            max = binding.txtMax.text.toString().let { if (it.isEmpty()) 0.00 else it.toDouble()}//12.00//%


            if(validate()){
                calculatePensionProjection()
                BottomSheetDetailedCalculatorResultsDialogFragment.newInstance(null, this@PensionProjectionCalculatorFragment,
                    fragMessage
                ).show(requireActivity().supportFragmentManager, "dialog")
            }
        }



    }

    private fun errorMessage(message: String): String {
        return "Please provide details for the field \"${message}\""
    }

    private fun validateYearToInvest():String{
       return when{
            binding.txtYearsToInvest.text.toString().isNotEmpty() -> {
                when{
                    (binding.txtYearsToInvest.text.toString().toInt() > 100) -> {"Please provide a value not greater than 100 for \"${getResourceString(R.string.years_to_invest, false)}\""}
                    (binding.txtYearsToInvest.text.toString().toInt() < 1) -> {"Please provide a value not less than 1 for \"${getResourceString(R.string.years_to_invest, false)}\""}
                    else -> ""
                }
            }
            else -> ""}
    }

    private fun validate(): Boolean{
        var message:String = when{
            //When Empty
            selectedInsuranceProvider == null -> errorMessage(getResourceString(R.array.providers,true))
            binding.txtYearsToInvest.text.toString().isEmpty() -> errorMessage(getResourceString(R.string.years_to_invest, false))
            binding.txtInitialInvestment.text.toString().isEmpty() -> errorMessage(getResourceString(R.string.initial_investment, false))
           // binding.txtExpectedAnnualInterestRate.text.toString().isEmpty() -> getResourceString(R.string.expected_annual_interest_rate, false)
            binding.txtDepositAmount.text.toString().isEmpty() -> errorMessage(getResourceString(R.string.deposit_amount, false))
            currentDepositFrequency == DepositFrequency.Additional_Annual_Investments -> errorMessage(getResourceString(R.array.deposit_frequency, true))
            //binding.txtAdditionalAnnualInvestments.text.toString().isEmpty() -> getResourceString(R.string.additional_annual_investments, false)

            binding.txtMin.text.toString().isEmpty() && randomRates -> errorMessage(getResourceString(R.string.min, false))
            binding.txtMax.text.toString().isEmpty() && randomRates -> errorMessage(getResourceString(R.string.max, false))
            else -> ""
        }

        //With data
      message =  validateYearToInvest().let { if(it.isEmpty()) message else it }


       return  if(message.isEmpty()) {binding.btnCalculate.isEnabled = true; true } else { binding.pensionProjectionCalculatorLayout.snack(message, Snackbar.LENGTH_LONG); false}

    }


    override fun onItemClicked(vararg messageParam: Any?) {
        messageParam.forEachIndexed { index, element ->
            when (index) {
                0 -> when (element) {
                    is Int -> actionMutablelistParam.forEachIndexed { index, pensionType ->
                        if (index == element) {
                            pensionType.apply {
                                isActive = true
                                val fragMessage: FragMessage = FragMessage().apply {
                                    fragTitle = title; fragImage = activeImage
                                }
                                BottomSheetItemListDialogFragment.newInstance(
                                    bottomSheetItemMutableListParam,
                                    this@PensionProjectionCalculatorFragment,
                                    fragMessage
                                ).show(requireActivity().supportFragmentManager, "dialog")
                                //toast("I am Clicked${title}")
                            }
                        } else {
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

            
}
