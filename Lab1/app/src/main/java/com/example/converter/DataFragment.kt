package com.example.converter

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.converter.databinding.FragmentButtonPanelBinding
import com.example.converter.databinding.FragmentDataBinding
import com.example.testfragmentview.DataModel


class DataFragment : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: FragmentDataBinding

    var index = 0
    var text:String = "empty"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDataBinding.inflate(inflater)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val edittext =  binding.editTextTextPersonName
        val edittext2 =  binding.editTextTextPersonName2
        edittext.showSoftInputOnFocus = false
        edittext.disableCopyPaste()
        edittext2.showSoftInputOnFocus = false
        edittext2.disableCopyPaste()



        //TODO BUTTONCLICK
        //ограничение на количество вводимых чисел
        dataModel.on_click.observe(activity as LifecycleOwner) {
            if (edittext.isFocused()) {
                index = edittext.getSelectionStart();
                edittext.text.insert(index, it)
                edittext.setSelection(index + 1)

                Update()
                //Log.d("(!)", "${edittext.text}   $index  bt1")
            }

        }
        dataModel.point_event.observe(activity as LifecycleOwner) {
            if (edittext.isFocused()) {//TODO + проверка на выход из диапазона
                index = edittext.getSelectionStart();
                edittext.setText(edittext.text.toString().replace("[.]".toRegex(), " "))

                edittext.text.insert(index, it)
                edittext.setSelection(index + 1)
                edittext.setText(edittext.text.toString().replace("[ ]".toRegex(), ""))

                edittext.setSelection(edittext.text.indexOf('.') + 1)

                Update()
                Log.d("(!)", "${edittext.text}   $index  bt1")
            }


        }
        dataModel.zero_event.observe(activity as LifecycleOwner) {
            if (edittext.isFocused()) {//ToDO проверка бесконечные нули

                index = edittext.getSelectionStart();
                edittext.text.insert(index, it)
                edittext.setSelection(index + 1)

                Update()
                //Log.d("(!)", "${edittext.text}   $index  bt1")
            }

        }


        //TODO DELETE
        dataModel.back_event.observe(activity as LifecycleOwner) {
            if(edittext.isFocused()) {
                index = edittext.getSelectionStart();
                //TODO Допилить проверку на выход за границы
                text = edittext.getText().toString();
                text = text.substring(0, index - 1) + text.substring(index);

                edittext.setText(text);
                edittext.setSelection(index - 1)

                Update()
            }
        }

    }

    fun Update(){
        //TODO логика категорий????
        var temp = binding.editTextTextPersonName.text.toString().toDouble() *  0.62137119224
        binding.editTextTextPersonName2.setText(temp.toString())
    }
    fun Swap_OnClick(){}//TODO
    fun Copy_OnClick(){}//TODO
    fun Paste_OnClick(){}//TODO




















    companion object {
        @JvmStatic
        fun newInstance() = DataFragment()
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