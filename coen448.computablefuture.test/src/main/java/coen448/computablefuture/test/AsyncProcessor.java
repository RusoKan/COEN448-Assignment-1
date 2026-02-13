package coen448.computablefuture.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AsyncProcessor {
	
    public CompletableFuture<String> processAsync(List<Microservice> microservices) {
        List<CompletableFuture<String>> futures = microservices.stream()
            .map(client -> client.retrieveAsync("hello"))
            .collect(Collectors.toList());
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" ")));
    }
    
    public CompletableFuture<String> processAsyncFailFast(
            List<Microservice> services,
            List<String> messages) {

        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < services.size(); i++) {
            futures.add( services.get(i).retrieveAsync(messages.get(i)));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v ->
                    futures.stream()
                           .map(CompletableFuture::join)
                           .collect(Collectors.joining(" "))
                );
    };
    public static CompletableFuture<List<String>> processAsyncFailPartial(
            List<Microservice> services,
            List<String> messages
    ) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        
        for (int i = 0; i < services.size(); i++) {
            Microservice service = services.get(i);
            String message = messages.get(i);

            CompletableFuture<String> future = service.retrieveAsync(message)
                    .handle((result, ex) -> {
                        if (ex != null) {
                            return "Failed!";
                        } else {
                            return result;
                        }
                    });

            futures.add(future);
        }

        // Combine all futures into one CompletableFuture<List<String>>
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                                        .map(CompletableFuture::join)
                                        .collect(Collectors.toList()));
    }
    public static CompletableFuture<String> processAsyncFailSoft(
            List<Microservice> services,
            List<String> messages,
            String fallbackValue
    ) {

        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < services.size(); i++) {
            Microservice service = services.get(i);
            String message = messages.get(i);
            //creates a fallback structure to make sure error get replaced
            CompletableFuture<String> future =
                    service.retrieveAsync(message)
                           .exceptionally(ex -> fallbackValue); 

            futures.add(future);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v ->
                        futures.stream()
                               .map(CompletableFuture::join) 
                               .collect(Collectors.joining(" "))
                );
    }
    
    

}
