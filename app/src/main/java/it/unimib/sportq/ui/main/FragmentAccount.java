package it.unimib.sportq.ui.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;



import it.unimib.sportq.R;
import it.unimib.sportq.adapter.AvatarAdapter;
import it.unimib.sportq.data.repository.user.IUserRepository;
import it.unimib.sportq.model.Result;
import it.unimib.sportq.model.User;
import it.unimib.sportq.ui.welcome.UserViewModel;
import it.unimib.sportq.ui.welcome.UserViewModelFactory;
import it.unimib.sportq.util.ServiceLocator;

public class FragmentAccount extends Fragment {

    private static String TAG = FragmentAccount.class.getSimpleName();

    private TextView email;
    private ImageView avatar;
    private TextView first_name;
    private TextView last_name;
    private UserViewModel userViewModel;

    public FragmentAccount() {
        // Required empty public constructor
    }

    public static FragmentAccount newInstance(String param1, String param2) {
        FragmentAccount fragment = new FragmentAccount();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        Button logoutButton = view.findViewById(R.id.logout_button);
        Button deleteButton = view.findViewById(R.id.delete_account_button);

        first_name = view.findViewById(R.id.user_firstname);
        last_name = view.findViewById(R.id.user_lastname);
        avatar = view.findViewById(R.id.avatar_image);
        email = view.findViewById(R.id.user_email);

        userViewModel.getUserMutableLiveData(userViewModel.getLoggedUser()).observe(
                getViewLifecycleOwner(), result -> {
                    if (result.isSuccess()) {
                        User user = ((Result.UserResponseSuccess) result).getData();
                        if (user != null) {
                            first_name.setText(user.getFirstName());
                            last_name.setText(user.getLastName());
                            email.setText(user.getEmail());
                            int resourceId = user.getId_avatar();
                            Log.d(TAG, user.toString());
                            Context context = view.getContext();
                            Drawable drawable = ContextCompat.getDrawable(context, resourceId);
                            avatar.setImageDrawable(drawable);
                        }
                    } else {
                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                getErrorMessage(((Result.Error) result).getMessage()),
                                Snackbar.LENGTH_SHORT).show();
                    }
                });

        // GESTIONE DEL CLICK DEL PULSANTE LOGOUT
        logoutButton.setOnClickListener(v -> {
            userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
                if (result.isSuccess()) {
                    Log.d(TAG, "entraaa?LOGOUT");
                    Navigation.findNavController(view).navigate(
                            R.id.action_fragmentAccount_to_loginPage);
                } else {
                    Snackbar.make(view,
                            requireActivity().getString(R.string.unexpected_error),
                            Snackbar.LENGTH_SHORT).show();
                }
            });
        });

        deleteButton.setOnClickListener(v ->{new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_account_question)
                .setMessage(R.string.delete_account_message)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {

                    userViewModel.deleteAccount().observe(getViewLifecycleOwner(), result -> {
                        if (result.isSuccess()) {
                            Log.d(TAG, "entraaa?1");
                            Navigation.findNavController(view).navigate(
                                    R.id.action_fragmentAccount_to_registrationPage);
                            Log.d(TAG, "entraaa?2");
                        } else {
                            Snackbar.make(view,
                                    requireActivity().getString(R.string.unexpected_error),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    dialog.dismiss(); // Chiudi il dialog
                })
                .setNegativeButton(R.string.close, (dialog, which) -> {
                    // Azione da eseguire quando l'utente preme "Annulla"
                    dialog.dismiss(); // Chiudi il dialog
                })
                .show();

        });

        // GESTIONE DEL CLICK DELL'IMMAGINE AVATAR
        avatar.setOnClickListener(v -> showPopup(v));

    } // FINE METODO ONVIEWCREATED()
    private String getErrorMessage(String errorType) {
        return errorType;
    }

    private void showPopup(View anchorView) {

        // Infla il layout del pop up
        View popupView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.popup_avatar, null);
        // Creazione del pop up
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // Inizializzazione del gridView nel popupView
        GridView gridView = popupView.findViewById(R.id.grid_view_avatar);
        // Creazione e impostazione dell'adapter per il gridView
        AvatarAdapter adapter = new AvatarAdapter(anchorView.getContext());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick() triggered");
                // Ottieni l'ID dell'immagine cliccata
                int selectedImageId = (int) view.getTag();
                Log.d(TAG, "Selected Image ID: " + selectedImageId);
                userViewModel.setUserAvatar(userViewModel.getLoggedUser(), selectedImageId).observe(
                        getViewLifecycleOwner(), result -> {
                            if (result.isSuccess()) {
                                //User user = ((Result.UserResponseSuccess) result).getData();
                                Context context = view.getContext();
                                Drawable drawable = ContextCompat.getDrawable(context, selectedImageId);
                                avatar.setImageDrawable(drawable);
                                popupWindow.dismiss();
                            } else {
                                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                        getErrorMessage(((Result.Error) result).getMessage()),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // animazione di entrata se lo desideri
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // Visualizzazione del popup nella posizione desiderata, ad esempio, al centro dell'ancora
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}