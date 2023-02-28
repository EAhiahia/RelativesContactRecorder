package com.limboooo.contactrecorder.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.CardContactDetailBinding

class ContactDetailCardView(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {

    private val binding: CardContactDetailBinding

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.card_contact_detail, this, false)
        binding = CardContactDetailBinding.bind(view)
    }

}