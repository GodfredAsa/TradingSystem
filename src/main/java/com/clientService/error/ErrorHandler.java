//package com.clientService.error;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.error.ErrorAttributeOptions;
//import org.springframework.boot.web.servlet.error.ErrorAttributes;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController

//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ExceptionHandler({Exception.class})
//public class ErrorHandler implements ErrorController {
//
//    @Autowired
//    private ErrorAttributes errorAttributes;
//
//
//    /**
//     * Misty's Custom Validation Errors and Exception handler
//     * for a more presentable and uniform response to
//     * the frontend team :)
//     *
//     * @param webRequest
//     * @return
//     */
//    @RequestMapping("/error")
//    public ApiError handleError(WebRequest webRequest) {
//
//        Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.BINDING_ERRORS));
//        String message = (String) attributes.get("message");
//        String path = (String) attributes.get("path");
//        int status = (Integer) attributes.get("status");
//        ApiError error = new ApiError(status, message, path);
//
//        if (attributes.containsKey("errors")) {
//            List<FieldError> fieldErrors = (List<FieldError>) attributes.get("errors");
//            Map<String, String> validationErrors = new HashMap<>();
//            fieldErrors.forEach((fieldError) -> {
//                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
//            });
//            error.setValidationErrors(validationErrors);
//        }
//
//        return error;
//    }
//
//}
=