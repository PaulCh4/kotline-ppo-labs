package com.example.converter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.example.converter.databinding.FragmentDataBinding
import com.example.testfragmentview.DataModel
import java.math.BigDecimal


class DataFragment : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: FragmentDataBinding
    private var SHORT_DELAY = 10

    var fromUnit = "meter"
    var toUnit = "meter"

    var index = 0
    var point_index = 1
    var text:String = "empty"

    val b = mapOf(
        "meter" to 1.00,
        "foot" to 1.09,
        "yard" to 3.28,

        "bit" to 1.00,
        "byte" to 0.125,
        "kilobyte" to 0.000125,

        "kilogram" to 1.00,
        "оunce" to 35.274,
        "pound" to 2.20462
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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


        //--------------------------------TODO BUTTON
        dataModel.on_click.observe(activity as LifecycleOwner) {
           /*
                сли точка есть
            66666.6\ запомнить позицию точки
            66666 6\ удалить
            666666 \ очистить пробел
            666666\ переставить

            666666\7 вывести цифру
            66666.67 вернуть точку на позицию назад от той, что была

            6666666
            */



            if (edittext.text.length < 16 || (edittext.text.length == 16 && "." in edittext.text.toString())) {

                if(edittext.text.toString() == "0") edittext.text.clear()
                index = edittext.selectionStart
                edittext.text.insert(index, it)

                edittext.setSelection(index + 1)
                Update()

                Log.d("(!)", "${edittext.text.toString()}   $index $point_index")
            }
            else Toast.makeText(requireActivity(), "Достигнуто максимальное количество символов", SHORT_DELAY).show()

        }

        //------------------------------TODO SPECIAL BUTTON
        dataModel.zero_event.observe(activity as LifecycleOwner) {

            if (edittext.text.length < 16 && edittext.text.toString() != "0" ){//ToDO проверка бесконечные нули//

                index = edittext.selectionStart
                edittext.text.insert(index, it)
                edittext.setSelection(index + 1)
                Update()
            }
            else if(edittext.text.length >= 16) Toast.makeText(requireActivity(), "Достигнуто максимальное количество символов", SHORT_DELAY).show()


            /*
            if (edittext.text.length < 16 && edittext.text.toString() != "0" ){//ToDO проверка бесконечные нули//

                index = edittext.selectionStart

                if (edittext.text.replace("^0+(?!$)".toRegex(), "") == edittext.text.toString() || "." in edittext.text) {

                    edittext.text.insert(index, it)
                    edittext.setSelection(index + 1)

                    if(!(edittext.text.replace("^0+(?!$)".toRegex(), "") == edittext.text.toString())) {

                        edittext.setText(edittext.text.replace("^0+(?!$)".toRegex(), ""))
                        edittext.setSelection(index)
                    }
                }
                Update()
            }
            else if(edittext.text.length >= 16) Toast.makeText(requireActivity(), "Достигнуто максимальное количество символов", Toast.LENGTH_SHORT).show()
            */
        }
        dataModel.point_event.observe(activity as LifecycleOwner) {
            index = edittext.selectionStart

            if (edittext.text.isNotEmpty() && index < 17 && !("." in edittext.text.toString())) {//TODO + проверка на выход из диапазона


                edittext.setSelection(index)

                if (edittext.text.isNotEmpty() && index < 16) {
                    edittext.setText(edittext.text.toString().replace("[.]".toRegex(), " "))
                    edittext.text.insert(index, it)
                    edittext.setSelection(index + 1)
                    edittext.setText(edittext.text.toString().replace("[ ]".toRegex(), ""))
                    edittext.setSelection(edittext.text.indexOf('.') + 1)

                    point_index = index

                    Update()
                }
                else Toast.makeText(requireActivity(), "Достигнуто максимальное количество символов", SHORT_DELAY).show()

            }
            else if("." in edittext.text.toString()) Toast.makeText(requireActivity(), "Точка уже стоит", SHORT_DELAY).show()
            else Toast.makeText(requireActivity(), "Достигнуто максимальное количество символов", SHORT_DELAY).show()

            if(edittext.text.isEmpty()) Toast.makeText(requireActivity(), "Требуется что-то ввести", SHORT_DELAY).show()



        }
        dataModel.back_event.observe(activity as LifecycleOwner) {

            index = edittext.selectionStart

            Log.d("(!)","${edittext.text.toString()}   position: $index ${binding.editTextTextPersonName.text.isEmpty()}")

            if(edittext.text.isNotEmpty() && index != 0) {

                text = edittext.text.toString()
                text = text.substring(0, index - 1) + text.substring(index)

                edittext.setText(text)
                edittext.setSelection(index - 1)

                Update()
            }
            else Toast.makeText(requireActivity(), "Удалять нечего :)", SHORT_DELAY).show()

        }

        //----------------------------------TODO COPY\Paste\Swap
        binding.btCopy.setOnClickListener{

                val textToCopy = edittext.text

                val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text",textToCopy)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(requireActivity(), "скопировано в буфер обмена", SHORT_DELAY).show()

        }
        binding.btCopy2.setOnClickListener{

                val textToCopy = edittext2.text

                val clipboardManager =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", textToCopy)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(requireActivity(), "скопировано в буфер обмена", SHORT_DELAY).show()

        }
        binding.btPaste.setOnClickListener {
            val clipboardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val pasteText = clipboardManager.primaryClip?.getItemAt(0)?.text
            if (pasteText.toString().length <= 17 && isNumeric(pasteText.toString())){
                edittext.setText(pasteText)
                Toast.makeText(requireActivity(), "вставка выполнена",SHORT_DELAY).show()
            }
            if (pasteText!!.isEmpty()) Toast.makeText(requireActivity(), "вставка выполнена",SHORT_DELAY).show()
            else Toast.makeText(requireActivity(), "достигнуто максимальное количество символов", SHORT_DELAY).show()
            Update()
        }
        binding.btSwap.setOnClickListener {
            if (edittext2.text.toString().length <= 17){

                edittext.setText(edittext2.text.toString())
                Update()
            }
            else Toast.makeText(requireActivity(), "достигнуто максимальное количество символов", SHORT_DELAY).show()

        }
        binding.btPrem.setOnClickListener{

            binding.btCopy.isEnabled = true
            binding.btCopy2.isEnabled = true
            binding.btPaste.isEnabled = true
            binding.btSwap.isEnabled = true

            binding.btPrem.isVisible = false
            binding.textView.isVisible = false
            binding.imageView5.isVisible = true

            binding.imblock1.isVisible = false
            binding.imblock2.isVisible = false
            binding.imblock3.isVisible = false

            Toast.makeText(requireActivity(), "добро пожаловать!", SHORT_DELAY).show()

        }
        binding.imageView5.setOnClickListener{
            binding.imageView4.isVisible = true

            Glide.with(this).load(R.drawable.blood).into(binding.imageView4)

        }

        //---------------------------------------TODO CATEGORY
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
        val edittext2 = binding.editTextTextPersonName2




        if(edittext.text.isNotEmpty() && edittext.text.toString() != ".") {
            val temp = edittext.text.toString()
                .toBigDecimal() * (b[toUnit].toString().toBigDecimal() / b[fromUnit].toString().toBigDecimal()) // BigDecimal(1.0000)
            binding.editTextTextPersonName2.setText(temp.toString())
        }
        else{
            binding.editTextTextPersonName.text.clear()
            binding.editTextTextPersonName2.text.clear()
        }

        if(edittext.text.isNotEmpty() && edittext.text.toString().toBigDecimal() <= BigDecimal(.0)) {

            edittext2.setText("0")
        }

        Log.d("(!)","${edittext.text.toString()}   ")
    }

    fun isNumeric(s: String): Boolean {
        return try {
            s.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
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
