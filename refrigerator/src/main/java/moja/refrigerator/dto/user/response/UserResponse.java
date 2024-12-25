package moja.refrigerator.dto.user.response;

import lombok.Data;
import moja.refrigerator.aggregate.user.User;

import java.util.List;

@Data
public class UserResponse {
    private long userPk;
    private String Id;
    private String userEmail;
    private String userNickName;
    private String userRole;
    private List<UserFollowingResponse> following;


}
