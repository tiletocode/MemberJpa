package com.kh.memberjpa.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberForm {
    private Integer num;
    @NotEmpty
    private String id;
    @NotEmpty
    private String name;
    private String email;
    @NotEmpty
    private String pass;
    private Boolean newMember;
}
