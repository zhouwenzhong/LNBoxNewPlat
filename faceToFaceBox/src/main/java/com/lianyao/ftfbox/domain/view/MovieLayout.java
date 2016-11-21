package com.lianyao.ftfbox.domain.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lianyao.ftfbox.R;
import com.lianyao.ftfbox.adapter.MovieAdapter;
import com.lianyao.ftfbox.domain.Contact;

public class MovieLayout extends LinearLayout {

	private MovieAdapter adapter;
	private Context context;
	
	private ShowOp showop;
	
	public MovieLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}
	
	public void setOnShowListener(ShowOp onShowListener) {
        this.showop = onShowListener;
    }
	
	public void setAdapter(MovieAdapter adapter) {
		this.adapter = adapter;
		this.removeAllViews();
		for(int i=0;i<adapter.getCount();i++){
			final Contact contact=adapter.getItem(i);
			View view=adapter.getView(i, null, null);
			final ImageView image = (ImageView) view.findViewById(R.id.movie_image);
			
			view.setPadding(10, 0, 10, 0);
			view.setFocusable(true);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showop.show(contact);
				}
			});
			this.setOrientation(HORIZONTAL);
			view.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean focus) {
					if(focus) {
						switch(contact.getImgTag()) {
						case 0:
							image.setBackgroundResource(R.drawable.ye_h);
							break;
						case 1:
							image.setBackgroundResource(R.drawable.ba_h);
							break;
						case 2:
							image.setBackgroundResource(R.drawable.er_h);
							break;
						case 3:
							image.setBackgroundResource(R.drawable.sun_h);
							break;
						case 4:
							image.setBackgroundResource(R.drawable.hao_h);
							break;
						case 5:
							image.setBackgroundResource(R.drawable.nai_h);
							break;
						case 6:
							image.setBackgroundResource(R.drawable.ma_h);
							break;
						case 7:
							image.setBackgroundResource(R.drawable.nv_h);
							break;
						case 8:
							image.setBackgroundResource(R.drawable.sn_h);
							break;
						case 9:
							image.setBackgroundResource(R.drawable.hy_h);
							break;
						}
					} else {
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
					}
				}
			});
			this.addView(view,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		}
	}
	
}


