package it.unimib.sportq.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.sportq.model.Result;
import it.unimib.sportq.model.Stadio;
import it.unimib.sportq.model.User;

public interface IUserRepository {
    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getUser(String firstName, String lastName,String email, String password, int id_avatar, boolean isUserRegistered);
    MutableLiveData<Result> getUserData(User user);
    MutableLiveData<Result> logout();
    MutableLiveData<Result> deleteAccount();
    User getLoggedUser();
    void resetPassword(String email);
    MutableLiveData<Result> setUserAvatar(User user, int selectedImage);
    void addStadiumToFavorite(User user, Stadio stadio);
    void removeStadiumFromFavorite(User user, Stadio stadio);
    MutableLiveData<Result> getUserFavoriteList(User user);
    void signUp(String firstName, String lastName, String email, int id_avatar, String password);
    void signIn(String email, String password);
}
