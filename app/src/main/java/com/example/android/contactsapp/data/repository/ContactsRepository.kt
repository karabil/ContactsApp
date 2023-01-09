package com.example.android.contactsapp.data.repository

import com.example.android.contactsapp.data.model.Contact
import com.example.android.contactsapp.data.repository.source.ContactsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * A repository to fetch the contacts list from the data source.
 * @param source - the data source (using Contacts provider API).
 * @param coroutineDispatcher - a coroutine for IO operation, instead of hardcoding
 * used in a constructor to make testing easier later.
 */
class ContactsRepository(private val source: ContactsDataSource, private val coroutineDispatcher: CoroutineDispatcher) {

    suspend fun fetchContacts(query:String) :List<Contact> {
        return withContext(coroutineDispatcher) {
            source.fetchContacts(query)
        }
    }

     fun fetchAllPhones(id: String) :List<Contact> {
            return source.fetchAllPhones(id)
    }

     fun fetchAllEmails(id: String) :List<Contact> {
        return source.fetchAllEmails(id)

    }

    suspend fun editDesiredEmailColumn(oldValue: String, newValue: String) {
         withContext(coroutineDispatcher) {
            source.editDesiredEmailColumn(oldValue, newValue)
        }
    }

    suspend fun editDesiredPhoneColumn(oldValue: String, newValue: String) {
         withContext(coroutineDispatcher) {
            source.editDesiredPhoneColumn(oldValue, newValue)
        }
    }
}