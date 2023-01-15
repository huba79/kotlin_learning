package com.example.mathgame.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mathgame.R
import com.example.mathgame.databinding.ActivitySettingsBinding
import com.example.mathgame.game.GameSettings
import com.google.android.material.checkbox.MaterialCheckBox

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var settings:GameSettings
    private lateinit var binding:ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Deprecated("Deprecated in Java")
        settings = intent?.getSerializableExtra("currentSettings") as GameSettings

        binding.timeLimitEdit.setText(settings.timeFrame.toString())
        binding.performanceRewardCheckbox.checkedState = if (settings.rewardPerformance) MaterialCheckBox.STATE_CHECKED
                                                                                                else MaterialCheckBox.STATE_UNCHECKED
        binding.livesEdit.setText(settings.livesAvailable.toString())

        binding.additionMaxEdit.setText( settings.additionRange.second.toString() )
        binding.additionMinEdit.setText( settings.additionRange.first.toString() )
        binding.multiplicationMaxEdit.setText(settings.multiplicationRange.second.toString())
        binding.multiplicationMinEdit.setText( settings.multiplicationRange.first.toString() )

        binding.saveButton.setOnClickListener(this@SettingsActivity)

    }

    override fun onClick(p0: View?) {
        settings.rewardPerformance = binding.performanceRewardCheckbox.isChecked

        if (binding.timeLimitEdit.text.toString() != "") {
            settings.timeFrame = binding.timeLimitEdit.text.toString().toInt()
        }
        if(binding.additionMinEdit.text.toString() != "" && binding.additionMaxEdit.text.toString() != "") {
            settings.additionRange=Pair (binding.additionMinEdit.text.toString().toInt(),
                binding.additionMaxEdit.text.toString().toInt() )
        }

        if(binding.multiplicationMinEdit.text.toString() != "" && binding.multiplicationMaxEdit.text.toString() != "") {
            settings.multiplicationRange=Pair (binding.multiplicationMinEdit.text.toString().toInt(),
                binding.multiplicationMaxEdit.text.toString().toInt() )
        }
        if(binding.livesEdit.text.toString() != "") {
            settings.livesAvailable=binding.livesEdit.text.toString().toInt()
        }

        val intent = Intent(this@SettingsActivity,MainActivity::class.java)
        intent.putExtra("settings",settings)
        Log.d("mathLog","Settings pushed towards MainActivity")
        startActivity(intent)

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Log.d("mathLog","Back pressed")
        Toast.makeText(this@SettingsActivity, getString(R.string.toast_use_save_button),Toast.LENGTH_SHORT).show()
    }
}