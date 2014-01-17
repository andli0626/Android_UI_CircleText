package com.epoint.epointuniversal;

import android.app.Activity;
import android.os.Bundle;

import com.epoint.epointuniversal.pjq.R;

/**
 * 
 * @author lilin
 * @date 2014-1-17 上午11:37:25
 * @annotation 自定义跑马灯效果
 */
public class MainActivity extends Activity {

	ScrollTextView scrollText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		scrollText = (ScrollTextView) findViewById(R.id.scrolltxt);
		scrollText.setText("青海省人民政府行政服务和公共资源交易中心    青海省人民政府行政服务和公共资源交易中心");
	}
}
