package moja.refrigerator.dto.recipe.request;

import lombok.Data;

@Data
public class RecipeLikeRequest {
    private Long recipePk;
    private Long userPk;
    private Boolean likeStatus;
}
