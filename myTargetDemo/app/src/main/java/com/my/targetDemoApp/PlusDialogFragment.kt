package com.my.targetDemoApp

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class PlusDialogFragment : androidx.fragment.app.DialogFragment() {

    private var saveTypeListener: ((CustomAdvertisingType) -> Unit)? = null

    private val cancelListener = DialogInterface.OnClickListener { _, _ -> dismiss() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogLayout = FrameLayout(requireContext())
        val v = requireActivity().layoutInflater.inflate(R.layout.fragment_dialog, dialogLayout,
                false)

        dialogLayout.addView(v)
        val editText = v.findViewById<EditText>(R.id.editText)
        val radioGroup = v.findViewById<RadioGroup>(R.id.rg_choose_ad)
        val nameEditText = v.findViewById<EditText>(R.id.et_name)
        return AlertDialog.Builder(requireActivity())
                .setView(dialogLayout)
                .setPositiveButton(R.string.ok) { _, _ ->
                    val slotId = editText?.text?.toString()
                            ?.toIntOrNull()

                    if (slotId == null) {
                        Toast.makeText(requireContext(), "Null slot, cannot save",
                                Toast.LENGTH_SHORT)
                                .show()
                    }
                    else {
                        val type: CustomAdvertisingType.AdType =
                                when (radioGroup.checkedRadioButtonId) {
                                    R.id.adtype_banner_320x50  -> CustomAdvertisingType.AdType.STANDARD_320X50
                                    R.id.adtype_banner_300x250 -> CustomAdvertisingType.AdType.STANDARD_300X250
                                    R.id.adtype_banner_728x90  -> CustomAdvertisingType.AdType.STANDARD_728X90
                                    R.id.adtype_banner_adaptive-> CustomAdvertisingType.AdType.STANDARD_ADAPTIVE
                                    R.id.adtype_interstitial   -> CustomAdvertisingType.AdType.INTERSTITIAL
                                    R.id.adtype_rewarded       -> CustomAdvertisingType.AdType.REWARDED
                                    R.id.adtype_instream       -> CustomAdvertisingType.AdType.INSTREAM
                                    R.id.adtype_native         -> CustomAdvertisingType.AdType.NATIVE_AD
                                    R.id.adtype_native_banner  -> CustomAdvertisingType.AdType.NATIVE_BANNER
                                    else                       -> CustomAdvertisingType.AdType.STANDARD_320X50
                                }
                        val checkedAdType = CustomAdvertisingType(type, slotId)
                        val name = nameEditText.text.toString()
                        if (name.isNotEmpty()) {
                            checkedAdType.name = name
                        }
                        saveTypeListener?.invoke(checkedAdType)
                    }

                }
                .setNegativeButton(R.string.cancel, cancelListener)
                .setTitle(R.string.add_ad_title)
                .create()
    }

    fun setSaveTypeListener(saveTypeListener: (CustomAdvertisingType) -> Unit) {
        this.saveTypeListener = saveTypeListener
    }
}
