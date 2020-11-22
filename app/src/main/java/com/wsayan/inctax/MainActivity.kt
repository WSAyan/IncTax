package com.wsayan.inctax

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.taxText).text =
        IncTaxCore().calculateTaxByDefaultGross(IncTaxCore.GENDER_FEMALE,55000,60000,0).toString()
    }
}