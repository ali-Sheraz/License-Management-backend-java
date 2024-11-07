package com.avanza.license.util;

import com.avanza.license.Enum.ErrorCode;

public class ErrorHandlerUtil {
    public static void handleError(ErrorCode errorCode) {
        switch (errorCode) {
            case INVALID_USER_ID:
                throw new CustomApplicationException("Invalid user ID provided.");
            case MODULE_ALREADY_EXISTS:
                throw new CustomApplicationException("Module already exist.");
            case INVALID_APP_ID:
                throw new CustomApplicationException("Invalid APP ID provided.");
            case INVALID_USER_ID_APP_ID:
                throw new CustomApplicationException("Invalid USER ID & APP ID provided.");
            case INVALID_SESSION_ID_APP_ID:
                throw new CustomApplicationException("Invalid Session ID & APP ID provided.");
            case INVALID_SUBSCRIPTION_ID:
                throw new CustomApplicationException("Invalid Subscription ID provided");
            case INVALID_USER_SUBSCRIPTION_ID:
                throw new CustomApplicationException("Invalid USER Subscription ID provided");
            case INVALID_USER_ID_APP_ID_LICENSE_KEY:
                throw new CustomApplicationException("Invalid USER ID & APP ID & Key Value provided");
            case ALREADY_EXIST_USER_ID_APP_ID:
                throw new CustomApplicationException("Alread exist USER ID & APP ID");
            case LICENSE_KEY_EXPIRED:
                throw new CustomApplicationException("License Expired update your subscription plan");
            case INVALID_SESSION_ID:
                throw new CustomApplicationException("Invalid session ID");
            case MAX_USER_REACHED:
                throw new CustomApplicationException("Max user Reached");
            case LICENSE_GENERATION_FAILED:
                throw new CustomApplicationException("License Generation Failed");
            case APPLICATION_SAVE_FAILED:
                throw new CustomApplicationException("Application Save Failed");
            case DUPLICATE_EMAIL:
                throw new CustomApplicationException("Duplicate Email");
            case INVALID_CREDENTIALS:
                throw new CustomApplicationException("Invalid credentials");
            case INVALID_PASSWORD:
                throw new CustomApplicationException("Invalid password");
            case INVALID_MODULE_ID:
                throw new CustomApplicationException("Invalid Module ID");
            default:
                throw new CustomApplicationException("Unknown error code: " + errorCode);
        }
    }
//    public class ErrorHandlerUtil {
//        public static void handleError(ErrorCode errorCode) {
//            switch (errorCode) {
//                case INVALID_USER_ID:
//                    throw new IllegalArgumentException("Invalid user ID provided.");
//                case MODULE_ALREADY_EXISTS:
//                    throw new IllegalArgumentException("Module already exist.");
//                case INVALID_APP_ID:
//                    throw new IllegalArgumentException("Invalid APP ID provided.");
//                case INVALID_USER_ID_APP_ID:
//                    throw new IllegalArgumentException("Invalid USER ID & APP ID provided.");
//                case INVALID_SESSION_ID_APP_ID:
//                    throw new IllegalArgumentException("Invalid Session ID & APP ID provided.");
//                case INVALID_SUBSCRIPTION_ID:
//                    throw new IllegalArgumentException("Invalid Subscription ID provided");
//                case INVALID_USER_SUBSCRIPTION_ID:
//                    throw new IllegalArgumentException("Invalid USER Subscription ID provided");
//                case INVALID_USER_ID_APP_ID_LICENSE_KEY:
//                    throw new IllegalArgumentException("Invalid USER ID & APP ID & Key Value provided");
//                case ALREADY_EXIST_USER_ID_APP_ID:
//                    throw new IllegalArgumentException("Alread exist USER ID & APP ID");
//                case LICENSE_KEY_EXPIRED:
//                    throw new IllegalArgumentException("License Expired update your subscription plan");
//                case INVALID_SESSION_ID:
//                    throw new IllegalArgumentException("Invalid session ID");
//                case MAX_USER_REACHED:
//                    throw new IllegalArgumentException("Max user Reached");
//                case LICENSE_GENERATION_FAILED:
//                    throw new IllegalArgumentException("License Generation Failed");
//                case APPLICATION_SAVE_FAILED:
//                    throw new IllegalArgumentException("Application Save Failed");
//                case DUPLICATE_EMAIL:
//                    throw new IllegalArgumentException("Duplicate Email");
//                case INVALID_CREDENTIALS:
//                    throw new IllegalArgumentException("Invalid credentials");
//                case INVALID_PASSWORD:
//                    throw new IllegalArgumentException("Invalid password");
//                case INVALID_MODULE_ID:
//                    throw new RuntimeException("Invalid Module ID");
//                default:
//                    throw new IllegalArgumentException("Unknown error code: " + errorCode);
//            }
//        }

    // Helper method to extract organization name from subject string
    public static String getOrganizationName(String subject) {
        String[] parts = subject.split(",");
        for (String part : parts) {
            if (part.trim().startsWith("O=")) {
                return part.trim().substring(3); // Extract the organization name
            }
        }
        return ""; // Return an empty string if organization name is not found
    }

    // Helper method to extract common name from subject string
    public static String getCommonName(String subject) {
        String[] parts = subject.split(",");
        for (String part : parts) {
            if (part.trim().startsWith("CN=")) {
                return part.trim().substring(3); // Extract the common name
            }
        }
        return ""; // Return an empty string if common name is not found
    }
}
