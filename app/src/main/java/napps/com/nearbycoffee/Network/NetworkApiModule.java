package napps.com.nearbycoffee.Network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by "nithesh" on 7/15/2017.
 */

/*
    This module is basically an extension of NetworkModule. Initially this was part of NetworkModule but that doesn't create true abstraction.
    So It was separated from NetworkModule to make networkmodule concentrates only on network components.

    This contains different provider methods that will provide api Interface for different API Services
 */
@Module
public class NetworkApiModule {

    @Provides
    @Singleton
    PlacesAPIInterface providesPlacesApiInterface(Retrofit retrofit){
        return retrofit.create(PlacesAPIInterface.class);
    }
}
