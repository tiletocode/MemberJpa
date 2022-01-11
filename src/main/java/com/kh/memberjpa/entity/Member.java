package com.kh.memberjpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer num;

    @Column
    @NotEmpty
    private String id;

    @Column
    @NotEmpty
    private String name;

    @Column
    private String email;

    @Column
    @NotEmpty
    private String pass;
}
