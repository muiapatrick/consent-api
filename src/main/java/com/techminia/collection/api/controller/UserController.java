package com.techminia.collection.api.controller;


import com.techminia.collection.api.domain.Notification;
import com.techminia.collection.api.domain.PasswordToken;
import com.techminia.collection.api.domain.User;
import com.techminia.collection.api.model.ConfirmResetPwd;
import com.techminia.collection.api.model.CredsAdditionalDetails;
import com.techminia.collection.api.model.PWDModel;
import com.techminia.collection.api.model.UserModel;
import com.techminia.collection.global.GlobalValues;
import com.techminia.collection.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.techminia.collection.api.validation.phonenumber.ValidatePhone.formartedPhoneNumber;
import static com.techminia.collection.api.validation.phonenumber.ValidatePhone.validPhoneNumber;
import static com.techminia.collection.global.GlobalService.*;
import static com.techminia.collection.util.RandomString.randUserPassword;

@RestController
@RequestMapping(value = "/user")
@Api(tags = "User Endpoints", description = "All endpoints for user management", produces = "application/json")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @ApiOperation(value = "Add user account", notes = "Creates new user account")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Internal Server Error") })
    @PostMapping(value = "")
    public ResponseEntity<?> signup(OAuth2Authentication auth, @Valid @RequestBody UserModel model) {
        try {
            //check if the email details already exists
            List<User> usersByEmail = userService.getUsers(new User(model.getEmailAddress(), null), null, PageRequest.of(0, 1, Sort.Direction.DESC, "id")).getContent();
            if (!usersByEmail.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.DUPLICATE_EMAIL.getCode(), "User email already used", null);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            //check if the phone number details already exists
            List<User> usersByPhone = userService.getUsers(new User(null, formartedPhoneNumber(model.getPhoneNumber())), null, PageRequest.of(0, 1, Sort.Direction.DESC, "id")).getContent();
            if (!usersByPhone.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.DUPLICATE_PHONE.getCode(), "User Phone number already used", null);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            String userNumber = new KeyGenerator(6, ThreadLocalRandom.current()).nextString();

            String randomPwd = randUserPassword();
            User user = userService.saveUser(new User(true, true, userNumber.toUpperCase(), model.getEmailAddress(),  model.getEmailAddress(), GlobalValues.passwordEncoder.encode(randomPwd), model.getFirstName(), model.getLastName(),
                    model.getOtherName(), formartedPhoneNumber(model.getPhoneNumber())));

            //create password reset token
            String token = UUID.randomUUID().toString();
            PasswordToken passwordToken = new PasswordToken(new Date(), DateUtils.addDays(new Date(), 1), token, user.getUsername(), false);
            userService.savePasswordToken(passwordToken);

            //log sms notification
            Notification smsNotification = new Notification(NotificationType.SMS.getCode(), NotificationCategory.NEW_USER_ACCOUNT.getCode(), user.getPhoneNumber(), user.getUsername(), user.getFirstName(), randomPwd);
            notificationService.saveNotification(smsNotification);

            //log email notification
            Notification emailNotification = new Notification(NotificationType.EMAIL.getCode(), NotificationCategory.NEW_USER_ACCOUNT.getCode(), user.getPhoneNumber(), user.getUsername(), user.getFirstName(), token);
            notificationService.saveNotification(emailNotification);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "User account Created Successfully", null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Error While Creating User Account", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update user account details", notes = "Update user account")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Internal Server Error") })
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUserDetails(@PathVariable Integer id, @Valid @RequestBody UserModel model) {
        try {
            //check if the user exists
            Page<User> users = userService.getUsers(new User(id), null, PageRequest.of(0, 1, Sort.Direction.DESC, "id"));
            if (users.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Operator does not exist", null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            User user = users.getContent().get(0);

            user.setFirstName(model.getFirstName() == null || model.getFirstName().trim().isEmpty() ? user.getFirstName() : model.getFirstName());
            user.setLastName(model.getLastName() == null || model.getLastName().trim().isEmpty() ? user.getLastName() : model.getLastName());
            user.setOtherName(model.getOtherName() == null || model.getOtherName().trim().isEmpty() ? user.getOtherName() : model.getOtherName());
            user.setEmailAddress(model.getEmailAddress() == null || model.getEmailAddress().trim().isEmpty() ? user.getEmailAddress() : model.getEmailAddress());
            user.setPhoneNumber(model.getPhoneNumber() == null || model.getPhoneNumber().trim().isEmpty() ? user.getPhoneNumber() : formartedPhoneNumber(model.getPhoneNumber()));
            user.setEnabled(model.isEnabled());

            User updatedUser = userService.saveUser(user);


            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "User account details Updated successfully", null, updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Error while updating user details", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get users", notes = "List of users", response = Json.class, responseContainer = "Map", code = 200)
    @GetMapping(value = "")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Internal Server Error") })
    public ResponseEntity<?> getUsers(HttpServletRequest request,
                                      @RequestParam(value = "id", required = false) Integer id,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "user_number", required = false) String userNumber,
                                      @RequestParam(value = "phone_number", required = false) String phoneNumber,
                                      @RequestParam(value = "email_address", required = false) String emailAddress,
                                      @RequestParam(value = "page", required = false) String page,
                                      @RequestParam(value = "page_size", required = false) String pageSize) {
        try {
            List<String> knownParams = Arrays.asList("id", "email_address", "user_number", "name","phone_number", "page", "page_size");
            Enumeration<String> query = request.getParameterNames();
            List<String> list = Collections.list(query);
            list.removeAll(knownParams);
            if (!list.isEmpty()){
                String apiDesc = list.stream().map(x -> "'" +x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            int pageNo = (page != null && Integer.valueOf(page) > 0) ? Integer.valueOf(page) - 1 : 0;
            int pageRequestSize = (pageSize != null) ? Integer.valueOf(pageSize) : Integer.MAX_VALUE;

            PageRequest pages = PageRequest.of(pageNo, pageRequestSize, Sort.Direction.DESC, "id");
            Page<User> users = userService.getUsers(new User(id, userNumber, emailAddress, formartedPhoneNumber(phoneNumber)), name, pages);

            PagenatedJsonResponse response = JsonSetSuccessResponse.setPagenatedResponse(ApiCode.SUCCESS.getCode(), users.isEmpty() ?  "No users found" : "List of users", null, users);
            return new ResponseEntity<>(response, users.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Error while fetching users", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/forgot_password", method= {RequestMethod.POST})
    public ResponseEntity<?> forgotPassword(OAuth2Authentication auth, HttpServletRequest request, @RequestParam(value = "username")  String username) {
        try {
            List<String> knownParams = Arrays.asList("username");
            Enumeration<String> query = request.getParameterNames();
            List<String> list = Collections.list(query);
            list.removeAll(knownParams);
            if (!list.isEmpty()){
                //get all errors
                String apiDesc = list.stream().map(x -> "'" +x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Page<User> users = userService.getUsers(new User(validPhoneNumber(username) ? null : username, validPhoneNumber(username) ? formartedPhoneNumber(username) : null),null, PageRequest.of(0, 1));
            if (users.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Username Not Found", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            User user = users.getContent().get(0);
            String randomPwd = randUserPassword();
            user.setPassword(GlobalValues.passwordEncoder.encode(randomPwd));
            user.setResetPassword(true);

            userService.saveUser(user);

            //send msg to respective source
            CredsAdditionalDetails details = securityService.getAdditionalDetails(auth.getPrincipal().toString());
            //create password reset token
            String token = UUID.randomUUID().toString();
            PasswordToken passwordToken = new PasswordToken(new Date(), DateUtils.addDays(new Date(), 1), token, user.getUsername(), false);
            userService.savePasswordToken(passwordToken);

            //log sms notification
            Notification smsNotification = new Notification(NotificationType.SMS.getCode(), NotificationCategory.PASSWORD_RESET.getCode(), user.getPhoneNumber(), user.getUsername(), user.getFirstName(), randomPwd);
            notificationService.saveNotification(smsNotification);

            //log email notification
            Notification emailNotification = new Notification(NotificationType.EMAIL.getCode(), NotificationCategory.PASSWORD_RESET.getCode(), user.getPhoneNumber(), user.getUsername(), user.getFirstName(), token);
            notificationService.saveNotification(emailNotification);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "Forgot User Password request successfully", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Error while requesting forgot password", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Returns nothing success", notes = "Returns nothing on success")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Internal Server Error") })
    @PostMapping(value = "/reset_password")
    public ResponseEntity<JsonResponse> resetPassword(OAuth2Authentication auth, @Valid @RequestBody PWDModel model) {
        try {
            User user = securityService.getAuthenticatedUser();
            String username = user.getUsername();
            //check the logged in user
            //reset customer password
            user.setResetPassword(false);
            user.setPassword(GlobalValues.passwordEncoder.encode(model.getPassword()));
            userService.saveUser(user);

            securityService.revokeAccessTokenM(auth.getOAuth2Request().getClientId(), username, true);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "User Password Set successfully", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Error while setting user password", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Internal Server Error") })
    @GetMapping(value = "/confirmToken")
    public ResponseEntity<JsonResponse> confirmToken(HttpServletRequest request, @RequestParam(value = "token")  String token) {
        try {
            List<String> knownParams = Arrays.asList("token");
            Enumeration<String> query = request.getParameterNames();
            List<String> list = Collections.list(query);
            list.removeAll(knownParams);
            if (!list.isEmpty()){
                String apiDesc = list.stream().map(x -> "'" +x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<PasswordToken> passwordTokens = userService.getPasswordTokens(new PasswordToken(token, null, false));
            if (passwordTokens.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "The link is invalid or broken!", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            PasswordToken passwordToken = passwordTokens.get(0);
            //check if link has expired
            Calendar cal = Calendar.getInstance();
            if ((passwordToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.FAILED.getCode(), "Reset Token Expired", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "Valid Token", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Error while confirming Password reset token", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Internal Server Error") })
    @PostMapping(value = "/confirm/reset_password")
    public ResponseEntity<?> confirmResetPassword(OAuth2Authentication auth, @Valid @RequestBody ConfirmResetPwd model) {
        try {
            //get the password token
            List<PasswordToken> passwordTokens = userService.getPasswordTokens(new PasswordToken(model.getToken(), null, false));
            if (passwordTokens.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Invalid Password Token Reset", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            PasswordToken passwordToken = passwordTokens.get(0);
            //check if link has expired
            Calendar cal = Calendar.getInstance();
            if ((passwordToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.FAILED.getCode(), "Password Reset Token Expired", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            //set the user new password
            List<User> users = userService.getUsers(new User(validPhoneNumber(passwordToken.getUserEmail()) ? null : passwordToken.getUserEmail(),
                    validPhoneNumber(passwordToken.getUserEmail()) ? formartedPhoneNumber(passwordToken.getUserEmail()) : null),null, PageRequest.of(0, 1, Sort.Direction.DESC, "id")).getContent();
            if (users.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Invalid Password Token Reset", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            User user = users.get(0);
            String username = user.getUsername();
            //check the logged in user
            //reset customer password
            user.setResetPassword(false);
            user.setPassword(GlobalValues.passwordEncoder.encode(model.getPassword()));
            userService.saveUser(user);

            passwordToken.setTokenUsed(true);
            userService.savePasswordToken(passwordToken);

            securityService.revokeAccessTokenM(auth.getOAuth2Request().getClientId(), username, true);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "User Password Set successfully", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Error while resetting Password", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
