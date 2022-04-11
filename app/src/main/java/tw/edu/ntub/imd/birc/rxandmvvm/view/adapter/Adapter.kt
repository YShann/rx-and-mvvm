package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter

import android.view.View
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.plugin.AdapterPlugin

interface Adapter {
    operator fun plus(adapter: Adapter): AdapterChain = AdapterChain(this, adapter)

    operator fun iterator(): Iterator<AdapterItem>

    operator fun get(index: Int): AdapterItem {
        return getItem(index)
    }

    operator fun get(indexRange: IntRange): List<AdapterItem>

    fun addPlugin(plugin: AdapterPlugin)

    fun addItemUpdateListener(listener: ItemUpdateListener)

    fun removeItemUpdateListener(listener: ItemUpdateListener)

    fun notifyUpdate(index: Int, payloads: List<Any> = emptyList())

    fun notifyRangeUpdate(positionStart: Int, itemCount: Int, payload: Any? = null)

    fun getItemList(): List<AdapterItem>

    fun getItem(index: Int): AdapterItem

    fun getItemIndex(item: AdapterItem): Int

    fun isNotEmpty(): Boolean = isEmpty().not()

    fun isEmpty(): Boolean = getItemCount() == 0

    fun getItemCount(): Int

    fun getLastItemIndex(): Int

    fun bindView(index: Int, itemView: View)

    fun updateView(index: Int, itemView: View, payloads: List<Any>)

    fun onItemClick(index: Int, itemView: View)

    interface ItemUpdateListener {
        fun onItemUpdate(adapter: Adapter, index: Int, payloads: List<Any> = emptyList()) {

        }

        fun onItemRangeUpdate(adapter: Adapter, positionStart: Int, itemCount: Int, payload: Any?) {

        }
    }
}