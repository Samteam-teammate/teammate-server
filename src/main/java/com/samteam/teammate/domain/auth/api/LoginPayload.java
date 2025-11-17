package com.samteam.teammate.domain.auth.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginPayload {
    private String id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pw;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPw() { return pw; }
    public void setPw(String pw) { this.pw = pw; }

    @Override public String toString() {
        return "LoginPayload{id=" + (id == null ? "" : id.replaceAll("(?<=.{3}).", "*")) + ", pw=***}";
    }
}