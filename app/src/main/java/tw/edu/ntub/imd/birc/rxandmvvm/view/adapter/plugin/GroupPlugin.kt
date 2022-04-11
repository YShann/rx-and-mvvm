package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.plugin

import android.view.View
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem

open class GroupPlugin : ExpandablePlugin() {
    companion object {
        val INSTANCE: GroupPlugin by lazy { GroupPlugin() }
    }

    override fun bindView(adapter: Adapter, item: AdapterItem, view: View, index: Int) {
        TODO("Not yet implemented")
    }

    override fun updateView(adapter: Adapter, item: AdapterItem, view: View, index: Int) {
        TODO("Not yet implemented")
    }

    override fun onClick(adapter: Adapter, item: AdapterItem, view: View, index: Int) {
        TODO("Not yet implemented")
    }
}