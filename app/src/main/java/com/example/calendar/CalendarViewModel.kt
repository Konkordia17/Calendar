package com.example.calendar

import androidx.lifecycle.ViewModel
import java.util.Calendar
import java.util.GregorianCalendar

class CalendarViewModel :ViewModel() {

    private fun getYearAndMonth(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        return Pair(month, year)
    }

    fun getDataForMonth(): String {
        return "${Month.values()[getYearAndMonth().first].title} ${getYearAndMonth().second}"
    }

    private fun getCalendar(year: Int, month: Int): GregorianCalendar {
        return GregorianCalendar(year, month, 1)
    }

    private fun getNumberOfDays(): Int {
        return getCalendar(getYearAndMonth().second, getYearAndMonth().first)
            .getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    private fun getFirstDayOfMonth(): Int {
        return getCalendar(
            getYearAndMonth().second,
            getYearAndMonth().first
        )[Calendar.DAY_OF_WEEK] - 1
    }

     fun getSumAllDaysAndEmptyDays(): List<DayData> {
        var listDays = emptyList<DayData>()
        var countEmptyString = 1
        var countDaysForMonth = 1
        while (countEmptyString < getFirstDayOfMonth()) {
            listDays = listDays + DayData(title = "", isWeekend = false, isSelected = false)
            countEmptyString += 1
        }
        while (countDaysForMonth <= getNumberOfDays()) {
            listDays = listDays + DayData(
                title = countDaysForMonth.toString(),
                isWeekend = false,
                isSelected = false
            )
            countDaysForMonth += 1
        }
        while (listDays.size % 7 != 0) {
            listDays = listDays + DayData(title = "", isWeekend = false, isSelected = false)
        }
        return listDays
    }

    fun getPlansList(): List<Pair<String, String>> {
        val listPlans = listOf(
            Pair("07:00", "Подъем"),
            Pair("08:00", "Завтра"),
            Pair("09:00", "Начало рабочего дня"),
            Pair("11:00", "Stand Up"),
            Pair("13:00", "Обед"),
            Pair("14:00", "Еще чуть чуть поработать"),
            Pair("15:00", "Проверить почту"),
            Pair("16:00", "Перекур"),
            Pair("17:00", "Заполнить отчет о проделанной работе, выпить чаю, сделать зарядку"),
            Pair(
                "19:00",
                "С появлением навигации Jetpack Compose отпала необходимость во фрагментах или навигационных графиках с меню. Простой, интуитивно понятный способ управления отображением страниц на устройстве пользователя — очень похоже на маршруты в проекте React."
            ),
            Pair("20:00", "Ужин"),
            Pair("21:00", "Просмотр Сериалов")
        )
        return listPlans
    }
}