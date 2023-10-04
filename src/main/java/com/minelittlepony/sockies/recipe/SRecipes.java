package com.minelittlepony.sockies.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;

public interface SRecipes {
    RecipeSerializer<ShapedRecipe> SHAPED = RecipeSerializer.register("sockies:crafting_shaped", new SockRecipe.Serializer());

    static void bootstrap() {}
}
