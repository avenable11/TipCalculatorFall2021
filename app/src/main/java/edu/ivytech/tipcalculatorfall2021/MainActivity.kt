package edu.ivytech.tipcalculatorfall2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import edu.ivytech.tipcalculatorfall2021.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var tipPercent = .15
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.upButton.setOnClickListener {
            tipPercent += .01
            displayPercent()
            //binding.upButton.isEnabled = tipPercent <= .30
        }
        binding.downButton.setOnClickListener {
            tipPercent -= .01
            displayPercent()
        }
        binding.calculateBtn.setOnClickListener{
            calculateAndDisplay()
        }
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
        val tipAmount = billAmount * tipPercent
        val totalAmount = billAmount + tipAmount

        val currencyFormat = NumberFormat.getCurrencyInstance()
        val symbol = DecimalFormatSymbols()
        symbol.currencySymbol = ""
        (currencyFormat as DecimalFormat).decimalFormatSymbols = symbol
        binding.tipAmountEditText.setText(currencyFormat.format(tipAmount))
        binding.totalAmountEditText.setText(currencyFormat.format(totalAmount))
        binding.billAmountEditText.setText(currencyFormat.format(billAmount))
        displayPercent()
    }

}