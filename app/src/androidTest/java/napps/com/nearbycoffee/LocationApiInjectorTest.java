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


    @Rule: Rules allow you to flexibly add or redefine the behavior of each test method in a reusable way. In Android testing, use this annotation together with one of the test rule classes that the Android Testing Support Library provides, such as ActivityTestRule or ServiceTestRule.
    Rules are basically a way of executing the test cases. Let's suppose you don't want to execute all the test methods at once. You want it to do it one after the other or in some particular sequence.
    For example whenever testing an activity class you want ot execute Oncreate->Onstart->OnResume in sequence one after the other. Test Rule comes useful in that scenario.

    Similarly there are different code sections possible in any android application.

    @Before: Use this annotation to specify a block of code that contains test setup operations. The test class invokes this code block before each test. You can have multiple @Before methods but the order in which the test class calls these methods is not guaranteed.
    @After: This annotation specifies a block of code that contains test tear-down operations. The test class calls this code block after every test method. You can define multiple @After operations in your test code. Use this annotation to release any resources from memory.
    @Test: Use this annotation to mark a test method. A single test class can contain multiple test methods, each prefixed with this annotation.
    @Rule: Rules allow you to flexibly add or redefine the behavior of each test method in a reusable way. In Android testing, use this annotation together with one of the test rule classes that the Android Testing Support Library provides, such as ActivityTestRule or ServiceTestRule.
    @BeforeClass: Use this annotation to specify static methods for each test class to invoke only once. This testing step is useful for expensive operations such as connecting to a database.
    @AfterClass: Use this annotation to specify static methods for the test class to invoke only after all tests in the class have run. This testing step is useful for releasing any resources allocated in the @BeforeClass block.
    @Test(timeout=): Some annotations support the ability to pass in elements for which you can set values. For example, you can specify a timeout period for the test. If the test starts but does not complete within the given timeout period, it automatically fails. You must specify the timeout period in milliseconds, for example: @Test(timeout=5000).



    A basic JUnit 4 test class is a Java class that contains one or more test methods. A test method begins with the @Test annotation and contains the code to exercise and verify a single functionality (that is, a logical unit) in the component that you want to test.

    The following snippet shows an example JUnit 4 integration test that uses the Espresso APIs to perform a click action on a UI element, then checks to see if an expected string is displayed.

    @RunWith(AndroidJUnit4.class)
    @LargeTest
    public class MainActivityInstrumentationTest {

        @Rule
        public ActivityTestRule mActivityRule = new ActivityTestRule<>(
                MainActivity.class);

        @Test
        public void sayHello(){
            onView(withText("Say hello!")).perform(click());

           onView(withId(R.id.textView)).check(matches(withText("Hello, World!")));
        }
    }

    Consider the below class.

        @RunWith(MockitoJUnitRunner.class)
    public class LocationApiInjectorTest {

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        //context = InstrumentationRegistry.getTargetContext();
        //Assert.assertNotNull(context);
        context = Mockito.mock(Context.class);
        //Mockito.verify(context).getApplicationContext();
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

    In this class whenever we ran this as an android unit test on local jvm, 4 test cases passed but 2 failed always.
    4 passed because : They did not have any dependency on android classes.
    2 failed because : They did have android context dependency. So we mocked the context object but they still failed throwing this exception.
                        This means they had deep dependencies on android. These methods not just required context but something else.
    java.lang.RuntimeException: Method myLooper in android.os.Looper not mocked. See http://g.co/androidstudio/not-mocked for details.

	at android.os.Looper.myLooper(Looper.java)
	at com.google.android.gms.common.api.zzd.zzpj(Unknown Source)
	at com.google.android.gms.common.api.GoogleApi.<init>(Unknown Source)
	at com.google.android.gms.location.FusedLocationProviderClient.<init>(Unknown Source)
	at com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(Unknown Source)
	at com.napps.nearbycoffee.Injectors.LocationApiInjector.providesFusedLocationClient(LocationApiInjector.java:62)
	at com.napps.nearbycoffee.LocationApiInjectorTest.providesFusedLocationClient(LocationApiInjectorTest.java:76)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.mockito.internal.runners.DefaultInternalRunner$1.run(DefaultInternalRunner.java:78)
	at org.mockito.internal.runners.DefaultInternalRunner.run(DefaultInternalRunner.java:84)
	at org.mockito.internal.runners.StrictRunner.run(StrictRunner.java:39)
	at org.mockito.junit.MockitoJUnitRunner.run(MockitoJUnitRunner.java:161)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:117)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:42)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:262)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:84)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:147)


    From the above example, we get to know that class is not designed properly. There is improper mix of methods which have different dependency requirements.
    We designed the class so that all location related stuff should be in one class which was a poor and bad design. There is no technical
    evidence for that kind of thinking. So a good design is not putting all the conceptually related stuff together but technically related stuff together.
    As we see from the methods the two methods don't even need the other methods to function and are not even dependent on them. But the other 4 methods are kind
    of dependent on each other. A good design would be separating the two methods and keeping them separately in other file and moving that file to the androidTest
    file so that when run it runs on real device and gets the required behaviour.

    One more thing is testing the methods that involve third party APIs or while testing third party APIs and if they require any android dependencies, it is always
    good to treat them as instrumentation tests requiring hard android dependencies. It is because, we never know what these apis might internally request from the passed
    in android object. If we pass in a context, the apis might internally call many methods of context to do something. Since we don't know we can't use mockito's when-return
    to mock all those methods. It's totally not possible. So it is always best to pass in a real object and see how the apis behave. So we have to aggregate those kinds of methods
    into a common place which makes a good testable and maintainable architecture.
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