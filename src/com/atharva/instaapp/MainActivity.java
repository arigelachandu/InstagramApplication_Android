package com.atharva.instaapp;

import org.json.JSONException;
import org.json.JSONObject;

import com.atharva.network.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity {

	private JSONObject imageData;
	private GridView gridView;
	private static int TILE_WIDTH = 220;
	int number = 0;
	GetInstagram request;
	Context contexto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.image_grid_view);

		request = new GetInstagram(
				"https://api.instagram.com/v1/tags/selfie/media/recent?access_token=1772844996.f5e225c.0b9510b90bd34127b46386c72412bf69",
				this);
		request.execute();

		contexto = this;

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		gridView.setNumColumns(metrics.widthPixels / TILE_WIDTH);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				Intent i = new Intent(MainActivity.this, DisplayImage.class);

				try {

					String url = imageData.getJSONArray("data")
							.getJSONObject(position).getJSONObject("images")
							.getJSONObject("standard_resolution")
							.getString("url");
					i.putExtra("url", url);
				} catch (JSONException e) {
					i.putExtra("url", "");
				}

				startActivity(i);
			}
		});
		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

	}

	private class GetInstagram extends AsyncTask<Void, Void, Void> {
		private String url;
		private Context c;

		public GetInstagram(String url, Context c) {
			super();
			this.url = url;
			this.c = c;
		}

		@Override
		protected Void doInBackground(Void... params) {
			imageData = GetJSONData.requestWebService(url);
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			gridView.setAdapter(new InstagramStreamAdapter(c, imageData, number));
		}

	}

}