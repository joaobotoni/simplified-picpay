package com.simplified.picpay.model.domain.transaction;

import com.simplified.picpay.model.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "transaction")
@Table(name = "transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "id_sender", nullable = false)
    private User sender;
    @ManyToOne
    @JoinColumn(name = "id_receiver", nullable = false)
    private User receiver;
    private LocalDateTime time;

}
