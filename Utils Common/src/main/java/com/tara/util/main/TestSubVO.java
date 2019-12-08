package com.tara.util.main;

import com.tara.util.annotation.FieldGET;
import com.tara.util.annotation.FieldSET;
import com.tara.util.annotation.Persistable;

@Persistable("TEST_SUB_VO")
public class TestSubVO {
    private String test1;
    private String test2;

    public TestSubVO() {
        test1 = "Hello";
        test2 = "World";
    }

    @FieldGET("SUBTEST_1")
    public String getTest1() {
        return test1;
    }

    @FieldSET("SUBTEST_1")
    public void setTest1(String test1) {
        this.test1 = test1;
    }

    @FieldGET("SUBTEST_2")
    public String getTest2() {
        return test2;
    }

    @FieldSET("SUBTEST_2")
    public void setTest2(String test2) {
        this.test2 = test2;
    }
}
