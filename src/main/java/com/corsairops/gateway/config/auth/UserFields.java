package com.corsairops.gateway.config.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFields {
    private String id;
    private String email;
    private String givenName;
    private String familyName;
    private String gender;
    private String role;
}