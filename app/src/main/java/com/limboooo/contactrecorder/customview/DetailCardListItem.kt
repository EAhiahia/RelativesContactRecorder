package com.limboooo.contactrecorder.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.ItemContactDetailCardBinding

class DetailCardListItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

     private val binding: ItemContactDetailCardBinding

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_contact_detail_card, this, false)
        binding = ItemContactDetailCardBinding.bind(view)
    }

    fun setData(thing: String, money: Int, checked: Boolean) {
        binding.thing.text = thing
        binding.money.text = money.toString()
        binding.state.isChecked = checked
    }

    fun setOnDeleteClicked(onClick: () -> Unit) {
        binding.delete.performClick()
        onClick.invoke()
    }

    fun setOnCheckBoxClicked(onClick: () -> Unit) {
        binding.state.performClick()
        onClick.invoke()
    }

}