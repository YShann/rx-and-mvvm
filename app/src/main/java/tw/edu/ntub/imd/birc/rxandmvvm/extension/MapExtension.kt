package tw.edu.ntub.imd.birc.rxandmvvm.extension

operator fun <K, V, R> MutableMap<K, V>.set(
    iterableKeyRange: R,
    value: V
) where K : Comparable<K>, R : Iterable<K> {
    for (key in iterableKeyRange) {
        set(key, value)
    }
}

operator fun <K, V, R> MutableMap<K, V>.set(
    iterableKeyRange: R,
    collectionValue: Collection<V>
) where K : Comparable<K>, R : Iterable<K> {
    val keyIterator = iterableKeyRange.iterator()
    for (value in collectionValue) {
        set(keyIterator.next(), value)
    }
}

fun <K, V> MutableMap<K, V>.remove(
    keyArray: Iterable<K>
) {
    for (key in keyArray) {
        remove(key)
    }
}
