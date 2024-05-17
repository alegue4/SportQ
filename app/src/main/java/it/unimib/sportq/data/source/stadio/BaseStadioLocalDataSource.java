package it.unimib.sportq.data.source.stadio;

import java.util.List;

import it.unimib.sportq.data.repository.stadi.StadioResponseCallback;
import it.unimib.sportq.model.Stadio;

public abstract class BaseStadioLocalDataSource {
    protected StadioResponseCallback stadioResponseCallback;

    public void setStadioResponseCallback(StadioResponseCallback stadioResponseCallback) {
        this.stadioResponseCallback = stadioResponseCallback;
    }
    public abstract void saveStadiumListInRoom(List<Stadio> stadioList);
    public abstract void deleteAllFromLocal();
}
