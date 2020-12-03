package fr.uhcraft.bonusrewards.core;

import fr.uhcraft.bonusrewards.core.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SqlQueries {

    public static double getBonus(UUID uuid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet ranks = null;
        ResultSet bonus = null;

        try {
            connection = DatabaseManager.SERVER.getDataBaseAccess().getConnection();
            preparedStatement = connection.prepareStatement("SELECT parent FROM permissions_inheritance WHERE child = ?");
            preparedStatement.setString(1, uuid.toString());
            ranks = preparedStatement.executeQuery();
            preparedStatement.close();
            double total = 1.0D;

            label144:
            while (true) {
                if (ranks.next()) {
                    preparedStatement = connection.prepareStatement("SELECT BONUS FROM PEXBONUS where PEXGROUP = ?");
                    preparedStatement.setString(1, ranks.getString("parent"));
                    bonus = preparedStatement.executeQuery();
                    preparedStatement.close();

                    while (true) {
                        if (!bonus.next()) {
                            continue label144;
                        }

                        total += bonus.getDouble("BONUS");
                    }
                }

                return total;
            }
        } catch (SQLException var18) {
            var18.printStackTrace();
        } finally {
            try {
                if (bonus != null) {
                    bonus.close();
                }

                if (ranks != null) {
                    ranks.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException var17) {
                var17.printStackTrace();
            }
        }

        return 1.0D;
    }
}
