package com.xiaoke_1256.orders.bigdata.aimode.mapper;

import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataClusterDefined;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataClusterDefinedExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface BigDataClusterDefinedMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int countByExample(BigDataClusterDefinedExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int deleteByExample(BigDataClusterDefinedExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int deleteByPrimaryKey(Long clusterId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int insert(BigDataClusterDefined record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int insertSelective(BigDataClusterDefined record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    List<BigDataClusterDefined> selectByExampleWithRowbounds(BigDataClusterDefinedExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    List<BigDataClusterDefined> selectByExample(BigDataClusterDefinedExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    BigDataClusterDefined selectByPrimaryKey(Long clusterId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int updateByExampleSelective(@Param("record") BigDataClusterDefined record, @Param("example") BigDataClusterDefinedExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int updateByExample(@Param("record") BigDataClusterDefined record, @Param("example") BigDataClusterDefinedExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int updateByPrimaryKeySelective(BigDataClusterDefined record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cluster_defined
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int updateByPrimaryKey(BigDataClusterDefined record);
}