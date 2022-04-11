package tw.edu.ntub.imd.birc.rxandmvvm.extension

import androidx.recyclerview.widget.RecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.MutableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.RecyclerViewAdapter

fun Adapter.attachToRecyclerView(
    recyclerView: RecyclerView,
    layoutManager: RecyclerView.LayoutManager? = null
) {
    recyclerView.adapter = RecyclerViewAdapter(this, layoutManager)
}

fun <Listener> MutableAdapter.addListener(listener: Listener)
        where Listener : Adapter.ItemUpdateListener,
              Listener : MutableAdapter.ItemChangeListener {
    addItemUpdateListener(listener)
    addItemChangeListener(listener)
}

fun <Listener> MutableAdapter.removeListener(listener: Listener)
        where Listener : Adapter.ItemUpdateListener,
              Listener : MutableAdapter.ItemChangeListener {
    removeItemUpdateListener(listener)
    removeItemChangeListener(listener)
}
