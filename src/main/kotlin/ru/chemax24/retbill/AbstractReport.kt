package ru.chemax24.retbill

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

const val HOUR = 60

abstract class AbstractReport(private val datetime: String) {
    lateinit var calls: MutableList<Call>
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
    private val currentDate: ZonedDateTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Tashkent"))
    var billsec: Int = 0
    var asr: Double = 0.0
    var acd: Int = 0
    var hourBillsec: Int = 0
    var hourAsr: Double = 0.0
    var hourACD: Int = 0

    protected fun generateStats() {
        if (calls.size == 0) return
        var hourSuccess = 0
        var hourFail = 0
        var hourCount = 0
        var success = 0
        var fail = 0
        var count = 0
        calls.forEach {
            //Billsec
            billsec += it.billsec
            //ASR
            if (it.terminatestatus == "ANSWER") {
                success++
            } else {
                fail++
            }
            //ACD
            if (it.terminatestatus == "ANSWER") {
                count++
            }
//            println(ChronoUnit.MINUTES.between(it.starttime, currentDate))
            if (ChronoUnit.MINUTES.between(it.starttime, currentDate) <= HOUR) {
                println(it.starttime.format(formatter).toString())
                println(currentDate.format(formatter).toString())
                //BillsecHour
                hourBillsec += it.billsec
                //ASR hour
                if (it.terminatestatus == "ANSWER") {
                    hourSuccess++
                } else {
                    hourFail++
                }
                //ACD hour
                if (it.terminatestatus == "ANSWER") {
                    hourCount++
                }
            }
        }
        if (hourCount > 0)
            hourACD = hourBillsec / hourCount
        if (count > 0)
            acd = billsec / count
        asr = round(success.toDouble() / (fail + success) * 100)
        hourAsr = round(hourSuccess.toDouble() / (hourFail + hourSuccess) * 100)
    }

    fun convertSecondToHHMMString(secondsTime: Int): String? {
        val tz = TimeZone.getTimeZone("UTC")
        val df = SimpleDateFormat("mm:ss")
        df.timeZone = tz
        return df.format(Date(secondsTime * 1000L))
    }


    abstract fun message(): String

}