package at.technikum.persistence.dao;

import at.technikum.DAL.DAO.Card;
import at.technikum.DAL.DAO.Deck;
import at.technikum.DAL.DAO.Package;
import at.technikum.httpserver.server.Session;
import at.technikum.persistence.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DeckDao implements Dao<Deck> {
    @Override
    public Optional<Deck> get(int id) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, userid, token
                FROM sessions 
                WHERE id=?
                """)
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() ) {
                return Optional.of(new Deck(
                        resultSet.getLong(1),
                        resultSet.getString( 2 )
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return Optional.empty();
    }

    @Override
    public List<Deck> getAll() {
        return List.of();
    }

    @Override
    public void save(Deck deck) {

    }

    public void saveWithCards(Deck deck, List<Card> cards) throws SQLException {
        boolean success = false;
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO decks
                (name) 
                VALUES (?);
                """ )
        ) {
            // Auto-commit must be disabled to handle transactions manually
            DbConnection.getInstance().setAutoCommit(false);

            statement.setString(1, deck.getName());
            //statement.execute();
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                DbConnection.getInstance().rollback();
            }

            CardDao cardDao = new CardDao();
            for (Card card : cards) {
                if(!cardDao.setDeckId(card, deck)) {
                    success = false;
                    break;
                }
                success = true;
            }

            // If cards are successfully inserted, commit the transaction
            if (success) {
                DbConnection.getInstance().commit();
            } else {
                DbConnection.getInstance().rollback();  // Rollback if cards insertion fails
            }
        } catch (SQLException throwables) {
            DbConnection.getInstance().rollback();  // Rollback if there is any error
            throw new SQLException(throwables);
        }
    }


    @Override
    public void saveWithId(Deck deck) {

    }

    @Override
    public void update(Deck deck, String[] params) {

    }

    @Override
    public void update(Deck deck) {

    }

    @Override
    public void delete(Deck deck) {

    }

    @Override
    public Optional<Deck> getByUsername(String uuid) {
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
