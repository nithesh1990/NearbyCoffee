package napps.com.nearbycoffee.Network;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    It searches if there is a provides method for application instance. If it's there it creates a dependency graph of Application->SharedPreferences.

    Next time we ask for sharedPreferences object it sees that there is application dependency. So it first creates Application instance and then sharedpreferences
    instance and gives that instance to us. So we don't have to worry about creating the

    There are complete dependencies and incomplete dependencies

    @Module
    class DripCoffeeModule {
    @Provides Heater provideHeater(Executor executor) {
        return new CpuHeater(executor);
        }
    }
    When compiling it, javac rejects the missing binding:

    [ERROR] COMPILATION ERROR :
    [ERROR] error: No binding for java.util.concurrent.Executor
               required by provideHeater(java.util.concurrent.Executor)
    Fix the problem either by adding an @Provides-annotated method for Executor, or by marking the module as incomplete. Incomplete modules are permitted to have missing dependencies.

    @Module(complete = false)
    class DripCoffeeModule {
    @Provides Heater provideHeater(Executor executor) {
        return new CpuHeater(executor);
        }
    }

    The Network Module class is replacement for below code

            OkHttpClient client = new OkHttpClient();

            // Enable caching for OkHttp
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(getApplication().getCacheDir(), cacheSize);
            client.setCache(cache);

            // Used for caching authentication tokens
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            // Instantiate Gson
            Gson gson = new GsonBuilder().create();
            GsonConverterFactory converterFactory = GsonConverterFactory.create(gson);

            // Build Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("https://api.github.com")
                                        .addConverterFactory(converterFactory)
                                        .client(client)  // custom client
                                        .build();


        The provider methods are grouped together so that their dependencies are interconnected.
        This module contains all the providers to make a network request using retrofit
 */



@Module
public class NetworkModule {

    private String baseUrl;

    public NetworkModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Cache providesOkHttpCache(Application application) {
        int cacheSize = 5 * 1024 * 1024; //5 mb
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;

    }

    @Provides
    @Singleton
    Gson providesGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //This is useful in converting java to json i.e during post
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(Cache cache) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.cache(cache);
        //We can add interceptor to provide headers and related information if required
        return okHttpClientBuilder.build();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }
}
