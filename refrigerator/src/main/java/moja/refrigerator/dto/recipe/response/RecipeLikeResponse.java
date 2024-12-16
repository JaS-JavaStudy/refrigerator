package moja.refrigerator.dto.recipe.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipeLikeResponse {
    private long likesCount;
    private long dislikesCount;
    private Boolean userReaction;
}
