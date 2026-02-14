# COEN448-Assignment-1

## Failure Semantics in Concurrent Systems

In this assignment, multiple microservices run in parallel using CompletableFuture. Because execution order is nondeterministic, we cannot assume services complete in any predictable sequence. The problem question becomes:

What should the system do if one of those services fails?

To answer that question, we implemented three different exception-handling policies:

Fail-Fast

Fail-Partial

Fail-Soft

Each policy represents a different method for handling failure.

## 1. Fail-Fast (Atomic Policy)
If any microservice fails, the entire operation fails.
No partial results are returned. The exception is propagated to the caller.

This policy treats the whole concurrent operation as a single atomic unit.

For example, transferring money between two accounts, If one step fails but the other succeeds, the system becomes inconsistent. In this situation, it is safer to fail completely than to return partial success.

The drawback of this policy is that one small failure can cancel the whole operation, not always ideal.

## 2. Fail-Partial (Best-Effort Policy)
Each microservice runs independently.
If one fails, the others can still succeed.
The system returns whatever results are available.

One example could be If one data source fails, the dashboard can still display the remaining data. Users receive incomplete output, but the system remains functional. This can have drawback such as Missing results may not be obvious, users can be assuming completeness and not be aware of missing data.

## 3. Fail-Soft (Fallback Policy)

Failures are replaced with a predefined fallback value.The operation never fails.
Even if a microservice throws an exception, the system substitutes a default value and continues normally.

One example could be if a website misses data, it can return unavailable instead of crashing which can help with user experience. The main drawback is that it can hide serious system problems if not handle properly
