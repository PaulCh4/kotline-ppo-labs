package com.example.converter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.converter.databinding.FragmentButtonPanelBinding
import com.example.converter.databinding.FragmentDataBinding
import com.example.testfragmentview.DataModel

class ButtonPanelFragment : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    lateinit var binding:FragmentButtonPanelBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentButtonPanelBinding.inflate(inflater)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bt1.setOnClickListener {
            dataModel.bt1_data.value = "1"
        }
        binding.bt2.setOnClickListener {
            dataModel.bt2_data.value = "2"
        }
        binding.btBack.setOnClickListener {
            dataModel.bt_back.value = ""
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ButtonPanelFragment()
    }


}