package com.apps.paul.myclothes.Fragmentos.Ropa;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.paul.myclothes.R;

/**
 * Created by paul on 11/23/2017.
 */

public class RegistrarPrenda_Fragment extends Fragment {
    public interface RegistrarFragment_Events {
        public void registroFinalizado();
    }

    private RegistrarFragment_Events m_callback = null;
    public static String TAG = "RegistrarPrenda_Fragment ";

    public static RegistrarPrenda_Fragment newInstance(Bundle args) {
        RegistrarPrenda_Fragment fragment = new RegistrarPrenda_Fragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
            m_callback = (RegistrarFragment_Events) context;
        } catch (ClassCastException e) {
            Log.wtf(TAG, context.getClass().getSimpleName() + " Debe implementar la interfaz " + this.getClass().getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        m_callback = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registrar_ropa_fragment, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}
