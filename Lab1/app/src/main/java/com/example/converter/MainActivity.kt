package com.example.converter

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.converter.databinding.ActivityMainBinding
import com.example.testfragmentview.DataModel


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private  val dataModel: DataModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //---------------
        supportFragmentManager.beginTransaction().replace(R.id.frButtonPanel, ButtonPanelFragment()).commit()
        supportFragmentManager.beginTransaction().replace(R.id.frDataPanel, DataFragment()).commit()

        //dataModel.message.observe(this,{
        //    it
        //})
        //--------------



    }






}