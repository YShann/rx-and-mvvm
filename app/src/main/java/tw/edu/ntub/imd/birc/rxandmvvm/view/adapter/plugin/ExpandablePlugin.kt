package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.plugin

import android.util.Log
import android.view.View
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.MutableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.ExpandableItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.ifExpandable

open class ExpandablePlugin : AdapterPlugin, MutableAdapter.ItemChangeListener {
    companion object {
        val INSTANCE by lazy { ExpandablePlugin() }
    }

    override fun onAttachToAdapter(adapter: Adapter) {
        if (adapter is MutableAdapter) {
            adapter.addItemChangeListener(this)
            if (adapter.getItemCount() != 0) {
                onItemRangeInserted(adapter, 0, adapter.getItemList())
            }
        } else {
            Log.e(this::class.toString(), "此插件僅會在MutableAdapter上作用")
        }
    }

    override fun onItemRangeInserted(
        adapter: MutableAdapter,
        positionStart: Int,
        insertedItemList: List<AdapterItem>
    ) {
        var newIndex = positionStart + 1
        for (item in insertedItemList) {
            if (item is ExpandableItem && item.isExpand) {
                adapter.addItem(newIndex, item.getSubItem())
                newIndex += item.getSubItemCount()
            } else {
                newIndex++
            }
        }
    }

    override fun onItemRangeRemoved(
        adapter: MutableAdapter,
        positionStart: Int,
        removedItemList: List<AdapterItem>
    ) {
        var removeStartIndex = positionStart
        var index = 0
        while (index < removedItemList.size) {
            val removedItem = removedItemList[index]
            if (removedItem is ExpandableItem && removedItem.isExpand) {
                val removedSubItemCount = removedItemList.size - index - 1
                val shouldRemoveCount =
                    if (removedSubItemCount < removedItem.getSubItemCount()) {
                        removedItem.getSubItemCount() - removedSubItemCount
                    } else {
                        removedItem.getSubItemCount()
                    }
                if (shouldRemoveCount > 0) {
                    adapter.removeItem(removeStartIndex until removeStartIndex + shouldRemoveCount)
                    removeStartIndex += shouldRemoveCount
                    index += shouldRemoveCount
                } else {
                    index += 1
                }
            } else {
                index += 1
            }
        }
    }

    override fun bindView(adapter: Adapter, item: AdapterItem, view: View, index: Int) {

    }

    override fun updateView(adapter: Adapter, item: AdapterItem, view: View, index: Int) {

    }

    override fun onClick(adapter: Adapter, item: AdapterItem, view: View, index: Int) {
        (adapter as? MutableAdapter)?.let {
            item.ifExpandable { expandableItem ->
                collapseOrExpandItem(it, expandableItem, view, index)
            }
        }
    }

    private fun collapseOrExpandItem(
        adapter: MutableAdapter,
        item: ExpandableItem,
        view: View,
        index: Int
    ) {
        if (item.isCollapse) {
            expandItem(adapter, item, index)
            item.onExpanded(adapter, view, index)
        } else {
            collapseItem(adapter, item, index)
            item.onCollapsed(adapter, view, index)
        }
    }

    private fun collapseItem(
        adapter: MutableAdapter,
        item: ExpandableItem,
        index: Int
    ) {
        val startIndex = index + 1
        adapter.removeItem(startIndex until startIndex + item.getSubItemCount())
    }

    private fun expandItem(
        adapter: MutableAdapter,
        item: ExpandableItem,
        index: Int
    ) {
        adapter.addItem(index + 1, item.getSubItem())
    }
}