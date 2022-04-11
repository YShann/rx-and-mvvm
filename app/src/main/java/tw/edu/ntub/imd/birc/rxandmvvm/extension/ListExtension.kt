package tw.edu.ntub.imd.birc.rxandmvvm.extension

operator fun <E> List<E>.get(indexRange: IntRange): List<E> {
    val result = mutableListOf<E>()
    for (index in indexRange) {
        result.add(this[index])
    }
    return result
}