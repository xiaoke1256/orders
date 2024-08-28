package com.xiaoke_1256.orders.bigdata.product.mapper;

import com.xiaoke_1256.orders.bigdata.product.dto.ProductCondition;
import com.xiaoke_1256.orders.bigdata.product.model.Product;
import java.util.List;

public interface ProductMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product
     *
     * @mbg.generated Wed Aug 28 16:03:57 CST 2024
     */
    int deleteByPrimaryKey(String productCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product
     *
     * @mbg.generated Wed Aug 28 16:03:57 CST 2024
     */
    int insert(Product row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product
     *
     * @mbg.generated Wed Aug 28 16:03:57 CST 2024
     */
    Product selectByPrimaryKey(String productCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product
     *
     * @mbg.generated Wed Aug 28 16:03:57 CST 2024
     */
    List<Product> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product
     *
     * @mbg.generated Wed Aug 28 16:03:57 CST 2024
     */
    int updateByPrimaryKey(Product row);

    List<Product> queryByCondition(ProductCondition condition);
}