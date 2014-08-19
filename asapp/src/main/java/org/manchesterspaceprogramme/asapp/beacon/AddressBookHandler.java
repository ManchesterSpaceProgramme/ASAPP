package org.manchesterspaceprogramme.asapp.beacon;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dave on 05/07/14.
 */
public class AddressBookHandler {

    public static List<String> getPhoneNumbersFromContacts(Context ctx) {
        Map<String,String> phoneNumbers = getPhoneNumbersFromContacts(ctx.getContentResolver());
        return new ArrayList<String>(phoneNumbers.values());
    }

    public static Map<String,String> getPhoneNumbersFromContacts(ContentResolver cr) {

        Map<String,String> phoneNumbers = new HashMap<String, String>();

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);


        if (cur.getCount() > 0) {

            while (cur.moveToNext()) {

                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (name != null && name.toUpperCase().startsWith("MSP_")) {



                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                        Log.i("getPhoneNumbersFromContacts", String.format("name : %s, ID : %s ", name, id));

                        // get the phone number
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phone = pCur.getString(
                                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNumbers.put(name,phone);
                        }
                        pCur.close();
                    }
                }
            }
        }

        return Collections.unmodifiableMap(phoneNumbers);
    }


}
