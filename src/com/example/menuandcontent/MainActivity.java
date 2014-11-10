package com.example.menuandcontent;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.os.Build;

public class MainActivity extends Activity {
	private int Width;//屏幕宽度
	
	private int MenuWidth;
	private LinearLayout MenuLinear;
	private RelativeLayout.LayoutParams MenuParams;
	private LinearLayout ContentLinear;
	private RelativeLayout.LayoutParams ContentParams;
	private Button Change;
	
	private Boolean IsMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Init();
		BindEvent();
	}
	private void Init()
	{
		Width  = getWindowManager().getDefaultDisplay().getWidth();
		IsMenu = false;//记录在默认的情况下隐藏Menu
		MenuLinear = (LinearLayout)findViewById(R.id.MenuLinear);
		ContentLinear = (LinearLayout)findViewById(R.id.ContentLinear);
		MenuParams = (RelativeLayout.LayoutParams)MenuLinear.getLayoutParams();
		ContentParams = (RelativeLayout.LayoutParams)ContentLinear.getLayoutParams();
		Change = (Button)findViewById(R.id.Change);
		MenuWidth = Width * 2 / 3;
		MenuParams.width = MenuWidth;
		MenuParams.leftMargin = - MenuWidth;
	}
	private void BindEvent()
	{
		Change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(IsMenu)
				{
					new ScrollTask().execute(-2);
					IsMenu = false;
				}
				else
				{
					new ScrollTask().execute(2);
					IsMenu = true;
				}
			}
		});
	}
	
	/**
	 * 由于切换content和menu的操作
	 * @author Coder
	 *
	 */
	class ScrollTask extends AsyncTask<Integer, Integer, Integer> 
	{
		@Override
		protected Integer doInBackground(Integer... speed) 
		{
			int leftMargin = MenuParams.leftMargin;
			while(true)
			{
				int length = speed[0];//标记本次循环的位移
				if(leftMargin < -Width / 4)
					length *= 8;
				else if(leftMargin < -Width / 6)
					length *= 6;
				else if(leftMargin < -Width / 8)
					length *= 4;
				else if(leftMargin < -Width / 10)
					length *= 2;
				leftMargin += length;
				if(leftMargin > 0)
				{
					leftMargin = 0;
					break;
				}
				if(leftMargin < -MenuWidth)
				{
					leftMargin = -MenuWidth;
					break;
				}
				publishProgress(leftMargin);
				sleep(2);
			}
			return leftMargin;
		}
		@Override
		protected void onProgressUpdate(Integer... leftMargin) 
		{
			MenuParams.leftMargin = leftMargin[0];
			MenuLinear.setLayoutParams(MenuParams);
		}
		@Override
		protected void onPostExecute(Integer leftMargin) 
		{
			MenuParams.leftMargin = leftMargin;
			MenuLinear.setLayoutParams(MenuParams);
		}
	}
	/**
	 * 使当前线程睡眠指定的毫秒数。
	 * 
	 * @param millis
	 *            指定当前线程睡眠多久，以毫秒为单位
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
}
