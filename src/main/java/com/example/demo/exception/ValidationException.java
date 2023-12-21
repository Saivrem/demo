package com.example.demo.exception;

import com.example.demo.validation.Violation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ValidationException extends Exception {

    private final List<Violation> violations;
}
