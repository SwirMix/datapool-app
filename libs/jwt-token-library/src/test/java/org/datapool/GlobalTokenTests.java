package org.datapool;

import org.datapool.dto.GlobalToken;
import org.datapool.jwt.GlobalTokenService;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GlobalTokenTests {
    private String secret = "1234567890";
    private long days = 3l;
    private GlobalTokenService tokenService;
    private

    @DataProvider(name = "email-provider") Object[][] dpMethod(){
        return new Object[][] {{"ponchick@gmail.com"}, {"ponchick1@gmail.com"}};
    }

    public GlobalToken prepareToken(){
        long startTimestamp = System.currentTimeMillis();
        GlobalToken tokenObject = new GlobalToken()
                .setEmail("ponchick@gmail.com")
                .setUserId("1")
                .setCreateTimestamp(startTimestamp)
                .setEndValidTimestamp(startTimestamp + days * 24 * 3600 * 1000);
        return tokenObject;
    }

    @BeforeTest
    public void prepare(){
        tokenService = new GlobalTokenService(secret);
    }

    @Test (dataProvider = "email-provider")
    public void createTokenTest(String email){
        GlobalToken tokenObject = prepareToken();
        String token = tokenService.createGlobalToken(prepareToken().setEmail(email));
        GlobalToken decryptToken = tokenService.decryptGlobalToken(token);
        System.out.println(decryptToken.toString());
        Assert.assertEquals(decryptToken.equals(tokenObject), true);
    }
}
