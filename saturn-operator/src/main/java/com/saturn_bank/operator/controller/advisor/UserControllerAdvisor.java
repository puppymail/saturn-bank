package com.saturn_bank.operator.controller.advisor;

import static com.saturn_bank.operator.exception.ExceptionErrorMessages.ENTITY_ALREADY_PRESENT_EX_MSG;
import static com.saturn_bank.operator.exception.ExceptionErrorMessages.NO_SUCH_ENTITY_EX_MSG;
import static org.springframework.http.HttpHeaders.WARNING;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.saturn_bank.operator.exception.DeletedEntityException;
import com.saturn_bank.operator.exception.EntityAlreadyPresentException;
import com.saturn_bank.operator.exception.NoSuchEntityException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice("com.saturn_bank.operator.controller.user")
public class UserControllerAdvisor {

    @ExceptionHandler(EntityAlreadyPresentException.class)
    @ResponseStatus(CONFLICT)
    public final ResponseEntity<String> handleEntityAlreadyPresentEx(EntityAlreadyPresentException eape) {
        return ResponseEntity.status(CONFLICT)
                .header(WARNING, ENTITY_ALREADY_PRESENT_EX_MSG)
                .body(eape.getMessage());
    }

    @ExceptionHandler({NoSuchEntityException.class, DeletedEntityException.class})
    @ResponseStatus(NOT_FOUND)
    public final ResponseEntity<String> handleNoSuchEntityEx(Exception e) {
        return ResponseEntity.status(NOT_FOUND)
                .header(WARNING, NO_SUCH_ENTITY_EX_MSG)
                .body(e.getMessage());
    }

}
