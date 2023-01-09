package com.example.android.contactsapp.data.repository.source

import android.content.ContentResolver
import android.content.ContentValues
import android.database.DatabaseUtils
import android.provider.ContactsContract
import android.util.Log
import com.example.android.contactsapp.data.model.Contact


/**
 * A data source from which we get the contacts from.
 * @param contentResolver - used to query the contacts data
 * through the Contacts Provider API.
 */
class ContactsDataSource(private val contentResolver: ContentResolver) {

    /**
     * Fetch all contacts from the contact provider.
     */
    fun fetchContacts(query: String): List<Contact> {
        val result: MutableList<Contact> = mutableListOf()

        // Query desired columns for a contact.
        val mCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
            ),
            "${ContactsContract.Contacts.DISPLAY_NAME} LIKE '%$query%'",
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        val dCursor = DatabaseUtils.dumpCursorToString(mCursor)
        Log.d("ContactsDataSource", dCursor)

        mCursor?.apply {
            val displayNameIndex =
                getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val photoUriIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
            while (moveToNext()) {
                result.add(
                    Contact(
                        name = getString(displayNameIndex), image = getString(photoUriIndex),
                    )
                )
            }
        }
        mCursor?.close()
        return result.toList()
    }

    /**
     * Fetching all phones of the contact sorted by type.
     */
    fun fetchAllPhones(displayName: String): List<Contact> {
        val result: MutableList<Contact> = mutableListOf()

        val mCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE
            ),
            "${ContactsContract.Contacts.DISPLAY_NAME} = '$displayName'",
            null,
            ContactsContract.CommonDataKinds.Phone.TYPE
        )

        // Nice display of the provider database for debugging purposes.
        val dCursor = DatabaseUtils.dumpCursorToString(mCursor)
        Log.d("ContactsDataSource", dCursor)

        mCursor?.apply {
            while (moveToNext()) {
                val numberIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val numberTypeIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
                result.add(
                    Contact(
                        number = getString(numberIndex), numberType = getString(numberTypeIndex)
                    )
                )
            }
        }
        mCursor?.close()
        return result.toList()
    }

    /**
     * Fetching all the emails of the contact sorted by type.
     */
    fun fetchAllEmails(displayName: String): List<Contact> {
        val emailsList: MutableList<Contact> = mutableListOf()

        val mCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Email.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.TYPE,
                ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID,
                ContactsContract.CommonDataKinds.Email._ID
            ),
            "${ContactsContract.Contacts.DISPLAY_NAME} = '${displayName}'",
            null,
            ContactsContract.CommonDataKinds.Email.TYPE
        )

        // Nice display of the provider database for debugging purposes.
        val dCursor = DatabaseUtils.dumpCursorToString(mCursor)
        Log.d("ContactsDataSource", dCursor)

        mCursor?.apply {
            while (moveToNext()) {
                val emailIndex = getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                val emailTypeIndex = getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)
                emailsList.add(
                    Contact(
                        email = mCursor.getString(emailIndex), emailType = getString(emailTypeIndex)
                    )
                )
            }
        }
        mCursor?.close()
        return emailsList.toList()
    }

    /**
     * Edit the desired email address column in
     * the database after user changed its value.
     */
    fun editDesiredEmailColumn(oldValue: String, newValue: String) {
        val contentValues = ContentValues()
        contentValues.put(ContactsContract.CommonDataKinds.Email.ADDRESS, newValue)
        contentResolver.update(
            ContactsContract.Data.CONTENT_URI,
            contentValues,
            "${ContactsContract.CommonDataKinds.Email.ADDRESS} = ?",
            arrayOf(oldValue)
        )
    }

    /**
     * Edit the desired phone number column in
     * the database after user changed its value.
     */
    fun editDesiredPhoneColumn(oldValue: String, newValue: String) {
        val contentValues = ContentValues()
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newValue)
        contentResolver.update(
            ContactsContract.Data.CONTENT_URI,
            contentValues,
            "${ContactsContract.CommonDataKinds.Phone.NUMBER} = ?",
            arrayOf(oldValue)
        )
    }
}