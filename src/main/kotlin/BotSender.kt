package ru.chemax24.retbill

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.*


class BotSender(private val prop: Properties) : TelegramLongPollingBot() {

    override fun onUpdateReceived(update: Update?) {
    }

    override fun getBotUsername(): String {
        return prop.getProperty("telegram_bot_name")
    }

    override fun getBotToken(): String {
        return prop.getProperty("telegram_bot_token")
    }

    fun sendStartMessage(date: String) {
        val message = SendMessage()
        message.enableHtml(true)
        message.chatId = prop.getProperty("default_report_chat_id")
        message.text = "========== $date =========="
        try {
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    fun sendReport(report: AbstractReport) {
        val msg = report.message()
        val message = SendMessage()
        message.enableHtml(true)
        if (msg.isNullOrBlank()) return
        message.chatId = prop.getProperty("default_report_chat_id")
//            report.user.contacts ?: prop.getProperty("default_report_chat_id")
        message.text = msg
        try {
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}