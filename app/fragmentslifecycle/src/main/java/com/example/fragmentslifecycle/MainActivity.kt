package com.example.fragmentslifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {
    lateinit var fragmentManager:FragmentManager
    lateinit var loadFragmentButton:Button
    var fragment1 = Fragment1.newInstance("par1","par2")
    var fragment2 = Fragment2.newInstance("par1","par2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager
        loadFragmentButton =findViewById(R.id.loadFragmentButtonId)

        loadFragmentButton.setOnClickListener{

            val fragment = fragmentManager.findFragmentByTag("fragment1")

            if(fragment !=null && fragment.isVisible ){
                var fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.remove(fragment1)
                fragmentTransaction.add(R.id.fragmentContainerViewId,fragment2,"fragment2")
                fragmentTransaction.commit()
                loadFragmentButton.text="Load First Fragment"
            } else  {
                var fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.remove(fragment2)
                fragmentTransaction.add(R.id.fragmentContainerViewId,fragment1,"fragment1")
                fragmentTransaction.commit()
                loadFragmentButton.text="Load Second Fragment"
            }

        }



    }
}