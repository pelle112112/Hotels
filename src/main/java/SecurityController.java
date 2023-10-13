

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.*;

import io.javalin.http.Handler;


import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityController implements ISecurity {
    private String dev_secret_key = "AAAAAAAAAAAAABBBBBBBBBBBBCCCCCCCCCCCCCCC";
    UserDAO securityDAO = UserDAO.getInstance();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Handler login() {
        return ctx -> {
            User user = ctx.bodyAsClass(User.class);
            String token = getToken(user.getUsername(), user.getPassword());
            ObjectNode on = mapper.createObjectNode().put("token", token).put("username", user.getUsername());
            ctx.json(on);
        };

    }

    @Override
    public Handler authenticate() {
        // Purpose: before filter -> Check for Authorization header, find user inside the token, forward the ctx object with username on attribute
        ObjectNode returnObject = mapper.createObjectNode();
        return (ctx) -> {
            try {
                String header = ctx.header("Authorization");
                if(header == null)
                    ctx.status(401).json(returnObject.put("msg","Authorization header missing"));
                String token = header.split(" ")[1];
                if (token == null) {
                    ctx.status(401).json(returnObject.put("msg","Authorization header malformed"));
                }
                User verifiedTokenUserEntity = verifyToken(token);
                if (verifiedTokenUserEntity == null) {
                    ctx.status(401).json(returnObject.put("msg","Invalid User or Token"));
                }
                String userName = verifiedTokenUserEntity.getUsername();
                ctx.attribute("userName", userName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Cannot authenticate user");
            }
        };
    }

    private String getToken(String username, String password) throws Exception {

        if (securityDAO.authenticateUser(username, password)){
            User foundUser = securityDAO.getUserByusername(username);
            try {
                return generateToken(foundUser);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Could not create token");
            }
        }
        throw new Exception("Wrong username or password");
    }

    public User verifyToken(String token) throws Exception {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean isDeployed = (System.getenv("DEPLOYED") != null);
            String SECRET = isDeployed ? System.getenv("SECRET_KEY") : dev_secret_key;

            JWSVerifier verifier = new MACVerifier(SECRET);

            if (signedJWT.verify(verifier)) {
                if (new Date().getTime() > signedJWT.getJWTClaimsSet().getExpirationTime().getTime()) {
                    throw new RuntimeException("Your Token is no longer valid");
                }
                System.out.println("Token verified");
                return jwt2user(signedJWT);

            } else {
                throw new JOSEException("UserEntity could not be extracted from token");
            }
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException( "Could not validate token");
        }
    }
    private User jwt2user(SignedJWT jwt) throws ParseException {
        String roles = jwt.getJWTClaimsSet().getClaim("roles").toString();
        String username = jwt.getJWTClaimsSet().getClaim("username").toString();

        Set<Role> rolesSet = Arrays
                .stream(roles.split(","))
                .map(role -> new Role(role))
                .collect(Collectors.toSet());
        return new User(username, rolesSet);
    }

    private String generateToken(User user) throws Exception {

        String ISSUER;
        String TOKEN_EXPIRE_TIME;
        String SECRET_KEY;

        boolean isDeployed = (System.getenv("DEPLOYED") != null);

        if (isDeployed) {
            ISSUER = System.getenv("ISSUER");
            TOKEN_EXPIRE_TIME = System.getenv("TOKEN_EXPIRE_TIME");
            SECRET_KEY = System.getenv("SECRET_KEY");
        } else {
            ISSUER = "UR NAME";
            TOKEN_EXPIRE_TIME = "1800000";
            SECRET_KEY = dev_secret_key;
        }
        try {
            // https://codecurated.com/blog/introduction-to-jwt-jws-jwe-jwa-jwk/

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer(ISSUER)
                    .claim("username", user.getUsername())
                    .claim("roles", user.getRolesAsString())
                    .expirationTime(new Date(new Date().getTime() + Integer.parseInt(TOKEN_EXPIRE_TIME)))
                    .build();
            Payload payload = new Payload(claimsSet.toJSONObject());

            JWSSigner signer = new MACSigner(SECRET_KEY);
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            jwsObject.sign(signer);
            return jwsObject.serialize();

        } catch (JOSEException e) {
            e.printStackTrace();
            throw new Exception("Could not create token");
        }
    }

}