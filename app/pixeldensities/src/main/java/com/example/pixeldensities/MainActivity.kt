package com.example.pixeldensities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setLogo(R.drawable.fish_logo)
        supportActionBar!!.setDisplayUseLogoEnabled(true)

        //todo check drawable folder and res->new->Batch ImaGE iMPORTER
        // + THE DRAWABLE FOLDER FOR SAMPLE IMPORTED IMGS WITH DIFFERENT PIXEL DENSITIES

        setContentView(R.layout.activity_main)


    }
}