package com.simplified.picpay.model.domain.transaction;

import com.simplified.picpay.model.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "transaction")
@Table(name = "transaction")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    @ManyToMany
    @JoinColumn(name = "id_sender")
    private User sender;
    @ManyToMany
    @JoinColumn(name = "id_receiver")
    private User receiver;
    private LocalDateTime time;

}
