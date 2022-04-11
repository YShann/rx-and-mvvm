package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter

interface AdapterItem {
    @get:LayoutRes
    val layoutId: Int

    fun createView(inflater: LayoutInflater, parent: ViewGroup): View

    fun bindView(adapter: Adapter, view: View)

    fun updateView(adapter: Adapter, view: View, payloads: List<Any>)

    fun onClick(adapter: Adapter, view: View) {

    }
}