package org.yearup.data.mysql;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }
    @Override
    public List<Category> getAllCategories()
    {
        List<Category> category = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM categories")) {

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Category category1 = new Category(
                            resultSet.getInt("category_id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"));
                    category.add(category1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
    @Override
    public Category getById(int categoryId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM categories WHERE category_id = ?")) {
                preparedStatement.setInt(1, (categoryId));
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapRow(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }
    @Override
    public Category create(Category category) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO categories (name, description) Values (?, ?)")) {
                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0){
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                        if (generatedKeys.next()){
                            category.setCategoryId(generatedKeys.getInt(1));
                            return category;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return category;
    }
    @Override
    public void update(int categoryId, Category category)
    {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE categories SET name = ?, description = ?, WHERE category_id = ?")){
                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                preparedStatement.setInt(3, category.getCategoryId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delete(int categoryId)
    {
        try (Connection connection = dataSource.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM categories WHERE category_id = ?")){
                preparedStatement.setInt(0, categoryId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");
        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};
        return category;
    }
}