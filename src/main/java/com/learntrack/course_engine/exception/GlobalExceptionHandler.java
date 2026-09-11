package com.learntrack.course_engine.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            "The course capacity was updated concurrently by another user. Please retry."
        );
        problem.setTitle("Concurrent Modification Conflict");
        problem.setType(URI.create("https://api.learntrack.com/errors/concurrent-conflict"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Invalid Request");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // Handles Resilience4j Rate Limit Exceptions -> HTTP 429
    @ExceptionHandler(RequestNotPermitted.class)
    public ProblemDetail handleRateLimitExceeded(RequestNotPermitted ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.TOO_MANY_REQUESTS, 
            "Rate limit exceeded. Maximum 5 requests allowed per second on enrollment."
        );
        problem.setTitle("Rate Limit Exceeded");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(
        HttpStatus.CONFLICT,
        "A resource with this ID already exists or violates a database constraint."
    );
    problem.setTitle("Duplicate Resource Conflict");
    problem.setProperty("timestamp", Instant.now());
    return problem;
}
}