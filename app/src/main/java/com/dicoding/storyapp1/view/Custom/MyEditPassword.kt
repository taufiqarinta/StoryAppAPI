package com.dicoding.storyapp1.view.Custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEditPassword : AppCompatEditText{

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

      fun init(){

          addTextChangedListener(object : TextWatcher{
              override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

              }

              override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                  if (s.toString().length < 8) {
                      setError("Password tidak boleh kurang dari 8 karakter", null)
                  } else {
                      error = null
                  }
              }

              override fun afterTextChanged(s: Editable?) {

              }

          })
    }

}