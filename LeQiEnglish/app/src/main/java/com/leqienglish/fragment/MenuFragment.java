package com.leqienglish.fragment;


import com.leqienglish.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class MenuFragment  extends Fragment{
	  int index = -1;
	    private View view;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        setRetainInstance(true);
	        //set the preference xml to the content view
	        
	    }
	    
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	       // Log.e("Krislq", "onCreateView:"+ text);
	        //inflater the layout 
	        this.view = inflater.inflate(R.layout.frame_menu, null);
	       // TextView textView =(TextView)view.findViewById(R.id.textView);
	       // if(!TextUtils.isEmpty(text)) {
	          //  textView.setText(text);
	        //}
	        //this.handler = new Handler(this);
			//this.init();
	        return view;
	    }
}
