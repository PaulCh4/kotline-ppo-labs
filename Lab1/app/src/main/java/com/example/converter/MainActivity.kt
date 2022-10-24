package com.example.converter

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.converter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var index = 0
    var text:String = "empty"
    var edittext_number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val edittext =  binding.editTextTextPersonName
        val edittext2 =  binding.editTextTextPersonName2
        edittext.showSoftInputOnFocus = false
        edittext.disableCopyPaste()
        edittext2.showSoftInputOnFocus = false
        edittext2.disableCopyPaste()



        //EDIT ФОРМОЧКИ
        //TODO Получать информацию о том, где сейчас находится курсор(?)
        edittext.setOnClickListener(){
            edittext.setFocusable(true)
            index = edittext.getSelectionStart();
            binding.tv1.text = index.toString()

            edittext_number = 1
            Log.d("(!)","${edittext.text}   $index  bt1")

        }
        edittext2.setOnClickListener(){
            index = edittext.getSelectionStart();
            binding.tv2.text = index.toString()

            edittext_number = 2
        }





        //ЦИФЕРКИ
        //TODO присвоить один onClick всем циферкам

        binding.bt1.setOnClickListener {

            if(edittext.isFocused()) {
                index = edittext.getSelectionStart();
                edittext.text.insert(index, binding.bt1.text)
                edittext.setSelection(index+1)

                Log.d("(!)","${edittext.text}   $index  bt1")
            }
            else if(edittext2.isFocused()){
                index = edittext2.getSelectionStart();
                edittext2.text.insert(index, binding.bt1.text)
                edittext2.setSelection(index+1)

                Log.d("(!)","${edittext2.text}   $index  bt1")
            }

        }


        binding.bt2.setOnClickListener {
            if(edittext.isFocused()) {
                index = edittext.getSelectionStart();
                edittext.text.insert(index, binding.bt2.text)
                edittext.setSelection(index+1)

                Log.d("(!)","${edittext.text}   $index  bt2")
            }
            else if(edittext2.isFocused()){
                index = edittext2.getSelectionStart();
                edittext2.text.insert(index, binding.bt2.text)
                edittext2.setSelection(index+1)

                Log.d("(!)","${edittext2.text}   $index  bt2")
            }
        }






        //DELETE BUTTON
        binding.btBack.setOnClickListener {
            if(edittext.isFocused()) {
                index = edittext.getSelectionStart();
                //TODO Допилить проверку на выход за границы
                text = edittext.getText().toString();
                text = text.substring(0, index - 1) + text.substring(index);

                edittext.setText(text);
                Log.d("(!)", "${edittext.text}   $index   Back")
                edittext.setSelection(index - 1)
            }
            else if(edittext2.isFocused()){
                index = edittext2.getSelectionStart();
                //TODO Допилить проверку на выход за границы
                text = edittext2.getText().toString();
                text = text.substring(0, index - 1) + text.substring(index);

                edittext2.setText(text);
                Log.d("(!)", "${edittext2.text}   $index   Back")
                edittext2.setSelection(index - 1)
            }
        }
    }





    fun TextView.disableCopyPaste() {
        isLongClickable = false
        setTextIsSelectable(false)
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu) = false
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu) = false
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem) = false
            override fun onDestroyActionMode(mode: ActionMode?) {}
        }
        //disable action mode when edittext gain focus at first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            customInsertionActionModeCallback = object : ActionMode.Callback {
                override fun onCreateActionMode(mode: ActionMode?, menu: Menu) = false
                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu) = false
                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem) = false
                override fun onDestroyActionMode(mode: ActionMode?) {}
            }
        }
    }
}