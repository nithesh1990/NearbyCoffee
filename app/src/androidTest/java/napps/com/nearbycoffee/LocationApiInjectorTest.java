package napps.com.nearbycoffee;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.napps.nearbycoffee.Utils.Constants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by "nithesh" on 7/19/2017.
 */
/*
    There are different runners available.
    A JUnit Runner is class that extends JUnit's abstract Runner class. Runners are used for running test classes. The Runner that should be used to run a test can be set using the @RunWith annotation.

    JUnit tests are started using the JUnitCore class. This can either be done by running it from command line or using one of its various run() methods

    JUnitCore then uses reflection to find an appropriate Runner for the passed test classes. One step here is to look for a @RunWith annotation on the test class. If no other Runner is found the default runner (BlockJUnit4ClassRunner) will be used. The Runner will be instantiated and the test class will be passed to the Runner. Now it is Job of the Runner to instantiate and run the passed test class.

    Whenever you want to execute some methods of a class you need to instantiate the class and then call its methods.
    For this class we are nowhere instantiating it and neither calling the test methods. But someone should do it when we click Run.
    We don't even have main method here.

    The job of instantiating the class and calling the test methods is handled by Runners which are described below.

    AndroidJunitRunner and JunitRunner are almost same.

    MockitoJunitRunner is little bit different as it works with mock objects.

    We create mock objects using @Mock annotations or Mockito...mock(Class class). If we use @Mock objects then someone should take care
    of creating those instances.

    MockitoJunitRunner initializes mocks annotated with Mock, so that explicit usage of MockitoAnnotations.initMocks(Object) is not necessary. Mocks are initialized before each test method.
    Runner is completely optional - there are other ways you can get @Mock working, for example by writing a base class. Explicitly validating framework usage is also optional because it is triggered automatically by Mockito every time you use the framework. See javadoc for Mockito.validateMockitoUsage().

    @RunWith(MockitoJUnitRunner.class) annotation. This annotation tells the Mockito test runner to validate that your usage of the framework is correct and simplifies the initialization of your mock objects.


 */
@RunWith(MockitoJUnitRunner.class)
@RunWith(AndroidJUnit4.class)
@RunWith(JUnit4.class)
public class LocationApiInjectorTest {

