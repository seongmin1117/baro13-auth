package com.baro13.auth.adapter.in.dto.request;

import com.baro13.auth.application.in.command.LoginCommand;

public record LoginRequest(String username, String password) {
    public LoginCommand toCommand() {
        return new LoginCommand(username, password);
    }
}
