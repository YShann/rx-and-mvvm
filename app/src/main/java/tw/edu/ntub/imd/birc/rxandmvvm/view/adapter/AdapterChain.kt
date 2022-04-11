package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter

import tw.edu.ntub.imd.birc.rxandmvvm.exception.EmptyAdapterChainException
import tw.edu.ntub.imd.birc.rxandmvvm.extension.addListener
import tw.edu.ntub.imd.birc.rxandmvvm.extension.get
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem
import kotlin.math.min

class AdapterChain(vararg _adapters: Adapter) : MutableItemAdapter() {
    private val adapters = _adapters.toMutableList()
    private val mutableAdapterListenerMap =
        mutableMapOf<MutableAdapter, MutableAdapter.ItemChangeListener>()

    init {
        for (adapter in _adapters) {
            (adapter as? MutableAdapter)?.let {
                setItemChangeListener(it)
            }
        }
    }

    private fun setItemChangeListener(adapter: MutableAdapter) {
        val listener = OnSubAdapterItemChanged(
            this,
            adapter
        )
        mutableAdapterListenerMap[adapter] = listener
        adapter.addListener(listener)
    }

    override fun plus(adapter: Adapter): AdapterChain {
        addAdapter(adapter)
        return this
    }

    operator fun plusAssign(adapter: Adapter) {
        plus(adapter)
    }

    fun getAdapterCount() = adapters.size

    fun addAdapter(adapter: Adapter) {
        val lastItemIndex = getLastItemIndex()
        (adapter as? MutableItemAdapter)?.let {
            setItemChangeListener(it)
        }
        adapters.add(adapter)
        if (adapter.getItemCount() > 0) {
            notifyRangeInserted(lastItemIndex + 1, adapter.getItemList())
        }
    }

    fun removeAdapter(adapterIndex: Int): Adapter {
        if (adapterIndex in adapters.indices) {
            val adapter = adapters[adapterIndex]
            removeAdapter(adapter)
            return adapter
        } else {
            throw EmptyAdapterChainException()
        }
    }

    fun removeAdapter(adapter: Adapter) {
        val index = adapters.indexOf(adapter)
        if (adapters.remove(adapter)) {
            if (adapter is MutableAdapter) {
                val listener = if (adapter in adapters) {
                    mutableAdapterListenerMap[adapter]!!
                } else {
                    mutableAdapterListenerMap.remove(adapter)!!
                }
                adapter.removeItemChangeListener(listener)
            }
            if (adapter.getItemCount() > 0) {
                notifyRangeRemoved(
                    getItemCount(0 until index),
                    adapter.getItemList()
                )
            }
        }
    }

    fun getAdapterIndex(adapter: Adapter) = adapters.indexOf(adapter)

    fun getAdapterIndex(item: AdapterItem): Int {
        for (i in adapters.indices) {
            if (adapters[i].getItemIndex(item) != -1) {
                return i
            }
        }
        return -1
    }

    fun getAdapterIndex(itemIndex: Int): Int {
        var startIndex = 0
        for (i in adapters.indices) {
            val endIndex = startIndex + adapters[i].getItemCount()
            if (itemIndex in startIndex until startIndex + endIndex) {
                return i
            } else {
                startIndex += adapters[i].getItemCount()
            }
        }
        return -1
    }

    fun getItemCount(adapterIndexRange: IntRange): Int =
        adapters[adapterIndexRange].sumOf { it.getItemCount() }

    override fun iterator(): Iterator<AdapterItem> =
        getItemList().iterator()

    override fun get(indexRange: IntRange): List<AdapterItem> =
        getItemList()[indexRange]

    override fun getItemList(): List<AdapterItem> =
        adapters.map { it.getItemList() }.flatten()

    override fun getItem(index: Int): AdapterItem =
        getItemList()[index]

    override fun getItemIndex(item: AdapterItem): Int =
        getItemList().indexOf(item)

    override fun getItemCount(): Int = adapters.sumOf { it.getItemCount() }

    override fun getLastItemIndex(): Int = getItemCount() - 1

    override fun addItem(item: AdapterItem) {
        if (adapters.isNotEmpty() && adapters.last() is MutableAdapter) {
            (adapters.last() as MutableAdapter).addItem(item)
            notifyRangeInserted(getLastItemIndex(), listOf(item))
        } else {
            addAdapter(MutableItemAdapter(mutableListOf(item)))
        }
    }

    override fun addItem(index: Int, item: AdapterItem) {
        val adapterIndex = getAdapterIndex(index)
        if (adapterIndex != -1) {
            val adapter = adapters[adapterIndex]
            if (adapter is MutableAdapter) {
                val adapterAddIndex = index - getItemCount(0 until adapterIndex)
                adapter.addItem(adapterAddIndex, item)
                notifyRangeInserted(index, listOf(item))
            } else {
                throw UnsupportedOperationException("此Adapter不支援此操作：$adapter")
            }
        } else {
            addItem(item)
        }
    }

