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
    }

   ;

}
