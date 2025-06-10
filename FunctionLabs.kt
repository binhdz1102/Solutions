package practicalsample

// thuật toán danh sách phần tử với vách ngăn


fun <T> collapseAdjacent(
    input: List<T>,
    separator: T
): List<T> {
    val result = ArrayList<T>(input.size)
    var lastIsSep = false

    for (item in input) {
        if (item == separator) {
            // chỉ thêm 1 B khi trước đó không phải B và đã có ít nhất 1 phần tử khác
            if (result.isNotEmpty() && !lastIsSep) {
                result.add(item)
                lastIsSep = true
            }
        } else {
            result.add(item)
            lastIsSep = false
        }
    }

    // nếu cuối cùng vẫn là B thì xoá
    if (result.isNotEmpty() && result.last() == separator) {
        result.removeAt(result.lastIndex)
    }

    return result
}



///////////////////////////////////////////////////


fun <T> MutableList<T>.collapseAdjacentInPlace(separator: T) {
    if (isEmpty()) return

    var write = 0
    var lastIsSep = false

    // read từ 0..lastIndex, write từ 0..
    for (read in indices) {
        val item = this[read]
        if (item == separator) {
            if (write > 0 && !lastIsSep) {
                this[write++] = item
                lastIsSep = true
            }
        } else {
            this[write++] = item
            lastIsSep = false
        }
    }

    // nếu phần tử cuối cùng viết ra là separator thì lùi lại 1
    if (write > 0 && this[write - 1] == separator) {
        write--
    }

    // xoá hết phần dư ở cuối list
    for (i in lastIndex downTo write) {
        removeAt(i)
    }
}


// Ví dụ dùng:
fun main() {
    val input = listOf(
        "A1","B","B","B","B","A2","A3","B","B","A4",
        "B","A5","A6","A7","A8","B","A9","B","B","A10","B","B"
    )
    val output = collapseAdjacent(input, "B")
    println(output)
    // Kết quả: [A1, B, A2, A3, B, A4, B, A5, A6, A7, A8, B, A9, B, A10]




    val list = mutableListOf(
        "A1","B","B","B","B","A2","A3","B","B","A4",
        "B","A5","A6","A7","A8","B","A9","B","B","A10","B","B"
    )
    list.collapseAdjacentInPlace("B")
    println(list) // [A1, B, A2, B, A3]
}
