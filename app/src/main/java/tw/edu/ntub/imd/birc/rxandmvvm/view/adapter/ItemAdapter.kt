package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter

import tw.edu.ntub.imd.birc.rxandmvvm.extension.get
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem

open class ItemAdapter(
    private val itemList: List<AdapterItem>,
) : AbstractAdapter() {
    companion object {
        fun ofSingleItem(item: AdapterItem) = ItemAdapter(listOf(item))
    }

    override fun getItemList(): List<AdapterItem> = itemList.toList()

    override fun getItem(index: Int): AdapterItem = itemList[index]

    override fun getItemIndex(item: AdapterItem): Int = itemList.indexOf(item)

    override fun getItemCount(): Int = itemList.size

    override fun getLastItemIndex(): Int = itemList.lastIndex

    override operator fun iterator(): Iterator<AdapterItem> = itemList.iterator()

    override operator fun get(indexRange: IntRange): List<AdapterItem> = itemList[indexRange]
}