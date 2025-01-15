package at.technikum.persistence.dao;

import at.technikum.DAL.DAO.User;
import at.technikum.httpserver.server.Session;
import at.technikum.persistence.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class SessionDao implements Dao<Session> {
    private static Dao<User> userDao = new UserDao();
    //                SELECT id, username, password, token
    @Override
    public Optional<Session> get(int id) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, userid, token
                FROM sessions 
                WHERE id=?
                """)
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() ) {
                return Optional.of(new Session(
                        resultSet.getLong(1),
                        getUser(resultSet.getInt(2)),
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
    public Collection<Session> getAll() {
        ArrayList<Session> result = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT id, userid, token
                FROM sessions 
                """)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                result.add(new Session(
                        resultSet.getLong(1),
                        getUser(resultSet.getInt(2)),
                        resultSet.getString(3 )
                        //resultSet.getString(4)
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    //                (id, username, password, token)
    @Override
    public void save(Session session) {
        Optional<User> userOptional = userDao.getByUsername(session.getUser().getUsername());
        User user = userOptional.get();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                INSERT INTO sessions 
                (userid, token) 
                VALUES (?, ?);
                """ )
        ) {
            //statement.setLong(1, session.getId());
            statement.setLong(1, user.getId());
            statement.setString(2, session.getToken());
            //statement.setString(4, user.getToken());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void saveWithId(Session session) {

    }

    @Override
    public void update(Session session, String[] params) {
        // update the item
        session.setId(Long.parseLong(Objects.requireNonNull( params[0], "ObjectId cannot be null" )));
        session.setUser(Objects.requireNonNull(getUser(Integer.parseInt(params[1])), "Username cannot be null"));
        session.setToken(Objects.requireNonNull(params[2], "Password cannot be null"));
        //user.setToken(Objects.requireNonNull(params[3], "Token cannot be null"));
        // persist the updated item
        //                SET username = ?, password = ?, token = ?
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                UPDATE session 
                SET userid = ?, token = ?
                WHERE id = ?;
                """)
        ) {
            statement.setLong(1, session.getUser().getId());
            statement.setString(2, session.getToken());
            //statement.setString(3, user.getToken());
            statement.setLong(3, session.getId());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Session session) {

    }

    @Override
    public void delete(Session session) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                DELETE FROM sessions 
                WHERE id = ?;
                """)
        ) {
            //statement.setInt( 1, user.getId() );
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<Session> getByUsername(String username) {
        return Optional.empty();
    }

    private static User getUser(int id) {
        Optional<User> user = userDao.get(id);

        return user.orElseGet(
                User::new
        );
    }

}