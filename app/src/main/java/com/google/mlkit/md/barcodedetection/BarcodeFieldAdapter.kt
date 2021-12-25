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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.common.escape.Escaper
import com.google.mlkit.md.R
import com.google.mlkit.md.barcodedetection.BarcodeFieldAdapter.BarcodeFieldViewHolder

/** Presents a list of field info in the detected barcode.  */
internal class BarcodeFieldAdapter(private val barcodeFieldList: List<BarcodeField>) :
    RecyclerView.Adapter<BarcodeFieldViewHolder>() {

    internal class BarcodeFieldViewHolder private constructor(val view: View) : RecyclerView.ViewHolder(view) {

        private val labelView: TextView = view.findViewById(R.id.barcode_field_label)
        private val valueView: TextView = view.findViewById(R.id.barcode_field_value)

        fun bindBarcodeField(barcodeField: BarcodeField) {
            labelView.text = barcodeField.label
            valueView.text = barcodeField.value
            valueView.setOnClickListener {
                barcodeField.value.asUri()?.openInBrowser(view.context)
            }
        }

        companion object {

            fun create(parent: ViewGroup): BarcodeFieldViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.barcode_field, parent, false)
                return BarcodeFieldViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarcodeFieldViewHolder =
        BarcodeFieldViewHolder.create(parent)

    override fun onBindViewHolder(holder: BarcodeFieldViewHolder, position: Int) =
        holder.bindBarcodeField(barcodeFieldList[position]).also {

        }

    override fun getItemCount(): Int =
        barcodeFieldList.size
}

fun Uri?.openInBrowser(context: Context) {
    this ?: return // Do nothing if uri is null
    Log.d("Nurs","$this")
    val browserIntent = Intent(Intent.ACTION_VIEW, this)
    try {
    ContextCompat.startActivity(context, browserIntent, null)
    }catch (exc:Exception){
        Toast.makeText(context,"invalid Code",Toast.LENGTH_LONG).show()
    }
}
fun String?.asUri(): Uri? {
    return try {
        Uri.parse(this)
    } catch (e: Exception) {
        null
    }
}
