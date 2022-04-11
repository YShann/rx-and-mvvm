package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter

import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem

open class MutableItemAdapter(itemList: MutableList<AdapterItem> = mutableListOf()) :
    AbstractMutableItemAdapter(itemList) {
    private val _itemList = itemList

    override fun addItem(item: AdapterItem) {
        _itemList.add(item)
        notifyRangeInserted(_itemList.lastIndex, listOf(item))
    }

    override fun addItem(index: Int, item: AdapterItem) {
        if (index <= _itemList.lastIndex) {
            _itemList.add(index, item)
        } else {
            _itemList.add(item)
        }
        notifyRangeInserted(index, listOf(item))
    }

    override fun addItem(items: Collection<AdapterItem>) {
        val lastIndex = _itemList.lastIndex
        _itemList.addAll(items)
        notifyRangeInserted(lastIndex + 1, items.toList())
    }

    override fun addItem(index: Int, items: Collection<AdapterItem>) {
        if (index <= _itemList.lastIndex) {
            _itemList.addAll(index, items)
        } else {
            _itemList.addAll(items)
        }
        notifyRangeInserted(index, items.toList())
    }

    override fun removeItem(items: Collection<AdapterItem>) {
        items.map { getItemIndex(it) }.forEach { removeItem(it) }
    }

    override fun removeItem(index: Int): AdapterItem {
        val removedItem = _itemList.removeAt(index)
        notifyRangeRemoved(index, listOf(removedItem))
        return removedItem
    }

    override fun removeItem(indexRange: IntRange): List<AdapterItem> {
        val removedItems = this[indexRange]
        _itemList.removeAll(removedItems)
        notifyRangeRemoved(indexRange.first, removedItems)
        return removedItems
    }
}