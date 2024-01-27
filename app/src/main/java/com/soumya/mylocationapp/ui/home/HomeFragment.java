package com.soumya.mylocationapp.ui.home;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.soumya.mylocationapp.databinding.FragmentHomeBinding;
import com.soumya.mylocationapp.location.ConnectivityReciever;
import com.soumya.mylocationapp.location.LocationService;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ConnectivityReciever connectivityReciever;
    private int receiverFlags;
    private Intent serviceIntent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        connectivityReciever = new ConnectivityReciever();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        boolean listenToBroadcastsFromOtherApps = false;
        if (listenToBroadcastsFromOtherApps) {
            receiverFlags = ContextCompat.RECEIVER_EXPORTED;
        } else {
            receiverFlags = ContextCompat.RECEIVER_NOT_EXPORTED;
        }
        ContextCompat.registerReceiver(super.getContext(),connectivityReciever, filter,receiverFlags);

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(connectivityReciever);
    }

    @Override
    public void onStart() {
        serviceIntent = new Intent(getActivity(), LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            getActivity().startForegroundService(serviceIntent);
        } else {
            getActivity().startService(serviceIntent);
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (serviceIntent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getActivity().startForegroundService(serviceIntent);
            } else {
                getActivity().startService(serviceIntent);
            }
        }
        super.onDestroy();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}