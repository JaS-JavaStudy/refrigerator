package moja.refrigerator.aggregate.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_follow")
@Data
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_pk")
    private long followPk;

    @ManyToOne
    @JoinColumn(name = "follower")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following")
    private User following;
}
