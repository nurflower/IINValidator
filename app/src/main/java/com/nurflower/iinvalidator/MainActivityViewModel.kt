package com.nurflower.iinvalidator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File.separator
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val EMPTY = 100
const val WRONG_LENGTH = 101
const val WRONG_IIN = 102
const val CORRECT_IIN = 103

class MainActivityViewModel : ViewModel(){

    var iin = MutableLiveData<String>()

    private var _iinStatus = MutableLiveData<Int>()
    val iinStatus:LiveData<Int> get() = _iinStatus

    /** Method for button press **/
    fun onCheckClick(){
        when {
            iin.value.isNullOrEmpty() -> {
                _iinStatus.value = EMPTY
            }
            iin.value.toString().length < 12 -> {
                _iinStatus.value = WRONG_LENGTH
            }
            else -> {
                if (checkIIN()){
                    _iinStatus.value = CORRECT_IIN
                }else{
                    _iinStatus.value = WRONG_IIN
                }
            }
        }
    }

   /** Method for IIN check **/
    private fun checkIIN():Boolean{
        val intList = mutableListOf<Int>()
        var correct = true

        iin.value?.forEach { char ->
            intList.add(Integer.parseInt(char.toString()))
        }

        if (!dateIsValid(intList)) correct = false
        if (!lastNumberIsValid(intList)) correct = false
        if (!centuryIsValid(intList[6])) correct = false

        return correct
    }

    /** Method for century check **/
    private fun centuryIsValid(century: Int) = century in 1..6


    /** Method for IIN's last number check **/
    private fun lastNumberIsValid(iin: List<Int>): Boolean {
        var sum = 0
        for (i in 0..10) {
            sum += (i + 1) * iin[i]
        }
        var remain = sum % 11

        if (remain == 10) {
            sum = 0
            for (i in 0..10) {
                var t = (i + 3) % 11
                if (t == 0) {
                    t = 11
                }
                sum += t * iin[i]
            }
            remain = sum % 11
            return if (remain == 10) false else remain == iin[11]
        }
        return remain == iin[11]
    }


    /** Method for date check **/
    private fun dateIsValid(iin: List<Int>): Boolean {
        var century = 0
        when {
            iin[6] == 1 || iin[6] == 2 -> {
                century = 18
            }
            iin[6] == 3 || iin[6] == 4 -> {
                century = 19
            }
            iin[6] == 5 || iin[6] == 6 -> {
                century = 20
            }
        }

        val date = (century.toString().plus(iin.subList(0,6).joinToString("")))

        val format: DateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        format.isLenient = false

        return try {
            format.parse(date)
            true
        } catch (e: ParseException) {
            false
        }
    }

}