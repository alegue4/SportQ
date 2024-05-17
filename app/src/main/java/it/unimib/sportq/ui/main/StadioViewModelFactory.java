package it.unimib.sportq.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.sportq.data.repository.stadi.IStadioRepositoryWithLiveData;

public class StadioViewModelFactory implements ViewModelProvider.Factory {
    private final IStadioRepositoryWithLiveData stadioRepository;

    public StadioViewModelFactory(IStadioRepositoryWithLiveData stadioRepository) {
        this.stadioRepository = stadioRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StadioViewModel(stadioRepository);
    }
}
