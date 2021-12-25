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

package com.google.mlkit.md.barcodedetection

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.mlkit.md.R
import com.google.mlkit.md.camera.WorkflowModel
import com.google.mlkit.md.camera.WorkflowModel.WorkflowState

/** Displays the bottom sheet to present barcode fields contained in the detected barcode.  */
class BarcodeResultFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        val view = layoutInflater.inflate(R.layout.barcode_bottom_sheet, viewGroup)

        val arguments = arguments
        val barcodeFieldList: ArrayList<BarcodeField> =
            if (arguments?.containsKey(ARG_BARCODE_FIELD_LIST) == true) {
                arguments.getParcelableArrayList(ARG_BARCODE_FIELD_LIST) ?: ArrayList()
            } else {
                Log.e(TAG, "No barcode field list passed in!")
                ArrayList()
            }
        val barcodeInfoToDisplay =
            ViewModelProviders.of(requireActivity())[WorkflowModel::class.java].getInfo(barcodeFieldList.first())
        val greeting = view.findViewById<ComposeView>(R.id.greeting)
        greeting.setContent {
            Greeting(barcodeInfoToDisplay)
        }

        return view
    }

    @Composable
    private fun Greeting(barcodeFieldList: BarcodeInfoToDisplay) {
        val context = LocalContext.current
        Column(modifier = Modifier.clickable {
            barcodeFieldList.barcodeUrl.asUri()?.openInBrowser(context)
        }) {
            var textFirstpart by remember { mutableStateOf(barcodeFieldList.description)}
            Row() {
                Image(
                    painter = rememberImagePainter(
                        data = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Sun_emperor.JPG/1024px-Sun_emperor.JPG",
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.logo_mlkit)
                            transformations(RoundedCornersTransformation(20F))
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                )
                Column(modifier = Modifier.height(128.dp)) {
                    Text(
                        text = barcodeFieldList.title,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                    var readyToDraw by remember { mutableStateOf(false) }
                    Text(
                        text = textFirstpart,
                        style = MaterialTheme.typography.h5,
                        overflow = TextOverflow.Clip,
                        modifier = Modifier.drawWithContent {
                            if (readyToDraw) drawContent()
                        }.padding(horizontal = 8.dp),
                        onTextLayout = { textLayoutResult ->
                            if (textLayoutResult.hasVisualOverflow) {
                                textFirstpart = getFitableText(textFirstpart)
//                                textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
                            } else {
                                readyToDraw = true
                            }
                        }
                    )
                }
            }
            Text(
                text = barcodeFieldList.description.substring(textFirstpart.length),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }

    }

    private fun getFitableText(textFirstpart: String) :String{
        val halfOfTheTextIndex = textFirstpart.length / 2
        var finaltext = textFirstpart.substring(0, halfOfTheTextIndex)
        if(!textFirstpart[halfOfTheTextIndex].isWhitespace()){
            val indexOfFirstSpace =  textFirstpart.substring(halfOfTheTextIndex).indexOf(' ')
            finaltext += textFirstpart.substring(halfOfTheTextIndex, halfOfTheTextIndex + indexOfFirstSpace)
        }
        Log.d("Nurs","$finaltext")
      return  finaltext
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        activity?.let {
            // Back to working state after the bottom sheet is dismissed.
            ViewModelProviders.of(it).get(WorkflowModel::class.java).setWorkflowState(WorkflowState.DETECTING)
        }
        super.onDismiss(dialogInterface)
    }

    companion object {

        private const val TAG = "BarcodeResultFragment"
        private const val ARG_BARCODE_FIELD_LIST = "arg_barcode_field_list"

        fun show(fragmentManager: FragmentManager, barcodeFieldArrayList: ArrayList<BarcodeField>) {
            val barcodeResultFragment = BarcodeResultFragment()
            barcodeResultFragment.arguments = Bundle().apply {
                putParcelableArrayList(ARG_BARCODE_FIELD_LIST, barcodeFieldArrayList)
            }
            barcodeResultFragment.show(fragmentManager, TAG)
        }

        fun dismiss(fragmentManager: FragmentManager) {
            (fragmentManager.findFragmentByTag(TAG) as BarcodeResultFragment?)?.dismiss()
        }
    }
}
