# mockito-verify
Small extension that provides flixible approach to operate with verifying mocks invocations.

It provides to get delayed verification instead of performing that on exactl place where you define all `verify` options.

## How it works?

You can specify any verification conditionals and options in kind of wrapper that is called `Verify` and using feature of builders
set all options into that object and than after a while you just can call `Verify::verify` method to perform this options into `Mockito.verify`.
It means that `Verify` is a kind of container that can keep all options and than you can execute verification using this container.

## Example

```java
        /** predefine verifying conditions **/
        Verify saveAndNotify = new Verify()
                .checkThat(bar, Mockito.times(1))
                .calledMethod("saveAndNotify", String.class)
                .eq("fooBar");

        /** predefine verifying conditions **/

        /** predefine verifying conditions **/
        foo.getString();

        /** predefine verifying conditions **/
        saveAndNotify.verify();
```

## How to use, API description

There are a few methods that helps you to operate with this extension. First of all you need to create new `Verify`

```java
Verify myVerify = new Verify();
```

After that you need to apply a few methods to set up mock or spy object that you want to verify. Also you need to specify `VerificationMode` from Mockito.

```java
Verify myVerify = new Verify()
      .chechkThat(myMock, Mockito.atLeastOnce());
```

So, after that you need to specify what exactly method you expect to invoke and which arguments this method can operate with (becasue extension uses reflection to invoke method a bit after we need to specify String method name and all arguemnts types)

Use `calledMethod` method to set up method description.

```java
Verify myVerify = new Verify()
      .chechkThat(myMock, Mockito.atLeastOnce())
      .calledMethod("methodName", String.class);
```

Then you can feel free to use matchers that provides you the same interface as Mockito does (`eq(object)` and `any(Class<T>`):

```java
Verify myVerify = new Verify()
      .chechkThat(myMock, Mockito.atLeastOnce())
      .calledMethod("methodName", String.class)
      .any(String.class); //or .eq("exactValue")
```

You can apply six matchers in row. You need to keep order of method arguments sequence because it would be applied step by step. 
Why only six matchers? Because its a first version and there is no available way to implement flexible mechanism endlessly. But I promise that I implement that in furthure versions.


After all of these manipulations you can call `verify` method to check invocations. Whanever you want you can call this method. This is main feature why this extension is developed.
You can set up e.g. in any DataProvider all conditions and then just call one method to check that.

```java
someHandler.callRealMethod(); //real invoke in this line
myVerify.verify();
```

## One more example with DataProvide

Will be soon