    override fun addItem(items: Collection<AdapterItem>) {
        if (adapters.isNotEmpty() && adapters.last() is MutableAdapter) {
            (adapters.last() as MutableAdapter).addItem(items)
            notifyRangeInserted(getLastItemIndex(), items.toList())
        } else {
            addAdapter(MutableItemAdapter(items.toMutableList()))
        }
    }

    override fun addItem(index: Int, items: Collection<AdapterItem>) {
        val adapterIndex = getAdapterIndex(index)
        if (adapterIndex != -1) {
            val adapter = adapters[adapterIndex]
            if (adapter is MutableAdapter) {
                adapter.addItem(
                    index - getItemCount(0 until adapterIndex),
                    items
                )
            }
        } else {
            addItem(items)
        }
    }

    override fun removeItem(index: Int): AdapterItem {
        val adapterIndex = getAdapterIndex(index)
        if (adapterIndex != -1) {
            val adapter = adapters[adapterIndex]
            if (adapter is MutableAdapter) {
                return adapter.removeItem(
                    index - getItemCount(0 until adapterIndex)
                )
            } else {
                throw UnsupportedOperationException("此Adapter不支援此操作：$adapter")
            }
        }
        throw IndexOutOfBoundsException("沒有此項目：$index")
    }

    override fun removeItem(indexRange: IntRange): List<AdapterItem> {
        val removedItem = mutableListOf<AdapterItem>()
        val notRemovedIndexList = indexRange.toMutableList()
        while (notRemovedIndexList.isNotEmpty()) {
            val index = notRemovedIndexList.first()
            val adapterIndex = getAdapterIndex(index)
            if (adapterIndex != -1) {
                val adapter = adapters[adapterIndex]
                if (adapter is MutableAdapter) {
                    val startIndex = index - getItemCount(0 until adapterIndex)
                    val endIndex =
                        min(adapter.getLastItemIndex(), notRemovedIndexList.last())
                    removedItem.addAll(adapter.removeItem(startIndex..endIndex))
                    notRemovedIndexList.removeAll(startIndex..endIndex)
                } else {
                    throw UnsupportedOperationException("此Adapter不支援此操作：$adapter")
                }
            } else {
                throw IndexOutOfBoundsException("沒有此項目：$index")
            }
        }
        notifyRangeRemoved(indexRange.first, removedItem)
        return removedItem
    }

    private class OnSubAdapterItemChanged(
        private val adapterChain: AdapterChain,
        private val subAdapter: MutableAdapter
    ) : MutableAdapter.ItemChangeListener, Adapter.ItemUpdateListener {

        override fun onChanged(adapter: MutableAdapter) {
            adapterChain.notifyChanged()
        }

        override fun onItemUpdate(adapter: Adapter, index: Int, payloads: List<Any>) {
            val adapterChainStartPosition = getAdapterChainPositionStart(index)
            if (adapterChainStartPosition != -1) {
                adapterChain.notifyUpdate(
                    adapterChainStartPosition,
                    payloads
                )
            }
        }

        override fun onItemRangeUpdate(
            adapter: Adapter,
            positionStart: Int,
            itemCount: Int,
            payload: Any?
        ) {
            val adapterChainStartPosition = getAdapterChainPositionStart(positionStart)
            if (adapterChainStartPosition != -1) {
                adapterChain.notifyRangeUpdate(
                    adapterChainStartPosition,
                    itemCount,
                    payload
                )
            }
        }

        private fun getAdapterChainPositionStart(subItemPositionStart: Int): Int {
            val adapterPosition = adapterChain.getAdapterIndex(subAdapter)
            return if (adapterPosition != -1) {
                var result = 0
                for (i in 0 until adapterPosition) {
                    result += adapterChain.adapters[i].getItemCount()
                }
                result + subItemPositionStart
            } else {
                -1
            }
        }

        override fun onItemRangeInserted(
            adapter: MutableAdapter,
            positionStart: Int,
            insertedItemList: List<AdapterItem>
        ) {
            val adapterChainStartPosition = getAdapterChainPositionStart(positionStart)
            if (adapterChainStartPosition != -1) {
                adapterChain.notifyRangeInserted(
                    adapterChainStartPosition,
                    insertedItemList
                )
            }
        }

        override fun onItemRangeRemoved(
            adapter: MutableAdapter,
            positionStart: Int,
            removedItemList: List<AdapterItem>
        ) {
            val adapterChainStartPosition = getAdapterChainPositionStart(positionStart)
            if (adapterChainStartPosition != -1) {
                adapterChain.notifyRangeRemoved(
                    adapterChainStartPosition,
                    removedItemList
                )
            }
        }
    }
}