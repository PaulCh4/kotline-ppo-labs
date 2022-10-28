package com.example.converter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.converter.databinding.FragmentDataBinding
import com.example.testfragmentview.DataModel
import java.math.BigDecimal


class DataFragment : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: FragmentDataBinding

    var fromUnit = "meter"
    var toUnit = "meter"

    var index = 0
    var text:String = "empty"

    val b = mapOf(
        "meter" to 1.00,
        "foot" to 1.09,
        "yard" to 3.28,

        "bit" to 1.00,
        "byte" to 0.125,
        "kilobyte" to 0.000125,

        "minute" to 60.0,
        "second" to 3600.0,
        "hour" to 1.0
    )


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

        var array_data = resources.getStringArray(R.array.Distance)


        //--------------------------------TODO BUTTONCLICK
        dataModel.on_click.observe(activity as LifecycleOwner) {

            if (edittext.isFocused() && edittext.text.length < 16) {
                if(edittext.text.toString() == "0") edittext.text.clear()
                index = edittext.getSelectionStart();
                edittext.text.insert(index, it)
                edittext.setSelection(index + 1)

                Update()
            }

        }

        //------------------------------TODO SPECIAL BUTTON
        dataModel.zero_event.observe(activity as LifecycleOwner) {

            Log.d("(!)", "${edittext.text.toString()}     ${edittext.text.replace("^0+(?!$)".toRegex(), "")}")

            if (edittext.isFocused() && edittext.text.length < 16 && edittext.text.toString() != "0"){//ToDO проверка бесконечные нули//

                index = edittext.getSelectionStart();

                if (edittext.text.replace("^0+(?!$)".toRegex(), "") == edittext.text.toString()) {

                    edittext.text.insert(index, it)
                    edittext.setSelection(index + 1)

                    edittext.setText(edittext.text.replace("^0+(?!$)".toRegex(), ""))
                    edittext.setSelection(index)
                }


                Update()
            }
            /*
            000 1.0 000
            0.0
            0
               0-> 5
            1.0
            0.1

            0000 100024.12000414 0000

            if not empty
            for(идет по строке)
                if(s[i] == 0 && s[i+1] == 0) 0x0x0x 01.
                    index +1
                else if(s[i] == 0 && s[i+1] != 0 && s[i+1]!='.')  0.0  0x1.0
                    delete s[i]
                    index +1

                if(i+1 == '.') break



             */

        }
        dataModel.point_event.observe(activity as LifecycleOwner) {
            index = edittext.getSelectionStart();

            if (edittext.isFocused() && !edittext.text.isEmpty()  && !(index >= 16)) {//TODO + проверка на выход из диапазона

                edittext.setText(edittext.text.toString().replace("[.]".toRegex(), " "))

                edittext.text.insert(index, it)
                edittext.setSelection(index + 1)
                edittext.setText(edittext.text.toString().replace("[ ]".toRegex(), ""))
                edittext.setSelection(edittext.text.indexOf('.') + 1)

                Update()
            }


        }
        dataModel.back_event.observe(activity as LifecycleOwner) {

            index = edittext.getSelectionStart();

            Log.d("(!)","${edittext.text.toString()}   position: $index ${binding.editTextTextPersonName.text.isEmpty()}")

            if(edittext.isFocused() && !edittext.text.isEmpty() && index != 0) {

                text = edittext.getText().toString();
                text = text.substring(0, index - 1) + text.substring(index);

                edittext.setText(text);
                edittext.setSelection(index - 1)

                Update()
            }
        }

        //----------------------------------TODO COPY\Paste\Swap
        binding.btCopy.setOnClickListener{

                val textToCopy = edittext.text

                val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text",textToCopy)
                clipboardManager.setPrimaryClip(clipData)

        }
        binding.btCopy2.setOnClickListener{

                val textToCopy = edittext2.text

                val clipboardManager =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", textToCopy)
                clipboardManager.setPrimaryClip(clipData)

        }
        binding.btPaste.setOnClickListener{
            val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var pasteText = clipboardManager.primaryClip?.getItemAt(0)?.text
            if(pasteText.toString().length <= 17)
                edittext.setText(pasteText)
            else Toast.makeText(requireActivity(), "НИЗЯ!", Toast.LENGTH_SHORT).show()
            Update()
        }
        binding.btSwap.setOnClickListener{
            edittext.setText(edittext2.text.toString())
            Update()
        }

        //---------------------------------------TODO CATEGOTY
        setSpinner(array_data)
        binding.btCategory1.setOnClickListener{
            array_data = resources.getStringArray(R.array.Data)
            setSpinner(array_data)
        }
        binding.btCategory2.setOnClickListener{
            array_data = resources.getStringArray(R.array.Distance)
            setSpinner(array_data)
        }
        binding.btCategory3.setOnClickListener{
            array_data = resources.getStringArray(R.array.Time)
            setSpinner(array_data)
        }


    }

    //--------------------------TODO UPDATE
    fun Update(){
        val edittext = binding.editTextTextPersonName

        if(!edittext.text.isEmpty() && edittext.text.toString() != ".") {
            var temp = edittext.text.toString()
                .toBigDecimal() * (b[toUnit].toString().toBigDecimal() / b[fromUnit].toString().toBigDecimal()) / BigDecimal(1.0000)
            binding.editTextTextPersonName2.setText(temp.toString())
        }
        else{
            binding.editTextTextPersonName.text.clear()
            binding.editTextTextPersonName2.text.clear()
        }

        Log.d("(!)","${edittext.text.toString()}   ")
    }



    //--------------------------TODO SPINNER
    fun setSpinner(languages: Array<String>) {
        val spinner = binding.spinner
        val spinner2 = binding.spinner2

        val adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item, languages
        )
        spinner.adapter = adapter
        spinner2.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                fromUnit = languages[position]
                Update()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                toUnit = languages[position]
                Update()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
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

//focus default

//тосты по любому поводу

//Меню цвета? и Vip