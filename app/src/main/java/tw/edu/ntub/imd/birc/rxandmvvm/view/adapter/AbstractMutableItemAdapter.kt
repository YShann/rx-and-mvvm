package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter

import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem

abstract class AbstractMutableItemAdapter(itemList: List<AdapterItem>) : MutableAdapter,
    ItemAdapter(itemList) {
    private val itemChangeListenerList =
        mutableListOf<MutableAdapter.ItemChangeListener>()

    override fun addItemChangeListener(listener: MutableAdapter.ItemChangeListener) {
        itemChangeListenerList.add(listener)
    }

    override fun removeItemChangeListener(listener: MutableAdapter.ItemChangeListener) {
        itemChangeListenerList.remove(listener)
    }

    override fun notifyChanged() {
        itemChangeListenerList.forEach {
            it.onChanged(this)
        }
    }

    override fun notifyRangeInserted(
        positionStart: Int,
        insertedList: List<AdapterItem>
    ) {
        itemChangeListenerList.forEach {
            it.onItemRangeInserted(
                this,
                positionStart,
                insertedList
            )
            it.onChanged(this)
        }
    }

    override fun notifyRangeRemoved(
        positionStart: Int,
        removedItemList: List<AdapterItem>
    ) {
        itemChangeListenerList.forEach {
            it.onItemRangeRemoved(this, positionStart, removedItemList)
            it.onChanged(this)
        }
    }
}