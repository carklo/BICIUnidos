package com.sis.biciunidos;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AdaptadorLista extends BaseExpandableListAdapter 
{

	private ArrayList<Item> groups;
	private LayoutInflater inflater;
	private Activity activity;
	public AdaptadorLista(Activity act, ArrayList<Item> grupos) 
	{
		activity = act;
		groups = grupos;
		inflater = activity.getLayoutInflater();
	}
	
	@Override
	public int getGroupCount() 
	{
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) 
	{
		return groups.get(groupPosition).getChilds().size();
	}

	@Override
	public Object getGroup(int groupPosition) 
	{
		return groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) 
	{
		return groups.get(groupPosition).getChilds().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) 
	{
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) 
	{
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) 
	{
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.group_item, null);
		}
		Item ite = (Item) getGroup(groupPosition);
		TextView txt = (TextView)convertView.findViewById(R.id.tVie1);
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageVie1);
		txt.setText(ite.getNombre());
		iv.setImageResource(ite.getImagen());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) 
	{
		SubItem si = (SubItem) getChild(groupPosition, childPosition);
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.child_item, null);
		}
		TextView txt = (TextView) convertView.findViewById(R.id.tView1);
		txt.setText(si.getNombreComponente());
		final CheckBox ch = (CheckBox) convertView.findViewById(R.id.checkBox1);
		final RadioGroup rg = (RadioGroup) convertView.findViewById(R.id.radioGroup1);
		final EditText et = (EditText) convertView.findViewById(R.id.editText1);
		final View cv = convertView;
		ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
			{
				int sel = rg.getCheckedRadioButtonId();
				if(sel == -1)
				{
					AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
		            builder1.setMessage("Debe seleccionar algun estado de este componente");
		            builder1.setCancelable(true);
		            builder1.setPositiveButton("Aceptar",
		                    new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) 
		                {
		                    dialog.cancel();
		                }
		            });
		            AlertDialog alert11 = builder1.create();
		            alert11.show();
		            ch.setChecked(false);
				}
				else
				{
					RadioButton b = (RadioButton) cv.findViewById(sel);
					//String val = et.getText().toString();
					//TODO Guardar esta informacion
					Toast.makeText(activity, b.getText(), Toast.LENGTH_SHORT).show();
					rg.clearCheck();
					et.setText("");
					
				}
			}
		});
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
