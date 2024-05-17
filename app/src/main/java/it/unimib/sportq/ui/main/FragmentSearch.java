package it.unimib.sportq.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.sportq.R;
import it.unimib.sportq.adapter.SearchRecyclerViewAdapter;
import it.unimib.sportq.model.Sport;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearch extends Fragment {
    private static final String TAG = FragmentSearch.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSearch newInstance(String param1, String param2) {
        FragmentSearch fragment = new FragmentSearch();
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
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewSports = getView().findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        List<Sport> sportList = getSportList();

        SearchRecyclerViewAdapter searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(sportList,
                new SearchRecyclerViewAdapter.OnItemClickListener(){
                    @Override
                    public void onSportItemClick(Sport sportList) {
                        String sportName = sportList.getName();
                        int sportImage = sportList.getImage();

                        Log.d(TAG, "Sport name: " + sportName);
                        Log.d(TAG, "Sport Image Id: " + sportImage);

                        Bundle bundle = new Bundle();
                        bundle.putString("sportName", sportName);
                        bundle.putInt("sportImage", sportImage);

                        Navigation.findNavController(view).navigate(R.id.action_fragmentSearch_to_fragmentSport, bundle);
                    }
                });

        recyclerViewSports.setLayoutManager(layoutManager);
        recyclerViewSports.setAdapter(searchRecyclerViewAdapter);

    }
    private List<Sport> getSportList() {
        List<Sport> sportList = new ArrayList<>();

        // Aggiungi gli sport alla lista
        sportList.add(new Sport(getString(R.string.soccer), R.drawable.logo_calcio));
        sportList.add(new Sport(getString(R.string.basket), R.drawable.logo_basket));
        sportList.add(new Sport(getString(R.string.tennis), R.drawable.logo_tennis));
        sportList.add(new Sport(getString(R.string.football), R.drawable.logo_football));
        sportList.add(new Sport(getString(R.string.baseball), R.drawable.logo_baseball));
        sportList.add(new Sport(getString(R.string.volleyball), R.drawable.logo_pallavolo));
        sportList.add(new Sport(getString(R.string.bowling), R.drawable.logo_bowling));
        sportList.add(new Sport(getString(R.string.badminton), R.drawable.logo_badminton));
        sportList.add(new Sport(getString(R.string.pool), R.drawable.logo_biliardo));

        return sportList;
    }
}