package it.unimib.sportq.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.sportq.R;
import it.unimib.sportq.adapter.AllStadiumsAdapter;
import it.unimib.sportq.data.repository.stadi.IStadioRepositoryWithLiveData;
import it.unimib.sportq.data.repository.user.IUserRepository;
import it.unimib.sportq.model.Result;
import it.unimib.sportq.model.Stadio;
import it.unimib.sportq.ui.welcome.UserViewModel;
import it.unimib.sportq.ui.welcome.UserViewModelFactory;
import it.unimib.sportq.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSport extends Fragment {
    private static final String TAG = FragmentSport.class.getSimpleName();
    private TextView nomeSport;
    private ImageView logoSport;

    private ImageView backButton;
    private ImageView mapButton;
    private StadioViewModel stadioViewModel;
    private UserViewModel userViewModel;
    private List<Stadio> listaStadi;
    public FragmentSport() {
        // Required empty public constructor
    }

    public static FragmentSport newInstance(String sportName, int sportImage) {
        FragmentSport fragment = new FragmentSport();
        Bundle args = new Bundle();
        args.putString("sportName", sportName);
        args.putInt("sportImage", sportImage);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IStadioRepositoryWithLiveData stadioRepository = ServiceLocator.getInstance().
                getStadioRepositoryWithLiveData(requireActivity().getApplication());
        stadioViewModel = new ViewModelProvider(
                requireActivity(),
                new StadioViewModelFactory(stadioRepository)).get(StadioViewModel.class);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        nomeSport = view.findViewById(R.id.sport_name);
        logoSport = view.findViewById(R.id.sport_image);

        nomeSport.setText(getArguments().getString("sportName"));
        logoSport.setImageResource(getArguments().getInt("sportImage"));

        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(item -> {
            Navigation.findNavController(item).navigate(R.id.action_fragmentSport_to_fragmentSearch);
        });

        stadioViewModel.getStadioMutableLiveData().observe(
                getViewLifecycleOwner(), result -> {
                    if (result.isSuccess()) {
                        listaStadi = ((Result.StadioResponseSuccess) result).getData();
                        Log.d(TAG, "Grandezza Lista Stadi: " + listaStadi.size());
                        AllStadiumsAdapter allStadiumsAdapter = new AllStadiumsAdapter(listaStadi,
                                new AllStadiumsAdapter.OnItemClickListener(){
                                    @Override
                                    public void onStadioItemClick(Stadio stadio) {
                                        String stadioName = stadio.getStadiumName();
                                        String stadioAddress = stadio.getAddress();
                                        if(!stadio.isFavorite())
                                        {
                                            new MaterialAlertDialogBuilder(requireContext())
                                                    .setTitle(stadioName)
                                                    .setMessage("Indirizzo: " + stadioAddress)
                                                    .setPositiveButton(R.string.add_favorite, (dialog, which) -> {
                                                        stadio.setFavorite(true);
                                                        userViewModel.addStadiumToFavorite(userViewModel.getLoggedUser(), stadio);


                                                        dialog.dismiss(); // Chiudi il dialog
                                                        Snackbar.make(
                                                                requireActivity().findViewById(android.R.id.content),
                                                                getString(R.string.added_favorite),
                                                                Snackbar.LENGTH_SHORT
                                                        ).show();
                                                    })
                                                    .setNegativeButton(R.string.close, (dialog, which) -> {
                                                        // Azione da eseguire quando l'utente preme "Annulla"
                                                        dialog.dismiss(); // Chiudi il dialog
                                                    })
                                                    .show();
                                        } else {
                                            new MaterialAlertDialogBuilder(requireContext())
                                                    .setTitle(stadioName)
                                                    .setMessage("Indirizzo: " + stadioAddress)
                                                    .setPositiveButton(R.string.remove_favorite, (dialog, which) -> {

                                                        userViewModel.removeStadiumFromFavorite(userViewModel.getLoggedUser(), stadio);
                                                        stadio.setFavorite(false);

                                                        dialog.dismiss(); // Chiudi il dialog
                                                        Snackbar.make(
                                                                requireActivity().findViewById(android.R.id.content),
                                                                getString(R.string.removed_favorite),
                                                                Snackbar.LENGTH_SHORT
                                                        ).show();
                                                    })
                                                    .setNegativeButton(R.string.close, (dialog, which) -> {
                                                        // Azione da eseguire quando l'utente preme "Annulla"
                                                        dialog.dismiss(); // Chiudi il dialog
                                                    })
                                                    .show();
                                        }
                                    }

                                });
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(requireContext(),
                                        LinearLayoutManager.VERTICAL, false);

                        RecyclerView recyclerViewStadiums = getView().findViewById(R.id.recycler_lista_stadi);
                        recyclerViewStadiums.setLayoutManager(layoutManager);
                        recyclerViewStadiums.setAdapter(allStadiumsAdapter);
                    } else {
                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                getErrorMessage(((Result.Error) result).getMessage()),
                                Snackbar.LENGTH_SHORT).show();
                    }
                });

        mapButton = view.findViewById(R.id.map);
        mapButton.setOnClickListener(item -> {

            List<Stadio> listaStadiMarker = new ArrayList<>();
            for (Stadio stadio : listaStadi) {
                listaStadiMarker.add(new Stadio(stadio.getStadiumName(), stadio.getLatitude(), stadio.getLongitude()));
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("listaStadi", new ArrayList<>(listaStadiMarker));
            Navigation.findNavController(item).navigate(R.id.action_fragmentSport_to_mapsActivityCurrentPlace, bundle);
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////
        NavController navController = Navigation.findNavController(view);

        // Aggiungi il listener per il cambiamento di destinazione
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Assicurati che il frammento sia ancora associato all'attività
            if (isAdded()) {
                // Ottieni il riferimento alla BottomNavigationView dall'attività
                BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation_view);

                // Imposta manualmente l'elemento "Home" come selezionato quando si è in questo frammento
                MenuItem homeItem = bottomNav.getMenu().findItem(R.id.fragmentSearch);
                homeItem.setChecked(destination.getId() == R.id.fragmentSearch);
            }
        });
    }

    private String getErrorMessage(String errorType) {
        return errorType;
    }

}