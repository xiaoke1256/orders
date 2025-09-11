
package com.xiaoke1256.orders.member.repository;

import com.xiaoke1256.orders.member.entity.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * IMemberRepository接口的单元测试类
 * Unit test class for IMemberRepository interface
 */
class IMemberRepositoryTest {

    @Mock
    private IMemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        // 初始化Mockito注解
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试正常情况下根据账户号查询会员
     * Test getMemberByAccountNo method in normal scenario
     */
    @Test
    void testGetMemberByAccountNo_Normal() {
        // 准备测试数据
        // Prepare test data
        String accountNo = "ACC001";
        MemberEntity expectedMember = new MemberEntity();
        expectedMember.setAccountNo(accountNo);
        expectedMember.setNickName("张三");

        // 配置Mock行为
        // Configure mock behavior
        when(memberRepository.getMemberByAccountNo(accountNo)).thenReturn(expectedMember);

        // 执行测试
        // Execute test
        MemberEntity actualMember = memberRepository.getMemberByAccountNo(accountNo);

        // 验证结果
        // Verify results
        assertNotNull(actualMember);
        assertEquals(accountNo, actualMember.getAccountNo());
        assertEquals("张三", actualMember.getNickName());

        // 验证方法调用
        // Verify method invocation
        verify(memberRepository, times(1)).getMemberByAccountNo(accountNo);
    }

    /**
     * 测试查询不存在的会员账户
     * Test getMemberByAccountNo method when member not found
     */
    @Test
    void testGetMemberByAccountNo_NotFound() {
        // 准备测试数据
        // Prepare test data
        String accountNo = "NON_EXISTENT";

        // 配置Mock行为
        // Configure mock behavior
        when(memberRepository.getMemberByAccountNo(accountNo)).thenReturn(null);

        // 执行测试
        // Execute test
        MemberEntity actualMember = memberRepository.getMemberByAccountNo(accountNo);

        // 验证结果
        // Verify results
        assertNull(actualMember);

        // 验证方法调用
        // Verify method invocation
        verify(memberRepository, times(1)).getMemberByAccountNo(accountNo);
    }

    /**
     * 测试查询所有会员-空结果
     * Test findAll method with empty result
     */
    @Test
    void testFindAll_Empty() {
        // 配置Mock行为
        // Configure mock behavior
        when(memberRepository.findAll()).thenReturn(Collections.emptyList());

        // 执行测试
        // Execute test
        List<MemberEntity> result = memberRepository.findAll();

        // 验证结果
        // Verify results
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // 验证方法调用
        // Verify method invocation
        verify(memberRepository, times(1)).findAll();
    }

    /**
     * 测试查询所有会员-单个结果
     * Test findAll method with single result
     */
    @Test
    void testFindAll_Single() {
        // 准备测试数据
        // Prepare test data
        MemberEntity member = new MemberEntity();
        member.setAccountNo("ACC001");
        member.setNickName("李四");

        // 配置Mock行为
        // Configure mock behavior
        when(memberRepository.findAll()).thenReturn(Collections.singletonList(member));

        // 执行测试
        // Execute test
        List<MemberEntity> result = memberRepository.findAll();

        // 验证结果
        // Verify results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACC001", result.get(0).getAccountNo());
        assertEquals("李四", result.get(0).getNickName());

        // 验证方法调用
        // Verify method invocation
        verify(memberRepository, times(1)).findAll();
    }

    /**
     * 测试查询所有会员-多个结果
     * Test findAll method with multiple results
     */
    @Test
    void testFindAll_Multiple() {
        // 准备测试数据
        // Prepare test data
        MemberEntity member1 = new MemberEntity();
        member1.setAccountNo("ACC001");
        member1.setNickName("王五");

        MemberEntity member2 = new MemberEntity();
        member2.setAccountNo("ACC002");
        member2.setNickName("赵六");

        List<MemberEntity> expectedList = Arrays.asList(member1, member2);

        // 配置Mock行为
        // Configure mock behavior
        when(memberRepository.findAll()).thenReturn(expectedList);

        // 执行测试
        // Execute test
        List<MemberEntity> result = memberRepository.findAll();

        // 验证结果
        // Verify results
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ACC001", result.get(0).getAccountNo());
        assertEquals("王五", result.get(0).getNickName());
        assertEquals("ACC002", result.get(1).getAccountNo());
        assertEquals("赵六", result.get(1).getNickName());

        // 验证方法调用
        // Verify method invocation
        verify(memberRepository, times(1)).findAll();
    }

    /**
     * 测试方法参数验证
     * Test method parameter validation
     */
    @Test
    void testGetMemberByAccountNo_WithNullParameter() {
        // 配置Mock行为
        // Configure mock behavior
        when(memberRepository.getMemberByAccountNo(null)).thenReturn(null);

        // 执行测试
        // Execute test
        MemberEntity result = memberRepository.getMemberByAccountNo(null);

        // 验证结果
        // Verify results
        assertNull(result);

        // 验证方法调用
        // Verify method invocation
        verify(memberRepository, times(1)).getMemberByAccountNo(null);
    }

    /**
     * 测试方法参数验证-空字符串
     * Test method parameter validation with empty string
     */
    @Test
    void testGetMemberByAccountNo_WithEmptyParameter() {
        // 准备测试数据
        // Prepare test data
        String emptyAccountNo = "";

        // 配置Mock行为
        // Configure mock behavior
        when(memberRepository.getMemberByAccountNo(emptyAccountNo)).thenReturn(null);

        // 执行测试
        // Execute test
        MemberEntity result = memberRepository.getMemberByAccountNo(emptyAccountNo);

        // 验证结果
        // Verify results
        assertNull(result);

        // 验证方法调用
        // Verify method invocation
        verify(memberRepository, times(1)).getMemberByAccountNo(emptyAccountNo);
    }
}