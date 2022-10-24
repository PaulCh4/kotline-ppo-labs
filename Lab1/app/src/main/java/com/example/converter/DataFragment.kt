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
    var edittext_number: Int = 0
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




        dataModel.bt1_data.observe(activity as LifecycleOwner) {
            if (edittext.isFocused()) {
                index = edittext.getSelectionStart();
                edittext.text.insert(index, it)
                edittext.setSelection(index + 1)

                Log.d("(!)", "${edittext.text}   $index  bt1")
            } else if (edittext2.isFocused()) {
                index = edittext2.getSelectionStart();
                edittext2.text.insert(index, it)
                edittext2.setSelection(index + 1)

                Log.d("(!)", "${edittext2.text}   $index  bt1")
            }
        }
        dataModel.bt2_data.observe(activity as LifecycleOwner) {
            if(edittext.isFocused()) {
                index = edittext.getSelectionStart();
                edittext.text.insert(index, it)
                edittext.setSelection(index+1)

                Log.d("(!)","${edittext.text}   $index  bt2")
            }
            else if(edittext2.isFocused()){
                index = edittext2.getSelectionStart();
                edittext2.text.insert(index, it)
                edittext2.setSelection(index+1)

                Log.d("(!)","${edittext2.text}   $index  bt2")
            }
        }











        fun Delete(){
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
        dataModel.bt_back.observe(activity as LifecycleOwner) {
            Delete()
        }
    }





















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