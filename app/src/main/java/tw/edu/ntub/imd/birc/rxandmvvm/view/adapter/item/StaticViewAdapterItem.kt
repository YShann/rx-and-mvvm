package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.view.View
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter

class StaticViewAdapterItem(
    override val layoutId: Int,
    private val viewBinder: ((adapter: Adapter, view: View) -> Unit)? = null
) : AbstractViewItem() {

    override fun bindView(adapter: Adapter, view: View) {
        viewBinder?.invoke(adapter, view)
    }

    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {

    }
}