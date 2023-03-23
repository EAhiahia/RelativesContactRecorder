package com.limboooo.contactrecorder.customview

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.CardContactDetailBinding

class DetailCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : MaterialCardView(context, attrs) {

    var binding: CardContactDetailBinding

    init {
        val view = inflate(context, R.layout.card_contact_detail, this)
        binding = CardContactDetailBinding.bind(view)
        context.obtainStyledAttributes(attrs, R.styleable.DetailCardView).apply {
            try {
                binding.cardTitle.text = getString(R.styleable.DetailCardView_title)
                    ?: throw Exception("请给设置title属性")
            } finally {
                recycle()
            }
        }
    }
}