package com.my.targetDemoApp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.my.targetDemoApp.R;

public class PlusDialogFragment extends DialogFragment
{

	private SaveTypeListener saveTypeListener;
	private Spinner spinner;
	private EditText editText;

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

		spinner = (Spinner) v.findViewById(R.id.spinner);
		editText = (EditText) v.findViewById(R.id.editText);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.advertisement_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

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
					saveTypeListener.onSaveType(spinner.getSelectedItemPosition(), slotId);
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
