package it.unimib.sportq.data.repository.user;

import java.util.List;

import it.unimib.sportq.model.Stadio;
import it.unimib.sportq.model.User;

public interface UserResponseCallback {

    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessFromRemoteDatabase(User user);

    void onFailureFromRemoteDatabase(String message);
    void onSuccessLogout();

    void onSuccessFromRemoteDatabase(List<Stadio> favoriteList);

    void onSuccessDeleteUser(User user);
    void onSuccessDeleteUserRealtime();

    void onSuccessSetAvatar(User user);
}
