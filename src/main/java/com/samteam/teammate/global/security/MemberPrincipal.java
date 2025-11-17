package com.samteam.teammate.global.security;

import java.io.Serializable;

public record MemberPrincipal(
    Long id, Long studentId) implements Serializable {

}
