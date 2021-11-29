package com.jukti.code4lyst.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.jukti.code4lyst.R
import com.jukti.code4lyst.domain.model.BreedDomainModel
import kotlin.properties.Delegates

class BreedAdapter(context: Context) : ArrayAdapter<BreedDomainModel>(context,0){

    internal var collection: MutableList<BreedDomainModel> by Delegates.observable(mutableListOf()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val breed = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.breed_spinner_item, parent, false)
        (view?.findViewById(R.id.breed_name_tv) as TextView).text = breed.name
        return view
    }

    override fun getCount(): Int {
        return collection.size
    }

    override fun getItem(position: Int): BreedDomainModel {
        return collection.get(position)
    }
}