    /*

    Just by annotating mock objects won't be created
    There are different ways of initializing mock objects using mockito framework.
    1. @Mock
        Context context;

        later before using

        MockitoAnnotations.init(this)
    2. Context context;

        later before using

        context = Mockito.mock(Context.class);
    3. @Runwith(MockitoJunitRunner.class)

        @Mock
        Context context;

     These mock objects are just instances, kind of skeleton and they don't contain any data. Just by creating mock objects
     don't expect the object to behave like a real object. It is just a fake object to make sure you don't get nullpointer exception.

     If that is the case, why can't we just use Object obj = new Object() and use it. Why do we need mock objects then?
     :- It largely depends on whether every single field of object is used. If yes, then the two approaches would be equivalent and it would be up to you to decide which one is more readable.
        Otherwise, it's much cleaner to create a mock object that only stubs the methods that are interacted with, leaving out those that are not relevant.
        This would make it easier to understand what's being tested by clearly stating the given/when/then parts.


    The below code should actually create mock object but it is not creating the mock object
    Context context;

    @Before
    public void setUp() throws Exception {
        //context = InstrumentationRegistry.getTargetContext();
        //Assert.assertNotNull(context);
        context = Mockito.mock(Context.class);
        Mockito.verify(context).getApplicationContext();
    }

    The reason is

    By default Mockito uses CGLib or ByteBuddy, both of which generage .class files. You're running on an Android device or emulator, so .class files won't help; you need .dex instead.
    Adjust your dependencies to use DexMaker, which will override Mockito's default and allow Mocking in Android environments.


    Since version 2.6.+, Mockito added a new artifact that works for Android without the need of any other dependency (i.e. no need to import DexMaker anymore). (Reference)
    Just use org.mockito:mockito-android as a dependency for your instrumented unit tests (androidTest). You would still use the usual org.mockito:mockito-core for your local unit tests.

    Example:

    dependencies {
        ...
        testCompile 'org.mockito:mockito-core:2.7.15'
        androidTestCompile 'org.mockito:mockito-android:2.7.15'
        ...
    }
    In the above code it is not able to generate the mock object because.

    This actually solved the problem.

    Now we wanted to run this test. We don't know whether it is unit test or an instrumentation test. After clicking the run it did gradle build
    Installed the apk and it ran the test. That's fine. But for just a few methods it's a bad idea to run by installing and launching the app.

    The reason it used app launch because the test file was placed under androidTest/ which android studio thinks it is an instrumentation test and
    runs using the app.

    We moved the file to test/  folder which made android studio think it is a local unit test and uses local jvm to run these tests. But local jvm doesn't
    about android classes because we used android's context class. So

    By default, the Android Plug-in for Gradle executes your local unit tests against a modified version of the android.jar library, which does not contain any actual code.
    Instead, method calls to Android classes from your unit test throw an exception. This is to make sure you test only your code and do not depend on any particular behavior of the Android platform (that you have not explicitly mocked).

    To generalize, there are steps to create mock objects can mock both android and normal java classes.
    **The objects are just mock objects and not real objects. Don't expect any android behaviour to happen**
    If the behaviour is just restricted to method(just getting a string value as illustrated in the example below) you can do it using when, return of mockito framework.
    Don't expect any lifecycle, context state save or any other real behaviour to happen.


    Steps
    To add a mock object to your local unit test using this framework, follow this programming model:

    1. Include the Mockito library dependency in your build.gradle file, as described in Set Up Your Testing Environment.
    2. At the beginning of your unit test class definition, add the @RunWith(MockitoJUnitRunner.class) annotation. This annotation tells the Mockito test runner to validate that your usage of the framework is correct and simplifies the initialization of your mock objects.
    3. To create a mock object for an Android dependency, add the @Mock annotation before the field declaration.
    4. To stub the behavior of the dependency, you can specify a condition and return value when the condition is met by using the when() and thenReturn() methods.

    @RunWith(MockitoJUnitRunner.class)
    public class UnitTestSample {

        private static final String FAKE_STRING = "HELLO WORLD";

        @Mock
        Context mMockContext;

        @Test
        public void readStringFromContext_LocalizedString() {
            // Given a mocked Context injected into the object under test...
            when(mMockContext.getString(R.string.hello_word))
                .thenReturn(FAKE_STRING);
            ClassUnderTest myObjectUnderTest = new ClassUnderTest(mMockContext);

            // ...when the string is returned from the object under test...
            String result = myObjectUnderTest.getHelloWorldString();

            // ...then the result should be the expected one.
            assertThat(result, is(FAKE_STRING));
        }
    }
    */

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        //context = InstrumentationRegistry.getTargetContext();
        //Assert.assertNotNull(context);
    }

    @Test
    public void providesLocationRequest() throws Exception {
        LocationRequest locationRequest = LocationApiInjector.providesLocationRequest();
        Assert.assertNotNull(locationRequest);
        Assert.assertEquals(LocationRequest.PRIORITY_HIGH_ACCURACY, locationRequest.getPriority());
        Assert.assertEquals(Constants.LOCATION_UPDATE_INTERVAL_MS, locationRequest.getInterval());
        Assert.assertEquals(Constants.LOCATION_UPDATE_FASTEST_INTERVAL_MS, locationRequest.getFastestInterval());
        Assert.assertEquals(Constants.LOCATION_UPDATE_DISPLACEMENT_METERS, locationRequest.getSmallestDisplacement(), 10);
    }

    @Test
    public void providesCustomLocationRequest() throws Exception {

        LocationRequest locationRequest = LocationApiInjector.providesCustomLocationRequest(LocationRequest.PRIORITY_HIGH_ACCURACY,
                Constants.LOCATION_UPDATE_INTERVAL_MS,
                Constants.LOCATION_UPDATE_FASTEST_INTERVAL_MS,
                Constants.LOCATION_UPDATE_DISPLACEMENT_METERS);
        Assert.assertNotNull(locationRequest);
        Assert.assertEquals(LocationRequest.PRIORITY_HIGH_ACCURACY, locationRequest.getPriority());
        Assert.assertEquals(Constants.LOCATION_UPDATE_INTERVAL_MS, locationRequest.getInterval());
        Assert.assertEquals(Constants.LOCATION_UPDATE_FASTEST_INTERVAL_MS, locationRequest.getFastestInterval());
        Assert.assertEquals(Constants.LOCATION_UPDATE_DISPLACEMENT_METERS, locationRequest.getSmallestDisplacement(), 10);

    }

    @Test
    public void providesLocationSettingsRequest() throws Exception {
        LocationSettingsRequest locationSettingsRequest = LocationApiInjector.providesLocationSettingsRequest();
        Assert.assertNotNull(locationSettingsRequest);
    }

    @Test
    public void provideCustomLocationSettingsRequest() throws Exception {
        LocationSettingsRequest locationSettingsRequest = LocationApiInjector.provideCustomLocationSettingsRequest(LocationApiInjector.providesLocationRequest());
        Assert.assertNotNull(locationSettingsRequest);
    }

    @Test
    public void providesFusedLocationClient() throws Exception {
        FusedLocationProviderClient fusedLocationProviderClient = LocationApiInjector.providesFusedLocationClient(context);
        Assert.assertNotNull(fusedLocationProviderClient);
     }

    @Test
    public void providesSettingsClient() throws Exception {
        SettingsClient settingsClient = LocationApiInjector.providesSettingsClient(context);
        Assert.assertNotNull(settingsClient);
    }

}