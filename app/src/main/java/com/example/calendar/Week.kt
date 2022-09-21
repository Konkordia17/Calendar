package com.example.calendar

enum class Week(val id: Int, val text: String) {
    MONDAY(id = 2, text = "ПН"),
    TUESDAY(id = 3, text = "ВТ"),
    WEDNESDAY(id = 4, text = "СР"),
    THURSDAY(id = 5, text = "ЧТ"),
    FRIDAY(id = 6, text = "ПТ"),
    SATURDAY(id = 7, text = "СБ"),
    SUNDAY(id = 1, text = "ВС")
}