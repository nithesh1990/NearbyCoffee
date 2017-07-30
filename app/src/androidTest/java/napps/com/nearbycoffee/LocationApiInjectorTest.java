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


    Actual Working example of Mockito in Android
    @RunWith(MockitoJUnitRunner.class)
    public class AppUtilsTest {

    @Mock
    Context context;
    @Mock
    ConnectivityManager connectivityManager;

    @Mock
    NetworkInfo networkInfo;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void checkNetworkConnection() throws Exception {
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnectedOrConnecting()).thenReturn(false);
        boolean expected = false;
        boolean actual = AppUtils.checkNetworkConnection(context);
        Assert.assertEquals(expected, actual);
     }

    It does object mocking correctly and runs the test successfully.

    If we replace the following line
    Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

    with this
    Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(null);

    We are gettting the following error
    org.mockito.exceptions.misusing.UnnecessaryStubbingException:
    Unnecessary stubbings detected in test class: AppUtilsTest
    Clean & maintainable test code requires zero unnecessary code.
    Following stubbings are unnecessary (click to navigate to relevant line of code):
      1. -> at com.napps.nearbycoffee.Utils.AppUtilsTest.checkNetworkConnection(AppUtilsTest.java:38)
    Please remove unnecessary stubbings or use 'silent' option. More info: javadoc for UnnecessaryStubbingException class.

        at org.mockito.internal.runners.StrictRunner.run(StrictRunner.java:49)
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


    It says that some methods are not used. Let's read an example from the documentation


    Unnecessary stubs are stubbed method calls that were never realized during test execution (see also MockitoHint), example:

     //code under test:
     ...
     String result = translator.translate("one")
     ...

     //test:
     ...
     when(translator.translate("one")).thenReturn("jeden"); // <- stubbing realized during code execution
     when(translator.translate("two")).thenReturn("dwa"); // <- stubbing never realized
     ...

    Notice that one of the stubbed methods were never realized in the code under test, during test execution. The stray stubbing might be an oversight of the developer, the artifact of copy-paste or the effect not understanding the test/code. Either way, the developer ends up with unnecessary test code. In order to keep the codebase clean & maintainable it is necessary to remove unnecessary code. Otherwise tests are harder to read and reason about.
    To find out more about detecting unused stubbings see MockitoHint.


    We don't have any duplicate methods but still why did we get the error. Let's analyze the code part

        #Test Code
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(null);
        Mockito.when(networkInfo.isConnectedOrConnecting()).thenReturn(false);

        #Actual Code
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        In the actual code whenever activeNetwork == null, it immediately returns false and will not check the next part as it is AND operation.
        Since the next part is not check the test rule corresponding the next part

        Mockito.when(networkInfo.isConnectedOrConnecting()).thenReturn(false);

        is also not run.

       We tried to test the below code

      public static boolean getLocationPermissionsStatus(@NonNull Context context){
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
       }

       This actually calls ContextCompat.checkSelfPermission(..). Since there is only one dependency, we thought we will mock ContextCompat and the method like below

       Mockito.when(contextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(PackageManager.PERMISSION_GRANTED);

       and we tried executing the test but got the following error.

        java.lang.RuntimeException: Method myPid in android.os.Process not mocked. See http://g.co/androidstudio/not-mocked for details.

        at android.os.Process.myPid(Process.java)
        at android.support.v4.content.ContextCompat.checkSelfPermission(ContextCompat.java:453)
        at com.napps.nearbycoffee.Utils.AppUtilsTest.getLocationPermissionsStatus(AppUtilsTest.java:46)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
        at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
        at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
        at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
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


        The following error is because internally in ContextCompat.checkselfPermission(..) it uses

        public static int checkSelfPermission(@NonNull Context context, @NonNull String permission) {
            if (permission == null) {
                throw new IllegalArgumentException("permission is null");
            }

            return context.checkPermission(permission, android.os.Process.myPid(), Process.myUid());
        }

        But we have to observe one more thing here. We mocked the whole method so it should return what we say. Is it?
        because the same thing we did for the previous method. Here we have to observe the core thing which makes us understand better
        how mocking actually happens.

        Consider the method

        public static boolean checkNetworkConnection(@NonNull Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        ........
        ........

        This method uses that mock object that we provided and subsequently we create the chaining methods and it works fine.

        But in this method

        public static int checkSelfPermission(@NonNull Context context, @NonNull String permission) {
            if (permission == null) {
                throw new IllegalArgumentException("permission is null");
            }

            return context.checkPermission(permission, android.os.Process.myPid(), Process.myUid());
        }

        even though we mock context compat object

        @Mock
        ContextCompat contextCompat;

        it is not using our mock object because checkSelfPermission is a static method which does not require any contextcompat object
        morever we are not even passing the mocked object inside the method. How will it know about contextcompat object.

        So mockito is not suitable for testing public static methods and deep android calls. So we have to move this under androidTest

        Similarly use mockito only if you have method calls that are only 1 or 2 levels deep and it uses
        objects/classes that you have created so that you can mock any method if required.
        Don't use if there is deep nested call inside any method or the method uses any framework or platform specific calls.

        Limitations in Mockito

            1.Final classes
            2. Anonymous classes
            3. Primitive types
            I want to show you how we can test Android things.

            @Test
            public void shouldBeCreatedIntent() {
                 Context context = Mockito.mock(Context.class);
                 Intent intent = MainActivity.createQuery(context, "query", "value");
                 assertNotNull(intent);
                 Bundle extras = intent.getExtras();
                 assertNotNull(extras);
                 assertEquals("query", extras.getString("QUERY"));
                 assertEquals("value", extras.getString("VALUE"));
            }
            Unfortunately if we run this test, we get next error log.

            java.lang.RuntimeException: Method putExtra in android.content.Intent not mocked. See http://g.co/androidstudio/not-mocked for details.
             at android.content.Intent.putExtra(Intent.java)
             at PaymentActivity.createIntent(PaymentActivity.java:15)
             at MainActivityTest.shouldBeCreatedIntent(MainActivityTest.java:118)

        So Although mockito is good, you cannot really unit test android apps because most of the classes in any android application will have
        strong dependencies of android framework. Fortunately there is another library to the rescue of android unit testing : ROBOELECTRIC.

        By performing most of the tests as Unit tests, concentrating much on tests, changing the implementation/architecture based on test results
        is called Test Driven Development(TDD).

        Robolectric

            Unit testing has been particularly difficult in Android, although with Robolectric it is much easier.
            This library also allows you to mock the Android SDK by removing the stubs that throw RuntimeExceptions.
            Roboelectric is a unit testing library that allows you to run  your test in a Java Virtual Machine (JVM). This library also allow you to do Test Driven Development (TDD).
            Roboelectric replaced all Android classes by so-called shadow objects.
            If a method is implemented by Robolectric, it forwards these method calls to the shadow object which act similar to the objects of the Android SDK. If a method is not implemented by the shadow object, it simply returns a default value, e.g., null or 0.

        Before proceeding to the configuration, let's create a separate folder for RoboElectric Tests. This can do android unit testing which does not depend on pure android behaviour.

        For this we had to create a folder in android application. It is better to create folder in project View/project perspective

        *project view image*

        When we opened that folder we found two additional folders - debug and release. We opened that folder curiously to see what's inside.

        Debug folder contained
                    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">AIzaSyBOx732ytnR0tWQggzQprzCEu50c_vJeCM</string>

        Release folder contained
            release/res/values/google_maps_api.xml which had
                    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">YOUR_KEY_HERE</string>

        Here we can see that only debug folder contained that key but not release folder. While releasing we have to make sure all these things are correct.

        We can actually create custom xml files and put in this folder which will be helpful in development and release like API Keys etc.

        Coming back to RoboElectric

        Running tests on an Android emulator or device is slow! Building, deploying, and launching the app often takes a minute or more. That’s no way to do TDD. There must be a better way.

        Robolectric is a unit test framework that de-fangs the Android SDK jar so you can test-drive the development of your Android app. Tests run inside the JVM on your workstation in seconds.

        SDK, Resources, & Native Method Emulation
        Robolectric handles inflation of views, resource loading, and lots of other stuff that’s implemented in native C code on Android devices. This allows tests to do most things you could do on a real device. It’s easy to provide our own implementation for specific SDK methods too, so you could simulate error conditions or real-world sensor behavior, for example.

        Run Tests Outside of the Emulator
        Robolectric lets you run your tests on your workstation, or on your Continuous Integration environment in a regular JVM, without an emulator. Because of this, the dexing, packaging, and installing-on-the emulator steps aren’t necessary, reducing test cycles from minutes to seconds so you can iterate quickly and refactor your code with confidence.

        No Mocking Frameworks Required
        An alternate approach to Robolectric is to use mock frameworks such as Mockito or to mock out the Android SDK. While this is a valid approach, it often yields tests that are essentially reverse implementations of the application code.

        Robolectric allows a test style that is closer to black box testing, making the tests more effective for refactoring and allowing the tests to focus on the behavior of the application instead of the implementation of Android. You can still use a mocking framework along with Robolectric if you like.
            So before adding roboelectric dependency let's see what is the difference between testcompile and androidTestCompile?
            Simply testCompile is the configuration for unit tests (those located in src/test) and androidTestCompile is used for the test api (that located in src/androidTest). Since you are intending to write unit tests, you should use testCompile.
            Update: The main distinction between the two is the test sourceset runs in a regular Java JVM, whereas the androidTest sourceset tests run on an Android device (or an emulator).


        In RoboElectric test we write

        @RunWith(RobolectricTestRunner.class)
        @Config(constants = BuildConfig.class, sdk = 21)
        public class AppUtilsAndroidMock {

            @Test
            public void getLocationPermissionsStatus() throws Exception {
                MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
                boolean expected = false;
                boolean actual = AppUtils.getLocationPermissionsStatus(mainActivity);
                Assert.assertEquals(expected, actual);
            }
        }

        @Config(constants = BuildConfig.class, sdk = 21) this line configures the current class to run against android 5.0. For that it
        has to run against android 5.0 shadow jar which contains all the shadow objects. So it downloads that jar from online as we can see from logs

        No such manifest file: build\intermediates\bundles\debug\AndroidManifest.xml
        Downloading: org/robolectric/android-all/5.0.0_r2-robolectric-1/android-all-5.0.0_r2-robolectric-1.pom from repository sonatype at https://oss.sonatype.org/content/groups/public/
        Transferring 1K from sonatype
        Downloading: org/robolectric/android-all/5.0.0_r2-robolectric-1/android-all-5.0.0_r2-robolectric-1.jar from repository sonatype at https://oss.sonatype.org/content/groups/public/
        Transferring 41864K from sonatype


        We tested the checkLocationPermission method using Mockito and Robolectric using below code

        @RunWith(RobolectricTestRunner.class)
        @Config(constants = BuildConfig.class, sdk = 19)
        public class AppUtilsAndroidMock {

            @Mock
            Context context;

            @Test
            public void getLocationPermissionsStatus() throws Exception {
                boolean expected = true;
                boolean actual = AppUtils.getLocationPermissionsStatus(context);
                Assert.assertEquals(expected, actual);
            }

            @Before
            public void setUp() throws Exception {
                MockitoAnnotations.initMocks(this);

            }
        }


        The results were not satisfying as it gave wrong results. It might be due to some other settings inside which might have resulted default value/null
        which might have lead to wrong result. One more thing is that this method actually checks for location permission. So we should actually
        test this using actual device by turning on and off the location permission.

        There is one more method which we tried which actually gave the correct results. The above code wrong results because we had mocked the context using mockito
        and tried using robolectric which is not correct. Robolectric has class and method to provide context

                Context context = RuntimeEnvironment.application.getApplicationContext();

        By using this we got the correct results.

        We can avoid downloading the manifest everytime by configuring and specifying those things in robolectric.properties file.

        Android Annotations
        Annotations are very useful in verifying the code at build time. For example consider the following code

        public static FragmentTransaction addFragmentToActivity (FragmentManager fragmentManager,
                                               Fragment fragment, int frameId) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId, fragment);
            transaction.commit();

            return transaction;
        }

        In the above code we are passing some parameters and returning some values. But we are not checking whether it is null or not.
        This might result in crashes and unexpected behaviours. We are also returning values which might be null. Referencing them will create crashes.

        We are passing in an Id. It should actually be a resource id. Since it takes an int, passing any int will call that method but eventually crashes it.

        So we need to have build checks so that there will not be any breaks. We will use the following annotations to make sure that everything is correct.

        @Nonnull, @Nullable - The @Nullable annotation indicates a variable, parameter, or return value that can be null while @NonNull indicates a variable, parameter, or return value that cannot be null.

        @IdRes -  Annotations that check that a ressource parameter is of kind R.id.

        @StringRes - Annotations that check that a resource parameter contains an R.string reference

        There are lot of useful annotations that can be used frequently.

        - @DrawableRes, @DimenRes, @ColorRes, @InterpolatorRes
        - @MainThread, @UiThread, @WorkerThread, @BinderThread, @AnyThread
        - @IntRange, @FloatRange, @Size
        - @IntDef and @StringDef
        - @CheckResult annotation to validate that a method's result or return value is actually used. Instead of annotating every non-void method with @CheckResult, add the annotation to clarify the results of potentially confusing methods.
                For example, new Java developers often mistakenly think that <String>.trim() removes whitespace from the original string. Annotating the method with @CheckResult flags uses of <String>.trim() where the caller does not do anything with the method's return value.
                Denotes that the annotated method returns a result that it typically is an error to ignore. This is usually used for methods that have no side effect, so calling it without actually looking at the result usually means the developer has misunderstood what the method does.
                public @CheckResult String trim(String s) { return s.trim(); }
                    ...
                    s.trim(); // this is probably an error
                    s = s.trim(); // ok

        Build Flavors
        In our application we are creating different versions of the application or we can say different build types. There is actually difference between build types
        and product flavors. To understand let's see this StackOverflow post:

            Some concrete examples that led to my confusion:
            The signingConfig property can be set in both build types and product flavors... but minifyEnabled (and, I assume, shrinkResources?) can only be configured in build types.
            applicationId can only be specified in product flavors... and applicationIdSuffix can only be specified in build types!?

            Explanation: build types are for different builds of your application that aren't functionally different -- if you have a debug and release version of your app, they're the same app, but one contains debugging code, maybe more logging, etc., and the other is streamlined and optimized and possibly obfuscated via ProGuard.
            With flavors, the intent is that the app is notably different in some way. The clearest example would be a free vs. a paid version of your app, but developers may also differentiate based on where it's being distributed (which could affect in-app billing API use).

            Sometimes it so happens that you are developing a free and a paid version of the app. Free version uses different API Key and Paid version ues different API Key and that is the only difference. Before generating the apk you make sure that correct key goes into correct apk.
            This means to have different build types because it is the just the same app with different credentials or an apk with or without progaurd enables. For Progaurd you can see in the build.gradle file.

            buildTypes {
                debug {
                    minifyEnabled false
                    proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
                }
                release {
                    minifyEnabled true
                    proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
                }
            }

            buildType configure how we package our app

            - shrinkResources
            - progaurdFile

            Flavor configure different classes and resources.

            - in Flavor1 your MainActivity can do something, and in Flavor2 different implementation
            - differente App name

            Basically Flavors change your app completely. Suppose for example you want to test the app which involves network calls. You can't do network calls in unit testing.
            So you decide to use dummy response data and test how the things work. Here we are changing how the app is working and on what it is working. We can create new files
            that are responsible for generating this dummy data but keeping them in the same src folder creates confusion and it is bad style of coding. So we create different flavors
            which contain different files. 1 flavor contains files that fetch real data from server and another flavor the fetches dummy data. Creation is simple

                productFlavors {
                    mock {
                        applicationIdSuffix = ".mock"
                    }
                    prod {

                    }
                }

            As we can see, the name of one flavor is prod which relates production version of the app and another version is mock which relates data mocked version of the app.

            There will be a defaultconfig for android

            defaultConfig {
                applicationId "com.napps.nearbycoffee"
                minSdkVersion 15
                targetSdkVersion 25
                versionCode 1
                versionName "1.0"
                testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
            }

            This configuration can be distributed over different flavors like below

            productFlavors {
                    blueberry {
                        minSdkVersion 21
                        applicationId 'com.sample.foo.blueberry'
                        targetSdkVersion 23
                        versionCode 1
                        versionName '1.0'
                    }
                    chocolate {
                        minSdkVersion 19
                        applicationId 'com.sample.foo.chocolate'
                        targetSdkVersion 24
                        versionCode 1
                        versionName '1.0'
                    }
                    raspberry {
                        minSdkVersion 19
                        applicationId 'com.sample.foo.multipleflavorsample.raspberry'
                        targetSdkVersion 23
                        versionCode 1
                        versionName '1.0'
                    }
            }
            Note: We have changed the applicationID, so all three flavors can exist on the same device at the same time, as well as modified the minimum and target SDK versions.

            A build type applies settings relating to the build and packaging of the app, such as if it’s debuggable and the signing keys. The default build types are debug and release.
            In contrast, a flavor can specify features, device and API requirements (such as custom code, layout and drawables), and minimum and target API levels among others. The combination of build types and flavors forms a “Build Variant”. There is a build variant for every defined flavor/build type combination.

            Adding and Editing Build Flavors can also be done in Studio using Build -> Edit BuildTypes or Build -> Edit Flavors

            Best Example for two different flavors would be phone and tablet

            productFlavors {
                phone {
                    applicationId
                    "com.ebookfrenzy.buildexample.app.phone"
                    versionName "1.0-phone"
                }
                tablet {
                    applicationId
                    "com.ebookfrenzy.buildexample.app.tablet"
                    versionName "1.0-tablet"
                }
           }


          Different product flavors might have same classes with different implementations. But in android it will not allow you to create same class twice.
          Here's the thing. There will be different folders and you can see them if you switch to project view

            F:\Github Repo\android-architecture\android-architecture\todoapp\app\src\androidTest
            F:\Github Repo\android-architecture\android-architecture\todoapp\app\src\androidTestMock
            F:\Github Repo\android-architecture\android-architecture\todoapp\app\src\main
            F:\Github Repo\android-architecture\android-architecture\todoapp\app\src\mock
            F:\Github Repo\android-architecture\android-architecture\todoapp\app\src\prod
            F:\Github Repo\android-architecture\android-architecture\todoapp\app\src\test

            Here we can see that there are two folders mock and prod and also main. Main folder is the main application folder and you cannot create alternative for that.
            If there is a class in main folder, you can't create a duplicate class in either main or any other folder. This becomes confusing then how to create same class file
            twice having different implementations. The idea is to separate the classes which might have different flavors from the main folder. Main folder is like holding the
            common classes across different product flavors. So first separate those files. After separating those files you can create two different versions of the class - one in prod
            and one in mock. While building android picks up the corresponding file from the corresponding folder based on the build variant.


            There was a scenario in nearbycoffeeshops app. There was coffeeshopsrepository and coffeeshopsremoterepository. There was a repositoryInjector which injected the repositories.
            We had something like this

            public final class RepositoryInjector {

                public static final CoffeeShopsRemoteRepository providesCoffeeShopsRemoteRepository(){
                    return new CoffeeShopsRemoteRepository();
                }
            }

            But this is not following the proper MVP architecture. When asked for repository, we should always provide CoffeeShopsRepository. Inside Coffeeshopsrepository we had something like this

            public class CoffeeShopsRepository implements CoffeeShopsDataSource {

                    private static CoffeeShopsRepository sInstance = null;

                    private CoffeeShopsRemoteRepository coffeeShopsRemoteRepository;
            ...
            ...

            where we are having a specific "CoffeeShopsRemoteRepository" but that should not be the case. A repository should communicate with a "Data Source" be it either - remote or local
            So we will change the instance to coffeedatasource.

            public class CoffeeShopsRepository implements CoffeeShopsDataSource {

                private static CoffeeShopsRepository sInstance = null;

                private CoffeeShopsDataSource coffeeShopsRemoteRepository;

                private CoffeeShopsRepository(@NonNull CoffeeShopsDataSource coffeeShopsDataSource){
                    this.coffeeShopsRemoteRepository = coffeeShopsDataSource;
                }

                public static CoffeeShopsRepository getsInstance(@NonNull CoffeeShopsDataSource coffeeShopsDataSource){
                    if(sInstance == null){
                        sInstance = new CoffeeShopsRepository(coffeeShopsDataSource);
                }

                return sInstance;
            }

            Now It's fine if we just provide CoffeeShopsDataSource instance. Be it is implemented by CoffeeShopsRemoteRepository, CoffeeShopsLocalRepository, FakeCoffeeShopsRepository doesn't matter.
            Now we can create fake one and pass it without having to change the implementation. This we call it clean testable architecture.

            Now coffeeshopsremoterepository and fakecoffeeshopsremoterepository can have completely different implementations. One can be singleton and other may not be. They just have to implement the methods
            of CoffeeDataSource. Later in the project we can add one more type of repository like Coffeeshopscacherepository which is also a valid repository and accepted by
            CoffeeShopsRepository because it doesn't care which class it is, it just wants a datasource and Coffeeshopscacherepository. This we call clean maintainable architecture.
            Here we are satisfying two SOLID principles - Single Responsbility and Interface Seggregation.

            Now Android View contains four folders if the build variant is mockDebug/mockRelease

            nearbycoffee
            nearbycoffee(androidTest)
            nearbycoffee (test)
            nearbycoffee\Injectors (mock)

            If the build variant is prodDebug/prodRelease, then

            nearbycoffee
            nearbycoffee(androidTest)
            nearbycoffee (test)
            nearbycoffee\Injectors (prod)

            As we can see the last folder acts as a detachable folder which contains flavor specific files. At any point of time we will be either in prod/mock.
            Prod version is kind of separate project and mock version is kind of separate project. When in particular version, all classes used in that flavor should be present
            and each flavor is built as a single independent project.
            Having this kind of environment is called Hermetic Environment

            Coming back to Testing, Sometimes we have to test callbacks. Testing callbacks is not an easy job. We call a method and pass a callback. There might be different methods
            of callback that can be called. We can't verify each and every method. It becomes like we are creating a whole new callback and implementing those methods.
            Sometimes we have to test methods that use callbacks, meaning that they are asynchronous by definition.
            These methods are not easy to test and using Thread.sleep(milliseconds) method to wait for the response is not a good practice and can convert your tests in non-deterministic ones

            There are basically two types of callbacks - synchronous and asynchronous callbacks

            Synchronous callbacks run on the same thread and provide immediate callbacks. Let's see the way of testing it.
            These callbacks are tested by creating a mock objects and using Mockito's doAnswer.

            Consider the following method to be tested:
            public void getCoffeeShops(final CoffeeShopsReceiver coffeeShopsReceiver, double latitude, double longitude, int radius)

            It takes in callback CoffeeShopsReceiver. Once the method has finished computation, it either calls onCoffeeShopsReceived(..) to post results
            or OnNetworkError(..) to post error. We know that we are gonna receive callback in either of the two methods. So we have to mock those two methods. Let's see how can we mock
            OnCoffeeShopsReceived method

            doAnswer(receive something using answer instance).when(Callback.class).callbackmethod(..);

                final List<Result> results = new ArrayList<>();
                Mockito.doAnswer(new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        List<Result> mockedResult = invocation.getArgument(0);
                        Assert.assertEquals(results, mockedResult);
                        return null;
                    }
                }).when(coffeeShopsReceiver).OnCoffeeShopsReceived(results);


            Similarly for OnNetworkError(..)

                final String message = "something happened";
                Mockito.doAnswer(new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        String messageReceived = invocation.getArgument(0);
                        Assert.assertEquals(message, messageReceived);
                        return null;
                    }
                }).when(coffeeShopsReceiver).OnNetworkError(message);

            Let's test the method by calling the following method
            coffeeShopsRemoteRepository.getCoffeeShops(coffeeShopsReceiver, -1, -1, 10);

            After running we got the following error. The error is as shown below

            java.lang.IllegalArgumentException: Unable to create call adapter for io.reactivex.Observable<com.napps.nearbycoffee.Model.PlaceSearchResponse>
            for method PlacesAPIService.getNearbyCoffeeShops

            It is due to the wrong usage of rx-java adapter. We have to observer one thing here. We got to know that we are using wrong rx-java adapter library
            by running a unit test. If not this would have resulted in crash and it would have taken more time running and debugging in the actual device.


            Some insights about Junit

            JUnit Lifecycle, continued
            Lifecycle stage	Method called	Method description

            Setup
            public void setUp()
            Called to do any required preprocessing before a test. Examples include instantiating

            Test
            public void testXYZ()
            Each test method is called once within the test lifecycle. It performs all required testing. Test results are recorded by JUnit for reporting to the test runner upon completion.

            Teardown
            public void tearDown()
            Called to do any required post processing after a test. Examples include cleaning up of database tables and closing database connections.


            Assertions
            Assertion	                            What it does
            assertNull(Object x)	                Validates that the parameter is null
            assertNotNull(Object x)	                Validates that the parameter is not null
            assertTrue(boolean x)	                Validates that the parameter is true
            assertFalse(boolean x)	                Validates that the parameter is false
            assertEquals(Object x, Object y)	    Validates that the two objects passed are equal based on the .equals(Object obj1, Object obj2) method
            assertSame(Object x, Object y)	        Validates that the two objects passed are equal based on the == operator
            assertNotSame(Object x, Object y)	    Validates that the two objects passed are not equal based on the == operator
            fail()	                                Programmatically fail the test.


            Annotation	        Parameters	        Use
            @After	            None	            Method will be executed after each test method (similar to the tearDown() method in JUnit 3.x). Multiple methods may be tagged with the @After annotation, however no order is guaranteed.
            @AfterClass	        None	            Method will be executed after all of the test methods and teardown methods have been executed within the class. Multiple methods may be tagged with the @AfterClass annotation, however no order is guaranteed.
            @Before	            None	            Method will be executed before each test method (similar to the setUp() method in JUnit 3.x). Multiple methods may be tagged with the @Before annotation, however no order is guaranteed.
            @BeforeClass	    None	            Executed before any other methods are executed within the class. Multiple methods may be tagged with the @BeforeClass annotation, however no order is guaranteed.
            @Ignore	            String (optional)	Used to temporarily exclude a test method from test execution. Accepts an optional String reason parameter.
            @Parameters	        None	            Indicates a method that will return a Collection of objects that match the parameters for an available constructor in your test. This is used for parameter driven tests.
            @RunWith	        Class	            Used to tell JUnit the class to use as the test runner. The parameter must implement the interface junit.runner.Runner.
            @SuiteClasses	    Class []	        Tells JUnit a collection of classes to run. Used with the @RunWith(Suite.class) annotation is used.
            @Test	            Class(optional)     Used to indicate a test method. Same functionality as naming a method public void testXYZ() in JUnit 3.x. The class parameter is used to indicate an exception is expected to be thrown and what the exception is. The timeout parameter specifies in milliseconds how long to allow a single test to run. If the test takes longer than the timeout, it will be considered a failure.

            Sometimes it happens that you are testing an interface like Map<K,V> . You mock the object and it is created but there is no implementation of the method. So you want to assign an instance of object that implements this interface to test the methods
            like HashMap<K, V>.

            a Similar kind of example is given below
            @RunWith(MockitoJUnitRunner.class)
            public class BackgroundExecutorServiceTest {

                BackgroundExecutorContract<PlaceSearchResponse, Throwable> backgroundExecutorContract;

                @Test
                public void executeSingleTask() throws Exception {
                    backgroundExecutorContract = Mockito.mock(BackgroundExecutorService.class);
                }

            }

            BackgroundExecutorContract is the interface, BackgroundExecutorService is the class implementing it.

            How to test RxJava and Retrofit

                            1. Get rid of the static call - use dependency injection

                            The first problem in your code is that you use static methods. This is not a testable architecture, at least not easily, because it makes it harder to mock the implementation. To do things properly, instead of using Api that accesses ApiClient.getService(), inject this service to the presenter through the constructor:

                            public class SplashPresenterImpl implements SplashPresenter {

                            private SplashView splashView;
                            private final ApiService service;

                            public SplashPresenterImpl(SplashView splashView, ApiService service) {
                                this.splashView = splashView;
                                this.apiService = service;
                            }
                            2. Create the test class

                            Implement your JUnit test class and initialize the presenter with mock dependencies in the @Before method:

                            public class SplashPresenterImplTest {

                            @Mock
                            ApiService apiService;

                            @Mock
                            SplashView splashView;

                            private SplashPresenter splashPresenter;

                            @Before
                            public void setUp() throws Exception {
                                this.splashPresenter = new SplashPresenter(splashView, apiService);
                            }
                            3. Mock and test

                            Then comes the actual mocking and testing, for example:

                            @Test
                            public void testEmptyListResponse() throws Exception {
                                // given
                                when(apiService.syncGenres()).thenReturn(Observable.just(Collections.emptyList());
                                // when
                                splashPresenter.syncGenres();
                                // then
                                verify(... // for example:, verify call to splashView.navigateToHome()
                            }
                            That way you can test your Observable + Subscription, if you want to test if the Observable behaves correctly, subscribe to it with an instance of TestSubscriber.

                            Troubleshooting

                            When testing with RxJava and RxAndroid schedulers, such as Schedulers.io() and AndroidSchedulers.mainThread() you might encounter several problems with running your observable/subscription tests.

                            NullPointerException

                            The first is NullPointerException thrown on the line that applies given scheduler, for example:

                            .observeOn(AndroidSchedulers.mainThread()) // throws NPE
                            The cause is that AndroidSchedulers.mainThread() is internally a LooperScheduler that uses android's Looper thread. This dependency is not available on JUnit test environment, and thus the call results in a NullPointerException.

                            Race condition

                            The second problem is that if applied scheduler uses a separate worker thread to execute observable, the race condition occurs between the thread that executes the @Test method and the said worker thread. Usually it results in test method returning before observable execution finishes.

                            Solutions

                            Both of the said problems can be easily solved by supplying test-compliant schedulers, and there are few options:

                            Use RxJavaHooks and RxAndroidPlugins API to override any call to Schedulers.? and AndroidSchedulers.?, forcing the Observable to use, for example, Scheduler.immediate():


                                    @Before
                                    public void setUp() throws Exception {
                                            // Override RxJava schedulers
                                            RxJavaHooks.setOnIOScheduler(new Func1<Scheduler, Scheduler>() {
                                                @Override
                                                public Scheduler call(Scheduler scheduler) {
                                                    return Schedulers.immediate();
                                                }
                                            });

                                            RxJavaHooks.setOnComputationScheduler(new Func1<Scheduler, Scheduler>() {
                                                @Override
                                                public Scheduler call(Scheduler scheduler) {
                                                    return Schedulers.immediate();
                                                }
                                            });

                                            RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
                                                @Override
                                                public Scheduler call(Scheduler scheduler) {
                                                    return Schedulers.immediate();
                                                }
                                            });

                                            // Override RxAndroid schedulers
                                            final RxAndroidPlugins rxAndroidPlugins = RxAndroidPlugins.getInstance();
                                            rxAndroidPlugins.registerSchedulersHook(new RxAndroidSchedulersHook() {
                                                @Override
                                                public Scheduler getMainThreadScheduler() {
                                                    return Schedulers.immediate();
                                            }
                                        });
                                    }

                                    @After
                                    public void tearDown() throws Exception {
                                        RxJavaHooks.reset();
                                        RxAndroidPlugins.getInstance().reset();
                                    }

                        This code has to wrap the Observable test, so it can be done within @Before and @After as shown, it can be put into JUnit @Rule or placed anywhere in the code. Just don't forget to reset the hooks.
                        Second option is to provide explicit Scheduler instances to classes (Presenters, DAOs) through dependency injection, and again just use Schedulers.immediate() (or other suitable for testing).
                        As pointed out by @aleien, you can also use an injected RxTransformer instance that executes Scheduler application.
                        I've used the first method with good results in production.


            In the NearByCoffee project, we decided to use retrofit and rxjava. For each retrofit call we get a Call<R> object. Rxjava provides a adapter which in turn returns an observable.

            Normal Retrofit Interface
                @GET(NEARBY_SEARCH_JSON)
                Call<PlaceSearchResponse> getNearbyCoffeeShops(@QueryMap Map<String, String> queryParams);
            Observable Retrofit Interface
                @GET(NEARBY_SEARCH_JSON)
                Observable<PlaceSearchResponse> getNearbyCoffeeShops(@QueryMap Map<String, String> queryParams);

             Providing an observable is pretty good because in case if we want to do chaining requests, rxjava flatmap operations it comes handy.
             But by directly coupling Retrofit calls to observable makes testing difficult. You can't individually  test RxJava and Retrofit.
             RxJava also has some issues while doing instrumentation testing due to thread race conditions. So in case of Observable only way to test the api call
             is to mock the observable and get the results in callback. Due to rxjava unit testing isssues it was difficult for us to test the calls directly.

             So we had to decouple them. If we decouple and go back to original Call<R> style, we won't get the benefits of RxJava. So we had to intercept in between
             to manually convert a Retorofit Call<R> instance to Observable<R> in a separate method using RxJava2CallAdapter. Here's the method.

            public static <T> Observable<T> providesCallObservable(T responseType, Call<T> call, Class<?> annotations, Retrofit retrofit){
                RxJava2CallAdapterFactory rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create();
                CallAdapter<T, Observable> callAdapter = (CallAdapter<T, Observable>)rxJava2CallAdapterFactory.get(Observable.class, annotations.getAnnotations(), retrofit);
                return callAdapter.adapt(call);
            }

            This is a generic method. Using type parameters to just restricting to a static method and without applying it to enclosing component is called generic method

            Benefits of doing this.
            1. Loose coupling - Once of the SOLID principles acheived
            2. We can easily test the actual network calls directly using Call<R> service without depending on RxJava
            3. We can RxJava only when required or else we can directly use Call<R> as this also uses efficient background threading to perform network operations
            4. RxJava is dependent on Schedulers and since we are using AndroidSchedulers, we had to do it as an instrumentation test to get real instance of AndroidSchedulers. Now we
                don't need to worry about mocking schedulers and all.
            5. Network Operations are decoupled with

            Testing a Callback which runs on a separate thread is quite impractical because test methods run in a new thread and this thread calls another method
            which has callback which in turn runs on separate thread. Now the current thread doesn't wait for completion of another thread and this terminates immediately
            which makes other thread difficult to communicate callback to the main thread.

            Whenever you copy a normal test to different folders like(test/androidtest) it does not automatically turn into a unit test or a instrumentation test. Sometimes it happens
            automatically and sometimes we have to do it on our own. So it is important to mention the test runner that you want to run with. Instrumentation test is also a test unless and until
            you mention a runner which says it to run as an instrumentation test.

            The test files under androidTest folder are by default run with androidInstrumentationRunner as specified in the gradle file

                testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

            Importance of @serializedName annotation in model files.

            It is very important because json parsing fails even though there is a character change in the parameter name.
            Names play a very important role because serializing and deserializing works using reflection. It searches the parameter with that name
            If the name is not present it will not parse. Once we have specified the parameter names, it is difficult to change them. To avoid all these
            difficulties it is better to put annotations.

            Unit testing has really been helpful. Many bugs were discovered with the help of unit testing
            1. Google places base url correction
            2. Some null pointer exception
            3. Gson parsing error due to incorrect names
            4. Got to know that status code is string and while comparing we have to compare


            CallBack Testing
            We wanted to test the following interface callback which also takes a callback

                        void executeSingleTask(BackgroundNotifier<T, E> backgroundNotifier, Observable<T> observable, Scheduler scheduler);

            Now this is implemented by some class and it returns suitable callback to backgroundNotifier. We want to test how the backgroundNotifier
            behaves when different forms of result are passed i.e null, empty etc. Here our main testing component is backgroundNotifier. So we simulate the call
            executeSingleTask and when it is called we will give callback results to backgroundNotifier like below

                    Mockito.doAnswer(new Answer() {
                        @Override
                        public Object answer(InvocationOnMock invocation) throws Throwable {
                            BackgroundExecutor.BackgroundNotifier backgroundNotifier = (BackgroundExecutor.BackgroundNotifier)invocation.getArguments()[0];
                            Observable currentObservable = (Observable) invocation.getArguments()[1];
                            backgroundNotifier.onTaskResponse(null);
                            backgroundNotifier.onTaskResponse();
                            return null;
                        }
                    }).when(backgroundExecutor).executeSingleTask(backgroundNotifier(argument 0), coffeeShopsObservable(argument 1), scheduler(argument 2));

            We are not concerned with what executeSingleTask does but about the different types of callbacks that we receive, we are only concerned with callbacks
            so we tested like the above.

            Asynchronous callback testing:
            We know the Junit tests run on a separate thread and if there is a asynchronous operation, it is difficult to test callbacks. There is workaround for this issue
            using CountdownLatch. It is basically a countdowntimer that waits for predefined set of seconds before it shuts down. It basically makes current thread before shutdown
            so that the callback is executed in that sufficient time. If callback is executed then our task is finished we inform the timer to shutdown.

            Here's an example for it

                        final CountDownLatch lock = new CountDownLatch(1);

                        coffeeShopsCall.enqueue(new Callback<PlaceSearchResponse>() {

                            @Override
                            public void onResponse(Call<PlaceSearchResponse> call, Response<PlaceSearchResponse> response) {
                                Assert.assertNotNull(response);
                                Assert.assertTrue(response.isSuccessful());
                                Assert.assertTrue(okStatus.equalsIgnoreCase(response.body().getStatus()));
                                lock.countDown();

                            }

                            @Override
                            public void onFailure(Call<PlaceSearchResponse> call, Throwable t) {
                                Assert.assertNotNull(t);
                                Assert.assertEquals(coffeeShopsCall, call);
                                lock.countDown();
                            }
                        });

                        lock.await(20000, TimeUnit.MILLISECONDS);

            This simple example tests the asynchronous operations easily.





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