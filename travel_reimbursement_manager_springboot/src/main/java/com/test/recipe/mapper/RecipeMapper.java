package com.test.recipe.mapper;

import com.test.recipe.model.Recipe;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecipeMapper {
    Recipe selectRecipe(Long id);

    List<Recipe> selectAllRecipes(Recipe recipe);

    int insertRecipe(Recipe recipe);

    int updateRecipe(Recipe recipe);

    int deleteRecipe(Long id);
}
