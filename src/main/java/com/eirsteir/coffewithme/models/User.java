package com.eirsteir.coffewithme.models;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
