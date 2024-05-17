package it.unimib.sportq.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

import it.unimib.sportq.R;
import it.unimib.sportq.adapter.PreferitiAdapter;
import it.unimib.sportq.data.repository.user.IUserRepository;
import it.unimib.sportq.model.Result;
import it.unimib.sportq.model.Stadio;
import it.unimib.sportq.ui.welcome.UserViewModel;
import it.unimib.sportq.ui.welcome.UserViewModelFactory;
import it.unimib.sportq.util.ServiceLocator;

public class FragmentHome extends Fragment {
    private TextView frase_motivazionale;
    private List<Stadio> listaPreferiti;
    private UserViewModel userViewModel;

    public FragmentHome() {
        // Required empty public constructor
    }

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        frase_motivazionale = view.findViewById(R.id.motivational_quotes);
        frase_motivazionale.setText(randomQuotes());



        userViewModel.getUserFavoriteListMutableLiveData(userViewModel.getLoggedUser()).observe(getViewLifecycleOwner(), result -> {
            if (result != null && result.isSuccess()) {
                listaPreferiti = ((Result.StadioResponseSuccess) result).getData();


                PreferitiAdapter preferitiAdapter = new PreferitiAdapter(listaPreferiti,
                        new PreferitiAdapter.OnItemClickListener(){
                            @Override
                            public void onStadioItemClick(Stadio stadio) {

                                String stadioName = stadio.getStadiumName();
                                String stadioAddress = stadio.getAddress();

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
                        });
                RecyclerView.LayoutManager layoutManager =
                        new LinearLayoutManager(requireContext(),
                                LinearLayoutManager.HORIZONTAL, false);

                RecyclerView recyclerFavorites = getView().findViewById(R.id.recycler_favorites);
                recyclerFavorites.setLayoutManager(layoutManager);
                recyclerFavorites.setAdapter(preferitiAdapter);

                preferitiAdapter.notifyDataSetChanged();

            }
        });




    }

    public String randomQuotes()
    {
        String[] frase = new String[10];
            frase[0] = "Arrivare secondo significa soltanto essere il primo degli sconfitti.";
            frase[1] = "La forza mentale distingue i campioni dai quasi campioni.";
            frase[2] = "Se non credi in te stesso, troverai sempre un modo per non vincere.";
            frase[3] = "Chi non dà tutto, non dà niente.";
            frase[4] = "Non conta se finisci per terra, conta se ti rialzi.";
            frase[5] = "Se non credi in te stesso, nessuno lo farà per te.";
            frase[6] = "I limiti esistono solo nell’anima di chi è a corto di sogni.";
            frase[7] = "Un vincitore è un sognatore che non si è arreso.";
            frase[8] = "Piu è ardua la sfida, più è grande la vittoria.";
            frase[9] = "Il fallimento non è il contrario del successo, ma una parte del successo.";
        Random random = new Random();
        return frase[random.nextInt(10)];
    }
}


