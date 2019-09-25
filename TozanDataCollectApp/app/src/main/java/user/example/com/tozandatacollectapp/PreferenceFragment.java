package user.example.com.tozandatacollectapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import user.example.com.tozandatacollectapp.Dialog.StoragePrefDialogFragment;
import user.example.com.tozandatacollectapp.sub.StorageAcquirer;

public class PreferenceFragment extends PreferenceFragmentCompat{

    public static final String PREF_DIALOG_TAG = PreferenceFragment.class.getSimpleName();

    private StorageAcquirer storageAcquirer;
    private SharedPreferences data;

    private String storagePath;
    private EditTextPreference storagePreference;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preference_test);
        StorageAcquirer.init(getActivity());
        storageAcquirer = StorageAcquirer.getInstance();

        data = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getStoragePrefValue();

        storagePreference = new EditTextPreference(getActivity());
        storagePreference.setTitle(R.string.preftitle_storage);
        storagePreference.setKey(getString(R.string.key_storage));
        storagePreference.setDefaultValue(storageAcquirer.getInternalStorageList().get(0));
        onStoragePrefChanged(storagePath);

        getPreferenceScreen().addPreference(storagePreference);

        storageAcquirer = StorageAcquirer.getInstance();

    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (getFragmentManager().findFragmentByTag(PREF_DIALOG_TAG) != null) {
            return;
        }

        if (preference.getKey().equals(getString(R.string.key_storage))) {
            DialogFragment dialogFragment = StoragePrefDialogFragment.newInstance();
            dialogFragment.show(getFragmentManager(), PREF_DIALOG_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void getStoragePrefValue(){
        storageAcquirer.reacquire();
        storagePath = data.getString(getString(R.string.key_storage), storageAcquirer.getInternalStorageList().get(0));
    }

    public void onStoragePrefChanged(String value){
        storagePath = value;
        storagePreference.setSummary(String.format(getString(R.string.prefsummary_storage), StorageAcquirer.toStorageRootPath(value)));
    }
}