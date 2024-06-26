package it.unimib.sportq.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.sportq.data.source.user.BaseUserAuthenticationRemoteDataSource;
import it.unimib.sportq.data.source.user.BaseUserDataRemoteDataSource;
import it.unimib.sportq.model.Result;
import it.unimib.sportq.model.Stadio;
import it.unimib.sportq.model.User;

public class UserRepository implements IUserRepository,UserResponseCallback{

    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> userFavoritesMutableLiveData;

    public UserRepository(BaseUserAuthenticationRemoteDataSource userRemoteDataSource, BaseUserDataRemoteDataSource userDataRemoteDataSource){
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.userFavoritesMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
        //qui mettere anche quelle delle prenotazioni
    }

    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        signIn(email, password);
        return userMutableLiveData;
    }
    public MutableLiveData<Result> getUser(String firstName, String lastName, String email, String password, int id_avatar, boolean isUserRegistered) {
        signUp(firstName, lastName, email, id_avatar, password);
        return userMutableLiveData;
    }
    public MutableLiveData<Result> getUserData(User user){
        userDataRemoteDataSource.getUserRealtime(user);
        return userMutableLiveData;
    }
    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }
    @Override
    public MutableLiveData<Result> deleteAccount() {
        userRemoteDataSource.deleteAccount();
        return userMutableLiveData;
    }

    @Override
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override
    public void resetPassword(String email) {
        userRemoteDataSource.sendEmailPasswordReset(email);
    }

    @Override
    public MutableLiveData<Result> setUserAvatar(User user, int selectedImage) {
        userDataRemoteDataSource.setUserAvatar(user, selectedImage);
        return userMutableLiveData;
    }

    @Override
    public void addStadiumToFavorite(User user, Stadio stadio) {
        userDataRemoteDataSource.addStadiumToFavorite(user, stadio);
    }

    @Override
    public void removeStadiumFromFavorite(User user, Stadio stadio) {
        userDataRemoteDataSource.removeStadiumFromFavorite(user, stadio);
    }

    public MutableLiveData<Result> getUserFavoriteList(User user) {
        userDataRemoteDataSource.getUserFavoriteList(user);
        return userFavoritesMutableLiveData;
    }

    @Override
    public void signUp(String firstName, String lastName, String email, int id_avatar, String password) {
        userRemoteDataSource.signUp(firstName, lastName, email, id_avatar, password);
    }

    @Override
    public void signIn(String email, String password) {
        userRemoteDataSource.signIn(email, password);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Stadio> favoriteList) {
        Result.StadioResponseSuccess result = new Result.StadioResponseSuccess(favoriteList);
        userFavoritesMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(null);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessDeleteUser(User user) {
        userDataRemoteDataSource.deleteUserRealtime(user);
    }

    @Override
    public void onSuccessDeleteUserRealtime() {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(null);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessSetAvatar(User user) {
        userDataRemoteDataSource.getUserRealtime(user);
    }
}