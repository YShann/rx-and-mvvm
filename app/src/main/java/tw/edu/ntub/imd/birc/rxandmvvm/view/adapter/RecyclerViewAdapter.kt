package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.addListener
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem

open class RecyclerViewAdapter(
    private val adapter: Adapter,
    private val layoutManager: RecyclerView.LayoutManager? = null
) :
    RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewItemViewHolder>() {
    private val viewFactory: MutableList<AdapterItem> = mutableListOf()
    private lateinit var context: Context
    private lateinit var inflater: LayoutInflater

    init {
        if (adapter is MutableAdapter) {
            adapter.addListener(object : Adapter.ItemUpdateListener,
                MutableAdapter.ItemChangeListener {
                override fun onItemUpdate(adapter: Adapter, index: Int, payloads: List<Any>) {
                    notifyItemChanged(index, payloads)
                }

                override fun onItemRangeUpdate(
                    adapter: Adapter,
                    positionStart: Int,
                    itemCount: Int,
                    payload: Any?
                ) {
                    notifyItemRangeChanged(positionStart, itemCount, payload)
                }

                override fun onItemRangeInserted(
                    adapter: MutableAdapter,
                    positionStart: Int,
                    insertedItemList: List<AdapterItem>
                ) {
                    notifyItemRangeInserted(positionStart, insertedItemList.size)
                }

                override fun onItemRangeRemoved(
                    adapter: MutableAdapter,
                    positionStart: Int,
                    removedItemList: List<AdapterItem>
                ) {
                    notifyItemRangeRemoved(positionStart, removedItemList.size)
                }
            })
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.context = recyclerView.context
        this.inflater = LayoutInflater.from(context)
        recyclerView.layoutManager =
            layoutManager ?: LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun getItemCount(): Int = adapter.getItemCount()

    override fun getItemViewType(position: Int): Int {
        val item = adapter[position]
        return viewFactory.indexOf(item).let {
            if (it == -1) {
                viewFactory.add(item).run { viewFactory.lastIndex }
            } else {
                it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewItemViewHolder {
        val factory = viewFactory[viewType]
        return RecyclerViewItemViewHolder(factory.createView(inflater, parent))
    }

    override fun onBindViewHolder(holder: RecyclerViewItemViewHolder, position: Int) {
        adapter.bindView(position, holder.itemView)
        holder.itemView.setOnClickListener {
            adapter.onItemClick(position, it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(
        holder: RecyclerViewItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            adapter.updateView(position, holder.itemView, payloads[0] as? List<Any> ?: payloads)
        }
    }

    inner class RecyclerViewItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}