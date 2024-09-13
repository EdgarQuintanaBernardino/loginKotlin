package com.login.classes

import android.annotation.SuppressLint
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.quics.login.R
import android.content.Context
import android.widget.Button


open class validateInput(private var label: TextView, private var input: EditText,private var validates:Map<String,String>, private var ctx: Context) {
    fun validateEmail():Boolean{
        if(input.text.isEmpty()||!EmailValidation.isEmail(input.text.toString())){
            if(input.text.isEmpty()){
                label.text=ctx.getString(R.string.msg_error_email_empty);
                label.setTextColor(ContextCompat.getColor(ctx, R.color.red))

            }else{
                    label.text=ctx.getString(R.string.msg_error_email_invalid);
                    label.setTextColor(ContextCompat.getColor(ctx, R.color.red))

            }

            return false;
        }else{
            label.text=ctx.getString(R.string.email_valid_sucess);
            label.setTextColor(ContextCompat.getColor(ctx, R.color.green))
            return true;
        }
    }
    private fun validateEmail_():Boolean{
       return !(input.text.isEmpty()||!EmailValidation.isEmail(input.text.toString()));
    }
    private fun validateMin_(min:Int):Boolean{
        return input.text.length>=min;
    }
    private fun validateMax_(max:Int):Boolean{
        return input.text.length<max;
    }
    private fun required_(): Boolean {
        return input.text.isNotEmpty();
    }
    private fun validateMin(min:Int):Boolean{
        if(input.text.length<min){
            label.text=ctx.getString(R.string.validate_form_min).plus(min.toString());
            label.setTextColor(ContextCompat.getColor(ctx, R.color.red))
            return false;
        }else{

            return true;
        }

    }
    fun validateMax(max:Int):Boolean{
        if(input.text.length>max){
            label.text=ctx.getString(R.string.validate_form_max).plus(max.toString());
            label.setTextColor(ContextCompat.getColor(ctx, R.color.red));

            return false;
        }else{

            return true;
        }
    }
    fun required():Boolean{
        if(input.text.isEmpty()){
            label.text=ctx.getString(R.string.validate_form_required);
            label.setTextColor(ContextCompat.getColor(ctx, R.color.red))
            return false;
        }else{

            return true;
        }
    }
    open fun validate():Boolean{
        var validations:MutableList<Boolean> = mutableListOf();

        for ((key, value) in validates) {
            if(key== "required"){
                  validations.add(required());
            }
            if(key== "min"){
                validations.add(validateMin(value.toInt()));
            }
            if(key== "max"){
                var response=validateMax(value.toInt());
                validations.add(response);

            }

            if(key== "email"){


                validations.add(validateEmail());
            }
        }
        if(!validations.contains(false)){
            label.text=ctx.getString(R.string.password_valid);
            label.setTextColor(ContextCompat.getColor(ctx, R.color.green))
        }
        return !validations.contains(false);

    }
    open fun validate_():Boolean{
        var validations:MutableList<Boolean> = mutableListOf();
        for ((key, value) in validates) {

            if(key== "min"){

                validations.add(validateMin_(value.toInt()));
            }
            if(key== "max"){
                validations.add(validateMax_(value.toInt()));
            }
            if(key== "required"){
               validations.add(required_());
            }
            if(key== "email"){
               validations.add(validateEmail_());
            }
        }

        return !validations.contains(false);

    }

    @SuppressLint("SetTextI18n")
    fun reset(){
        label.text="";
        input.text.clear();

    }

}

class formRegister(private var inputs: Array<validateInput>,private var btn:Button,private var ctx: Context){
    fun resetForm(){
        inputs.forEach{
          item->item.reset();
      }
    }
    fun validateForm(){
        val validations:MutableList<Boolean> = mutableListOf();

        inputs.forEach{
            item->

            validations.add(item.validate_());



        }
        btn.isEnabled=!validations.contains(false);
        if(validations.contains(false)){
            btn.setBackgroundColor(ContextCompat.getColor(ctx, R.color.btn_disabled))
        }else{
            btn.setBackgroundColor(ContextCompat.getColor(ctx, R.color.orange))

        }


    }
}
class confirm(private var label: TextView, private var input: EditText,private var validates:Map<String,String>, private var ctx: Context,private var confirm: EditText): validateInput(label,input,validates,ctx) {

    override fun validate(): Boolean {

        if (input.text.toString() == confirm.text.toString()&& super.validate_()) {
            label.text = ctx.getString(R.string.inputsEquals);
            label.setTextColor(ContextCompat.getColor(ctx, R.color.green))
            return true;
        }else{
            label.text = ctx.getString(R.string.inputsNoEquals);
            label.setTextColor(ContextCompat.getColor(ctx, R.color.red))
            return false;
        }

    }
    override fun validate_(): Boolean{
        return input.text.toString() == confirm.text.toString();

    }
}