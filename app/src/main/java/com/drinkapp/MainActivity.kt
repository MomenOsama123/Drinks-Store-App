package com.drinkapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    lateinit var drinksMenu: AutoCompleteTextView
    lateinit var drinksInputLayout: TextInputLayout
    lateinit var priceTV: TextView
    lateinit var submitOrder: Button

    //Drinks
    private val orangeJuice = "Orange Juice"
    private val appleJuice = "Apple Juice"
    private val mangoJuice = "Mango Juice"
    private val strawberryJuice = "Strawberry Juice"
    private val bananaShake = "Banana Shake"
    val prices = mapOf(
        orangeJuice to 15,
        appleJuice to 20,
        mangoJuice to 30,
        strawberryJuice to 25,
        bananaShake to 10
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializer()

        populateDropDownMenu()

        drinksMenu.setOnItemClickListener { adapterView, view, i, l ->
            val selectedDrink = drinksMenu.text.toString()
            val price = prices[selectedDrink]
            priceTV.text = price.toString()
            drinksInputLayout.error = null
        }

        submitOrder.setOnClickListener {
            val selectedDrink = drinksMenu.text.toString()
            val defaultChoice = getString(R.string.enter_your_choice)

            if (selectedDrink == defaultChoice || selectedDrink.isEmpty()) {
                drinksInputLayout.error = "You need to select a drink"
            } else if (!prices.containsKey(selectedDrink)) {
                drinksInputLayout.error = "Invalid selection"
            } else {
                drinksInputLayout.error = null
                val sendIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("JuiceCaffe@examplepetstore.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "Order")
                    putExtra(Intent.EXTRA_TEXT, "I would like to order a $selectedDrink")
                }
                startActivity(sendIntent)
            }
        }
    }

    private fun initializer() {
        drinksMenu = findViewById(R.id.DrinksMenu)
        drinksInputLayout = findViewById(R.id.textInputLayout)
        priceTV = findViewById(R.id.Price)
        submitOrder = findViewById(R.id.Sub_Order)
    }

    private fun populateDropDownMenu() {
        val listOfDrinks = listOf(orangeJuice, appleJuice, mangoJuice, strawberryJuice, bananaShake)
        val adapter = ArrayAdapter(this, R.layout.list_items_tv, listOfDrinks)
        drinksMenu.setAdapter(adapter)
    }
}