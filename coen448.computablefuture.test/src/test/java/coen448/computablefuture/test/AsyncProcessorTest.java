package coen448.computablefuture.test;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.Arrays;
public class AsyncProcessorTest {
    @Test
    public void testProcessAsyncSuccess() throws ExecutionException, InterruptedException {
        Microservice mockService1 = mock(Microservice.class);
        Microservice mockService2 = mock(Microservice.class);
        when(mockService1.retrieveAsync(any())).thenReturn(CompletableFuture.completedFuture("Hello"));
        when(mockService2.retrieveAsync(any())).thenReturn(CompletableFuture.completedFuture("World"));

        AsyncProcessor processor = new AsyncProcessor();
      CompletableFuture<String> resultFuture = processor.processAsync(List.of(mockService1, mockService2));
//        CompletableFuture<String> resultFuture = processor.processAsync(Arrays.asList(mockService1, mockService2));
        CompletableFuture<String> resultFuture1 = processor.processAsyncFailFast(
                Arrays.asList(mockService1, mockService2),
                Arrays.asList("msg1", "msg2")
            );
        String result = resultFuture1.get();
        assertEquals("Hello World", result);
    }
   // ASSIGNMENT 1 3.2
//Checking for Failure propagation for Fail Fast 
    @Test
    public void failFast_failure_propagates() {
     	//fail ( shouldfail boolean= true)
        Microservice s1 = new FakeMicroService(true);
        //pass ( shouldfail boolean= false)
        Microservice s2 = new FakeMicroService(false);
        
        AsyncProcessor processor = new AsyncProcessor();

        CompletableFuture<String> result = processor.processAsyncFailFast(
                Arrays.asList(s1, s2),
                Arrays.asList("message1", "message2")
        );
        //No Partial result can be returned, result returns error when one fails with a timeout of 2 seconds
        assertThrows(ExecutionException.class, () -> result.get(2, TimeUnit.SECONDS));
        assertTrue(result.isCompletedExceptionally());
        

      
}
  //Checking that Test succesfully
    @Test
    public void failFast_all_success() throws Exception {
    	 //pass ( shouldfail boolean= false)
        Microservice s1 = new FakeMicroService(false);
        Microservice s2 = new FakeMicroService(false);

        AsyncProcessor processor = new AsyncProcessor();

        CompletableFuture<String> result = processor.processAsyncFailFast(
                Arrays.asList(s1, s2),
                Arrays.asList("hello", "world")
        );

        assertEquals("HELLO WORLD", result.get(2, TimeUnit.SECONDS));
    }
    
    
    
    

}


//ADD 2 tests for each Async Processor Types