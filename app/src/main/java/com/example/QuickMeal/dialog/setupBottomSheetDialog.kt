package com.example.QuickMeal.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.QuickMeal.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


fun Fragment.setupBottomSheetDialog(
    onSendClick:(String)->Unit,

    ){
    val dialogue = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view=layoutInflater.inflate(R.layout.reset_password_dialog,null)
    dialogue.setContentView(view)
    dialogue.behavior.state= BottomSheetBehavior.STATE_EXPANDED
    dialogue.show()


    val edemail=view.findViewById<EditText>(R.id.editText)
    val submit =view.findViewById<Button>(R.id.send)
    val cancel =view.findViewById<Button>(R.id.cancel)
    submit.setOnClickListener {
        val email =edemail.text.toString().trim()
        onSendClick(email)
        dialogue.dismiss()
    }
    cancel.setOnClickListener {
        dialogue.dismiss()
    }
}