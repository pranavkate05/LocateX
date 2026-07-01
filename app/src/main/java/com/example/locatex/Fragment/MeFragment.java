package com.example.locatex.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.locatex.EditProfileActivity;
import com.example.locatex.HelpSupportActivity;
import com.example.locatex.LoginActivity2;
import com.example.locatex.PrivacyPolicyActivity;
import com.example.locatex.R;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locatex.AboutUsActivity;
import com.example.locatex.ContactUsActivity;

import com.example.locatex.R;
import com.example.locatex.SavedLocationActivity;
import com.example.locatex.SettingActivity;


public class MeFragment extends Fragment {
    ImageView imgProfile;
    TextView editName, editPhone, editEmail, editAddress,logoutbutton;
    Button btnEdit;
    LinearLayout myProfileLogout;

    CardView cardSavedLocation, cardSettings,
            cardHelp, cardPrivacy,
            cardAbout, cardContact, cardLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        imgProfile = view.findViewById(R.id.imgProfile);
        editName = view.findViewById(R.id.txtName);
        editPhone = view.findViewById(R.id.editPhone);
        editEmail = view.findViewById(R.id.editEmail);
        editAddress = view.findViewById(R.id.editAddress);
        btnEdit = view.findViewById(R.id.btnEdit);

        cardSavedLocation = view.findViewById(R.id.cardSavedLocation);
        cardSettings = view.findViewById(R.id.cardSettings);
        cardHelp = view.findViewById(R.id.cardHelp);
        cardPrivacy = view.findViewById(R.id.cardPrivacy);
        cardAbout = view.findViewById(R.id.cardAbout);
        cardContact = view.findViewById(R.id.cardContact);
        cardLogout = view.findViewById(R.id.cardLogout);

        cardSavedLocation.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SavedLocationActivity.class));
        });
        cardSettings.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        });
        cardHelp.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), HelpSupportActivity.class));
        });
        cardPrivacy.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
        });
        cardAbout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AboutUsActivity.class));
        });
        cardContact.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ContactUsActivity.class));
        });




        logoutbutton = view.findViewById(R.id.logoutbutton);
        logoutbutton.setOnClickListener(v -> {

            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(requireContext());

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLogin", false);
            editor.apply();

            Intent intent = new Intent(requireActivity(), LoginActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            requireActivity().finish();

        });


        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        loadProfile();

        return view;
    }
    private void loadProfile() {
        if (getActivity() == null) return;

        SharedPreferences sp = getActivity().getSharedPreferences("ProfileData", Context.MODE_PRIVATE);

        editName.setText(sp.getString("name", "Pranav"));
        editPhone.setText(sp.getString("phone", "7020407337"));
        editEmail.setText(sp.getString("email", "pranavkate@gmail.com"));
        editAddress.setText(sp.getString("address", "Nandura"));

        String image = sp.getString("image", "");

        if (!image.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(image))
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(imgProfile);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
    }
}