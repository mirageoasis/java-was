package repository;

import java.sql.SQLException;
import model.User;

public abstract class UserRepository {
    abstract User[] findAll() throws SQLException;
    public abstract void addUser(User user) throws SQLException;
    abstract void removeUser(String userId) throws SQLException;
    public abstract User getUserById(String userId) throws SQLException;
    static UserRepository getInstance() throws SQLException {
        return null;
    }
}
