package tw.edu.ntub.imd.birc.rxandmvvm.extension

import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ItemAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem

fun AdapterItem.asAdapter() = ItemAdapter.ofSingleItem(this)
