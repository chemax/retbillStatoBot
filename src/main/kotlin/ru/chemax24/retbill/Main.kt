package ru.chemax24.retbill

import java.io.FileInputStream
import java.util.*


fun main(args: Array<String>) {
//    println(args.toString())
    val fis = FileInputStream("./config.properties")
    val prop = Properties()
    prop.load(fis)
    val d = DataGrapper(prop)
    d.get()
}
