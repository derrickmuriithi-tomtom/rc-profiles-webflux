
package com.tomtom.discovery.exceptions;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class HttpExceptionJsonResponse {
    String message;
    int status;
}