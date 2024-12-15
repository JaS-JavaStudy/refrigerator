package moja.refrigerator.aggregate.recipe;

import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.user.User;

@Data
@Entity
@Table(name = "tbl_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_pk")
    private long commentPk ;

    @Column(name = "comment_create_time")
    private String commentCreateTime  ;
    @Column(name = "comment_update_time")
    private String commentUpdateTime  ;
    @Column(name = "comment_contents")
    private String commentContents  ;

    @JoinColumn(name = "user")
    @ManyToOne
    private User user ;

    @JoinColumn(name = "recipe")
    @ManyToOne
    private Recipe recipe ;
}
