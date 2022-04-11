package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.plugin

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem

abstract class AbstractBindingAdapterPlugin<Binding : ViewDataBinding> : AdapterPlugin {
    override fun bindView(adapter: Adapter, item: AdapterItem, view: View, index: Int) {
        bindView(adapter, item, DataBindingUtil.getBinding<Binding>(view)!!, index)
    }

    abstract fun bindView(adapter: Adapter, item: AdapterItem, binding: Binding, index: Int)

    override fun updateView(adapter: Adapter, item: AdapterItem, view: View, index: Int) {
        updateView(adapter, item, DataBindingUtil.getBinding<Binding>(view)!!, index)
    }

    abstract fun updateView(adapter: Adapter, item: AdapterItem, binding: Binding, index: Int)

    override fun onClick(adapter: Adapter, item: AdapterItem, view: View, index: Int) {
        onClick(adapter, item, DataBindingUtil.getBinding<Binding>(view)!!, index)
    }

    abstract fun onClick(adapter: Adapter, item: AdapterItem, binding: Binding, index: Int)
}