package com.avanza.license.util;

import com.avanza.license.Enum.ErrorCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorHandlerUtil {
//    public static void handleError(ErrorCode errorCode) {
//        switch (errorCode) {
//            case INVALID_USER_ID:
//                throw new CustomApplicationException("Invalid user ID provided.");
//            case MODULE_ALREADY_EXISTS:
//                throw new CustomApplicationException("Module already exist.");
//            case INVALID_APP_ID:
//                throw new CustomApplicationException("Invalid APP ID provided.");
//            case INVALID_USER_ID_APP_ID:
//                throw new CustomApplicationException("Invalid USER ID & APP ID provided.");
//            case INVALID_SESSION_ID_APP_ID:
//                throw new CustomApplicationException("Invalid Session ID & APP ID provided.");
//            case INVALID_SUBSCRIPTION_ID:
//                throw new CustomApplicationException("Invalid Subscription ID provided");
//            case INVALID_USER_SUBSCRIPTION_ID:
//                throw new CustomApplicationException("Invalid USER Subscription ID provided");
//            case INVALID_USER_ID_APP_ID_LICENSE_KEY:
//                throw new CustomApplicationException("Invalid USER ID & APP ID & Key Value provided");
//            case ALREADY_EXIST_USER_ID_APP_ID:
//                throw new CustomApplicationException("Alread exist USER ID & APP ID");
//            case LICENSE_KEY_EXPIRED:
//                throw new CustomApplicationException("License Expired update your subscription plan");
//            case INVALID_SESSION_ID:
//                throw new CustomApplicationException("Invalid session ID");
//            case MAX_USER_REACHED:
//                throw new CustomApplicationException("Max user Reached");
//            case LICENSE_GENERATION_FAILED:
//                throw new CustomApplicationException("License Generation Failed");
//            case APPLICATION_SAVE_FAILED:
//                throw new CustomApplicationException("Application Save Failed");
//            case DUPLICATE_EMAIL:
//                throw new CustomApplicationException("Duplicate Email");
//            case INVALID_CREDENTIALS:
//                throw new CustomApplicationException("Invalid credentials");
//            case INVALID_PASSWORD:
//                throw new CustomApplicationException("Invalid password");
//            case INVALID_MODULE_ID:
//                throw new CustomApplicationException("Invalid Module ID");
//            default:
//                throw new CustomApplicationException("Unknown error code: " + errorCode);
//        }
//    }
public static void handleError(ErrorCode errorCode) {
    switch (errorCode) {
        case INVALID_USER_ID:
            throw new CustomApplicationException("Invalid user ID provided.",400);
        case MODULE_ALREADY_EXISTS:
            throw new CustomApplicationException("Module already exist.",400);
        case INVALID_APP_ID:
            throw new CustomApplicationException("Invalid APP ID provided.",400);
        case INVALID_USER_ID_APP_ID:
            throw new CustomApplicationException("Invalid USER ID & APP ID provided.",411);
        case INVALID_SESSION_ID_APP_ID:
            throw new CustomApplicationException("Invalid Session ID & APP ID provided.",400);
        case INVALID_SUBSCRIPTION_ID:
            throw new CustomApplicationException("Invalid Subscription ID provided",400);
        case INVALID_USER_SUBSCRIPTION_ID:
            throw new CustomApplicationException("Invalid USER Subscription ID provided",400);
        case INVALID_USER_ID_APP_ID_LICENSE_KEY:
            throw new CustomApplicationException("Invalid USER ID & APP ID & Key Value provided",411);
        case LICENSE_KEY_EXPIRED:
            throw new CustomApplicationException("License Expired update your subscription plan",413);
        case MAX_USER_REACHED:
            throw new CustomApplicationException("Max user Reached",412);
        case ALREADY_EXIST_USER_ID_APP_ID:
            throw new CustomApplicationException("Alread exist USER ID & APP ID",400);
        case INVALID_SESSION_ID:
            throw new CustomApplicationException("Invalid session ID",400);
        case LICENSE_GENERATION_FAILED:
            throw new CustomApplicationException("License Generation Failed",400);
        case APPLICATION_SAVE_FAILED:
            throw new CustomApplicationException("Application Save Failed",400);
        case DUPLICATE_EMAIL:
            throw new CustomApplicationException("Duplicate Email",400);
        case INVALID_CREDENTIALS:
            throw new CustomApplicationException("Invalid credentials",400);
        case INVALID_PASSWORD:
            throw new CustomApplicationException("Invalid password",400);
        case INVALID_MODULE_ID:
            throw new CustomApplicationException("Invalid Module ID",400);
        case DUPLICATE_USERNAME:
            throw new CustomApplicationException("Duplicate username",400);

        default:
            throw new CustomApplicationException("Unknown error code: " + errorCode,500);
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

    /**
     *
     *  Helper method to extract organization name from subject string
     */
    public static String getOrganizationName(String subject) {
        String[] parts = subject.split(",");
        for (String part : parts) {
            if (part.trim().startsWith("O=")) {
                return part.trim().substring(2); // Extract the organization name
            }
        }
        return ""; // Return an empty string if organization name is not found
    }

    /**
     *
     *  Helper method to extract common name from subject string
     */
    public static String getCommonName(String subject) {
        String[] parts = subject.split(",");
        for (String part : parts) {
            if (part.trim().startsWith("CN=")) {
                return part.trim().substring(3); // Extract the common name
            }
        }
        return ""; // Return an empty string if common name is not found
    }
    /**
     *
     *  Helper method to extract application id from subject string
     */
    public static String getApplicationId(String subject) {
        String[] parts = subject.split(",");
        for (String part : parts) {
            part = part.trim(); // Remove any leading or trailing spaces
            if (part.startsWith("OID.1.2.3.4.5.1000=")) {
                return part.substring("OID.1.2.3.4.5.1000=".length()); // Extract the value after the key
            }
        }
        return ""; // Return an empty string if the key is not found
    }
    /**
     *
     *  Helper method to extract user id from subject string
     */
    public static String getUserId(String subject) {
        String[] parts = subject.split(",");
        for (String part : parts) {
            part = part.trim(); // Remove any leading or trailing spaces
            if (part.startsWith("OID.1.2.3.4.5.1001=")) {
                return part.substring("OID.1.2.3.4.5.1001=".length()); // Extract the value after the key
            }
        }
        return ""; // Return an empty string if the key is not found
    }
    /**
     *
     *  Helper method to extract max user from subject string
     */

    public static int getMaxUser(String subject) {
        String[] parts = subject.split(",");
        for (String part : parts) {
            part = part.trim(); // Remove any leading or trailing spaces
            if (part.startsWith("OID.1.2.3.4.5.1003=")) {
                String maxUsersStr = part.substring("OID.1.2.3.4.5.1003=".length()); // Extract the value after the key
                return Integer.parseInt(maxUsersStr); // Convert to integer
            }
        }
        return 0; // Return a default value (e.g., 0) if the key is not found
    }
    /**
     *
     *  Helper method to extract module list from subject string
     */
    public static List<String> getModulesList(String subject) {
        // Regular expression to match key-value pairs, including quoted values
        Pattern pattern = Pattern.compile("([^=,]+)=(\"[^\"]*\"|[^,]*)");
        Matcher matcher = pattern.matcher(subject);

        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();

            // Check for the specific key "OID.1.2.3.4.5.1002"
            if ("OID.1.2.3.4.5.1002".equals(key)) {
                // Remove quotes from the value and split into a list
                value = value.replaceAll("^\"|\"$", ""); // Remove enclosing quotes
                return Arrays.asList(value.split(",")); // Split the value into individual modules
            }
        }

        return new ArrayList<>(); // Return an empty list if the key is not found
    }
    /**
     *
     *  Helper method to extract licnense key from subject string
     */
    public static String getLicneseKey(String subject) {
        String[] parts = subject.split(",");
        for (String part : parts) {
            part = part.trim(); // Remove any leading or trailing spaces
            if (part.startsWith("OID.1.2.3.4.5.1004=")) {
                return part.substring("OID.1.2.3.4.5.1004=".length()); // Extract the value after the key
            }
        }
        return ""; // Return an empty string if the key is not found
    }

}
