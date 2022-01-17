package com.anna.mindhealth.base

import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.radiobutton.MaterialRadioButton

open class BaseFragment: Fragment() {
    protected fun getRadioSelectedItem(radioGroup: RadioGroup, view: View): String {
        val checkedRadioBtn: MaterialRadioButton? = view.findViewById(radioGroup.checkedRadioButtonId)
        return checkedRadioBtn?.text.toString()
    }

    protected fun getSelectedSpinnerItem(spinner: Spinner): String {
        return spinner.selectedItem.toString()
    }

    protected fun getSelectedCheckboxesInHashMap(checkBoxes: List<MaterialCheckBox>): HashMap<String, Boolean> {
        val selectedItems = HashMap<String, Boolean>()

        checkBoxes.forEach { checkBox ->
            selectedItems[checkBox.text.toString()] = checkBox.isChecked
        }

        return selectedItems
    }

    protected fun getSelectedCheckboxesInString(checkBoxes: List<MaterialCheckBox>): String{
        val selectedItems = ArrayList<String>()

        checkBoxes.forEach { checkBox ->
            if (checkBox.isChecked){
                selectedItems.add(checkBox.text.toString())
            }
        }

        return selectedItems.joinToString()
    }

    protected fun setSelectedSpinnerItem(spinner: Spinner, textValue: String){
        val spinnerAdapter = spinner.adapter
        for (position in 0 until spinnerAdapter.count - 1){
            if (spinnerAdapter.getItem(position) == textValue){
                spinner.setSelection(position)
            }
        }
    }

    protected fun setSelectedRadioButton(radioGroup: RadioGroup, rootView: View, textValue: String){
        val radioButtons = radioGroup.children.toList()
        radioButtons.forEach { radioButton ->
            val currentRadioBtn = rootView.findViewById<MaterialRadioButton>(radioButton.id)
            if (currentRadioBtn.text == textValue){
                currentRadioBtn.isChecked = true
            }
        }
    }

    protected fun setSelectedCheckBoxes(checkBoxes: List<MaterialCheckBox>, values: HashMap<String, Boolean>){
        checkBoxes.forEach { checkBox ->
            checkBox.isChecked = values[checkBox.text.toString()] == true
        }
    }

}