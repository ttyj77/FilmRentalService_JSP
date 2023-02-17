package controller;

import dbConn.ConnectionMaker;
import model.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    private Connection connection;

    public UserController(ConnectionMaker connectionMaker) {
        this.connection = connectionMaker.makeConnection();
    }

    public boolean insert(UserDTO userDTO) {
        String query = "insert into user (username, password, nickname) values (?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, userDTO.getUsername());
            pstmt.setString(2, userDTO.getPassword());
            pstmt.setString(3, userDTO.getNickname());

            pstmt.executeUpdate();

            pstmt.close();

        } catch (SQLException e) {
            System.out.println("중복된 아이디로 가입할 수 없습니다.");
            return false;
        }
        return true;
    }


    public UserDTO auth(String username, String password) {
        String query = "select * from user where username = ? and password = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(resultSet.getInt("id"));
                userDTO.setUsername(resultSet.getString("username"));
                userDTO.setNickname(resultSet.getString("nickname"));
//                userDTO.setPassword(resultSet.getString("password")); // 보안상으로 매우 취약하다

                return userDTO;
            }

            resultSet.close();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(UserDTO userDTO) {
        String query = "update user set password = ?, nickname = ? where id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, userDTO.getPassword());
            pstmt.setString(2, userDTO.getNickname());
            pstmt.setInt(3, userDTO.getId());

            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(int id) {
        String query = "delete from user where id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public UserDTO selectOne(int id) {
        UserDTO u = null;
        String query = "select * from user where id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                u = new UserDTO();
                u.setId(resultSet.getInt("id"));
                u.setNickname(resultSet.getString("nickname"));
                return u;
            }
            resultSet.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }
}