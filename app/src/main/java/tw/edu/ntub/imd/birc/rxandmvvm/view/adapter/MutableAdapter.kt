package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter

import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem

interface MutableAdapter : Adapter {

    fun addItemChangeListener(listener: ItemChangeListener)

    fun removeItemChangeListener(listener: ItemChangeListener)

    fun notifyChanged()

    fun notifyRangeInserted(
        positionStart: Int,
        insertedList: List<AdapterItem>
    )

    fun notifyRangeRemoved(
        positionStart: Int,
        removedItemList: List<AdapterItem>
    )

    fun addItem(item: AdapterItem)

    fun addItemAfterFirstItem(item: AdapterItem) {
        addItem(
            if (getItemCount() > 0) {
                1
            } else {
                0
            },
            item
        )
    }

    fun addItemBeforeLastItem(item: AdapterItem) {
        addItem(getLastItemIndex(), item)
    }

    fun addItem(index: Int, item: AdapterItem)

    fun addItem(items: Collection<AdapterItem>)

    fun addItemAfterFirstItem(items: Collection<AdapterItem>) {
        addItem(
            if (getItemCount() > 0) {
                1
            } else {
                0
            },
            items
        )
    }

    fun addItemBeforeLastItem(items: Collection<AdapterItem>) {
        addItem(getLastItemIndex(), items)
    }

    fun addItem(index: Int, items: Collection<AdapterItem>)

    fun removeFirstItem(): AdapterItem = removeItem(0)

    fun removeLastItem(): AdapterItem =
        removeItem(getLastItemIndex())

    fun removeItem(items: Collection<AdapterItem>)

    fun removeItem(index: Int): AdapterItem

    fun removeAll(): List<AdapterItem> =
        removeItem(0..getLastItemIndex())

    fun removeItem(indexRange: IntRange): List<AdapterItem>

    interface ItemChangeListener {
        fun onChanged(adapter: MutableAdapter) {

        }

        fun onItemRangeInserted(
            adapter: MutableAdapter,
            positionStart: Int,
            insertedItemList: List<AdapterItem>
        ) {

        }

        fun onItemRangeRemoved(
            adapter: MutableAdapter,
            positionStart: Int,
            removedItemList: List<AdapterItem>
        ) {

        }
    }
}