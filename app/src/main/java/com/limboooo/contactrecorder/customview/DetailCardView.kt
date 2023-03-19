package com.limboooo.contactrecorder.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.google.android.material.button.MaterialButton
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.CardContactDetailBinding
import com.limboooo.contactrecorder.fragment.DialogAddExchanges
import com.limboooo.contactrecorder.repository.initAnimator

class DetailCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var binding: CardContactDetailBinding

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.card_contact_detail, this, false)
        binding = CardContactDetailBinding.bind(view)
        context.obtainStyledAttributes(R.styleable.DetailCardView).apply {
            try {
                binding.cardTitle.text = getString(R.styleable.DetailCardView_title)
                    ?: throw Exception("请给carTitle属性设置内容")
            } finally {
                recycle()
            }
        }
        binding.addReceivedMoney.setOnClickListener {
            navigator.push(DialogAddExchanges::class) {
                initAnimator()

            }
        }
        binding.showPaid.setOnClickListener {
            if (binding.paidList.visibility == VISIBLE) {
                binding.paidList.visibility = GONE
                binding.showPaid.text = "显示已归还的钱"
                (binding.showPaid as MaterialButton).icon = AppCompatResources.getDrawable(context, R.drawable.ic_show)
            } else {
                binding.paidList.visibility = VISIBLE
                binding.showPaid.text = "隐藏已归还的钱"
                (binding.showPaid as MaterialButton).icon = AppCompatResources.getDrawable(context, R.drawable.ic_hide)
            }
        }
    }

    fun setListAdapter(){

    }

}