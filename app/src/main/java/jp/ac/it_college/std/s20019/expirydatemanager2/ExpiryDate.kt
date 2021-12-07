package jp.ac.it_college.std.s20019.expirydatemanager2

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ExpiryDate : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var date: Date = Date()
    var title: String = ""
    var detail: String = ""
}