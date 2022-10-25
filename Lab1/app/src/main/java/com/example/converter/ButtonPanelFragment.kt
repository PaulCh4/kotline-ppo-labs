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
            dataModel.on_click.value = "1"
        }
        binding.bt2.setOnClickListener {
            dataModel.on_click.value = "2"
        }
        binding.bt3.setOnClickListener {
            dataModel.on_click.value = "3"
        }
        binding.bt4.setOnClickListener {
            dataModel.on_click.value = "4"
        }
        binding.bt5.setOnClickListener {
            dataModel.on_click.value = "5"
        }
        binding.bt6.setOnClickListener {
            dataModel.on_click.value = "6"
        }
        binding.bt7.setOnClickListener {
            dataModel.on_click.value = "7"
        }
        binding.bt8.setOnClickListener {
            dataModel.on_click.value = "8"
        }
        binding.bt9.setOnClickListener {
            dataModel.on_click.value = "9"
        }
        binding.btBack.setOnClickListener {
            dataModel.back_event.value = "back"
        }
        binding.btPoint.setOnClickListener {
            dataModel.point_event.value = "."
        }
        binding.bt0.setOnClickListener {
            dataModel.zero_event.value = "0"
        }


    }

    companion object {
        @JvmStatic
        fun newInstance() = ButtonPanelFragment()
    }



}