package napps.com.nearbycoffee.Network;

import java.util.Map;

import io.reactivex.Observable;
import napps.com.nearbycoffee.Model.PlaceSearchResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public interface PlacesAPIInterface {

    /*
    RxJava - Following are the main concepts of RxJava
    1. Observable - The entity that is responsible to do the work. It is named observable, in the sense it can be observed, if it can be observed
                    then it is probably emitting something, If it is emitting something then it is doing something to emit something, The work it is doing
                    is nothing but the asynchronous work it is doing in the background. Although it does a simple task, it is named observable in a confusing way.
                    After they have done with some operation, obviously they will return results. "Returning results" is called "Emitting data" in RxJava terms.

                    In RxJava terms, Observable is always considered as the one which emits a stream of data. It is never viewed as a function that returns results.
                    So it is important for us get into the perspective of data streams and look Observable as the one which emits data stream. Rxjava provides a whole
                    lot of methods to emit an empty object to a big series of callbacks. Here are the list of operators that observable supports to emit data streams in our own
                    required way.

                                1. Observable.just()

                                You can use the .just() operator to convert any object into an Observable. The result Observable will then emit the original object and complete.

                                For example, here we're creating an Observable that'll emit a single string to all its Observers:

                                1
                                Observable<String> observable = Observable.just("Hello World!");
                                2. Observable.from()

                                The .from() operator allows you to convert a collection of objects into an observable stream. You can convert an array into an Observable using Observable.fromArray, a Callable into an Observable using Observable.fromCallable, and an Iterable into an Observable using Observable.fromIterable.

                                3. Observable.range()

                                You can use the .range() operator to emit a range of sequential integers. The first integer you provide is the initial value, and the second is the number of integers you want to emit. For example:

                                1
                                Observable<Integer> observable = Observable.range(0, 5);
                                4. Observable.interval()

                                This operator creates an Observable that emits an infinite sequence of ascending integers, with each emission separated by a time interval chosen by you. For example:

                                1
                                Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                                5. Observable.empty()

                                The empty() operator creates an Observable that emits no items but terminates normally, which can be useful when you need to quickly create an Observable for testing purposes.

                                1
                                Observable<String> observable = Observable.empty();

                    So Observables(more than 1 Observable) emit streams of data i.e return many results. Sometimes it so happens, we have to combine multiple
                    results into one single result just like "Hadoop Map Reduce". In Apache Spark we have functions like map(..), flatmap(..) etc to work on obtained results.
                    Similarly in RxJava we have so many operators like - chaining(work one after the other sequentially), Transforming (multiple to one or bundle), Selectable filtering
                    (Select first, select last), Combining operators etc. Below is the list of Operators by category.


                                Creating Observables
                                Operators that originate new Observables.

                                Create — create an Observable from scratch by calling observer methods programmatically
                                Defer — do not create the Observable until the observer subscribes, and create a fresh Observable for each observer
                                Empty/Never/Throw — create Observables that have very precise and limited behavior
                                From — convert some other object or data structure into an Observable
                                Interval — create an Observable that emits a sequence of integers spaced by a particular time interval
                                Just — convert an object or a set of objects into an Observable that emits that or those objects
                                Range — create an Observable that emits a range of sequential integers
                                Repeat — create an Observable that emits a particular item or sequence of items repeatedly
                                Start — create an Observable that emits the return value of a function
                                Timer — create an Observable that emits a single item after a given delay
                                Transforming Observables
                                Operators that transform items that are emitted by an Observable.

                                Buffer — periodically gather items from an Observable into bundles and emit these bundles rather than emitting the items one at a time
                                FlatMap — transform the items emitted by an Observable into Observables, then flatten the emissions from those into a single Observable
                                GroupBy — divide an Observable into a set of Observables that each emit a different group of items from the original Observable, organized by key
                                Map — transform the items emitted by an Observable by applying a function to each item
                                Scan — apply a function to each item emitted by an Observable, sequentially, and emit each successive value
                                Window — periodically subdivide items from an Observable into Observable windows and emit these windows rather than emitting the items one at a time
                                Filtering Observables
                                Operators that selectively emit items from a source Observable.

                                Debounce — only emit an item from an Observable if a particular timespan has passed without it emitting another item
                                Distinct — suppress duplicate items emitted by an Observable
                                ElementAt — emit only item n emitted by an Observable
                                Filter — emit only those items from an Observable that pass a predicate test
                                First — emit only the first item, or the first item that meets a condition, from an Observable
                                IgnoreElements — do not emit any items from an Observable but mirror its termination notification
                                Last — emit only the last item emitted by an Observable
                                Sample — emit the most recent item emitted by an Observable within periodic time intervals
                                Skip — suppress the first n items emitted by an Observable
                                SkipLast — suppress the last n items emitted by an Observable
                                Take — emit only the first n items emitted by an Observable
                                TakeLast — emit only the last n items emitted by an Observable
                                Combining Observables
                                Operators that work with multiple source Observables to create a single Observable

                                And/Then/When — combine sets of items emitted by two or more Observables by means of Pattern and Plan intermediaries
                                CombineLatest — when an item is emitted by either of two Observables, combine the latest item emitted by each Observable via a specified function and emit items based on the results of this function
                                Join — combine items emitted by two Observables whenever an item from one Observable is emitted during a time window defined according to an item emitted by the other Observable
                                Merge — combine multiple Observables into one by merging their emissions
                                StartWith — emit a specified sequence of items before beginning to emit the items from the source Observable
                                Switch — convert an Observable that emits Observables into a single Observable that emits the items emitted by the most-recently-emitted of those Observables
                                Zip — combine the emissions of multiple Observables together via a specified function and emit single items for each combination based on the results of this function
                                Error Handling Operators
                                Operators that help to recover from error notifications from an Observable

                                Catch — recover from an onError notification by continuing the sequence without error
                                Retry — if a source Observable sends an onError notification, resubscribe to it in the hopes that it will complete without error
                                Observable Utility Operators
                                A toolbox of useful Operators for working with Observables

                                Delay — shift the emissions from an Observable forward in time by a particular amount
                                Do — register an action to take upon a variety of Observable lifecycle events
                                Materialize/Dematerialize — represent both the items emitted and the notifications sent as emitted items, or reverse this process
                                ObserveOn — specify the scheduler on which an observer will observe this Observable
                                Serialize — force an Observable to make serialized calls and to be well-behaved
                                Subscribe — operate upon the emissions and notifications from an Observable
                                SubscribeOn — specify the scheduler an Observable should use when it is subscribed to
                                TimeInterval — convert an Observable that emits items into one that emits indications of the amount of time elapsed between those emissions
                                Timeout — mirror the source Observable, but issue an error notification if a particular period of time elapses without any emitted items
                                Timestamp — attach a timestamp to each item emitted by an Observable
                                Using — create a disposable resource that has the same lifespan as the Observable
                                Conditional and Boolean Operators
                                Operators that evaluate one or more Observables or items emitted by Observables

                                All — determine whether all items emitted by an Observable meet some criteria
                                Amb — given two or more source Observables, emit all of the items from only the first of these Observables to emit an item
                                Contains — determine whether an Observable emits a particular item or not
                                DefaultIfEmpty — emit items from the source Observable, or a default item if the source Observable emits nothing
                                SequenceEqual — determine whether two Observables emit the same sequence of items
                                SkipUntil — discard items emitted by an Observable until a second Observable emits an item
                                SkipWhile — discard items emitted by an Observable until a specified condition becomes false
                                TakeUntil — discard items emitted by an Observable after a second Observable emits an item or terminates
                                TakeWhile — discard items emitted by an Observable after a specified condition becomes false
                                Mathematical and Aggregate Operators
                                Operators that operate on the entire sequence of items emitted by an Observable

                                Average — calculates the average of numbers emitted by an Observable and emits this average
                                Concat — emit the emissions from two or more Observables without interleaving them
                                Count — count the number of items emitted by the source Observable and emit only this value
                                Max — determine, and emit, the maximum-valued item emitted by an Observable
                                Min — determine, and emit, the minimum-valued item emitted by an Observable
                                Reduce — apply a function to each item emitted by an Observable, sequentially, and emit the final value
                                Sum — calculate the sum of numbers emitted by an Observable and emit this sum
                                Backpressure Operators
                                backpressure operators — strategies for coping with Observables that produce items more rapidly than their observers consume them
                                Connectable Observable Operators
                                Specialty Observables that have more precisely-controlled subscription dynamics

                                Connect — instruct a connectable Observable to begin emitting items to its subscribers
                                Publish — convert an ordinary Observable into a connectable Observable
                                RefCount — make a Connectable Observable behave like an ordinary Observable
                                Replay — ensure that all observers see the same sequence of emitted items, even if they subscribe after the Observable has begun emitting items
                                Operators to Convert Observables
                                To — convert an Observable into another object or data structure

    2. Observer - Observer is basically an entity as the name suggests listens for the data emitted by observable or we can say one that receives the results from Observable.
                  This basically receives data through 1 channel and status/signal messages through 3 channels. Here channels are nothing but the callback
                  methods where we receive these data streams/results emitted/returned by Observables.

                  1. Data Channel - OnNext(T t), where t is the data item emitted/the result which is returned
                  2. Status messages/Signals - OnCompleted() -> Signal to the observer that Observable has done emitting all the items
                                               OnError(Throwable e) -> Signal to the observer that some error occurred while processing/emitting data items
                                               OnSubscirbe(Disposable d) -> Signal to the observer that observable has started and might start to emit items at any time
                                                                            Disposable is an Entity which can be used to cancel the sequence at any time

    3. Subscriber/Subscription  -   We have seen how observable emits data stream and observer receives it. Different ways of emitting data streams, different ways of collecting streams etc.
                                    But When does all these actions start? That is where the Subscriber comes into picture.
                                    We subscribe to an observable to receive data items on Observer. It is like we subscribe to News paper company to receive magazines at our homes.
                                    We call that a subscription. There will be one subscription to an observable to receive in an observer. It is like one agreement/commitment. It doesn't mean
                                    that News paper company has only one subscriber. It might have n number of subscribers. Whoever subscribes to it, it has the responsibility of sending magazines
                                    to their homes. Similarly, an observable can have as many subscribers as possible. It will emit data streams to all the observers that have a valid subscription.

    4. Schedulers - Now that we have known how work needs to be started, how data streams are emitted, how they are received etc. So we pretty much covered how?
                    Next we will cover "Where?" part. Where are these things done? Where is the work done? where are the results received? etc. RxJava is known for asynchronous programming.
                    So we can assume most of the things happen in background thread. But to that exception all the above things can happen on a single thread/calling thread/main UI thread in android.
                    But we don't want that to happen. If that happens then there is no use of learning RxJava in first place. So we deliberately want this to happen on other threads/background threads.
                    Before moving into that, there are two main things that happen in RxJava - Computation/Data Emission and Obsorption. RxJa




     */
    @GET("nearbysearch/{responseFormat}")
    Observable<PlaceSearchResponse> getNearbyPlaces(@Path("responseFormat") String responseFormat, @QueryMap Map<String, String> queryParams);
}
