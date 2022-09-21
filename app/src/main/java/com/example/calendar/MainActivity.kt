package com.example.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.util.Calendar
import java.util.GregorianCalendar

class MainActivity : ComponentActivity() {

    val vm: CalendarViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "calendarScreen") {
                composable("calendarScreen") { WelcomeScreen(navController = navController) }
                composable("secondScreen") { SecondScreen(navController = navController) }
            }
        }
    }

    @Composable
    fun WelcomeScreen(navController: NavController) {
        Calendar(navController = navController)
    }

    @Composable
    fun SecondScreen(navController: NavController) {
        Schedule("331")
    }

    // First screen
    @Composable
    fun Calendar(navController: NavController) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var selectedDay by remember { mutableStateOf("") }
            MonthWithYear(title = vm.getDataForMonth())
            DaysOfWeek()
            Weeks(selectedDay = selectedDay, onClick = {
                selectedDay = it
            })
            ScheduleButton(selectedDay.isNotEmpty(), navController = navController)
        }
    }

    @Composable
    fun ScheduleButton(isEnabled: Boolean, navController: NavController, day: String? = null) {
        Button(
            onClick = {
                navController.navigate("secondScreen")
            },
            enabled = isEnabled,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .padding(top = 29.dp)
                .width(282.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        )
        {
            Text(
                text = "Открыть расписание",
                color = Color.White
            )
        }
    }

    @Composable
    fun DaysOfWeek() {
        Row(modifier = Modifier.fillMaxWidth()) {
            var count = 0
            while (count <= 6) {
                CalendarDayOfWeek(day = Week.values()[count].text, modifier = Modifier.weight(1f))
                count += 1
            }
        }
    }

    @Composable
    fun Weeks(selectedDay: String, onClick: (String) -> Unit) {
        val datesForMonth = vm.getSumAllDaysAndEmptyDays()
        datesForMonth.chunked(7) {
            it.mapIndexed { index, dayData ->
                if (index == 5 || index == 6) {
                    dayData.copy(isWeekend = true)
                } else {
                    dayData
                }
            }
        }.forEach {
            WeekRow(days = it, selectedDay = selectedDay, onClick = { onClick(it) })
        }
    }

    @Composable
    fun WeekRow(days: List<DayData>, selectedDay: String, onClick: (String) -> Unit) {
        Row(modifier = Modifier.fillMaxWidth()) {
            days.forEach { dayData ->
                Day(
                    data = dayData.copy(isSelected = dayData.title == selectedDay),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onClick(dayData.title)
                    })
            }
        }
    }

    @Composable
    fun CalendarDayOfWeek(day: String, modifier: Modifier) {
        Text(
            modifier = modifier,
            textAlign = TextAlign.Center,
            text = day,
            fontSize = 12.sp,
            color = Color(R.color.gray)
        )
    }

    @Composable
    fun Day(data: DayData, modifier: Modifier, onClick: (String) -> Unit) {
        Text(
            textAlign = TextAlign.Center,
            text = data.title,
            fontSize = 20.sp,
            modifier = modifier
                .padding(top = 16.dp)
                .clickable { onClick(data.title) },
            color = when (data.isSelected) {
                true -> {
                    Color.Red
                }
                false -> {
                    if (data.isWeekend) {
                        Color(R.color.gray_for_number)
                    } else {
                        Color.Black
                    }
                }
            }
        )
    }

    @Composable
    fun MonthWithYear(title: String) {
        Row(modifier = Modifier.padding(start = 20.dp, bottom = 12.dp, top = 32.dp)) {
            Text(
                text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            ImageVerticalArrows()
            ImageHorizontalArrows(id = R.drawable.arrow_left)
            ImageHorizontalArrows(id = R.drawable.arrow_right)
        }
    }

    @Composable
    fun ImageVerticalArrows() {
        Image(
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 7.dp, top = 3.dp, end = 148.dp)
                .size(width = 6.dp, height = 20.dp),
        )
    }

    @Composable
    fun ImageHorizontalArrows(id: Int) {
        Image(
            painter = painterResource(id = id),
            contentDescription = null,
            modifier = Modifier
                .size(width = 32.dp, height = 32.dp)
                .padding(end = 8.dp)
        )
    }

    // second screen
    @Composable
    fun Schedule(title: String) {
        LazyColumn(Modifier.fillMaxWidth()) {
            item { ScheduleTitle(title = title) }
            item { RowSchedule(plans = vm.getPlansList()) }
        }
    }

    @Composable
    fun ScheduleTitle(title: String) {
        Text(
            text = "Планы на $title число",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 21.dp, top = 24.dp, bottom = 13.dp)
        )
    }

    @Composable
    fun RowSchedule(plans: List<Pair<String, String>>) {
        plans.forEach {
            Row(modifier = Modifier.fillMaxWidth()) {
                RedCircle()
                Time(text = it.first)
                PlanInfo(text = it.second)
            }
        }
    }

    @Composable
    fun RedCircle() {
        Image(
            painter = painterResource(id = R.drawable.bullet_l),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 28.dp, top = 21.dp, end = 12.dp)
                .size(width = 8.dp, height = 8.dp),
        )
    }

    @Composable
    fun Time(text: String) {
        Text(
            text = text,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp, end = 5.dp)
        )
    }

    @Composable
    fun PlanInfo(text: String) {
        Text(
            text = text,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp, end = 5.dp)
        )
    }
}

