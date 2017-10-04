package com.akk;/**
 * Created by KHM on 2017/7/25.
 */

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by KHM
 * 2017/7/25 16:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoggerTest {

    //要写当前类，很麻烦
    //private final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test1(){
        String name = "akk";
        String password = "123456";

        log.debug("debug...");
        log.info("name:"+name+"password:"+password);
        log.error("error...");

    }
}
