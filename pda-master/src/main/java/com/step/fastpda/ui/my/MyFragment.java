package com.step.fastpda.ui.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.step.fastpda.R;
import com.step.fastpda.databinding.FragmentMyBinding;
import com.step.fastpda.model.User;
import com.step.fastpda.ui.login.UserManager;
import com.step.fastpda.utils.PreferenceUtils;
import com.step.fastpda.utils.StatusBar;
import com.tech.libnavannotation.FragmentDestination;

/**
 * @author zhushubin
 * @date 2020-08-13.
 * GitHub：
 * email： 604580436@qq.com
 * description：
 */
@FragmentDestination(pageUrl = "main/tabs/my", asStarter = true)
public class MyFragment extends Fragment {
    private FragmentMyBinding mBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMyBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){


       User user= UserManager.get().getUser();
       if(user!=null){
           mBinding.setUser(user);
       }
        UserManager.get().refresh().observe(this, newUser -> {
            if (newUser != null) {
                mBinding.setUser(newUser);
            }
        });
        mBinding.actionLogout.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.fragment_my_logout))
                .setPositiveButton(getString(R.string.fragment_my_logout_ok), (dialog, which) -> {
                    dialog.dismiss();
                    PreferenceUtils.putString(getContext(),"PASSWORD","");
                    UserManager.get().logout();

                    getActivity().onBackPressed();
                }).setNegativeButton(getString(R.string.fragment_my_logout_cancel), null)
                .create().show());


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBar.lightStatusBar(getActivity(), false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        StatusBar.lightStatusBar(getActivity(), hidden);
    }
}
