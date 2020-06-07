package com.absinthe.libchecker.recyclerview

import android.text.format.Formatter
import android.view.View
import com.absinthe.libchecker.R
import com.absinthe.libchecker.bean.LibStringItem
import com.absinthe.libchecker.constant.GlobalValues
import com.absinthe.libchecker.constant.librarymap.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.chip.Chip

class LibStringAdapter : BaseQuickAdapter<LibStringItem, BaseViewHolder>(R.layout.item_lib_string) {

    var mode = Mode.NATIVE

    init {
        addChildClickViewIds(R.id.chip)
    }

    override fun convert(holder: BaseViewHolder, item: LibStringItem) {
        holder.setText(R.id.tv_name, item.name)
        if (item.size != 0L) {
            holder.setText(R.id.tv_lib_size, sizeToString(item.size))
            holder.setGone(R.id.tv_lib_size, false)
        } else {
            holder.setGone(R.id.tv_lib_size, true)
        }

        val libIcon = holder.getView<Chip>(R.id.chip)

        val map: BaseMap = when (mode) {
            Mode.NATIVE -> NativeLibMap
            Mode.SERVICE -> ServiceLibMap
            Mode.ACTIVITY -> ActivityLibMap
            Mode.RECEIVER -> ReceiverLibMap
            Mode.PROVIDER -> ProviderLibMap
        }

        map.getChip(item.name)?.let {
            libIcon.apply {
                setChipIconResource(it.iconRes)
                text = it.name
                visibility = View.VISIBLE

                if (GlobalValues.isColorfulIcon.value!!) {
                    setChipIconTintResource(R.color.textNormal)
                }
            }
        } ?: let {
            libIcon.visibility = View.GONE
        }
    }

    private fun sizeToString(size: Long): String {
        return "(${Formatter.formatFileSize(context, size)})"
    }

    enum class Mode {
        NATIVE,
        SERVICE,
        ACTIVITY,
        RECEIVER,
        PROVIDER
    }
}