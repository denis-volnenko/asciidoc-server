package ru.volnenko.cloud.as;

import org.junit.Assert;
import org.junit.Test;
import ru.volnenko.cloud.as.util.FileUtil;

public class FileUtilTest {

    @Test
    public void test() {
        Assert.assertEquals("index.adoc", FileUtil.prepare("/index.adoc"));
        Assert.assertEquals("index.adoc", FileUtil.prepare("//index.adoc"));
        Assert.assertEquals("index.adoc", FileUtil.prepare("../index.adoc"));
        Assert.assertEquals("index.adoc", FileUtil.prepare("../../index.adoc"));
        Assert.assertEquals("index.adoc", FileUtil.prepare("./index.adoc"));
        Assert.assertEquals("index.adoc", FileUtil.prepare("././index.adoc"));
        Assert.assertEquals("index.adoc", FileUtil.prepare("./../index.adoc"));
        Assert.assertEquals("foo/index.adoc", FileUtil.prepare("foo/../../index.adoc"));
        Assert.assertEquals("foo/index.adoc", FileUtil.prepare("./foo/../../index.adoc"));
    }

}
