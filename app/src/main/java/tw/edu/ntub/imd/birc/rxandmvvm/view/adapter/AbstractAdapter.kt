package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter

import android.view.View
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.plugin.AdapterPlugin

abstract class AbstractAdapter : Adapter {
    private val itemUpdateListenerList = mutableListOf<Adapter.ItemUpdateListener>()
    private val pluginList = mutableListOf<AdapterPlugin>()

    override fun addPlugin(plugin: AdapterPlugin) {
        if (plugin !in pluginList) {
            plugin.onAttachToAdapter(this)
            pluginList.add(plugin)
        }
    }

    override fun addItemUpdateListener(listener: Adapter.ItemUpdateListener) {
        if (listener !in itemUpdateListenerList) {
            itemUpdateListenerList.add(listener)
        }
    }

    override fun removeItemUpdateListener(listener: Adapter.ItemUpdateListener) {
        itemUpdateListenerList.remove(listener)
    }

    override fun notifyUpdate(index: Int, payloads: List<Any>) {
        itemUpdateListenerList.forEach { it.onItemUpdate(this, index, payloads) }
    }

    override fun notifyRangeUpdate(positionStart: Int, itemCount: Int, payload: Any?) {
        itemUpdateListenerList.forEach {
            it.onItemRangeUpdate(
                this,
                positionStart,
                itemCount,
                payload
            )
        }
    }

    override fun bindView(index: Int, itemView: View) {
        val item = this[index]
        item.bindView(this, itemView)
        pluginList.forEach { it.bindView(this, item, itemView, index) }
    }

    override fun updateView(index: Int, itemView: View, payloads: List<Any>) {
        val item = this[index]
        item.updateView(this, itemView, payloads)
        pluginList.forEach { it.updateView(this, item, itemView, index) }
    }

    override fun onItemClick(index: Int, itemView: View) {
        val item = this[index]
        item.onClick(this, itemView)
        pluginList.forEach { it.onClick(this, item, itemView, index) }
    }
}