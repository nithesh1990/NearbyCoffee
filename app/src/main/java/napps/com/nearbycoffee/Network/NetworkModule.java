package napps.com.nearbycoffee.Network;

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
    In Dagger, Modules are the supplier of objects. They create and give objects to other classes wherever necessary.
    Suppose we want sharedPreferences in many screens and fragments. In all those files, if we write code like this

        SharedPreferences sharedPreferences = context.getSharedPreferences(....);

    It creates lots of boilerplate code(duplicate code). Whenever we create sharedPreferences object it always refers to a single source.
    Why not we create at one place and access at all places instead of creating and accessing at all places?
    Dagger does this job for us. For any object you need, create a single @Provides method across the application. This method is used
    always to create the object.

    In the above code to create sharedPreferences we need context object. We also have to take the responsibility of creating that context object also.

    Application instance is nothing but a context instance. We can use application instance wherever context is required. Creating application instance
    in dagger is simple and easy as creating sharedpreferences instance. Now dagger knows that for sharedPreferences instance, it needs an application instance.
    It searches if there is a provides method for application instance. If 


 */



@Module
public class NetworkModule {

    private String baseUrl;

    public NetworkModule(String baseUrl){
        this.baseUrl = baseUrl;
    }



    //Creating shared preferences is so easy in dagger. This i
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application){
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
