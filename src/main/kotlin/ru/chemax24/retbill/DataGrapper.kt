package ru.chemax24.retbill

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class DataGrapper(prop: Properties) {
    private val bot = BotSender(prop)

    init {
        Database.connect(
            url = prop.getProperty("url"),
            driver = "org.mariadb.jdbc.Driver",
            user = prop.getProperty("user"),
            password = prop.getProperty("password")
        )
    }

    fun get() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")
//        val zone = ZoneOffset("")

        val startDate = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Tashkent")).format(formatter).toString()
        val summaryReport = SummaryReport(startDate)
        summaryReport.calls = mutableListOf()
        bot.sendStartMessage(startDate)
        transaction {
            val users = Users.selectAll()
                .andWhere { Users.monitoring greater 0 }
//                .let { User.wrapRow(it) }
                .toList()
            users.forEach { user ->
                val userWrapped = User.wrapRow(user)
                val fromDate = LocalDateTime.now().format(formatter2).toString()
                val m = Report(userWrapped, ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Asia/Tashkent")).format(formatter).toString())
                m.calls = mutableListOf()
                val trunks = userWrapped.trunks
                trunks.forEach { trunk ->
                    val callList = Calls.selectAll()
                        //starttime > NOW() - INTERVAL 1 HOUR
//                        .andWhere { Calls.starttime greaterEq  "2021-04-25 00:00:00" }
//                        .andWhere { Calls.starttime lessEq  "2021-04-25 23:59:59" }
                        .andWhere { Calls.starttime greater fromDate }
                        .andWhere { Calls.trunk eq trunk.id }
                        .orderBy(Calls.starttime)
                        .let { Call.wrapRows(it) }
                        .toList()
                    m.calls.addAll(callList)
                    summaryReport.calls.addAll(callList)
                }
                bot.sendReport(m)
            }

        }
        bot.sendReport(summaryReport)
        bot.sendStartMessage(startDate)

    }

}