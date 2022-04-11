package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class AbstractViewItem : AdapterItem {
    override fun createView(inflater: LayoutInflater, parent: ViewGroup): View =
        inflater.inflate(layoutId, parent, false)
}