@file:Suppress("unused", "unused", "unused", "unused")

package com.example.widgets

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar.make


@Suppress("unused")
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var img1: ImageView
    private lateinit var img2: ImageView
    private lateinit var swapButton: Button
    private lateinit var spinner: Spinner
    private lateinit var snackButton:Button
    private lateinit var exitButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img1 = findViewById(R.id.image1)
        img2 = findViewById(R.id.image2)
        swapButton = findViewById(R.id.swapButton)
        spinner = findViewById(R.id.spinner)
        snackButton=findViewById(R.id.snackButtonId)
        exitButton = findViewById(R.id.exitButtonId)

        swapButton.setOnClickListener {
            val drawable = img1.drawable
            img1.setImageDrawable(img2.drawable)
            img2.setImageDrawable(drawable)
        }

        snackButton.setOnClickListener{
            val rootView: View =
                this@MainActivity.window.decorView.findViewById(android.R.id.content)
            make(rootView,"You have selected something, somewhere", LENGTH_INDEFINITE)
                .setAction("Close") { }.show()
        }

        exitButton.setOnClickListener{
            this.showAlertDialog()
        }



        val arrayAdapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item,
            arrayListOf<String?>("Egy","Ketto","Harom"))
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener=this

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent !=null) {
            Toast
                .makeText(this@MainActivity,"You have selected ${parent.getItemAtPosition(position)}",
                    Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast
            .makeText(this@MainActivity,"You have canceled your selection!",Toast.LENGTH_SHORT)
            .show()
    }

    private fun showAlertDialog(){
        val alertDialog = MaterialAlertDialogBuilder(this@MainActivity)
        alertDialog.setTitle("Confirm exit intent")
            .setIcon(R.drawable.ic_round_warning_24)
            .setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialogInterface, whichButton ->
                this.run {
                    this@MainActivity.finish()
                }
            }
            .setNegativeButton("Cancel") { dialogInterface, whichButton ->
                this.run {
                    dialogInterface.cancel()
                }
            }
        alertDialog.create().show()
    }

}