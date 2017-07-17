package napps.com.nearbycoffee.Location;

import android.app.Application;
import android.support.annotation.IntDef;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import napps.com.nearbycoffee.Utils.Constants;

/**
 * Created by "nithesh" on 7/16/2017.
 */

/*
    About @Singleton annotation in Dagger

    @Singleton does not really create a Singleton, it is just a Scope, it is advised to not use @Singleton as it is misleading, it gives the impression that we are infact getting a Singleton, but we are not.

    Let's say you annotate your database dependency with @Singleton and link with a Component, now let's say that you initialise this Component in Activities A and B, you will have different instances of your database in your two Activities which is something most people don't want.

    How do you overcome this?

    Initialise your Component once in your Application class and access it statically in other places like Activities or Fragments, now this could soon get out of hand if you have more than 20 Component's as you cannot initialise all of them in your Application class, doing so will also slow down your app launch time.

    The best solution according to me is to create a real Singleton, either double checked or of other variants and use this statically as getInstance() and use this under @Provides in your Module.

    I know it breaks my heart too, but please understand that @Singleton is not really a Singleton, it's a Scope.

    The singleton scope is only till the scope of it's component. So If we create that component in application its lifetime is till that application.
    If you create that component in some activity its lifetime will be till that activity is destroyed
 */
@Module
public class LocationModule {

    @IntDef(
            {LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
             LocationRequest.PRIORITY_HIGH_ACCURACY,
             LocationRequest.PRIORITY_LOW_POWER,
             LocationRequest.PRIORITY_NO_POWER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LocationPriority {}

    @Provides
    @Singleton
    @Named("default")
    LocationRequest providesLocationRequest(){
        return new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.LOCATION_UPDATE_INTERVAL_MS)
                .setFastestInterval(Constants.LOCATION_UPDATE_FASTEST_INTERVAL_MS)
                .setSmallestDisplacement(Constants.LOCATION_UPDATE_DISPLACEMENT_METERS);
    }

    @Provides
    @Singleton
    @Named("custom")
    LocationRequest providesCustomLocationRequest(int interval, int fastestInterval, @LocationPriority int priority, float updateDisplacement) {
        return new LocationRequest()
                .setPriority(priority)
                .setInterval(interval)
                .setFastestInterval(fastestInterval)
                .setSmallestDisplacement(updateDisplacement);
    }

    @Provides
    @Singleton
    LocationSettingsRequest providesLocationSettingsRequest(LocationRequest locationRequest){
         return new LocationSettingsRequest.Builder()
                 .addLocationRequest(locationRequest)
                 .build();
    }

    @Provides
    @Singleton
    FusedLocationProviderClient providesFusedLocationClient(Application application){
        return LocationServices.getFusedLocationProviderClient(application);
    }

    @Provides
    @Singleton
    SettingsClient providesSettingsClient(Application application){
        return LocationServices.getSettingsClient(application);
    }
}
