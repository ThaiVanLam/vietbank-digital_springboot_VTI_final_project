package com.vietbank.vietbank_digital.config.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/exception")
    public void exception() {
        throw new RuntimeException("Something went wrong");
    }

    @GetMapping("/method-not-supported")
    public void methodNotSupported() {
        // chỉ GET, nhưng test sẽ gọi POST
    }

    @PostMapping(value = "/media-not-supported", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void mediaNotSupported(@RequestBody String body) {
    }

    @GetMapping("/validation")
    public void validation(@Valid @RequestParam @Size(min = 5) String name) {
    }

    @GetMapping("/arg-type-mismatch")
    public void typeMismatch(@RequestParam int number) {
    }

    @PostMapping("/body-validation")
    public void bodyValidation(@Valid @RequestBody DummyRequest request) {
    }

    @GetMapping("/missing-param")
    public void missingParam(@RequestParam String requiredParam) {
    }

    static class DummyRequest {
        @NotBlank
        private String name;
        // getter/setter
    }
}
