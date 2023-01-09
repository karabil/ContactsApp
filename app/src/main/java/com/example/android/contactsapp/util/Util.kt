package com.example.android.contactsapp.util

import android.provider.ContactsContract

/**
 * Utility object to help transform database numbers into readable text.
 *
 */
object Util {

    /**
     * @param phoneTypeNum - phone type as declared in the Contact Provider API.
     */
    fun replacePhoneTypeWithDescription(phoneTypeNum: Int) :String{
       return when(phoneTypeNum) {
           ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> "Home"
           ContactsContract.CommonDataKinds.Phone.TYPE_CAR -> "Car"
           ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK -> "Call Back"
           ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT -> "Assitant"
           ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN -> "Company Main"
           ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM -> "Custom"
           ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME -> "Fax Home"
           ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK -> "Fax Work"
           ContactsContract.CommonDataKinds.Phone.TYPE_ISDN -> "ISDN"
           ContactsContract.CommonDataKinds.Phone.TYPE_MAIN -> "Main"
           ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> "Mobile"
           ContactsContract.CommonDataKinds.Phone.TYPE_MMS -> "MMS"
           ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> "Work"
           ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE -> "Mobile"
           ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER -> "Pager"
           ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD -> "TTY TDD"
           ContactsContract.CommonDataKinds.Phone.TYPE_TELEX -> "Telex"
           ContactsContract.CommonDataKinds.Phone.TYPE_RADIO -> "Radio"
           ContactsContract.CommonDataKinds.Phone.TYPE_PAGER -> "Pager"
           else -> {
               "Other Case"
           }
       }
    }

    /**
     * @param emailTypeNum - email type as declared in the Contact Provider API.
     */
    fun replaceEmailTypeWithDescription(emailTypeNum: Int) :String{
        return when(emailTypeNum) {
            ContactsContract.CommonDataKinds.Email.TYPE_HOME -> "Home"
            ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM -> "Custom"
            ContactsContract.CommonDataKinds.Email.TYPE_WORK -> "Work"
            ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> "Mobile"
            ContactsContract.CommonDataKinds.Email.TYPE_OTHER -> "Other"
            else -> {
                "Other Case"
            }
        }
    }
}