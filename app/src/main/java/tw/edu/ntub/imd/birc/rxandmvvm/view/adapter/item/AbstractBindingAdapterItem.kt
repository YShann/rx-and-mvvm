package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter

abstract class AbstractBindingAdapterItem<in B : ViewDataBinding>(
    private val lifecycleOwner: LifecycleOwner
) : AdapterItem {
    override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<B>(inflater, layoutId, parent, false)
        binding.lifecycleOwner = lifecycleOwner
        return binding.root
    }

    override fun bindView(adapter: Adapter, view: View) {
        val binding = DataBindingUtil.getBinding<B>(view)!!
        val bindingItemVariableKey = getItemVariableKey()
        binding.setVariable(bindingItemVariableKey, this)
    }

    abstract fun getItemVariableKey(): Int

    override fun onClick(adapter: Adapter, view: View) {
        onClick(adapter, DataBindingUtil.getBinding<B>(view)!!)
    }

    abstract fun onClick(adapter: Adapter, binding: B)

    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {
        updateView(adapter, DataBindingUtil.getBinding<B>(view)!!, payloads)
    }

    abstract fun updateView(adapter: Adapter, binding: B, payloads: List<Any>)
}