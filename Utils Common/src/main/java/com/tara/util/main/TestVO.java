package com.tara.util.main;

import com.tara.util.annotation.persistence.FieldGET;
import com.tara.util.annotation.persistence.FieldSET;
import com.tara.util.annotation.persistence.Persistable;

@Persistable("TEST_VO")
public class TestVO {
    private String test1;
    private String test2;
    private Integer test3;
    private TestSubVO subVO;

    public TestVO(String test1, String test2, Integer test3, TestSubVO subVO) {
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
    public String getTest2() {
        return test2 + " :)";
    }

    @FieldSET("TEST_2")
    public void setTest2(String test2) {
        this.test2 = test2.replace(" :)", "");
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
}
