package com.minelittlepony.sockies.recipe;

import net.minecraft.recipe.RecipeSerializer;

public interface SRecipes {
    RecipeSerializer<SockRecipe> SHAPED = RecipeSerializer.register("sockies:crafting_shaped", new SockRecipe.Serializer());

    static void bootstrap() {}
}
