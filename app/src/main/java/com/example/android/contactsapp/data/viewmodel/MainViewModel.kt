package com.example.android.contactsapp.data.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.example.android.contactsapp.data.model.Contact
import com.example.android.contactsapp.data.repository.ContactsRepository
import com.example.android.contactsapp.data.repository.source.ContactsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * An Android View Model to construct the UI, chose this view model
 * in order to serve a context for the ContentResolver without affecting
 * the view model lifecycle properties.
 * @param context - used explicitly for the ContentResolver.
 * @param contactsRepository - the repo from which data is being fetched from.
 */
class MainViewModel(context: Application, private val contactsRepository: ContactsRepository) :
    AndroidViewModel(context) {

    private val _allContacts = MutableLiveData<List<Contact>>()
    val allContacts: LiveData<List<Contact>> = _allContacts

    private val _currentContact = MutableLiveData<Contact>()
    val currentContact: LiveData<Contact> = _currentContact


    var query by mutableStateOf("")
    private var focusedText by mutableStateOf("")
    private lateinit var allContactsList: List<Contact>

    /**
     * Fetching contacts from the Contact Provider API.
     */
    fun fetchContacts(query: String) {
        viewModelScope.launch {
            val result = contactsRepository.fetchContacts(query)
            _allContacts.value = result
            allContactsList = result
        }
    }

    /**
     * Fetching all emails from a desired contact. already running
     * in a coroutine, so no need for one.
     * @see getContactByDisplayName
     */
    private fun fetchAllEmails(displayName: String) {
        val result = contactsRepository.fetchAllEmails(displayName)
        _currentContact.value?.allEmailsList = result
    }

    /**
     * Fetching all phones from a desired contact, already running
     * in a coroutine, so no need for one.
     * @see getContactByDisplayName
     */
    private fun fetchAllPhones(displayName: String) {
        val result = contactsRepository.fetchAllPhones(displayName)
        _currentContact.value?.allPhonesList = result
    }

    /**
     * Fetching contacts every time the text changes inside
     *  the search view, querying the relevant contacts.
     */
    fun onQueryChanged(text: String) {
        query = text
        viewModelScope.launch {
            val result = contactsRepository.fetchContacts(text)
            _allContacts.value = result
        }
    }

    /**
     * Saving the text state before typing.
     */
    fun onTextFocused(text: String) {
        focusedText = text
    }

    /**
     * Edit the desired email address in the database after focus is gone.
     */
    fun onEmailTextUnfocused(unfocusedText: String) {
        viewModelScope.launch {
            contactsRepository.editDesiredEmailColumn(focusedText, unfocusedText)
        }
    }

    /**
     * Edit the desired phone number in the database after focus is gone.
     */
    fun onPhoneTextUnfocused(unfocusedText: String) {
        viewModelScope.launch {
            contactsRepository.editDesiredPhoneColumn(focusedText, unfocusedText)
        }
    }

    /**
     * Initializing Contact class and its attributes,
     * after entering the detailed contact screen (SecondScreen).
     * Both fetching methods are awaiting results and thus -
     * are not running separate coroutines to avoid being
     * launch asynchronously.
     * @see fetchAllPhones
     * @see fetchAllEmails
     */
    fun getContactByDisplayName(displayName: String): Contact? {
        fetchAllEmails(displayName)
        fetchAllPhones(displayName)
        for ((index) in allContactsList.withIndex()) {
            if (allContactsList[index].name == displayName) {
                _currentContact.value = allContactsList[index]
                return allContactsList[index]
            }
        }
        return null
    }
}

/**
 * Factory function to enable context for the content resolver
 * in the datasource without affecting the ViewModel's lifecycle
 * using Application ViewModel context.
 */
@Suppress("UNCHECKED_CAST")
class MyViewModelFactory(private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val source = ContactsDataSource(application.contentResolver)
            MainViewModel(application, ContactsRepository(source, Dispatchers.IO)) as T
        } else throw IllegalArgumentException("Unknown ViewModel class")
    }
}