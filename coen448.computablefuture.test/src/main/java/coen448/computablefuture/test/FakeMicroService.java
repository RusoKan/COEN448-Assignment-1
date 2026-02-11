package coen448.computablefuture.test;

import java.util.concurrent.CompletableFuture;

public class FakeMicroService extends Microservice {
    private final boolean shouldFail;
    

    public FakeMicroService( boolean shouldFail) {
     
        this.shouldFail = shouldFail;
		
    }


    @Override
    public CompletableFuture<String> retrieveAsync(String input) {
        if (shouldFail) {
            CompletableFuture<String> f = new CompletableFuture<>();
            f.completeExceptionally(new RuntimeException("failure"));
            return f;
        }
        return CompletableFuture.completedFuture(input.toUpperCase());
    }}

