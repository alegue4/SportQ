package it.unimib.sportq.data.repository.stadi;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.sportq.data.source.stadio.BaseStadioLocalDataSource;
import it.unimib.sportq.data.source.stadio.BaseStadioRemoteDataSource;
import it.unimib.sportq.model.Result;
import it.unimib.sportq.model.Stadio;

public class StadioRepositoryWithLiveData implements IStadioRepositoryWithLiveData, StadioResponseCallback {

    private final BaseStadioRemoteDataSource stadioRemoteDataSource;
    private final BaseStadioLocalDataSource stadioLocalDataSource;
    private final MutableLiveData<Result> stadioMutableLiveData;

    public StadioRepositoryWithLiveData(BaseStadioRemoteDataSource stadioRemoteDataSource, BaseStadioLocalDataSource stadioLocalDataSource){
        this.stadioRemoteDataSource = stadioRemoteDataSource;
        this.stadioLocalDataSource = stadioLocalDataSource;
        this.stadioMutableLiveData = new MutableLiveData<>();
        this.stadioRemoteDataSource.setStadioResponseCallback(this);
        this.stadioLocalDataSource.setStadioResponseCallback(this);
    }

    @Override
    public MutableLiveData<Result> getListaStadi() {
        stadioRemoteDataSource.getListaStadi();
        return stadioMutableLiveData;
    }
    @Override
    public void onSuccessFromFindingStadiums(List<Stadio> stadioList) {
        Result.StadioResponseSuccess result = new Result.StadioResponseSuccess(stadioList);
        stadioMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromFindingStadiums(String message) {
        Result.Error result = new Result.Error(message);
        stadioMutableLiveData.postValue(result);
    }

}
