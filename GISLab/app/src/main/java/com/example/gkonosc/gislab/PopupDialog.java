package com.example.gkonosc.gislab;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.esri.android.map.popup.PopupContainer;

/**
 * Created by bujulia on 20.11.2015.
 */
public class PopupDialog extends Dialog {
    private PopupContainer popupContainer;

    public PopupDialog(Context context, PopupContainer popupContainer) {
        super(context, android.R.style.Theme);
        this.popupContainer = popupContainer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(getContext());
        layout.addView(popupContainer.getPopupContainerView(), android.widget.LinearLayout.LayoutParams.FILL_PARENT, android.widget.LinearLayout.LayoutParams.FILL_PARENT);
        setContentView(layout, params);
    }
}
