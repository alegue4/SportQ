package it.unimib.sportq.data.repository.stadi;

import androidx.lifecycle.MutableLiveData;

import it.unimib.sportq.model.Result;

public interface IStadioRepositoryWithLiveData {
    MutableLiveData<Result> getListaStadi();
}

