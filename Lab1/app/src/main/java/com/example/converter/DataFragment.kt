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


class DataFragment : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: FragmentDataBinding

    var fromUnit = "meter"
    var toUnit = "meter"

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



        //--------------------------------TODO BUTTONCLICK
        //ограничение на количество вводимых чисел
        dataModel.on_click.observe(activity as LifecycleOwner) {
            if (edittext.isFocused() && binding.editTextTextPersonName.text.length < 17) {
                index = edittext.getSelectionStart();
                edittext.text.insert(index, it)
                edittext.setSelection(index + 1)

                Update()
                //Log.d("(!)", "${edittext.text}   $index  bt1")
            }

        }
        //------------------------------TODO SPECIAL BUTTON
        dataModel.point_event.observe(activity as LifecycleOwner) {
            if (edittext.isFocused() && !binding.editTextTextPersonName.text.isEmpty()) {//TODO + проверка на выход из диапазона
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
        dataModel.back_event.observe(activity as LifecycleOwner) {
            index = edittext.getSelectionStart();
            if(edittext.isFocused() && !binding.editTextTextPersonName.text.isEmpty() && index!=0) {
                //TODO Допилить проверку на выход за границы
                text = edittext.getText().toString();
                text = text.substring(0, index - 1) + text.substring(index);

                edittext.setText(text);
                edittext.setSelection(index - 1)

            }
            Update()
        }

        binding.btSwap.setOnClickListener{
            edittext.setText(edittext2.text.toString())
            Update()
        }


        //----------------------------------TODO COPY\Paste
        binding.btCopy.setOnClickListener{
            val textToCopy = edittext.text

            val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text",textToCopy)
            clipboardManager.setPrimaryClip(clipData)


        }
        binding.btCopy2.setOnClickListener{
            val textToCopy = edittext2.text

            val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text",textToCopy)
            clipboardManager.setPrimaryClip(clipData)


        }
        binding.btPaste.setOnClickListener{
            val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            binding.editTextTextPersonName.setText(clipboardManager.primaryClip?.getItemAt(0)?.text)
            Update()
        }

        //---------------------------------------TODO CATEGOTY
        var array_data = resources.getStringArray(R.array.Distance)
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



    fun Update(){
        //TODO логика категорий????
        if(!binding.editTextTextPersonName.text.isEmpty() ) {
            var temp = binding.editTextTextPersonName.text.toString()
                .toBigDecimal() * (b[toUnit].toString().toBigDecimal() / b[fromUnit].toString().toBigDecimal())
            binding.editTextTextPersonName2.setText(temp.toString())
        }
        else if(binding.editTextTextPersonName.text.isEmpty()) binding.editTextTextPersonName2.text.clear()
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

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                fromUnit = languages[position]
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.selected_item) + " " +
                            "" + languages[position], Toast.LENGTH_SHORT
                ).show()
                Update()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        spinner2.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                toUnit = languages[position]
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.selected_item) + " " +
                            "" + languages[position], Toast.LENGTH_SHORT
                ).show()
                Update()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }


}

