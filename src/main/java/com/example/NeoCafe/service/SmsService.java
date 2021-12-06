package com.example.NeoCafe.service;

import com.example.NeoCafe.Enums.ERole;
import com.example.NeoCafe.dto.CodeGeneration;
import com.example.NeoCafe.dto.RegisterClient;
import com.example.NeoCafe.entity.User;
import com.example.NeoCafe.exception.ResourceNotFoundException;
import com.example.NeoCafe.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class
SmsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final String ACCOUNT_SID = "SID";

    private final String AUTH_TOKEN = "токен аккаунта Twilio";

    private final String FROM_NUMBER = "номер";

    public boolean send(RegisterClient client) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            CodeGeneration codeGeneration = new CodeGeneration();

            User users = new User();
            users.setPhoneNumber(client.getPhoneNumber());
            users.setFirstName(client.getFirstName());

            users.setRole(ERole.getRole(4));
            users.setActivationCode(encoder.encode(codeGeneration.getCode() + ""));
            userRepository.save(users);
            String code = codeGeneration.getCode() + " - this is your NeoCafe code.";
            Message message = Message.creator(new PhoneNumber(client.getPhoneNumber()),
                            new PhoneNumber(FROM_NUMBER), code)
                    .create();
            System.out.println("message sent to number - " + client.getPhoneNumber() + " generated code - " + code);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void send_auth(String phone) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        CodeGeneration codeGeneration = new CodeGeneration();

        User users = userRepository.findByPhoneNumber(phone).orElseThrow(
                () -> new ResourceNotFoundException(
                        "пользователь с таким номером не существует!")
        );
        users.setActivationCode(encoder.encode(codeGeneration.getCode() + ""));
        userRepository.save(users);
        String code = codeGeneration.getCode() + " - this is your NeoCafe code.";
        Message message = Message.creator(new PhoneNumber(phone),
                        new PhoneNumber(FROM_NUMBER), code)
                .create();
        System.out.println("message sent to number - " + phone + " generated code - " + code);
    }
}