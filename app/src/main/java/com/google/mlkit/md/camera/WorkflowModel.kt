/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.md.camera

import android.app.Application
import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.md.barcodedetection.BarcodeField
import com.google.mlkit.md.barcodedetection.BarcodeInfoToDisplay
import com.google.mlkit.md.settings.PreferenceUtils
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.HashSet

/** View model for handling application workflow based on camera preview.  */
class WorkflowModel(application: Application) : AndroidViewModel(application) {

    val workflowState = MutableLiveData<WorkflowState>()
    val detectedBarcode = MutableLiveData<Barcode>()

    private val objectIdsToSearch = HashSet<Int>()

    var isCameraLive = false
        private set


    private val context: Context
        get() = getApplication<Application>().applicationContext

    /**
     * State set of the application workflow.
     */
    enum class WorkflowState {
        NOT_STARTED,
        DETECTING,
        DETECTED,
        CONFIRMING,
        CONFIRMED,
        SEARCHING,
        SEARCHED
    }

    @MainThread
    fun setWorkflowState(workflowState: WorkflowState) {
        if (workflowState != WorkflowState.CONFIRMED &&
            workflowState != WorkflowState.SEARCHING &&
            workflowState != WorkflowState.SEARCHED
        ) {
        }
        this.workflowState.value = workflowState
    }



    fun markCameraLive() {
        isCameraLive = true
        objectIdsToSearch.clear()
    }

    fun markCameraFrozen() {
        isCameraLive = false
    }

    fun getInfo(barcodeField: BarcodeField) : BarcodeInfoToDisplay {
        return BarcodeInfoToDisplay("Saks",
            "\"Sacae\" redirects here. For the South Australian College of Advanced Education, see University of South Australia.\n" +
                    "For the land of the Saka under the Sassanid dynasty, see Sakastan. Not to be confused with the Sakha, the endonym of the Yakut people of Siberia. For other uses, see Saka (disambiguation).\n" +
                    "Saka\n" +
                    "\n" +
                    "Cataphract-style parade armour of a Saka royal, also known as \"The Golden Warrior\", from the Issyk kurgan, a historical burial site near Almaty, Kazakhstan. Circa 400–200 BC.[1]\n" +
                    "Part of a series on\n" +
                    "Indo-European topics\n" +
                    "Indo-European migrations.gif\n" +
                    "show\n" +
                    "Languages\n" +
                    "show\n" +
                    "Philology\n" +
                    "show\n" +
                    "Origins\n" +
                    "show\n" +
                    "Archaeology\n" +
                    "show\n" +
                    "Peoples and societies\n" +
                    "show\n" +
                    "Religion and mythology\n" +
                    "show\n" +
                    "Indo-European studies\n" +
                    "vte\n" +
                    "The Saka, Śaka, Shaka, Śāka or Sacae (Old Persian: \uD800\uDFBF\uD800\uDFA3\uD800\uDFA0 Sakā; Kharosthi: \uD802\uDE2F\uD802\uDE10 Saka; Brahmi: Gupta allahabad sh.svg Gupta allahabad k.svg, Śaka; Sanskrit: शक Śaka, शाक Śāka; Ancient Greek: Σάκαι Sákai; Latin: Sacae; Chinese: 塞, old *Sək, mod. Sè, Sāi; Ancient Egyptian: \uD80C\uDEF4\uD80C\uDF9D\uD80C\uDFA1\uD80C\uDE09 sk, \uD80D\uDC20\uD80C\uDFBC\uD80C\uDE09 sꜣg) were a group of nomadic Iranian peoples who historically inhabited the northern and eastern Eurasian Steppe and the Tarim Basin.[2][3]\n" +
                    "\n" +
                    "Though closely related, the Sakas are to be distinguished from the Scythians of the Pontic Steppe and the Massagetae of the Aral Sea region,[3][4][5] although they form part of the wider Scythian cultures.[6] Like the European Scythians, the Sakas were ultimately derived from the earlier Andronovo culture. Their language formed part of the Scythian languages. Prominent archaeological remains of the Sakas include Arzhan,[7] Tunnug,[8] the Pazyryk burials,[9] the Issyk kurgan, Saka Kurgan tombs,[10] the Barrows of Tasmola[11] and possibly Tillya Tepe.\n" +
                    "\n" +
                    "In the 2nd century BC, many Sakas were driven by the Yuezhi from the steppe into Sogdia and Bactria and then to the northwest of the Indian subcontinent, where they were known as the Indo-Scythians.[12][13][14] Other Sakas invaded the Parthian Empire, eventually settling in Sistan, while others may have migrated to the Dian Kingdom in Yunnan, China. In the Tarim Basin and Taklamakan Desert region of Northwest China, they settled in Khotan, Yarkand, Kashgar and other places, which were at various times vassals to greater powers, such as Han China and Tang China.[15]",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Sun_emperor.JPG/220px-Sun_emperor.JPG",
            barcodeField.value
        )
    }

}
