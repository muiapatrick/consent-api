package com.techminia.collection.api.controller;

import com.techminia.collection.api.domain.OauthClientDetails;
import com.techminia.collection.api.model.AppForm;
import com.techminia.collection.api.model.CredsAdditionalDetails;
import com.techminia.collection.api.model.GrantType;
import com.techminia.collection.api.model.KeyGeneratorModel;
import com.techminia.collection.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.validation.Valid;
import java.util.concurrent.ThreadLocalRandom;

import static com.techminia.collection.global.GlobalService.securityService;

@RestController
@Api(tags = "Authentication Token Controller", value = "Config", description = "Endpoints associated with generating the public and private keys for generating access token", produces = "application/json")
public class TokenController {


    @ApiOperation(value = "Returns The Public and Private Keys", httpMethod = "POST", notes = "Returns The Public and Private Keys", response = Json.class, responseContainer = "Map", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    @RequestMapping(value = "/generate", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<?> createApplication(@Valid @RequestBody AppForm appForm) {
        JsonResponse response = null;

        try {

            //DEFINE PROJECT RESOURCE
            KeyGeneratorModel keyGeneratorModel = new KeyGeneratorModel(new KeyGenerator(32, ThreadLocalRandom.current()).nextString(), new KeyGenerator(16, ThreadLocalRandom.current()).nextString(), appForm.getAppName());

            CredsAdditionalDetails additionalDetails = new CredsAdditionalDetails();
            additionalDetails.setClientType(Integer.valueOf(appForm.getClientType()));
            additionalDetails.setSource(Integer.valueOf(appForm.getSource()));

            String grantTypeValues = "";
            for (GrantType g : appForm.getGrantTypes()) {
                System.out.println("Grant Type :: "+g.getGrantType());
                grantTypeValues = grantTypeValues.isEmpty() ? g.getGrantType() :  grantTypeValues + "," + g.getGrantType();
            }

            System.out.println("ADDITION INFO :: "+ ConvertToJson.setJsonString(additionalDetails));

            Long accessTokenValidity = appForm.getAccessTokenValidity() == null ? 3600 : Long.valueOf(appForm.getAccessTokenValidity());
            Long refreshTokenValidity = appForm.getRefreshTokenValidity() == null ? 0 : Long.valueOf(appForm.getRefreshTokenValidity());

            OauthClientDetails oauthClientDetails = new OauthClientDetails(keyGeneratorModel.getPublic_key(), "collection-api", keyGeneratorModel.getPrivate_key(), "read,write", grantTypeValues, null, null, accessTokenValidity, refreshTokenValidity,
                    ConvertToJson.setJsonString(additionalDetails), false, appForm.getAppName());

            securityService.addOauthClientDetails(oauthClientDetails);

            response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "App Created Successful",  null, keyGeneratorModel);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
            response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Error While generating app Keys ", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

