package com.example.demo.exception;

import com.example.demo.validation.Violation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ValidationException extends Exception {

    @Getter
    private final List<Violation> violations;
}
