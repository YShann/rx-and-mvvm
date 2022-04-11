package tw.edu.ntub.imd.birc.rxandmvvm.extension

operator fun String.times(times: Int): String {
    return repeat(times)
}