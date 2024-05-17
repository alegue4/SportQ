package it.unimib.sportq.data.repository.stadi;

import java.util.List;

import it.unimib.sportq.model.Stadio;

public interface StadioResponseCallback {
    void onSuccessFromFindingStadiums(List<Stadio> stadioList);
    void onFailureFromFindingStadiums(String message);
}
