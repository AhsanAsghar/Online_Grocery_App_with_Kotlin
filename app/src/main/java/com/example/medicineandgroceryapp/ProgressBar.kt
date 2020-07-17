package com.example.medicineandgroceryapp

import android.R.style
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.view.isVisible
import com.example.medicineandgroceryapp.R

class ProgressBar
{
    private var acitivity : Activity? = null
    private var dialog: Dialog? = null

    constructor(activity: Activity){
        this.acitivity = activity
        this.dialog = Dialog(this.acitivity!!, style.TextAppearance_Theme_Dialog)
    }

    fun startLoading(cancelable:Boolean,title : String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(acitivity)
        val view = this.acitivity?.layoutInflater?.inflate(R.layout.progress_bar,null)
        view?.findViewById<TextView>(R.id.text_progress_bar)?.setText((title))
        dialog?.setContentView(view!!)
        dialog?.setCancelable(cancelable)
        dialog?.show()
    }
    fun dismissDialog(){
        dialog?.dismiss()
    }

}