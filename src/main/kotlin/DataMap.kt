package ru.chemax24.retbill

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Calls : IntIdTable("call") {
    val trunk = reference("trunk_id", Trunks)
    val billsec = integer("sessiontime")
    val starttime = datetime("starttime")
    val terminatestatus = varchar("terminatestatus", 40)
}

class Call(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Call>(Calls)

    var trunk by Trunk referencedOn Calls.trunk
    val billsec by Calls.billsec
    val starttime by Calls.starttime
    val terminatestatus by Calls.terminatestatus
}

object Trunks : IntIdTable("trunk") {
    val user = reference("user_id", Users)
    val name = varchar("name", 50)
}

class Trunk(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Trunk>(Trunks)

    var user by User referencedOn Trunks.user
    var name by Trunks.name
    val calls by Call referrersOn Calls.trunk
}


object Users : IntIdTable("user") {
    val login = varchar("login", 50)
    val contacts = varchar("contacts", 50)
    val monitoring = integer("monitoring")
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var login by Users.login
    var contacts by Users.contacts
    val monitoring by Users.monitoring
    val trunks by Trunk referrersOn Trunks.user

    override fun toString() = "User(id=$id, login=$login)"

}