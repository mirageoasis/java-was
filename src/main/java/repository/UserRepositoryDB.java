package repository;

import database.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepositoryDB extends UserRepository{
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryDB.class);
    private static UserRepositoryDB instance;
    private Connection connection;

    private UserRepositoryDB() throws SQLException {
        this.connection = DatabaseConnector.getInstance().getConnection();
    }

    public static UserRepositoryDB getInstance() throws SQLException {
        if (instance == null) {
            instance = new UserRepositoryDB();
        }
        return instance;
    }

    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (user_id, password, name, email) VALUES (?, ?, ?, ?)";
        logger.info("prepared query info: {}", query);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getEmail());
            logger.info("final query info: {}", statement);
            statement.executeUpdate();
        }
    }

    public User getUserById(String userId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ?";
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("user_id"), resultSet.getString("password"),
                    resultSet.getString("name"), resultSet.getString("email"));
            }
        }
        return user;
    }

    public User[] findAll() throws SQLException {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(resultSet.getString("user_id"), resultSet.getString("password"),
                    resultSet.getString("name"), resultSet.getString("email")));
            }
        }
        return users.toArray(new User[0]);
    }

    public void removeUser(String userId) throws SQLException {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            statement.executeUpdate();
        }
    }
}
