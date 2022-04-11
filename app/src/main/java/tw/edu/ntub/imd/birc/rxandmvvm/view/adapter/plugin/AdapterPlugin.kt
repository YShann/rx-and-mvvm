package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.plugin

import android.view.View
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem

interface AdapterPlugin {
    fun onAttachToAdapter(adapter: Adapter)

    fun bindView(adapter: Adapter, item: AdapterItem, view: View, index: Int)

    fun updateView(adapter: Adapter, item: AdapterItem, view: View, index: Int)

    fun onClick(adapter: Adapter, item: AdapterItem, view: View, index: Int)
}