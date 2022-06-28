package com.tomtom.discovery.exceptions;

import static java.util.Objects.isNull;

import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes{

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes =  super.getErrorAttributes(request, options);
        errorAttributes.put("error", null);
        errorAttributes.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        Throwable throwable = getError(request);

        if (isNull(throwable)) {
            errorAttributes.put("error","Internal server error.");
        }
        else if (throwable instanceof WebExchangeBindException){
            var errors = ((WebExchangeBindException) throwable).getBindingResult()
                .getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

            errorAttributes.put("error", errors);
            errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
        }
        else if(throwable instanceof ResponseStatusException){
            errorAttributes.put("error", throwable.getMessage());
        }
        else if(throwable instanceof EntityNotFoundException){
            errorAttributes.put("error", throwable.getMessage());
            errorAttributes.put("status", HttpStatus.NOT_FOUND.value());
        }
        else if(throwable instanceof ValidationException){
            errorAttributes.put("error", throwable.getMessage());
            errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
        }

        else if(throwable instanceof DataAccessException){
            errorAttributes.put("error", "Data access error");
            errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
        }

        else if(throwable instanceof RuntimeException){
            errorAttributes.put("error", throwable.getMessage());
        }
        return  errorAttributes;
    }
}
