package se.curtrune.lucy.classes

import java.util.Locale
import kotlinx.serialization.Serializable
@Serializable
class Contact : Content {
    var displayName: String? = null
    var email: String? = null
    @JvmField
    var phoneNumber: String? = null
    var id: Long = 0
    fun contains(string: String): Boolean {
        return (displayName + email + phoneNumber).lowercase(Locale.getDefault()).contains(
            string.lowercase(
                Locale.getDefault()
            )
        )
    }

    fun hasEmailAddress(): Boolean {
        return !email!!.isEmpty()
    }

    fun hasPhoneNumber(): Boolean {
        return !phoneNumber!!.isEmpty()
    }


    fun getPhoneNumber(): String {
        return phoneNumber!!
    }



    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    override fun toString(): String {
        return "Contact{" +
                "displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id=" + id +
                '}'
    }
}
