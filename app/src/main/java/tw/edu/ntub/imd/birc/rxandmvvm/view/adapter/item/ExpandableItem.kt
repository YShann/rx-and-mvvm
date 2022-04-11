package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.view.View
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter

fun AdapterItem.ifExpandable(ifExpandable: (expandableItem: ExpandableItem) -> Unit) {
    if (this is ExpandableItem) {
        ifExpandable(this)
    }
}

interface ExpandableItem {
    var isExpand: Boolean
    val isCollapse: Boolean
        get() = isExpand.not()

    fun getSubItemCount() = getSubItem().size

    fun getSubItem(): List<AdapterItem>

    fun onExpanded(adapter: Adapter, view: View, index: Int) {
        isExpand = true
    }

    fun onCollapsed(adapter: Adapter, view: View, index: Int) {
        isExpand = false
    }
}