package com.tracyzhang.xgif.adapter;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tracyzhang.xgif.R;
import com.tracyzhang.xgif.util.FaceManager;
import com.tracyzhang.xgif.view.AnimatedGifDrawable;
import com.tracyzhang.xgif.view.AnimatedImageSpan;


/**
 * 
 * @author tracyZhang  QQ:381356799
 *
 */

public class ChatAdapter extends BaseAdapter{
	
	private Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
	private FaceManager fm;
	private Context cxt;
	private List<String> data;

	public ChatAdapter(Context cxt, List<String> data) {
		super();
		this.cxt = cxt;
		this.data = data;
		fm = FaceManager.getInstance();
	}

	@Override
	public int getCount() {
		if(null == data)
			return 0;
		return data.size();
	}

	@Override
	public Object getItem(int pos) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(null == convertView){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(cxt).inflate(R.layout.list_item, null);
			holder.msg = (TextView) convertView.findViewById(R.id.item_msg);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.msg.setText(convertNormalStringToSpannableString(data.get(position),holder.msg));
		return convertView;
	}
	
	class ViewHolder{
		TextView msg;
	}
	
	/**
	 * check the cacke first
	 * @param message
	 * @param tv
	 * @return
	 */
	private SpannableString convertNormalStringToSpannableString(String message , final TextView tv) {
		SpannableString value = SpannableString.valueOf(message);
		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				int face = fm.getFaceId(str2);
				if(-1!=face){//wrapping with weakReference
					WeakReference<AnimatedImageSpan> localImageSpanRef = new WeakReference<AnimatedImageSpan>(new AnimatedImageSpan(new AnimatedGifDrawable(cxt.getResources().openRawResource(face), new AnimatedGifDrawable.UpdateListener() {   
					    @Override
					    public void update() {//update the textview
					        tv.postInvalidate();
					    }
					})));
					value.setSpan(localImageSpanRef.get(), k, m, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				}
			}
		}
		return value;
	}
	
}
