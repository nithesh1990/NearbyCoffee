package napps.com.nearbycoffee.Utils;

/**
 * Created by "nithesh" on 7/16/2017.
 */

/*
    The reason for making the class final and having a private constructor is outlined below:

    Generally we see a need of a constants file in an application, which stores constants to be shared across multiple locations. While designing an app, I came across a similar situation, where we required various constants to be used at multiple places across the application.

    I was sure, that I need a separate file which stores public static constants. But I wasn't very sure to make it an Interface or a Class. (Enum was not suitable for my requirement). So I had two options to chose from:

    Either an Interface, e.g.

    package one;
    public interface Constants {
    String NAME="name1";
    int MAX_VAL=25;
    }
    Or

    package two;
    public class Constants {
    public static final String NAME="name1";
    public static final int MAX_VAL=25;
    }


    I was of a view, to use Interfaces. My argument was that since the interface automatically makes fields as public static and final, it would be advantageous just in case someone misses adding them to a new constant. It also makes the code look simpler and cleaner.

    Also, simple test reveal that same interface (byte class) has size 209 bytes (on ubuntu 14.04) where as the class (byte class) has size 366 bytes (on same os). Smaller byte class means less loading and maintenance. Also, while loading the interface JVM would not need to worry about extra features it provides to class (e.g. overloading/ dynamic bindings of methods etc) hence faster loading.

    This looks good enough, however this is a typical case of an anti-pattern. Although using interface as constants might look helpful, it leaves a loop hole which might impact application strategies later on.

    Assume that there is a class, which is heavily dependent upon these constants. The developer while writing the code sees the interface name repeated all across the class e.g.

    packagename.Constant.CONSTANT_NAME


    So, to "clean up" the code he might want to implement the interface, so he doesn't need to write "packagename.Constants" everywhere, and all the constants are accessible directly.

    But as soon as he implements the interface, all the constants become a part of his "Contract" (as they are public and static). This may result in adding non-required constants part of the contract of the class. This is shaking the fundamentals, and adds to confusion. There is no way in Java to stop an interface from being implemented by a class.

    On the other hand, we can make the class final and hence non-extendable. Moreover we can make the constructor private hence this class is non-instantiable. Since the new class is non-instantiable, it will never break the contract. Also, if a particular constant is being used multiple times in the same class, the developer might make use of static import.

 */

public final class Constants {

    private Constants(){}

    public static final String GOOGLE_MAPS_API_BASE_URL = "https://maps.googleapis.com/maps/api";

    public static final String GOOGLE_PLACES_API_BASE_URL = GOOGLE_MAPS_API_BASE_URL+ "/place";

    //There is a next page result token that we get using places search api. Use this in next cascaded asynchronous call
    //to get the results

    public static final int LOCATION_UPDATE_DISPLACEMENT_METERS = 100;
    public static final int LOCATION_UPDATE_INTERVAL_MS = 10*60*1000;
    public static final int LOCATION_UPDATE_FASTEST_INTERVAL_MS = LOCATION_UPDATE_INTERVAL_MS/2;
}
