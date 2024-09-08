package com.xiaoke_1256.orders.bigdata.aimode.mapper;

import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataCalExecInfo;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataCalExecInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface BigDataCalExecInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int countByExample(BigDataCalExecInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int deleteByExample(BigDataCalExecInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int deleteByPrimaryKey(Long execId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int insert(BigDataCalExecInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int insertSelective(BigDataCalExecInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    List<BigDataCalExecInfo> selectByExampleWithRowbounds(BigDataCalExecInfoExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    List<BigDataCalExecInfo> selectByExample(BigDataCalExecInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    BigDataCalExecInfo selectByPrimaryKey(Long execId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int updateByExampleSelective(@Param("record") BigDataCalExecInfo record, @Param("example") BigDataCalExecInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int updateByExample(@Param("record") BigDataCalExecInfo record, @Param("example") BigDataCalExecInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int updateByPrimaryKeySelective(BigDataCalExecInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table big_data_cal_exec_info
     *
     * @mbggenerated Sun Sep 08 13:52:06 CST 2024
     */
    int updateByPrimaryKey(BigDataCalExecInfo record);
}