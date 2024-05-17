package it.unimib.sportq.data.source.user;

import it.unimib.sportq.data.repository.user.UserResponseCallback;
import it.unimib.sportq.model.Stadio;
import it.unimib.sportq.model.User;

public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;
    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }
    public abstract void getUserRealtime(User user);
    public abstract void saveUserData(User user);
    public abstract void deleteUserRealtime(User user);
    public abstract void setUserAvatar(User user, int selectedImage);

    public abstract void addStadiumToFavorite(User user, Stadio stadio);
    public abstract void removeStadiumFromFavorite(User user, Stadio stadio);
    public abstract void getUserFavoriteList(User user);

}
