package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter

interface ExpandableBindingItem<Binding : ViewDataBinding> : ExpandableItem {
    override fun onExpanded(adapter: Adapter, view: View, index: Int) {
        super.onExpanded(adapter, view, index)
        onExpanded(adapter, DataBindingUtil.getBinding<Binding>(view)!!, index)
    }

    fun onExpanded(adapter: Adapter, binding: Binding, index: Int) {

    }

    override fun onCollapsed(adapter: Adapter, view: View, index: Int) {
        super.onCollapsed(adapter, view, index)
        onCollapsed(adapter, DataBindingUtil.getBinding<Binding>(view)!!, index)
    }

    fun onCollapsed(adapter: Adapter, binding: Binding, index: Int) {

    }
}