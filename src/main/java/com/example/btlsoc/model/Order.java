package com.example.btlsoc.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Order {
    @Id
    @Column(name = "order_id", unique = true)
    private String id;

    private int amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:MM")
    private Date date;
    private String orderDesc;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusOrder statusOrder;

    @ManyToOne
    @JoinColumn(name = "plan_id", referencedColumnName = "plan_id")
//    @JsonIdentityReference(alwaysAsId = true)
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonIdentityReference(alwaysAsId = true)
    private User user;
}
