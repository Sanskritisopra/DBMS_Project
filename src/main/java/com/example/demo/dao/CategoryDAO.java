package com.example.demo.dao;

import com.example.demo.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Repository
public class CategoryDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // List all categories
    public List<Category> listCategories() {
        String sql = "SELECT * FROM Category";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    // Get a category by ID
    public Category getCategoryById(Integer id) {
        String sql = "SELECT * FROM Category WHERE CatID = ?";
        try {
            return jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(Category.class),
                id
            );
        } catch (EmptyResultDataAccessException e) {
            // Handle case where no result is found
            System.out.println("No Category found with ID: " + id);
            return null; // or throw a custom exception
        } catch (DataAccessException e) {
            // Handle any other data access exceptions
            System.out.println("Error retrieving Category: " + e.getMessage());
            return null; // or throw a custom exception
        }
    }

    // Save a new category
    public void saveCategory(Category category) {
        String sql = "INSERT INTO Category (CatID, CaseType) VALUES (?, ?)";
        jdbcTemplate.update(sql, category.getCatID(), category.getCaseType()); // Adjust as per your Category entity
    }

    // Update an existing category
    public void updateCategory(Category category) {
        String sql = "UPDATE Category SET CaseType = ? WHERE CatID = ?";
        jdbcTemplate.update(sql, category.getCaseType(), category.getCatID());
    }

    // Delete a category
    public void deleteCategory(Integer catID) {
        String sql = "DELETE FROM Category WHERE CatID = ?";
        jdbcTemplate.update(sql, catID);
    }

    
    public int getCatIdByCaseType(String caseType) {
        String sql = "SELECT CatID FROM Category WHERE CaseType = ?";
        
        // Prepare to set the parameters using PreparedStatementSetter
        List<Integer> catIds = jdbcTemplate.query(
            sql,
            (PreparedStatementSetter) ps -> ps.setString(1, caseType), // Set the caseType parameter
            (rs, rowNum) -> rs.getInt("CatID") // RowMapper to extract CatID
        );
    
        if (catIds.isEmpty()) {
            // Handle the case where no result is found
            System.out.println("No Category found with caseType: " + caseType);
            return -1; // or handle as needed
        } else {
            return catIds.get(0); // Return the first (and should be only) CatID
        }
    }
    
}