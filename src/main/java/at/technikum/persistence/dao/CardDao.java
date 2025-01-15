package at.technikum.persistence.dao;

import at.technikum.DAL.DAO.Card;
import at.technikum.DAL.DAO.Deck;
import at.technikum.persistence.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CardDao implements Dao<Card> {
    

    //                SELECT id, cardname, password, token
    @Override
    public Optional<Card> get(int id) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, name, damage
                FROM cards 
                WHERE id=?
                """)
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() ) {
                return Optional.of(new Card(
                        resultSet.getString(1),
                        resultSet.getString( 2 ),
                        resultSet.getDouble( 3 )
                        //resultSet.getString( 4 )
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Card> getByUsername(String cardname) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, name, damage
                FROM cards 
                WHERE cardname=?
                """)
        ) {
            statement.setString(1, cardname);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() ) {
                return Optional.of(new Card(
                        resultSet.getString(1),
                        resultSet.getString( 2 ),
                        resultSet.getDouble( 3 )
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    //                SELECT id, cardname, password, token
    @Override
    public List<Card> getAll() {
        ArrayList<Card> result = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, name, damage
                FROM cards 
                """)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                result.add(new Card(
                        resultSet.getString(1),
                        resultSet.getString( 2 ),
                        resultSet.getDouble( 3 )
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public void saveWithId(Card card) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO cards 
                (id, name, damage) 
                VALUES (?, ?, ?);
                """ )
        ) {
            statement.setString(1, card.getId());
            statement.setString(2, card.getName());
            statement.setDouble(3, card.getDamage());
            //statement.setString(4, card.getToken());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //                (id, cardname, password, token)
    @Override
    public void save(Card card) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO cards 
                (name, damage) 
                VALUES (?, ?);
                """ )
        ) {
            statement.setString(1, card.getName());
            statement.setDouble(2, card.getDamage());
            //statement.setString(4, card.getToken());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Card card, String[] params) {
        // update the item
        card.setId(Objects.requireNonNull( params[0], "ObjectId cannot be null" ));
        card.setName(Objects.requireNonNull(params[1], "cardname cannot be null"));
        card.setDamage(Double.parseDouble(Objects.requireNonNull(params[2], "Password cannot be null")));
        //card.setToken(Objects.requireNonNull(params[3], "Token cannot be null"));
        // persist the updated item
        //                SET cardname = ?, password = ?, token = ?
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE cards
                SET cardname = ?, password = ?
                WHERE id = ?;
                """)
        ) {
            statement.setString(1, card.getId());
            statement.setString(2, card.getName());
            //statement.setString(3, card.getToken());
            statement.setDouble(4, card.getDamage());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Card card) {

    }

    @Override
    public void delete(Card card) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                DELETE FROM cards 
                WHERE id = ?;
                """)
        ) {
            statement.setString( 1, card.getId());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Method to insert a single Card into the database
    public boolean insertCard(Card card, String packageId) throws SQLException {
        String insertCardSQL = "INSERT INTO cards (id, name, damage, package_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = DbConnection.getInstance().prepareStatement(insertCardSQL)) {
            ps.setString(1, card.getId());
            ps.setString(2, card.getName());
            ps.setDouble(3, card.getDamage());
            ps.setString(4, packageId); // Foreign key to the Package
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }
    }

    // Method to insert a list of Cards into the database
    public boolean insertCards(List<Card> cards, String packageId) throws SQLException {
        String insertCardSQL = "INSERT INTO cards (id, name, damage, package_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = DbConnection.getInstance().prepareStatement(insertCardSQL)) {
            for (Card card : cards) {
                ps.setString(1, card.getId());
                ps.setString(2, card.getName());
                ps.setDouble(3, card.getDamage());
                ps.setString(4, packageId); // Foreign key to the Package
                ps.addBatch();
            }

            int[] result = ps.executeBatch();
            for (int r : result) {
                if (r == Statement.EXECUTE_FAILED) {
                    return false; // If any batch fails, return false
                }
            }

            return true;
        }
    }

    public boolean setDeckId(Card card, Deck deck) throws SQLException {
        String updateQuery = "UPDATE cards SET deck_id = ? WHERE id = ?";

        try (PreparedStatement stmt = DbConnection.getInstance().prepareStatement(updateQuery)) {

            // Set the parameters for the query
            stmt.setLong(1, deck.getId());  // Set the new deck_id
            stmt.setString(2, card.getId());   // Set the card_id to identify which card to update

            // Execute the update statement
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Deck ID(s) updated successfully!");
                return true;
            } else {
                System.out.println("No card found with the specified ID.");
            }

        }
        return false;
    }

    // Method to retrieve all cards associated with a package ID
    public List<Card> getCardsByPackageId(String packageId) throws SQLException {
        String selectSQL = "SELECT * FROM cards WHERE package_id = ?";
        List<Card> cards = new ArrayList<>();

        try (PreparedStatement ps = DbConnection.getInstance().prepareStatement(selectSQL)) {
            ps.setString(1, packageId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Card card = new Card(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("damage")
                );
                cards.add(card);
            }
        }

        return cards;
    }
}