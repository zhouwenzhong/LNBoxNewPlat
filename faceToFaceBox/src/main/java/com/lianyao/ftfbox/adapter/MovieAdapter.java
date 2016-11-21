package com.lianyao.ftfbox.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianyao.ftfbox.R;
import com.lianyao.ftfbox.domain.Contact;

public class MovieAdapter extends BaseAdapter {

	private List<Contact> list;
	private Context context;

	public MovieAdapter(Context context) {
		this.context = context;
		this.list = new ArrayList<Contact>();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Contact getItem(int location) {
		return list.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void addObject(Contact contact) {
		list.add(contact);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int location, View arg1, ViewGroup arg2) {
		View view = LayoutInflater.from(context).inflate(R.layout.movie, null);
		ImageView image = (ImageView) view.findViewById(R.id.movie_image);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		TextView tv_mobile = (TextView) view.findViewById(R.id.tv_mobile);
		Contact contact = getItem(location);
		view.setTag(contact);
		switch(contact.getImgTag()) {
		case 0:
			image.setBackgroundResource(R.drawable.ye);
			break;
		case 1:
			image.setBackgroundResource(R.drawable.ba);
			break;
		case 2:
			image.setBackgroundResource(R.drawable.er);
			break;
		case 3:
			image.setBackgroundResource(R.drawable.sun);
			break;
		case 4:
			image.setBackgroundResource(R.drawable.hao);
			break;
		case 5:
			image.setBackgroundResource(R.drawable.nai);
			break;
		case 6:
			image.setBackgroundResource(R.drawable.ma);
			break;
		case 7:
			image.setBackgroundResource(R.drawable.nv);
			break;
		case 8:
			image.setBackgroundResource(R.drawable.sn);
			break;
		case 9:
			image.setBackgroundResource(R.drawable.hy);
			break;
		}
		tv_name.setText(contact.getName());
		tv_mobile.setText(contact.getMobile());
		return view;
	}

}
