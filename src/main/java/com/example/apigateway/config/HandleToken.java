package com.example.apigateway.config;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

public class HandleToken {

    private static String secretKey="123";
    public static Boolean validateToken(String token){
        String[] chunks = token.split("\\.");
        SignatureAlgorithm sa = HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];
        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);

        if (!validator.isValid(tokenWithoutSignature, signature)) {
            return false;
        }
        return true;
    }
    public static String getRole(String token){
        String[] chunks = token.split("\\.");
        String payload = chunks[1];
        byte[] temp= Base64.getDecoder().decode(payload);
        String data=new String(temp);
        JsonObject jsonpObject=new JsonParser().parse(data).getAsJsonObject();
        return jsonpObject.get("role").getAsString();
    }
}
