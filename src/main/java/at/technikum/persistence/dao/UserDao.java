package at.technikum.persistence.dao;

import at.technikum.DAL.DAO.User;
import at.technikum.persistence.DbConnection;

import java.sql.*;
import java.util.*;

public class UserDao implements Dao<User> {

    /**
     * initializes the database with its tables
     */
    // PostgreSQL documentation: https://www.postgresqltutorial.com/postgresql-create-table/
    public static void initDb() {
        // re-create the database
        try (Connection connection = DbConnection.getInstance().connect("")) {
            DbConnection.executeSql(connection, "DROP DATABASE swen", false );
            DbConnection.executeSql(connection,  "CREATE DATABASE swen", false );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // create the table
        // PostgreSQL documentation: https://www.postgresqltutorial.com/postgresql-create-table/
        //                    token VARCHAR ( 255 ) NOT NULL,
        try {
            DbConnection.getInstance().executeSql("""
                CREATE TABLE IF NOT EXISTS users (
                    id serial PRIMARY KEY,
                    username VARCHAR ( 255 ) NOT NULL,
                    password VARCHAR (255 ) NOT NULL,
                    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //                SELECT id, username, password, token
    @Override
    public Optional<User> get(int id) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, username, password
                FROM users 
                WHERE id=?
                """)
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() ) {
                return Optional.of(new User(
                        resultSet.getInt(1),
                        resultSet.getString( 2 ),
                        resultSet.getString( 3 )
                        //resultSet.getString( 4 )
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getByUsername(String username) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, username, password
                FROM users 
                WHERE username=?
                """)
        ) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() ) {
                return Optional.of(new User(
                        resultSet.getInt(1),
                        resultSet.getString( 2 ),
                        resultSet.getString( 3 )
                        //resultSet.getString( 4 )
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    //                SELECT id, username, password, token
    @Override
    public List<User> getAll() {
        ArrayList<User> result = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, username, password
                FROM users 
                """)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                result.add(new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3 )
                        //resultSet.getString(4)
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public void saveWithId(User user) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO users 
                (id, username, password) 
                VALUES (?, ?, ?);
                """ )
        ) {
            statement.setLong(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            //statement.setString(4, user.getToken());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //                (id, username, password, token)
    @Override
    public void save(User user) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO users 
                (username, password) 
                VALUES (?, ?);
                """ )
        ) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            //statement.setString(4, user.getToken());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(User user, String[] params) {
        // update the item
        user.setId(Long.parseLong(Objects.requireNonNull( params[0], "ObjectId cannot be null" )));
        user.setUsername(Objects.requireNonNull(params[1], "Username cannot be null"));
        user.setPassword(Objects.requireNonNull(params[2], "Password cannot be null"));
        //user.setToken(Objects.requireNonNull(params[3], "Token cannot be null"));
        // persist the updated item
        //                SET username = ?, password = ?, token = ?
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE users 
                SET username = ?, password = ?
                WHERE id = ?;
                """)
        ) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            //statement.setString(3, user.getToken());
            statement.setLong(4, user.getId());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(User user) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                DELETE FROM users 
                WHERE id = ?;
                """)
        ) {
            statement.setLong( 1, user.getId());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}