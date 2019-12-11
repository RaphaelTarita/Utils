package com.tara.util.main;

import com.tara.util.annotation.FieldGET;
import com.tara.util.annotation.FieldSET;
import com.tara.util.annotation.Persistable;

import java.util.ArrayList;
import java.util.List;

@Persistable("TEST_VO")
public class TestVO {
    private String test1;
    private List<Integer> test2;
    private Integer test3;
    private TestSubVO subVO;

    public TestVO() {
        test1 = "";
        test2 = new ArrayList<>();
        test3 = 0;
        subVO = new TestSubVO();
    }

    public TestVO(String test1, List<Integer> test2, Integer test3, TestSubVO subVO) {
        this.test1 = test1;
        this.test2 = test2;
        this.test3 = test3;
        this.subVO = subVO;
    }

    @FieldGET("TEST_1")
    public String getTest1() {
        return test1;
    }

    @FieldSET("TEST_1")
    public void setTest1(String test1) {
        this.test1 = test1;
    }

    @FieldGET("TEST_2")
    public List<Integer> getTest2() {
        return test2;
    }

    @FieldSET("TEST_2")
    public void setTest2(List<Integer> test2) {
        this.test2 = test2;
    }

    @FieldGET("TEST_3")
    public Integer getTest3() {
        return test3 * 2;
    }

    @FieldSET("TEST_3")
    public void setTest3(Integer test3) {
        this.test3 = test3 / 2;
    }

    @FieldGET("SUB_VO")
    public TestSubVO getSubVO() {
        return subVO;
    }

    @FieldSET("SUB_VO")
    public void setSubVO(TestSubVO subVO) {
        this.subVO = subVO;
    }

    @Override
    public String toString() {
        return "TestVO{" +
                "test1='" + test1 + '\'' +
                ", test2=" + test2 +
                ", test3=" + test3 +
                ", subVO=" + subVO +
                '}';
    }
}
