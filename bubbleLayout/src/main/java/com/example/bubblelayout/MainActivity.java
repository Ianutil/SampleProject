package com.example.bubblelayout;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private BubbleLayout bubbleLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bubbleLayout = (BubbleLayout)findViewById(R.id.bubble_layout);

	}

	@Override
	protected void onStart() {
		super.onStart();

		bubbleLayout.startBubble();
	}

	@Override
	protected void onPause() {
		super.onPause();

		bubbleLayout.stopBubble();
	}
}
