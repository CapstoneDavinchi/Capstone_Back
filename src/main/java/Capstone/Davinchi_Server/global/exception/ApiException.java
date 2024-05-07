package Capstone.Davinchi_Server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private ApiResponseStatus status; // ApiResponseStatus 객체 매핑
}
