package com.limboooo.contactrecorder.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.ItemContactListBinding

class ContactListItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var binding: ItemContactListBinding

    init {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_contact_list, this, false)
        binding = ItemContactListBinding.bind(itemView)
    }

    /**
     * 需要考虑用户记录的时候
     */
    fun setDate(name: String, received: Int, gave: Int) {
        binding.relativesName.text = name
        binding.receivedMoney.text = if (received != 0) "收到了${received}元" else ""
        binding.gaveMoney.text = if (gave != 0) "给了${gave}元" else ""
    }

}