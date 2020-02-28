# Skanders Commons

[![license badge](https://img.shields.io/github/license/alexskanders/Skanders-Commons?logo=apache)](https://github.com/alexskanders/Skanders-Commons/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.skanders.commons/skanders-commons)](https://search.maven.org/search?q=g:%22com.skanders.commons%22%20AND%20a:%22skanders-commons%22)
[![javadoc](https://javadoc.io/badge2/com.skanders.commons/skanders-commons/javadoc.svg)](https://javadoc.io/doc/com.skanders.service/skanders-commons)

Maven:

~~~xml
<dependency>
    <groupId>com.skanders.commons</groupId>
    <artifactId>skanders-commons</artifactId>
    <version>0.8.0</version>
</dependency>
~~~

Gradle:
~~~javascript
implementation 'com.skanders.commons:skanders-commons:0.8.0'
~~~


## Result

The Result class holds possible result code and messages to keep track of the different outcomes of Micro-Service events. It is used as the primary status indicator of a request being handled in RMS and can help manage work flow with the `Resulted` class and quickly return the result of a request. It is recommended to create a class of prebuilt static Results to reference. The class holds four internal values, however only two (`Integer:code, String:message`) are deserialized by Jackson for external use, the other two (`Status:status, Exception:exception`) (`Status` being `javax.ws.rs.core.Response.Status`) are for internal use to be used however the caller sees fit. 

~~~javascript
Result.declare(Integer code, String message)
~~~
- Main static builder, defaults the internal `Status` code to be `Status.OK`.

~~~javascript
Result.declare(Integer code, String message, Status status)
~~~
- Allows the `Status` to be variable.

~~~javascript
Result.declare(Exception exception)
~~~
- Stores the exception internally for the caller to use as needed. The other three values are set to default exception values (`code:-1, message:"Something went wrong", status:INTERNAL_SERVER_ERROR`).

~~~javascript
Result.exception(String message)
~~~
- Similar to `Result.build(Exception exception)` however the Exception type will always be `RmsException` but with the given message.





## Resulted

The Resulted class helps keep track of the state of endpoint request handing. Resulted implements `AutoCloseable` and has a `close()` function to help ensure resource management. Resulted is considered "Not Valid" if any other result value other than `Result.VALID` is contained in the Resulted. The class can be created using three different types.

### Main static creators

~~~javascript
Resulted.inValue(T value)
~~~
- When the request has been completed successfully a Resulted instance should be created from the value and returned to the caller with `inValue()`. 
- Once a value has been loaded into Resulted it cannot be changed or removed from that Resulted instance.
- This builder will internally set the result to be `Result.VALID` and cause `result.withResult()` to be FALSE ensuring the callers request continue to be handled.

~~~javascript
Resulted.inResult(Result result)
~~~
- When the request has been preemptively stopped for a Service related reason, the reason should be created with `Result.declare(int code, String message)` or `Result.declare(int code, String message, Status Response.status)` and a Resulted instance should be created from the result with `inResult()`.
- This builder will cause `result.withResult()` to be TRUE and will alert the caller to handle the Result accordingly.

~~~javascript
Resulted.inException(Exception exception)
~~~
- When the request has been preemptively stopped due to a Exception being raised, a Resulted instance should be created from the exception with `inException()`. This will automatically create an internal Result value with the default Exception traits `code: -1, message: "Something went wrong", status:Status.INTERNAL_SERVER_ERROR` and will be preloaded with the raised exception for internal use. 
- This builder will cause `result.withResult()` to be TRUE and will alert the caller to handle the Exception accordingly.

### Helper static creator

~~~javascript
Resulted.inResulted(Resulted resulted)
~~~
- This is a helper function that allows the Resulted to be passed as a new Resulted<T> type if there is a Non-Valid Result as well as a contradiction between the Return Resulted<T> T type and the current Resulted<T> Type.

#### Example

##### Resulted example

~~~java
public Resulted<Integer> divideNoRemainder(int a, int b)
{
    Result remainderFound = Result.declare(100, "Numbers produce a remainder");
    try {
        if (a % b != 0)
            return Resulted.inResult(remainderFound);
            
        int answer = a / b;
        
        return Resulted.inValue(answer);
        
    } catch (ArithmeticException e) {
        return Resulted.inException(e);
        
    }
}
~~~


### Dependencies
- [Javax WS RS api](https://mvnrepository.com/artifact/javax.ws.rs/javax.ws.rs-api)
- [Jackson Databind](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind)
- [Jackson JAXRS JSON](https://mvnrepository.com/artifact/com.fasterxml.jackson.jaxrs/jackson-jaxrs-json-provider)
- [Jackson Dataformat YAML](https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml)
- [Jackson Dataformat XML](https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-xml)
- [HikariCP](https://mvnrepository.com/artifact/com.zaxxer/HikariCP)
- [Google Guava](https://mvnrepository.com/artifact/com.google.guava/guava)
- [JASYPT](https://mvnrepository.com/artifact/org.jasypt/jasypt)
