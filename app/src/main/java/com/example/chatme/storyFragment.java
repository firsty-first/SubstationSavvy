package com.example.chatme;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.chatme.databinding.FragmentCalllBinding;
import com.example.chatme.databinding.FragmentChatBinding;
import com.example.chatme.databinding.FragmentStoryBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link storyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class storyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentStoryBinding binding;

    public storyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment storyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static storyFragment newInstance(String param1, String param2) {
        storyFragment fragment = new storyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        binding = FragmentStoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

   // @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding.animationView.cancelAnimation(); // Stop the animation
//        binding.animationView.clearAnimation();  // Clear any animation state
//    // Reset the animation progress
//    }

}