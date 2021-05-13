package ru.chemax24.retbill

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SummaryReport(private val datetime: String) : AbstractReport(datetime) {

    override fun message(): String {
        generateStats()
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(formatter2).toString()
        return "<a href=\"http://116.203.55.139/callsreport/index?f_date_from=$date&f_date_to=$date\">" +
                "Суммарный отчёт</a>" +
                "\n<i>$datetime</i>" +
                "\n<b>ASR</b>: $asr% / $hourAsr%" +
                "\n<b>ACD</b>: ${convertSecondToHHMMString(acd)} / ${convertSecondToHHMMString(hourACD)}" +
                "\n<b>Sum</b>: ${billsec / 60} min/${hourBillsec / 60} min"
    }
}