package napps.com.nearbycoffee;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by "nithesh" on 7/16/2017.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {


    /*Component methods

    Every type annotated with @Component must contain at least one abstract component method. Component methods may have any name, but must have signatures that conform to either provision or members-injection contracts.

    Provision methods

    Provision methods have no parameters and return an injected or provided type. Each method may have a Qualifier annotation as well. The following are all valid provision method declarations:


    SomeType getSomeType();
    Set<SomeType> getSomeTypes();
    @PortNumber int getPortNumber();

    Provision methods, like typical injection sites, may use Provider or Lazy to more explicitly control provision requests. A Provider allows the user of the component to request provision any number of times by calling Provider.get(). A Lazy will only ever request a single provision, but will defer it until the first call to Lazy.get(). The following provision methods all request provision of the same type, but each implies different semantics:


    SomeType getSomeType();
    Provider<SomeType> getSomeTypeProvider();
    Lazy<SomeType> getLazySomeType();

    Members-injection methods

    Members-injection methods have a single parameter and inject dependencies into each of the Inject-annotated fields and methods of the passed instance. A members-injection method may be void or return its single parameter as a convenience for chaining. The following are all valid members-injection method declarations:


    void injectSomeType(SomeType someType);
    SomeType injectAndReturnSomeType(SomeType someType);

    A method with no parameters that returns a MembersInjector is equivalent to a members injection method. Calling MembersInjector.injectMembers(T) on the returned object will perform the same work as a members injection method. For example:


    MembersInjector<SomeType> getSomeTypeMembersInjector();

    A note about covariance

    While a members-injection method for a type will accept instances of its subtypes, only Inject-annotated members of the parameter type and its supertypes will be injected; members of subtypes will not. For example, given the following types, only a and b will be injected into an instance of Child when it is passed to the members-injection method injectSelf(Self instance):


    class Parent {
        @Inject A a;
    }

    class Self extends Parent {
        @Inject B b;
    }

    class Child extends Self {
        @Inject C c;
    }

    Instantiation

    Component implementations are primarily instantiated via a generated builder. An instance of the builder is obtained using the builder() method on the component implementation. If a nested @Component.Builder type exists in the component, the builder() method will return a generated implementation of that type. If no nested @Component.Builder exists, the returned builder has a method to set each of the modules() and component dependencies() named with the lower camel case version of the module or dependency type. Each component dependency and module without a visible default constructor must be set explicitly, but any module with a default or no-args constructor accessible to the component implementation may be elided. This is an example usage of a component builder:


    public static void main(String[] args) {
        OtherComponent otherComponent = ...;
        MyComponent component = DaggerMyComponent.builder()
                // required because component dependencies must be set
                .otherComponent(otherComponent)
                // required because FlagsModule has constructor parameters
                .flagsModule(new FlagsModule(args))
                // may be elided because a no-args constructor is visible
                .myApplicationModule(new MyApplicationModule())
                .build();
    }

    In the case that a component has no component dependencies and only no-arg modules, the generated component will also have a factory method create(). SomeComponent.create() and SomeComponent.builder().build() are both valid and equivalent.

    Scope

    Each Dagger component can be associated with a scope by annotating it with the scope annotation. The component implementation ensures that there is only one provision of each scoped binding per instance of the component. If the component declares a scope, it may only contain unscoped bindings or bindings of that scope anywhere in the graph. For example:


    @Singleton  @Component
    interface MyApplicationComponent {
        // this component can only inject types using unscoped or  @Singleton bindings
    }

    In order to get the proper behavior associated with a scope annotation, it is the caller's responsibility to instantiate new component instances when appropriate. A Singleton component, for instance, should only be instantiated once per application, while a RequestScoped component should be instantiated once per request. Because components are self-contained implementations, exiting a scope is as simple as dropping all references to the component instance.

    Component relationships

    While there is much utility in isolated components with purely unscoped bindings, many applications will call for multiple components with multiple scopes to interact. Dagger provides two mechanisms for relating components.

    Subcomponents

    The simplest way to relate two components is by declaring a Subcomponent. A subcomponent behaves exactly like a component, but has its implementation generated within a parent component or subcomponent. That relationship allows the subcomponent implementation to inherit the entire binding graph from its parent when it is declared. For that reason, a subcomponent isn't evaluated for completeness until it is associated with a parent.

    Subcomponents are declared by listing the class in the Module.subcomponents() attribute of one of the parent component's modules. This binds the Subcomponent.Builder within the parent component.

    Subcomponents may also be declared via a factory method on a parent component or subcomponent. The method may have any name, but must return the subcomponent. The factory method's parameters may be any number of the subcomponent's modules, but must at least include those without visible no-arg constructors. The following is an example of a factory method that creates a request-scoped subcomponent from a singleton-scoped parent:


    @Singleton  @Component
    interface ApplicationComponent {
        // component methods...

        RequestComponent newRequestComponent(RequestModule requestModule);
    }*/

    //Since this is no more a subcomponent, we won't be using inject directly here instead allowing it to subcomponent to create more abstraction
    //void inject(MainActivity mainActivity)

    //We will be providing the instances that AppModule can provide so that other subcomponents like NetworkComponent can use them
    BaseApplication getBaseApplication();
}
