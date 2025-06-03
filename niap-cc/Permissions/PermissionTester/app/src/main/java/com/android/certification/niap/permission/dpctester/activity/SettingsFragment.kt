package com.android.certification.niap.permission.dpctester.activity
/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.android.certification.niap.permission.dpctester.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
       // val c = this.requireContext()
        setPreferencesFromResource(R.xml.preference, rootKey)
        val intent = activity?.intent
        val array = intent?.getSerializableExtra("prefmap") as ArrayList<Pair<String,String>>?
        //Log.d("HashMapTest", array.toString());
        //Log.v("HashMapTest", hashMap!!["key"]!!)
        //createPreferenceHierarchy()
        preferenceScreen = createPreferenceHierarchy2(array)

    }
    private fun createPreferenceHierarchy2(array:ArrayList<Pair<String,String>>?): PreferenceScreen {
        val c= this.requireContext()
        val root = preferenceManager.createPreferenceScreen(c)
        for(pair in array!!){

            if(pair.first == "suite"){
                val categorypref = PreferenceCategory(c)
                categorypref.title = pair.second
                categorypref.isIconSpaceReserved = false
                root.addPreference(categorypref);
            } else if(pair.first=="module") {
                val pref1 = CheckBoxPreference(c)
                var values = pair.second.split(":");
                if(values.size>=2) {
                    pref1.title = values[0]
                    pref1.key = values[1]
                    pref1.isIconSpaceReserved = false
                    pref1.setDefaultValue(true)
                }
                root.addPreference(pref1);
            } else if(pair.first=="bool") {
                val pref1 = CheckBoxPreference(c)
                var values = pair.second.split(":");
                if(values.size>=2) {
                    pref1.title = values[0]
                    pref1.key = values[1]
                    pref1.isIconSpaceReserved = false
                    pref1.setDefaultValue(false)
                }
                root.addPreference(pref1);
            }
            }

        return root
    }

}
