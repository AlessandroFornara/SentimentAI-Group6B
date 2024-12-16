package polimi.aui.sentimentaigroup6b.controller;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            String errorMessage = "An error occurred with status code " + statusCode;

            Throwable exception = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            if (exception != null) {
                errorMessage += ": " + exception.getMessage();
            }

            return ResponseEntity.status(HttpStatus.valueOf(statusCode))
                    .body(errorMessage);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred. Please try again later.");
    }
}
