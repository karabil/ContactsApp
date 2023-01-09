package com.example.android.contactsapp.data.model

data class Contact(
    var name: String? = null,
    var image: String? = null,
    var number: String? = null,
    var email: String? = null,
    var numberType: String? = null,
    var emailType: String? = null,
    var allPhonesList: List<Contact>? = null,
    var allEmailsList: List<Contact>? = null
    )