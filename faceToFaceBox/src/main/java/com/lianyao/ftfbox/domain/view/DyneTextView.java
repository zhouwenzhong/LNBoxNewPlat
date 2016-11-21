package com.lianyao.ftfbox.domain.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;

import com.lianyao.ftfbox.R;

public class DyneTextView extends TextView {

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		if (focused) {
			if(getId() == R.id.tv_yuyin) {
				Drawable drawable = getResources().getDrawable( R.drawable.yuyin_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.tv_shipin) {
				Drawable drawable = getResources().getDrawable( R.drawable.shipin_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_kuaijie ) {
				setBackgroundResource(R.drawable.btn_kuaijie_h);
			} else if(getId() == R.id.dtv_yeyey) {
				Drawable drawable = getResources().getDrawable( R.drawable.ye_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_baba) {
				Drawable drawable = getResources().getDrawable( R.drawable.ba_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_erzi) {
				Drawable drawable = getResources().getDrawable( R.drawable.er_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_sunzi) {
				Drawable drawable = getResources().getDrawable( R.drawable.sun_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_haoyou1) {
				Drawable drawable = getResources().getDrawable( R.drawable.hao_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_nainai) {
				Drawable drawable = getResources().getDrawable( R.drawable.nai_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_mama) {
				Drawable drawable = getResources().getDrawable( R.drawable.ma_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_nver) {
				Drawable drawable = getResources().getDrawable( R.drawable.nv_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_suner) {
				Drawable drawable = getResources().getDrawable( R.drawable.sn_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_haoyou2) {
				Drawable drawable = getResources().getDrawable( R.drawable.hy_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_cancle) {
				setBackgroundResource(R.drawable.btn_h);
			} else if(getId() == R.id.dtv_jietingshi || getId() == R.id.dtv_jietingfou) {
				setBackgroundResource(R.drawable.jieting_btn_h);
			} else if(getId() == R.id.dtv_yuyin) {
				setBackgroundResource(R.drawable.yuyin_h);
//    			Drawable drawable = getResources().getDrawable( R.drawable.icon_yy_h); 
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
//				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_shipin) {
				setBackgroundResource(R.drawable.shipin_h);
//    			Drawable drawable = getResources().getDrawable( R.drawable.icon_sp_h); 
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
//				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.tv_phone_fullscreen) {
				Drawable drawable = getResources().getDrawable( R.drawable.shipin_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.tv_phone_halfscreen) {
				Drawable drawable = getResources().getDrawable( R.drawable.shipin_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.tv_phone_bigsmallscreen) {
				Drawable drawable = getResources().getDrawable( R.drawable.shipin_h);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			}
//			else if(getId() == R.id.tv_phone_mike) {
//				Drawable drawable = getResources().getDrawable( R.drawable.yuyin_h);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				setCompoundDrawables(null, drawable, null, null);
//			}
//			else if(getId() == R.id.tv_phone_mute) {
//				Drawable drawable = getResources().getDrawable( R.drawable.yuyin_h);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				setCompoundDrawables(null, drawable, null, null);
//			}
			else if(getId() == R.id.tv_phone_camera) {
				Drawable drawable = getResources().getDrawable( R.drawable.camera_select);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			}  else if(getId() == R.id.tv_phone_over) {
				setBackgroundResource(R.drawable.shipin_bg_gua_h);
			} else if(getId() == R.id.dtv_shanchu) {
				setBackgroundResource(R.drawable.btn_h);
//    			Drawable drawable = getResources().getDrawable( R.drawable.icon_del_h);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_cut_call) {
				setBackgroundResource(R.drawable.shipin_bg_gua_h);
			} else if(getId() == R.id.dtv_exit_full_call) {
				setBackgroundResource(R.drawable.shipin_bg_gua_h);
			} else if(getId() == R.id.dtv_cut_yycall){
				setBackgroundResource(R.drawable.btn_jieshu_h);
			} else {
				setBackgroundResource(R.drawable.quan_h);
			}
		} else {
			if(getId() == R.id.tv_yuyin) {
				Drawable drawable = getResources().getDrawable( R.drawable.yuyin);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.tv_shipin) {
				Drawable drawable = getResources().getDrawable( R.drawable.shipin);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_kuaijie ) {
				setBackgroundResource(R.drawable.btn_kuaijie);
			} else if(getId() == R.id.dtv_yeyey) {
				Drawable drawable = getResources().getDrawable( R.drawable.ye);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_baba) {
				Drawable drawable = getResources().getDrawable( R.drawable.ba);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_erzi) {
				Drawable drawable = getResources().getDrawable( R.drawable.er);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_sunzi) {
				Drawable drawable = getResources().getDrawable( R.drawable.sun);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_haoyou1) {
				Drawable drawable = getResources().getDrawable( R.drawable.hao);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_nainai) {
				Drawable drawable = getResources().getDrawable( R.drawable.nai);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_mama) {
				Drawable drawable = getResources().getDrawable( R.drawable.ma);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_nver) {
				Drawable drawable = getResources().getDrawable( R.drawable.nv);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_suner) {
				Drawable drawable = getResources().getDrawable( R.drawable.sn);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_haoyou2) {
				Drawable drawable = getResources().getDrawable( R.drawable.hy);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_cancle) {
				setBackgroundResource(R.drawable.btn);
			} else if(getId() == R.id.dtv_jietingshi || getId() == R.id.dtv_jietingfou) {
				setBackgroundResource(R.drawable.jieting_btn);
			} else if(getId() == R.id.dtv_yuyin) {
				setBackgroundResource(R.drawable.yuyin);
//    			Drawable drawable = getResources().getDrawable( R.drawable.icon_yy);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_shipin) {
				setBackgroundResource(R.drawable.shipin);
//    			Drawable drawable = getResources().getDrawable( R.drawable.icon_sp);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.tv_phone_fullscreen) {
				Drawable drawable = getResources().getDrawable( R.drawable.shipin);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.tv_phone_halfscreen) {
				Drawable drawable = getResources().getDrawable( R.drawable.shipin);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.tv_phone_bigsmallscreen) {
				Drawable drawable = getResources().getDrawable( R.drawable.shipin);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			}
//			else if(getId() == R.id.tv_phone_mike) {
//				Drawable drawable = getResources().getDrawable( R.drawable.yuyin);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				setCompoundDrawables(null, drawable, null, null);
//			}
//			else if(getId() == R.id.tv_phone_mute) {
//				Drawable drawable = getResources().getDrawable( R.drawable.yuyin);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				setCompoundDrawables(null, drawable, null, null);
//			}
			else if(getId() == R.id.tv_phone_camera) {
				Drawable drawable = getResources().getDrawable( R.drawable.camera_bg);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.tv_phone_over) {
				setBackgroundResource(R.drawable.shipin_bg_gua);
			} else if(getId() == R.id.dtv_shanchu) {
				setBackgroundResource(R.drawable.btn);
//    			Drawable drawable = getResources().getDrawable( R.drawable.icon_del);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				setCompoundDrawables(null, drawable, null, null);
			} else if(getId() == R.id.dtv_cut_call) {
				setBackgroundResource(R.drawable.shipin_bg_gua);
			} else if(getId() == R.id.dtv_exit_full_call) {
				setBackgroundResource(R.drawable.shipin_bg_gua);
			} else if(getId() == R.id.dtv_cut_yycall){
				setBackgroundResource(R.drawable.btn_jieshu);
			} else {
				setBackgroundResource(R.drawable.img_num);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);

	}

	public DyneTextView(Context context) {
		super(context);
	}

	public DyneTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DyneTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
