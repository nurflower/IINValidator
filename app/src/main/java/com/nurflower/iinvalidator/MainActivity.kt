package com.nurflower.iinvalidator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nurflower.iinvalidator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.iin_validation)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.apply {
            viewModel = mainActivityViewModel
            lifecycleOwner = this@MainActivity
        }

        mainActivityViewModel.iinStatus.observe(this, Observer { status->
            showStatus(status)
        })
    }

    private fun showStatus(status: Int) {
        when(status){
            EMPTY ->{
                binding.iinEt.error = getString(R.string.empty_field)
            }
            WRONG_LENGTH ->{
                binding.iinEt.error = getString(R.string.incorrect_iin)
            }
            WRONG_IIN ->{
                binding.iinEt.error = getString(R.string.iin_does_not_exist)
            }
            CORRECT_IIN ->{
                binding.iinEt.error = null
                binding.iinEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_baseline_check_24), null)
            }
        }
    }


}