package com.mutualfundtrading.portfolioservice.utils;
import org.apache.commons.codec.binary.Base64;

@SuppressWarnings("ALL")
public class ServiceUtils {
    public ServiceUtils() { }

    public static String decodeJWTForUserId(String jwtToken){

        String[] split_string = jwtToken.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);

        String body = new String(base64Url.decode(base64EncodedBody));
        String userId = null;
        try {
            userId = body.split(",")[0].split(":")[1];
            userId = userId.substring(1, userId.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }
}
