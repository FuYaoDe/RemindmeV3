/*
 * 
 */
package me.iamcxa.remindme.fragment;

import me.iamcxa.remindme.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author cxa
 * 
 */
public class CardFragmentLoader2 extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)  {
		View 		view = inflater.inflate(R.layout.fragment_local, container, false);
		
		return view;

	}

}