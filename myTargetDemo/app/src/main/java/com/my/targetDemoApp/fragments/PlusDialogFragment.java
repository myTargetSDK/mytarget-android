package com.my.targetDemoApp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.my.targetDemoApp.AdTypes;
import com.my.targetDemoApp.R;

public class PlusDialogFragment extends DialogFragment
{

	private SaveTypeListener saveTypeListener;
	private EditText editText;
	private RadioGroup radioGroup;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		FrameLayout dialogLayout = new FrameLayout(getContext());
		View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog, dialogLayout,
				false);

		editText = (EditText) v.findViewById(R.id.editText);
		radioGroup = (RadioGroup) v.findViewById(R.id.radiogroup);

		dialogLayout.addView(v);

		return new AlertDialog.Builder(getActivity())
				.setView(dialogLayout)
				.setPositiveButton(R.string.ok, okListener)
				.setNegativeButton(R.string.cancel, cancelListener)
				.setTitle(R.string.add_ad_title)
				.create();
	}

	private DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if (saveTypeListener != null)
			{
				try
				{
					int slotId = Integer.parseInt(editText.getText().toString());
					int checkedAdType;
					switch (radioGroup.getCheckedRadioButtonId())
					{
						case R.id.adtype_banner_320x50:
						default:
							checkedAdType = AdTypes.AD_TYPE_320X50;
							break;
						case R.id.adtype_banner_300x250:
							checkedAdType = AdTypes.AD_TYPE_300X250;
							break;
						case R.id.adtype_banner_728x90:
							checkedAdType = AdTypes.AD_TYPE_728X90;
							break;
						case R.id.adtype_interstitial:
							checkedAdType = AdTypes.AD_TYPE_FULLSCREEN;
							break;
						case R.id.adtype_instream:
							checkedAdType = AdTypes.AD_TYPE_INSTREAM;
							break;
						case R.id.adtype_native:
							checkedAdType = AdTypes.AD_TYPE_NATIVE;
							break;
					}

					saveTypeListener.onSaveType(checkedAdType, slotId);
				} catch (NumberFormatException e)
				{
					Toast.makeText(getActivity(), getString(R.string.wrong_slot), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}
	};

	private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			dismiss();
		}
	};

	public void setSaveTypeListener(SaveTypeListener saveTypeListener)
	{
		this.saveTypeListener = saveTypeListener;
	}

	public interface SaveTypeListener
	{
		void onSaveType(int adType, int slotId);
	}
}
