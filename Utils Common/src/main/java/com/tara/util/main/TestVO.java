package com.tara.util.main;

import com.tara.util.annotation.Field;
import com.tara.util.annotation.Persistable;

import java.util.ArrayList;
import java.util.List;

@Persistable("TEST_VO")
public class TestVO {
    @Field("TEST_1")
    private String test1;

    @Field("TEST_2")
    private List<Integer> test2;

    @Field("TEST_3")
    private Integer test3;

    @Field("SUB_VO")
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
