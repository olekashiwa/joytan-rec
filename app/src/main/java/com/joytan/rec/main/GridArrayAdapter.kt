package com.joytan.rec.main

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.joytan.rec.R

class GridBaseAdapter(private val context: Context,
                      private val mainScripts: MutableList<String>,
                      private val clientProgress: MutableList<Int>?,
                      private val adminProgress: MutableList<Int>?) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val gridItem : View
        if (convertView == null) {
            gridItem = View.inflate(context, R.layout.grid_item, null)
        } else {
            gridItem = convertView
        }
        val textView = gridItem.findViewById<TextView>(R.id.grid_text)
        val idView = gridItem.findViewById<TextView>(R.id.grid_id)


//        Log.e(MainActivity.DEBUG_TAG, "$position in ${clientProgress!!} ${adminProgress} ?")
//        Log.e(MainActivity.DEBUG_TAG, clientProgress.indexOf(position).toString())
//        Log.e(MainActivity.DEBUG_TAG, "${position.javaClass} ${clientProgress[0].javaClass} ${clientProgress.javaClass}")
//
//        Log.e(MainActivity.DEBUG_TAG, "$position == ${clientProgress[0]} = " + (position == clientProgress[0]).toString())

//        if (adminProgress!!.filter { it == position }.any()) {
//            gridItem.setBackgroundColor(ContextCompat.getColor(context, R.color.bs_green))
//        }
//        else if (clientProgress!!.filter { it == position}.any()) {
////            Log.e(MainActivity.DEBUG_TAG, "currentIndex in clientProgress")
//            gridItem.setBackgroundColor(ContextCompat.getColor(context, R.color.bs_blue))
//        }
        if (position in adminProgress!!) {
            gridItem.setBackgroundColor(ContextCompat.getColor(context, R.color.bs_green))
        }
        else if (position in clientProgress!!) {
            gridItem.setBackgroundColor(ContextCompat.getColor(context, R.color.bs_blue))
        }
        else {
            gridItem.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_dark))
        }
        textView.setText(mainScripts[position])
        idView.setText((position + 1).toString())

        return gridItem
    }

    override fun getItem(p0: Int): Any {
        return 0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }


    override fun getCount(): Int {
        return mainScripts.size
    }
}

class GridArrayAdapter(context: Context,
                       private val layoutResource: Int,
                       private val values: List<Int>,
                       private val mainScripts: MutableList<String>,
                       private val progressDB: MutableMap<String, MutableList<Int>>,
                       private val currentDirname: String) : ArrayAdapter<Int>(
        context, layoutResource, values) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view as TextView

        val currentIndex = textView.text.toString().toInt() - 1

        if(currentIndex in progressDB[currentDirname]!!) {
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.play))
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_dark))
        }
        textView.setText(mainScripts[currentIndex])

        return textView
    }
}
