package it.unimib.sportq.util;

import android.app.Application;

import it.unimib.sportq.data.repository.stadi.IStadioRepositoryWithLiveData;
import it.unimib.sportq.data.repository.stadi.StadioRepositoryWithLiveData;
import it.unimib.sportq.data.repository.user.IUserRepository;
import it.unimib.sportq.data.repository.user.UserRepository;
import it.unimib.sportq.data.source.stadio.BaseStadioLocalDataSource;
import it.unimib.sportq.data.source.stadio.BaseStadioRemoteDataSource;
import it.unimib.sportq.data.source.stadio.StadioLocalDataSource;
import it.unimib.sportq.data.source.stadio.StadioRemoteDataSource;
import it.unimib.sportq.data.source.user.BaseUserAuthenticationRemoteDataSource;
import it.unimib.sportq.data.source.user.BaseUserDataRemoteDataSource;
import it.unimib.sportq.data.source.user.UserAuthenticationRemoteDataSource;
import it.unimib.sportq.data.source.user.UserDataRemoteDataSource;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }



    public IStadioRepositoryWithLiveData getStadioRepositoryWithLiveData(Application application) {


        BaseStadioRemoteDataSource stadioRemoteDataSource = new StadioRemoteDataSource(application.getApplicationContext());
        BaseStadioLocalDataSource stadioLocalDataSource = new StadioLocalDataSource();

        return new StadioRepositoryWithLiveData(stadioRemoteDataSource, stadioLocalDataSource);
    }
    public IUserRepository getUserRepository(Application application) {


        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource = new UserAuthenticationRemoteDataSource();
        BaseUserDataRemoteDataSource userDataRemoteDataSource = new UserDataRemoteDataSource();

        return new UserRepository(userRemoteAuthenticationDataSource, userDataRemoteDataSource);
    }
}
