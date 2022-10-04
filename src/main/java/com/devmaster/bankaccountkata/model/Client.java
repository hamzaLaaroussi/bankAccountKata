package com.devmaster.bankaccountkata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {

    private long id;
    private String address;
    private String firstName;
    private String lastName;
    private String phone;

}
