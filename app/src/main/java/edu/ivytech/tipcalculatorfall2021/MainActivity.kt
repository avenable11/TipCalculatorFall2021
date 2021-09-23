package edu.ivytech.tipcalculatorfall2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.SeekBar

import edu.ivytech.tipcalculatorfall2021.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var tipPercent = .15
    private val ROUND_NONE = 0
    private val ROUND_TIP = 1
    private val ROUND_TOTAL = 2
    private var rounding = ROUND_NONE
    private var split = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateBtn.setOnClickListener{
            calculateAndDisplay()
        }

        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                tipPercent = progress/100.0
                binding.tipPercentEditText.setText(progress.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        binding.radioGroup.check(R.id.noRoundingRadioButton)
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId) {
                R.id.noRoundingRadioButton -> rounding = ROUND_NONE
                R.id.roundTipRadioButton -> rounding = ROUND_TIP
                R.id.roundTotalRadioButton -> rounding = ROUND_TOTAL
            }
        }

        val items = resources.getStringArray(R.array.split_array)
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        binding.splitDropdown.setAdapter(adapter)
        binding.splitDropdown.setText(items[0], false)
        binding.splitDropdown.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                for(i in items.indices) {
                    if(items[i] == s.toString()) {
                        split = i + 1
                        break
                    }
                }
                if(split > 1) {
                    binding.eachPays.visibility = View.VISIBLE
                } else {
                    binding.eachPays.visibility = View.GONE
                }
                calculateAndDisplay()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    private fun displayPercent() {
        val percentFormat = NumberFormat.getPercentInstance()
        val symbol = DecimalFormatSymbols()
        symbol.percent = ' '
        (percentFormat as DecimalFormat).decimalFormatSymbols = symbol
        binding.tipPercentEditText.setText(percentFormat.format(tipPercent))
    }

    private fun calculateAndDisplay() {
        val billAmountStr = binding.billAmountEditText.text.toString()
        var billAmount = 0.0
        if(billAmountStr.isNotEmpty()) {
            billAmount = billAmountStr.toDouble()
        }
        var tipAmount = billAmount * tipPercent
        if(rounding == ROUND_TIP) {
            tipAmount = round(tipAmount)
        }
        var totalAmount = billAmount + tipAmount
        if(rounding == ROUND_TOTAL) {
            totalAmount = round(totalAmount)
            tipAmount = totalAmount - billAmount
        }


        val currencyFormat = NumberFormat.getCurrencyInstance()
        val symbol = DecimalFormatSymbols()
        symbol.currencySymbol = ""
        (currencyFormat as DecimalFormat).decimalFormatSymbols = symbol
        binding.tipAmountEditText.setText(currencyFormat.format(tipAmount))
        binding.totalAmountEditText.setText(currencyFormat.format(totalAmount))
        binding.billAmountEditText.setText(currencyFormat.format(billAmount))
        //displayPercent()

        if(split > 1) {
            val splitAmount = totalAmount/split
            binding.eachPaysET.setText(currencyFormat.format(splitAmount))
        }
    }

}