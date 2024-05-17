package it.unimib.sportq.data.source.stadio;

import it.unimib.sportq.data.repository.stadi.StadioResponseCallback;

public abstract class BaseStadioRemoteDataSource {
    protected StadioResponseCallback stadioResponseCallback;

    public void setStadioResponseCallback(StadioResponseCallback stadioResponseCallback) {
        this.stadioResponseCallback = stadioResponseCallback;
    }

    public abstract void getListaStadi();
}
