/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package me.iamcxa.remindme.cardfragment;

import commonVar.MainVar;
import commonVar.MainVar.TaskCursor;

import it.gmariotti.cardslib.library.view.CardListView;
import me.iamcxa.remindme.R;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * List with Cursor Example
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class ListCursorCardFragment extends BaseFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static MyCursorCardAdapter mAdapter;
	public static MyCursorCardAdapter getmAdapter() {
		return mAdapter;
	}

	public static void setmAdapter(MyCursorCardAdapter mAdapter) {
		ListCursorCardFragment.mAdapter = mAdapter;
	}

	private static CardListView mListView;
	private static String[] projection = TaskCursor.PROJECTION;
	private static String selection = null;
	private static String sortOrder = MainVar.DEFAULT_SORT_ORDER;
	private static String[] selectionArgs;
	private static Cursor cursor;
	private static Double Latitude;
	private static Double Longitude;

	/********************/
	/** Initialization **/
	/********************/
	private void init() {
		mAdapter = new MyCursorCardAdapter(getActivity());
		mListView = (CardListView) getActivity().findViewById(
				R.id.carddemo_list_cursor);

		if (mListView != null) {
			mListView.setAdapter(mAdapter);
		}
		
		getLoaderManager();
		
		// Force start background query to load sessions
		getLoaderManager().restartLoader(0, null, this);
		
		// LoaderManager.enableDebugLogging(true);
	}

	@Override
	public int getTitleResourceId() {
		return R.string.app_name;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceStat) {

		View root = inflater.inflate(R.layout.card_fragment_list_cursor,
				container, false);

		// mScrollView = (ScrollView) root.findViewById(R.id.card_scrollview);

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getLoaderManager().initLoader(0, null, this);
		init();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		Loader<Cursor> loader = null;
		loader = new CursorLoader(getActivity(), MainVar.CONTENT_URI,
				projection, selection, selectionArgs, sortOrder);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (getActivity() == null) {
			return;
		}
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}



	public static Double getLongitude() {
		return Longitude;
	}

	public static void setLongitude(Double longitude) {
		Longitude = longitude;
	}

	public static Double getLatitude() {
		return Latitude;
	}

	public static void setLatitude(Double latitude) {
		Latitude = latitude;
	}

	public static String[] getProjection() {
		return projection;
	}

	public static void setProjection(String[] projection) {
		ListCursorCardFragment.projection = projection;
	}

	public static String getSortOrder() {
		return sortOrder;
	}

	public static void setSortOrder(String sortOrders) {
		ListCursorCardFragment.sortOrder = sortOrders;
	}

	public static String getSelection() {
		return selection;
	}

	public static void setSelection(String selections) {
		ListCursorCardFragment.selection = selections;
	}

}
