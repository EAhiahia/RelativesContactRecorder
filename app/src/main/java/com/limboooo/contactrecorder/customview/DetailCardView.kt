package com.limboooo.contactrecorder.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.CardContactDetailBinding
import com.limboooo.contactrecorder.fragment.DialogAddExchanges
import com.limboooo.contactrecorder.repository.initAnimator

class DetailCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var binding: CardContactDetailBinding

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.card_contact_detail, this, false)
        binding = CardContactDetailBinding.bind(view)
        context.obtainStyledAttributes(R.styleable.DetailCardView).apply {
            try {
                binding.cardTitle.text = getString(R.styleable.DetailCardView_title)
                    ?: throw Exception("请给设置title属性")
            } finally {
                recycle()
            }
        }
        binding.addReceivedMoney.setOnClickListener {
            navigator.push(DialogAddExchanges::class) {
                initAnimator()
                //todo 默认加载数据，其实更改targetData就可以了，那么在主页面进来的时候就要清除targetData数据
            }
        }
    }
}