package at.technikum.persistence.dao;

import at.technikum.persistence.DbConnection;
import at.technikum.DAL.DAO.Package;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PackageDao implements Dao<Package> {
    @Override
    public Optional<Package> get(int id) {
        return Optional.empty();
    }

    @Override
    public List<Package> getAll() {
        return List.of();
    }

    @Override
    public void save(Package pkg) {
        boolean success = false;
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO packages
                (id, price) 
                VALUES (?, ?);
                """ )
        ) {
            // Auto-commit must be disabled to handle transactions manually
            DbConnection.getInstance().setAutoCommit(false);

            statement.setString(1, pkg.getId());
            statement.setDouble(2, pkg.getPrice());
            //statement.execute();
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                DbConnection.getInstance().rollback();
            }

            // Insert associated cards using CardDao
            CardDao cardDao = new CardDao();
            success = cardDao.insertCards(pkg.getCards(), pkg.getId()); // Insert cards for this package

            // If cards are successfully inserted, commit the transaction
            if (success) {
                DbConnection.getInstance().commit();
            } else {
                DbConnection.getInstance().rollback();  // Rollback if cards insertion fails
            }
        } catch (SQLException throwables) {
            DbConnection.getInstance().rollback();  // Rollback if there is any error
            throwables.printStackTrace();
        }
    }


    @Override
    public void saveWithId(Package aPackage) {

    }

    @Override
    public void update(Package aPackage, String[] params) {

    }

    @Override
    public void delete(Package aPackage) {

    }

    @Override
    public Optional<Package> getByUsername(String username) {
        return Optional.empty();
    }

    /*
    // Method to insert a Package and its associated Cards
    public boolean insertPackage(Package pkg) throws SQLException {
        String insertPackageSQL = "INSERT INTO packages (id, price) VALUES (?, ?)";

        try (PreparedStatement psPackage = connection.prepareStatement(insertPackageSQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psCard = connection.prepareStatement(insertCardSQL)) {

            // Insert Package
            psPackage.setString(1, pkg.getId());
            psPackage.setInt(2, pkg.getPrice());
            int affectedRows = psPackage.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback();
                return false; // No rows affected, rollback
            }

            // Get generated package id (if auto-generated id is required)
            ResultSet generatedKeys = psPackage.getGeneratedKeys();
            String packageId = null;
            if (generatedKeys.next()) {
                packageId = generatedKeys.getString(1);
            }

            // Insert Cards associated with the Package
            for (Card card : pkg.getCards()) {
                psCard.setString(1, card.getId());
                psCard.setString(2, card.getName());
                psCard.setInt(3, card.getValue());
                psCard.setString(4, packageId); // Foreign key to the Package
                psCard.addBatch();
            }

            // Execute batch insert for cards
            int[] cardInsertCount = psCard.executeBatch();

            // If cards are not inserted correctly, rollback
            for (int count : cardInsertCount) {
                if (count == Statement.EXECUTE_FAILED) {
                    connection.rollback();
                    return false;
                }
            }

            // Commit the transaction if all operations are successful
            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();  // Rollback if there is any error
            throw new SQLException("Error inserting package and cards", e);
        } finally {
            connection.setAutoCommit(true);  // Reset auto-commit mode
        }
    }
    */
}
