package napps.com.nearbycoffee;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by "nithesh" on 7/15/2017.
 */

/*
    We group the creation of singleton instances into one group to make sure that they are related.
    Application module has application instance which gives context, which can be useful in instantiating sharedpreferences, system services
    or any singleton classes that depend on context.

    Make sure all the interdependent objects in the dependency/object graph are all created in one interface class.

    The SharedPreferences was actually present in the NetworkModule which makes irrelevant given the type of objects created
    in network module class. So it was moved here to have corresponding dependencies.
 */
@Module
public class AppModule {

    private Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return application;
    }
}
