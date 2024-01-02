package nl.itz_kiwisap_.spigot.customrecipes.utils;

import org.bukkit.inventory.ItemStack;

public final class ArrayUtils {

    private ArrayUtils() {
    }

    public static ItemStack[][] cloneMatrix(ItemStack[][] matrix) {
        ItemStack[][] clonedMatrix = new ItemStack[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            ItemStack[] row = matrix[i];
            if (row == null || row.length == 0) continue;

            for (int j = 0; j < row.length; j++) {
                ItemStack itemStack = row[j];
                if (itemStack == null) continue;

                clonedMatrix[i][j] = itemStack.clone();
            }
        }

        return clonedMatrix;
    }

    public static void updateMatrix(ItemStack[][] matrix, ItemStack[][] clonedMatrix) {
        for (int y = 0; y < clonedMatrix.length; y++) {
            ItemStack[] row = clonedMatrix[y];
            if (row == null || row.length == 0) continue;

            for (int x = 0; x < row.length; x++) {
                ItemStack itemStack = row[x];
                matrix[y][x] = itemStack == null ? null : itemStack.clone();
            }
        }
    }
}