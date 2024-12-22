package moja.refrigerator.dto.recipe.response;

import lombok.Data;

import java.util.List;


@Data
public class RecipeStepWithImage {
    private int order;
    private String content;
    private List<RecipeSourceInfo> images;
}
